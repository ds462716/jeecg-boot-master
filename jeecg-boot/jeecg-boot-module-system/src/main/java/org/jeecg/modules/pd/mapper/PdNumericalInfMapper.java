package org.jeecg.modules.pd.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.pd.entity.PdNumericalInf;

import java.util.HashMap;
import java.util.List;

/**
 * @Description: 耗材/试剂使用统计报表
 * @Author: jeecg-boot
 * @Date:   2020-02-25
 * @Version: V1.0
 */
public interface PdNumericalInfMapper extends BaseMapper<PdNumericalInf> {

    Page<PdNumericalInf> selectList(Page<PdNumericalInf> page, @Param("entity") PdNumericalInf pdNumericalInf);

    List<PdNumericalInf> selectList(@Param("entity") PdNumericalInf pdNumericalInf);

    PdNumericalInf getMonth();

    PdNumericalInf getOne(PdNumericalInf numericalInf);

    /*根据月份统计  所属科室   试剂入库数量   试剂入库金额   耗材入库数量   耗材入库金额*/
    public  HashMap selectItemOrRecordNum(PdNumericalInf  numericalInf);


    /*根据月份统计试剂使用金额	试剂使用数量*/
    public  HashMap selectItemNumOrItemPrice(PdNumericalInf  numericalInf);

    /*统计试剂库存数量	试剂库存金额     耗材库存数量    耗材库存金额*/
    public  HashMap  selectStockNumOrStockPrice(PdNumericalInf  numericalInf);

    /*统计检验项目收入金额	  检验项目数量*/
    public  HashMap  selectExInspectionPrice(PdNumericalInf  numericalInf);

    /*统计耗材使用数量    耗材使用金额*/
    public  HashMap  selectDosageNumOrPrice(PdNumericalInf  numericalInf);
}
