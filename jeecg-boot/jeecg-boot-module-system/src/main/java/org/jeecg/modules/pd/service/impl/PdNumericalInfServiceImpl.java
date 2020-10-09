package org.jeecg.modules.pd.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.modules.pd.entity.PdNumericalInf;
import org.jeecg.modules.pd.mapper.PdNumericalInfMapper;
import org.jeecg.modules.pd.service.IPdNumericalInfService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;

/**
 * @Description:
 * @Author: jeecg-boot
 * @Date: 2018-12-29
 * @Version: V1.0
 */
@Service
public class PdNumericalInfServiceImpl extends ServiceImpl<PdNumericalInfMapper, PdNumericalInf> implements IPdNumericalInfService {
    @Autowired
    private PdNumericalInfMapper pdNumericalInfMapper;


    @Override
    public Page<PdNumericalInf> selectListByPage(Page<PdNumericalInf> page, PdNumericalInf pdNumericalInf) {
        return pdNumericalInfMapper.selectList(page, pdNumericalInf);
    }

    @Override
    public List<PdNumericalInf> selectList(PdNumericalInf pdNumericalInf) {
        return pdNumericalInfMapper.selectList(pdNumericalInf);
    }

    @Override
    public PdNumericalInf getMonth() {
        return pdNumericalInfMapper.getMonth();
    }

    ;

    @Override
    public PdNumericalInf getOne(PdNumericalInf numericalInf) {
        return pdNumericalInfMapper.getOne(numericalInf);
    }

    ;

    /*根据月份统计  所属科室   试剂入库数量   试剂入库金额   耗材入库数量   耗材入库金额*/
    @Override
    public HashMap selectInItemOrRecordNum(PdNumericalInf numericalInf) {
        return pdNumericalInfMapper.selectInItemOrRecordNum(numericalInf);
    }

    ;

    /*根据月份统计  所属科室   试剂出库数量   试剂出库金额   耗材出库数量   耗材出库金额*/
    @Override
    public HashMap selectOutRecordNum(PdNumericalInf numericalInf) {
        return pdNumericalInfMapper.selectOutRecordNum(numericalInf);
    }

    ;

    /*根据月份统计试剂使用金额	试剂使用数量*/
    @Override
    public HashMap selectItemNumOrItemPrice(PdNumericalInf numericalInf) {
        return pdNumericalInfMapper.selectItemNumOrItemPrice(numericalInf);
    }

    ;

    /*统计试剂库存数量	试剂库存金额     耗材库存数量    耗材库存金额*/
    @Override
    public HashMap selectStockNumOrStockPrice(PdNumericalInf numericalInf) {
        return pdNumericalInfMapper.selectStockNumOrStockPrice(numericalInf);
    }

    ;

    /*统计检验项目收入金额	  检验项目数量*/
    @Override
    public HashMap selectExInspectionPrice(PdNumericalInf numericalInf) {
        return pdNumericalInfMapper.selectExInspectionPrice(numericalInf);
    }

    ;

    /*统计耗材使用数量    耗材使用金额*/
    @Override
    public HashMap selectDosageNumOrPrice(PdNumericalInf numericalInf) {
        return pdNumericalInfMapper.selectDosageNumOrPrice(numericalInf);
    }

    ;

    /*统计退货数量    退货金额*/
    @Override
    public HashMap selectrejectedNumOrPrice(PdNumericalInf numericalInf) {
        return pdNumericalInfMapper.selectrejectedNumOrPrice(numericalInf);
    }

    ;

    /*统计试剂理论使用规格数量   实际使用规格数量*/
    @Override
    public HashMap selectItemSpecNum(PdNumericalInf numericalInf) {
        return pdNumericalInfMapper.selectItemSpecNum(numericalInf);
    }

    ;
}