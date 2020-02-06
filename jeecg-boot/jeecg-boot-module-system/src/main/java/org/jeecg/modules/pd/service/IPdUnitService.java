package org.jeecg.modules.pd.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.jeecg.modules.pd.entity.PdUnit;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @Description: 产品单位表
 * @Author: jiangxz
 * @Date:   2020-01-08
 * @Version: V1.0
 */
public interface IPdUnitService extends IService<PdUnit> {
    
    List<PdUnit> queryList(PdUnit pdUnit);

    Page<PdUnit> queryList(Page<PdUnit> pageList, PdUnit pdUnit);
}
