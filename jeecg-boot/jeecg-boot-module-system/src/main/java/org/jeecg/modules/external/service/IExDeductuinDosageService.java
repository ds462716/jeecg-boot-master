package org.jeecg.modules.external.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.external.entity.ExDeductuinDosage;
import org.jeecg.modules.pd.entity.PdProductStock;

/**
 * @Description: 试剂用量扣减记录表
 * @Author: jiangxz
 * @Date:   2020-05-22
 * @Version: V1.0
 */
public interface IExDeductuinDosageService extends IService<ExDeductuinDosage> {

    IPage<ExDeductuinDosage> selectList(Page<ExDeductuinDosage> pageList, ExDeductuinDosage deductuinDosage);

    void saveExdeuctuinDosage(PdProductStock stock);

    void saveExdeuctuinDosageAcc(PdProductStock stock);

}
