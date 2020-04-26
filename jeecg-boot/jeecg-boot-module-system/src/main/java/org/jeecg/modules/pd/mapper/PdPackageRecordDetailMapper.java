package org.jeecg.modules.pd.mapper;

import java.util.List;
import org.jeecg.modules.pd.entity.PdPackageRecordDetail;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * @Description: pd_package_record_detail
 * @Author: jiangxz
 * @Date:   2020-04-22
 * @Version: V1.0
 */
public interface PdPackageRecordDetailMapper extends BaseMapper<PdPackageRecordDetail> {

	public boolean deleteByMainId(@Param("mainId") String mainId);
    
	public List<PdPackageRecordDetail> selectByMainId(@Param("mainId") String mainId);
}
