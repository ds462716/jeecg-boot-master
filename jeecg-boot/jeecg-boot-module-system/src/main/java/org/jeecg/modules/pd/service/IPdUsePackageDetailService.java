package org.jeecg.modules.pd.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.pd.entity.PdUsePackageDetail;

import java.util.List;

/**
 * @Description: 使用包明细
 * @Author: zxh
 * @Date:   2020年4月21日09:09:04
 * @Version: V1.0
 */
public interface IPdUsePackageDetailService extends IService<PdUsePackageDetail> {

 	List<PdUsePackageDetail> selectByMainId(String mainId);

	Page<PdUsePackageDetail> selectList(Page<PdUsePackageDetail> pageList, PdUsePackageDetail usePackageDetail);

	List<PdUsePackageDetail> queryPdUsePackageList(PdUsePackageDetail usePackageDetail);

}
