package org.jeecg.modules.external.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.jeecg.common.constant.PdConstant;
import org.jeecg.modules.external.entity.ExInspectionInf;
import org.jeecg.modules.external.entity.ExInspectionItems;
import org.jeecg.modules.external.mapper.ExInspectionItemsMapper;
import org.jeecg.modules.external.service.IExInspectionInfService;
import org.jeecg.modules.external.service.IExInspectionItemsService;
import org.jeecg.modules.pd.entity.PdUsePackage;
import org.jeecg.modules.pd.entity.PdUsePackageDetail;
import org.jeecg.modules.pd.service.IPdProductStockTotalService;
import org.jeecg.modules.pd.service.IPdUsePackageDetailService;
import org.jeecg.modules.pd.service.IPdUsePackageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * @Description: 检查项目表
 * @Author: jiangxz
 * @Date:   2020-04-23
 * @Version: V1.0
 */
@Service
public class ExInspectionItemsServiceImpl extends ServiceImpl<ExInspectionItemsMapper, ExInspectionItems> implements IExInspectionItemsService {

    @Autowired
    private ExInspectionItemsMapper exInspectionItemsMapper;
    @Autowired
    private IPdUsePackageService pdUsePackageService;
    @Autowired
    private IPdUsePackageDetailService pdUsePackageDetailService;
    @Autowired
    private IPdProductStockTotalService pdProductStockTotalService;
    @Autowired
    private IExInspectionInfService exInspectionInfService;
    /**
     * 查询列表
     * @param page
     * @param exInspectionItems
     * @return
     */
    @Override
    public Page<ExInspectionItems> selectList(Page<ExInspectionItems> page, ExInspectionItems exInspectionItems) {
            return exInspectionItemsMapper.selectListByPage(page,exInspectionItems);
    }

    @Override
    public List<ExInspectionItems> selectList(ExInspectionItems exInspectionItems) {
        return exInspectionItemsMapper.selectList(exInspectionItems);
    }

    @Override
    public List<String> selectListIds() {
        return exInspectionItemsMapper.selectListIds();
    }

    @Override
    public void batchUsePackageDetail(String ids) {
        String[] idList = ids.split(",");
        for(int i = 0 ; i < idList.length ; i ++){
            ExInspectionItems items=  exInspectionItemsMapper.selectById(idList[i]);
            if(PdConstant.ACCEPT_STATUS_0.equals(items.getAcceptStatus())){
                break;
            }
            if(StringUtils.isEmpty(items.getTestItemCode())){
                break;
            }
            LambdaQueryWrapper<PdUsePackage> query = new LambdaQueryWrapper<>();
            query.eq(PdUsePackage::getCode, items.getTestItemCode());
            /*query.eq(PdUsePackage::getName,items.getTestItemName());*/
            PdUsePackage pdUsePackage = pdUsePackageService.getOne(query);
            //不存在或沒有配置檢驗用量明細
            if(pdUsePackage!=null){
                String testDpeartId=pdUsePackage.getTestDepartId();
                String deductuinType=pdUsePackage.getDeductuinType();
                if(StringUtils.isEmpty(testDpeartId)){
                    items.setRemarks("未配置检验科室");
                    items.setAcceptStatus(PdConstant.ACCEPT_STATUS_2);// 0：已扣减  1：未配置检验用量  2:未扣减
                }else if (PdConstant.DEDUCTUIN_TYPE_1.equals(deductuinType)) {
                    items.setRemarks("需人工扣减:" + pdUsePackage.getRemarks());
                    items.setAcceptStatus(PdConstant.ACCEPT_STATUS_2);// 0:已扣减  1：无检验项目  2：未扣减  3：无试剂用量
                } else if (PdConstant.DEDUCTUIN_TYPE_2.equals(deductuinType)) {
                    items.setRemarks("无需扣减:" + pdUsePackage.getRemarks());
                    items.setAcceptStatus(PdConstant.ACCEPT_STATUS_2);// 0:已扣减  1：无检验项目  2：未扣减  3：无试剂用量
                }else {
                    ExInspectionInf inspectionInf=new ExInspectionInf();
                    inspectionInf.setCode(items.getTestItemCode());
                    inspectionInf.setJyId(items.getJyId());
                    List<PdUsePackageDetail> pdUsePackageDetails = exInspectionInfService.queryPdUsePackageList(inspectionInf);
                    if (pdUsePackageDetails != null && pdUsePackageDetails.size() > 0) {
                        try {
                            String bool=PdConstant.TRUE;
                            Iterator<PdUsePackageDetail> it = pdUsePackageDetails.iterator();
                            while(it.hasNext()){ // remove掉已经扣减的试剂
                                PdUsePackageDetail detail= it.next();
                                String status=detail.getStatus();
                                if(StringUtils.isNotEmpty(status) && "0".equals(status)){
                                    bool=PdConstant.FALSE;
                                    it.remove();
                                }
                            }
                            Map map = pdProductStockTotalService.lisUpdateUseStock(items, testDpeartId, pdUsePackageDetails);
                            String code= MapUtils.getString(map,"code");
                            String msg=MapUtils.getString(map,"msg");
                            if ("400".equals(code)) {
                                items.setRemarks(items.getPatientType() + "病人用量未配置");
                                items.setAcceptStatus(PdConstant.ACCEPT_STATUS_2);//未扣减
                            } else if("300".equals(code)) {
                                items.setRemarks(msg);
                                if(bool.equals(PdConstant.FALSE)) {
                                    items.setAcceptStatus(PdConstant.ACCEPT_STATUS_4);//部分扣减
                                }else{
                                    items.setAcceptStatus(PdConstant.ACCEPT_STATUS_2);//未扣减
                                }
                            }else if("500".equals(code)) {
                                items.setRemarks(" ");
                                items.setAcceptStatus(PdConstant.ACCEPT_STATUS_4);//部分扣减
                            }else{
                                items.setRemarks(" ");
                                items.setAcceptStatus(PdConstant.ACCEPT_STATUS_0);//已扣减
                            }
                        } catch (Exception e) {
                            e.getMessage();
                             log.error("扣減用量失敗:" + e.getMessage());
                            items.setRemarks(e.getMessage());
                            items.setAcceptStatus(PdConstant.ACCEPT_STATUS_2);
                        }
                    } else {
                        items.setRemarks("检验项目用量未配置:"+pdUsePackage.getRemarks());
                        items.setAcceptStatus(PdConstant.ACCEPT_STATUS_3);// 0：已扣减  1：未配置检验用量  2:未扣减
                    }
                }
            }else{
                items.setRemarks("检验项目未配置");
                items.setAcceptStatus(PdConstant.ACCEPT_STATUS_1);// 0：已扣减  1：未配置检验用量  2:未扣减
            }
            exInspectionItemsMapper.updateById(items);
      }
    }
}
