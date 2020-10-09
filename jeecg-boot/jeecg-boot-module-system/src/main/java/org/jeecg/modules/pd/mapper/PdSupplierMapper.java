package org.jeecg.modules.pd.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.pd.entity.PdSupplier;

import java.util.List;

/**
 * @Description: 供应商
 * @Author: zxh
 * @Date:   2020-01-09
 * @Version: V1.0
 */
public interface PdSupplierMapper extends BaseMapper<PdSupplier> {

    List<PdSupplier> verify(PdSupplier pdSupplier);

    List<PdSupplier> selectList(PdSupplier pdSupplier);

    void updateValidityFlag(PdSupplier pdSupplier);

    List<PdSupplier> selectAllList(PdSupplier pdSupplier);

    Page<PdSupplier> selectListByPage(Page<PdSupplier> page, @Param("entity") PdSupplier entity);
}
