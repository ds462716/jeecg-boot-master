package org.jeecg.modules.pd.service;

import org.jeecg.modules.pd.entity.PdPackageRecordDetail;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;

/**
 * @Description: pd_package_record_detail
 * @Author: jiangxz
 * @Date:   2020-04-22
 * @Version: V1.0
 */
public interface IPdPackageRecordDetailService extends IService<PdPackageRecordDetail> {

	public List<PdPackageRecordDetail> selectByMainId(String mainId);
}
