package org.jeecg.modules.pd.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.pd.entity.PdUsePackage;
import org.jeecg.modules.pd.entity.PdUsePackageDetail;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

/**
 * @Description: 定数包
 * @Author: zxh
 * @Date:   2020年4月21日08:55:20
 * @Version: V1.0
 */
public interface IPdUsePackageService extends IService<PdUsePackage> {

	/**
	 * 添加一对多
	 * 
	 */
	 void saveMain(PdUsePackage pdUsePackage, List<PdUsePackageDetail> pdUsePackageDetailList) ;
	
	/**
	 * 修改一对多
	 * 
	 */
	 void updateMain(PdUsePackage pdUsePackage, List<PdUsePackageDetail> pdUsePackageDetailList);
	
	/**
	 * 删除一对多
	 */
	 void delMain(String id);
	
	/**
	 * 批量删除一对多
	 */
	 void delBatchMain(Collection<? extends Serializable> idList);

	/**
	 * 查询列表
	 * @param pdUsePackage
	 * @return
	 */
	List<PdUsePackage> queryList(PdUsePackage pdUsePackage);

	/**
	 * 分页查询列表
	 * @param pageList
	 * @param pdUsePackage
	 * @return
	 */
	Page<PdUsePackage> queryList(Page<PdUsePackage> pageList, PdUsePackage pdUsePackage);
}
