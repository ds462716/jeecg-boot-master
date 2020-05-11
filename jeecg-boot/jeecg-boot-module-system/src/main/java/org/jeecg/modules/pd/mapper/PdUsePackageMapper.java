package org.jeecg.modules.pd.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.pd.entity.PdUsePackage;

import java.util.List;

/**
 * @Description: 检验项目
 * @Author: zxh
 * @Date:   2020年4月21日08:57:11
 * @Version: V1.0
 */
public interface PdUsePackageMapper extends BaseMapper<PdUsePackage> {

    List<PdUsePackage> queryList(@Param("entity") PdUsePackage pdUsePackage);

    Page<PdUsePackage> queryList(Page<PdUsePackage> page, @Param("entity") PdUsePackage pdUsePackage);

    List<PdUsePackage> verify(PdUsePackage pdUsePackage);

}
