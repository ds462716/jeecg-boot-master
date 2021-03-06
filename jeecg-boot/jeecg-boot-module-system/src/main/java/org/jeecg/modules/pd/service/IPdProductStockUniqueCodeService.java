package org.jeecg.modules.pd.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.pd.entity.PdProductStock;
import org.jeecg.modules.pd.entity.PdProductStockUniqueCode;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @Description: 库存关联条码表
 * @Author: jiangxz
 * @Date:   2020-05-26
 * @Version: V1.0
 */
public interface IPdProductStockUniqueCodeService extends IService<PdProductStockUniqueCode> {

    Result<List<PdProductStockUniqueCode>> uniqueCodeGeneration(PdProductStockUniqueCode pdProductStockUniqueCode);

    Result<List<PdProductStockUniqueCode>> batchCodeGeneration(List<String> asList);

    Result<Object> deleteCode(String id);

    IPage<PdProductStockUniqueCode> selectList(Page<PdProductStockUniqueCode> page, PdProductStockUniqueCode pdProductStockUniqueCode);

    List<PdProductStockUniqueCode> selectList(PdProductStockUniqueCode pdProductStockUniqueCode);

    List<PdProductStock> selectListByGroup(PdProductStockUniqueCode pdProductStockUniqueCode);

    void updatePrintNum(List<PdProductStockUniqueCode> pdProductStockUniqueCodes);

    IPage<PdProductStockUniqueCode> findList(Page<PdProductStockUniqueCode> page, PdProductStockUniqueCode pdProductStockUniqueCode);


    String queryUniqueCode(PdProductStockUniqueCode pdProductStockUniqueCode);

    void foreignSaveUniqueCode(PdProductStockUniqueCode psc);
}
