package org.jeecg.modules.pd.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.pd.entity.PdStockRecord;
import org.jeecg.modules.pd.entity.PdStockRecordDetail;

import java.util.List;
import java.util.Map;

/**
 * @Description: 出入库明细表
 * @Author: jiangxz
 * @Date:   2020-02-13
 * @Version: V1.0
 */
public interface IPdStockRecordDetailService extends IService<PdStockRecordDetail> {

	public List<PdStockRecordDetail> selectByMainId(PdStockRecordDetail pdStockRecordDetail);

	List<PdStockRecordDetail> selectList(PdStockRecordDetail pdStockRecordDetail);
	IPage<PdStockRecordDetail> selectList(Page<PdStockRecordDetail> pageList, PdStockRecordDetail pdStockRecordDetail);

	/**
	 * 查询入库单明细
	 * @param pdStockRecordDetail
	 * @return
	 */
	List<PdStockRecordDetail> selectInList(PdStockRecordDetail pdStockRecordDetail);
	IPage<PdStockRecordDetail> selectInList(Page<PdStockRecordDetail> pageList, PdStockRecordDetail pdStockRecordDetail);

	/**
	 * 查询出库单明细
	 * @param pdStockRecordDetail
	 * @return
	 */
	List<PdStockRecordDetail> selectOutList(PdStockRecordDetail pdStockRecordDetail);
	IPage<PdStockRecordDetail> selectOutList(Page<PdStockRecordDetail> pageList, PdStockRecordDetail pdStockRecordDetail);


	/**
	 * 通过条件查询出入库明细
	 * @param pdStockRecordDetail
	 * @return
	 */
	public List<PdStockRecordDetail> queryPdStockRecordDetail(PdStockRecordDetail pdStockRecordDetail);

	/**
	 * 统计库存总数量——首页展示用
	 * @param detail
	 * @return
	 */
	Map<String,Object> queryStockRecordCount(PdStockRecordDetail detail);

	List<PdStockRecordDetail> selectListForRefBarCodeCheck(PdStockRecordDetail pdStockRecordDetail);

	List<PdStockRecordDetail> chooseStockRecordDetailList(PdStockRecord pdStockRecord);

	/**
	 * 库存管理  查询出入库明细
	 * @param pageList
	 * @param pdStockRecordDetail
	 * @return
	 */
	IPage<PdStockRecordDetail> selectStockRecordListPage(Page<PdStockRecordDetail> pageList, PdStockRecordDetail pdStockRecordDetail);

	List<PdStockRecordDetail> selectStockRecordList(PdStockRecordDetail pdStockRecordDetail);

	/**
	 * 用于市立医院供应室查询出库明细
	 * @param pageList
	 * @param pdStockRecordDetail
	 * @return
	 */
	IPage<PdStockRecordDetail> selectGZSLRecordDetailPage(Page<PdStockRecordDetail> pageList, PdStockRecordDetail pdStockRecordDetail);

	List<PdStockRecordDetail> selectGZSLRecordDetailList(PdStockRecordDetail pdStockRecordDetail);

}
