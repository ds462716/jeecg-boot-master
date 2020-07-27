package org.jeecg.modules.pd.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.apache.commons.collections.CollectionUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.constant.CacheConstant;
import org.jeecg.common.constant.CommonConstant;
import org.jeecg.common.constant.PdConstant;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.pd.entity.PdProductStockCheck;
import org.jeecg.modules.pd.entity.PdProductStockCheckPermission;
import org.jeecg.modules.pd.mapper.PdProductStockCheckMapper;
import org.jeecg.modules.pd.mapper.PdProductStockCheckPermissionMapper;
import org.jeecg.modules.pd.service.IPdDepartService;
import org.jeecg.modules.pd.service.IPdProductStockCheckPermissionService;
import org.jeecg.modules.pd.service.IPdProductStockCheckService;
import org.jeecg.modules.system.entity.SysDepartRolePermission;
import org.jeecg.modules.system.service.ISysDepartRolePermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * @Description: 盘点权限表
 * @Author: jiangxz
 * @Date:   2020-07-14
 * @Version: V1.0
 */
@Service
public class PdProductStockCheckPermissionServiceImpl extends ServiceImpl<PdProductStockCheckPermissionMapper, PdProductStockCheckPermission> implements IPdProductStockCheckPermissionService {

    @Autowired
    private IPdDepartService pdDepartService;
    @Autowired
    private ISysDepartRolePermissionService sysDepartRolePermissionService;
    @Autowired
    private PdProductStockCheckMapper pdProductStockCheckMapper;
    @Autowired
    public RedisTemplate<String, Object> redisTemplate;


    /**
     *  锁定
     * @param id
     * @return
     */
    @Override
    @Transactional
    public Result<Object> locking(String id,String recordId) {
        if(oConvertUtils.isEmpty(id) || oConvertUtils.isEmpty(recordId)){
            return Result.error("参数错误!");
        }
        //修改状态
        PdProductStockCheck pdProductStockCheck = new PdProductStockCheck();
        pdProductStockCheck.setId(recordId);
        pdProductStockCheck.setLockingState(PdConstant.PRODUCT_STOCK_CHECK_LOCKING_STATE_1);//锁定
        pdProductStockCheckMapper.updateById(pdProductStockCheck);

        // ↓↓↓↓↓↓↓ add by jiangxz 20200727
        LambdaQueryWrapper<PdProductStockCheck> queryStockCheck = new LambdaQueryWrapper<PdProductStockCheck>()
                .eq(PdProductStockCheck::getId, recordId);
        pdProductStockCheck = pdProductStockCheckMapper.selectOne(queryStockCheck);
        PdProductStockCheckPermission pdProductStockCheckPermission = new PdProductStockCheckPermission();
        pdProductStockCheckPermission.setTargetDepartId(id);
        //先删除该部门下所有的历史记录
        LambdaQueryWrapper<PdProductStockCheckPermission> query = new LambdaQueryWrapper<PdProductStockCheckPermission>()
                .eq(PdProductStockCheckPermission::getTargetDepartId, id);
        this.remove(query);
        this.save(pdProductStockCheckPermission);
        // ↑↑↑↑↑↑↑ add by jiangxz 20200727

// ↓↓↓↓↓↓↓ modified by jiangxz 20200727 盘点不控制权限，直接判断该表是否有数据，若有数据就不能出入库 退货 使用
        //查询出需要锁定的权限
//        Map<String,Object> map = new HashMap<>();
//        map.put("departId",id);
//        List<String> stringList = new ArrayList<>();
//        stringList.add(PdConstant.IN_RECORD_EXAMINE);//入库审核按钮
//        stringList.add(PdConstant.OUT_RECORD_EXAMINE);//出库审核按钮
//        stringList.add(PdConstant.REJECTED_SAVE);//出库审核按钮
//        map.put("names",stringList);
//        List<SysDepartRolePermission> sysDepartRolePermissions = pdDepartService.findDepartRolePermissionByName(map);
//        //如果存在
//        if(sysDepartRolePermissions!=null && sysDepartRolePermissions.size()>0){
//            List<PdProductStockCheckPermission> pdProductStockCheckPermissions = new ArrayList<>();
//            List<String> sysDepartRolePermissionIds = new ArrayList<>();
//            for(SysDepartRolePermission sysDepartRolePermission:sysDepartRolePermissions){
//                PdProductStockCheckPermission pdProductStockCheckPermission = new PdProductStockCheckPermission();
//                pdProductStockCheckPermission.setRoleId(sysDepartRolePermission.getRoleId());
//                pdProductStockCheckPermission.setPermissionId(sysDepartRolePermission.getPermissionId());
//                pdProductStockCheckPermission.setTargetDepartId(sysDepartRolePermission.getDepartId());
//                pdProductStockCheckPermissions.add(pdProductStockCheckPermission);
//                sysDepartRolePermissionIds.add(sysDepartRolePermission.getId());
//            }
//            //先删除该部门下所有的历史记录
//            LambdaQueryWrapper<PdProductStockCheckPermission> query = new LambdaQueryWrapper<PdProductStockCheckPermission>()
//                    .eq(PdProductStockCheckPermission::getTargetDepartId, id);
//            this.remove(query);
//            //保存到临时权限表中
//            this.saveBatch(pdProductStockCheckPermissions);
//            //删除现有的权限
//            sysDepartRolePermissionService.removeByIds(sysDepartRolePermissionIds);
//            pdDepartService.refreshShiro();
//        }
// ↑↑↑↑↑↑↑ modified by jiangxz 20200727 盘点不控制权限，直接判断该表是否有数据，若有数据就不能出入库 退货 使用

        return Result.ok("锁定成功!");
    }

