package org.jeecg.modules.external.service;

import org.jeecg.modules.external.entity.ExDeductuinDosage;
import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.pd.entity.PdProductStock;

/**
 * @Description: 试剂用量扣减记录表
 * @Author: jiangxz
 * @Date:   2020-05-22
 * @Version: V1.0
 */
public interface IExDeductuinDosageService extends IService<ExDeductuinDosage> {


    void saveExdeuctuinDosage(PdProductStock stock);

    void saveExdeuctuinDosageAcc(PdProductStock stock);

}
