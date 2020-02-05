package org.jeecg.modules.pd.mapper;

import java.util.List;
import org.jeecg.modules.pd.entity.PdPackageDetail;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * @Description: 定数包明细
 * @Author: jiangxz
 * @Date:   2020-02-02
 * @Version: V1.0
 */
public interface PdPackageDetailMapper extends BaseMapper<PdPackageDetail> {

	public boolean deleteByMainId(@Param("mainId") String mainId);
    
	public List<PdPackageDetail> selectByMainId(@Param("mainId") String mainId);
}
