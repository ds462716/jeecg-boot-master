package org.jeecg.modules.pd.service;

import org.jeecg.modules.pd.entity.PdStockRecordDetail;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;

/**
 * @Description: 出入库明细表
 * @Author: jiangxz
 * @Date:   2020-02-13
 * @Version: V1.0
 */
public interface IPdStockRecordDetailService extends IService<PdStockRecordDetail> {

	public List<PdStockRecordDetail> selectByMainId(String mainId);
}
