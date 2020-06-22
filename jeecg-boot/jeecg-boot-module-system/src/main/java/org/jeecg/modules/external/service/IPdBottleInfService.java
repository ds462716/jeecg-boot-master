package org.jeecg.modules.external.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.external.entity.PdBottleInf;

import java.util.HashMap;
import java.util.List;

/**
 * @Description: 开闭瓶记录表
 * @Author: mcb
 * @Date:   2020-05-26
 * @Version: V1.0
 */
public interface IPdBottleInfService extends IService<PdBottleInf> {

    public Page<PdBottleInf> selectList(Page<PdBottleInf> page, PdBottleInf pdBottleInf);

    public List<PdBottleInf> selectList(PdBottleInf pdBottleInf);

    public Page<PdBottleInf> bottleInfReportQuery(Page<PdBottleInf> page, PdBottleInf pdBottleInf);

    public List<PdBottleInf> bottleInfReportQuery(PdBottleInf pdBottleInf);

    public List<HashMap> queryRecordViewMoney(PdBottleInf pdBottleInf);

    public List<HashMap> queryRecordViewCount(PdBottleInf pdBottleInf);

}
