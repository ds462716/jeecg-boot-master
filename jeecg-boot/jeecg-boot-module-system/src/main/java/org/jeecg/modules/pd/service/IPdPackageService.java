package org.jeecg.modules.pd.service;

import org.jeecg.modules.pd.entity.PdPackageDetail;
import org.jeecg.modules.pd.entity.PdPackage;
import com.baomidou.mybatisplus.extension.service.IService;
import java.io.Serializable;
import java.util.Collection;
import java.util.List;

/**
 * @Description: 定数包
 * @Author: jiangxz
 * @Date:   2020-02-02
 * @Version: V1.0
 */
public interface IPdPackageService extends IService<PdPackage> {

	/**
	 * 添加一对多
	 * 
	 */
	public void saveMain(PdPackage pdPackage, List<PdPackageDetail> pdPackageDetailList) ;
	
	/**
	 * 修改一对多
	 * 
	 */
	public void updateMain(PdPackage pdPackage, List<PdPackageDetail> pdPackageDetailList);
	
	/**
	 * 删除一对多
	 */
	public void delMain(String id);
	
	/**
	 * 批量删除一对多
	 */
	public void delBatchMain(Collection<? extends Serializable> idList);
	
}
