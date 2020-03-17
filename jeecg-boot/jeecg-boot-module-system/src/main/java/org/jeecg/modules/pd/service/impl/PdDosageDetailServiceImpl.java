package org.jeecg.modules.pd.service.impl;

import org.jeecg.modules.pd.entity.PdDosageDetail;
import org.jeecg.modules.pd.mapper.PdDosageDetailMapper;
import org.jeecg.modules.pd.service.IPdDosageDetailService;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.List;

/**
 * @Description: 用量详情表
 * @Author: jiangxz
 * @Date:   2020-03-13
 * @Version: V1.0
 */
@Service
public class PdDosageDetailServiceImpl extends ServiceImpl<PdDosageDetailMapper, PdDosageDetail> implements IPdDosageDetailService {

    @Override
    public List<PdDosageDetail> selectList(PdDosageDetail pdDosageDetail) {
        return baseMapper.selectList(pdDosageDetail);
    }
}
