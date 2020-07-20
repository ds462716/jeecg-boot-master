package org.jeecg.modules.pd.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.ibatis.session.SqlSession;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.constant.PdConstant;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.util.DateUtils;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.pd.entity.PdProductStockCheck;
import org.jeecg.modules.pd.entity.PdProductStockCheckChild;
import org.jeecg.modules.pd.entity.PdStockLog;
import org.jeecg.modules.pd.mapper.PdProductStockCheckChildMapper;
import org.jeecg.modules.pd.mapper.PdProductStockCheckMapper;
import org.jeecg.modules.pd.service.*;
import org.jeecg.modules.pd.util.UUIDUtil;
import org.jeecg.modules.system.entity.SysDepart;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.*;

/**
 * @Description: 盘点记录表
 * @Author: jiangxz
 * @Date:   2020-02-28
 * @Version: V1.0
 */
@Service
public class PdProductStockCheckServiceImpl extends ServiceImpl<PdProductStockCheckMapper, PdProductStockCheck> implements IPdProductStockCheckService {

	@Autowired
	private PdProductStockCheckMapper pdProductStockCheckMapper;
	@Autowired
	private PdProductStockCheckChildMapper pdProductStockCheckChildMapper;

	@Autowired
	private IPdProductStockCheckChildService pdProductStockCheckChildService;
	@Autowired
	private SqlSession sqlSession;
	@Autowired
	private IPdDepartService pdDepartService;
	@Autowired
	private IPdProductStockTotalService pdProductStockTotalService;
	@Autowired
	private IPdProductStockCheckPermissionService pdProductStockCheckPermissionService;


	/**
	 * 查询列表
	 * @param page
	 * @param stockCheck
	 * @return
	 */
	@Override
	public Page<PdProductStockCheck> selectList(Page<PdProductStockCheck> page, PdProductStockCheck stockCheck) {
		return baseMapper.selectListByPage(page,stockCheck);
	}

	@Override
	@Transactional
	public void saveMain(PdProductStockCheck pdProductStockCheck, List<PdProductStockCheckChild> pdProductStockCheckChildList) {
		pdProductStockCheckMapper.insert(pdProductStockCheck);
		PdProductStockCheckChildMapper dao=sqlSession.getMapper(PdProductStockCheckChildMapper.class);
		if(pdProductStockCheckChildList!=null && pdProductStockCheckChildList.size()>0) {
			for(PdProductStockCheckChild entity:pdProductStockCheckChildList) {
				//外键设置
				entity.setCheckNo(pdProductStockCheck.getCheckNo());
				dao.insert(entity);
				//pdProductStockCheckChildMapper.insert(entity);
			}
		}
	}

	@Override
	@Transactional
	public String saveMainOne(PdProductStockCheck pdProductStockCheck, List<PdProductStockCheckChild> pdProductStockCheckChildList) {
		// 修改前先删除数据
		if (oConvertUtils.isNotEmpty(pdProductStockCheck.getId())) {
			this.delMain(pdProductStockCheck.getCheckNo());
		}
		pdProductStockCheck.setCheckStatus(PdConstant.SUBMIT_STATE_1); // 待提交
		pdProductStockCheck.setAuditStatus(PdConstant.AUDIT_STATE_1); // 待审核
		pdProductStockCheckMapper.insert(pdProductStockCheck);
		if(pdProductStockCheckChildList!=null && pdProductStockCheckChildList.size()>0) {
			for(PdProductStockCheckChild entity:pdProductStockCheckChildList) {
				//外键设置
				entity.setCheckNo(pdProductStockCheck.getCheckNo());
			}
			pdProductStockCheckChildService.saveBatch(pdProductStockCheckChildList);
		}
		return pdProductStockCheck.getId();
	}

	@Override
	@Transactional
	public void updateMain(PdProductStockCheck pdProductStockCheck,List<PdProductStockCheckChild> pdProductStockCheckChildList) {
		pdProductStockCheckMapper.updateById(pdProductStockCheck);
		
		//1.先删除子表数据
		pdProductStockCheckChildMapper.deleteByCheckNo(pdProductStockCheck.getCheckNo());
		//2.子表数据重新插入
		PdProductStockCheckChildMapper dao=sqlSession.getMapper(PdProductStockCheckChildMapper.class);
		if(pdProductStockCheckChildList!=null && pdProductStockCheckChildList.size()>0) {
			for(PdProductStockCheckChild entity:pdProductStockCheckChildList) {
				//外键设置
				entity.setCheckNo(pdProductStockCheck.getCheckNo());
				dao.insert(entity);
				//pdProductStockCheckChildMapper.insert(entity);
			}
		}
	}

