package org.jeecg.modules.pd.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.pd.entity.HisDepartInf;
import org.jeecg.modules.pd.service.IHisChargeService;
import org.jeecg.modules.pd.service.IHisDepartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
* @Description: HIS系统科室信息
* @Author: jeecg-boot
* @Date:   2020-03-11
* @Version: V1.0
*/
@RestController
@RequestMapping("/pd/pdHisDepart")
@Slf4j
public class PdHisDepartController {
    @Autowired
    private IHisDepartService hisDepartService;
    @Autowired
    private IHisChargeService hisChargeService;
   /**
    * 分页列表查询
    * @param hisDepartInf
    * @param pageNo
    * @param pageSize
    * @param req
    * @return
    */
   @GetMapping(value = "/list")
   public Result<?> queryPageList(HisDepartInf hisDepartInf,
                                  @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
                                  @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
                                  HttpServletRequest req) {
       Page<HisDepartInf> page = new Page<HisDepartInf>(pageNo, pageSize);
       IPage<HisDepartInf> pageList = hisDepartService.selectList(page, hisDepartInf);
       return Result.ok(pageList);
   }



    /**
     *   同步更新HIS科室及HIS用户信息
     * @param hisDepartInf
     * @return
     */
    @PostMapping(value = "/synUpdateDept")
    public Result<?> synUpdateDeptOrUser(@RequestBody HisDepartInf hisDepartInf) {
        List<HisDepartInf> deptList= hisChargeService.selectHisDepart();//查询HIS科室信息
        hisDepartService.synUpdateDept(deptList);
        return Result.ok("操作成功！");
    }


    /**
     *  编辑
     *
     * @param hisDepartInf
     * @return
     */
    @PutMapping(value = "/edit")
    public Result<?> edit(@RequestBody HisDepartInf hisDepartInf) {
        //先判断科室是否已经关联了HIS系统科室
        HisDepartInf departInf=new HisDepartInf();
        departInf.setSpdDepartId(hisDepartInf.getSpdDepartId());
        List<HisDepartInf> list= hisDepartService.selectHisDepartInf(departInf);
        if(CollectionUtils.isNotEmpty(list) && list.size()>0){
             String fsfKsbh= list.get(0).getFsfKsbh();
             if(!fsfKsbh.equals(hisDepartInf.getFsfKsbh())){
                 return Result.error("該科室已经关联对应的HIS系统"+list.get(0).getFsfKsmc()+"科室，请确认！");
             }
            }
        hisDepartService.updateById(hisDepartInf);
        return Result.ok("编辑成功!");
    }

}
