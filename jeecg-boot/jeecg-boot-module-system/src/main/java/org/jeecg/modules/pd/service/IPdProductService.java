package org.jeecg.modules.pd.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.jeecg.common.api.vo.Result;
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

    IPage<PdProductPage> chooseProductList(Page<PdProductPage> pageList, PdProduct pdProduct);

    IPage<PdProduct> selectList(Page<PdProduct> pageList, PdProduct pdProduct);

    void updateProduct(PdProduct pdProduct);

    void saveProduct(PdProduct pdProduct);

    Result<Map>  getScanCode(String Barcode1, String Barcode2, Result<Map> result);

    Result<List<PdProductStock>> getStocks(String productBarCode, String Barcode2, String productFlag,String barCodeType,String nestatStatus,Result<List<PdProductStock>> result);

    void editChargeCodeBatch(String ids, String chargeCode);

    List<PdProduct> selectList(PdProduct pdProduct);

    List<PdProduct> verify(PdProduct pdProduct);

    List<PdProduct> selectListByCT(PdProduct pdProduct);

    List<PdProduct> selectListByCTs(Map<String,Object> map);

    List<PdProduct> selectListByChargeCode(PdProduct entity);

    Result<Object> importExcel(Map<String, MultipartFile> fileMap);

    Result<Object> deleteV(String id);

    Result<Object> deleteBatchV(String ids);

    Result<Object>  isDisabledNumber(PdProduct pdProduct);

    void updateValidityFlag(PdProduct pdProduct);

    Result<Object> importExcelReagents(Map<String, MultipartFile> fileMap);

    Result<List<PdProductStock>> openingQuotation(String barcode,String instrCode, Result<List<PdProductStock>> result);

    Result<PdProductStock> uniqueScanCodeUrl(String barcode, String productFlag,String nestatStatus,Result<PdProductStock> result);

    Result<List<PdProductStock>> closeIngQuotation(String barcode,String closeRemarks,String instrCode, Result<List<PdProductStock>> result);

}
