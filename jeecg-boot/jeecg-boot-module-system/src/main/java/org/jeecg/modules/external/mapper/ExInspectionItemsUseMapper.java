package org.jeecg.modules.external.mapper;

import java.util.List;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.external.entity.ExInspectionItemsUse;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * @Description: 检验项目使用表
 * @Author: jiangxz
 * @Date:   2020-05-11
 * @Version: V1.0
 */
public interface ExInspectionItemsUseMapper extends BaseMapper<ExInspectionItemsUse> {

    IPage<ExInspectionItemsUse> selectListByPage(Page<ExInspectionItemsUse> page, @Param("entity") ExInspectionItemsUse entity);
}
