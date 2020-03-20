package org.jeecg.modules.pd.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.collections.CollectionUtils;
import org.apache.ibatis.session.SqlSession;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.modules.pd.entity.PdProduct;
import org.jeecg.modules.pd.entity.PdSupplier;
import org.jeecg.modules.pd.mapper.PdSupplierMapper;
import org.jeecg.modules.pd.service.IPdProductService;
import org.jeecg.modules.pd.service.IPdSupplierService;
import org.jeecgframework.poi.excel.ExcelImportUtil;
import org.jeecgframework.poi.excel.entity.ImportParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

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
        return page.setRecords(pdSupplierMapper.selectList(pdSupplier));
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
        boolean bl = true;
        for (Map.Entry<String, MultipartFile> entity : fileMap.entrySet()) {
            MultipartFile file = entity.getValue();// 获取上传文件对象
            ImportParams params = new ImportParams();
            params.setTitleRows(2);
            params.setHeadRows(1);
            params.setNeedSave(true);
            try {
                List<PdSupplier> list = ExcelImportUtil.importExcel(file.getInputStream(), PdSupplier.class, params);
                for(PdSupplier ps :list){
                    ps.setDepartParentId(sysUser.getDepartParentId());
                    List<PdSupplier> obj = dao.verify(ps);
                    if (obj != null && obj.size()>0) {
                        bl = false;
                        continue;
                    }
                    save(ps);
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
        if(bl){
            return Result.ok("文件导入成功！");
        }else{
            return Result.ok("供应商名称存在重复，文件内容部分导入成功！");
        }
    }
}
