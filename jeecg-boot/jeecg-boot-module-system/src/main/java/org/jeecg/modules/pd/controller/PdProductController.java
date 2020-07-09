package org.jeecg.modules.pd.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.constant.PdConstant;
import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.pd.entity.PdProduct;
import org.jeecg.modules.pd.entity.PdProductStock;
import org.jeecg.modules.pd.service.IPdProductService;
import org.jeecg.modules.pd.util.BarCodeUtil;
import org.jeecg.modules.pd.util.FileUploadUtil;
import org.jeecg.modules.pd.vo.PdProductPage;
import org.jeecg.modules.pd.vo.PdProductReagents;
import org.jeecgframework.poi.excel.def.NormalExcelConstants;
import org.jeecgframework.poi.excel.entity.ExportParams;
import org.jeecgframework.poi.excel.view.JeecgEntityExcelView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @Description: pd_product
 * @Author: zxh
 * @Date: 2020-02-05
 * @Version: V1.0
 */
@RestController
@RequestMapping("/pd/pdProduct")
@Slf4j
public class PdProductController extends JeecgController<PdProduct, IPdProductService> {
    @Autowired
    private IPdProductService pdProductService;

    /**
     * 分页列表查询
     *
     * @param pdProduct
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @GetMapping(value = "/list")
    public Result<?> queryPageList(PdProduct pdProduct,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                   HttpServletRequest req) {
        Page<PdProduct> page = new Page<PdProduct>(pageNo, pageSize);
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        pdProduct.setDepartParentId(sysUser.getDepartParentId());
        IPage<PdProduct> pageList = pdProductService.selectList(page, pdProduct);//
        return Result.ok(pageList);
    }

    /**
     * 关联ex_his_charge_inf表的分页查询
     * @param pdProduct
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @GetMapping(value = "/listForHisCharge")
    public Result<?> queryPageListForHisCharge(PdProduct pdProduct,
                                               @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                               @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                               HttpServletRequest req) {
        Page<PdProduct> page = new Page<PdProduct>(pageNo, pageSize);
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        pdProduct.setDepartParentId(sysUser.getDepartParentId());
        pdProduct.setStatus(PdConstant.DISABLE_ENABLE_STATUS_0);//只查启用
        IPage<PdProduct> pageList = pdProductService.selectListForHisChargeByPage(page, pdProduct);
        return Result.ok(pageList);
    }

    /**
     * 添加
     *
     * @param pdProduct
     * @return
     */
    @PostMapping(value = "/add")
    public Result<?> add(@RequestBody PdProduct pdProduct) {
        pdProductService.save(pdProduct);
        return Result.ok("添加成功！");
    }

