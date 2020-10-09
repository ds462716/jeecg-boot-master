package org.jeecg.modules.pd.mapper;

import java.util.List;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.pd.entity.PdVender;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * @Description: 生产厂家
 * @Author: zxh
 * @Date:   2020-01-09
 * @Version: V1.0
 */
public interface PdVenderMapper extends BaseMapper<PdVender> {

    List<PdVender> verify(PdVender pdVender);

    List<PdVender> selectList(PdVender pdVender);

    void updateValidityFlag(PdVender pdVender);

    List<PdVender> selectAllList(PdVender pdVender);

    Page<PdVender> selectListByPage(Page<PdVender> page, @Param("entity") PdVender entity);
}
