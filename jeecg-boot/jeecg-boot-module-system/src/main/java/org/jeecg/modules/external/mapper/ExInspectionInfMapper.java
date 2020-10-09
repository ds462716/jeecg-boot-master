package org.jeecg.modules.external.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.external.entity.ExInspectionInf;
import org.jeecg.modules.pd.entity.PdUsePackageDetail;

import java.util.List;

/**
 * @Description: 检查项目表
 * @Author: jiangxz
 * @Date:   2020-04-23
 * @Version: V1.0
 */
public interface ExInspectionInfMapper extends BaseMapper<ExInspectionInf> {
    List<ExInspectionInf> selectList(ExInspectionInf inspectionInf);

    Page<ExInspectionInf> selectListByPage(Page<ExInspectionInf> page, @Param("entity") ExInspectionInf entity);

    List<PdUsePackageDetail> queryPdUsePackageList(ExInspectionInf inspectionInf);

}
