package org.jeecg.modules.external.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.external.entity.PdBottleInf;

import java.util.List;

/**
 * @Description: 开闭瓶记录表
 * @Author: jiangxz
 * @Date:   2020-05-26
 * @Version: V1.0
 */
public interface PdBottleInfMapper extends BaseMapper<PdBottleInf> {

    Page<PdBottleInf> selectList(Page<PdBottleInf> page, @Param("entity") PdBottleInf entity);

    List<PdBottleInf> selectList(@Param("entity") PdBottleInf entity);
    /**
     * 获取一条记录
     * @param pdBottleInf
     * @return
     */
    PdBottleInf getOne(PdBottleInf pdBottleInf);

    /**
     * 更新使用規格数量
     * @param pdBottleInf
     */
    public void updateSpecNum(PdBottleInf pdBottleInf);

}
