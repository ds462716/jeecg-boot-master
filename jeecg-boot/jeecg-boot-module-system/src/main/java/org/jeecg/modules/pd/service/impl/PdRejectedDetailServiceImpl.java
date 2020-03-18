package org.jeecg.modules.pd.service.impl;

import org.jeecg.modules.pd.entity.PdRejectedDetail;
import org.jeecg.modules.pd.mapper.PdRejectedDetailMapper;
import org.jeecg.modules.pd.service.IPdRejectedDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.List;

/**
 * @Description: pd_rejected_detail
 * @Author: jiangxz
 * @Date:   2020-03-16
 * @Version: V1.0
 */
@Service
public class PdRejectedDetailServiceImpl extends ServiceImpl<PdRejectedDetailMapper, PdRejectedDetail> implements IPdRejectedDetailService {

    @Autowired
    private PdRejectedDetailMapper pdRejectedDetailMapper;

    @Override
    public List<PdRejectedDetail> selectByMainId(PdRejectedDetail pdRejectedDetail) {
        return pdRejectedDetailMapper.selectByMainId(pdRejectedDetail);
    }
}
