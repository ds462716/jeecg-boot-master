package org.jeecg.modules.pd.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
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
public interface PdStockRecordDetailMapper extends BaseMapper<PdStockRecordDetail> {

	public boolean deleteByMainId(@Param("mainId") String mainId);

	/**
	 * 假删除
	 * @param pdStockRecordDetail
	 * @return
	 */
	public boolean deleteByDelFlag(PdStockRecordDetail pdStockRecordDetail);

	public List<PdStockRecordDetail> selectByMainId(PdStockRecordDetail pdStockRecordDetail);

	public List<PdStockRecordDetail> selectListForRefBarCodeCheck(@Param("entity") PdStockRecordDetail pdStockRecordDetail);

	/**
	 * 查询出入库记录明细
	 * @param pdStockRecordDetail
	 * @return
	 */
	public List<PdStockRecordDetail> selectList(@Param("entity") PdStockRecordDetail pdStockRecordDetail);
	IPage<PdStockRecordDetail> selectList(Page<PdStockRecordDetail> page, @Param("entity") PdStockRecordDetail pdStockRecordDetail);

	/**
	 * 查询入库单明细
	 * @param pdStockRecordDetail
	 * @return
	 */
	public List<PdStockRecordDetail> selectInList(@Param("entity") PdStockRecordDetail pdStockRecordDetail);
	IPage<PdStockRecordDetail> selectInList(Page<PdStockRecordDetail> page, @Param("entity") PdStockRecordDetail pdStockRecordDetail);

	/**
	 * 查询出库单明细
	 * @param pdStockRecordDetail
	 * @return
	 */
	public List<PdStockRecordDetail> selectOutList(@Param("entity") PdStockRecordDetail pdStockRecordDetail);
	IPage<PdStockRecordDetail> selectOutList(Page<PdStockRecordDetail> page, @Param("entity") PdStockRecordDetail pdStockRecordDetail);


	void updateInHuoweiCode(PdStockRecordDetail detail);

	void updateOutHuoweiCode(PdStockRecordDetail detail);

	/**
	 * 统计库存总数量——首页展示用
	 * @param detail
	 * @return
	 */
	Map<String,Object> queryStockRecordCount(PdStockRecordDetail detail);

	public List<PdStockRecordDetail> chooseStockRecordDetailList(PdStockRecord pdStockRecord);

	IPage<PdStockRecordDetail> selectStockRecordList(Page<PdStockRecordDetail> page, @Param("entity") PdStockRecordDetail pdStockRecordDetail);

	public List<PdStockRecordDetail> selectStockRecordList(@Param("entity") PdStockRecordDetail pdStockRecordDetail);

	/**
	 * 根据入库id查询发票号
	 * @param recordId
	 * @return
	 */
	public List<String> selectInvoiceNoByRecordId(@Param("recordId") String recordId);

	/**
	 * 根据发票号和入库id查询入库明细
	 * @param pdStockRecordDetail
	 * @return
	 */
	public List<PdStockRecordDetail> selectRecordDetailByInvoiceNo(PdStockRecordDetail pdStockRecordDetail);

	/**
	 * 用于市立医院供应室查询出库明细
	 * @param pdStockRecordDetail
	 * @return
	 */
	IPage<PdStockRecordDetail> selectGZSLRecordDetailList(Page<PdStockRecordDetail> page, @Param("entity") PdStockRecordDetail pdStockRecordDetail);
	/**
	 * 用于市立医院供应室查询出库明细(不分页)
	 * @param pdStockRecordDetail
	 * @return
	 */
	public List<PdStockRecordDetail> selectGZSLRecordDetailList( @Param("entity") PdStockRecordDetail pdStockRecordDetail);


}
