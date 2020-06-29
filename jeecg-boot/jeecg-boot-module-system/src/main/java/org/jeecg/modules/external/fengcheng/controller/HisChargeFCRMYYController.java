package org.jeecg.modules.external.fengcheng.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.constant.PdConstant;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.external.fengcheng.entity.HisChargeFCRMYY;
import org.jeecg.modules.external.fengcheng.entity.HisChargeFCZYY;
import org.jeecg.modules.external.fengcheng.util.HisApiForFCRenminUtils;
import org.jeecg.modules.pd.entity.HisChargeInf;
import org.jeecg.modules.pd.service.IHisChargeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
* @Description: HIS系统收费项目基础信息
* @Author: jeecg-boot
* @Date:   2020-02-11
* @Version: V1.0
*/
@RestController
@RequestMapping("/pd/hisChargeFCRMYY")
@Slf4j
public class HisChargeFCRMYYController {

    @Autowired
    private IHisChargeService hisChargeService;


    @GetMapping(value = "/hisProductList")
    public Result<?> hisProductList(HisChargeInf inf,
                                    @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
                                    @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,HttpServletRequest req) {
        Page<HisChargeInf> page = new Page<HisChargeInf>(pageNo, pageSize);

        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        inf.setDepartParentId(sysUser.getDepartParentId());

        IPage<HisChargeInf> pageList = hisChargeService.selectList(page, inf);
        return Result.ok(pageList);
    }

    /**
     * 查询his打包项目列表
     * @param inf
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @GetMapping(value = "/hisPackageList")
    public Result<?> hisPackageList(HisChargeInf inf,
                                    @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
                                    @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,HttpServletRequest req) {
        Page<HisChargeInf> page = new Page<HisChargeInf>(pageNo, pageSize);

        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        inf.setDepartParentId(sysUser.getDepartParentId());
        inf.setFsfSpec("打包");
        IPage<HisChargeInf> pageList = hisChargeService.selectList(page, inf);
        return Result.ok(pageList);
    }

    /**
     * 查询HIS收费代码列表
     * @param hisCharge
     * @param req
     * @return
     */
    @GetMapping(value = "/hisChargeCodeList")
    public Result<?> hisChargeCodeList(HisChargeFCRMYY hisCharge, HttpServletRequest req) {

        if(hisCharge == null){
            return Result.error("请输入查询条件查询！");
        }
        if(oConvertUtils.isEmpty(hisCharge.getChargeCode()) && oConvertUtils.isEmpty(hisCharge.getProName())){
            return Result.error("请输入查询条件查询！");
        }

        JSONObject chargeCodeJson = HisApiForFCRenminUtils.queryHisChargeCode(hisCharge);
        if(chargeCodeJson == null){
            return Result.error("未查询到数据！");
        }
        if(!PdConstant.SUCCESS_0.equals(chargeCodeJson.getString("statusCode"))){
            return Result.error("查询HIS项目收费代码失败，请重新查询或联系管理员！HIS返回："+chargeCodeJson.getString("msg"));
        }

        try{
            JSONArray jsonArray = chargeCodeJson.getJSONArray("data");
            // his返回报文最后会带一条空数据，这边把空数据移除
            for (int i = 0; i < jsonArray.size(); i++){
                if(oConvertUtils.isEmpty(jsonArray.getJSONObject(i).getString("chargeCode"))){
                    jsonArray.remove(i);
                }
            }
            List<HisChargeFCRMYY> hisChargeCodeList = JSONArray.parseArray(jsonArray.toJSONString(),HisChargeFCRMYY.class);
            return Result.ok(hisChargeCodeList);
        }catch (Exception e){
            //his不到数据，会返回一个空JSONObject，不会返回一个空数组，返回报文如下。  转换JSONArray会抛异常，这边捕获异常，并提示未查询到数据
//        "data": { "chargeCode": "", "proName": "", "price": "", "spec": "" } }
            return Result.error("未查询到数据！");
        }
    }

    /**
     * 同步HIS项目
     * @return
     */
    @PostMapping(value = "/synChargeCode")
    public Result<?> synChargeCode() {

        JSONObject chargeCodeJson = HisApiForFCRenminUtils.queryHisChargeCode(null);
        if(chargeCodeJson == null){
            return Result.error("未查询到数据！");
        }
        if(!PdConstant.SUCCESS_0.equals(chargeCodeJson.getString("statusCode"))){
            return Result.error("查询HIS收费项目失败，请重新查询或联系管理员！HIS返回："+chargeCodeJson.getString("msg"));
        }

        try{
            JSONArray jsonArray = chargeCodeJson.getJSONArray("data");
            List<HisChargeInf> chargeInfList = new ArrayList<>();
            for (int i = 0; i < jsonArray.size(); i++){
                if(oConvertUtils.isNotEmpty(jsonArray.getJSONObject(i).getString("chargeCode"))){
                    HisChargeInf inf = new HisChargeInf();
                    String fsfJe = jsonArray.getJSONObject(i).getString("price");
                    inf.setFsfXmmc(jsonArray.getJSONObject(i).getString("proName"));
                    inf.setFsfXmbh(jsonArray.getJSONObject(i).getString("chargeCode"));
                    inf.setFsfSpec(jsonArray.getJSONObject(i).getString("spec"));
                    inf.setPy(jsonArray.getJSONObject(i).getString("pyCode"));
                    if(oConvertUtils.isNotEmpty(fsfJe)){
                        inf.setFsfJe(new BigDecimal(fsfJe));
                    }
                    chargeInfList.add(inf);
                }
            }
            if(CollectionUtils.isNotEmpty(chargeInfList)){
                LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
                boolean bool = hisChargeService.deleteByDepartParentId(sysUser.getDepartParentId());
                hisChargeService.saveBatch(chargeInfList);
                return Result.ok("同步成功");
            }else{
                return Result.error("未查询到数据！");
            }
        }catch (Exception e){
            return Result.error("未查询到数据！");
        }
    }



}
