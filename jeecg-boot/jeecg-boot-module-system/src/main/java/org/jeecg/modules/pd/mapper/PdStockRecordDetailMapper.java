package org.jeecg.modules.pd.mapper;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.jeecg.modules.pd.entity.PdStockRecordDetail;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

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

	public List<PdStockRecordDetail> selectList(@Param("entity") PdStockRecordDetail pdStockRecordDetail);

	public List<PdStockRecordDetail> selectListForRefBarCodeCheck(@Param("entity") PdStockRecordDetail pdStockRecordDetail);

	IPage<PdStockRecordDetail> selectList(Page<PdStockRecordDetail> page, @Param("entity") PdStockRecordDetail pdStockRecordDetail);


	void updateInHuoweiCode(PdStockRecordDetail detail);

	void updateOutHuoweiCode(PdStockRecordDetail detail);

	/**
	 * 统计库存总数量——首页展示用
	 * @param detail
	 * @return
	 */
	Map<String,Object> queryStockRecordCount(PdStockRecordDetail detail);
}
