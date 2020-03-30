package org.jeecg.modules.pd.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.jeecg.modules.pd.entity.PdPurchaseOrder;
import org.jeecg.modules.pd.entity.PdStockRecordDetail;
import com.baomidou.mybatisplus.extension.service.IService;
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

	Page<PdStockRecordDetail> selectList(Page<PdStockRecordDetail> pageList, PdStockRecordDetail pdStockRecordDetail);

	List<PdStockRecordDetail> selectList(PdStockRecordDetail pdStockRecordDetail);

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
}
