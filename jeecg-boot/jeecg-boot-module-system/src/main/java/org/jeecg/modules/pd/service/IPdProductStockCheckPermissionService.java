package org.jeecg.modules.pd.service;

import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.pd.entity.PdProductStockCheckPermission;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @Description: 盘点权限表
 * @Author: jiangxz
 * @Date:   2020-07-14
 * @Version: V1.0
 */
public interface IPdProductStockCheckPermissionService extends IService<PdProductStockCheckPermission> {

    Result<Object> locking(String id,String recordId);

    Result<Object> unlock(String id,String recordId);
}
