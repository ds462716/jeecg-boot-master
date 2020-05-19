package org.jeecg.modules.external.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.external.entity.ExInspectionItemsUse;
import org.jeecg.modules.external.entity.ExInspectionItemsUseDetail;
import org.jeecg.modules.external.mapper.ExInspectionItemsUseMapper;
import org.jeecg.modules.external.service.IExInspectionItemsUseDetailService;
import org.jeecg.modules.external.service.IExInspectionItemsUseService;
import org.jeecg.modules.pd.entity.PdProductStock;
import org.jeecg.modules.pd.entity.PdUsePackageDetail;
import org.jeecg.modules.pd.service.IPdProductStockTotalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.beans.BeanUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * @Description: 检验项目使用表
 * @Author: zxh
 * @Date:   2020-05-11
 * @Version: V1.0
 */
@Service
public class ExInspectionItemsUseServiceImpl extends ServiceImpl<ExInspectionItemsUseMapper, ExInspectionItemsUse> implements IExInspectionItemsUseService {

    @Autowired
    private IPdProductStockTotalService pdProductStockTotalService;

    @Autowired
    private IExInspectionItemsUseDetailService exInspectionItemsUseDetailService;


    //检验项目手动保存
    @Transactional
    @Override
    public void submit(ExInspectionItemsUse exInspectionItemsUse) {
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        String departId = sysUser.getCurrentDepartId();
        List<PdProductStock> pdProductStockAll = new ArrayList<>();
        List<ExInspectionItemsUseDetail> exInspectionItemsUseAll = new ArrayList<>();
        //需要放在同一个事物里面

        //选择定数包的产品
        if(exInspectionItemsUse.getPdUsePackageDetails()!=null && exInspectionItemsUse.getPdUsePackageDetails().size()>0){
            List<PdProductStock> pdProductStocks  = pdProductStockTotalService.jyUpdatePackageStockNum(departId,exInspectionItemsUse.getPdUsePackageDetails());
            pdProductStockAll.addAll(pdProductStocks);
        }
        //手动选择的库存
        if(exInspectionItemsUse.getExInspectionItemsUseDetails()!=null && exInspectionItemsUse.getExInspectionItemsUseDetails().size()>0){
            pdProductStockTotalService.jyUpdateProductStockNum(departId,exInspectionItemsUse.getExInspectionItemsUseDetails());
            pdProductStockAll.addAll(exInspectionItemsUse.getExInspectionItemsUseDetails());
        }
        if(pdProductStockAll != null && pdProductStockAll.size()>0){
            this.save(exInspectionItemsUse);
            for(PdProductStock pdProductStock :pdProductStockAll){
                ExInspectionItemsUseDetail eiu = new ExInspectionItemsUseDetail();
                BeanUtils.copyProperties(pdProductStock, eiu);
                eiu.setRefId(exInspectionItemsUse.getId());
                eiu.setProductStockId(pdProductStock.getId());
                eiu.setId("");
                exInspectionItemsUseAll.add(eiu);
            }
            exInspectionItemsUseDetailService.saveBatch(exInspectionItemsUseAll);
        }

    }

    @Override
    public ExInspectionItemsUse initOutModal(String id) {
        ExInspectionItemsUse exInspectionItemsUse = this.getById(id);

        ExInspectionItemsUseDetail exInspectionItemsUseDetail = new ExInspectionItemsUseDetail();
        exInspectionItemsUseDetail.setRefId(id);
        exInspectionItemsUseDetail.setPackageTrueFlag("true");
        //查询定数包消耗的产品
        List<ExInspectionItemsUseDetail> exInspectionItemsUseDetails = exInspectionItemsUseDetailService.selectList(exInspectionItemsUseDetail);
        if(exInspectionItemsUseDetails!=null && exInspectionItemsUseDetails.size()>0){
            exInspectionItemsUse.setPakageUseDetailList(exInspectionItemsUseDetails);
        }
        //查询消耗的产品
        ExInspectionItemsUseDetail eiud = new ExInspectionItemsUseDetail();
        eiud.setRefId(id);
        eiud.setPackageFalseFlag("true");
        //查询定数包消耗的产品
        List<ExInspectionItemsUseDetail> exInspectionItemsUseDetailList = exInspectionItemsUseDetailService.selectList(eiud);
        if(exInspectionItemsUseDetailList!=null && exInspectionItemsUseDetailList.size()>0){
            exInspectionItemsUse.setUseDetailList(exInspectionItemsUseDetailList);
        }
        return exInspectionItemsUse;
    }
}
