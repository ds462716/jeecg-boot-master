package org.jeecg.modules.external.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.constant.PdConstant;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.modules.external.entity.ExDeductuinDosage;
import org.jeecg.modules.external.mapper.ExDeductuinDosageMapper;
import org.jeecg.modules.external.service.IExDeductuinDosageService;
import org.jeecg.modules.pd.entity.PdProductStock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * @Description: 试剂用量扣减记录表
 * @Author: jiangxz
 * @Date:   2020-05-22
 * @Version: V1.0
 */
@Service
public class ExDeductuinDosageServiceImpl extends ServiceImpl<ExDeductuinDosageMapper, ExDeductuinDosage> implements IExDeductuinDosageService {
    @Autowired
    private ExDeductuinDosageMapper deductuinDosageMapper;


    @Override
    public Page<ExDeductuinDosage> selectList(Page<ExDeductuinDosage> page, ExDeductuinDosage deductuinDosage) {
        return deductuinDosageMapper.selectListByPage(page,deductuinDosage);
    }



    /**
     * 人工扣减试剂用量记录
     * @param stock
     */
  @Override
  public void saveExdeuctuinDosage(PdProductStock stock){
      LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
      ExDeductuinDosage deductuinDosage=new ExDeductuinDosage();
      deductuinDosage.setDepartId(stock.getDepartId());//所属科室ID
      deductuinDosage.setDepartParentId(stock.getDepartParentId());//所属机构ID
      deductuinDosage.setProductId(stock.getProductId());//产品ID
      deductuinDosage.setExpDate(stock.getExpDate());//产品有效期
      deductuinDosage.setBatchNo(stock.getBatchNo());//产品批次号
      deductuinDosage.setProductBarCode(stock.getProductBarCode());//产品条码
      deductuinDosage.setPatientName("");//病人姓名
      deductuinDosage.setInHospitalNo("");//住院号
      deductuinDosage.setOutpatientNumber("");//门诊号
      deductuinDosage.setDeductuinType(PdConstant.DEDUCTUIN_TYPE_1);
      deductuinDosage.setSpecUnitId(stock.getSpecUnitId());  //规格单位
      deductuinDosage.setSpecQuantity(stock.getSpecQuantity());//规格数量
      deductuinDosage.setSpecNum(stock.getProductNum());//扣减规格数量
      deductuinDosage.setSpecDate(new Date());//扣减日期
      deductuinDosage.setStockId(stock.getId());//对应库存明细ID
      deductuinDosage.setPersonName(sysUser.getRealname());//操作人
      deductuinDosageMapper.insert(deductuinDosage);
   }

    /**
     * 系统自动扣减试剂用量记录
     * @param stock
     */
    @Override
    public void saveExdeuctuinDosageAcc(PdProductStock stock){
        ExDeductuinDosage deductuinDosage=new ExDeductuinDosage();
        deductuinDosage.setDepartId(stock.getDepartId());//所属科室ID
        deductuinDosage.setDepartParentId(stock.getDepartParentId());//所属机构ID
        deductuinDosage.setProductId(stock.getProductId());//产品ID
        deductuinDosage.setExpDate(stock.getExpDate());//产品有效期
        deductuinDosage.setBatchNo(stock.getBatchNo());//产品批次号
        deductuinDosage.setProductBarCode(stock.getProductBarCode());//产品条码
        deductuinDosage.setPatientName("");//病人姓名
        deductuinDosage.setInHospitalNo("");//住院号
        deductuinDosage.setOutpatientNumber("");//门诊号
        deductuinDosage.setDeductuinType(PdConstant.DEDUCTUIN_TYPE_0);
        deductuinDosage.setSpecUnitId(stock.getSpecUnitId());  //规格单位
        deductuinDosage.setSpecQuantity(stock.getSpecQuantity());//规格数量
        deductuinDosage.setSpecNum(stock.getProductNum());//扣减规格数量
        deductuinDosage.setSpecDate(new Date());//扣减日期
        deductuinDosage.setStockId(stock.getId());//对应库存明细ID
        deductuinDosage.setPersonName("定时任务");//操作人
        deductuinDosageMapper.insert(deductuinDosage);
    }

}
