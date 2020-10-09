package org.jeecg.modules.external.ganzhouzhongliu.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.external.ganzhouzhongliu.entity.ExHisChargeCodeGzzl;
import org.jeecg.modules.external.ganzhouzhongliu.entity.ExHisMzChargeInfGzzl;
import org.jeecg.modules.external.ganzhouzhongliu.entity.ExHisZyChargeInfGzzl;
import org.jeecg.modules.pd.entity.PdDosage;

import java.util.List;

/**
 * @Description: 试剂用量扣减记录表
 * @Author: jiangxz
 * @Date:   2020-05-22
 * @Version: V1.0
 */
public interface ExHisChargeInfGzzlMapper extends BaseMapper<ExHisZyChargeInfGzzl> {


    Page<PdDosage> queryZyPatientInfoList(Page<PdDosage> page, @Param("entity")PdDosage pdDosage);

    Page<PdDosage> queryMzPatientInfoList(Page<PdDosage> page, @Param("entity")PdDosage pdDosage);

    Page<ExHisChargeCodeGzzl> queryHisChargeCode(Page<ExHisChargeCodeGzzl> page, @Param("entity")ExHisChargeCodeGzzl exHisChargeCodeGzzl);

    List<ExHisZyChargeInfGzzl> queryZyPatientInfoList(@Param("entity")ExHisZyChargeInfGzzl exHisZyChargeInfGzzl);
}