    /**
     * 包含文件上传功能
     *
     * @param pdProduct
     * @return
     */
    @PostMapping(value = "/save")
    public Result<?> save(@ModelAttribute PdProduct pdProduct, @RequestParam MultipartFile[] licenceSiteUp0,
                          @RequestParam MultipartFile[] licenceSiteUp1,
                          @RequestParam MultipartFile[] licenceSiteUp2, @RequestParam MultipartFile[] licenceSiteUp3,
                          @RequestParam MultipartFile[] licenceSiteUp4, @RequestParam MultipartFile[] licenceSiteUp5,
                          @RequestParam MultipartFile[] licenceSiteUp6, @RequestParam MultipartFile[] licenceSiteUp7,
                          @RequestParam MultipartFile[] licenceSiteUp8, @RequestParam MultipartFile[] licenceSiteUp9,
                          @RequestParam MultipartFile[] licenceSiteUp10, @RequestParam MultipartFile[] licenceSiteUp11
    ) {
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        pdProduct.setDepartParentId(sysUser.getDepartParentId());
        pdProduct.setNumber(BarCodeUtil.trimStr(pdProduct.getNumber()));
        //校验产品编号是否唯
        List<PdProduct> obj = pdProductService.verify(pdProduct);
        if (obj != null && obj.size() > 0) {
            return Result.error("产品编号已存在");
        }
        //存入图片
        if (!FileUploadUtil.isImgEmpty(licenceSiteUp0)) {
            String filePath = FileUploadUtil.upload(licenceSiteUp0);
            pdProduct.setLicenceSite0(filePath);
        }
        if (!FileUploadUtil.isImgEmpty(licenceSiteUp1)) {
            String filePath = FileUploadUtil.upload(licenceSiteUp1);
            pdProduct.setLicenceSite1(filePath);
        }
        if (!FileUploadUtil.isImgEmpty(licenceSiteUp2)) {
            String filePath = FileUploadUtil.upload(licenceSiteUp2);
            pdProduct.setLicenceSite2(filePath);
        }
        if (!FileUploadUtil.isImgEmpty(licenceSiteUp3)) {
            String filePath = FileUploadUtil.upload(licenceSiteUp3);
            pdProduct.setLicenceSite3(filePath);
        }
        if (!FileUploadUtil.isImgEmpty(licenceSiteUp4)) {
            String filePath = FileUploadUtil.upload(licenceSiteUp4);
            pdProduct.setLicenceSite4(filePath);
        }
        if (!FileUploadUtil.isImgEmpty(licenceSiteUp5)) {
            String filePath = FileUploadUtil.upload(licenceSiteUp5);
            pdProduct.setLicenceSite5(filePath);
        }
        if (!FileUploadUtil.isImgEmpty(licenceSiteUp6)) {
            String filePath = FileUploadUtil.upload(licenceSiteUp6);
            pdProduct.setLicenceSite6(filePath);
        }
        if (!FileUploadUtil.isImgEmpty(licenceSiteUp7)) {
            String filePath = FileUploadUtil.upload(licenceSiteUp7);
            pdProduct.setLicenceSite7(filePath);
        }
        if (!FileUploadUtil.isImgEmpty(licenceSiteUp8)) {
            String filePath = FileUploadUtil.upload(licenceSiteUp8);
            pdProduct.setLicenceSite8(filePath);
        }
        if (!FileUploadUtil.isImgEmpty(licenceSiteUp9)) {
            String filePath = FileUploadUtil.upload(licenceSiteUp9);
            pdProduct.setLicenceSite9(filePath);
        }
        if (!FileUploadUtil.isImgEmpty(licenceSiteUp10)) {
            String filePath = FileUploadUtil.upload(licenceSiteUp10);
            pdProduct.setLicenceSite10(filePath);
        }
        if (!FileUploadUtil.isImgEmpty(licenceSiteUp11)) {
            String filePath = FileUploadUtil.upload(licenceSiteUp11);
            pdProduct.setLicenceSite11(filePath);
        }
        pdProduct.setValidityFlag(PdConstant.PD_STATE_0);
        pdProductService.saveProduct(pdProduct);
        return Result.ok("添加成功！");
    }

