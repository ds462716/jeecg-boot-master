package org.jeecg.modules.pd.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.pd.entity.PdProduct;
import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.pd.entity.PdProductStock;
import org.jeecg.modules.pd.vo.PdProductPage;

import java.util.List;
import java.util.Map;

/**
 * @Description: pd_product
 * @Author: zxh
 * @Date:   2020-02-05
 * @Version: V1.0
 */
public interface IPdProductService extends IService<PdProduct> {

    Page<PdProductPage> chooseProductList(Page<PdProductPage> pageList, PdProduct pdProduct);

    Page<PdProduct> selectList(Page<PdProduct> pageList, PdProduct pdProduct);

    void updateProduct(PdProduct pdProduct);

    void saveProduct(PdProduct pdProduct);

    Result<Map>  getScanCode(String Barcode1, String Barcode2, Result<Map> result);

    Result<List<PdProductStock>> getStocks(String productBarCode, String Barcode2, Result<List<PdProductStock>> result);
}
