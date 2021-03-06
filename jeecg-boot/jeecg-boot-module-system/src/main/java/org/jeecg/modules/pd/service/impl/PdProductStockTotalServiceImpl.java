package org.jeecg.modules.pd.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.constant.PdConstant;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.external.entity.ExInspectionInf;
import org.jeecg.modules.external.entity.ExInspectionItems;
import org.jeecg.modules.external.entity.PdBottleInf;
import org.jeecg.modules.external.mapper.PdBottleInfMapper;
import org.jeecg.modules.external.service.IExDeductuinDosageService;
import org.jeecg.modules.external.service.IExInspectionInfService;
import org.jeecg.modules.pd.entity.*;
import org.jeecg.modules.pd.mapper.PdProductMapper;
import org.jeecg.modules.pd.mapper.PdProductStockMapper;
import org.jeecg.modules.pd.mapper.PdProductStockTotalMapper;
import org.jeecg.modules.pd.mapper.PdProductStockUniqueCodeMapper;
import org.jeecg.modules.pd.service.*;
import org.jeecg.modules.pd.vo.PdProductStockTotalPage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * @Description: 库存总表
 * @Author: jeecg-boot
 * @Date: 2020-02-11
 * @Version: V1.0
 */
@Service
public class PdProductStockTotalServiceImpl extends ServiceImpl<PdProductStockTotalMapper, PdProductStockTotal> implements IPdProductStockTotalService {
    @Autowired
    private IPdProductStockService productStockService;
    @Autowired
    private PdProductStockTotalMapper pdProductStockTotalMapper;
    @Autowired
    private PdProductStockMapper pdProductStockMapper;
    @Autowired
    private PdProductMapper pdProductMapper;
    @Autowired
    private IPdOnOffService pdOnOffService;
    @Autowired
    private IPdUsePackageService pdUsePackageService;
    @Autowired
    private IHisDepartService hisDepartService;
    @Autowired
    private IPdSpecLogService pdSpecLogService;
    @Autowired
    private IExDeductuinDosageService exDeductuinDosageService;
    @Autowired
    private PdBottleInfMapper pdBottleInfMapper;
    @Autowired
    private PdProductStockUniqueCodeMapper pdProductStockUniqueCodeMapper;
    @Autowired
    private IExInspectionInfService exInspectionInfService;
    /**
     * 查询列表
     *
     * @param page
     * @param stockTotal
     * @return
     */
    @Override
    public Page<PdProductStockTotalPage> selectList(Page<PdProductStockTotalPage> page, PdProductStockTotal stockTotal) {
        return pdProductStockTotalMapper.selectListByPage(page,stockTotal);
    }

