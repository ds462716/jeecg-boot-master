package org.jeecg.modules.pd.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.pd.entity.HisDepartInf;
import org.jeecg.modules.pd.entity.HisUserInf;

import java.util.List;

/**
 * @Description: HIS用户信息
 * @Author: jeecg-boot
 * @Date:  2020-4-29
 * @Version: V1.0
 */
public interface IHisUserService extends IService<HisUserInf> {

    /**
     * 分页查询
     * @param pageList
     * @param hisUserInf
     * @return
     */
    Page<HisUserInf> selectList(Page<HisUserInf> pageList, HisUserInf hisUserInf);




    /*更新HIS系统科室信息及用户信息*/
    void synUpdateDeptOrUser(List<HisDepartInf> deptList, List<HisUserInf> userList);

}
