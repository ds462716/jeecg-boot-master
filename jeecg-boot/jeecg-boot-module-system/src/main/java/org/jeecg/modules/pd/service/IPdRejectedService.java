package org.jeecg.modules.pd.service;

import org.jeecg.modules.pd.entity.PdRejected;
import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.pd.entity.PdRejectedDetail;

import java.util.List;

/**
 * @Description: pd_rejected
 * @Author: jiangxz
 * @Date:   2020-03-16
 * @Version: V1.0
 */
public interface IPdRejectedService extends IService<PdRejected> {
    /**
     * 添加一对多
     *
     */
    public void saveMain(PdRejected pdRejected, List<PdRejectedDetail> pdRejectedDetailList) ;

}