    /**
     * 包含文件上传功能
     *
     * @param pdProduct
     * @return
     */
    @PostMapping(value = "/update")
    public Result<?> update(@ModelAttribute PdProduct pdProduct, @RequestParam MultipartFile[] licenceSiteUp0,
                            @RequestParam MultipartFile[] licenceSiteUp1,
                            @RequestParam MultipartFile[] licenceSiteUp2, @RequestParam MultipartFile[] licenceSiteUp3,
                            @RequestParam MultipartFile[] licenceSiteUp4, @RequestParam MultipartFile[] licenceSiteUp5,
                            @RequestParam MultipartFile[] licenceSiteUp6, @RequestParam MultipartFile[] licenceSiteUp7,
                            @RequestParam MultipartFile[] licenceSiteUp8, @RequestParam MultipartFile[] licenceSiteUp9,
                            @RequestParam MultipartFile[] licenceSiteUp10, @RequestParam MultipartFile[] licenceSiteUp11
    ) {
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        pdProduct.setDepartParentId(sysUser.getDepartParentId());
        //校验产品编号是否唯
        pdProduct.setNumber(BarCodeUtil.trimStr(pdProduct.getNumber()));
        List<PdProduct> obj = pdProductService.verify(pdProduct);
        if (obj != null && obj.size() > 0) {
            return Result.error("产品编号已存在");
        }
        //存入图片
        if (!FileUploadUtil.isImgEmpty(licenceSiteUp0)) {
            String filePath = FileUploadUtil.upload(licenceSiteUp0);
            if (pdProduct.getLicenceSite0() != null && !"".equals(pdProduct.getLicenceSite0())) {
                //先删除再更新
                FileUploadUtil.deleteFile(pdProduct.getLicenceSite0());
            }
            if (!"".equals(filePath)) {
                pdProduct.setLicenceSite0(filePath);
            }
        }
        if (!FileUploadUtil.isImgEmpty(licenceSiteUp1)) {
            String filePath = FileUploadUtil.upload(licenceSiteUp1);
            if (pdProduct.getLicenceSite1() != null && !"".equals(pdProduct.getLicenceSite1())) {
                //先删除再更新
                FileUploadUtil.deleteFile(pdProduct.getLicenceSite1());
            }
            if (!"".equals(filePath)) {
                pdProduct.setLicenceSite1(filePath);
            }
        }
        if (!FileUploadUtil.isImgEmpty(licenceSiteUp2)) {
            String filePath = FileUploadUtil.upload(licenceSiteUp2);
            if (pdProduct.getLicenceSite2() != null && !"".equals(pdProduct.getLicenceSite2())) {
                //先删除再更新
                FileUploadUtil.deleteFile(pdProduct.getLicenceSite2());
            }
            if (!"".equals(filePath)) {
                pdProduct.setLicenceSite2(filePath);
            }
        }
        if (!FileUploadUtil.isImgEmpty(licenceSiteUp3)) {
            String filePath = FileUploadUtil.upload(licenceSiteUp3);
            if (pdProduct.getLicenceSite3() != null && !"".equals(pdProduct.getLicenceSite3())) {
                //先删除再更新
                FileUploadUtil.deleteFile(pdProduct.getLicenceSite3());
            }
            if (!"".equals(filePath)) {
                pdProduct.setLicenceSite3(filePath);
            }
        }
        if (!FileUploadUtil.isImgEmpty(licenceSiteUp4)) {
            String filePath = FileUploadUtil.upload(licenceSiteUp4);
            if (pdProduct.getLicenceSite4() != null && !"".equals(pdProduct.getLicenceSite4())) {
                //先删除再更新
                FileUploadUtil.deleteFile(pdProduct.getLicenceSite4());
            }
            if (!"".equals(filePath)) {
                pdProduct.setLicenceSite4(filePath);
            }
        }
        if (!FileUploadUtil.isImgEmpty(licenceSiteUp5)) {
            String filePath = FileUploadUtil.upload(licenceSiteUp5);
            if (pdProduct.getLicenceSite5() != null && !"".equals(pdProduct.getLicenceSite5())) {
                //先删除再更新
                FileUploadUtil.deleteFile(pdProduct.getLicenceSite5());
            }
            if (!"".equals(filePath)) {
                pdProduct.setLicenceSite5(filePath);
            }
        }
        if (!FileUploadUtil.isImgEmpty(licenceSiteUp6)) {
            String filePath = FileUploadUtil.upload(licenceSiteUp6);
            if (pdProduct.getLicenceSite6() != null && !"".equals(pdProduct.getLicenceSite6())) {
                //先删除再更新
                FileUploadUtil.deleteFile(pdProduct.getLicenceSite6());
            }
            if (!"".equals(filePath)) {
                pdProduct.setLicenceSite6(filePath);
            }
        }
        if (!FileUploadUtil.isImgEmpty(licenceSiteUp7)) {
            String filePath = FileUploadUtil.upload(licenceSiteUp7);
            if (pdProduct.getLicenceSite7() != null && !"".equals(pdProduct.getLicenceSite7())) {
                //先删除再更新
                FileUploadUtil.deleteFile(pdProduct.getLicenceSite7());
            }
            if (!"".equals(filePath)) {
                pdProduct.setLicenceSite7(filePath);
            }
        }
        if (!FileUploadUtil.isImgEmpty(licenceSiteUp8)) {
            String filePath = FileUploadUtil.upload(licenceSiteUp8);
            if (pdProduct.getLicenceSite8() != null && !"".equals(pdProduct.getLicenceSite8())) {
                //先删除再更新
                FileUploadUtil.deleteFile(pdProduct.getLicenceSite8());
            }
            if (!"".equals(filePath)) {
                pdProduct.setLicenceSite8(filePath);
            }
        }
        if (!FileUploadUtil.isImgEmpty(licenceSiteUp9)) {
            String filePath = FileUploadUtil.upload(licenceSiteUp9);
            if (pdProduct.getLicenceSite9() != null && !"".equals(pdProduct.getLicenceSite9())) {
                //先删除再更新
                FileUploadUtil.deleteFile(pdProduct.getLicenceSite9());
            }
            if (!"".equals(filePath)) {
                pdProduct.setLicenceSite9(filePath);
            }
        }
        if (!FileUploadUtil.isImgEmpty(licenceSiteUp10)) {
            String filePath = FileUploadUtil.upload(licenceSiteUp10);
            if (pdProduct.getLicenceSite10() != null && !"".equals(pdProduct.getLicenceSite10())) {
                //先删除再更新
                FileUploadUtil.deleteFile(pdProduct.getLicenceSite10());
            }
            if (!"".equals(filePath)) {
                pdProduct.setLicenceSite10(filePath);
            }
        }
        if (!FileUploadUtil.isImgEmpty(licenceSiteUp11)) {
            String filePath = FileUploadUtil.upload(licenceSiteUp11);
            if (pdProduct.getLicenceSite11() != null && !"".equals(pdProduct.getLicenceSite11())) {
                //先删除再更新
                FileUploadUtil.deleteFile(pdProduct.getLicenceSite11());
            }
            if (!"".equals(filePath)) {
                pdProduct.setLicenceSite11(filePath);
            }
        }
        pdProductService.updateProduct(pdProduct);
        return Result.ok("编辑成功！");
    }

