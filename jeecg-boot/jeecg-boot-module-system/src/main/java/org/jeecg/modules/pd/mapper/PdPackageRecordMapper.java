package org.jeecg.modules.pd.mapper;

import java.util.List;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.pd.entity.PdPackageRecord;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * @Description: pd_package_record
 * @Author: jiangxz
 * @Date:   2020-04-22
 * @Version: V1.0
 */
public interface PdPackageRecordMapper extends BaseMapper<PdPackageRecord> {


    List<PdPackageRecord> queryList(@Param("entity") PdPackageRecord pdPackageRecord);

    IPage<PdPackageRecord> queryList(Page<PdPackageRecord> page, @Param("entity") PdPackageRecord pdPackageRecord);


}
