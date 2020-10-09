package org.jeecg.modules.pd.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.pd.entity.PdPurchaseTempDetail;

import java.util.List;

/**
 * @Description: 申购模板详细表
 * @Author: mcb
 * @Date:   2020-02-04
 * @Version: V1.0
 */
public interface IPdPurchaseTempDetailService extends IService<PdPurchaseTempDetail> {

	public List<PdPurchaseTempDetail> queryPdPurchaseTempDetail(PdPurchaseTempDetail purchaseTempDetail);
}
