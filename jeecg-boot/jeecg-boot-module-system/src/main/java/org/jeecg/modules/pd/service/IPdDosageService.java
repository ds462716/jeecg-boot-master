package org.jeecg.modules.pd.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.jeecg.modules.pd.entity.PdDosage;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description: 用量表
 * @Author: jiangxz
 * @Date:   2020-03-13
 * @Version: V1.0
 */
public interface IPdDosageService extends IService<PdDosage> {

    PdDosage initModal(String id);

    void saveMain(PdDosage pdDosage,String displayFlag);

    IPage<PdDosage> queryList(Page<PdDosage> page, PdDosage pdDosage);


    Map<String,Object> queryPdDosageCount(PdDosage pdDosage);


    List<HashMap> queryPdDosageDateList(PdDosage pdDosage);

    List<HashMap> queryPurchaseOrderTotalList(PdDosage pdDosage);
}
