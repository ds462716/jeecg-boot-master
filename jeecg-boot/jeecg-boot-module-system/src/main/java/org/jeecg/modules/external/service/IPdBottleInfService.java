package org.jeecg.modules.external.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.external.entity.PdBottleInf;

/**
 * @Description: 开闭瓶记录表
 * @Author: mcb
 * @Date:   2020-05-26
 * @Version: V1.0
 */
public interface IPdBottleInfService extends IService<PdBottleInf> {

    public Page<PdBottleInf> selectList(Page<PdBottleInf> page, PdBottleInf pdBottleInf);

}
