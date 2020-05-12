package org.jeecg.modules.pd.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.pd.entity.PdAutoOrderInf;

import java.util.List;

/**
 * <p>
 * 自动补货表 服务实现类
 * <p>
 * 
 * @Author:Steve
 * @Since：   2020-05-11
 */
public interface IPdAutoOrderService extends IService<PdAutoOrderInf>{
    /**
     * 查询补货记录
     * @return
     */
    List<PdAutoOrderInf> queryList(PdAutoOrderInf autoOrderInf);




}
