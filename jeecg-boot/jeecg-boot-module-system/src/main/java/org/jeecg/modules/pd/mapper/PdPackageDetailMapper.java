package org.jeecg.modules.pd.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.pd.entity.PdPackageDetail;

import java.util.List;

/**
 * @Description: 套包明细
 * @Author: jiangxz
 * @Date:   2020-02-02
 * @Version: V1.0
 */
public interface PdPackageDetailMapper extends BaseMapper<PdPackageDetail> {

	public boolean deleteByMainId(@Param("mainId") String mainId);
    
	public List<PdPackageDetail> selectByMainId(@Param("mainId") String mainId);

	public List<PdPackageDetail> selectList(PdPackageDetail packageDetail);

}
