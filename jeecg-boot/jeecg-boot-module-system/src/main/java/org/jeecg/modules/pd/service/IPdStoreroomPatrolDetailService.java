package org.jeecg.modules.pd.service;

import org.jeecg.modules.pd.entity.PdStoreroomPatrolDetail;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;

/**
 * @Description: pd_storeroom_patrol_detail
 * @Author: jiangxz
 * @Date:   2020-04-07
 * @Version: V1.0
 */
public interface IPdStoreroomPatrolDetailService extends IService<PdStoreroomPatrolDetail> {

	public List<PdStoreroomPatrolDetail> selectByMainId(String mainId);
}
