package org.jeecg.modules.pd.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.jeecg.modules.pd.entity.PdAutoOrderInf;

import java.util.List;

/**
 * @Description:  自动补货
 * @Author: jeecg-boot
 * @Date:   2020-02-25
 * @Version: V1.0
 */
public interface PdAutoOrderMapper extends BaseMapper<PdAutoOrderInf> {

	List<PdAutoOrderInf> queryList(PdAutoOrderInf autoOrderInf);
}
