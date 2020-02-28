package org.jeecg.modules.pd.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.system.entity.SysDepart;
import org.jeecg.modules.system.model.SysDepartTreeModel;

import java.util.List;
import java.util.Map;

public interface IPdDepartService extends IService<SysDepart> {

    /**
     * 查询所有部门信息,并分节点进行显示
     * @return
     */
    List<SysDepartTreeModel> queryTreeList(SysDepart sysDepart);

    List<String> findSysDepart(SysDepart sysDepart);
}
