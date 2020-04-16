package org.jeecg.modules.pd.mapper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jeecg.modules.pd.entity.PdDosage;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * @Description: 用量表
 * @Author: jiangxz
 * @Date:   2020-03-13
 * @Version: V1.0
 */
public interface PdDosageMapper extends BaseMapper<PdDosage> {

    List<PdDosage> selectList(PdDosage pdDosage);

    Map<String,Object> queryPdDosageCount(PdDosage PdDosage);

    List<HashMap> queryPdDosageDateList(PdDosage PdDosage);

    List<HashMap> queryPdDosageTotalList(PdDosage PdDosage);

    List<PdDosage> queryPdDosageList(PdDosage pdDosage);
}
