package org.jeecg.modules.pd.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.pd.entity.HisDepartInf;
import org.jeecg.modules.pd.entity.HisUserInf;
import org.jeecg.modules.pd.service.IHisChargeService;
import org.jeecg.modules.pd.service.IHisUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
* @Description: HIS系统用户信息
* @Author: jeecg-boot
* @Date:   2020-03-11
* @Version: V1.0
*/
@RestController
@RequestMapping("/pd/pdHisUser")
@Slf4j
public class PdHisUserController {
    @Autowired
    private IHisUserService hisUserService;
    @Autowired
    private IHisChargeService hisChargeService;
   /**
    * 分页列表查询
    * @param hisUserInf
    * @param pageNo
    * @param pageSize
    * @param req
    * @return
    */
   @GetMapping(value = "/list")
   public Result<?> queryPageList(HisUserInf hisUserInf,
                                  @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
                                  @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
                                  HttpServletRequest req) {
       Page<HisUserInf> page = new Page<HisUserInf>(pageNo, pageSize);
       IPage<HisUserInf> pageList = hisUserService.selectList(page, hisUserInf);
       return Result.ok(pageList);
   }



    /**
     *   同步更新HIS科室及HIS用户信息
     *
     * @param hisUserInf
     * @return
     */
    @PostMapping(value = "/synUpdateDeptOrUser")
    public Result<?> synUpdateDeptOrUser(@RequestBody HisUserInf hisUserInf) {
        List<HisDepartInf> deptList= hisChargeService.selectHisDepart();//查询HIS科室信息
        List<HisUserInf> userList= hisChargeService.selectHisUser();//查询HIS用户信息
        hisUserService.synUpdateDeptOrUser(deptList,userList);
        return Result.ok("操作成功！");
    }
}
