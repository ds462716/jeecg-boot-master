package org.jeecg.modules.external.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.jeecg.modules.external.entity.HUserFingerFace;

import java.util.List;

/**
 * @Description:
 * @Author: mcb
 * @Date:   2020-07-09
 * @Version: V1.0
 */
public interface HUserFingerFaceMapper extends BaseMapper<HUserFingerFace> {


    public List<HUserFingerFace> queryHuserFingerFaceList(HUserFingerFace face);

}
