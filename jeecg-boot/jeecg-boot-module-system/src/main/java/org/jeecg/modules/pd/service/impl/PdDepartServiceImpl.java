package org.jeecg.modules.pd.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.common.constant.CacheConstant;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.pd.service.IPdDepartService;
import org.jeecg.modules.system.entity.SysDepart;
import org.jeecg.modules.system.entity.SysDepartRolePermission;
import org.jeecg.modules.system.entity.SysUser;
import org.jeecg.modules.system.entity.SysUserDepart;
import org.jeecg.modules.system.mapper.SysDepartMapper;
import org.jeecg.modules.system.mapper.SysDepartRolePermissionMapper;
import org.jeecg.modules.system.mapper.SysUserMapper;
import org.jeecg.modules.system.model.SysDepartTreeModel;
import org.jeecg.modules.system.service.ISysDepartRolePermissionService;
import org.jeecg.modules.system.service.ISysUserDepartService;
import org.jeecg.modules.system.util.FindsDepartsChildrenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * 重构部门管理
 */
@Service
public class PdDepartServiceImpl extends ServiceImpl<SysDepartMapper, SysDepart> implements IPdDepartService {

    @Autowired
    private SysUserMapper userMapper;

    @Autowired
    private ISysDepartRolePermissionService sysDepartRolePermissionService;

    @Autowired
    private SysDepartMapper sysDepartMapper;

    @Autowired
    private ISysUserDepartService sysUserDepartService;

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

    /**
     * 根据部门查询该部门下所有的角色
     * @param parMap
     * @return
     */
    @Override
    public IPage<SysUser> departUserList(Page<SysUser> page, Map<String, Object> parMap) {
        return userMapper.departUserList(page,parMap);
    }

    /**
     * 保存角色授权
     * @param roleId
     * @param permissionIds
     * @param lastPermissionIds
     * @param departId
     */
    @Override
    public void saveDeptRolePermission(String roleId, String permissionIds, String lastPermissionIds, String departId) {

        List<String> add = getDiff(lastPermissionIds,permissionIds);
        if(add!=null && add.size()>0) {
            List<SysDepartRolePermission> list = new ArrayList<>();
            for (String p : add) {
                if(oConvertUtils.isNotEmpty(p)) {
                    SysDepartRolePermission rolepms = new SysDepartRolePermission(roleId, p,departId);
                    list.add(rolepms);
                }
            }
            sysDepartRolePermissionService.saveBatch(list);
        }

        List<String> delete = getDiff(permissionIds,lastPermissionIds);
        if(delete!=null && delete.size()>0) {
            for (String permissionId : delete) {
                sysDepartRolePermissionService.remove(new QueryWrapper<SysDepartRolePermission>().lambda().eq(SysDepartRolePermission::getRoleId, roleId)
                        .eq(SysDepartRolePermission::getPermissionId, permissionId)
                        .eq(SysDepartRolePermission::getDepartId, departId)
                             );
            }
        }

    }

    @Override
    public IPage<SysUser> findUserList(Page<SysUser> page, Map<String, Object> parMap) {
        return userMapper.findUserList(page,parMap);
    }

    @Override
    public List<SysDepart> selectList(SysDepart sysDepart) {
        return sysDepartMapper.selectDepartList(sysDepart);
    }

    /**
     * 查询用户的所有部门
     * @param eq
     * @return
     */
    @Override
    public List<SysUserDepart> queryUserDepart(LambdaQueryWrapper<SysUserDepart> eq) {
        return sysUserDepartService.list(eq);
    }

    /**
     * 从diff中找出main中没有的元素
     * @param main
     * @param diff
     * @return
     */
    private List<String> getDiff(String main, String diff){
        if(oConvertUtils.isEmpty(diff)) {
            return null;
        }
        if(oConvertUtils.isEmpty(main)) {
            return Arrays.asList(diff.split(","));
        }

        String[] mainArr = main.split(",");
        String[] diffArr = diff.split(",");
        Map<String, Integer> map = new HashMap<>();
        for (String string : mainArr) {
            map.put(string, 1);
        }
        List<String> res = new ArrayList<String>();
        for (String key : diffArr) {
            if(oConvertUtils.isNotEmpty(key) && !map.containsKey(key)) {
                res.add(key);
            }
        }
        return res;
    }
}
