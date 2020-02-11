package org.jeecg.modules.pd.controller;

import java.util.*;

import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.pd.entity.PdCategoryTree;
import org.jeecg.modules.system.model.TreeModel;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.constant.CommonConstant;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.modules.pd.entity.PdCategory;
import org.jeecg.modules.pd.service.IPdCategoryService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.system.base.controller.JeecgController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

 /**
 * @Description: 产品分类
 * @Author: zxh
 * @Date:   2020-01-15
 * @Version: V1.0
 */
@RestController
@RequestMapping("/pd/pdCategory")
@Slf4j
public class PdCategoryController extends JeecgController<PdCategory, IPdCategoryService> {
	@Autowired
	private IPdCategoryService pdCategoryService;
	
	/**
	 * 分页列表查询
	 *
	 * @param pdCategory
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public Result<?> queryPageList(PdCategory pdCategory,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		long start = System.currentTimeMillis();
		Result<List<PdCategoryTree>> result = new Result<>();
		try {
			LambdaQueryWrapper<PdCategory> query = new LambdaQueryWrapper<PdCategory>();
			//query.eq(PdCategory::getDelFlag, CommonConstant.DEL_FLAG_0);
			List<PdCategory> list = pdCategoryService.list(query);
			List<PdCategoryTree> treeList = new ArrayList<>();
			getTreeList(treeList, list, null);
			result.setResult(treeList);
			result.setSuccess(true);
			log.info("======获取全部菜单数据=====耗时:" + (System.currentTimeMillis() - start) + "毫秒");
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return result;
	}

	 /**
	  * 不分页列表查询
	  *
	  * @return
	  */
	 @GetMapping(value = "/getCategoryOneList")
	 public Result<List<PdCategory>> getEncodingIdentifierList(PdCategory pdCategory) {
		 long start = System.currentTimeMillis();
		 Result<List<PdCategory>> result = new Result<>();
		 try {
			 List<PdCategory> list = pdCategoryService.selectCategoryOneList(pdCategory);
			 result.setResult(list);
			 result.setSuccess(true);
		 }catch(Exception e){
			 log.error(e.getMessage(), e);
		 }
		 log.info("======获取产品数据=====耗时:" + (System.currentTimeMillis() - start) + "毫秒");
		 return result;
	 }
	
	/**
	 *   添加
	 *
	 * @param pdCategory
	 * @return
	 */
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody PdCategory pdCategory) {
		pdCategoryService.addPdCategory(pdCategory);
		return Result.ok("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param pdCategory
	 * @return
	 */
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody PdCategory pdCategory) {
		pdCategoryService.editPdCategory(pdCategory);
		return Result.ok("编辑成功!");
	}


	 /**
	  * 获取全部的权限树
	  *
	  * @return
	  */
	 @RequestMapping(value = "/queryCategoryTreeList", method = RequestMethod.GET)
	 public Result<Map<String, Object>> queryTreeList() {
		 Result<Map<String, Object>> result = new Result<>();
		 // 全部权限ids
		 List<String> ids = new ArrayList<>();
		 try {
			 LambdaQueryWrapper<PdCategory> query = new LambdaQueryWrapper<PdCategory>();
			 query.eq(PdCategory::getDelFlag, CommonConstant.DEL_FLAG_0);
			 List<PdCategory> list = pdCategoryService.list(query);
			 for (PdCategory pc : list) {
				 ids.add(pc.getId());
			 }
			 List<TreeModel> treeList = new ArrayList<>();
			 getTreeModelList(treeList, list, null);

			 Map<String, Object> resMap = new HashMap<String, Object>();
			 resMap.put("treeList", treeList); // 全部树节点数据
			 resMap.put("ids", ids);// 全部树ids
			 result.setResult(resMap);
			 result.setSuccess(true);
		 } catch (Exception e) {
			 log.error(e.getMessage(), e);
		 }
		 return result;
	 }

	 /**
	  * 获取vue的树对象
	  * @param treeList
	  * @param metaList
	  * @param temp
	  */
	 private void getTreeList(List<PdCategoryTree> treeList, List<PdCategory> metaList, PdCategoryTree temp) {
		 for (PdCategory pc : metaList) {
			 String tempPid = pc.getParentId();
			 PdCategoryTree tree = new PdCategoryTree(pc);
			 if (temp == null && oConvertUtils.isEmpty(tempPid)) {
				 treeList.add(tree);
				 if (!tree.getIsLeaf()) {
					 getTreeList(treeList, metaList, tree);
				 }
			 } else if (temp != null && tempPid != null && tempPid.equals(temp.getId())) {
				 temp.getChildren().add(tree);
				 if (!tree.getIsLeaf()) {
					 getTreeList(treeList, metaList, tree);
				 }
			 }

		 }
	 }

     /**
      * 获取vue的树对象
      * @param treeList
      * @param metaList
      * @param temp
      */
	 private void getTreeModelList(List<TreeModel> treeList, List<PdCategory> metaList, TreeModel temp) {
		 for (PdCategory pdCategory : metaList) {
			 String tempPid = pdCategory.getParentId();
			 TreeModel tree = new TreeModel(pdCategory);
			 if (temp == null && oConvertUtils.isEmpty(tempPid)) {
				 treeList.add(tree);
				 if (!tree.getIsLeaf()) {
					 getTreeModelList(treeList, metaList, tree);
				 }
			 } else if (temp != null && tempPid != null && tempPid.equals(temp.getKey())) {
				 temp.getChildren().add(tree);
				 if (!tree.getIsLeaf()) {
					 getTreeModelList(treeList, metaList, tree);
				 }
			 }

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
		pdCategoryService.removePdCategory(id);
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
		this.pdCategoryService.removeByIds(Arrays.asList(ids.split(",")));
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
		PdCategory pdCategory = pdCategoryService.getById(id);
		if(pdCategory==null) {
			return Result.error("未找到对应数据");
		}
		return Result.ok(pdCategory);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param pdCategory
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, PdCategory pdCategory) {
        return super.exportXls(request, pdCategory, PdCategory.class, "产品分类");
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
        return super.importExcel(request, response, PdCategory.class);
    }

}
