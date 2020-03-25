package org.jeecg.modules.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.apache.commons.collections.CollectionUtils;
import org.apache.ibatis.session.SqlSession;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.modules.system.entity.SysDepartRole;
import org.jeecg.modules.system.entity.SysDepartRolePermission;
import org.jeecg.modules.system.entity.SysDepartRoleUser;
import org.jeecg.modules.system.mapper.SysDepartRoleMapper;
import org.jeecg.modules.system.mapper.SysDepartRoleUserMapper;
import org.jeecg.modules.system.service.ISysDepartRolePermissionService;
import org.jeecg.modules.system.service.ISysDepartRoleService;
import org.jeecg.modules.system.service.ISysDepartRoleUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

/**
 * @Description: 部门角色
 * @Author: jeecg-boot
 * @Date:   2020-02-12
 * @Version: V1.0
 */
@Service
public class SysDepartRoleServiceImpl extends ServiceImpl<SysDepartRoleMapper, SysDepartRole> implements ISysDepartRoleService {

    @Autowired
    private ISysDepartRoleUserService departRoleUserService;

    @Autowired
    private ISysDepartRolePermissionService sysDepartRolePermissionService;

    @Autowired
    private ISysDepartRoleUserService sysDepartRoleUserService;


    @Autowired
    private SqlSession sqlsession;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Result<Object> deleteV(String id) {
        try{
            List<SysDepartRoleUser> roleUserList = departRoleUserService.list(new QueryWrapper<SysDepartRoleUser>().eq("drole_id",id));
            if(CollectionUtils.isNotEmpty(roleUserList)){
                return Result.error("删除失败!，当前部门角色被使用不能删除");
            }
            this.removeById(id);
            return Result.ok("删除成功!");
        }catch(Exception e){
            e.printStackTrace();
            return Result.error("删除失败!，系统异常");
        }

    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Result<Object> deleteBatchV(String ids) {
        try{
            SysDepartRoleUserMapper dao = sqlsession.getMapper(SysDepartRoleUserMapper.class);
            List<String> idList = Arrays.asList(ids.split(","));
            if(idList!=null && idList.size()>0){
                boolean bl = true;
                for(String id : idList){
                    List<SysDepartRoleUser> roleUserList = departRoleUserService.list(new QueryWrapper<SysDepartRoleUser>().eq("drole_id",id));
                    if(CollectionUtils.isNotEmpty(roleUserList)){
                        bl = false;
                        continue;
                    }
                    dao.deleteById(id);
                    sysDepartRolePermissionService.remove(new QueryWrapper<SysDepartRolePermission>().lambda().eq(SysDepartRolePermission::getRoleId, id));
                    sysDepartRoleUserService.remove(new QueryWrapper<SysDepartRoleUser>().lambda().eq(SysDepartRoleUser::getDroleId, id));
                }
                if(bl){
                    return Result.ok("批量删除成功!");
                }else{
                    return Result.ok("部分删除成功，被使用的不能删除!");
                }
            }else{
                return Result.error("删除失败,参数不正确!");
            }
        }catch(Exception e){
            e.printStackTrace();
            return Result.error("删除失败!，系统异常");
        }


    }
}
