package org.jeecg.modules.pd.service;

import org.jeecg.modules.pd.entity.PdAllocationDetail;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;

/**
 * @Description: 调拨明细表
 * @Author: jeecg-boot
 * @Date:   2020-02-25
 * @Version: V1.0
 */
public interface IPdAllocationDetailService extends IService<PdAllocationDetail> {

	public List<PdAllocationDetail> selectByMainId(String mainId);
}
