package org.jeecg.modules.pd.mapper;

import java.util.List;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.pd.entity.PdRejected;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * @Description: pd_rejected
 * @Author: jiangxz
 * @Date:   2020-03-16
 * @Version: V1.0
 */
public interface PdRejectedMapper extends BaseMapper<PdRejected> {

    List<PdRejected> selectList(@Param("entity") PdRejected pdRejected);

    IPage<PdRejected> selectList(Page<PdRejected> page, @Param("entity") PdRejected pdRejected);

}
