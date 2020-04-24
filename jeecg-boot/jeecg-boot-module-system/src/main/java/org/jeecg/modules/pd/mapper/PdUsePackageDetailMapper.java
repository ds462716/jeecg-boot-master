package org.jeecg.modules.pd.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.pd.entity.PdUsePackageDetail;

import java.util.List;

/**
 * @Description: 检验项目
 * @Author: zxh
 * @Date:   2020年4月21日09:01:02
 * @Version: V1.0
 */
public interface PdUsePackageDetailMapper extends BaseMapper<PdUsePackageDetail> {

	 boolean deleteByMainId(@Param("mainId") String mainId);
    
	 List<PdUsePackageDetail> selectByMainId(@Param("mainId") String mainId);

	 List<PdUsePackageDetail> selectList(PdUsePackageDetail pdUsePackageDetail);

}
