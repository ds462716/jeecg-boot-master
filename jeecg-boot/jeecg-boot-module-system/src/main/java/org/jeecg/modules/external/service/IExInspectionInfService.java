package org.jeecg.modules.external.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.external.entity.ExInspectionInf;

/**
 * @Description: 检查项目表
 * @Author: jiangxz
 * @Date:   2020-04-23
 * @Version: V1.0
 */
public interface IExInspectionInfService extends IService<ExInspectionInf> {

    /**
     * 分页查询
     * @param pageList
     * @param exInspectionInf
     * @return
     */
    IPage<ExInspectionInf> selectList(Page<ExInspectionInf> pageList, ExInspectionInf exInspectionInf);



}
