package org.jeecg.modules.pd.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.commons.collections.CollectionUtils;
import org.apache.ibatis.session.SqlSession;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.constant.PdConstant;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.pd.entity.PdProduct;
import org.jeecg.modules.pd.entity.PdVender;
import org.jeecg.modules.pd.mapper.PdVenderMapper;
import org.jeecg.modules.pd.service.IPdProductService;
import org.jeecg.modules.pd.service.IPdVenderService;
import org.jeecgframework.poi.excel.ExcelImportUtil;
import org.jeecgframework.poi.excel.entity.ImportParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * @Description: 生产厂家
 * @Author: zxh
 * @Date:   2020-01-09
 * @Version: V1.0
 */
@Service
public class PdVenderServiceImpl extends ServiceImpl<PdVenderMapper, PdVender> implements IPdVenderService {

    @Autowired
    private PdVenderMapper pdVenderMapper;
    @Autowired
    private IPdProductService pdProductService;
    @Autowired
    private SqlSession sqlsession;
    @Override
    public  List<PdVender> verify(PdVender pdVender) {
        return pdVenderMapper.verify(pdVender);
    }

    /**
     * 下拉选择
     * @param pdVender
     * @return
     */
    @Override
    public List<PdVender> selectList(PdVender pdVender) {
        return pdVenderMapper.selectList(pdVender);
    }

    @Override
    public List<PdVender> selectAllList(PdVender pdVender) {
        return pdVenderMapper.selectAllList(pdVender);
    }

    @Override
    public void updateValidityFlag(PdVender pdVender){
        pdVenderMapper.updateValidityFlag(pdVender);
    }

    @Override
    public Page<PdVender> selectList(Page<PdVender> page, PdVender pdVender) {
        return pdVenderMapper.selectListByPage(page,pdVender);
    }

    @Override
    public Result<Object> deleteV(String id) {
        try{
            PdProduct pdProduct = new PdProduct();
            LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
            pdProduct.setDepartParentId(sysUser.getDepartParentId());
            pdProduct.setVenderId(id);
            List<PdProduct> pdProducts = pdProductService.selectListByCT(pdProduct);
            if(CollectionUtils.isNotEmpty(pdProducts)){
                return Result.error("删除失败!，当前生产厂家被使用不能删除");
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
            PdVenderMapper dao = sqlsession.getMapper(PdVenderMapper.class);
            List<String> idList = Arrays.asList(ids.split(","));
            if(idList!=null && idList.size()>0){
                boolean bl = true;
                for(String id : idList){
                    PdProduct pdProduct = new PdProduct();
                    LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
                    pdProduct.setDepartParentId(sysUser.getDepartParentId());
                    pdProduct.setVenderId(id);
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
    public Result<Object> importExcel(Map<String, MultipartFile> fileMap)
    {
        PdVenderMapper dao = sqlsession.getMapper(PdVenderMapper.class);
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        List<PdVender> list = new ArrayList<>();
        boolean bl = true;
        String message = "表格内没有内容";
        for (Map.Entry<String, MultipartFile> entity : fileMap.entrySet()) {
            MultipartFile file = entity.getValue();// 获取上传文件对象
            ImportParams params = new ImportParams();
            params.setTitleRows(2);
            params.setHeadRows(1);
            params.setNeedSave(true);
            try {
                list = ExcelImportUtil.importExcel(file.getInputStream(), PdVender.class, params);
                int i = 0;
                //查询所有的产品
                PdVender pdVender = new PdVender();
                pdVender.setDepartParentId(sysUser.getDepartParentId());
                List<PdVender> pdVenders = this.selectList(pdVender);
                for(PdVender ps :list){
                    if(oConvertUtils.isNotEmpty(ps.getName())){
                        if(checkName(pdVenders,ps)){
                            pdVenders.add(ps);
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
     * 校验名称是否重复
     * @param pdVenders
     * @param pdVender
     * @return
     */
    private boolean checkName(List<PdVender> pdVenders, PdVender pdVender) {
        for (PdVender p : pdVenders) {
            if(p.getName().equals(pdVender.getName().trim())){
                return false;
            }
        }
        return true;
    }
}
