package org.jeecg.modules.pd.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.constant.CacheConstant;
import org.jeecg.common.constant.CommonConstant;
import org.jeecg.common.constant.PdConstant;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.util.PasswordUtil;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.pd.service.IPdDepartService;
import org.jeecg.modules.pd.util.JmUtil;
import org.jeecg.modules.pd.util.UUIDUtil;
import org.jeecg.modules.system.entity.*;
import org.jeecg.modules.system.mapper.SysDepartMapper;
import org.jeecg.modules.system.mapper.SysDepartRolePermissionMapper;
import org.jeecg.modules.system.mapper.SysUserDepartMapper;
import org.jeecg.modules.system.mapper.SysUserMapper;
import org.jeecg.modules.system.model.SysDepartTreeModel;
import org.jeecg.modules.system.service.*;
import org.jeecg.modules.system.util.FindsDepartsChildrenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
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
    private ISysUserService sysUserService;

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

    @Autowired
    private ISysDepartRoleService sysDepartRoleService;

    @Autowired
    private ISysDepartPermissionService sysDepartPermissionService;

    @Autowired
    private SysDepartRolePermissionMapper sysDepartRolePermissionMapper;

    @Autowired
    public RedisTemplate<String, Object> redisTemplate;




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
        //刷新用户权限
        this.refreshShiro();

    }

    @Override
    public IPage<SysUser> findUserList(Page<SysUser> page, Map<String, Object> parMap) {
        return userMapper.findUserList(page,parMap);
    }

    @Override
    public List<SysUser> findUserList(Map<String, Object> parMap) {
        return userMapper.findAllUserList(parMap);
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

       String departType= sysDepart.getDepartType();
       List<String> departTypeList=null;
       if(StringUtils.isNotEmpty(departType)){
           departTypeList= Arrays.asList(departType.split(","));
       }
        Map<String,Object> map = new HashMap<>();
        map.put("ids",sb);
        map.put("departTypeList",departTypeList);
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
                    for(String departId:idList){
                        sysDepartRoleService.remove(new QueryWrapper<SysDepartRole>().lambda().eq(SysDepartRole::getDepartId, departId));
                        sysDepartRolePermissionService.remove(new QueryWrapper<SysDepartRolePermission>().lambda().eq(SysDepartRolePermission::getDepartId, departId));
                    }
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

    /**
     * 保存部门权限
     * @param departId
     * @param permissionIds
     * @param lastPermissionIds
     */
    @Override
    public void saveDepartPermission(String departId, String permissionIds, String lastPermissionIds) {
        List<String> add = getDiff(lastPermissionIds,permissionIds);
        if(add!=null && add.size()>0) {
            List<SysDepartPermission> list = new ArrayList<SysDepartPermission>();
            for (String p : add) {
                if(oConvertUtils.isNotEmpty(p)) {
                    SysDepartPermission rolepms = new SysDepartPermission(departId, p);
                    list.add(rolepms);
                }
            }
            sysDepartPermissionService.saveBatch(list);
        }
        List<String> delete = getDiff(permissionIds,lastPermissionIds);
        if(delete!=null && delete.size()>0) {
            for (String permissionId : delete) {
                sysDepartPermissionService.remove(new QueryWrapper<SysDepartPermission>().lambda().eq(SysDepartPermission::getDepartId, departId).eq(SysDepartPermission::getPermissionId, permissionId));
                sysDepartRolePermissionService.remove(new QueryWrapper<SysDepartRolePermission>().lambda().eq(SysDepartRolePermission::getDepartId, departId).eq(SysDepartRolePermission::getPermissionId, permissionId));
            }
        }
        //刷新用户权限
        this.refreshShiro();
    }

    @Override
    public List<SysDepart> getSysTwoDepartList(SysDepart sysDepart) {
        return sysDepartMapper.getSysTwoDepartList(sysDepart);
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


    //查询当前登录下的所有部门,并组装Id返回
    @Override
    public List<String> selectListDepart(SysDepart sysDepart) {
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
        List<SysDepart> sysList=sysDepartMapper.selectListByCTs(map);
        List<String> list=new ArrayList<>();
               for(SysDepart sys:sysList){
                   list.add(sys.getId());
              }
         return list;
    }

    /**
     * 复制粘贴部门权限
     * @param copyId
     * @param pasteId
     * @param result
     * @return
     */
    @Override
    public Result<Object> copyPermission(String copyId, String pasteId, Result<Object> result) {
        //复制部门权限
        List<SysDepartPermission> sysDepartPermissions = sysDepartPermissionService.list(new QueryWrapper<SysDepartPermission>().lambda().eq(SysDepartPermission::getDepartId, copyId));
        if(sysDepartPermissions!=null && sysDepartPermissions.size()>0){
            sysDepartPermissionService.remove(new QueryWrapper<SysDepartPermission>().lambda().eq(SysDepartPermission::getDepartId, pasteId));
            for(SysDepartPermission sysDepartPermission :sysDepartPermissions){
                sysDepartPermission.setId("");
                sysDepartPermission.setDepartId(pasteId);
            }
            sysDepartPermissionService.saveBatch(sysDepartPermissions);
            List<SysDepartRole> sysDepartRoles = sysDepartRoleService.list(new QueryWrapper<SysDepartRole>().lambda().eq(SysDepartRole::getDepartId, copyId));
            //复制部门角色
            if(sysDepartRoles!=null && sysDepartRoles.size()>0){
                sysDepartRoleService.remove(new QueryWrapper<SysDepartRole>().lambda().eq(SysDepartRole::getDepartId, pasteId));
                sysDepartRolePermissionService.remove(new QueryWrapper<SysDepartRolePermission>().lambda().eq(SysDepartRolePermission::getDepartId, pasteId));
                List<SysDepartRolePermission> allSysDepartRolePermission = new ArrayList<>();
                for(SysDepartRole sysDepartRole :sysDepartRoles){
                    String sysDepartRoleId = UUIDUtil.getUuid();
                    List<SysDepartRolePermission> sysDepartRolePermissions = sysDepartRolePermissionService.list(new QueryWrapper<SysDepartRolePermission>().lambda().eq(SysDepartRolePermission::getRoleId, sysDepartRole.getId()));
                    if(sysDepartRolePermissions!=null && sysDepartRolePermissions.size()>0){
                        for(SysDepartRolePermission sysDepartRolePermission :sysDepartRolePermissions){
                            sysDepartRolePermission.setId("");
                            sysDepartRolePermission.setDepartId(pasteId);
                            sysDepartRolePermission.setRoleId(sysDepartRoleId);
                            allSysDepartRolePermission.add(sysDepartRolePermission);
                        }
                    }
                    sysDepartRole.setId(sysDepartRoleId);
                    sysDepartRole.setDepartId(pasteId);
                    sysDepartRole.setRoleName(sysDepartRole.getRoleName()+UUIDUtil.getCurrentTimeNum());
                    sysDepartRole.setRoleCode(sysDepartRole.getRoleCode()+UUIDUtil.getCurrentTimeNum());
                }
                sysDepartRoleService.saveBatch(sysDepartRoles);
                sysDepartRolePermissionService.saveBatch(allSysDepartRolePermission);
            }
            return Result.ok("粘贴成功");
        }else{
            return Result.error("粘贴失败，复制部门没有权限！");
        }
    }

    /**
     * 一键生成部门的拼音简码自定义码
     * @return
     */
    @Cacheable(value = CacheConstant.SYS_DEPARTS_CACHE)
    @Override
    public Result<Object> generatePyWb() {
        LambdaQueryWrapper<SysDepart> query = new LambdaQueryWrapper<>();
        List<SysDepart> list = this.list(query);
        if(list!=null && list.size()>0){
            for(SysDepart sysDepart :list){
                //生成拼音简码
                sysDepart.setPy(JmUtil.getAllFirstLetter(sysDepart.getDepartName()));
                //生成五笔简码
                sysDepart.setWb(JmUtil.getWBCode(sysDepart.getDepartName()));
            }
            this.updateBatchById(list);
        }
        return Result.ok("生成简码成功");
    }

    /**
     * 一键设置密码123生成用户的简码
     * @return
     */
    @Override
    public Result<Object> generateUserPyWb() {
        LambdaQueryWrapper<SysUser> query = new LambdaQueryWrapper<>();
        List<SysUser> list = sysUserService.list(query);
        if(list!=null && list.size()>0){
            for(SysUser sysUser :list){
                //生成拼音简码
                sysUser.setPy(JmUtil.getAllFirstLetter(sysUser.getRealname()));
                //生成五笔简码
                sysUser.setWb(JmUtil.getWBCode(sysUser.getRealname()));
                String salt = oConvertUtils.randomGen(8);
                sysUser.setSalt(salt);
                String password = "123";
                String passwordEncode = PasswordUtil.encrypt(sysUser.getUsername(), password, salt);
                sysUser.setPassword(passwordEncode);
            }
            sysUserService.updateBatchById(list);
        }
        return Result.ok("生成简码成功");
    }


    @Override
    public List<Map<String,Object>>  findDepartList(SysDepart sysDepart) {
      return  sysDepartMapper.findDepartList(sysDepart);
    }

    @Override
    public List<SysDepartRolePermission> findDepartRolePermissionByName(Map<String,Object> map) {
        return sysDepartRolePermissionMapper.findDepartRolePermissionByName(map);
    }

    @Override
    public void refreshShiro(){
        Set keys = redisTemplate.keys(CommonConstant.PREFIX_USER_SHIRO_CACHE  + "*");
        redisTemplate.delete(keys);
    }

    @Override
    public List<String> queryDepartIdByParentId(String parentId){
        return sysDepartMapper.queryDepartIdByParentId(parentId);
    }
}
