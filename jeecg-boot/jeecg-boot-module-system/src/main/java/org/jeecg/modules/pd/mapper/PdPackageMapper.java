package org.jeecg.modules.pd.mapper;

import java.util.List;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.pd.entity.PdPackage;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * @Description: 定数包
 * @Author: jiangxz
 * @Date:   2020-02-02
 * @Version: V1.0
 */
public interface PdPackageMapper extends BaseMapper<PdPackage> {

    List<PdPackage> queryList(@Param("entity") PdPackage pdPackage);
    IPage<PdPackage> queryList(Page<PdPackage> page, @Param("entity") PdPackage pdPackage);

}
