package org.jeecg.modules.external.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.external.entity.ExHisChargeCodeGzzl;
import org.jeecg.modules.external.entity.ExHisMzChargeInfGzzl;
import org.jeecg.modules.external.entity.ExHisZyChargeInfGzzl;

import java.util.List;

/**
 * @Description: 试剂用量扣减记录表
 * @Author: jiangxz
 * @Date:   2020-05-22
 * @Version: V1.0
 */
public interface ExHisChargeInfGzzlMapper extends BaseMapper<ExHisZyChargeInfGzzl> {


    Page<ExHisZyChargeInfGzzl> queryZyPatientInfoList(Page<ExHisZyChargeInfGzzl> page, @Param("entity")ExHisZyChargeInfGzzl exHisZyChargeInfGzzl);

    Page<ExHisMzChargeInfGzzl> queryMzPatientInfoList(Page<ExHisMzChargeInfGzzl> page, @Param("entity")ExHisMzChargeInfGzzl exHisMzChargeInfGzzl);

    Page<ExHisChargeCodeGzzl> queryHisChargeCode(Page<ExHisChargeCodeGzzl> page, @Param("entity")ExHisChargeCodeGzzl exHisChargeCodeGzzl);

}
