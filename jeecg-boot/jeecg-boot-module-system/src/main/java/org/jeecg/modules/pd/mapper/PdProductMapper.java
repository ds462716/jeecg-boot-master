package org.jeecg.modules.pd.mapper;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.pd.entity.PdProduct;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.jeecg.modules.pd.vo.PdProductPage;

/**
 * @Description: pd_product
 * @Author: zxh
 * @Date:   2020-02-05
 * @Version: V1.0
 */
public interface PdProductMapper extends BaseMapper<PdProduct> {

    Page<PdProductPage> chooseProductList(Page<PdProductPage> page, @Param("entity") PdProduct entity);

    List<PdProduct> selectList(@Param("entity") PdProduct pdProduct);

    void updateChargeCode(PdProduct pdProduct);

    List<PdProduct> verify(PdProduct pdProduct);

    List<PdProduct> selectListByCT(PdProduct pdProduct);

    List<PdProduct> selectListByCTs(@Param("parMap")Map<String,Object> map);

    List<PdProduct> selectListByChargeCode(@Param("entity") PdProduct entity);

    void updateValidityFlag(PdProduct pdProduct);

    Page<PdProduct> selectListByPage(Page<PdProduct> page, @Param("entity") PdProduct entity);

    Page<PdProduct> selectListForHisChargeByPage(Page<PdProduct> page, @Param("entity") PdProduct entity);

}
