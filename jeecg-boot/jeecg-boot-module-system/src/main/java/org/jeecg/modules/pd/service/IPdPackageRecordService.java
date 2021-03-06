package org.jeecg.modules.pd.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.pd.entity.PdPackageRecordDetail;
import org.jeecg.modules.pd.entity.PdPackageRecord;
import com.baomidou.mybatisplus.extension.service.IService;
import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * @Description: pd_package_record
 * @Author: jiangxz
 * @Date:   2020-04-22
 * @Version: V1.0
 */
public interface IPdPackageRecordService extends IService<PdPackageRecord> {

	/**
	 * 添加一对多
	 * 
	 */
	Map<String, String> saveMain(PdPackageRecord pdPackageRecord,List<PdPackageRecordDetail> pdPackageRecordDetailList) ;
	
	/**
	 * 修改一对多
	 * 
	 */
	public void updateMain(PdPackageRecord pdPackageRecord,List<PdPackageRecordDetail> pdPackageRecordDetailList);
	
	/**
	 * 删除一对多
	 */
	public void delMain (String id);
	
	/**
	 * 批量删除一对多
	 */
	public String delBatchMain (Collection<? extends Serializable> idList);

	/**
	 * 查询列表
	 * @param pdPackageRecord
	 * @return
	 */
	List<PdPackageRecord> queryList(PdPackageRecord pdPackageRecord);

	/**
	 * 分页查询列表
	 * @param pageList
	 * @param pdPackageRecord
	 * @return
	 */
	IPage<PdPackageRecord> queryList(Page<PdPackageRecord> pageList, PdPackageRecord pdPackageRecord);

	/**
	 * 套包打包记录扫码
	 * @param Barcode1
	 * @param result
	 * @return
	 */
	Result<Map> packageRecordScanCode(String Barcode1, Result<Map> result);
}
