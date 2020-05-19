package org.jeecg.modules.external.service;

import org.jeecg.modules.external.entity.ExInspectionItemsUseDetail;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @Description: 检验项目使用详情表
 * @Author: jiangxz
 * @Date:   2020-05-11
 * @Version: V1.0
 */
public interface IExInspectionItemsUseDetailService extends IService<ExInspectionItemsUseDetail> {

    List<ExInspectionItemsUseDetail> selectList(ExInspectionItemsUseDetail exInspectionItemsUseDetail);
}
