package org.jeecg.modules.pd.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.jeecg.modules.pd.entity.PdUsePackage;

import java.util.List;

/**
 * @Description: 检验项目
 * @Author: zxh
 * @Date:   2020年4月21日08:57:11
 * @Version: V1.0
 */
public interface PdUsePackageMapper extends BaseMapper<PdUsePackage> {

    List<PdUsePackage> queryList(PdUsePackage pdUsePackage);
}
