package org.jeecg.modules.pd.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.pd.entity.PdPurchaseTemp;
import org.jeecg.modules.pd.entity.PdPurchaseTempDetail;

import java.util.List;

/**
 * @Description: 申购模板主表
 * @Author: jeecg-boot
 * @Date:   2020-02-04
 * @Version: V1.0
 */
public interface IPdPurchaseTempService extends IService<PdPurchaseTemp> {

	/**
	 * 分页查询
	 * @param pageList
	 * @param pdPurchaseTemp
	 * @return
	 */
	Page<PdPurchaseTemp> selectList(Page<PdPurchaseTemp> pageList, PdPurchaseTemp pdPurchaseTemp);

	/**
	 * 添加一对多
	 *
	 */
	public void saveMain(PdPurchaseTemp pdPurchaseTemp, List<PdPurchaseTempDetail> pdPurchaseTempDetailList) ;

	/**
	 * 修改一对多
	 *
	 */
	public void updateMain(PdPurchaseTemp pdPurchaseTemp, List<PdPurchaseTempDetail> pdPurchaseTempDetailList);



}
