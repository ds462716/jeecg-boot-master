package org.jeecg.modules.pd.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.pd.entity.PdDosage;
import org.jeecg.modules.pd.entity.PdDosageDetail;
import org.jeecg.modules.pd.vo.ExHisZyInfPage;

import java.util.List;

/**
 * @Description: HIS系统接口相关服务
 * @Author: jeecg-boot
 * @Date:  2020-4-29
 * @Version: V1.0
 */
public interface IExHisZyInfService extends IService<ExHisZyInfPage> {


    //计费信息插入HIS中间表(住院)
    public int saveExHisZyInf(PdDosage pdDosage, List<PdDosageDetail> chargeArray);

    //计费信息插入HIS中间表(门诊)
    public int saveExHisMzInf(PdDosage pdDosage, List<PdDosageDetail> chargeArray);

}
