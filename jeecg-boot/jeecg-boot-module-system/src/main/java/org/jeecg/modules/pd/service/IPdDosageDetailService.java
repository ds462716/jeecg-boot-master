package org.jeecg.modules.pd.service;

import org.jeecg.modules.pd.entity.PdDosageDetail;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @Description: 用量详情表
 * @Author: jiangxz
 * @Date:   2020-03-13
 * @Version: V1.0
 */
public interface IPdDosageDetailService extends IService<PdDosageDetail> {

    List<PdDosageDetail> selectList(PdDosageDetail pdDosageDetail);
}