	@Override
	@Transactional
	public void delMain(String id) {
		pdProductStockCheckChildMapper.deleteByMainId(id);
		pdProductStockCheckMapper.deleteByMainId(id);
	}

	@Override
	@Transactional
	public void delBatchMain(Collection<? extends Serializable> idList) {
		PdProductStockCheckChildMapper childDao=sqlSession.getMapper(PdProductStockCheckChildMapper.class);
		PdProductStockCheckMapper dao=sqlSession.getMapper(PdProductStockCheckMapper.class);
		for(Serializable id:idList) {
			childDao.deleteByMainId(id.toString());
			dao.deleteById(id);
			//pdProductStockCheckChildMapper.deleteByMainId(id.toString());
			//pdProductStockCheckMapper.deleteById(id);
		}
	}

	@Transactional
	@Override
	public String submit(PdProductStockCheck pdProductStockCheck, List<PdProductStockCheckChild> pdProductStockCheckChildList) {
		// 修改前先删除数据
		if (oConvertUtils.isNotEmpty(pdProductStockCheck.getId())) {
			this.delMain(pdProductStockCheck.getCheckNo());
		}
		pdProductStockCheck.setRefuseReason("");//清空审批意见
		pdProductStockCheck.setCreateTime(DateUtils.getDate());
		pdProductStockCheck.setCheckStatus(PdConstant.SUBMIT_STATE_2); // 已提交
		pdProductStockCheck.setAuditStatus(PdConstant.AUDIT_STATE_1);   // 待审核
		pdProductStockCheckMapper.insert(pdProductStockCheck);
		if(pdProductStockCheckChildList!=null && pdProductStockCheckChildList.size()>0) {
			for(PdProductStockCheckChild entity:pdProductStockCheckChildList) {
				//外键设置
				entity.setCheckNo(pdProductStockCheck.getCheckNo());
			}
			pdProductStockCheckChildService.saveBatch(pdProductStockCheckChildList);
		}
		//盘点完成修改库存数量
		if(PdConstant.CHECK_STATE_1.equals(pdProductStockCheck.getCheckStatus())){
			LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
			SysDepart sysDepart = pdDepartService.getById(sysUser.getCurrentDepartId());
			//产品物流
			List<PdStockLog> logList = new ArrayList<PdStockLog>();
			if(pdProductStockCheckChildList!=null && pdProductStockCheckChildList.size()>0){
				for(PdProductStockCheckChild entity:pdProductStockCheckChildList) {
					//产品追踪信息
					PdStockLog prodLog = new PdStockLog();
					if(entity.getProfitLossCount()>0){
						prodLog.setLogType(PdConstant.STOCK_LOG_TYPE_11);//盘盈入库
					}else{
						prodLog.setLogType(PdConstant.STOCK_LOG_TYPE_12);//盘亏出库
					}
					prodLog.setBatchNo(entity.getBatchNo());
					prodLog.setProductBarCode(entity.getProductBarCode());
					prodLog.setExpDate(entity.getExpDate());
					prodLog.setProductId(entity.getProductId());
					prodLog.setProductNum(Math.abs(entity.getProfitLossCount()));//转换成正整数
					prodLog.setInFrom("盘点盈亏");
					prodLog.setOutTo(sysDepart.getDepartName());
					prodLog.setPatientInfo("");
					prodLog.setInvoiceNo("");
					prodLog.setChargeDeptName("");
					prodLog.setRecordTime(DateUtils.getDate());
					logList.add(prodLog);
				}
			}

		}
		return pdProductStockCheck.getId();
	}

	@Override
	public PdProductStockCheck getByOne(PdProductStockCheck pdProductStockCheck) {
		return baseMapper.getByOne(pdProductStockCheck);
	}

	@Override
	public List<PdProductStockCheck> queryList(PdProductStockCheck pdProductStockCheck) {
		return baseMapper.queryList(pdProductStockCheck);
	}

