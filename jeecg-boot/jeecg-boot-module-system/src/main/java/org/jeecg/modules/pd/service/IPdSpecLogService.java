package org.jeecg.modules.pd.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.pd.entity.PdSpecLog;

/**
 * @Description: 规格数量清零操作日志表
 * @Author: jiangxz
 * @Date:   2020-05-20
 * @Version: V1.0
 */
public interface IPdSpecLogService extends IService<PdSpecLog> {

    IPage<PdSpecLog> selectList(Page<PdSpecLog> pageList, PdSpecLog pdSpecLog);

}
