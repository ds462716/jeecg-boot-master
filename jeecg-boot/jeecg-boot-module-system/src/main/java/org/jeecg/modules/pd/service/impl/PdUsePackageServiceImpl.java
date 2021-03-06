package org.jeecg.modules.pd.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.ibatis.session.SqlSession;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.modules.external.entity.ExInspectionItems;
import org.jeecg.modules.external.entity.ExInspectionItemsUseDetail;
import org.jeecg.modules.external.mapper.ExInspectionItemsMapper;
import org.jeecg.modules.external.service.IExInspectionItemsUseDetailService;
import org.jeecg.modules.pd.entity.PdUsePackage;
import org.jeecg.modules.pd.entity.PdUsePackageDetail;
import org.jeecg.modules.pd.mapper.PdUsePackageDetailMapper;
import org.jeecg.modules.pd.mapper.PdUsePackageMapper;
import org.jeecg.modules.pd.service.IPdDepartService;
import org.jeecg.modules.pd.service.IPdUsePackageDetailService;
import org.jeecg.modules.pd.service.IPdUsePackageService;
import org.jeecg.modules.system.entity.SysDepart;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Description: 检验项目
 * @Author: zxh
 * @Date:   2020年4月21日08:56:20
 * @Version: V1.0
 */
@Service
public class PdUsePackageServiceImpl extends ServiceImpl<PdUsePackageMapper, PdUsePackage> implements IPdUsePackageService {

	@Autowired
	private PdUsePackageMapper pdUsePackageMapper;

	@Autowired
	private IPdUsePackageDetailService pdUsePackageDetailService;

	@Autowired
	private PdUsePackageDetailMapper pdUsePackageDetailMapper;

	@Autowired
	private IExInspectionItemsUseDetailService exInspectionItemsUseDetailService;

	@Autowired
	private SqlSession sqlsession;

	@Autowired
	private ExInspectionItemsMapper exInspectionItemsMapper;

	@Autowired
	private IPdDepartService pdDepartService;

	
	@Override
	@Transactional
	public void saveMain(PdUsePackage PdUsePackage, List<PdUsePackageDetail> pdUsePackageDetailList) {
		pdUsePackageMapper.insert(PdUsePackage);
		if(pdUsePackageDetailList!=null && pdUsePackageDetailList.size()>0) {
			for(PdUsePackageDetail entity:pdUsePackageDetailList) {
				//外键设置
				entity.setPackageId(PdUsePackage.getId());
				entity.setId(null);
			}
			pdUsePackageDetailService.saveBatch(pdUsePackageDetailList);
		}
	}

	@Override
	@Transactional
	public void updateMain(PdUsePackage pdUsePackage,List<PdUsePackageDetail> pdUsePackageDetailList) {
		pdUsePackageMapper.updateById(pdUsePackage);
		
		//1.先删除子表数据
		pdUsePackageDetailMapper.deleteByMainId(pdUsePackage.getId());
		
		//2.子表数据重新插入
		if(pdUsePackageDetailList!=null && pdUsePackageDetailList.size()>0) {
			for(PdUsePackageDetail entity:pdUsePackageDetailList) {
				//外键设置
				entity.setPackageId(pdUsePackage.getId());
				entity.setId(null);
			}
			pdUsePackageDetailService.saveBatch(pdUsePackageDetailList);
		}
	}

	@Override
	@Transactional
	public void delMain(String id) {
		pdUsePackageDetailMapper.deleteByMainId(id);
		pdUsePackageMapper.deleteById(id);
	}

	@Override
	@Transactional
	public void delBatchMain(Collection<? extends Serializable> idList) {
		for(Serializable id:idList) {
			pdUsePackageDetailMapper.deleteByMainId(id.toString());
			pdUsePackageMapper.deleteById(id);
		}
	}

	@Override
	public List<PdUsePackage> queryList(PdUsePackage pdUsePackage) {
		return pdUsePackageMapper.queryList(pdUsePackage);
	}

