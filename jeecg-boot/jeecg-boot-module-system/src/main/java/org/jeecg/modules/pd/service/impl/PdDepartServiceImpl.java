package org.jeecg.modules.pd.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.collections.CollectionUtils;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.constant.CacheConstant;
import org.jeecg.common.constant.PdConstant;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.pd.service.IPdDepartService;
import org.jeecg.modules.system.entity.SysDepart;
import org.jeecg.modules.system.entity.SysDepartRolePermission;
import org.jeecg.modules.system.entity.SysUser;
import org.jeecg.modules.system.entity.SysUserDepart;
import org.jeecg.modules.system.mapper.SysDepartMapper;
import org.jeecg.modules.system.mapper.SysDepartRolePermissionMapper;
import org.jeecg.modules.system.mapper.SysUserDepartMapper;
import org.jeecg.modules.system.mapper.SysUserMapper;
import org.jeecg.modules.system.model.SysDepartTreeModel;
import org.jeecg.modules.system.service.ISysDepartRolePermissionService;
import org.jeecg.modules.system.service.ISysDepartService;
import org.jeecg.modules.system.service.ISysUserDepartService;
import org.jeecg.modules.system.util.FindsDepartsChildrenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    @Autowired
    private ISysDepartService sysDepasrtService;

    @Autowired
    private SysUserDepartMapper sysUserDepartMapper;



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


    /**
     * 查询同一部门下拥有菜单权限的用户
     * @param departId
     * @param menuName
     */
    @Override
    public List<String> findMenuUser(String departId , String menuName){
        List<String> sysUsers = userMapper.findMenuUser(departId,menuName);
        return sysUsers;
    }

    /**
     * 查询部门下所有的用户
     * @param departId
     * @return
     */
    @Override
    public List<String> findDepartUserIds(String departId){
        List<String> sysUsers = userMapper.findDepartUserIds(departId);
        return sysUsers;
    }


    //查询当前登录下的所有部门
    @Override
    public List<SysDepart> selectListTree(SysDepart sysDepart) {
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        LambdaQueryWrapper<SysDepart> query = new LambdaQueryWrapper<>();
        // 封装查询条件parentId为主键,
        query.eq(SysDepart::getDepartParentId, sysUser.getDepartParentId());
        query.eq(SysDepart::getParentId, sysUser.getCurrentDepartId());
        // 查出该主键下的所有子级
        List<SysDepart> sysDepartList = this.list(query);
        List<String> strs = new ArrayList<>();
        //当前登录人的部门
        strs.add(sysUser.getCurrentDepartId());
        if(CollectionUtils.isNotEmpty(sysDepartList)){
            String id = ""; // id
            int num = 0; // 查出的子级数量
            // 如果查出的集合不为空, 则先删除所有
            // 再遍历刚才查出的集合, 根据每个对象,查找其是否仍有子级
            for (int i = 0, len = sysDepartList.size(); i < len; i++) {
                id = sysDepartList.get(i).getId();
                strs.add(id);
                num = this.count(new LambdaQueryWrapper<SysDepart>().eq(SysDepart::getParentId, id));
                // 如果有, 则递归
                if (num > 0) {
                    findChildrenBy(id,strs);
                }
            }
        }
        //查询
        String sb ="";
        for(String pc:strs){
            sb+="'"+pc+("',");
        }
        //封装查询参数
        sb = sb.substring(0,sb.length()-1);
        Map<String,Object> map = new HashMap<>();
        map.put("ids",sb);
        map.put("departName",sysDepart.getDepartName()!=null?sysDepart.getDepartName():"");
        map.put("departParentId",sysUser.getDepartParentId());
        map.put("DEL_FLAG_NORMAL", PdConstant.DEL_FLAG_0);
        return sysDepartMapper.selectListByCTs(map);
    }

    /**
     * 递归查询
     * @param parentId
     */
    public void findChildrenBy(String parentId, List<String> strs) {
        LambdaQueryWrapper<SysDepart> query = new LambdaQueryWrapper<>();
        // 封装查询条件parentId为主键,
        query.eq(SysDepart::getParentId, parentId);
        // 查出该主键下的所有子级
        List<SysDepart> permissionList = this.list(query);
        if (permissionList != null && permissionList.size() > 0) {
            String id = ""; // id
            int num = 0; // 查出的子级数量
            // 再遍历刚才查出的集合, 根据每个对象,查找其是否仍有子级
            for (int i = 0, len = permissionList.size(); i < len; i++) {
                id = permissionList.get(i).getId();
                strs.add(id);
                num = this.count(new LambdaQueryWrapper<SysDepart>().eq(SysDepart::getParentId, id));
                // 如果有, 则递归
                if (num > 0) {
                    this.findChildrenBy(id,strs);
                }
            }
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Result<Object> deleteV(String id) {
        try{
            List<String> idList = new ArrayList<>();
            List<SysUserDepart> sysUserDeparts = sysUserDepartService.list(new QueryWrapper<SysUserDepart>().eq("dep_id",id));
            if(CollectionUtils.isNotEmpty(sysUserDeparts)){
                return Result.error("删除失败!，当前部门下还有用户不能删除");
            }else{
                idList.add(id);
                this.checkChildrenExists(id, idList);
                List<SysUserDepart> sysUserDepartList = sysUserDepartMapper.findSysUserDepartList(idList);
                if(CollectionUtils.isNotEmpty(sysUserDepartList)){
                    return Result.error("删除失败!，当前部门下的子部门还有用户不能删除");
                }else{
                    boolean ok = sysDepasrtService.removeByIds(idList);
                    if(ok) {
                        return Result.ok("删除成功!");
                    }else{
                        return Result.error("删除失败!，系统异常");
                    }
                }
            }
        }catch(Exception e){
            e.printStackTrace();
            return Result.error("删除失败!，系统异常");
        }

    }

    private void checkChildrenExists(String id, List<String> idList) {
        LambdaQueryWrapper<SysDepart> query = new LambdaQueryWrapper<SysDepart>();
        query.eq(SysDepart::getParentId,id);
        List<SysDepart> departList = sysDepasrtService.list(query);
        if(departList != null && departList.size() > 0) {
            for(SysDepart depart : departList) {
                idList.add(depart.getId());
                this.checkChildrenExists(depart.getId(), idList);
            }
        }
    }
}
