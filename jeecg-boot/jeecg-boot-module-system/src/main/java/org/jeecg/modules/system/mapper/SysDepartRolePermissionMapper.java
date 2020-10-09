package org.jeecg.modules.system.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.system.entity.SysDepartRolePermission;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * @Description: 部门角色权限
 * @Author: jeecg-boot
 * @Date:   2020-02-12
 * @Version: V1.0
 */
public interface SysDepartRolePermissionMapper extends BaseMapper<SysDepartRolePermission> {

    List<SysDepartRolePermission> findDepartRolePermissionByName(Map<String, Object> map);
}
