package org.jeecg.modules.pd.mapper;

import java.util.List;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.pd.entity.PdRejectedDetail;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * @Description: pd_rejected_detail
 * @Author: jiangxz
 * @Date:   2020-03-16
 * @Version: V1.0
 */
public interface PdRejectedDetailMapper extends BaseMapper<PdRejectedDetail> {

    List<PdRejectedDetail> selectByMainId(PdRejectedDetail pdRejectedDetail);

    List<PdRejectedDetail> selectList(@Param("entity") PdRejectedDetail pdRejectedDetail);

    IPage<PdRejectedDetail> selectList(Page<PdRejectedDetail> page, @Param("entity") PdRejectedDetail pdRejectedDetail);

}