    /**
     * 解锁
     * @param id
     * @return
     */
    @Override
    @Transactional
    public Result<Object> unlock(String id,String recordId) {
        if(oConvertUtils.isEmpty(id) || oConvertUtils.isEmpty(recordId)){
            return Result.error("参数错误!");
        }
        //修改状态
        PdProductStockCheck pdProductStockCheck = new PdProductStockCheck();
        pdProductStockCheck.setId(recordId);
        pdProductStockCheck.setLockingState(PdConstant.PRODUCT_STOCK_CHECK_LOCKING_STATE_0);//解锁
        pdProductStockCheckMapper.updateById(pdProductStockCheck);
        LambdaQueryWrapper<PdProductStockCheck> query_i = new LambdaQueryWrapper<PdProductStockCheck>()
                .eq(PdProductStockCheck::getLockingState, PdConstant.PRODUCT_STOCK_CHECK_LOCKING_STATE_1)
                .eq(PdProductStockCheck::getTargetDepartId, id);
        List<PdProductStockCheck> pdProductStockChecks = pdProductStockCheckMapper.selectList(query_i);
        if(pdProductStockChecks!=null && pdProductStockChecks.size()>0){
            //还有未解锁完的盘点单
            return Result.ok("当前盘点单解锁完成，请注意还有未解锁的盘点单!");
        }else{
            LambdaQueryWrapper<PdProductStockCheckPermission> query = new LambdaQueryWrapper<PdProductStockCheckPermission>()
                    .eq(PdProductStockCheckPermission::getTargetDepartId, id);
            List<PdProductStockCheckPermission> pdProductStockCheckPermissions = this.list(query);

            // ↓↓↓↓↓↓↓ add by jiangxz 20200727
            if(CollectionUtils.isNotEmpty(pdProductStockCheckPermissions)){
                for (PdProductStockCheckPermission tiem: pdProductStockCheckPermissions) {
                    this.removeById(tiem.getId());
                }
            }
            // ↑↑↑↑↑↑↑ add by jiangxz 20200727

// ↓↓↓↓↓↓↓ modified by jiangxz 20200727 盘点不控制权限，直接判断该表是否有数据，若有数据就不能出入库 退货 使用
//            //查询出锁定时删除的权限
//            List<SysDepartRolePermission> sysDepartRolePermissions = new ArrayList<>();
//            if(pdProductStockCheckPermissions!=null && pdProductStockCheckPermissions.size()>0){
//                List<String> sysDepartRolePermissionIds = new ArrayList<>();
//                List<String> pdProductStockCheckPermissionIds = new ArrayList<>();
//                for(PdProductStockCheckPermission pdProductStockCheckPermission:pdProductStockCheckPermissions){
//                    SysDepartRolePermission sysDepartRolePermission = new SysDepartRolePermission();
//                    sysDepartRolePermission.setRoleId(pdProductStockCheckPermission.getRoleId());
//                    sysDepartRolePermission.setDepartId(pdProductStockCheckPermission.getTargetDepartId());
//                    sysDepartRolePermission.setPermissionId(pdProductStockCheckPermission.getPermissionId());
//                    sysDepartRolePermissions.add(sysDepartRolePermission);
//                    sysDepartRolePermissionIds.add(pdProductStockCheckPermission.getPermissionId());
//                    pdProductStockCheckPermissionIds.add(pdProductStockCheckPermission.getId());
//                }
//                //删除前先清除重复的
//                LambdaQueryWrapper<SysDepartRolePermission> queryOne = new LambdaQueryWrapper<SysDepartRolePermission>()
//                        .eq(SysDepartRolePermission::getDepartId,id).in(SysDepartRolePermission::getPermissionId,sysDepartRolePermissionIds);
//                sysDepartRolePermissionService.remove(queryOne);
//                sysDepartRolePermissionService.saveBatch(sysDepartRolePermissions);
//                //删除备份表的内容
//                this.removeByIds(pdProductStockCheckPermissionIds);
//                pdDepartService.refreshShiro();
//            }
// ↑↑↑↑↑↑↑ modified by jiangxz 20200727 盘点不控制权限，直接判断该表是否有数据，若有数据就不能出入库 退货 使用

        }
        return Result.ok("解锁成功！");
    }

}
