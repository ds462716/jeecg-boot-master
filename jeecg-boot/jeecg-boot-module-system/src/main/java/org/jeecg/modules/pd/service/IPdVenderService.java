package org.jeecg.modules.pd.service;

import org.jeecg.modules.pd.entity.PdVender;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;

/**
 * @Description: 生产厂家
 * @Author: zxh
 * @Date:   2020-01-09
 * @Version: V1.0
 */
public interface IPdVenderService extends IService<PdVender> {

    List<PdVender> verify(PdVender pdVender);

    List<PdVender> selectList(PdVender pdVender);

    List<PdVender> selectAllList(PdVender pdVender);

    /**
     * 修改证照有效期标识
     */
    void updateValidityFlag(PdVender pdVender);

}
