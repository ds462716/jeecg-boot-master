package org.jeecg.modules.pd.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.collections.CollectionUtils;
import org.apache.ibatis.session.SqlSession;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.constant.PdConstant;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.pd.entity.PdProduct;
import org.jeecg.modules.pd.entity.PdSupplier;
import org.jeecg.modules.pd.mapper.PdSupplierMapper;
import org.jeecg.modules.pd.model.PdSupplierTreeModel;
import org.jeecg.modules.pd.service.IPdProductService;
import org.jeecg.modules.pd.service.IPdSupplierService;
import org.jeecg.modules.pd.util.JmUtil;
import org.jeecg.modules.system.entity.SysDepart;
import org.jeecg.modules.system.service.ISysDepartService;
import org.jeecgframework.poi.excel.ExcelImportUtil;
import org.jeecgframework.poi.excel.entity.ImportParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

/**
 * @Description: 供应商
 * @Author: zxh
 * @Date:   2020-01-09
 * @Version: V1.0
 */
@Service
public class PdSupplierServiceImpl extends ServiceImpl<PdSupplierMapper, PdSupplier> implements IPdSupplierService {

    @Autowired
    private PdSupplierMapper pdSupplierMapper;

    @Autowired
    private IPdProductService pdProductService;
    @Autowired
    private SqlSession sqlsession;
    @Autowired
    private ISysDepartService sysDepartService;


    @Override
    public List<PdSupplier> verify(PdSupplier pdSupplier) {
        return pdSupplierMapper.verify(pdSupplier);
    }

    @Override
    public List<PdSupplier> selectList(PdSupplier pdSupplier) {
        return pdSupplierMapper.selectList(pdSupplier);
    }

    @Override
    public List<PdSupplier> selectAllList(PdSupplier pdSupplier) {
        return pdSupplierMapper.selectAllList(pdSupplier);
    }

    @Override
    public void updateValidityFlag(PdSupplier pdSupplier){
        pdSupplierMapper.updateValidityFlag(pdSupplier);
    }

    @Override
    public Page<PdSupplier> selectList(Page<PdSupplier> page, PdSupplier pdSupplier) {
        return pdSupplierMapper.selectListByPage(page,pdSupplier);
    }

    @Override
    public Result<Object> deleteV(String id) {
        try{
            PdProduct pdProduct = new PdProduct();
            LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
            pdProduct.setDepartParentId(sysUser.getDepartParentId());
            pdProduct.setSupplierId(id);
            List<PdProduct> pdProducts = pdProductService.selectListByCT(pdProduct);
            if(CollectionUtils.isNotEmpty(pdProducts)){
                return Result.error("删除失败!，当前供应商被使用不能删除");
            }
            this.removeById(id);
            return Result.ok("删除成功!");
        }catch(Exception e){
            e.printStackTrace();
            return Result.error("删除失败!，系统异常");
        }

    }

