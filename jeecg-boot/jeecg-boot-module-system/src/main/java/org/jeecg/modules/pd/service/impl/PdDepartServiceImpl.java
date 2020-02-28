package org.jeecg.modules.pd.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.common.constant.CacheConstant;
import org.jeecg.common.constant.CommonConstant;
import org.jeecg.modules.pd.service.IPdDepartService;
import org.jeecg.modules.system.entity.SysDepart;
import org.jeecg.modules.system.mapper.SysDepartMapper;
import org.jeecg.modules.system.model.SysDepartTreeModel;
import org.jeecg.modules.system.util.FindsDepartsChildrenUtil;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 重构部门管理
 */
@Service
public class PdDepartServiceImpl extends ServiceImpl<SysDepartMapper, SysDepart> implements IPdDepartService {

    @Cacheable(value = CacheConstant.SYS_DEPARTS_CACHE)
    @Override
    /**
     * 查询当前登录人所在医院的全部部门 并拼接成树
     */
    public List<SysDepartTreeModel> queryTreeList(SysDepart sysDepart) {
        LambdaQueryWrapper<SysDepart> query = new LambdaQueryWrapper<>();
        query.eq(SysDepart::getDepartParentId,sysDepart.getDepartParentId());
        query.orderByAsc(SysDepart::getDepartOrder);
        List<SysDepart> list = this.list(query);
        // 调用wrapTreeDataToTreeList方法生成树状数据
        List<SysDepartTreeModel> listResult = FindsDepartsChildrenUtil.wrapTreeDataToTreeList(list);
        return listResult;
    }

    /**
     * 查询当前登录人所在医院的全部部门
     * @param sysDepart
     * @return
     */
    @Cacheable(value = CacheConstant.SYS_DEPARTS_CACHE)
    @Override
    public List<String> findSysDepart(SysDepart sysDepart) {
        // 全部权限ids
        List<String> ids = new ArrayList<>();
        LambdaQueryWrapper<SysDepart> query = new LambdaQueryWrapper<>();
        query.eq(SysDepart::getDepartParentId,sysDepart.getDepartParentId());
        query.orderByAsc(SysDepart::getDepartOrder);
        List<SysDepart> list = this.list(query);
        for (SysDepart pc : list) {
            ids.add(pc.getId());
        }
        return ids;
    }
}