	@Override
	public Page<PdUsePackage> queryList(Page<PdUsePackage> page, PdUsePackage pdUsePackage) {
		Page<PdUsePackage> pg = pdUsePackageMapper.queryList(page,pdUsePackage);
		List<PdUsePackage> pdUsePackages = pg.getRecords();
		if(pdUsePackages!=null && pdUsePackages.size()>0){
			//查询所有部门
			List<SysDepart> sysDeparts = pdDepartService.selectListTree(new SysDepart());
			//将部门转换成map
			Map<String, String> map = sysDeparts.stream().collect(Collectors.toMap(SysDepart::getId, SysDepart::getDepartName, (key1, key2) -> key2));
			//拼接成部门名称列表用
			for(PdUsePackage entity :pdUsePackages){
				String testDepartId=entity.getTestDepartId();
				if(StringUtils.isNotEmpty(testDepartId)){
					List<String> ids = Arrays.asList(entity.getTestDepartId().split(","));
					if(ids!=null && ids.size()>0){
						String testDepartNames = "";
						for(String idKey:ids){
							testDepartNames+=map.get(idKey)+",";
						}
						if(testDepartNames.endsWith(",")){
							testDepartNames = testDepartNames.substring(0,testDepartNames.length()-1);
						}
						entity.setTestDepartNames(testDepartNames);
					}
				}
			}
		}
		return pg;
	}

	@Override
	public List<PdUsePackage> verify(PdUsePackage pdUsePackage) {
		return pdUsePackageMapper.verify(pdUsePackage);
	}


	/**
	 * 检验项目删除及校验
	 * @param id
	 * @return
	 */
	@Override
	public Result<Object> deleteV(String id) {
		try{
			LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
			ExInspectionItemsUseDetail exInspectionItemsUseDetail = new ExInspectionItemsUseDetail();
			exInspectionItemsUseDetail.setDepartParentId(sysUser.getDepartParentId());
			exInspectionItemsUseDetail.setPackageId(id);
			List<PdUsePackage> pdUsePackages = exInspectionItemsUseDetailService.selectListByCT(exInspectionItemsUseDetail);
			ExInspectionItems exInspectionItems = new ExInspectionItems();
			exInspectionItems.setPackageId(id);
			List<ExInspectionItems> ExInspectionItemList=exInspectionItemsMapper.selectList(exInspectionItems);
			if(CollectionUtils.isNotEmpty(pdUsePackages) || CollectionUtils.isNotEmpty(ExInspectionItemList)){
				return Result.error("删除失败!，当前检验项目被使用不能删除");
			}
			this.removeById(id);
			return Result.ok("删除成功!");
		}catch(Exception e){
			e.printStackTrace();
			return Result.error("删除失败!，系统异常");
		}

	}

	/**
	 * 检验项目批量删除及校验
	 * @param ids
	 * @return
	 */
	@Override
	public Result<Object> deleteBatchV(String ids) {
		{
			try{
				PdUsePackageMapper dao = sqlsession.getMapper(PdUsePackageMapper.class);
				List<String> idList = Arrays.asList(ids.split(","));
				if(idList!=null && idList.size()>0){
					boolean bl = true;
					for(String id : idList){
						ExInspectionItemsUseDetail exInspectionItemsUseDetail = new ExInspectionItemsUseDetail();
						LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
						exInspectionItemsUseDetail.setDepartParentId(sysUser.getDepartParentId());
						exInspectionItemsUseDetail.setPackageId(id);
						List<PdUsePackage> pdUsePackages = exInspectionItemsUseDetailService.selectListByCT(exInspectionItemsUseDetail);
						if(CollectionUtils.isNotEmpty(pdUsePackages)){
							bl = false;
							continue;
						}
						dao.deleteById(id);
					}
					if(bl){
						return Result.ok("批量删除成功!");
					}else{
						return Result.ok("部分删除成功，被使用的不能删除!");
					}
				}else{
					return Result.error("删除失败,参数不正确!");
				}
			}catch(Exception e){
				e.printStackTrace();
				return Result.error("删除失败!，系统异常");
			}


		}
	}

}
