package org.jeecg.modules.external.mapper;

import java.util.List;
import org.jeecg.modules.external.entity.ExInspectionItemsUseDetail;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.jeecg.modules.pd.entity.PdUsePackage;

/**
 * @Description: 检验项目使用详情表
 * @Author: jiangxz
 * @Date:   2020-05-11
 * @Version: V1.0
 */
public interface ExInspectionItemsUseDetailMapper extends BaseMapper<ExInspectionItemsUseDetail> {

    List<ExInspectionItemsUseDetail> selectList(ExInspectionItemsUseDetail exInspectionItemsUseDetail);

    List<PdUsePackage> selectListByCT(ExInspectionItemsUseDetail exInspectionItemsUseDetail);
}
