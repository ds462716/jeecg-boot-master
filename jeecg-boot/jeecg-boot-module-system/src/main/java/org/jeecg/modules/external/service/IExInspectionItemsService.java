package org.jeecg.modules.external.service;

import org.jeecg.modules.external.entity.ExInspectionItems;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @Description: 检查项目表
 * @Author: jiangxz
 * @Date:   2020-04-23
 * @Version: V1.0
 */
public interface IExInspectionItemsService extends IService<ExInspectionItems> {

    List<ExInspectionItems> selectList(ExInspectionItems exInspectionItems);

    List<String> selectListIds();
}
