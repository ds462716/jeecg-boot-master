package org.jeecg.modules.external.fengcheng.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.external.fengcheng.entity.HisSpdChargeDetailFC;
import org.jeecg.modules.external.fengcheng.entity.HisSpdChargeFC;
import org.jeecg.modules.pd.entity.PdDosage;
import org.jeecg.modules.pd.entity.PdDosageDetail;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description: 用量表
 * @Author: jiangxz
 * @Date:   2020-03-13
 * @Version: V1.0
 */
public interface HisSpdChargeFCMapper extends BaseMapper<HisSpdChargeFC> {

    List<PdDosage> selectList(@Param("entity") PdDosage entity);

    List<PdDosageDetail> selectDetailList(@Param("entity") PdDosageDetail entity);
}
