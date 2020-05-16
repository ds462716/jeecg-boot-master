package org.jeecg.modules.external.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.modules.external.entity.ExInspectionItems;
import org.jeecg.modules.external.mapper.ExInspectionItemsMapper;
import org.jeecg.modules.external.service.IExInspectionItemsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Description: 检查项目表
 * @Author: jiangxz
 * @Date:   2020-04-23
 * @Version: V1.0
 */
@Service
public class ExInspectionItemsServiceImpl extends ServiceImpl<ExInspectionItemsMapper, ExInspectionItems> implements IExInspectionItemsService {

    @Autowired
    private ExInspectionItemsMapper exInspectionItemsMapper;

    @Override
    public List<ExInspectionItems> selectList(ExInspectionItems exInspectionItems) {
        return exInspectionItemsMapper.selectList(exInspectionItems);
    }

    @Override
    public List<String> selectListIds() {
        return exInspectionItemsMapper.selectListIds();
    }
}
