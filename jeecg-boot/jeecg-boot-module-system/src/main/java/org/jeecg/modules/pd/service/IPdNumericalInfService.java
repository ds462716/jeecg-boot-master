package org.jeecg.modules.pd.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.pd.entity.PdNumericalInf;

import java.util.HashMap;
import java.util.List;

/**
 * @Description: 耗材/试剂使用统计报表
 * @Author: jeecg-boot
 * @Date:  2020-4-29
 * @Version: V1.0
 */
public interface IPdNumericalInfService extends IService<PdNumericalInf> {

    Page<PdNumericalInf> selectListByPage(Page<PdNumericalInf> pageList, PdNumericalInf pdNumericalInf);

    List<PdNumericalInf> selectList(PdNumericalInf pdNumericalInf);

    /*获取配置表中当前年月*/
    PdNumericalInf getMonth();

    PdNumericalInf getOne(PdNumericalInf inf);

    /*根据月份统计  所属科室   试剂入库数量   试剂入库金额   耗材入库数量   耗材入库金额*/
     HashMap selectItemOrRecordNum(PdNumericalInf  numericalInf);


    /*根据月份统计试剂使用金额	试剂使用数量*/
     HashMap selectItemNumOrItemPrice(PdNumericalInf  numericalInf);

    /*统计试剂库存数量	试剂库存金额     耗材库存数量    耗材库存金额*/
     HashMap  selectStockNumOrStockPrice(PdNumericalInf  numericalInf);

    /*统计检验项目收入金额	  检验项目数量*/
     HashMap selectExInspectionPrice(PdNumericalInf  numericalInf);

    /*统计耗材使用数量    耗材使用金额*/
     HashMap  selectDosageNumOrPrice(PdNumericalInf  numericalInf);
}
