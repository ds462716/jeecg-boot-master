package org.jeecg.modules.pd.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.jeecg.modules.pd.entity.PdRejected;
import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.pd.entity.PdRejectedDetail;

import java.util.List;

/**
 * @Description: pd_rejected
 * @Author: jiangxz
 * @Date:   2020-03-16
 * @Version: V1.0
 */
public interface IPdRejectedService extends IService<PdRejected> {
    /**
     * 添加一对多
     *
     */
    public void saveMain(PdRejected pdRejected, List<PdRejectedDetail> pdRejectedDetailList) ;

    /**
     * 查询列表
     * @param pdRejected
     * @return
     */
    List<PdRejected> queryList(PdRejected pdRejected);

    /**
     * 分页查询列表
     * @param pageList
     * @param pdRejected
     * @return
     */
    IPage<PdRejected> queryList(Page<PdRejected> pageList, PdRejected pdRejected);

}
