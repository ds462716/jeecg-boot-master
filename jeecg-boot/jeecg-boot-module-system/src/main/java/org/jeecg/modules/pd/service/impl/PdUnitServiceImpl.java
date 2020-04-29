package org.jeecg.modules.pd.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.commons.collections.CollectionUtils;
import org.apache.ibatis.session.SqlSession;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.modules.pd.entity.PdEncodingRule;
import org.jeecg.modules.pd.entity.PdProduct;
import org.jeecg.modules.pd.entity.PdUnit;
import org.jeecg.modules.pd.mapper.PdUnitMapper;
import org.jeecg.modules.pd.service.IPdProductService;
import org.jeecg.modules.pd.service.IPdUnitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

/**
 * @Description: 产品单位表
 * @Author: jeecg-boot
 * @Date:   2020-01-08
 * @Version: V1.0
 */
@Service
public class PdUnitServiceImpl extends ServiceImpl<PdUnitMapper, PdUnit> implements IPdUnitService {
    @Autowired
    private PdUnitMapper pdUnitMapper;

    @Autowired
    private IPdProductService pdProductService;

    @Autowired
    private SqlSession sqlsession;

    @Override
    public List<PdUnit> queryList(PdUnit pdUnit) {
        return pdUnitMapper.queryList(pdUnit);
    }

    @Override
    public Page<PdUnit> queryList(Page<PdUnit> page, PdUnit pdUnit) {
        return pdUnitMapper.selectListByPage(page,pdUnit);
    }

    @Override
    public List<PdUnit> verify(PdUnit pdUnit) {
        return pdUnitMapper.verify(pdUnit);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Result<Object> deleteV(String id) {
        try{
            PdProduct pdProduct = new PdProduct();
            LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
            pdProduct.setDepartParentId(sysUser.getDepartParentId());
            pdProduct.setUnitId(id);
            List<PdProduct> pdProducts = pdProductService.selectListByCT(pdProduct);
            if(CollectionUtils.isNotEmpty(pdProducts)){
                return Result.error("删除失败!，当前单位被使用不能删除");
            }
            this.removeById(id);
            return Result.ok("删除成功!");
        }catch(Exception e){
            e.printStackTrace();
            return Result.error("删除失败!，系统异常");
        }

    }

    /**
     * 批量删除组别和校验
     * @param ids
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public Result<Object> deleteBatchV(String ids) {
        try{
            PdUnitMapper dao = sqlsession.getMapper(PdUnitMapper.class);
            List<String> idList = Arrays.asList(ids.split(","));
            if(idList!=null && idList.size()>0){
                boolean bl = true;
                for(String id : idList){
                    PdProduct pdProduct = new PdProduct();
                    LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
                    pdProduct.setDepartParentId(sysUser.getDepartParentId());
                    pdProduct.setUnitId(id);
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
}
