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
import org.jeecg.modules.external.fengcheng.vo.SynProductChargePage;
import org.jeecg.modules.pd.entity.HisChargeInf;
import org.jeecg.modules.pd.entity.PdProduct;
import org.jeecg.modules.pd.service.IHisChargeService;
import org.jeecg.modules.pd.service.IPdProductService;
import org.jeecgframework.poi.excel.def.NormalExcelConstants;
import org.jeecgframework.poi.excel.entity.ExportParams;
import org.jeecgframework.poi.excel.view.JeecgEntityExcelView;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

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
    @Autowired
    private IPdProductService pdProductService;


    @GetMapping(value = "/hisProductList")
    public Result<?> hisProductList(HisChargeInf inf,
                                    @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
                                    @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,HttpServletRequest req) {
        Page<HisChargeInf> page = new Page<HisChargeInf>(pageNo, pageSize);

        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        inf.setDepartParentId(sysUser.getDepartParentId());

        IPage<HisChargeInf> pageList = hisChargeService.selectListForFCRMYY(page, inf);
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
        IPage<HisChargeInf> pageList = hisChargeService.selectListForFCRMYY(page, inf);
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


    /**
     * 关联ex_his_charge_inf表的分页查询
     * @param pdProduct
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @GetMapping(value = "/listForHisCharge")
    public Result<?> queryPageListForHisCharge(PdProduct pdProduct,
                                               @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                               @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                               HttpServletRequest req) {
        Page<PdProduct> page = new Page<PdProduct>(pageNo, pageSize);
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        pdProduct.setDepartParentId(sysUser.getDepartParentId());
        IPage<PdProduct> pageList = pdProductService.selectListForHisChargeByPage(page, pdProduct);//
        return Result.ok(pageList);
    }

    /**
     * 导出excel
     *
     * @param request
     * @param pdProduct
     */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, PdProduct pdProduct) {
        // Step.1 组装查询条件查询数据
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        pdProduct.setDepartParentId(sysUser.getDepartParentId());
        List<PdProduct> queryList = pdProductService.selectListForHisCharge(pdProduct);

        //Step.2 获取导出数据
        // 过滤选中数据
        String selections = request.getParameter("selections");
        List<PdProduct> pdProductList = new ArrayList<PdProduct>();
        if (oConvertUtils.isEmpty(selections)) {
            pdProductList = queryList;
        } else {
            List<String> selectionList = Arrays.asList(selections.split(","));
            pdProductList = queryList.stream().filter(item -> selectionList.contains(item.getId())).collect(Collectors.toList());
        }

        // Step.3 组装pageList
        List<SynProductChargePage> pageList = new ArrayList<SynProductChargePage>();
        for (PdProduct main : pdProductList) {
            SynProductChargePage vo = new SynProductChargePage();
            BeanUtils.copyProperties(main, vo);
            pageList.add(vo);
        }

        // Step.4 AutoPoi 导出Excel
        ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
        mv.addObject(NormalExcelConstants.FILE_NAME, "产品价格对照表");
        mv.addObject(NormalExcelConstants.CLASS, SynProductChargePage.class);
        mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("产品价格对照表", "导出人:" + sysUser.getRealname(), "产品价格对照表"));
        mv.addObject(NormalExcelConstants.DATA_LIST, pageList);
        return mv;
    }


}
