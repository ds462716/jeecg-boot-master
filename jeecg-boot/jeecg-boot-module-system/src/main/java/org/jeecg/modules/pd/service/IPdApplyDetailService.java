package org.jeecg.modules.pd.service;

import org.jeecg.modules.pd.entity.PdApplyDetail;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @Description: 申领单明细表
 * @Author: jeecg-boot
 * @Date:   2020-02-11
 * @Version: V1.0
 */
public interface IPdApplyDetailService extends IService<PdApplyDetail> {

	public List<PdApplyDetail> selectByApplyNo(PdApplyDetail applyDetail);

	public List<PdApplyDetail> queryApplyDetailPack(PdApplyDetail applyDetail);
}
