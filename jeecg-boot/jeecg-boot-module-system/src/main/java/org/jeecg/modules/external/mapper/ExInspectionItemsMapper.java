package org.jeecg.modules.external.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.external.entity.ExInspectionItems;

import java.util.List;

/**
 * @Description: 检查项目表
 * @Author: jiangxz
 * @Date:   2020-04-23
 * @Version: V1.0
 */
public interface ExInspectionItemsMapper extends BaseMapper<ExInspectionItems> {

    List<ExInspectionItems> selectList(ExInspectionItems exInspectionItems);

    List<String> selectListIds();

    Page<ExInspectionItems> selectListByPage(Page<ExInspectionItems> page, @Param("entity") ExInspectionItems entity);

    Page<ExInspectionItems> patientListPage(Page<ExInspectionItems> page, @Param("entity") ExInspectionItems entity);


     ExInspectionItems  inspectionMonthQuery(ExInspectionItems exInspectionItems);


    Page<ExInspectionItems> selectExInsepectionMonth(Page<ExInspectionItems> page, @Param("entity") ExInspectionItems entity);


}
