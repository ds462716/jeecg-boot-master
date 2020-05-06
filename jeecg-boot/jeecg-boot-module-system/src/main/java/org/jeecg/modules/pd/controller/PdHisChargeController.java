package org.jeecg.modules.pd.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.pd.entity.HisChargeInf;
import org.jeecg.modules.pd.service.IHisChargeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
* @Description: HIS系统收费项目基础信息
* @Author: jeecg-boot
* @Date:   2020-02-11
* @Version: V1.0
*/
@RestController
@RequestMapping("/pd/pdHisCharge")
@Slf4j
public class PdHisChargeController {
   @Autowired
   private IHisChargeService hisChargeService;

   /**
    * 分页列表查询
    *
    * @param hisChargeInf
    * @param pageNo
    * @param pageSize
    * @param req
    * @return
    */
   @GetMapping(value = "/list")
   public Result<?> queryPageList(HisChargeInf hisChargeInf,
                                  @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
                                  @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
                                  HttpServletRequest req) {
       Page<HisChargeInf> page = new Page<HisChargeInf>(pageNo, pageSize);
       //LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
       //pdApplyOrderPage.setDepartId(sysUser.getCurrentDepartId());
      // pdApplyOrderPage.setDepartParentId(sysUser.getDepartParentId());
       IPage<HisChargeInf> pageList = hisChargeService.selectList(page, hisChargeInf);
       return Result.ok(pageList);
   }


   /**
    *   同步更新HIS收费项目基础信息
    *
    * @param hisChargeInf
    * @return
    */
   @PostMapping(value = "/synUpdate")
   public Result<?> synUpdate(@RequestBody HisChargeInf hisChargeInf) {
       List<HisChargeInf> list= hisChargeService.selectByHisCharge();
       hisChargeService.saveMain(list);
       return Result.ok("操作成功！");
   }

}
