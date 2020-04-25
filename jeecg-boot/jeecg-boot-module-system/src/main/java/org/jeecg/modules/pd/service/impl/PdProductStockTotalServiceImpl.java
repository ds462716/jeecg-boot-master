package org.jeecg.modules.pd.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.jeecg.common.constant.PdConstant;
import org.jeecg.modules.pd.entity.*;
import org.jeecg.modules.pd.mapper.PdProductMapper;
import org.jeecg.modules.pd.mapper.PdProductStockMapper;
import org.jeecg.modules.pd.mapper.PdProductStockTotalMapper;
import org.jeecg.modules.pd.service.IPdProductStockService;
import org.jeecg.modules.pd.service.IPdProductStockTotalService;
import org.jeecg.modules.pd.service.IPdUsePackageDetailService;
import org.jeecg.modules.pd.vo.PdProductStockTotalPage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    private IPdUsePackageDetailService  usePackageDetailService;

    /**
     * 查询列表
     *
     * @param page
     * @param stockTotal
     * @return
     */
    @Override
    public Page<PdProductStockTotalPage> selectList(Page<PdProductStockTotalPage> page, PdProductStockTotal stockTotal) {
        return page.setRecords(pdProductStockTotalMapper.selectList(stockTotal));
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


    /***
     * 	耗材入库更新库存信息
     *
     * @param   pdStockRecord    入库记录
     * @return String   更新库存结果  入库成功，返回字符串“true”，否则返回错误信息
     */
    @Transactional
    public String updateInStock(PdStockRecord pdStockRecord) {

        if (pdStockRecord == null || CollectionUtils.isEmpty(pdStockRecord.getPdStockRecordDetailList())) {
            return "参数有误";
        }

        String inDeptId = pdStockRecord.getInDepartId();
//		String supplierId = pdStockRecord.getSupplierId();
        List<PdStockRecordDetail> stockRecordDetails = pdStockRecord.getPdStockRecordDetailList();

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
                productStockTotal.setSupplierId(stockRecordDetail.getSupplierId());   //供应商
                super.save(productStockTotal);
            } else { //如果库存总表存在，则增加库存数量
                PdProductStockTotal productStockTotal = i_productStockTotals.get(0);
                productStockTotal.setStockNum(productNum + productStockTotal.getStockNum());
                pdProductStockTotalMapper.updateStockNum(productStockTotal);
            }
            //增加入库库存明细
//			PdProductStock i_productStockq = new PdProductStock();
//			i_productStockq.setDepartId(inDeptId);
//			i_productStockq.setProductId(productId);
//			i_productStockq.setProductBarCode(productBarCode);//2019年7月24日16:53:43 放开
//			i_productStockq.setBatchNo(batchNo);
//			i_productStockq.setHuoweiCode(inHuoweiCode);
//			List<PdProductStock> i_productStocks = pdProductStockMapper.findForUpdate(i_productStockq);
//			//如果库存明细表不存在，则新增
//			if(CollectionUtils.isEmpty(i_productStocks) || i_productStocks.size() == 0){

            // modified by jiangxz 20200327 库存明细关联入库明细
            PdProductStock productStock = new PdProductStock();
            productStock.setDepartId(inDeptId);
            productStock.setProductId(productId);
            productStock.setProductBarCode(productBarCode);
            productStock.setStockNum(productNum);
            productStock.setProductName(productName);
            productStock.setBatchNo(batchNo);
            productStock.setHuoweiCode(inHuoweiCode);
            productStock.setExpDate(stockRecordDetail.getExpDate());
            productStock.setProduceDate(stockRecordDetail.getProduceDate()); // 生产日期
            productStock.setRecordDetailId(stockRecordDetail.getId()); //入库明细ID
            productStock.setSupplierId(stockRecordDetail.getSupplierId());
            productStock.setSpecQuantity(stockRecordDetail.getSpecQuantity());
            productStock.setSpecUnitId(stockRecordDetail.getSpecUnitId());
            productStock.setSpecNum(stockRecordDetail.getSpecQuantity() == null ? 0D : stockRecordDetail.getSpecQuantity() * stockRecordDetail.getProductNum());// 库存规格数量= 产品规格数量* 入库数量
            productStockService.save(productStock);
//			}else{//存在，则增加库存数量
//				PdProductStock productStock = i_productStocks.get(0);
//				productStock.setStockNum(productNum + productStock.getStockNum());
//				pdProductStockMapper.updateStockNum(productStock);
//			}
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
                productStock.setSpecNum(stockRecordDetail.getSpecQuantity() == null ? 0D : stockRecordDetail.getSpecQuantity() * stockRecordDetail.getProductNum());// 库存规格数量= 产品规格数量* 入库数量

                pdProductStockMapper.updateStockNum(productStock);
            } else {
                throw new RuntimeException("库存没有产品[" + stockRecordDetail.getProductName() + "]！");
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
        //1、扣减出库库存，扣减出库库存明细
        for (PdDosageDetail dosageDetail : dosageDetails) {
            String productId = dosageDetail.getProductId();    //产品ID
            String prodBarcode = dosageDetail.getProductBarCode();  //条码
            String batchNo = dosageDetail.getBatchNo();
            String huoweiCode = dosageDetail.getOutHuoweiCode();
            Double productNum = dosageDetail.getDosageCount();  //数量
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
            o_productStockq.setDepartId(departId);
            o_productStockq.setProductId(productId);
            o_productStockq.setProductBarCode(prodBarcode);
            o_productStockq.setBatchNo(batchNo);
            o_productStockq.setHuoweiCode(huoweiCode);
            o_productStockq.setNestatStatus(PdConstant.STOCK_NESTAT_STATUS_1);//查询未使用的库存明细
            List<PdProductStock> productStocks = pdProductStockMapper.selectList(o_productStockq);
            if (productStocks != null && productStocks.size() >= 0) {
                PdProductStock productStock = productStocks.get(0);
                Double stockNum = productStock.getStockNum();
                Double num = stockNum - productNum;
                productStock.setStockNum(num);
                pdProductStockMapper.updateStockNum(productStock);
            }else {
                throw new RuntimeException("库存明细没有产品[" + dosageDetail.getProductName() + "]！");
            }
        }
        return PdConstant.TRUE;
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
        //1、增加库存，增加库存明细
        for (PdDosageDetail drt : detailList) {
            String productId = drt.getProductId();      //产品ID
            String number = drt.getNumber();      //产品编码
            String productBarCode = drt.getProductBarCode();  //产品条码
            String batchNo = drt.getBatchNo();       //产品批号
            String huoweiCode = drt.getInHuoweiCode();//入库货位号
            Double productNum_i = drt.getDosageCount();  //退回数量
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
            i_productStockq.setDepartId(departId);
            i_productStockq.setProductId(productId);
            i_productStockq.setProductBarCode(productBarCode);
            i_productStockq.setBatchNo(batchNo);
            i_productStockq.setNumber(number);
            i_productStockq.setHuoweiCode(huoweiCode);
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
        List<PdProductStock> i_productStocks = pdProductStockMapper.selectList(i_productStockq);
        //如果库存明细表不存在，直接修改货位就行
        if (CollectionUtils.isEmpty(i_productStocks) || i_productStocks.size() == 0) {
            PdProductStock pdproductStock = new PdProductStock();
            pdproductStock.setDepartId(productStock.getDepartId());
            pdproductStock.setProductId(productStock.getProductId());
            pdproductStock.setProductBarCode(productStock.getProductBarCode());
            pdproductStock.setStockNum(ykStockNum);
            pdproductStock.setProductName(productStock.getProductName());
            pdproductStock.setBatchNo(productStock.getBatchNo());
            pdproductStock.setHuoweiCode(huoWeiCode);
            pdproductStock.setExpDate(productStock.getExpDate());
            pdproductStock.setSupplierId(productStock.getSupplierId());
            pdproductStock.setProduceDate(productStock.getProduceDate());
            pdproductStock.setSpecNum(productStock.getSpecNum());
            pdproductStock.setNestatStatus(productStock.getNestatStatus());
            pdproductStock.setSpecQuantity(pdproductStock.getSpecQuantity());
            pdproductStock.setSpecUnitId(pdproductStock.getSpecUnitId());
            productStockService.save(pdproductStock);
            //更新老货位上的库存数量
            PdProductStock pdproductStock_1 = new PdProductStock();
            pdproductStock_1.setStockNum(stockNum - ykStockNum);
            pdproductStock_1.setId(productStock.getId());
            productStockService.updateProductStock(pdproductStock_1);
        } else { //如果修改后的货位已经有耗材存在，则在新货位上增加数量,之前货位的数量减去移库的数量
            PdProductStock stock = i_productStocks.get(0);
            if (!stock.getId().equals(productStock.getId())) {
                //更新新货位上的库存数量
                stock.setStockNum(ykStockNum + stock.getStockNum());
                pdProductStockMapper.updateById(stock);
                //更新老货位上的库存数量
                productStock.setStockNum(stockNum - ykStockNum);
                PdProductStock pdproductStock_2 = new PdProductStock();
                pdproductStock_2.setStockNum(stockNum - ykStockNum);
                pdproductStock_2.setId(productStock.getId());
                pdProductStockMapper.updateProductStock(pdproductStock_2);
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
            String id=(String)arr.get(i);
            PdProductStock stock=pdProductStockMapper.selectById(id);
            Double stockNum=stock.getStockNum();
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
     * 	试剂耗材产品更新库存用量信息(Lis系统推送的数据)
     */
    @Transactional
    public String lisUpdateUseStock( Map<String,Object> param) {
        String patientName= MapUtils.getString(param,"patientName");//患者姓名
        String patientSex= MapUtils.getString(param,"patientSex");//患者性别
        String patientAge= MapUtils.getString(param,"patientAge");//患者年龄
        String cardID= MapUtils.getString(param,"cardID");//就诊卡号
        String barCode= MapUtils.getString(param,"barCode");//条形码
        String applyDoctor= MapUtils.getString(param,"applyDoctor");//申请医生
        String applyDepartment= MapUtils.getString(param,"applyDepartment");//申请科室
        String testDoctor= MapUtils.getString(param,"testDoctor");//检验医生
        String testDepartment= MapUtils.getString(param,"testDepartment");//检验科室
        String patientType= MapUtils.getString(param,"patientType");//患者类型
        String groupBy= MapUtils.getString(param,"groupBy");//工作组
        String receiveDate= MapUtils.getString(param,"receiveDate");//接收日期
        String testDate= MapUtils.getString(param,"testDate");//检验日期
        String specimenType= MapUtils.getString(param,"specimenType");//样本日期
        List<Map<String,Object>> dataList=(List<Map<String,Object>>)param.get("data");
        for(Map<String,Object> map:dataList){
            String testCombinationName= MapUtils.getString(param,"testCombinationName");//组合名称
            String testItemName= MapUtils.getString(param,"testItemName");//检查项目名称
            String testItemCode= MapUtils.getString(param,"testItemCode");//检查项目代码
            String testItemCost= MapUtils.getString(param,"testItemCost");//检查项目费用

            PdUsePackageDetail usePackageDetail=new PdUsePackageDetail();
            usePackageDetail.setPackageId(testItemCode);
            List<PdUsePackageDetail> detailList= usePackageDetailService.queryPdUsePackageList(usePackageDetail);
                   if(CollectionUtils.isNotEmpty(detailList)){
                       for(PdUsePackageDetail detail:detailList){
                        String productId= detail.getProductId();//产品ID
                        Double syNum= 0.00;//detail.getCount();//配置的使用量
                        //查询检验科室库存明细(先查询使用中的)
                           PdProductStock o_productStockq = new PdProductStock();
                           o_productStockq.setDepartId("");//暂定检验科
                           o_productStockq.setProductId(productId);
                           o_productStockq.setNestatStatus("0");
                           //方法重新写
                           List<PdProductStock> productStocks = pdProductStockMapper.selectList(o_productStockq);
                            if(CollectionUtils.isEmpty(productStocks)){
                                PdProductStock productStockq = new PdProductStock();
                                productStockq.setDepartId("");//暂定检验科
                                productStockq.setProductId(productId);
                                //方法重新写
                                List<PdProductStock> productStockList = pdProductStockMapper.selectList(o_productStockq);
                                if(CollectionUtils.isNotEmpty(productStockList)){
                                    //新增一条使用中的明细信息,并扣减
                                    this.insertProdStock(productStockList.get(0),syNum);
                                }else{
                                    //没有库存明细数据的情况下，待处理



                                }
                            }else{
                                PdProductStock stock= productStocks.get(0);
                                Double specNum= stock.getSpecNum() - syNum;
                                if(specNum==0){
                                    stock.setSpecNum(specNum);
                                    stock.setStockNum(0.00);
                                    stock.setNestatStatus(PdConstant.STOCK_NESTAT_STATUS_2);//已用完
                                }else{
                                    stock.setSpecNum(specNum);
                                }
                                pdProductStockMapper.updateStockSpecNum(stock);
                            }

                       }
                   }else{
                       //待处理，数据先落地，后续配置完成后通过页面提交按钮去操作扣减库存
                   }
        }

       //先获取lis系统推送过来的数据以及检验项目代码等信息
        //根据检验项目代码查询检验项目配置表的配置用量明细信息
        //根据所配置的用量明细进行扣减库存
        //扣减库存是扣减规格数量的数据；

    /**3.扣减库存时，根据单位换算可通过规格数量扣减库存。
     * 当通过规格单位扣减库存时，且规格数量和包装数量不一致时，
     * 在原有库存数量减1，同时在库存表中增加一行库存数据，该行占用状态为使用中状态，
     * 不允许做用量之外的其他业务。新增行数据的包装单位为1，
     * 规格数量扣减一人份。当规格数量全部扣减完后，包装数量也为0。
     *
 结论：1)库存明细表增加占用状态字段：0：使用中  1：未使用
 2)新增的一行库存数据（使用中）只做用量扣减操作；
 3)除用量之外都扣减未使用的库存；


 4.试剂扣减库存的问题：
 例如：
 一瓶试剂为100人份，试剂用到80人份就一瓶使用完了，
     但是系统上库存的规格数量还有20人份或者100人份的试剂用到110人份了，
     这块需要如何处理，因为如果按照人份来扣减库存，就会存在一个系统库存和实际用量不一致的问题；

 结论：少用的情况下，如果1瓶100人份，实际用到了110人份，
     系统还是按照正常的扣减库存逻辑处理，就是扣完1瓶100人份，
     在继续扣减第二瓶的10份；
 多用的情况下，可以人为去修改库存明细规格数量和库存数量（这里的修改是直接规格数量和库存数量归零），
     且必须填写修改原因；*/

        //1、扣减出库库存，扣减出库库存明细
       /* for (PdDosageDetail dosageDetail : dosageDetails) {
            String productId = dosageDetail.getProductId();    //产品ID
            String prodBarcode = dosageDetail.getProductBarCode();  //条码
            String batchNo = dosageDetail.getBatchNo();
            String huoweiCode = dosageDetail.getOutHuoweiCode();
            Double productNum = dosageDetail.getDosageCount();  //数量
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
            o_productStockq.setDepartId(departId);
            o_productStockq.setProductId(productId);
            o_productStockq.setProductBarCode(prodBarcode);
            o_productStockq.setBatchNo(batchNo);
            o_productStockq.setHuoweiCode(huoweiCode);
            List<PdProductStock> productStocks = pdProductStockMapper.selectList(o_productStockq);
            if (productStocks != null && productStocks.size() >= 0) {
                PdProductStock productStock = productStocks.get(0);
                Double stockNum = productStock.getStockNum();
                Double num = stockNum - productNum;
                productStock.setStockNum(num);
                pdProductStockMapper.updateStockNum(productStock);
            }else {
                throw new RuntimeException("库存明细没有产品[" + dosageDetail.getProductName() + "]！");
            }
        }*/
        return PdConstant.TRUE;
    }



    //在原有库存数量减1，同时在库存表中增加一行库存数据，该行占用状态为使用中状态，
    public String insertProdStock(PdProductStock productStock,Double syNum){
       //将原有的库存明细中的库存数量减1
        Double stockNum=productStock.getStockNum();
        productStock.setStockNum(stockNum-1);
        pdProductStockMapper.updateStockNum(productStock);
        productStock.setId(null);
        productStock.setStockNum(1.00);
        Double specQuantity=productStock.getSpecQuantity();
        Double specNum=specQuantity - syNum;
        productStock.setSpecNum(specNum);
        pdProductStockMapper.insert(productStock);
        return PdConstant.TRUE;
    }
}