    /**
     * 判断产品编号是否禁用
     *
     * @param pdProduct
     * @param request
     * @return
     */
    @RequestMapping(value = "/isDisabledNumber", method = RequestMethod.GET)
    public Result<Object> isDisabledNumber(PdProduct pdProduct, HttpServletRequest request) {
        Result<Object> bl = pdProductService.isDisabledNumber(pdProduct);
        return bl;
    }


    /**
     * 编辑
     *
     * @param pdProduct
     * @return
     */
    @PutMapping(value = "/edit")
    public Result<?> edit(@RequestBody PdProduct pdProduct) {
        pdProductService.updateById(pdProduct);
        return Result.ok("编辑成功!");
    }

    /**
     * 入库条码规则解析
     *
     * @param Barcode1
     * @param Barcode2
     * @param req
     * @return
     */
    @PostMapping(value = "scanCode")
    public Result<Map> scanCode(String Barcode1, String Barcode2,
                                HttpServletRequest req) {
        Result<Map> result = new Result<Map>();
        try {
            result = pdProductService.getScanCode(Barcode1, Barcode2, result);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            result.setCode(500);
            result.setMessage("条码格式不正确");
        }
        return result;
    }

    /**
     * 唯一码扫码
     *
     * @param Barcode
     * @param req
     * @return
     */
    @PostMapping(value = "uniqueScanCodeUrl")
    public Result<?> uniqueScanCodeUrl(String Barcode, String productFlag, String nestatStatus) {
        Result<PdProductStock> result = new Result<>();
        try {
            result = pdProductService.uniqueScanCodeUrl(Barcode, productFlag != null ? productFlag : "", nestatStatus != null ? nestatStatus : "", result);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            result.setCode(500);
            result.setMessage("条码格式不正确");
        }
        return result;
    }

    /**
     * 开瓶
     *
     * @param Barcode
     * @return
     */
    @PostMapping(value = "openingQuotation")
    public Result<?> openingQuotation(String Barcode, String instrCode) {
        Result<List<PdProductStock>> result = new Result<>();
        try {
            result = pdProductService.openingQuotation(Barcode, instrCode, result);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            result.setCode(500);
            result.setMessage("条码格式不正确");
        }
        return result;
    }

    /**
     * 闭瓶
     * @param Barcode
     * @return
     */
    @PostMapping(value = "closeIngQuotation")
    public Result<?> closeIngQuotation(String Barcode, String closeRemarks, String instrCode) {
        Result<List<PdProductStock>> result = new Result<>();
        try {
            result = pdProductService.closeIngQuotation(Barcode, closeRemarks, instrCode, result);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            result.setCode(500);
            result.setMessage("条码格式不正确");
        }
        return result;
    }

    /**
     * 条码解析并查询库存
     *
     * @param Barcode1
     * @param Barcode2
     * @param req
     * @return
     */
    @PostMapping(value = "stockScanCode")
    public Result<List<PdProductStock>> stockScanCode(String Barcode1, String Barcode2, String productFlag, String nestatStatus, String barCodeType,
                                                      HttpServletRequest req) {
        Result<List<PdProductStock>> result = new Result<List<PdProductStock>>();
        try {
            result = pdProductService.getStocks(Barcode1, Barcode2,
                    productFlag != null ? productFlag : "",
                    nestatStatus != null ? nestatStatus : "",
                    barCodeType != null ? barCodeType : "", result);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            result.setCode(500);
            result.setMessage("系统异常");
        }
        return result;
    }

    /**
     * 批量修改产品收费代码
     *
     * @param ids
     * @return
     */
    @PostMapping(value = "/editChargeCodeBatch")
    public Result<?> editChargeCodeBatch(@RequestParam String ids, @RequestParam String chargeCode) {
        if (!oConvertUtils.isEmpty(ids) && !oConvertUtils.isEmpty(chargeCode)) {
            this.pdProductService.editChargeCodeBatch(ids, chargeCode);
            return Result.ok("批量修改成功!");
        } else {
            return Result.error("参数不正确!");
        }

    }

