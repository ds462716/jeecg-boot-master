package org.jeecg.modules.pd.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.jeecg.modules.pd.entity.PdStoreroomPatrolDetail;
import org.jeecg.modules.pd.entity.PdStoreroomPatrol;
import com.baomidou.mybatisplus.extension.service.IService;
import java.io.Serializable;
import java.util.Collection;
import java.util.List;

/**
 * @Description: pd_storeroom_patrol
 * @Author: jiangxz
 * @Date:   2020-04-07
 * @Version: V1.0
 */
public interface IPdStoreroomPatrolService extends IService<PdStoreroomPatrol> {

	/**
	 * 查询巡查列表
	 * @param pdStoreroomPatrol
	 * @return
	 */
	List<PdStoreroomPatrol> selectList(PdStoreroomPatrol pdStoreroomPatrol);

	/**
	 * 分页查询巡查列表
	 * @param pageList
	 * @param pdStoreroomPatrol
	 * @return
	 */
	Page<PdStoreroomPatrol> selectList(Page<PdStoreroomPatrol> pageList, PdStoreroomPatrol pdStoreroomPatrol);

	/**
	 * 添加一对多
	 * 
	 */
	public void saveMain(PdStoreroomPatrol pdStoreroomPatrol,List<PdStoreroomPatrolDetail> pdStoreroomPatrolDetailList) ;
	
	/**
	 * 修改一对多
	 * 
	 */
	public void updateMain(PdStoreroomPatrol pdStoreroomPatrol,List<PdStoreroomPatrolDetail> pdStoreroomPatrolDetailList);
	
	/**
	 * 删除一对多
	 */
	public void delMain (String id);
	
	/**
	 * 批量删除一对多
	 */
	public void delBatchMain (Collection<? extends Serializable> idList);
	
}
