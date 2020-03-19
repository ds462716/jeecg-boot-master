package org.jeecg.modules.pd.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.jeecg.modules.pd.entity.PdGroup;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @Description: 产品组别
 * @Author: zxh
 * @Date:   2020-01-14
 * @Version: V1.0
 */
public interface IPdGroupService extends IService<PdGroup> {

    List<PdGroup> selectList(PdGroup pdGroup);

    Page<PdGroup> selectList(Page<PdGroup> pageList, PdGroup pdGroup);

    List<PdGroup> verify(PdGroup pdGroup);
}
