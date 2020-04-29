package org.jeecg.modules.pd.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.jeecg.modules.pd.entity.PdRejectedDetail;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @Description: pd_rejected_detail
 * @Author: jiangxz
 * @Date:   2020-03-16
 * @Version: V1.0
 */
public interface IPdRejectedDetailService extends IService<PdRejectedDetail> {

    List<PdRejectedDetail> selectByMainId(PdRejectedDetail pdRejectedDetail);

    List<PdRejectedDetail> selectList(PdRejectedDetail pdRejectedDetail);

    IPage<PdRejectedDetail> selectList(Page<PdRejectedDetail> pageList, PdRejectedDetail pdRejectedDetail);
}
