package org.jeecg.modules.external.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.external.entity.ExDeductuinDosage;

/**
 * @Description: 试剂用量扣减记录表
 * @Author: jiangxz
 * @Date:   2020-05-22
 * @Version: V1.0
 */
public interface ExDeductuinDosageMapper extends BaseMapper<ExDeductuinDosage> {

    Page<ExDeductuinDosage> selectListByPage(Page<ExDeductuinDosage> page, @Param("entity") ExDeductuinDosage entity);

}
