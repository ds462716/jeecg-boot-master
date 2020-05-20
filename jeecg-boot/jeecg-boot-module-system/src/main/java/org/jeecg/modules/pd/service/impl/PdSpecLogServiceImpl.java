package org.jeecg.modules.pd.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.modules.pd.entity.PdSpecLog;
import org.jeecg.modules.pd.mapper.PdSpecLogMapper;
import org.jeecg.modules.pd.service.IPdSpecLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Description: 规格数量清零操作日志表
 * @Author: jiangxz
 * @Date:   2020-05-20
 * @Version: V1.0
 */
@Service
public class PdSpecLogServiceImpl extends ServiceImpl<PdSpecLogMapper, PdSpecLog> implements IPdSpecLogService {
    @Autowired
    private PdSpecLogMapper pdSpecLogMapper;


    @Override
    public Page<PdSpecLog> selectList(Page<PdSpecLog> page, PdSpecLog pdSpecLog) {
        return page.setRecords(pdSpecLogMapper.selectList(pdSpecLog));
    }
}
