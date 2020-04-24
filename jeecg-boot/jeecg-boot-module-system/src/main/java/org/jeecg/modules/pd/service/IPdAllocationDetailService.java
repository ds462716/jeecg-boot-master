package org.jeecg.modules.pd.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.pd.entity.PdAllocationDetail;

import java.util.List;

/**
 * @Description: 调拨明细表
 * @Author: jeecg-boot
 * @Date:   2020-02-25
 * @Version: V1.0
 */
public interface IPdAllocationDetailService extends IService<PdAllocationDetail> {


	public List<PdAllocationDetail> selectByAllocationNo(PdAllocationDetail allocationDetail);

	public List<PdAllocationDetail> queryAllocationDetailPack(PdAllocationDetail allocationDetail);

	}
