package org.jeecg.modules.pd.mapper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
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

    IPage<PdDosage> selectListByPage(Page<PdDosage> page, @Param("entity") PdDosage entity);

    PdDosage getByOne(PdDosage pdDosage);
}
