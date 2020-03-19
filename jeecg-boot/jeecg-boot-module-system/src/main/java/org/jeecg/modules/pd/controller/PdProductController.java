package org.jeecg.modules.pd.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.pd.entity.PdProduct;
import org.jeecg.modules.pd.entity.PdProductStock;
import org.jeecg.modules.pd.service.IPdProductService;
import org.jeecg.modules.pd.util.FileUploadUtil;
import org.jeecg.modules.pd.vo.PdProductPage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

 /**
 * @Description: pd_product
 * @Author: zxh
 * @Date:   2020-02-05
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
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		Result<Page<PdProduct>> result = new Result<Page<PdProduct>>();
		Page<PdProduct> pageList = new Page<PdProduct>(pageNo,pageSize);
		LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
		pdProduct.setDepartParentId(sysUser.getDepartParentId());
		pageList =pdProductService.selectList(pageList,pdProduct);//
		result.setSuccess(true);
		result.setResult(pageList);
		return Result.ok(pageList);
	}
	
	/**
	 *   添加
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
	  * @param pdProduct
	  * @return
	  */
	 @PostMapping(value = "/save")
	 public Result<?> save(@ModelAttribute PdProduct pdProduct,@RequestParam MultipartFile[] licenceSiteUp0,
						   @RequestParam MultipartFile[] licenceSiteUp1,
						   @RequestParam MultipartFile[] licenceSiteUp2,@RequestParam MultipartFile[] licenceSiteUp3,
						   @RequestParam MultipartFile[] licenceSiteUp4,@RequestParam MultipartFile[] licenceSiteUp5,
						   @RequestParam MultipartFile[] licenceSiteUp6,@RequestParam MultipartFile[] licenceSiteUp7,
						   @RequestParam MultipartFile[] licenceSiteUp8,@RequestParam MultipartFile[] licenceSiteUp9,
						   @RequestParam MultipartFile[] licenceSiteUp10,@RequestParam MultipartFile[] licenceSiteUp11
	 ) {
		 Result<Boolean> result = new Result<>();
		 //如果此参数为false则程序发生异常
		 result.setResult(true);
		 result.setMessage("添加成功");
		 //存入图片
		 if(!FileUploadUtil.isImgEmpty(licenceSiteUp0)){
			 String filePath = FileUploadUtil.upload(licenceSiteUp0);
			 pdProduct.setLicenceSite0(filePath);
		 }
		 if(!FileUploadUtil.isImgEmpty(licenceSiteUp1)){
			 String filePath = FileUploadUtil.upload(licenceSiteUp1);
			 pdProduct.setLicenceSite1(filePath);
		 }
		 if(!FileUploadUtil.isImgEmpty(licenceSiteUp2)){
			 String filePath = FileUploadUtil.upload(licenceSiteUp2);
			 pdProduct.setLicenceSite2(filePath);
		 }
		 if(!FileUploadUtil.isImgEmpty(licenceSiteUp3)){
			 String filePath = FileUploadUtil.upload(licenceSiteUp3);
			 pdProduct.setLicenceSite3(filePath);
		 }
		 if(!FileUploadUtil.isImgEmpty(licenceSiteUp4)){
			 String filePath = FileUploadUtil.upload(licenceSiteUp4);
			 pdProduct.setLicenceSite4(filePath);
		 }
		 if(!FileUploadUtil.isImgEmpty(licenceSiteUp5)){
			 String filePath = FileUploadUtil.upload(licenceSiteUp5);
			 pdProduct.setLicenceSite5(filePath);
		 }
		 if(!FileUploadUtil.isImgEmpty(licenceSiteUp6)){
			 String filePath = FileUploadUtil.upload(licenceSiteUp6);
			 pdProduct.setLicenceSite6(filePath);
		 }
		 if(!FileUploadUtil.isImgEmpty(licenceSiteUp7)){
			 String filePath = FileUploadUtil.upload(licenceSiteUp7);
			 pdProduct.setLicenceSite7(filePath);
		 }
		 if(!FileUploadUtil.isImgEmpty(licenceSiteUp8)){
			 String filePath = FileUploadUtil.upload(licenceSiteUp8);
			 pdProduct.setLicenceSite8(filePath);
		 }
		 if(!FileUploadUtil.isImgEmpty(licenceSiteUp9)){
			 String filePath = FileUploadUtil.upload(licenceSiteUp9);
			 pdProduct.setLicenceSite9(filePath);
		 }
		 if(!FileUploadUtil.isImgEmpty(licenceSiteUp10)){
			 String filePath = FileUploadUtil.upload(licenceSiteUp10);
			 pdProduct.setLicenceSite10(filePath);
		 }
		 if(!FileUploadUtil.isImgEmpty(licenceSiteUp11)){
			 String filePath = FileUploadUtil.upload(licenceSiteUp11);
			 pdProduct.setLicenceSite11(filePath);
		 }
		 pdProductService.saveProduct(pdProduct);
		 return Result.ok("添加成功！");
	 }

	    /**
     * 包含文件上传功能
     * @param pdProduct
     * @return
     */
    @PostMapping(value = "/update")
    public Result<?> update(@ModelAttribute PdProduct pdProduct,@RequestParam MultipartFile[] licenceSiteUp0,
                          @RequestParam MultipartFile[] licenceSiteUp1,
                          @RequestParam MultipartFile[] licenceSiteUp2,@RequestParam MultipartFile[] licenceSiteUp3,
                          @RequestParam MultipartFile[] licenceSiteUp4,@RequestParam MultipartFile[] licenceSiteUp5,
                          @RequestParam MultipartFile[] licenceSiteUp6,@RequestParam MultipartFile[] licenceSiteUp7,
                          @RequestParam MultipartFile[] licenceSiteUp8,@RequestParam MultipartFile[] licenceSiteUp9,
                          @RequestParam MultipartFile[] licenceSiteUp10,@RequestParam MultipartFile[] licenceSiteUp11
    ) {
        Result<Boolean> result = new Result<>();
        //如果此参数为false则程序发生异常
        result.setResult(true);
        result.setMessage("编辑成功");
        //存入图片
        if(!FileUploadUtil.isImgEmpty(licenceSiteUp0)){
            String filePath = FileUploadUtil.upload(licenceSiteUp0);
            if(pdProduct.getLicenceSite0()!=null && !"".equals(pdProduct.getLicenceSite0())){
                //先删除再更新
                FileUploadUtil.deleteFile(pdProduct.getLicenceSite0());
            }
            if(!"".equals(filePath)){
				pdProduct.setLicenceSite0(filePath);
            }
        }
        if(!FileUploadUtil.isImgEmpty(licenceSiteUp1)){
            String filePath = FileUploadUtil.upload(licenceSiteUp1);
            if(pdProduct.getLicenceSite1()!=null && !"".equals(pdProduct.getLicenceSite1())){
                //先删除再更新
                FileUploadUtil.deleteFile(pdProduct.getLicenceSite1());
            }
            if(!"".equals(filePath)){
				pdProduct.setLicenceSite1(filePath);
            }
        }
        if(!FileUploadUtil.isImgEmpty(licenceSiteUp2)){
            String filePath = FileUploadUtil.upload(licenceSiteUp2);
            if(pdProduct.getLicenceSite2()!=null && !"".equals(pdProduct.getLicenceSite2())){
                //先删除再更新
                FileUploadUtil.deleteFile(pdProduct.getLicenceSite2());
            }
            if(!"".equals(filePath)){
                pdProduct.setLicenceSite2(filePath);
            }
        }
        if(!FileUploadUtil.isImgEmpty(licenceSiteUp3)){
            String filePath = FileUploadUtil.upload(licenceSiteUp3);
            if(pdProduct.getLicenceSite3()!=null && !"".equals(pdProduct.getLicenceSite3())){
                //先删除再更新
                FileUploadUtil.deleteFile(pdProduct.getLicenceSite3());
            }
            if(!"".equals(filePath)){
                pdProduct.setLicenceSite3(filePath);
            }
        }
        if(!FileUploadUtil.isImgEmpty(licenceSiteUp4)){
            String filePath = FileUploadUtil.upload(licenceSiteUp4);
            if(pdProduct.getLicenceSite4()!=null && !"".equals(pdProduct.getLicenceSite4())){
                //先删除再更新
                FileUploadUtil.deleteFile(pdProduct.getLicenceSite4());
            }
            if(!"".equals(filePath)){
                pdProduct.setLicenceSite4(filePath);
            }
        }
        if(!FileUploadUtil.isImgEmpty(licenceSiteUp5)){
            String filePath = FileUploadUtil.upload(licenceSiteUp5);
            if(pdProduct.getLicenceSite5()!=null && !"".equals(pdProduct.getLicenceSite5())){
                //先删除再更新
                FileUploadUtil.deleteFile(pdProduct.getLicenceSite5());
            }
            if(!"".equals(filePath)){
                pdProduct.setLicenceSite5(filePath);
            }
        }
        if(!FileUploadUtil.isImgEmpty(licenceSiteUp6)){
            String filePath = FileUploadUtil.upload(licenceSiteUp6);
            if(pdProduct.getLicenceSite6()!=null && !"".equals(pdProduct.getLicenceSite6())){
                //先删除再更新
                FileUploadUtil.deleteFile(pdProduct.getLicenceSite6());
            }
            if(!"".equals(filePath)){
                pdProduct.setLicenceSite6(filePath);
            }
        }
        if(!FileUploadUtil.isImgEmpty(licenceSiteUp7)){
            String filePath = FileUploadUtil.upload(licenceSiteUp7);
            if(pdProduct.getLicenceSite7()!=null && !"".equals(pdProduct.getLicenceSite7())){
                //先删除再更新
                FileUploadUtil.deleteFile(pdProduct.getLicenceSite7());
            }
            if(!"".equals(filePath)){
                pdProduct.setLicenceSite7(filePath);
            }
        }
        if(!FileUploadUtil.isImgEmpty(licenceSiteUp8)){
            String filePath = FileUploadUtil.upload(licenceSiteUp8);
            if(pdProduct.getLicenceSite8()!=null && !"".equals(pdProduct.getLicenceSite8())){
                //先删除再更新
                FileUploadUtil.deleteFile(pdProduct.getLicenceSite8());
            }
            if(!"".equals(filePath)){
                pdProduct.setLicenceSite8(filePath);
            }
        }
        if(!FileUploadUtil.isImgEmpty(licenceSiteUp9)){
            String filePath = FileUploadUtil.upload(licenceSiteUp9);
            if(pdProduct.getLicenceSite9()!=null && !"".equals(pdProduct.getLicenceSite9())){
                //先删除再更新
                FileUploadUtil.deleteFile(pdProduct.getLicenceSite9());
            }
            if(!"".equals(filePath)){
                pdProduct.setLicenceSite9(filePath);
            }
        }
        if(!FileUploadUtil.isImgEmpty(licenceSiteUp10)){
            String filePath = FileUploadUtil.upload(licenceSiteUp10);
            if(pdProduct.getLicenceSite10()!=null && !"".equals(pdProduct.getLicenceSite10())){
                //先删除再更新
                FileUploadUtil.deleteFile(pdProduct.getLicenceSite10());
            }
            if(!"".equals(filePath)){
                pdProduct.setLicenceSite10(filePath);
            }
        }
        if(!FileUploadUtil.isImgEmpty(licenceSiteUp11)){
            String filePath = FileUploadUtil.upload(licenceSiteUp11);
            if(pdProduct.getLicenceSite11()!=null && !"".equals(pdProduct.getLicenceSite11())){
                //先删除再更新
                FileUploadUtil.deleteFile(pdProduct.getLicenceSite11());
            }
            if(!"".equals(filePath)){
                pdProduct.setLicenceSite11(filePath);
            }
        }
        pdProductService.updateProduct(pdProduct);
        return Result.ok("编辑成功！");
    }
	
	/**
	 *  编辑
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
	  * @param Barcode1
	  * @param Barcode2
	  * @param req
	  * @return
	  */
	@PostMapping(value = "scanCode")
	public Result<Map> scanCode(String Barcode1,String Barcode2,
								HttpServletRequest req) {
		Result<Map> result = new Result<Map>();
		try{
			result = pdProductService.getScanCode(Barcode1,Barcode2,result);
		}catch(Exception e){
			log.error(e.getMessage(), e);
			result.setCode(500);
			result.setMessage("系统异常");
		}
		return result;
	}

	 /**
	  * 条码解析并查询库存
	  * @param Barcode1
	  * @param Barcode2
	  * @param req
	  * @return
	  */
	 @PostMapping(value = "stockScanCode")
	 public Result<List<PdProductStock>> stockScanCode(String Barcode1,String Barcode2,
													   HttpServletRequest req) {
		 Result<List<PdProductStock>> result = new Result<List<PdProductStock>>();
		 try{
			 result = pdProductService.getStocks(Barcode1,Barcode2,result);
		 }catch(Exception e){
			 log.error(e.getMessage(), e);
             result.setCode(500);
			 result.setMessage("系统异常");
		 }
		 return result;
	 }

	 /**
	  *  批量修改产品收费代码
	  *
	  * @param ids
	  * @return
	  */
	 @PostMapping(value = "/editChargeCodeBatch")
	 public Result<?> editChargeCodeBatch(@RequestParam String ids,@RequestParam String chargeCode) {
		 if(!oConvertUtils.isEmpty(ids) && !oConvertUtils.isEmpty(chargeCode)){
			 this.pdProductService.editChargeCodeBatch(ids,chargeCode);
			 return Result.ok("批量修改成功!");
		 }else{
			 return Result.error("参数不正确!");
		 }

	 }

	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		pdProductService.removeById(id);
		return Result.ok("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.pdProductService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.ok("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		PdProduct pdProduct = pdProductService.getById(id);
		if(pdProduct==null) {
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
        return super.exportXls(request, pdProduct, PdProduct.class, "pd_product");
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
        return super.importExcel(request, response, PdProduct.class);
    }

	 /**
	  * 查询产品列表，用于选择产品弹出框
	  * add by jangxz 20200206
	  * @param pdProduct
	  * @param pageNo
	  * @param pageSize
	  * @param req
	  * @return
	  */
	 @GetMapping(value = "/chooseProductList")
	 public Result<?> chooseProductList(PdProductPage pdProduct,
									@RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
									@RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
									HttpServletRequest req) {
		 Page<PdProductPage> page = new Page<PdProductPage>(pageNo, pageSize);
		 LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
		 pdProduct.setDepartParentId(sysUser.getDepartParentId());
		 IPage<PdProductPage> pageList = pdProductService.chooseProductList(page, pdProduct);
		 return Result.ok(pageList);
	 }
}
