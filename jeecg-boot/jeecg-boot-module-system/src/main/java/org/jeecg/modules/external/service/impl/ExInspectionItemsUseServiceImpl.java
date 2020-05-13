package org.jeecg.modules.external.service.impl;

import org.jeecg.modules.external.entity.ExInspectionItemsUse;
import org.jeecg.modules.external.entity.ExInspectionItemsUseDetail;
import org.jeecg.modules.external.mapper.ExInspectionItemsUseMapper;
import org.jeecg.modules.external.service.IExInspectionItemsUseService;
import org.jeecg.modules.pd.entity.PdUsePackageDetail;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.ArrayList;
import java.util.List;

/**
 * @Description: 检验项目使用表
 * @Author: jiangxz
 * @Date:   2020-05-11
 * @Version: V1.0
 */
@Service
public class ExInspectionItemsUseServiceImpl extends ServiceImpl<ExInspectionItemsUseMapper, ExInspectionItemsUse> implements IExInspectionItemsUseService {



    @Override
    public void submit(ExInspectionItemsUse exInspectionItemsUse) {
        //this.save(exInspectionItemsUse);
        List<ExInspectionItemsUseDetail> exInspectionItemsUseDetailList = new ArrayList<>();
        //手动选择的库存
        if(exInspectionItemsUse.getExInspectionItemsUseDetails()!=null && exInspectionItemsUse.getExInspectionItemsUseDetails().size()>0){
            exInspectionItemsUseDetailList.addAll(exInspectionItemsUse.getExInspectionItemsUseDetails());
        }
        //选择定数包的产品
        if(exInspectionItemsUse.getPdUsePackageDetails()!=null && exInspectionItemsUse.getPdUsePackageDetails().size()>0){
            for(PdUsePackageDetail pdUsePackageDetail :exInspectionItemsUse.getPdUsePackageDetails()){

            }
        }
    }
}
