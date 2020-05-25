package org.jeecg.modules.external.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.external.entity.ExInspectionItems;

import java.util.List;

/**
 * @Description: 检查项目表
 * @Author: jiangxz
 * @Date:   2020-04-23
 * @Version: V1.0
 */
public interface IExInspectionItemsService extends IService<ExInspectionItems> {

    /**
     * 分页查询
     * @param pageList
     * @param exInspectionItems
     * @return
     */
    IPage<ExInspectionItems> selectList(Page<ExInspectionItems> pageList, ExInspectionItems exInspectionItems);

    List<ExInspectionItems> selectList(ExInspectionItems exInspectionItems);

    List<String> selectListIds();

    void batchUsePackageDetail(String ids);
}