    @Override
    public List<PdProductStockTotalPage> findListForQuery(PdProductStockTotal stockTotal) {
        return pdProductStockTotalMapper.selectList(stockTotal);
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateProductStockTotal(PdProductStockTotal stockTotal) {
        pdProductStockTotalMapper.updateProductStockTotal(stockTotal);
    }

    /**
     * 入库保存库存 add by jiangxz 2020年8月20日 16:47:53
     * @param pdStockRecord
     * @param stockRecordDetail
     * @return
     */
    public PdProductStock saveStock(PdStockRecord pdStockRecord,PdStockRecordDetail stockRecordDetail){
        String inDeptId = pdStockRecord.getInDepartId();
        String productId = stockRecordDetail.getProductId();            //产品ID
        String productBarCode = stockRecordDetail.getProductBarCode();  //产品条码
        String productName = stockRecordDetail.getProductName();  //产品名称
        String batchNo = stockRecordDetail.getBatchNo();
        Double productNum = stockRecordDetail.getProductNum();  //数量
        String inHuoweiCode = stockRecordDetail.getInHuoweiCode(); //货位编号

        PdProductStock productStock = new PdProductStock();
        productStock.setDepartId(inDeptId);
        productStock.setDepartParentId(pdStockRecord.getDepartParentId());
        productStock.setProductId(productId);
        productStock.setProductBarCode(productBarCode);
        productStock.setStockNum(productNum);
        productStock.setPurchasePrice(stockRecordDetail.getPurchasePrice());
        productStock.setProductName(productName);
        productStock.setBatchNo(batchNo);
        productStock.setHuoweiCode(inHuoweiCode);
        productStock.setExpDate(stockRecordDetail.getExpDate());
        productStock.setProduceDate(stockRecordDetail.getProduceDate()); // 生产日期
        if(oConvertUtils.isNotEmpty(pdStockRecord.getOutType())){
            // 出库入库
            productStock.setRecordId(stockRecordDetail.getInRecordId());
            productStock.setRecordDetailId(stockRecordDetail.getInRecordDetailId());
        }else{
            // 供应商入库
            productStock.setRecordId(pdStockRecord.getId());
            productStock.setRecordDetailId(stockRecordDetail.getId()); //入库明细ID
        }
        productStock.setPackageRecordId(stockRecordDetail.getPackageRecordId());
        productStock.setPackageRecordDetailId(stockRecordDetail.getPackageRecordDetailId());
        productStock.setSupplierId(stockRecordDetail.getSupplierId());
        productStock.setDistributorId(stockRecordDetail.getDistributorId());
        productStock.setSpecQuantity(stockRecordDetail.getSpecQuantity());
        productStock.setSpecUnitId(stockRecordDetail.getSpecUnitId());
        productStock.setSpecNum(stockRecordDetail.getSpecQuantity() == null ? 0D : stockRecordDetail.getSpecQuantity() * stockRecordDetail.getProductNum());// 库存规格数量= 产品规格数量* 入库数量
        if(PdConstant.CODE_PRINT_TYPE_1.equals(stockRecordDetail.getBarCodeType())){
            // 唯一码入库
            productStock.setBarCodeType(stockRecordDetail.getBarCodeType());
        }else{
            // 批量码入库
            productStock.setRefBarCode(stockRecordDetail.getRefBarCode());
        }
        productStockService.save(productStock);
        return productStock;
    }

    /***
     * 	耗材入库更新库存信息
     *
     * @param   pdStockRecord    入库记录
     * @return String   更新库存结果  入库成功，返回字符串“true”，否则返回错误信息
     */
    @Transactional(rollbackFor = Exception.class)
    public String updateInStock(PdStockRecord pdStockRecord) {

        if (pdStockRecord == null || CollectionUtils.isEmpty(pdStockRecord.getPdStockRecordDetailList())) {
            return "参数有误";
        }

        String inDeptId = pdStockRecord.getInDepartId();
        List<PdStockRecordDetail> stockRecordDetails = pdStockRecord.getPdStockRecordDetailList();
        String stockId = null;
        for (PdStockRecordDetail stockRecordDetail : stockRecordDetails) {
            String productId = stockRecordDetail.getProductId();            //产品ID
            String productBarCode = stockRecordDetail.getProductBarCode();  //产品条码
            String productName = stockRecordDetail.getProductName();  //产品名称
            String batchNo = stockRecordDetail.getBatchNo();
            Double productNum = stockRecordDetail.getProductNum();  //数量
            String inHuoweiCode = stockRecordDetail.getInHuoweiCode(); //货位编号
            //2、增加入库库存
            PdProductStockTotal stockTotalqi = new PdProductStockTotal();
            stockTotalqi.setDepartId(inDeptId);
            stockTotalqi.setProductId(productId);
            //先查询库存总表下是否存在该产品；
            List<PdProductStockTotal> i_productStockTotals = pdProductStockTotalMapper.findForUpdate(stockTotalqi);
            //如果库存总表不存在产品，则新增产品库存总表信息
            if (CollectionUtils.isEmpty(i_productStockTotals) || i_productStockTotals.size() == 0) {
                PdProductStockTotal productStockTotal = new PdProductStockTotal();
                productStockTotal.setDepartId(inDeptId);  //库房
                productStockTotal.setProductId(productId);    //产品ID
                productStockTotal.setStockNum(productNum);    //入库数量
                productStockTotal.setDepartParentId(pdStockRecord.getDepartParentId());
                productStockTotal.setSupplierId(stockRecordDetail.getSupplierId());   //供应商
                super.save(productStockTotal);
            } else { //如果库存总表存在，则增加库存数量
                PdProductStockTotal productStockTotal = i_productStockTotals.get(0);
                productStockTotal.setStockNum(productNum + productStockTotal.getStockNum());
                pdProductStockTotalMapper.updateStockNum(productStockTotal);
            }

            // modified by jiangxz 2020年8月20日 17:07:59 二级科室入库 合并同入库明细产品
            PdProductStock productStock = new PdProductStock();
//            if(oConvertUtils.isNotEmpty(pdStockRecord.getOutType())){
            if(PdConstant.RECODE_TYPE_2.equals(pdStockRecord.getRecordType())){
                // 2.1 出库入库 (上级科室出库到下级科室) 合并同一个供应商入库单同批号的产品
                if (StringUtils.isBlank(stockRecordDetail.getInRecordDetailId())) {
                    return "参数有误，入库明细id为空！";
                }
                PdProductStock i_productStockq = new PdProductStock();
                i_productStockq.setDepartId(inDeptId);
                i_productStockq.setRecordDetailId(stockRecordDetail.getInRecordDetailId()); //入库明细id
                i_productStockq.setPackageRecordDetailId(stockRecordDetail.getPackageRecordDetailId());// 套包记录明细id
                i_productStockq.setHuoweiCode(inHuoweiCode);
                i_productStockq.setBarCodeType(pdStockRecord.getBarCodeType()); // 0：普通条码 1：唯一码
                i_productStockq.setNestatStatus(PdConstant.STOCK_NESTAT_STATUS_1);//未使用
                List<PdProductStock> i_productStocks = pdProductStockMapper.findForInsert(i_productStockq);
                if(CollectionUtils.isEmpty(i_productStocks)){
                    // 若库存明细表不存在，则新增入库
                    productStock = this.saveStock(pdStockRecord,stockRecordDetail);
                    stockId = productStock.getId();
                }else{
                    // 若存在，则增加库存数量
                    PdProductStock updateStock = i_productStocks.get(0);
                    updateStock.setStockNum(productNum + updateStock.getStockNum());
                    updateStock.setUpdateTime(new Date());
                    pdProductStockMapper.updateStockNum(updateStock);
                    stockId = updateStock.getId();
                }
//            }else if(PdConstant.RECODE_TYPE_4.equals(pdStockRecord.getRecordType())){
            }else{
                // 2.2 供应商入库、盘盈入库、初始化库存 add by jiangxz 2020年8月20日 16:47:53
                productStock = this.saveStock(pdStockRecord,stockRecordDetail);
                stockId = productStock.getId();
            }

            if(PdConstant.CODE_PRINT_TYPE_1.equals(pdStockRecord.getBarCodeType())){
                // 唯一码入库，更新条码表库存id
                if(oConvertUtils.isNotEmpty(stockRecordDetail.getRefBarCode())){
                    List<String> refBarCodeList = Arrays.asList(stockRecordDetail.getRefBarCode().split(","));
                    for(String refBarCode : refBarCodeList){
                        PdProductStockUniqueCode code = new PdProductStockUniqueCode();
                        code.setId(refBarCode);
                        code.setProductStockId(stockId);
                        pdProductStockUniqueCodeMapper.updateById(code);
                    }
                }
            }
        }
        return PdConstant.TRUE;
    }


    /***
     * 	库存出库更新库存信息
     *
     * @param pdStockRecord   出库明细列表
     * @return String   更新库存结果  入库成功，返回字符串“true”，否则返回错误信息
     */
    @Transactional
    public String updateOutStock(PdStockRecord pdStockRecord) {
        if (pdStockRecord == null || CollectionUtils.isEmpty(pdStockRecord.getPdStockRecordDetailList())) {
            return "参数有误";
        }

        String outDeptId = pdStockRecord.getOutDepartId();
        List<PdStockRecordDetail> stockRecordDetails = pdStockRecord.getPdStockRecordDetailList();

        for (PdStockRecordDetail stockRecordDetail : stockRecordDetails) {
            String productId = stockRecordDetail.getProductId();     //产品ID
            Double productNum = stockRecordDetail.getProductNum();  //数量

            PdProductStockTotalPage stockTotalq = new PdProductStockTotalPage();
            stockTotalq.setDepartId(outDeptId);
            stockTotalq.setProductId(productId);
            stockTotalq.setFilterType("1");//有值的话，则过滤库存数量为0的数据
            List<PdProductStockTotalPage> productStockTotals = pdProductStockTotalMapper.selectList(stockTotalq);
            // 扣减总库存
            if (CollectionUtils.isNotEmpty(productStockTotals) && productStockTotals.size() == 1) {
                PdProductStockTotal productStockTotal = productStockTotals.get(0);
                Double num = productStockTotal.getStockNum() - productNum;
                if (num < 0) {
                    throw new RuntimeException("扣减总库存失败，[" + stockRecordDetail.getProductName() + "]总库存数量不足");
                }
                productStockTotal.setStockNum(num);
                pdProductStockTotalMapper.updateStockNum(productStockTotal);
            } else {
                throw new RuntimeException("库存没有产品[" + stockRecordDetail.getProductName() + "]！");
            }

            PdProductStock o_productStockq = new PdProductStock();
            o_productStockq.setId(stockRecordDetail.getProductStockId());
            o_productStockq.setNestatStatus(PdConstant.STOCK_NESTAT_STATUS_1);
            List<PdProductStock> productStocks = pdProductStockMapper.selectList(o_productStockq);
            // 扣减库存
            if (CollectionUtils.isNotEmpty(productStocks) && productStocks.size() >= 1) {
                PdProductStock productStock = productStocks.get(0);
                Double num = productStock.getStockNum() - productNum;
                if (num < 0) {
                    throw new RuntimeException("扣减库存失败，[" + stockRecordDetail.getProductName() + "]库存数量不足");
                }
                productStock.setStockNum(num);
                productStock.setSpecNum(stockRecordDetail.getSpecQuantity() == null ? 0D : stockRecordDetail.getSpecQuantity() * num);// 库存规格数量= 产品规格数量* 入库数量

                pdProductStockMapper.updateStockNum(productStock);
            } else {
                throw new RuntimeException("库存没有产品[" + stockRecordDetail.getProductName() + "]！");
            }
        }

        return PdConstant.TRUE;
    }

    /**
     * 套包拆包加库存
     * @param pdPackageRecord
     * @return
     */
    @Transactional
    @Override
    public String updateInStockForPackage(PdPackageRecord pdPackageRecord) {
        if (pdPackageRecord == null || CollectionUtils.isEmpty(pdPackageRecord.getPdPackageRecordDetailList())) {
            return "参数有误";
        }

        String outDeptId = pdPackageRecord.getDepartId();
        List<PdPackageRecordDetail> packageRecordDetails = pdPackageRecord.getPdPackageRecordDetailList();

        for (PdPackageRecordDetail pdPackageRecordDetail : packageRecordDetails) {
            String productId = pdPackageRecordDetail.getProductId();     //产品ID
            Double packageNum = pdPackageRecordDetail.getPackageNum();  //数量

            PdProductStockTotalPage stockTotalq = new PdProductStockTotalPage();
            stockTotalq.setDepartId(outDeptId);
            stockTotalq.setProductId(productId);
//            stockTotalq.setFilterType("1");//有值的话，则过滤库存数量为0的数据
            List<PdProductStockTotalPage> productStockTotals = pdProductStockTotalMapper.selectList(stockTotalq);
            // 扣减总库存
            if (CollectionUtils.isNotEmpty(productStockTotals) && productStockTotals.size() == 1) {
                PdProductStockTotal productStockTotal = productStockTotals.get(0);
                Double num = productStockTotal.getStockNum() + packageNum;
                productStockTotal.setStockNum(num);
                pdProductStockTotalMapper.updateStockNum(productStockTotal);
            } else {
                throw new RuntimeException("库存没有产品[" + pdPackageRecordDetail.getProductName() + "]！");
            }

            PdProductStock o_productStockq = new PdProductStock();
            o_productStockq.setId(pdPackageRecordDetail.getProductStockId());
            o_productStockq.setNestatStatus(PdConstant.STOCK_NESTAT_STATUS_1);
//            List<PdProductStock> productStocks = pdProductStockMapper.selectList(o_productStockq);
            PdProductStock productStock = pdProductStockMapper.getOne(o_productStockq);
            // 扣减库存
//            if (CollectionUtils.isNotEmpty(productStocks)) {
            if (productStock != null && oConvertUtils.isNotEmpty(productStock.getId())) {
//                PdProductStock productStock = productStocks.get(0);
                Double num = productStock.getStockNum() + packageNum;
                productStock.setStockNum(num);
                productStock.setSpecNum(productStock.getSpecQuantity() == null ? 0D : productStock.getSpecQuantity() * num);// 库存规格数量= 产品规格数量* 入库数量

                pdProductStockMapper.updateStockNum(productStock);
            } else {
                throw new RuntimeException("库存没有产品[" + pdPackageRecordDetail.getProductName() + "]！");
            }
        }

        return PdConstant.TRUE;
    }

    /**
     * 套包打包扣库存
     * @param pdPackageRecord
     * @return
     */
    @Transactional
    @Override
    public String updateOutStockForPackage(PdPackageRecord pdPackageRecord) {
        if (pdPackageRecord == null || CollectionUtils.isEmpty(pdPackageRecord.getPdPackageRecordDetailList())) {
            return "参数有误";
        }

        String outDeptId = pdPackageRecord.getDepartId();
        List<PdPackageRecordDetail> packageRecordDetails = pdPackageRecord.getPdPackageRecordDetailList();

        for (PdPackageRecordDetail pdPackageRecordDetail : packageRecordDetails) {
            String productId = pdPackageRecordDetail.getProductId();     //产品ID
            Double packageNum = pdPackageRecordDetail.getPackageNum();  //数量

            PdProductStockTotalPage stockTotalq = new PdProductStockTotalPage();
            stockTotalq.setDepartId(outDeptId);
            stockTotalq.setProductId(productId);
            stockTotalq.setFilterType("1");//有值的话，则过滤库存数量为0的数据
            List<PdProductStockTotalPage> productStockTotals = pdProductStockTotalMapper.selectList(stockTotalq);
            // 扣减总库存
            if (CollectionUtils.isNotEmpty(productStockTotals) && productStockTotals.size() == 1) {
                PdProductStockTotal productStockTotal = productStockTotals.get(0);
                Double num = productStockTotal.getStockNum() - packageNum;
                if (num < 0) {
                    throw new RuntimeException("扣减总库存失败，[" + pdPackageRecordDetail.getProductName() + "]总库存数量不足");
                }
                productStockTotal.setStockNum(num);
                pdProductStockTotalMapper.updateStockNum(productStockTotal);
            } else {
                throw new RuntimeException("库存没有产品[" + pdPackageRecordDetail.getProductName() + "]！");
            }

            PdProductStock o_productStockq = new PdProductStock();
            o_productStockq.setId(pdPackageRecordDetail.getProductStockId());
            o_productStockq.setNestatStatus(PdConstant.STOCK_NESTAT_STATUS_1);
            List<PdProductStock> productStocks = pdProductStockMapper.selectList(o_productStockq);
            // 扣减库存
            if (CollectionUtils.isNotEmpty(productStocks)) {
                PdProductStock productStock = productStocks.get(0);
                Double num = productStock.getStockNum() - packageNum;
                if (num < 0) {
                    throw new RuntimeException("扣减库存失败，[" + pdPackageRecordDetail.getProductName() + "]库存数量不足");
                }
                productStock.setStockNum(num);
                productStock.setSpecNum(productStock.getSpecQuantity() == null ? 0D : productStock.getSpecQuantity() * num);// 库存规格数量= 产品规格数量* 入库数量

                pdProductStockMapper.updateStockNum(productStock);
            } else {
                throw new RuntimeException("库存没有产品[" + pdPackageRecordDetail.getProductName() + "]！");
            }
        }

        return PdConstant.TRUE;
    }

    /***
     * 	器械使用更新库存信息
     * @param departId     科室ID
     * @param dosageDetails   使用明细
     * @return String   更新库存结果
     */
    @Transactional
    @Override
    public String updateUseStock(String departId, List<PdDosageDetail> dosageDetails) {
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        //1、扣减出库库存，扣减出库库存明细
        for (PdDosageDetail dosageDetail : dosageDetails) {
            String productStockId = dosageDetail.getProductStockId();   //庫存明細ID
            String productId = dosageDetail.getProductId();    //产品ID
            Double productNum = dosageDetail.getDosageCount();  //数量
            Double specNum_i= dosageDetail.getSpecNum();  //库存规格数量
          //获取开关  开关是开，则扣减规格数量，如果开关是关，则扣减库存数量
            PdOnOff query = new PdOnOff();
            query.setDepartParentId(sysUser.getDepartParentId());
            query.setCode(PdConstant.ON_OFF_SPEC_NUM);
            PdOnOff pdOnOff = pdOnOffService.getOne(query);
            if (ObjectUtil.isNotEmpty(specNum_i) && specNum_i>0 &&
                    pdOnOff != null && pdOnOff.getValue() == PdConstant.ON_OFF_SPEC_NUM_1) { //是
                //根据库存明细ID获取库存明细信息，判断是否是在用状态，如果是未使用状态，则新增正在使用状态的明细信息，赋值唯一码；
                //扣减规格数量，扣减完成后，如果规格数量小于0或等于0，那么直接将库存数量设置为0，同时更新使用状态为已用完；
                //如果使用状态更新为已用完，则库存总表库存数量减一；
                PdProductStock o_productStockq = new PdProductStock();
                o_productStockq.setId(productStockId);
                List<PdProductStock> productStocks = pdProductStockMapper.selectList(o_productStockq);
                if (productStocks != null && productStocks.size() > 0) {
                    PdProductStock productStock = productStocks.get(0);
                    String nestatStatus = productStock.getNestatStatus();
                   if(PdConstant.STOCK_NESTAT_STATUS_1.equals(nestatStatus)){ //如果状态是未使用，则新增一条已使用的明细信息
                       productStock.setRefBarCode(dosageDetail.getRefBarCode());
                       PdProductStock stock=this.insertProdStock(productStock);
                        this.updateStock(stock,productNum);
                   }else{
                       this.updateStock(productStock,productNum);
                   }
                }else {
                    throw new RuntimeException("库存明细没有产品[" + dosageDetail.getProductName() + "]！");
                }
            }else{  //否
            //1、扣减出库库存，扣减出库库存明细
            PdProductStockTotalPage stockTotalq = new PdProductStockTotalPage();
            stockTotalq.setDepartId(departId);
            stockTotalq.setProductId(productId);
            stockTotalq.setFilterType("1");//有值的话，则过滤库存数量为0的数据
            List<PdProductStockTotalPage> productStockTotals = pdProductStockTotalMapper.selectList(stockTotalq);
            if (productStockTotals != null && productStockTotals.size() == 1) {
                PdProductStockTotal productStockTotal = productStockTotals.get(0);
                Double stockNum = productStockTotal.getStockNum();
                Double num = stockNum - productNum;
                productStockTotal.setStockNum(num);
                pdProductStockTotalMapper.updateStockNum(productStockTotal);
            }else {
                throw new RuntimeException("库存没有产品[" + dosageDetail.getProductName() + "]！");
            }

            PdProductStock o_productStockq = new PdProductStock();
            o_productStockq.setId(productStockId);
            //o_productStockq.setNestatStatus(PdConstant.STOCK_NESTAT_STATUS_1);//查询未使用的库存明细
            List<PdProductStock> productStocks = pdProductStockMapper.selectList(o_productStockq);
            if (productStocks != null && productStocks.size() > 0) {
                PdProductStock productStock = productStocks.get(0);
                Double stockNum = productStock.getStockNum();
                Double num = stockNum - productNum;
                productStock.setStockNum(num);
                pdProductStockMapper.updateStockNum(productStock);

                if(PdConstant.CODE_PRINT_TYPE_1.equals(productStock.getBarCodeType())) {
                    PdProductStockUniqueCode code = new PdProductStockUniqueCode();
                    code.setId(dosageDetail.getRefBarCode());
                    code.setProductStockId(productStock.getId());
                    code.setCodeState(PdConstant.CODE_PRINT_STATE_2);
                    pdProductStockUniqueCodeMapper.updateById(code);
                }
            }else {
                throw new RuntimeException("库存明细没有产品[" + dosageDetail.getProductName() + "]！");
            }
          }
        }
        return PdConstant.TRUE;
    }


  public void updateStock(PdProductStock stock,Double productNum){
      Double specNum= stock.getSpecNum();
      Double newSpecNum= specNum - productNum;
      if(newSpecNum<=0){
          //扣减库存明细数量
          stock.setNestatStatus(PdConstant.STOCK_NESTAT_STATUS_2);
          stock.setStockNum(0.00);
          stock.setSpecNum(0.00);
          pdProductStockMapper.updateById(stock);
          //扣减总库存数量
          PdProductStockTotalPage stockTotalq = new PdProductStockTotalPage();
          stockTotalq.setDepartId(stock.getDepartId());
          stockTotalq.setProductId(stock.getProductId());
          stockTotalq.setFilterType("1");//有值的话，则过滤库存数量为0的数据
          List<PdProductStockTotalPage> productStockTotals = pdProductStockTotalMapper.selectList(stockTotalq);
          if (productStockTotals != null && productStockTotals.size() == 1) {
              PdProductStockTotal productStockTotal = productStockTotals.get(0);
              Double stockNum = productStockTotal.getStockNum();
              Double num = stockNum -1;
              productStockTotal.setStockNum(num);
              pdProductStockTotalMapper.updateStockNum(productStockTotal);
          }else {
              throw new RuntimeException("总库存没有产品[" + stock.getProductName() + "]！");
          }
           //修改条码状态
          if(PdConstant.CODE_PRINT_TYPE_1.equals(stock.getBarCodeType())) {
              PdProductStockUniqueCode code = new PdProductStockUniqueCode();
              code.setId(stock.getRefBarCode());
              code.setProductStockId(stock.getId());
              code.setCodeState(PdConstant.CODE_PRINT_STATE_2);
              pdProductStockUniqueCodeMapper.updateById(code);
          }
      }else{
          stock.setSpecNum(newSpecNum);
          stock.setNestatStatus(PdConstant.STOCK_NESTAT_STATUS_0);
          pdProductStockMapper.updateById(stock);
      }
  }



    /***
     * 	用量退回更新库存信息
     * @param  departId     退回科室ID
     * @param  detailList    退回用量明细
     * @return String        更新库存结果
     */
    @Transactional
    @Override
    public String updateRetunuseStock(String departId, List<PdDosageDetail> detailList) {
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        //1、增加库存，增加库存明细
        for (PdDosageDetail drt : detailList) {
            String productStockId = drt.getProductStockId();   //庫存明細ID
            String productId = drt.getProductId();      //产品ID
            Double productNum_i = drt.getDosageCount();  //退回数量
            Double specNum_i = drt.getSpecNum();  //库存规格数量
            //获取是否是根据规格数量扣减库存开关  开关是开，则更新规格数量，如果开关是关，则更新库存数量
            PdOnOff query = new PdOnOff();
            query.setDepartParentId(sysUser.getDepartParentId());
            query.setCode(PdConstant.ON_OFF_SPEC_NUM);
            PdOnOff pdOnOff = pdOnOffService.getOne(query);
            if ((ObjectUtil.isNotEmpty(specNum_i) && specNum_i>0) && pdOnOff != null && pdOnOff.getValue() == PdConstant.ON_OFF_SPEC_NUM_1) { //是
                //根据库存明细ID获取库存明细信息，先判断库存数量是否为0，如果是0，则更新库存总表和库存明细表的库存数量；
                //在更新库存明细表的规格数量
                PdProductStock i_productStockq = new PdProductStock();
                i_productStockq.setId(productStockId);
                List<PdProductStock> i_productStocks = pdProductStockMapper.findForUpdate(i_productStockq);
                if (i_productStocks != null || i_productStocks.size() > 0) {
                    PdProductStock productStock = i_productStocks.get(0);
                    Double stockNum=productStock.getStockNum();
                    if(stockNum<=0){
                        PdProductStockTotalPage stockTotalq = new PdProductStockTotalPage();
                        stockTotalq.setDepartId(departId);
                        stockTotalq.setProductId(productId);
                        List<PdProductStockTotalPage> i_productStockTotals = pdProductStockTotalMapper.selectList(stockTotalq);
                        if (i_productStockTotals != null || i_productStockTotals.size() > 0) {
                            //如果库存总表存在，则增加库存数量
                            PdProductStockTotal productStockTotal = i_productStockTotals.get(0);
                            Double totalStockNum=productStockTotal.getStockNum()+1;
                            productStockTotal.setStockNum(totalStockNum);
                            pdProductStockTotalMapper.updateById(productStockTotal);
                        }
                        stockNum=1.0;
                    }
                    productStock.setStockNum(stockNum);
                    Double specNum= productStock.getSpecNum()+productNum_i;
                        productStock.setSpecNum(specNum);
                    productStock.setNestatStatus(PdConstant.STOCK_NESTAT_STATUS_0);
                    pdProductStockMapper.updateById(productStock);
                }
            }else {
                //2、增加入库库存
                PdProductStockTotalPage stockTotalq = new PdProductStockTotalPage();
                stockTotalq.setDepartId(departId);
                stockTotalq.setProductId(productId);
                List<PdProductStockTotalPage> i_productStockTotals = pdProductStockTotalMapper.selectList(stockTotalq);
                if (i_productStockTotals != null || i_productStockTotals.size() > 0) {
                    //如果库存总表存在，则增加库存数量
                    PdProductStockTotal productStockTotal = i_productStockTotals.get(0);
                    productStockTotal.setStockNum(productNum_i);
                    pdProductStockTotalMapper.addStock(productStockTotal);
                } else {
                    //
                }
                //增加入库库存明细
                PdProductStock i_productStockq = new PdProductStock();
                i_productStockq.setId(productStockId);
                i_productStockq.setNestatStatus(PdConstant.STOCK_NESTAT_STATUS_1);//查询未使用的库存明细
                List<PdProductStock> i_productStocks = pdProductStockMapper.findForUpdate(i_productStockq);
                if (i_productStocks != null || i_productStocks.size() > 0) {
                    //存在，则增加库存数量
                    PdProductStock productStock = i_productStocks.get(0);
                    productStock.setStockNum(productNum_i);
                    pdProductStockMapper.addStock(productStock);
                } else {
                    //
                }
            }
        }
        return "";
    }


    /***
     * 	唯一码用量退回更新库存信息
     * @param  departId     退回科室ID
     * @param  detailList    退回用量明细
     * @return String        更新库存结果
     */
    @Transactional
    @Override
    public String updateRefBarCodeRetunuseStock(String departId, List<PdDosageDetail> detailList) {
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        //1、增加库存，增加库存明细
        for (PdDosageDetail drt : detailList) {
            String productStockId = drt.getProductStockId();   //庫存明細ID
            String productId = drt.getProductId();      //产品ID
            Double productNum_i = drt.getDosageCount();  //退回数量
            Double specNum_i = drt.getSpecNum();  //库存规格数量
            //获取是否是根据规格数量扣减库存开关  开关是开，则更新规格数量，如果开关是关，则更新库存数量
            PdOnOff query = new PdOnOff();
            query.setDepartParentId(sysUser.getDepartParentId());
            query.setCode(PdConstant.ON_OFF_SPEC_NUM);
            PdOnOff pdOnOff = pdOnOffService.getOne(query);
            if ((ObjectUtil.isNotEmpty(specNum_i) && specNum_i>0) && pdOnOff != null && pdOnOff.getValue() == PdConstant.ON_OFF_SPEC_NUM_1) { //是
                //根据库存明细ID获取库存明细信息，先判断库存数量是否为0，如果是0，则更新库存总表和库存明细表的库存数量；
                //在更新库存明细表的规格数量
                PdProductStock i_productStockq = new PdProductStock();
                i_productStockq.setRefBarCode(drt.getRefBarCode());
                i_productStockq.setBarCodeType(PdConstant.CODE_PRINT_TYPE_1);
                List<PdProductStock> i_productStocks = pdProductStockMapper.findForUpdate(i_productStockq);
                if (i_productStocks != null && i_productStocks.size() > 0) {
                    PdProductStock productStock = i_productStocks.get(0);
                    Double stockNum=productStock.getStockNum();
                    if(stockNum<=0){
                        PdProductStockTotalPage stockTotalq = new PdProductStockTotalPage();
                        stockTotalq.setDepartId(departId);
                        stockTotalq.setProductId(productId);
                        List<PdProductStockTotalPage> i_productStockTotals = pdProductStockTotalMapper.selectList(stockTotalq);
                        if (i_productStockTotals != null || i_productStockTotals.size() > 0) {
                            //如果库存总表存在，则增加库存数量
                            PdProductStockTotal productStockTotal = i_productStockTotals.get(0);
                            Double totalStockNum=productStockTotal.getStockNum()+1;
                            productStockTotal.setStockNum(totalStockNum);
                            pdProductStockTotalMapper.updateById(productStockTotal);
                        }
                        stockNum=1.0;
                    }
                    productStock.setStockNum(stockNum);
                    Double specNum= productStock.getSpecNum()+productNum_i;
                    productStock.setSpecNum(specNum);
                    productStock.setNestatStatus(PdConstant.STOCK_NESTAT_STATUS_0);
                    pdProductStockMapper.updateById(productStock);
                }
            }else {
                //2、增加入库库存
                PdProductStockTotalPage stockTotalq = new PdProductStockTotalPage();
                stockTotalq.setDepartId(departId);
                stockTotalq.setProductId(productId);
                List<PdProductStockTotalPage> i_productStockTotals = pdProductStockTotalMapper.selectList(stockTotalq);
                if (i_productStockTotals != null || i_productStockTotals.size() > 0) {
                    //如果库存总表存在，则增加库存数量
                    PdProductStockTotal productStockTotal = i_productStockTotals.get(0);
                    productStockTotal.setStockNum(productNum_i);
                    pdProductStockTotalMapper.addStock(productStockTotal);
                } else {
                    //
                }
                //增加入库库存明细
                PdProductStock i_productStockq = new PdProductStock();
                i_productStockq.setId(productStockId);
                //i_productStockq.setNestatStatus(PdConstant.STOCK_NESTAT_STATUS_1);//查询未使用的库存明细
                List<PdProductStock> i_productStocks = pdProductStockMapper.findForUpdate(i_productStockq);
                if (i_productStocks != null || i_productStocks.size() > 0) {
                    //存在，则增加库存数量
                    PdProductStock productStock = i_productStocks.get(0);
                    productStock.setStockNum(productNum_i);
                    pdProductStockMapper.addStock(productStock);
                } else {
                    //
                }
            }
        }
        return "";
    }

    /***
     * 	退货更新库存信息
     *
     * @param  pdRejected    退货明细列表，不允许为空
     * @return Map      更新库存结果  入库成功，返回字符串“true”，否则返回错误信息
     */
    @Transactional
    @Override
    public String updateRejectedStock(PdRejected pdRejected) {
        if (pdRejected == null || CollectionUtils.isEmpty(pdRejected.getPdRejectedDetailList())) {
            return "参数有误";
        }
        String drpartId = pdRejected.getDepartId();
        List<PdRejectedDetail> pdRejectedDetailList = pdRejected.getPdRejectedDetailList();
        for (PdRejectedDetail detail : pdRejectedDetailList) {
            String productId = detail.getProductId();     //产品ID
            Double productNum = detail.getRejectedCount();  //退货数量
            PdProduct pdProduct = pdProductMapper.selectById(productId);
            PdProductStockTotal stockTotalq = new PdProductStockTotal();
            stockTotalq.setDepartId(drpartId);
            stockTotalq.setProductId(productId);
            List<PdProductStockTotal> productStockTotals = pdProductStockTotalMapper.findForUpdate(stockTotalq);
            // 扣减总库存
            if (productStockTotals != null && productStockTotals.size() == 1) {
                PdProductStockTotal productStockTotal = productStockTotals.get(0);
                Double num = productStockTotal.getStockNum() - productNum;
                if (num < 0) {
                    throw new RuntimeException("扣减总库存失败，[" + pdProduct.getName() + "]总库存数量不足");
                }
                productStockTotal.setStockNum(num);
                pdProductStockTotalMapper.updateStockNum(productStockTotal);
            } else {
                throw new RuntimeException("库存没有产品[" + pdProduct.getName() + "]！");
            }

            PdProductStock o_productStockq = new PdProductStock();
            o_productStockq.setId(detail.getProductStockId());
            o_productStockq.setNestatStatus(PdConstant.STOCK_NESTAT_STATUS_1);//查询未使用的库存明细
            List<PdProductStock> productStocks = pdProductStockMapper.selectList(o_productStockq);
            // 扣减库存
            if (CollectionUtils.isNotEmpty(productStocks) && productStocks.size() >= 1) {
                PdProductStock productStock = productStocks.get(0);
                Double num = productStock.getStockNum() - productNum;
                if (num < 0) {
                    throw new RuntimeException("扣减库存失败，[" + pdProduct.getName() + "]库存数量不足");
                }
                productStock.setStockNum(num);
                pdProductStockMapper.updateStockNum(productStock);
            } else {
                throw new RuntimeException("库存没有产品[" + pdProduct.getName() + "]！");
            }
        }

        return PdConstant.TRUE;
    }


    /**
     * 院外退货更新库存信息
     *
     * @param stockTotal
     * @return
     */
    @Transactional
    public Map<String, String> updateStockNumByProdIdAndDeptId(PdProductStockTotal stockTotal) {
        List<PdProductStockTotal> totalList = pdProductStockTotalMapper.findForUpdate(stockTotal);
        PdProductStockTotal total = totalList.get(0);
        Map<String, String> result = new HashMap<String, String>();
        Double before = total.getStockNum();
        Double after = stockTotal.getStockNum();
        if (before >= after) {
            stockTotal.setStockNum(before - after);
            pdProductStockTotalMapper.updateForDosagert(stockTotal);
            result.put("code", "200");
            return result;
        } else {
            result.put("code", "500");
            return result;
        }
    }

    /**
     * 库存移库位更新库存信息
     *
     * @param productStock
     * @return
     */
    @Transactional
    @Override
    public String updateStockHuowei(PdProductStock productStock) {
        String huoWeiCode = productStock.getHuoweiCode();
        Double stockNum = productStock.getStockNum();
        Double ykStockNum = productStock.getYkStockNum();
        //先判断修改后的货位下是否存在当前批次的产品
        PdProductStock i_productStockq = new PdProductStock();
        i_productStockq.setDepartId(productStock.getDepartId());
        i_productStockq.setProductId(productStock.getProductId());
        i_productStockq.setProductBarCode(productStock.getProductBarCode());//2019年7月24日16:53:43 放开
        i_productStockq.setBatchNo(productStock.getBatchNo());
        i_productStockq.setHuoweiCode(huoWeiCode);
        i_productStockq.setNestatStatus(PdConstant.STOCK_NESTAT_STATUS_1);
        List<PdProductStock> i_productStocks = pdProductStockMapper.selectList(i_productStockq);
        //如果库存明细表不存在，直接修改货位就行
        if (CollectionUtils.isEmpty(i_productStocks) || i_productStocks.size() == 0) {
            PdProductStock pdproductStock = new PdProductStock();
            pdproductStock.setDepartId(productStock.getDepartId());
            pdproductStock.setProductId(productStock.getProductId());
            pdproductStock.setProductBarCode(productStock.getProductBarCode());
            pdproductStock.setStockNum(ykStockNum);
            pdproductStock.setPurchasePrice(productStock.getPurchasePrice());
            pdproductStock.setProductName(productStock.getProductName());
            pdproductStock.setBatchNo(productStock.getBatchNo());
            pdproductStock.setHuoweiCode(huoWeiCode);
            pdproductStock.setExpDate(productStock.getExpDate());
            pdproductStock.setSupplierId(productStock.getSupplierId());
            pdproductStock.setDistributorId(pdproductStock.getDistributorId());
            pdproductStock.setProduceDate(productStock.getProduceDate());
            pdproductStock.setSpecNum(productStock.getSpecNum());
            pdproductStock.setNestatStatus(productStock.getNestatStatus());
            pdproductStock.setSpecQuantity(productStock.getSpecQuantity());
            pdproductStock.setSpecUnitId(productStock.getSpecUnitId());
//            pdproductStock.setRefBarCode(SnowUtils.bigKey());
            productStockService.save(pdproductStock);
            //更新老货位上的库存数量
            PdProductStock pdProductStock_1 = new PdProductStock();
            pdProductStock_1.setStockNum(stockNum - ykStockNum);
            pdProductStock_1.setId(productStock.getId());
            pdProductStock_1.setSpecNum(productStock.getSpecQuantity() == null ? 0D : productStock.getSpecQuantity()    * pdProductStock_1.getStockNum());// 库存规格数量= 产品规格数量* 入库数量
            pdProductStockMapper.updateStockNum(pdProductStock_1);
        } else { //如果修改后的货位已经有耗材存在，则在新货位上增加数量,之前货位的数量减去移库的数量
            PdProductStock stock = i_productStocks.get(0);
            if (!stock.getId().equals(productStock.getId())) {
                //更新新货位上的库存数量
                stock.setStockNum(ykStockNum + stock.getStockNum());
                pdProductStockMapper.updateById(stock);
                //更新老货位上的库存数量
                PdProductStock pdProductStock_2 = new PdProductStock();
                pdProductStock_2.setStockNum(stockNum - ykStockNum);
                pdProductStock_2.setId(productStock.getId());
                pdProductStock_2.setSpecNum(productStock.getSpecQuantity() == null ? 0D : productStock.getSpecQuantity()    * pdProductStock_2.getStockNum());// 库存规格数量= 产品规格数量* 入库数量
                pdProductStockMapper.updateStockNum(pdProductStock_2);
            }
        }
        return PdConstant.TRUE;
    }

    /**
     * 获取待盘点产品总数量
     *
     * @param stockTotal
     * @return
     */
    @Override
    public Double queryCheckTotalNum(PdProductStockTotal stockTotal) {
        return pdProductStockTotalMapper.queryCheckTotalNum(stockTotal);
    }



    /**
     * 库存规格数量清零操作
     *
     * @param productStock
     * @return
     */
    @Transactional
    @Override
    public String updateStockSpecNum(PdProductStock productStock) {
        String ids = productStock.getIds();//库存明细ID
        String reason=productStock.getReason();//清零原因
        List arr = Arrays.asList(ids.split(","));
        for (int i = 0; i < arr.size(); i++) {
            PdSpecLog pdSpecLog=new PdSpecLog();
            String id=(String)arr.get(i);
            PdProductStock stock=pdProductStockMapper.selectById(id);
            Double stockNum=stock.getStockNum();

            //记录到清零日志表
            LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
            pdSpecLog.setPersonId(sysUser.getId());//操作人ID
            pdSpecLog.setPersonName(sysUser.getRealname());//操作人姓名
            pdSpecLog.setProductId(stock.getProductId());//产品id
            pdSpecLog.setStockId(id);//库存明细ID
            pdSpecLog.setSpecNum(stock.getSpecNum());//清零规格库存数量
            pdSpecLog.setReason(reason);//清零原因
            pdSpecLog.setSpecQuantity(stock.getSpecQuantity());//规格数量
            pdSpecLog.setSpecUnitId(stock.getSpecUnitId());//规格单位
            pdSpecLogService.save(pdSpecLog);

            //明細表清零
            stock.setSpecNum(0.00);
            stock.setStockNum(0.00);
            stock.setReason(reason);
            stock.setNestatStatus(PdConstant.STOCK_NESTAT_STATUS_2);
            pdProductStockMapper.updateById(stock);

            //更新总库存数量
            PdProductStockTotal stockTotal=new PdProductStockTotal();
            stockTotal.setDepartParentId(stock.getDepartParentId());
            stockTotal.setDepartId(stock.getDepartId());
            stockTotal.setProductId(stock.getProductId());
            List<PdProductStockTotal> list= pdProductStockTotalMapper.findForUpdate(stockTotal);
            if(CollectionUtils.isNotEmpty(list)){
                PdProductStockTotal total= list.get(0);
                Double stock_num=total.getStockNum();
                if(stock_num>0){
                    total.setStockNum(total.getStockNum() - stockNum);
                }
                pdProductStockTotalMapper.updateById(total);
            }
        }
        return PdConstant.TRUE;
    }


    /***
     * 	试剂耗材产品更新库存用量信息(重新扣减及批量扣减调用)
     */
    @Transactional
    @Override
    public Map<String,Object> lisUpdateUseStock(ExInspectionItems item, String departId,List<PdUsePackageDetail> detailList) {
        Map<String,Object> map=new HashMap<>();
        String instrCode=item.getInstrCode();//检验仪器代号
        List<String> departIds=Arrays.asList(departId.split(","));
        String bool=PdConstant.FALSE;
        map.put("code","400");
        for(PdUsePackageDetail detail:detailList) {
            String productId = detail.getProductId();//产品ID
            String productFlag = detail.getProductFlag();
            Double count = detail.getCount();//配置的使用量
            String  useType=detail.getUseType();//试剂使用类型
            String patientType=item.getPatientType();//病人类型
            if(!PdConstant.USE_TYPE_0.equals(useType) && StringUtils.isNotEmpty(patientType)){
                if(PdConstant.USE_TYPE_1.equals(useType) && !PdConstant.PATIENT_TYPE_1.equals(patientType)) {//住院病人
                    continue;
                }else if(PdConstant.USE_TYPE_2.equals(useType) && PdConstant.PATIENT_TYPE_1.equals(patientType)){
                    continue;
                }
            }
            if(PdConstant.PRODUCT_FLAG_1.equals(productFlag)){ //试剂
                //4：否则是试剂
                //先获取该仪器下已开瓶的试剂，如果不存在，则查询扣减科室下库存明细
                List<PdProductStock>  productStocks_i=null;
                if(StringUtils.isNotEmpty(instrCode)){
                    PdBottleInf bottleInf = new PdBottleInf();
                    bottleInf.setInstrCode(instrCode);
                    bottleInf.setDepartIdList(departIds);
                    bottleInf.setProductId(productId);
                    bottleInf.setNestatStatus(PdConstant.STOCK_NESTAT_STATUS_0);
                    productStocks_i=  pdBottleInfMapper.queryProductStock(bottleInf);
                }
                 if(CollectionUtils.isEmpty(productStocks_i)){
                    //4.1 查询扣減科室下库存明细(查询使用中的)，根据有效期排序
                    PdProductStock pproductStockq = new PdProductStock();
                    pproductStockq.setDepartIdList(departIds);
                    pproductStockq.setProductId(productId);
                    pproductStockq.setNestatStatus(PdConstant.STOCK_NESTAT_STATUS_0);
                    productStocks_i = pdProductStockMapper.selectOrExpDate(pproductStockq);
                }
                if (CollectionUtils.isEmpty(productStocks_i)) {
                    String remarks= "根据产品[" + detail.getProductName() + "]获取不到已开瓶的库存明细信息";
                    PdProductStock stock = new PdProductStock();
                    stock.setProductId(productId);
                    this.saveExInspectionInf(item,detail,stock,"1",remarks);
                    map.put("code","500");
                }else{
                    for(int i=0;i<productStocks_i.size();i++){
                        int size= productStocks_i.size();
                        PdProductStock  psii= productStocks_i.get(i);
                        Double specNum = psii.getSpecNum();
                        if(specNum==0.00 && i+1<size){//循环的时候先判断有没有规格库存数量不为0的数据
                            continue;
                        }else{
                            if (specNum >= count) {   //如果大于或等于要扣减的规格库存用量
                                Double newSpecNum = specNum - count;
                                if (newSpecNum >= 0.00) { //如果扣完后规格数量大于0，则继续扣库存明细表规格数量
                                    //更新明细库存
                                    psii.setSpecNum(newSpecNum);
                                    pdProductStockMapper.updateById(psii);
                                }
                                // 更新开瓶记录表数量
                                PdBottleInf pdBottleInf = new PdBottleInf();
                                pdBottleInf.setRefBarCode(psii.getRefBarCode());
                                pdBottleInf.setSpecNum(count);
                                pdBottleInf.setFilterType("0");//传值就过滤已闭瓶的数据
                                pdBottleInfMapper.updateSpecNum(pdBottleInf);
                                this.saveExInspectionInf(item,detail,psii,"0",null);
                                bool=PdConstant.TRUE;
                                break;
                            } else {
                                PdBottleInf pdBottleInf = new PdBottleInf();
                                pdBottleInf.setRefBarCode(psii.getRefBarCode());
                                pdBottleInf.setFilterType("0");//传值就过滤已闭瓶的数据
                                if (specNum > 0.00) {
                                    count = count - specNum;
                                    psii.setSpecNum(0.00);
                                    pdProductStockMapper.updateById(psii);
                                    // 更新开瓶记录表数量
                                    pdBottleInf.setSpecNum(specNum);
                                    pdBottleInfMapper.updateSpecNum(pdBottleInf);
                                    this.saveExInspectionInf(item,detail,psii,"0",null);
                                    bool=PdConstant.TRUE;
                                } else {
                                    // 更新开瓶记录表数量
                                    pdBottleInf.setSpecNum(count);
                                    pdBottleInfMapper.updateSpecNum(pdBottleInf);
                                    this.saveExInspectionInf(item,detail,psii,"0",null);
                                    bool=PdConstant.TRUE;
                                    break;
                                }
                            }
                        }
                    }
                }
            }else{
                throw new RuntimeException("不是试剂类产品，无法扣减用量");
            }
        }
        String code=MapUtils.getString(map,"code");
        if("500".equals(code) && PdConstant.TRUE.equals(bool)){//说明是4：部分扣减
            map.put("code","500");
        }else if("500".equals(code) && PdConstant.FALSE.equals(bool)){//说明是未扣减
            map.put("code","300");
            map.put("msg","获取不到已开瓶的产品信息");
        }else if("400".equals(code) && PdConstant.TRUE.equals(bool)){
            map.put("code","200");
        }
        return map;
    }


    /***
     * 	试剂耗材产品更新库存用量信息(Lis系统推送的数据)
     */
    @Transactional
    @Override
    public Map<String,Object> lisUpdateUseStockLis(ExInspectionItems item, String departId,List<PdUsePackageDetail> detailList) {
        Map<String,Object> map=new HashMap<>();
        String instrCode=item.getInstrCode();//检验仪器代号
        List<String> departIds=Arrays.asList(departId.split(","));
        String bool=PdConstant.FALSE;
        map.put("code","400");
        for(PdUsePackageDetail detail:detailList) {
            String productId = detail.getProductId();//产品ID
            String productFlag = detail.getProductFlag();
            Double count = detail.getCount();//配置的使用量
            String  useType=detail.getUseType();//试剂使用类型
            String patientType=item.getPatientType();//病人类型
            if(!PdConstant.USE_TYPE_0.equals(useType) && StringUtils.isNotEmpty(patientType)){
                if(PdConstant.USE_TYPE_1.equals(useType) && !PdConstant.PATIENT_TYPE_1.equals(patientType)) {//住院病人
                    continue;
                }else if(PdConstant.USE_TYPE_2.equals(useType) && PdConstant.PATIENT_TYPE_1.equals(patientType)){
                    continue;
                }
            }
            if(PdConstant.PRODUCT_FLAG_1.equals(productFlag)){ //试剂
                //4：否则是试剂
                //先获取该仪器下已开瓶的试剂，如果不存在，则查询扣减科室下库存明细
                List<PdProductStock>  productStocks_i=null;
                if(StringUtils.isNotEmpty(instrCode)){
                    PdBottleInf bottleInf = new PdBottleInf();
                    bottleInf.setInstrCode(instrCode);
                    bottleInf.setDepartIdList(departIds);
                    bottleInf.setProductId(productId);
                    bottleInf.setNestatStatus(PdConstant.STOCK_NESTAT_STATUS_0);
                    productStocks_i=  pdBottleInfMapper.queryProductStock(bottleInf);
                }
                if(CollectionUtils.isEmpty(productStocks_i)){
                    //4.1 查询扣減科室下库存明细(查询使用中的)，根据有效期排序
                    PdProductStock pproductStockq = new PdProductStock();
                    pproductStockq.setDepartIdList(departIds);
                    pproductStockq.setProductId(productId);
                    pproductStockq.setNestatStatus(PdConstant.STOCK_NESTAT_STATUS_0);
                    productStocks_i = pdProductStockMapper.selectOrExpDate(pproductStockq);
                }
                if (CollectionUtils.isEmpty(productStocks_i)) {
                    String remarks= "根据产品[" + detail.getProductName() + "]获取不到已开瓶的库存明细信息";
                    PdProductStock stock = new PdProductStock();
                    stock.setProductId(productId);
                    this.saveExInspectionInf(item,detail,stock,"1",remarks);
                    map.put("code","500");
                }else{
                    for(int i=0;i<productStocks_i.size();i++){
                        int size= productStocks_i.size();
                        PdProductStock  psii= productStocks_i.get(i);
                        Double specNum = psii.getSpecNum();
                        if(specNum==0.00 && i+1<size){//循环的时候先判断有没有规格库存数量不为0的数据
                            continue;
                        }else{
                            if (specNum >= count) {   //如果大于或等于要扣减的规格库存用量
                                Double newSpecNum = specNum - count;
                                if (newSpecNum >= 0.00) { //如果扣完后规格数量大于0，则继续扣库存明细表规格数量
                                    //更新明细库存
                                    psii.setSpecNum(newSpecNum);
                                    pdProductStockMapper.updateById(psii);
                                }
                                // 更新开瓶记录表数量
                                PdBottleInf pdBottleInf = new PdBottleInf();
                                pdBottleInf.setRefBarCode(psii.getRefBarCode());
                                pdBottleInf.setSpecNum(count);
                                pdBottleInf.setFilterType("0");//传值就过滤已闭瓶的数据
                                pdBottleInfMapper.updateSpecNum(pdBottleInf);
                                this.saveExInspectionInf(item,detail,psii,"0",null);
                                bool=PdConstant.TRUE;
                                break;
                            } else {
                                PdBottleInf pdBottleInf = new PdBottleInf();
                                pdBottleInf.setRefBarCode(psii.getRefBarCode());
                                pdBottleInf.setFilterType("0");//传值就过滤已闭瓶的数据
                                if (specNum > 0.00) {
                                    count = count - specNum;
                                    psii.setSpecNum(0.00);
                                    pdProductStockMapper.updateById(psii);
                                    // 更新开瓶记录表数量
                                    pdBottleInf.setSpecNum(specNum);
                                    pdBottleInfMapper.updateSpecNum(pdBottleInf);
                                    this.saveExInspectionInf(item,detail,psii,"0",null);
                                    bool=PdConstant.TRUE;
                                } else {
                                    // 更新开瓶记录表数量
                                    pdBottleInf.setSpecNum(count);
                                    pdBottleInfMapper.updateSpecNum(pdBottleInf);
                                    this.saveExInspectionInf(item,detail,psii,"0",null);
                                    bool=PdConstant.TRUE;
                                    break;
                                }
                            }
                        }
                    }
                }
            }else{
                throw new RuntimeException("不是试剂类产品，无法扣减用量");
            }
        }
        String code=MapUtils.getString(map,"code");
        if("500".equals(code) && PdConstant.TRUE.equals(bool)){//说明是4：部分扣减
            map.put("code","500");
        }else if("500".equals(code) && PdConstant.FALSE.equals(bool)){//说明是未扣减
            map.put("code","300");
            map.put("msg","获取不到已开瓶的产品信息");
        }else if("400".equals(code) && PdConstant.TRUE.equals(bool)){
            map.put("code","200");
        }
        return map;
    }

    /**
     * 记录每个检验项目的扣减用量明细
     * @param item
     * @param status
     * @param remarks
     */
    public void saveExInspectionInf(ExInspectionItems item,PdUsePackageDetail detail,PdProductStock stock,String status,String remarks){
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("product_id", stock.getProductId());
        queryWrapper.eq("jy_id", item.getJyId());
        queryWrapper.eq("code", item.getTestItemCode());
        ExInspectionInf inspectionInf  = exInspectionInfService.getOne(queryWrapper);
        if(inspectionInf==null || inspectionInf.getId()==null){
            ExInspectionInf exInspectionInf=new ExInspectionInf();
            exInspectionInf.setCode(item.getTestItemCode());
            exInspectionInf.setStockId(stock.getId());
            exInspectionInf.setRefBarCode(stock.getRefBarCode());
            exInspectionInf.setJyId(item.getJyId());
            exInspectionInf.setProductId(stock.getProductId());
            exInspectionInf.setDepartParentId(detail.getDepartParentId());
            exInspectionInf.setDepartId(detail.getDepartId());
            exInspectionInf.setStatus(status);//0:已扣减   1:未扣减
            exInspectionInf.setRemarks(status =="0" ? " " : remarks);
            exInspectionInfService.save(exInspectionInf);
        }else{
            inspectionInf.setStatus(status);//0:已扣减   1:未扣减
            inspectionInf.setRemarks(status =="0" ? " " : remarks);
            inspectionInf.setRefBarCode(stock.getRefBarCode());
            inspectionInf.setStockId(stock.getId());
            exInspectionInfService.updateById(inspectionInf);
        }
}


  /*
  * 手动扣减 产品+试剂的*/
    @Override
    @Transactional
    public String jyUpdateProductStockNum(String departId,List<PdProductStock> productStockList) {
        if(StringUtils.isEmpty(departId)){
            throw new RuntimeException("参数错误，科室ID不能为空！");
        }
        if(CollectionUtils.isNotEmpty(productStockList)){
            for(PdProductStock productStock:productStockList){
                String productFlag=productStock.getProductFlag();
                String productId= productStock.getProductId();//产品ID
                Double count= productStock.getProductNum();//扣减数量
                if(StringUtils.isEmpty(productStock.getId()) ||  StringUtils.isEmpty(productFlag)){
                    throw new RuntimeException("参数错误！");
                }
                PdProductStockTotal stockTotal=new PdProductStockTotal();
                stockTotal.setDepartId(departId);
                stockTotal.setProductId(productId);
                List<PdProductStockTotal> stockTotals= pdProductStockTotalMapper.findForUpdate(stockTotal);
                if(CollectionUtils.isEmpty(stockTotals)){
                    throw new RuntimeException("扣减总库存失败,根据产品：["+productStock.getProductName()+"]获取不到库存信息");
                }
                PdProductStock o_productStockq = new PdProductStock();
                o_productStockq.setId(productStock.getProductStockId());
                PdProductStock stock = pdProductStockMapper.getOne(o_productStockq);
                if(stock==null){
                    throw new RuntimeException("扣减库存失败,根据产品：["+productStock.getProductName()+"]获取不到库存明细信息");
                }
                if(PdConstant.PRODUCT_FLAG_0.equals(productFlag)) { //产品扣减
                    //扣减总库存
                    PdProductStockTotal productStockTotal = stockTotals.get(0);
                    Double stockNum_i = productStockTotal.getStockNum();
                    Double newStockNum = stockNum_i - count;
                    productStockTotal.setStockNum(newStockNum);
                    pdProductStockTotalMapper.updateStockNum(productStockTotal);
                    //扣减明细库存
                    Double num = stock.getStockNum() - count;
                    if (num < 0) {
                        throw new RuntimeException("扣减库存失败，[" + stock.getProductName() + "]库存数量不足");
                    }
                    stock.setStockNum(num);
                    stock.setSpecNum(productStock.getSpecQuantity() == null ? 0D : productStock.getSpecQuantity()    * stock.getStockNum());// 库存规格数量= 产品规格数量* 入库数量
                    pdProductStockMapper.updateStockNum(stock);
                }else{
                    //4：否则是试剂
                    PdProductStock pproductStockq = new PdProductStock();
                    pproductStockq.setId(productStock.getProductStockId());
                    PdProductStock  productStocks_i = pdProductStockMapper.getOne(pproductStockq);
                    if(productStocks_i ==null ){
                        throw new RuntimeException("扣减库存失败,根据产品名称：["+productStock.getProductName()+"]获取不到库存明细信息");
                    }else {
                        if(PdConstant.STOCK_NESTAT_STATUS_1.equals(productStocks_i.getNestatStatus())){
                          throw new RuntimeException("扣减库存失败, ["+productStock.getProductName()+"]未开瓶，请开瓶后在扣减");
                        }
                        try{
                            PdProductStockTotal productStockTotal = stockTotals.get(0);
                           Double specNum= productStocks_i.getSpecNum();//使用中的规格库存数量
                                  // if(specNum>=count){ //如果大于或等于要扣减的规格库存用量
                                     Double newSpecNum= specNum - count;
                                     if(newSpecNum>=0.00){ //如果扣完后规格数量大于0，则继续扣库存明细表规格数量
                                         //更新明细库存
                                         productStocks_i.setSpecNum(newSpecNum);
                                        }else{
                                         productStocks_i.setSpecNum(0.00);
                                        }
                                      pdProductStockMapper.updateById(productStocks_i);
                                     // 更新开瓶记录表数量
                                       PdBottleInf pdBottleInf=new PdBottleInf();
                                       pdBottleInf.setRefBarCode(productStocks_i.getRefBarCode());
                                       pdBottleInf.setSpecNum(count);
                                       pdBottleInf.setFilterType("0");//传值就过滤已闭瓶的数据
                                       pdBottleInfMapper.updateSpecNum(pdBottleInf);
                                   //}else{
                                      // throw new RuntimeException("扣减库存失败, ["+productStock.getProductName()+"]库存规格数量不足");
                                   //}
                        }catch (Exception e){
                            throw new RuntimeException("扣减库存失败,"+e.getMessage());
                        }
                    }
                }
            }
        }
        return PdConstant.TRUE;
    }


    /**
     *检验项目手动扣减库存方法(套包)
     * @param departId
     * @param usePackageDetailList
     * @return
     */
    @Transactional
    @Override
    public List<PdProductStock> jyUpdatePackageStockNum(String departId,List<PdUsePackageDetail> usePackageDetailList) {
        if(StringUtils.isEmpty(departId)){
            throw new RuntimeException("参数错误，科室ID不能为空！");
        }
        List<PdProductStock> productStockList=new ArrayList<PdProductStock>();
           if(CollectionUtils.isNotEmpty(usePackageDetailList)) {
               for (PdUsePackageDetail packageDetail : usePackageDetailList) {
                   String productId = packageDetail.getProductId();
                   String packageId = packageDetail.getPackageId();
                   Double count = packageDetail.getCount();
                   String productFlag = packageDetail.getProductFlag();//0:产品  1：试剂

                   //1:先查询扣减科室下总库存是否存在库存数据
                   PdProductStockTotal stockTotal = new PdProductStockTotal();
                   stockTotal.setDepartId(departId);
                   stockTotal.setProductId(productId);
                   List<PdProductStockTotal> stockTotals = pdProductStockTotalMapper.findForUpdate(stockTotal);
                   if (CollectionUtils.isEmpty(stockTotals)) {
                       throw new RuntimeException("扣减总库存失败,根据产品名称：[" + packageDetail.getProductName() + "]获取不到库存信息");
                   }
                   //2:如果存在数据，则根据产品ID查询该库存下使用中的库存明细
                   PdProductStock stock = new PdProductStock();
                   stock.setDepartId(departId);
                   stock.setProductId(productId);
                   List<PdProductStock> productStocks = pdProductStockMapper.selectOrExpDate(stock);
                   if (CollectionUtils.isEmpty(productStocks)) {
                       throw new RuntimeException("扣减库存失败,根据产品名称：[" + packageDetail.getProductName() + "]获取不到库存明细信息");
                   }
                   //3：如果是普通产品
                   if (PdConstant.PRODUCT_FLAG_0.equals(productFlag)) {
                       //判断用量是否大于产品总数量，如果大于则扣减失败
                       //3.1 扣减总库存数量
                       PdProductStockTotal productStockTotal = stockTotals.get(0);
                       if (productStockTotal.getStockNum() < count) {
                           throw new RuntimeException("扣减库存失败,根据产品：[" + packageDetail.getProductName() + "]获取不到库存明细信息");
                       }
                       Double stockNum_i = productStockTotal.getStockNum();
                       Double newStockNum = stockNum_i - count;
                       productStockTotal.setStockNum(newStockNum);
                       pdProductStockTotalMapper.updateStockNum(productStockTotal);
                       //3.2 扣减明细库存数量
                       try {
                           for (PdProductStock ps : productStocks) {
                               if (ps.getStockNum() >= count) {
                                   Double num = ps.getStockNum() - count;
                                   ps.setStockNum(num);
                                   if (num == 0.00) {
                                       ps.setNestatStatus(PdConstant.STOCK_NESTAT_STATUS_2);//已用完
                                   }
                                   pdProductStockMapper.updateStockNum(ps);
                                   ps.setPackageId(packageId);
                                   ps.setProductNum(count);
                                   productStockList.add(ps);
                                   break;
                               } else {
                                   ps.setPackageId(packageId);
                                   ps.setProductNum(ps.getStockNum());
                                   count = count - ps.getStockNum();
                                   ps.setStockNum(0.00);
                                   ps.setNestatStatus(PdConstant.STOCK_NESTAT_STATUS_2);//已用完
                                   pdProductStockMapper.updateStockNum(ps);
                                   productStockList.add(ps);
                               }
                           }
                       } catch (Exception e) {
                           throw new RuntimeException("扣减库存失败，"+e.getMessage());
                       }
                   } else {
                       //4：否则是试剂
                       //4.1 查询扣減科室下库存明细(查询使用中的)
                       PdProductStock o_productStockq = new PdProductStock();
                       o_productStockq.setDepartId(departId);
                       o_productStockq.setProductId(productId);
                       o_productStockq.setNestatStatus(PdConstant.STOCK_NESTAT_STATUS_0);
                       List<PdProductStock> productStocks_i = pdProductStockMapper.selectOrExpDate(o_productStockq);
                       if (CollectionUtils.isEmpty(productStocks_i)) {
                           throw new RuntimeException("扣减库存失败,根据产品名称：["+packageDetail.getProductName()+"]获取不到已开瓶的库存明细信息");
                       }else{
                           PdProductStockTotal productStockTotal = stockTotals.get(0);
                           for(PdProductStock psii :productStocks_i ){
                                   Double specNum =  psii.getSpecNum();
                                   Double newSpecNum= specNum - count;
                                   if(specNum>=count){ //如果大于或等于要扣减的规格库存用量
                                       if( newSpecNum>=0.00){ //如果扣完后规格数量大于0，则继续扣库存明细表规格数量
                                               //更新明细库存
                                               psii.setSpecNum(newSpecNum);
                                               pdProductStockMapper.updateById(psii);
                                            }
                                           // 更新开瓶记录表数量
                                           PdBottleInf pdBottleInf=new PdBottleInf();
                                           pdBottleInf.setRefBarCode(psii.getRefBarCode());
                                           pdBottleInf.setSpecNum(count);
                                           pdBottleInf.setFilterType("0");//传值就过滤已闭瓶的数据
                                           pdBottleInfMapper.updateSpecNum(pdBottleInf);
                                       psii.setPackageId(packageId);
                                       psii.setProductNum(count);
                                       productStockList.add(psii);
                                       break;
                                       }else{
                                         count=count-specNum;
                                         psii.setSpecNum(0.00);
                                         pdProductStockMapper.updateById(psii);
                                       // 更新开瓶记录表数量
                                       PdBottleInf pdBottleInf=new PdBottleInf();
                                       pdBottleInf.setRefBarCode(psii.getRefBarCode());
                                       pdBottleInf.setSpecNum(specNum);
                                       pdBottleInf.setFilterType("0");//传值就过滤已闭瓶的数据
                                       pdBottleInfMapper.updateSpecNum(pdBottleInf);
                                       psii.setPackageId(packageId);
                                       psii.setProductNum(count);
                                       productStockList.add(psii);
                                       }
                                 }
                       }
                   }
               }
           }
        return productStockList;
    }







    /**
     * 查询库存总表（设置了库存下限且自动补货数量大于0的数据）
     */
    @Override
    public List<PdProductStockTotalPage> findListForAutoNum(PdProductStockTotal stockTotal) {
        return pdProductStockTotalMapper.findListForAutoNum(stockTotal);
    }

    public static void main (String [] args){
        Double a = 220.00;
        Double b = 220.00;
        Double bzCount = (Double) Math.ceil(a/b);
        System.out.println(bzCount);
        System.out.println(a%b);
        if(a%b==0.00){
            System.out.println("1");
        }else{
            System.out.println("2");
        }
    }



    //新增已使用的库存明细
    @Transactional
    public PdProductStock insertProdStock(PdProductStock productStock){
        //更新老的库存明细表的数据
        PdProductStock stock=new PdProductStock();
        stock.setId(productStock.getId());
        stock=pdProductStockMapper.getOne(stock);
        Double stockNum=stock.getStockNum();
        if(stockNum>1){
            stock.setStockNum(stockNum-1);
            stock.setSpecNum(stock.getSpecQuantity() == null ? 0D : stock.getSpecQuantity() * stock.getStockNum());// 库存规格数量= 产品规格数量* 库存数量
            pdProductStockMapper.updateById(stock);
            //新增一条明细记录
            productStock.setId(null);
            productStock.setStockNum(1.00);
            productStock.setSpecNum(productStock.getSpecQuantity() == null ? 0D : productStock.getSpecQuantity() * productStock.getStockNum());// 库存规格数量= 产品规格数量* 库存数量
            productStock.setNestatStatus(PdConstant.STOCK_NESTAT_STATUS_0);
            pdProductStockMapper.insert(productStock);
            //更新条码表信息
            if(PdConstant.CODE_PRINT_TYPE_1.equals(productStock.getBarCodeType())){
                // 唯一码入库，更新条码表库存id
                if(oConvertUtils.isNotEmpty(productStock.getRefBarCode())){
                    List<String> refBarCodeList = Arrays.asList(productStock.getRefBarCode().split(","));
                    for(String refBarCode : refBarCodeList){
                        PdProductStockUniqueCode code = new PdProductStockUniqueCode();
                        code.setId(refBarCode);
                        code.setProductStockId(productStock.getId());
                        code.setCodeState(PdConstant.CODE_PRINT_STATE_0);
                        pdProductStockUniqueCodeMapper.updateById(code);
                    }
                }
            }
        }else{
            stock.setNestatStatus(PdConstant.STOCK_NESTAT_STATUS_0);
            pdProductStockMapper.updateById(stock);
        }

        return productStock;
    }

    //开瓶新增已使用的库存明细
    @Transactional
    public PdProductStock insertProdStockSj(PdProductStock productStock){
        //更新老的库存明细表的数据
        PdProductStock stock=new PdProductStock();
        stock.setId(productStock.getId());
        stock=pdProductStockMapper.getOne(stock);
        Double stockNum=stock.getStockNum();
        stock.setStockNum(stockNum-1);
        stock.setSpecNum(stock.getSpecQuantity() == null ? 0D : stock.getSpecQuantity() * stock.getStockNum());// 库存规格数量= 产品规格数量* 库存数量
        pdProductStockMapper.updateById(stock);
        //新增一条明细记录
        productStock.setId(null);
        productStock.setStockNum(1.00);
        Double specQuantity=productStock.getSpecQuantity();
        productStock.setSpecNum(specQuantity);
        productStock.setNestatStatus(PdConstant.STOCK_NESTAT_STATUS_0);
        pdProductStockMapper.insert(productStock);
        return productStock;
    }

    //闭瓶更新库存信息
    @Transactional
    public PdProductStock closeProdStock(PdProductStock productStock){
        PdProductStock stock=new PdProductStock();
      PdProductStockTotal total=new PdProductStockTotal();
        total.setProductId(productStock.getProductId());
        total.setDepartId(productStock.getDepartId());
        List<PdProductStockTotal> totalList=pdProductStockTotalMapper.findForUpdate(total);
            if(CollectionUtils.isEmpty(totalList)){
                throw new RuntimeException("闭瓶失败,根据条码找不到库存信息");
            }
                //更新总库存明细信息
                PdProductStockTotal stockTotal=  totalList.get(0);
                Double stockNum_i=stockTotal.getStockNum();
                stockTotal.setStockNum(stockNum_i-1);
                pdProductStockTotalMapper.updateById(stockTotal);
                //更新库存明细表的数据
               stock.setId(productStock.getId());
               stock=pdProductStockMapper.getOne(stock);
               if(!productStock.getRefBarCode().equals(stock.getRefBarCode())){
                   throw new RuntimeException("闭瓶失败,条码不一致");
               }
               Double stockNum=stock.getStockNum();
              stock.setStockNum(stockNum-1);
              stock.setNestatStatus(PdConstant.STOCK_NESTAT_STATUS_2);
              pdProductStockMapper.updateById(stock);

        return stock;
    }


    @Override
    public List<PdProductStockTotalPage> chooseStockTotalList(PdProductStockTotalPage stockTotal) {
        return pdProductStockTotalMapper.chooseStockTotalList(stockTotal);
    }

    /***
     * 	退货更新库存信息
     *
     * @param  pdProductStockCheck    退货明细列表，不允许为空
     * @return Map      更新库存结果  入库成功，返回字符串“true”，否则返回错误信息
     */
    @Transactional
    @Override
    public String updateCheckStock(PdProductStockCheck pdProductStockCheck) {
        if (pdProductStockCheck == null || CollectionUtils.isEmpty(pdProductStockCheck.getPdProductStockCheckChildList())) {
            return "参数有误";
        }
        String drpartId = pdProductStockCheck.getTargetDepartId();
        List<PdProductStockCheckChild> pdProductStockCheckChilds = pdProductStockCheck.getPdProductStockCheckChildList();
        for (PdProductStockCheckChild detail : pdProductStockCheckChilds) {
            String productId = detail.getProductId();     //产品ID
            Double productNum = detail.getProfitLossCount();  //盘点盈亏数量
            PdProduct pdProduct = pdProductMapper.selectById(productId);
            PdProductStockTotal stockTotalq = new PdProductStockTotal();
            stockTotalq.setDepartId(drpartId);
            stockTotalq.setProductId(productId);
            List<PdProductStockTotal> productStockTotals = pdProductStockTotalMapper.findForUpdate(stockTotalq);
            // 扣减总库存
            if (productStockTotals != null && productStockTotals.size() == 1) {
                PdProductStockTotal productStockTotal = productStockTotals.get(0);
                Double num = productStockTotal.getStockNum() + productNum;
                if (num < 0) {
                    throw new RuntimeException("扣减总库存失败，[" + pdProduct.getName() + "]总库存数量不足");
                }
                productStockTotal.setStockNum(num);
                pdProductStockTotalMapper.updateStockNum(productStockTotal);
            } else {
                throw new RuntimeException("库存没有产品[" + pdProduct.getName() + "]！");
            }

            PdProductStock o_productStockq = new PdProductStock();
            o_productStockq.setId(detail.getStockId());
            o_productStockq.setNestatStatus(PdConstant.STOCK_NESTAT_STATUS_1);//查询未使用的库存明细
            List<PdProductStock> productStocks = pdProductStockMapper.selectList(o_productStockq);
            // 扣减库存
            if (CollectionUtils.isNotEmpty(productStocks) && productStocks.size() >= 1) {
                PdProductStock productStock = productStocks.get(0);
                Double num = productStock.getStockNum() + productNum;
                if (num < 0) {
                    throw new RuntimeException("扣减库存失败，[" + pdProduct.getName() + "]库存数量不足");
                }
                productStock.setStockNum(num);
                pdProductStockMapper.updateStockNum(productStock);
            } else {
                throw new RuntimeException("库存没有产品[" + pdProduct.getName() + "]！");
            }
        }

        return PdConstant.TRUE;
    }
}
