package org.jeecg.modules.pd.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.pd.entity.HisDepartInf;

import java.util.List;

/**
 * @Description: HIS科室信息
 * @Author: jeecg-boot
 * @Date:  2020-4-29
 * @Version: V1.0
 */
public interface IHisDepartService extends IService<HisDepartInf> {

    /**
     * 分页查询
     * @param pageList
     * @param hisDepartInf
     * @return
     */
    Page<HisDepartInf> selectList(Page<HisDepartInf> pageList, HisDepartInf hisDepartInf);



    public List<HisDepartInf> selectHisDepartInf(HisDepartInf hisDepartInf);


    /*更新HIS系统科室信息*/
    void synUpdateDept(List<HisDepartInf> deptList);

}
