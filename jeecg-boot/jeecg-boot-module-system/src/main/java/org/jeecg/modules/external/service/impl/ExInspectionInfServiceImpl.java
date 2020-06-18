package org.jeecg.modules.external.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.modules.external.entity.ExInspectionInf;
import org.jeecg.modules.external.mapper.ExInspectionInfMapper;
import org.jeecg.modules.external.service.IExInspectionInfService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Description: 检查项目表
 * @Author: jiangxz
 * @Date:   2020-04-23
 * @Version: V1.0
 */
@Service
public class ExInspectionInfServiceImpl extends ServiceImpl<ExInspectionInfMapper, ExInspectionInf> implements IExInspectionInfService {

    @Autowired
    private ExInspectionInfMapper exInspectionInfMapper;

    /**
     * 查询列表
     * @param page
     * @param exInspectionInf
     * @return
     */
    @Override
    public Page<ExInspectionInf> selectList(Page<ExInspectionInf> page, ExInspectionInf exInspectionInf) {
            return exInspectionInfMapper.selectListByPage(page,exInspectionInf);
    }




}