	@Override
	public PdProductStockCheck initModal(String id) {
		PdProductStockCheck pdProductStockCheck = new PdProductStockCheck();
		LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
		if (oConvertUtils.isNotEmpty(id)) { // 查看页面
			pdProductStockCheck = this.getById(id);
			if(oConvertUtils.isEmpty(pdProductStockCheck.getAuditBy())){
				pdProductStockCheck.setAuditByName(sysUser.getRealname());
			}
			List<PdProductStockCheckChild> pdProductStockCheckChilds = pdProductStockCheckChildService.selectByCheckNo(pdProductStockCheck.getCheckNo());
			pdProductStockCheck.setPdProductStockCheckChildList(pdProductStockCheckChilds);
		}else{
			String checkNo = UUIDUtil.generateOrderNoByType(PdConstant.ORDER_NO_FIRST_LETTER_PD);
			pdProductStockCheck.setCheckNo(checkNo);
			pdProductStockCheck.setCheckDate(new Date());
			//获取当前日期
			pdProductStockCheck.setSubmitDateStr(DateUtils.formatDate());
			pdProductStockCheck.setSubmitByName(sysUser.getRealname());
			pdProductStockCheck.setCheckBy(sysUser.getId());
			pdProductStockCheck.setCheckName(sysUser.getRealname());
			pdProductStockCheck.setCheckCount(0.00);
			pdProductStockCheck.setProfitLossCount(0.00);
			pdProductStockCheck.setShouldCount(0.00);
		}
		return pdProductStockCheck;
	}

    /**
     * 校验盘点单删除
     * @param id
     * @return
     */
    @Override
    @Transactional
    public Result<Object> deleteV(String id) {
        PdProductStockCheck entity = this.getById(id);
        if (entity == null) {
            return Result.error("未找到对应数据");
        }
        if (PdConstant.SUBMIT_STATE_1.equals(entity.getCheckStatus()) || PdConstant.SUBMIT_STATE_3.equals(entity.getCheckStatus())) {
            if(PdConstant.PRODUCT_STOCK_CHECK_LOCKING_STATE_1.equals(entity.getLockingState())){
                return Result.error("当前库房被锁定，请先解除锁定再删除！");
            }
            //假删除
            LambdaQueryWrapper<PdProductStockCheck> query = new LambdaQueryWrapper<PdProductStockCheck>()
                    .eq(PdProductStockCheck::getCheckNo,entity.getCheckNo());
            this.remove(query);
            LambdaQueryWrapper<PdProductStockCheckChild> query_i = new LambdaQueryWrapper<PdProductStockCheckChild>()
                    .eq(PdProductStockCheckChild::getCheckNo,entity.getCheckNo());
            pdProductStockCheckChildService.remove(query_i);
            return Result.ok("删除成功!");
        }else{
            return Result.error("当前盘点单状态非待提交或已撤回状态，不能删除！");
        }
    }

    /**
     * 更改撤销状态
     * @param pdProductStockCheck
     */
    @Override
    public void updateStatus(PdProductStockCheck pdProductStockCheck) {
        PdProductStockCheck update = new PdProductStockCheck();
        update.setId(pdProductStockCheck.getId());
        update.setCheckStatus(PdConstant.SUBMIT_STATE_3); //已撤销
        update.setAuditStatus("");
        baseMapper.updateById(update);
    }

	/**
	 * 审核通过
	 * @param auditEntity
	 * @param pdProductStockCheck
	 * @return
	 */
	@Override
	public Map<String, String> audit(PdProductStockCheck auditEntity,PdProductStockCheck pdProductStockCheck) {
		Map<String, String> result = new HashMap<>();
		LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();

		//审核通过并修改库存
		if (PdConstant.AUDIT_STATE_2.equals(auditEntity.getAuditStatus())) {
			List<PdProductStockCheckChild> pdProductStockCheckChilds = pdProductStockCheckChildService.selectByCheckNo(pdProductStockCheck.getCheckNo());
			if(pdProductStockCheckChilds!=null && pdProductStockCheckChilds.size()>0){
				pdProductStockCheck.setPdProductStockCheckChildList(pdProductStockCheckChilds);
				//处理库存
				String inStr = pdProductStockTotalService.updateCheckStock(pdProductStockCheck);
				if (PdConstant.TRUE.equals(inStr)) {
					//保存出入库记录日志
					//this.saveStockLog(pdStockRecord, inType, "");
					//接触库存锁定状态
					pdProductStockCheckPermissionService.unlock(pdProductStockCheck.getTargetDepartId(),pdProductStockCheck.getId());
					result.put("code", PdConstant.SUCCESS_200);
					result.put("message", "审核成功！");
				} else {
					result.put("code", PdConstant.FAIL_500);
					result.put("message", inStr);
					throw new RuntimeException(inStr);
				}
			}
		}else if (PdConstant.AUDIT_STATE_3.equals(auditEntity.getAuditStatus())) {
			//驳回
			auditEntity.setCheckStatus(PdConstant.SUBMIT_STATE_1); // 待提交
			result.put("code", PdConstant.SUCCESS_200);
			result.put("message", "驳回成功！");
		}
		// 变更审批意见 以及 审批状态
		auditEntity.setAuditDate(DateUtils.getDate());
		auditEntity.setAuditBy(sysUser.getId());
		baseMapper.updateById(auditEntity);
		return result;
	}

}
