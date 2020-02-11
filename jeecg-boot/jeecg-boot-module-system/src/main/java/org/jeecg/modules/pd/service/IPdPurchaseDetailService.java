package org.jeecg.modules.pd.service;

import org.jeecg.modules.pd.entity.PdPurchaseDetail;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;

/**
 * @Description: 申购单详细表
 * @Author: mcb
 * @Date:   2020-02-04
 * @Version: V1.0
 */
public interface IPdPurchaseDetailService extends IService<PdPurchaseDetail> {

	public List<PdPurchaseDetail> selectByOrderNo(String orderNo);
}
