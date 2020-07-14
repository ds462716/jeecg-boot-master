package org.jeecg.modules.pd.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.pd.entity.PdStockRecord;
import org.jeecg.modules.pd.entity.PdStockRecordDetail;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description: 出入库记录表
 * @Author: jiangxz
 * @Date:   2020-02-13
 * @Version: V1.0
 */
public interface IPdStockRecordService extends IService<PdStockRecord> {

	/**
	 * 添加一对多
	 *
	 */
	public String saveMain(PdStockRecord pdStockRecord, List<PdStockRecordDetail> pdStockRecordDetailList, String recordType) ;

	/**
	 * 审核
	 */
	public String submit(PdStockRecord pdStockRecord, List<PdStockRecordDetail> pdStockRecordDetailList, String recordType) ;

	/**
	 * 修改审核、提交状态
	 * @param pdStockRecord
	 */
	public void updateStatus(PdStockRecord pdStockRecord);
	
	/**
	 * 删除一对多
	 */
	public void delMain(String id);

	/**
	 * 假删除
	 * @param id
	 */
	public void delMainByDelFlag(String id);
	
	/**
	 * 批量删除一对多
	 */
	public void delBatchMain(Collection<? extends Serializable> idList);

	/**
	 * 查询列表
	 * @param pdStockRecord
	 * @return
	 */
	List<PdStockRecord> queryList(PdStockRecord pdStockRecord,String recodeType);

	/**
	 * 分页查询列表
	 * @param pageList
	 * @param pdStockRecord
	 * @return
	 */
	IPage<PdStockRecord> queryList(Page<PdStockRecord> pageList, PdStockRecord pdStockRecord, String recodeType);

	/**
	 * 获取一条记录
	 * @param pdStockRecord
	 * @return
	 */
	PdStockRecord getOne(PdStockRecord pdStockRecord);

	/**
	 * 审批
	 */
	Map<String,String> audit(PdStockRecord auditEntity, PdStockRecord entity, String recordType);

	/**
	 * 初始化入库页面
	 * @param id
	 * @return
	 */
	PdStockRecord initInModal(String id);

	/**
	 * 获取开关
	 * @return
	 */
	PdStockRecord getOnOff();

	/**
	 * 初始化出库页面
	 * @param id
	 * @return
	 */
	PdStockRecord initOutModal(String id);

	/**
	 * 退货出库初始化页面
	 * @param id
	 * @return
	 */
	PdStockRecord initReturnModal(String id);

	/**
	 * 调入明细分页查询列表
	 * @param pageList
	 * @param pdStockRecord
	 * @return
	 */
	Page<PdStockRecord> selectTransferList(Page<PdStockRecord> pageList, PdStockRecord pdStockRecord);


	/**
	 * 入库统计报表分页查询列表
	 * @param pageList
	 * @param pdStockRecord
	 * @return
	 */
	Page<PdStockRecord> stockRecordReportQuery(Page<PdStockRecord> pageList, PdStockRecord pdStockRecord);

	/**
	 * 入库统计报表查询列表(不分页)
	 * @param pdStockRecord
	 * @return
	 */
	List<PdStockRecord> stockRecordReportQuery( PdStockRecord pdStockRecord);

	/**
	 * 入库统计报表 --根据产品按月统计入库金额
	 * @param stockRecord
	 * @return
	 */
	List<HashMap> queryRecordViewMoney(PdStockRecord stockRecord);
	/**
	 * 入库统计报表 --根据产品按月统计入库数量
	 * @param stockRecord
	 * @return
	 */
	List<HashMap> queryRecordViewCount(PdStockRecord stockRecord);
	/**
	 * 入库明细非分页--耗材柜接口查询用
	 * @param pdStockRecord
	 * @return
	 */
	public List<Map<String,Object>> findOutQueryList(PdStockRecord pdStockRecord);
}