    /**
     * 批量删除
     * @param ids
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public Result<Object> deleteBatchV(String ids) {
        try{
            PdSupplierMapper dao = sqlsession.getMapper(PdSupplierMapper.class);
            List<String> idList = Arrays.asList(ids.split(","));
            if(idList!=null && idList.size()>0){
                boolean bl = true;
                for(String id : idList){
                    PdProduct pdProduct = new PdProduct();
                    LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
                    pdProduct.setDepartParentId(sysUser.getDepartParentId());
                    pdProduct.setSupplierId(id);
                    List<PdProduct> pdProducts = pdProductService.selectListByCT(pdProduct);
                    if(CollectionUtils.isNotEmpty(pdProducts)){
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
    @Transactional(rollbackFor = Exception.class)
    @Override
    public Result<Object> importExcel(Map<String, MultipartFile> fileMap) {
        PdSupplierMapper dao = sqlsession.getMapper(PdSupplierMapper.class);
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        List<PdSupplier> list = new ArrayList<>();
        boolean bl = true;
        String message = "表格内没有内容";
        for (Map.Entry<String, MultipartFile> entity : fileMap.entrySet()) {
            MultipartFile file = entity.getValue();// 获取上传文件对象
            ImportParams params = new ImportParams();
            params.setTitleRows(2);
            params.setHeadRows(1);
            params.setNeedSave(true);
            try {
                list = ExcelImportUtil.importExcel(file.getInputStream(), PdSupplier.class, params);
                int i = 0;
                //查询所有的产品
                PdSupplier pdSupplier = new PdSupplier();
                pdSupplier.setDepartParentId(sysUser.getDepartParentId());
                List<PdSupplier> pdSuppliers = this.selectList(pdSupplier);
                for(PdSupplier ps :list){
                    if(oConvertUtils.isNotEmpty(ps.getName())){
                        if(checkName(pdSuppliers,ps)){
                            if(oConvertUtils.isEmpty(ps.getPy())){
                                //生成拼音简码
                                ps.setPy(JmUtil.getAllFirstLetter(ps.getName()));
                            }
                            if(oConvertUtils.isEmpty(ps.getWb())){
                                //生成五笔简码
                                ps.setWb(JmUtil.getWBCode(ps.getName()));
                            }
                            pdSuppliers.add(ps);
                        }else{
                            message = "导入失败,第"+(i+1)+"行名称不能重复";
                            bl = false;
                            break;
                        }
                    }else{
                        message = "导入失败,第"+(i+1)+"行名称不存在";
                        bl = false;
                        break;
                    }
                    i ++;
                    ps.setValidityFlag(PdConstant.PD_STATE_0);
                }
            } catch (Exception e) {
                log.error(e.getMessage(),e);
                return Result.error("文件导入失败:"+e.getMessage());
            } finally {
                try {
                    file.getInputStream().close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        //全部正确才能导入
        if(bl && list.size()>0){
            this.saveBatch(list);
            return Result.ok("文件导入成功");
        }else{
            return Result.error("文件导入失败:"+message);
        }
    }

    /**
     * 查询供应商以树节点的形式展示
     * @param pdSupplier
     * @return
     */
    @Override
    public List<PdSupplierTreeModel> queryTreeList(PdSupplier pdSupplier) {
        List<PdSupplierTreeModel> pdSupplierTreeModelList = new ArrayList<>();
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        pdSupplier.setDepartParentId(sysUser.getDepartParentId());
        SysDepart sysDepart = sysDepartService.getById(sysUser.getDepartParentId());
        if(sysDepart!=null ){
            List<PdSupplier> list = this.selectList(pdSupplier);
            PdSupplierTreeModel pdSupplierTreeModel = new PdSupplierTreeModel();
            pdSupplierTreeModel.setId("-1");
            pdSupplierTreeModel.setKey("-1");
            pdSupplierTreeModel.setTitle(sysDepart.getDepartName());
            pdSupplierTreeModel.setLeaf(true);
            if(CollectionUtils.isNotEmpty(list)){
                List<PdSupplierTreeModel> pdSupplierTreeModels = new ArrayList<>();
                for(PdSupplier ps :list){
                    PdSupplierTreeModel ptm = new PdSupplierTreeModel();
                    ptm.setId(ps.getId());
                    ptm.setKey(ps.getId());
                    ptm.setTitle(ps.getName());
                    ptm.setParentId("-1");
                    ptm.setLeaf(false);
                    pdSupplierTreeModels.add(ptm);
                }
                pdSupplierTreeModel.setChildren(pdSupplierTreeModels);
            }
            pdSupplierTreeModelList.add(pdSupplierTreeModel);
        }
        return pdSupplierTreeModelList;
    }

    /**
     * 校验名称是否重复
     * @param pdSuppliers
     * @param pdSupplier
     * @return
     */
    private boolean checkName(List<PdSupplier> pdSuppliers, PdSupplier pdSupplier) {
        for (PdSupplier p : pdSuppliers) {
            if(p.getName().equals(pdSupplier.getName().trim())){
                return false;
            }
        }
        return true;
    }
}
