package org.jeecg.modules.external.service.impl;

import org.jeecg.modules.external.entity.ExInspectionItemsUseDetail;
import org.jeecg.modules.external.mapper.ExInspectionItemsUseDetailMapper;
import org.jeecg.modules.external.service.IExInspectionItemsUseDetailService;
import org.jeecg.modules.pd.entity.PdUsePackage;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.List;

/**
 * @Description: 检验项目使用详情表
 * @Author: jiangxz
 * @Date:   2020-05-11
 * @Version: V1.0
 */
@Service
public class ExInspectionItemsUseDetailServiceImpl extends ServiceImpl<ExInspectionItemsUseDetailMapper, ExInspectionItemsUseDetail> implements IExInspectionItemsUseDetailService {

    @Override
    public List<ExInspectionItemsUseDetail> selectList(ExInspectionItemsUseDetail exInspectionItemsUseDetail) {
        return this.baseMapper.selectList(exInspectionItemsUseDetail);
    }

    @Override
    public List<PdUsePackage> selectListByCT(ExInspectionItemsUseDetail exInspectionItemsUseDetail) {
        return baseMapper.selectListByCT(exInspectionItemsUseDetail);
    }
}
