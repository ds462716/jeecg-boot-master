package org.jeecg.modules.pd.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.pd.entity.PdCategory;
import org.jeecg.modules.pd.entity.PdProduct;
import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.pd.entity.PdProductStock;
import org.jeecg.modules.pd.vo.PdProductPage;
import org.springframework.web.multipart.MultipartFile;

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

    void editChargeCodeBatch(String ids, String chargeCode);

    List<PdProduct> selectList(PdProduct pdProduct);

    List<PdProduct> verify(PdProduct pdProduct);

    List<PdProduct> selectListByCT(PdProduct pdProduct);

    List<PdProduct> selectListByCTs(Map<String,Object> map);

    Result<Object> importExcel(Map<String, MultipartFile> fileMap);

    Result<Object> deleteV(String id);

    Result<Object> deleteBatchV(String ids);

    Result<Object>  isDisabledNumber(PdProduct pdProduct);

    void updateValidityFlag(PdProduct pdProduct);

    Result<Object> importExcelReagents(Map<String, MultipartFile> fileMap);
}
