package org.jeecg.modules.external.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.jeecg.modules.external.entity.ExInspectionItemsUse;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @Description: 检验项目使用表
 * @Author: jiangxz
 * @Date:   2020-05-11
 * @Version: V1.0
 */
public interface IExInspectionItemsUseService extends IService<ExInspectionItemsUse> {

    void submit(ExInspectionItemsUse exInspectionItemsUse);

    ExInspectionItemsUse initOutModal(String id);

    IPage<ExInspectionItemsUse> selectList(Page<ExInspectionItemsUse> page, ExInspectionItemsUse exInspectionItemsUse);
}
