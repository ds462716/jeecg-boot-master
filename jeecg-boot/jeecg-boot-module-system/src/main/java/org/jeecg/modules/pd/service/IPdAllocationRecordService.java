package org.jeecg.modules.pd.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.pd.entity.PdAllocationDetail;
import org.jeecg.modules.pd.entity.PdAllocationRecord;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

/**
 * @Description: 调拨记录表
 * @Author: jeecg-boot
 * @Date:   2020-02-25
 * @Version: V1.0
 */
public interface IPdAllocationRecordService extends IService<PdAllocationRecord> {


	/**
	 * 分页查询
	 * @param pageList
	 * @param allocationRecord
	 * @return
	 */
	Page<PdAllocationRecord> selectList(Page<PdAllocationRecord> pageList, PdAllocationRecord allocationRecord);



	/**
	 * 添加一对多
	 * 
	 */
	public void saveMain(PdAllocationRecord pdAllocationRecord, List<PdAllocationDetail> pdAllocationDetailList) ;
	
	/**
	 * 修改一对多
	 * 
	 */
	public void updateMain(PdAllocationRecord pdAllocationRecord, List<PdAllocationDetail> pdAllocationDetailList);
	
	/**
	 * 删除一对多
	 */
	public void delMain(String id);
	
	/**
	 * 批量删除一对多
	 */
	public void delBatchMain(Collection<? extends Serializable> idList);
	/**
	 * 用于调拨单单弹出选择框
	 * @param allocationRecord
	 * @return
	 */
	Page<PdAllocationRecord> chooseAllocationList(Page<PdAllocationRecord> pageList, PdAllocationRecord allocationRecord);

}