    /**
     * 通过id删除
     *
     * @param id
     * @return
     */
    @DeleteMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name = "id", required = true) String id) {
        Result<Object> resul = pdProductService.deleteV(id);
        return resul;
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @DeleteMapping(value = "/deleteBatch")
    public Result<?> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        Result<Object> resul = pdProductService.deleteBatchV(ids);
        return resul;
    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    @GetMapping(value = "/queryById")
    public Result<?> queryById(@RequestParam(name = "id", required = true) String id) {
        PdProduct pdProduct = pdProductService.getById(id);
        if (pdProduct == null) {
            return Result.error("未找到对应数据");
        }
        return Result.ok(pdProduct);
    }

    /**
     * 导出excel
     *
     * @param request
     * @param pdProduct
     */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, PdProduct pdProduct) {
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        pdProduct.setDepartParentId(sysUser.getDepartParentId());
        pdProduct.setProductFlag(PdConstant.PRODUCT_FLAG_0);
        //Step.1 获取导出数据
        List<PdProduct> pdProducts = pdProductService.selectList(pdProduct);
        // Step.2 AutoPoi 导出Excel
        ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
        mv.addObject(NormalExcelConstants.FILE_NAME, "产品列表");
        mv.addObject(NormalExcelConstants.CLASS, PdProduct.class);
        mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("产品列表数据", "导出人:" + sysUser.getRealname(), "产品数据"));
        mv.addObject(NormalExcelConstants.DATA_LIST, pdProducts);
        return mv;
    }

    /**
     * 试剂导出
     *
     * @param request
     * @param pdProduct
     * @return
     */
    @RequestMapping(value = "/exportXlsReagents")
    public ModelAndView exportXlsReagents(HttpServletRequest request, PdProduct pdProduct) {
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        pdProduct.setDepartParentId(sysUser.getDepartParentId());
        pdProduct.setProductFlag(PdConstant.PRODUCT_FLAG_1);
        //Step.1 获取导出数据
        List<PdProduct> pdProducts = pdProductService.selectList(pdProduct);
        List<PdProductReagents> exportList = JSON.parseArray(JSON.toJSONString(pdProducts), PdProductReagents.class);
        // Step.2 AutoPoi 导出Excel
        ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
        mv.addObject(NormalExcelConstants.FILE_NAME, "试剂列表");
        mv.addObject(NormalExcelConstants.CLASS, PdProductReagents.class);
        mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("试剂列表数据", "导出人:" + sysUser.getRealname(), "试剂数据"));
        mv.addObject(NormalExcelConstants.DATA_LIST, exportList);
        return mv;
    }

    /**
     * 通过excel导入数据
     *
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
        Map<String, MultipartFile> fileMap = multipartRequest.getFileMap();
        Result<Object> resul = pdProductService.importExcel(fileMap);
        return resul;
    }

    /**
     * 试剂导入
     *
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/importExcelReagents", method = RequestMethod.POST)
    public Result<?> importExcelReagents(HttpServletRequest request, HttpServletResponse response) {
        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
        Map<String, MultipartFile> fileMap = multipartRequest.getFileMap();
        Result<Object> resul = pdProductService.importExcelReagents(fileMap);
        return resul;
    }

    /**
     * 查询产品列表，用于选择产品弹出框
     * add by jangxz 20200206
     *
     * @param pdProduct
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @GetMapping(value = "/chooseProductList")
    public Result<?> chooseProductList(PdProductPage pdProduct,
                                       @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                       @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                       HttpServletRequest req) {
        Page<PdProductPage> page = new Page<PdProductPage>(pageNo, pageSize);
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        pdProduct.setStatus(PdConstant.DISABLE_ENABLE_STATUS_0);//只查启用
        pdProduct.setDepartParentId(sysUser.getDepartParentId());
        IPage<PdProductPage> pageList = pdProductService.chooseProductList(page, pdProduct);
        return Result.ok(pageList);
    }

    /**
     * 批量停用和启用status 0启用1停用
     * @param jsonObject
     * @return
     */
    @RequestMapping(value = "/batchDisable", method = RequestMethod.POST)
    public Result<PdProduct> batchDisable(@RequestBody JSONObject jsonObject) {
        Result<PdProduct> result = new Result<PdProduct>();
        try {
            String ids = jsonObject.getString("ids");
            String status = jsonObject.getString("status");
            String[] arr = ids.split(",");
            List<PdProduct> pdProducts= new ArrayList<>();
            for (String id : arr) {
                if(oConvertUtils.isNotEmpty(id)) {
                    PdProduct pdProduct = new PdProduct();
                    pdProduct.setId(id);
                    pdProduct.setStatus(status);
                    pdProducts.add(pdProduct);
                }
            }
            if(pdProducts!=null && pdProducts.size()>0){
                pdProductService.updateBatchById(pdProducts);
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            result.error500("操作失败"+e.getMessage());
        }
        result.success("操作成功!");
        return result;

    }
}
