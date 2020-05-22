package org.jeecg.modules.pd.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.constant.PdConstant;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.modules.external.service.IExDeductuinDosageService;
import org.jeecg.modules.pd.entity.*;
import org.jeecg.modules.pd.mapper.PdProductMapper;
import org.jeecg.modules.pd.mapper.PdProductStockMapper;
import org.jeecg.modules.pd.mapper.PdProductStockTotalMapper;
import org.jeecg.modules.pd.service.*;
import org.jeecg.modules.pd.util.SnowUtils;
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
    private IPdUsePackageDetailService  usePackageDetailService;
    @Autowired
    private IPdUsePackageService pdUsePackageService;
    @Autowired
    private IHisDepartService hisDepartService;
    @Autowired
    private IPdSpecLogService pdSpecLogService;
    @Autowired
    private IExDeductuinDosageService exDeductuinDosageService;

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
            productStock.setRefBarCode(stockRecordDetail.getRefBarCode());
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
                productStock.setSpecNum(stockRecordDetail.getSpecQuantity() == null ? 0D : stockRecordDetail.getSpecQuantity() * num);// 库存规格数量= 产品规格数量* 入库数量

                pdProductStockMapper.updateStockNum(productStock);
            } else {
                throw new RuntimeException("库存没有产品[" + stockRecordDetail.getProductName() + "]！");
            }
        }

        return PdConstant.TRUE;
    }

    /**
     * 定数包拆包加库存
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
            List<PdProductStock> productStocks = pdProductStockMapper.selectList(o_productStockq);
            // 扣减库存
            if (CollectionUtils.isNotEmpty(productStocks)) {
                PdProductStock productStock = productStocks.get(0);
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
     * 定数包打包扣库存
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
        i_productStockq.setNestatStatus(PdConstant.STOCK_NESTAT_STATUS_1);
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
            pdproductStock.setSpecQuantity(productStock.getSpecQuantity());
            pdproductStock.setSpecUnitId(productStock.getSpecUnitId());
            pdproductStock.setRefBarCode(SnowUtils.bigKey());
            productStockService.save(pdproductStock);
            //更新老货位上的库存数量
            PdProductStock pdProductStock_1 = new PdProductStock();
            pdProductStock_1.setStockNum(stockNum - ykStockNum);
            pdProductStock_1.setId(productStock.getId());
            pdProductStock_1.setSpecNum(productStock.getSpecQuantity() == null ? 0D : productStock.getSpecQuantity()    * pdProductStock_1.getStockNum());// 库存规格数量= 产品规格数量* 入库数量
            productStockService.updateProductStock(pdProductStock_1);
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
                pdProductStockMapper.updateProductStock(pdProductStock_2);
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
     * 	试剂耗材产品更新库存用量信息(Lis系统推送的数据)
     */
    @Transactional
    @Override
    public String lisUpdateUseStock(String testDepartment,List<PdUsePackageDetail> detailList) {
        //HisDepartInf hisDepartInf=hisDepartService.queryHisDepart(testDepartment);
        String departId="743dc34c1bcd4e4fa9503ccebce7edc6";//目前只扣减检验科的
                       for(PdUsePackageDetail detail:detailList){
                        String productId= detail.getProductId();//产品ID
                        String productFlag= detail.getProductFlag();
                        Double count= detail.getCount();//配置的使用量
                           PdProductStockTotal stockTotal=new PdProductStockTotal();
                           stockTotal.setDepartId(departId);
                           stockTotal.setProductId(productId);
                           List<PdProductStockTotal> stockTotals= pdProductStockTotalMapper.findForUpdate(stockTotal);
                           if(CollectionUtils.isEmpty(stockTotals)){
                               throw new RuntimeException("扣减总库存失败,根据产品ID：["+productId+"]获取不到库存信息");
                           }
                       if(PdConstant.PRODUCT_FLAG_0.equals(productFlag)){ //普通耗材
                           //扣减总库存
                           PdProductStockTotal productStockTotal = stockTotals.get(0);
                           Double stockNum_i = productStockTotal.getStockNum();
                           Double newStockNum = stockNum_i - count;
                           productStockTotal.setStockNum(newStockNum);
                           pdProductStockTotalMapper.updateStockNum(productStockTotal);
                          //扣减明细库存
                           PdProductStock stock=new PdProductStock();
                           stock.setDepartId(departId);
                           stock.setProductId(productId);
                           List<PdProductStock> stocks= pdProductStockMapper.selectOrExpDate(stock);
                           if(CollectionUtils.isEmpty(stocks)){
                               throw new RuntimeException("扣减库存失败,根据产品ID：["+productId+"]获取不到库存信息");
                           }
                           PdProductStock productStock_i = stocks.get(0);
                           Double num = productStock_i.getStockNum() - count;
                           if (num < 0) {
                               throw new RuntimeException("扣减库存失败，[" + productStock_i.getProductName() + "]库存数量不足");
                           }
                           productStock_i.setStockNum(num);
                           productStock_i.setSpecNum(productStock_i.getSpecQuantity() == null ? 0D : productStock_i.getSpecQuantity()    * productStock_i.getStockNum());// 库存规格数量= 产品规格数量* 入库数量
                           pdProductStockMapper.updateStockNum(productStock_i);
                       }else{ //试剂
                           //4：否则是试剂
                           //4.1 查询扣減科室下库存明细(先查询使用中的)，根据有效期排序
                           PdProductStock pproductStockq = new PdProductStock();
                           pproductStockq.setDepartId(departId);
                           pproductStockq.setProductId(productId);
                           pproductStockq.setNestatStatus(PdConstant.STOCK_NESTAT_STATUS_0);
                           List<PdProductStock> productStocks_i = pdProductStockMapper.selectOrExpDate(pproductStockq);
                           // 4.2 如果没有使用中的库存明細，就查詢未使用的庫存明細（根据有效期排序）
                           if(CollectionUtils.isEmpty(productStocks_i)){
                               PdProductStock productStockq = new PdProductStock();
                               productStockq.setDepartId(departId);
                               productStockq.setProductId(productId);
                               productStockq.setNestatStatus(PdConstant.STOCK_NESTAT_STATUS_1);
                               List<PdProductStock>  StockList = pdProductStockMapper.selectOrExpDate(productStockq);
                               // 4.3 如果有值，則取最近有效期的数据用于新增一条使用中的明细信息,并扣减，否则报错
                               if(CollectionUtils.isNotEmpty(StockList)){
                                   //3.1 扣减总库存数量
                                   PdProductStockTotal productStockTotal = stockTotals.get(0);
                                   for(PdProductStock psii :StockList ){
                                       if(psii.getSpecNum()>=count){
                                           Double specQuantity = StockList.get(0).getSpecQuantity();
                                           Double bzCount = (Double) Math.ceil(count/specQuantity);
                                           if(productStockTotal.getStockNum()<bzCount){
                                               throw new RuntimeException("扣减库存失败,根据产品：["+psii.getProductName()+"]获取不到库存明细信息");
                                           }
                                           Double stockNum_i = productStockTotal.getStockNum();
                                           Double newStockNum = stockNum_i - (bzCount-1);
                                           //如果包装数量没有变则不扣减总库存
                                           if(stockNum_i!=newStockNum){
                                               productStockTotal.setStockNum(newStockNum);
                                               pdProductStockTotalMapper.updateStockNum(productStockTotal);
                                           }
                                           //扣减库存数量
                                           Double newSpecNum = psii.getSpecNum();
                                           //新建一条记录的规格数量
                                           Double newSpecNum_i = (bzCount*specQuantity)-count;
                                           //原本库存的规格数量
                                           Double zNewSpecNum_i = newSpecNum-(bzCount*specQuantity);
                                           psii.setSpecNum(zNewSpecNum_i);
                                           if(zNewSpecNum_i==0.00){
                                               psii.setNestatStatus(PdConstant.STOCK_NESTAT_STATUS_2);//已用完
                                               psii.setStockNum(0.00);
                                               pdProductStockMapper.updateStockNum(psii);
                                               //已用完修改库存总表
                                               Double stockTotalNum_i = productStockTotal.getStockNum();
                                               Double newStockTotalNum = stockTotalNum_i - 1;
                                               productStockTotal.setStockNum(newStockTotalNum);
                                               pdProductStockTotalMapper.updateStockNum(productStockTotal);
                                           }else{
                                               Double stockNum_ii = psii.getStockNum() - bzCount;
                                               psii.setStockNum(stockNum_ii);
                                               pdProductStockMapper.updateStockNum(psii);
                                               if(newSpecNum_i!=0.00){
                                                   psii.setId(null);
                                                   //如果已用完改状态
                                                   psii.setStockNum(1.00);
                                                   psii.setNestatStatus(PdConstant.STOCK_NESTAT_STATUS_0);
                                                   psii.setSpecNum(newSpecNum_i);
                                                   pdProductStockMapper.insert(psii);
                                               }else{
                                                   //已用完修改库存总表
                                                   Double stockTotalNum_i = productStockTotal.getStockNum();
                                                   Double newStockTotalNum = stockTotalNum_i - 1;
                                                   productStockTotal.setStockNum(newStockTotalNum);
                                                   pdProductStockTotalMapper.updateStockNum(productStockTotal);
                                               }
                                           }
                                           //记录扣减用量明细表
                                           psii.setProductNum(count);
                                           exDeductuinDosageService.saveExdeuctuinDosageAcc(psii);
                                           break;
                                       }else{
                                           Double stockNum_i = productStockTotal.getStockNum();
                                           Double newStockNum = stockNum_i - psii.getStockNum();
                                           productStockTotal.setStockNum(newStockNum);
                                           pdProductStockTotalMapper.updateStockNum(productStockTotal);

                                           psii.setProductNum(psii.getSpecNum());
                                           count = count - psii.getSpecNum();
                                           psii.setStockNum(0.00);
                                           psii.setSpecNum(0.00);
                                           psii.setNestatStatus(PdConstant.STOCK_NESTAT_STATUS_2);//已用完
                                           pdProductStockMapper.updateStockNum(psii);
                                           //记录扣减用量明细表
                                           exDeductuinDosageService.saveExdeuctuinDosageAcc(psii);
                                       }
                                   }
                               }else{
                                   throw new RuntimeException("扣减库存失败,根据产品ID：["+productId+"]获取不到库存信息");
                               }
                           }else {
                               //4.3 直接扣减使用中的库存规格数量
                               try{
                                   PdProductStockTotal productStockTotal = stockTotals.get(0);
                                   //先消耗所有使用中的库存
                                   for(PdProductStock psi :productStocks_i){
                                       if(psi.getSpecNum()>=count){
                                           Double specNum = psi.getSpecNum() - count;
                                           //如果已用完改状态
                                           if(specNum==0.00){
                                               psi.setStockNum(0.00);
                                               psi.setNestatStatus(PdConstant.STOCK_NESTAT_STATUS_2);
                                               Double stockTotalNum_i = productStockTotal.getStockNum();
                                               Double newStockTotalNum = stockTotalNum_i - 1;
                                               productStockTotal.setStockNum(newStockTotalNum);
                                               pdProductStockTotalMapper.updateStockNum(productStockTotal);
                                           }
                                           psi.setSpecNum(specNum);
                                           pdProductStockMapper.updateStockSpecNum(psi);
                                           //扣减完毕后归零
                                           psi.setProductNum(count);
                                           count = 0.00;
                                           //记录扣减用量明细表
                                           exDeductuinDosageService.saveExdeuctuinDosageAcc(psi);
                                           break;
                                       }else{
                                           psi.setProductNum(psi.getSpecNum());
                                           count = count - psi.getSpecNum();
                                           psi.setStockNum(0.00);
                                           psi.setSpecNum(0.00);
                                           psi.setNestatStatus(PdConstant.STOCK_NESTAT_STATUS_2);//已用完
                                           pdProductStockMapper.updateStockNum(psi);
                                           Double stockTotalNum_i = productStockTotal.getStockNum();
                                           Double newStockTotalNum = stockTotalNum_i - 1;
                                           productStockTotal.setStockNum(newStockTotalNum);
                                           pdProductStockTotalMapper.updateStockNum(productStockTotal);
                                           //记录扣减用量明细表
                                           exDeductuinDosageService.saveExdeuctuinDosageAcc(psi);
                                       }
                                   }
                                   //如果使用中的全部消耗完毕也没扣减完则查询未使用的
                                   if(count>0.00){
                                       PdProductStock productStockq = new PdProductStock();
                                       productStockq.setDepartId(departId);
                                       productStockq.setProductId(productId);
                                       productStockq.setNestatStatus(PdConstant.STOCK_NESTAT_STATUS_1);
                                       List<PdProductStock>  StockList = pdProductStockMapper.selectOrExpDate(productStockq);
                                       for(PdProductStock psii :StockList ){
                                           if(psii.getSpecNum()>=count){
                                               Double specQuantity = productStocks_i.get(0).getSpecQuantity();
                                               Double bzCount = (Double) Math.ceil(count/specQuantity);
                                               //3.1 扣减总库存数量
                                               if(productStockTotal.getStockNum()<bzCount){
                                                   throw new RuntimeException("扣减库存失败,根据产品ID：["+psii.getProductName()+"]获取不到库存明细信息");
                                               }
                                               Double stockNum_i = productStockTotal.getStockNum();
                                               Double newStockNum = stockNum_i - (bzCount-1);
                                               //如果包装数量没有变则不扣减总库存
                                               if(stockNum_i!=newStockNum){
                                                   productStockTotal.setStockNum(newStockNum);
                                                   pdProductStockTotalMapper.updateStockNum(productStockTotal);
                                               }
                                               //扣减库存数量
                                               Double newSpecNum = psii.getSpecNum();
                                               //新建一条记录的规格数量
                                               Double newSpecNum_i = (bzCount*specQuantity)-count;
                                               //原本库存的规格数量
                                               Double zNewSpecNum_i = newSpecNum-(bzCount*specQuantity);
                                               psii.setSpecNum(zNewSpecNum_i);
                                               if(zNewSpecNum_i==0.00){
                                                   psii.setNestatStatus(PdConstant.STOCK_NESTAT_STATUS_2);//已用完
                                                   psii.setStockNum(0.00);
                                                   pdProductStockMapper.updateStockNum(psii);
                                                   //已用完修改库存总表
                                                   Double stockTotalNum_i = productStockTotal.getStockNum();
                                                   Double newStockTotalNum = stockTotalNum_i - 1;
                                                   productStockTotal.setStockNum(newStockTotalNum);
                                                   pdProductStockTotalMapper.updateStockNum(productStockTotal);
                                               }else{
                                                   Double stockNum_ii = psii.getStockNum() - bzCount;
                                                   psii.setStockNum(stockNum_ii);
                                                   pdProductStockMapper.updateStockNum(psii);
                                                   if(newSpecNum_i!=0.00){
                                                       psii.setId(null);
                                                       //如果已用完改状态
                                                       psii.setStockNum(1.00);
                                                       psii.setNestatStatus(PdConstant.STOCK_NESTAT_STATUS_0);
                                                       psii.setSpecNum(newSpecNum_i);
                                                       pdProductStockMapper.insert(psii);
                                                   }else{
                                                       //已用完修改库存总表
                                                       Double stockTotalNum_i = productStockTotal.getStockNum();
                                                       Double newStockTotalNum = stockTotalNum_i - 1;
                                                       productStockTotal.setStockNum(newStockTotalNum);
                                                       pdProductStockTotalMapper.updateStockNum(productStockTotal);
                                                   }
                                               }
                                               psii.setProductNum(count);
                                               //记录扣减用量明细表
                                               exDeductuinDosageService.saveExdeuctuinDosageAcc(psii);
                                               break;
                                           }else{
                                               Double stockNum_i = productStockTotal.getStockNum();
                                               Double newStockNum = stockNum_i - psii.getStockNum();
                                               productStockTotal.setStockNum(newStockNum);
                                               pdProductStockTotalMapper.updateStockNum(productStockTotal);
                                               psii.setProductNum(psii.getSpecNum());
                                               count = count - psii.getSpecNum();
                                               psii.setStockNum(0.00);
                                               psii.setSpecNum(0.00);
                                               psii.setNestatStatus(PdConstant.STOCK_NESTAT_STATUS_2);//已用完
                                               pdProductStockMapper.updateStockNum(psii);
                                               //记录扣减用量明细表
                                               exDeductuinDosageService.saveExdeuctuinDosageAcc(psii);
                                           }
                                       }
                                   }
                               }catch (Exception e){
                                   throw new RuntimeException("扣减库存失败，[" + productStocks_i.get(0).getProductName() + "]库存数量不足");
                               }
                             }
                           }
                         }
        return PdConstant.TRUE;
    }


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
                List<PdProductStock> productStocks = pdProductStockMapper.selectList(o_productStockq);
                if(CollectionUtils.isEmpty(productStocks)){
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
                    PdProductStock productStock_i = productStocks.get(0);
                    Double num = productStock_i.getStockNum() - count;
                    if (num < 0) {
                        throw new RuntimeException("扣减库存失败，[" + productStock_i.getProductName() + "]库存数量不足");
                    }
                    productStock_i.setStockNum(num);
                    productStock_i.setSpecNum(productStock.getSpecQuantity() == null ? 0D : productStock.getSpecQuantity()    * productStock_i.getStockNum());// 库存规格数量= 产品规格数量* 入库数量
                    pdProductStockMapper.updateStockNum(productStock_i);
                }else{
                    //4：否则是试剂
                    //4.1 查询扣減科室下库存明细(先查询使用中的)，根据有效期排序
                    PdProductStock pproductStockq = new PdProductStock();
                    pproductStockq.setDepartId(departId);
                    pproductStockq.setProductId(productId);
                    pproductStockq.setNestatStatus(PdConstant.STOCK_NESTAT_STATUS_0);
                    List<PdProductStock> productStocks_i = pdProductStockMapper.selectOrExpDate(pproductStockq);
                    // 4.2 如果没有使用中的库存明細，就查詢未使用的庫存明細（根据有效期排序）
                    if(CollectionUtils.isEmpty(productStocks_i)){
                        PdProductStock productStockq = new PdProductStock();
                        productStockq.setDepartId(departId);
                        productStockq.setProductId(productId);
                        productStockq.setNestatStatus(PdConstant.STOCK_NESTAT_STATUS_1);
                        List<PdProductStock>  StockList = pdProductStockMapper.selectOrExpDate(productStockq);
                        // 4.3 如果有值，則取最近有效期的数据用于新增一条使用中的明细信息,并扣减，否则报错
                        if(CollectionUtils.isNotEmpty(StockList)){
                            //3.1 扣减总库存数量
                            PdProductStockTotal productStockTotal = stockTotals.get(0);
                            for(PdProductStock psii :StockList ){
                                if(psii.getSpecNum()>=count){
                                    Double specQuantity = StockList.get(0).getSpecQuantity();
                                    Double bzCount = (Double) Math.ceil(count/specQuantity);
                                    if(productStockTotal.getStockNum()<bzCount){
                                        throw new RuntimeException("扣减库存失败,根据产品：["+psii.getProductName()+"]获取不到库存明细信息");
                                    }
                                    Double stockNum_i = productStockTotal.getStockNum();
                                    Double newStockNum = stockNum_i - (bzCount-1);
                                    //如果包装数量没有变则不扣减总库存
                                    if(stockNum_i!=newStockNum){
                                        productStockTotal.setStockNum(newStockNum);
                                        pdProductStockTotalMapper.updateStockNum(productStockTotal);
                                    }
                                    //扣减库存数量
                                    Double newSpecNum = psii.getSpecNum();
                                    //新建一条记录的规格数量
                                    Double newSpecNum_i = (bzCount*specQuantity)-count;
                                    //原本库存的规格数量
                                    Double zNewSpecNum_i = newSpecNum-(bzCount*specQuantity);
                                    psii.setSpecNum(zNewSpecNum_i);
                                    if(zNewSpecNum_i==0.00){
                                        psii.setNestatStatus(PdConstant.STOCK_NESTAT_STATUS_2);//已用完
                                        psii.setStockNum(0.00);
                                        pdProductStockMapper.updateStockNum(psii);
                                        //已用完修改库存总表
                                        Double stockTotalNum_i = productStockTotal.getStockNum();
                                        Double newStockTotalNum = stockTotalNum_i - 1;
                                        productStockTotal.setStockNum(newStockTotalNum);
                                        pdProductStockTotalMapper.updateStockNum(productStockTotal);
                                    }else{
                                        Double stockNum_ii = psii.getStockNum() - bzCount;
                                        psii.setStockNum(stockNum_ii);
                                        pdProductStockMapper.updateStockNum(psii);
                                        if(newSpecNum_i!=0.00){
                                            psii.setId(null);
                                            //如果已用完改状态
                                            psii.setStockNum(1.00);
                                            psii.setNestatStatus(PdConstant.STOCK_NESTAT_STATUS_0);
                                            psii.setSpecNum(newSpecNum_i);
                                            pdProductStockMapper.insert(psii);
                                        }else{
                                            //已用完修改库存总表
                                            Double stockTotalNum_i = productStockTotal.getStockNum();
                                            Double newStockTotalNum = stockTotalNum_i - 1;
                                            productStockTotal.setStockNum(newStockTotalNum);
                                            pdProductStockTotalMapper.updateStockNum(productStockTotal);
                                        }
                                    }
                                    //记录扣减用量明细表
                                    psii.setProductNum(count);
                                    exDeductuinDosageService.saveExdeuctuinDosage(psii);
                                    break;
                                }else{
                                    Double stockNum_i = productStockTotal.getStockNum();
                                    Double newStockNum = stockNum_i - psii.getStockNum();
                                    productStockTotal.setStockNum(newStockNum);
                                    pdProductStockTotalMapper.updateStockNum(productStockTotal);

                                    psii.setProductNum(psii.getSpecNum());
                                    count = count - psii.getSpecNum();
                                    psii.setStockNum(0.00);
                                    psii.setSpecNum(0.00);
                                    psii.setNestatStatus(PdConstant.STOCK_NESTAT_STATUS_2);//已用完
                                    pdProductStockMapper.updateStockNum(psii);
                                    //记录扣减用量明细表
                                    exDeductuinDosageService.saveExdeuctuinDosage(psii);
                                }
                            }
                        }else{
                            throw new RuntimeException("扣减库存失败,根据产品ID：["+productId+"]获取不到库存信息");
                        }
                    }else {
                        //4.3 直接扣减使用中的库存规格数量
                        try{
                            PdProductStockTotal productStockTotal = stockTotals.get(0);
                            //先消耗所有使用中的库存
                            for(PdProductStock psi :productStocks_i){
                                if(psi.getSpecNum()>=count){
                                    Double specNum = psi.getSpecNum() - count;
                                    //如果已用完改状态
                                    if(specNum==0.00){
                                        psi.setStockNum(0.00);
                                        psi.setNestatStatus(PdConstant.STOCK_NESTAT_STATUS_2);
                                        Double stockTotalNum_i = productStockTotal.getStockNum();
                                        Double newStockTotalNum = stockTotalNum_i - 1;
                                        productStockTotal.setStockNum(newStockTotalNum);
                                        pdProductStockTotalMapper.updateStockNum(productStockTotal);
                                    }
                                    psi.setSpecNum(specNum);
                                    pdProductStockMapper.updateStockSpecNum(psi);
                                    //扣减完毕后归零
                                    psi.setProductNum(count);
                                    count = 0.00;
                                    //记录扣减用量明细表
                                    exDeductuinDosageService.saveExdeuctuinDosage(psi);
                                    break;
                                }else{
                                    psi.setProductNum(psi.getSpecNum());
                                    count = count - psi.getSpecNum();
                                    psi.setStockNum(0.00);
                                    psi.setSpecNum(0.00);
                                    psi.setNestatStatus(PdConstant.STOCK_NESTAT_STATUS_2);//已用完
                                    pdProductStockMapper.updateStockNum(psi);
                                    Double stockTotalNum_i = productStockTotal.getStockNum();
                                    Double newStockTotalNum = stockTotalNum_i - 1;
                                    productStockTotal.setStockNum(newStockTotalNum);
                                    pdProductStockTotalMapper.updateStockNum(productStockTotal);
                                    //记录扣减用量明细表
                                    exDeductuinDosageService.saveExdeuctuinDosage(psi);
                                }
                            }
                            //如果使用中的全部消耗完毕也没扣减完则查询未使用的
                            if(count>0.00){
                                PdProductStock productStockq = new PdProductStock();
                                productStockq.setDepartId(departId);
                                productStockq.setProductId(productId);
                                productStockq.setNestatStatus(PdConstant.STOCK_NESTAT_STATUS_1);
                                List<PdProductStock>  StockList = pdProductStockMapper.selectOrExpDate(productStockq);
                                for(PdProductStock psii :StockList ){
                                    if(psii.getSpecNum()>=count){
                                        Double specQuantity = productStocks_i.get(0).getSpecQuantity();
                                        Double bzCount = (Double) Math.ceil(count/specQuantity);
                                        //3.1 扣减总库存数量
                                        if(productStockTotal.getStockNum()<bzCount){
                                            throw new RuntimeException("扣减库存失败,根据产品ID：["+psii.getProductName()+"]获取不到库存明细信息");
                                        }
                                        Double stockNum_i = productStockTotal.getStockNum();
                                        Double newStockNum = stockNum_i - (bzCount-1);
                                        //如果包装数量没有变则不扣减总库存
                                        if(stockNum_i!=newStockNum){
                                            productStockTotal.setStockNum(newStockNum);
                                            pdProductStockTotalMapper.updateStockNum(productStockTotal);
                                        }
                                        //扣减库存数量
                                        Double newSpecNum = psii.getSpecNum();
                                        //新建一条记录的规格数量
                                        Double newSpecNum_i = (bzCount*specQuantity)-count;
                                        //原本库存的规格数量
                                        Double zNewSpecNum_i = newSpecNum-(bzCount*specQuantity);
                                        psii.setSpecNum(zNewSpecNum_i);
                                        if(zNewSpecNum_i==0.00){
                                            psii.setNestatStatus(PdConstant.STOCK_NESTAT_STATUS_2);//已用完
                                            psii.setStockNum(0.00);
                                            pdProductStockMapper.updateStockNum(psii);
                                            //已用完修改库存总表
                                            Double stockTotalNum_i = productStockTotal.getStockNum();
                                            Double newStockTotalNum = stockTotalNum_i - 1;
                                            productStockTotal.setStockNum(newStockTotalNum);
                                            pdProductStockTotalMapper.updateStockNum(productStockTotal);
                                        }else{
                                            Double stockNum_ii = psii.getStockNum() - bzCount;
                                            psii.setStockNum(stockNum_ii);
                                            pdProductStockMapper.updateStockNum(psii);
                                            if(newSpecNum_i!=0.00){
                                                psii.setId(null);
                                                //如果已用完改状态
                                                psii.setStockNum(1.00);
                                                psii.setNestatStatus(PdConstant.STOCK_NESTAT_STATUS_0);
                                                psii.setSpecNum(newSpecNum_i);
                                                pdProductStockMapper.insert(psii);
                                            }else{
                                                //已用完修改库存总表
                                                Double stockTotalNum_i = productStockTotal.getStockNum();
                                                Double newStockTotalNum = stockTotalNum_i - 1;
                                                productStockTotal.setStockNum(newStockTotalNum);
                                                pdProductStockTotalMapper.updateStockNum(productStockTotal);
                                            }
                                        }
                                        psii.setProductNum(count);
                                        //记录扣减用量明细表
                                        exDeductuinDosageService.saveExdeuctuinDosage(psii);
                                        break;
                                    }else{
                                        Double stockNum_i = productStockTotal.getStockNum();
                                        Double newStockNum = stockNum_i - psii.getStockNum();
                                        productStockTotal.setStockNum(newStockNum);
                                        pdProductStockTotalMapper.updateStockNum(productStockTotal);
                                        psii.setProductNum(psii.getSpecNum());
                                        count = count - psii.getSpecNum();
                                        psii.setStockNum(0.00);
                                        psii.setSpecNum(0.00);
                                        psii.setNestatStatus(PdConstant.STOCK_NESTAT_STATUS_2);//已用完
                                        pdProductStockMapper.updateStockNum(psii);
                                        //记录扣减用量明细表
                                        exDeductuinDosageService.saveExdeuctuinDosage(psii);
                                    }
                                }
                            }
                        }catch (Exception e){
                            throw new RuntimeException("扣减库存失败，[" + productStocks_i.get(0).getProductName() + "]库存数量不足");
                        }
                    }
                }
            }
        }
        return PdConstant.TRUE;
    }


    /**
     *检验项目手动扣减库存方法(定数包)
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
                  String productId= packageDetail.getProductId();
                  String packageId= packageDetail.getPackageId();
                  Double count= packageDetail.getCount();
                  String productFlag=packageDetail.getProductFlag();//0:产品  1：试剂

                   //1:先查询扣减科室下总库存是否存在库存数据
                   PdProductStockTotal stockTotal=new PdProductStockTotal();
                   stockTotal.setDepartId(departId);
                   stockTotal.setProductId(productId);
                   List<PdProductStockTotal> stockTotals= pdProductStockTotalMapper.findForUpdate(stockTotal);
                   if(CollectionUtils.isEmpty(stockTotals)){
                       throw new RuntimeException("扣减总库存失败,根据产品ID：["+packageDetail.getProductName()+"]获取不到库存信息");
                   }
                    //2:如果存在数据，则根据产品ID查询该库存下的产品明细信息，根据有效期排序
                   PdProductStock stock=new PdProductStock();
                   stock.setDepartId(departId);
                   stock.setProductId(productId);
                   List<PdProductStock> productStocks= pdProductStockMapper.selectOrExpDate(stock);
                   if(CollectionUtils.isEmpty(productStocks)){
                       throw new RuntimeException("扣减库存失败,根据产品ID：["+packageDetail.getProductName()+"]获取不到库存明细信息");
                   }
                   //3：如果是普通产品
                   if(PdConstant.PRODUCT_FLAG_0.equals(productFlag)){
                       //判断用量是否大于产品总数量，如果大于则扣减失败
                       //3.1 扣减总库存数量
                       PdProductStockTotal productStockTotal = stockTotals.get(0);
                       if(productStockTotal.getStockNum()<count){
                           throw new RuntimeException("扣减库存失败,根据产品ID：["+packageDetail.getProductName()+"]获取不到库存明细信息");
                       }
                       Double stockNum_i = productStockTotal.getStockNum();
                       Double newStockNum = stockNum_i - count;
                       productStockTotal.setStockNum(newStockNum);
                       pdProductStockTotalMapper.updateStockNum(productStockTotal);
                       //3.2 扣减明细库存数量
                       try{
                           for(PdProductStock ps :productStocks){
                               if(ps.getStockNum()>=count){
                                   Double num = ps.getStockNum() - count;
                                   ps.setStockNum(num);
                                   if(num==0.00){
                                       ps.setNestatStatus(PdConstant.STOCK_NESTAT_STATUS_2);//已用完
                                   }
                                   pdProductStockMapper.updateStockNum(ps);
                                   ps.setPackageId(packageId);
                                   ps.setProductNum(count);
                                   productStockList.add(ps);
                                   break;
                               }else{
                                   ps.setPackageId(packageId);
                                   ps.setProductNum(ps.getStockNum());
                                   count = count - ps.getStockNum();
                                   ps.setStockNum(0.00);
                                   ps.setNestatStatus(PdConstant.STOCK_NESTAT_STATUS_2);//已用完
                                   pdProductStockMapper.updateStockNum(ps);
                                   productStockList.add(ps);
                               }
                           }
                       }catch (Exception e){
                           throw new RuntimeException("扣减库存失败，[" + productStocks.get(0).getProductName() + "]库存数量不足");
                       }
                   }else{
                       //4：否则是试剂
                       //4.1 查询扣減科室下库存明细(先查询使用中的)，根据有效期排序
                       PdProductStock o_productStockq = new PdProductStock();
                       o_productStockq.setDepartId(departId);
                       o_productStockq.setProductId(productId);
                       o_productStockq.setNestatStatus(PdConstant.STOCK_NESTAT_STATUS_0);
                       List<PdProductStock> productStocks_i = pdProductStockMapper.selectOrExpDate(o_productStockq);
                       // 4.2 如果没有使用中的库存明細，就查詢未使用的庫存明細（根据有效期排序）
                       if(CollectionUtils.isEmpty(productStocks_i)){
                           PdProductStock productStockq = new PdProductStock();
                           productStockq.setDepartId(departId);
                           productStockq.setProductId(productId);
                           productStockq.setNestatStatus(PdConstant.STOCK_NESTAT_STATUS_1);
                           List<PdProductStock>  StockList = pdProductStockMapper.selectOrExpDate(productStockq);
                           // 4.3 如果有值，則取最近有效期的数据用于新增一条使用中的明细信息,并扣减，否则报错
                           if(CollectionUtils.isNotEmpty(StockList)){
                               //3.1 扣减总库存数量
                               PdProductStockTotal productStockTotal = stockTotals.get(0);
                               for(PdProductStock psii :StockList ){
                                   if(psii.getSpecNum()>=count){
                                       Double specQuantity = StockList.get(0).getSpecQuantity();
                                       Double bzCount = (Double) Math.ceil(count/specQuantity);
                                       if(productStockTotal.getStockNum()<bzCount){
                                           throw new RuntimeException("扣减库存失败,根据产品：["+psii.getProductName()+"]获取不到库存明细信息");
                                       }
                                       Double stockNum_i = productStockTotal.getStockNum();
                                       Double newStockNum = stockNum_i - (bzCount-1);
                                       //如果包装数量没有变则不扣减总库存
                                       if(stockNum_i!=newStockNum){
                                           productStockTotal.setStockNum(newStockNum);
                                           pdProductStockTotalMapper.updateStockNum(productStockTotal);
                                       }
                                       //扣减库存数量
                                       Double newSpecNum = psii.getSpecNum();
                                       //新建一条记录的规格数量
                                       Double newSpecNum_i = (bzCount*specQuantity)-count;
                                       //原本库存的规格数量
                                       Double zNewSpecNum_i = newSpecNum-(bzCount*specQuantity);
                                       psii.setSpecNum(zNewSpecNum_i);
                                       if(zNewSpecNum_i==0.00){
                                           psii.setNestatStatus(PdConstant.STOCK_NESTAT_STATUS_2);//已用完
                                           psii.setStockNum(0.00);
                                           pdProductStockMapper.updateStockNum(psii);
                                           //已用完修改库存总表
                                           Double stockTotalNum_i = productStockTotal.getStockNum();
                                           Double newStockTotalNum = stockTotalNum_i - 1;
                                           productStockTotal.setStockNum(newStockTotalNum);
                                           pdProductStockTotalMapper.updateStockNum(productStockTotal);
                                       }else{
                                           Double stockNum_ii = psii.getStockNum() - bzCount;
                                           psii.setStockNum(stockNum_ii);
                                           pdProductStockMapper.updateStockNum(psii);
                                           if(newSpecNum_i!=0.00){
                                               psii.setId(null);
                                               //如果已用完改状态
                                               psii.setStockNum(1.00);
                                               psii.setNestatStatus(PdConstant.STOCK_NESTAT_STATUS_0);
                                               psii.setSpecNum(newSpecNum_i);
                                               pdProductStockMapper.insert(psii);
                                           }else{
                                               //已用完修改库存总表
                                               Double stockTotalNum_i = productStockTotal.getStockNum();
                                               Double newStockTotalNum = stockTotalNum_i - 1;
                                               productStockTotal.setStockNum(newStockTotalNum);
                                               pdProductStockTotalMapper.updateStockNum(productStockTotal);
                                           }
                                       }

                                       psii.setPackageId(packageId);
                                       psii.setProductNum(count);
                                       productStockList.add(psii);
                                       //记录扣减用量明细表
                                       exDeductuinDosageService.saveExdeuctuinDosage(psii);
                                       break;
                                   }else{
                                       Double stockNum_i = productStockTotal.getStockNum();
                                       Double newStockNum = stockNum_i - psii.getStockNum();
                                       productStockTotal.setStockNum(newStockNum);
                                       pdProductStockTotalMapper.updateStockNum(productStockTotal);
                                       psii.setPackageId(packageId);
                                       psii.setProductNum(psii.getSpecNum());
                                       count = count - psii.getSpecNum();
                                       psii.setStockNum(0.00);
                                       psii.setSpecNum(0.00);
                                       psii.setNestatStatus(PdConstant.STOCK_NESTAT_STATUS_2);//已用完
                                       pdProductStockMapper.updateStockNum(psii);
                                       productStockList.add(psii);
                                       //记录扣减用量明细表
                                       exDeductuinDosageService.saveExdeuctuinDosage(psii);
                                   }
                               }
                           }else{
                               throw new RuntimeException("扣减库存失败,根据产品ID：["+productId+"]获取不到库存信息");
                           }
                       }else {
                           //4.3 直接扣减使用中的库存规格数量
                            try{
                                PdProductStockTotal productStockTotal = stockTotals.get(0);
                                //先消耗所有使用中的库存
                                for(PdProductStock psi :productStocks_i){
                                    if(psi.getSpecNum()>=count){
                                        Double specNum = psi.getSpecNum() - count;
                                        //如果已用完改状态
                                        if(specNum==0.00){
                                            psi.setStockNum(0.00);
                                            psi.setNestatStatus(PdConstant.STOCK_NESTAT_STATUS_2);
                                            Double stockTotalNum_i = productStockTotal.getStockNum();
                                            Double newStockTotalNum = stockTotalNum_i - 1;
                                            productStockTotal.setStockNum(newStockTotalNum);
                                            pdProductStockTotalMapper.updateStockNum(productStockTotal);
                                        }
                                        psi.setSpecNum(specNum);
                                        pdProductStockMapper.updateStockSpecNum(psi);
                                        psi.setPackageId(packageId);
                                        psi.setProductNum(count);
                                        productStockList.add(psi);//存储需要返回的库存明细对象
                                        //扣减完毕后归零
                                        count = 0.00;
                                        //记录扣减用量明细表
                                        exDeductuinDosageService.saveExdeuctuinDosage(psi);
                                        break;
                                    }else{
                                        psi.setPackageId(packageId);
                                        psi.setProductNum(psi.getSpecNum());
                                        count = count - psi.getSpecNum();
                                        psi.setStockNum(0.00);
                                        psi.setSpecNum(0.00);
                                        psi.setNestatStatus(PdConstant.STOCK_NESTAT_STATUS_2);//已用完
                                        pdProductStockMapper.updateStockNum(psi);
                                        Double stockTotalNum_i = productStockTotal.getStockNum();
                                        Double newStockTotalNum = stockTotalNum_i - 1;
                                        productStockTotal.setStockNum(newStockTotalNum);
                                        pdProductStockTotalMapper.updateStockNum(productStockTotal);
                                        productStockList.add(psi);
                                        //记录扣减用量明细表
                                        exDeductuinDosageService.saveExdeuctuinDosage(psi);
                                    }

                                }
                                //如果使用中的全部消耗完毕也没扣减完则查询未使用的
                                if(count>0.00){
                                    PdProductStock productStockq = new PdProductStock();
                                    productStockq.setDepartId(departId);
                                    productStockq.setProductId(productId);
                                    productStockq.setNestatStatus(PdConstant.STOCK_NESTAT_STATUS_1);
                                    List<PdProductStock>  StockList = pdProductStockMapper.selectOrExpDate(productStockq);
                                    for(PdProductStock psii :StockList ){
                                        if(psii.getSpecNum()>=count){
                                            Double specQuantity = productStocks_i.get(0).getSpecQuantity();
                                            Double bzCount = (Double) Math.ceil(count/specQuantity);
                                            //3.1 扣减总库存数量
                                            if(productStockTotal.getStockNum()<bzCount){
                                                throw new RuntimeException("扣减库存失败,根据产品ID：["+psii.getProductName()+"]获取不到库存明细信息");
                                            }
                                            Double stockNum_i = productStockTotal.getStockNum();
                                            Double newStockNum = stockNum_i - (bzCount-1);
                                            //如果包装数量没有变则不扣减总库存
                                            if(stockNum_i!=newStockNum){
                                                productStockTotal.setStockNum(newStockNum);
                                                pdProductStockTotalMapper.updateStockNum(productStockTotal);
                                            }
                                            //扣减库存数量
                                            Double newSpecNum = psii.getSpecNum();
                                            //新建一条记录的规格数量
                                            Double newSpecNum_i = (bzCount*specQuantity)-count;
                                            //原本库存的规格数量
                                            Double zNewSpecNum_i = newSpecNum-(bzCount*specQuantity);
                                            psii.setSpecNum(zNewSpecNum_i);
                                            if(zNewSpecNum_i==0.00){
                                                psii.setNestatStatus(PdConstant.STOCK_NESTAT_STATUS_2);//已用完
                                                psii.setStockNum(0.00);
                                                pdProductStockMapper.updateStockNum(psii);
                                                //已用完修改库存总表
                                                Double stockTotalNum_i = productStockTotal.getStockNum();
                                                Double newStockTotalNum = stockTotalNum_i - 1;
                                                productStockTotal.setStockNum(newStockTotalNum);
                                                pdProductStockTotalMapper.updateStockNum(productStockTotal);
                                            }else{
                                                Double stockNum_ii = psii.getStockNum() - bzCount;
                                                psii.setStockNum(stockNum_ii);
                                                pdProductStockMapper.updateStockNum(psii);
                                                if(newSpecNum_i!=0.00){
                                                    psii.setId(null);
                                                    //如果已用完改状态
                                                    psii.setStockNum(1.00);
                                                    psii.setNestatStatus(PdConstant.STOCK_NESTAT_STATUS_0);
                                                    psii.setSpecNum(newSpecNum_i);
                                                    pdProductStockMapper.insert(psii);
                                                }else{
                                                    //已用完修改库存总表
                                                    Double stockTotalNum_i = productStockTotal.getStockNum();
                                                    Double newStockTotalNum = stockTotalNum_i - 1;
                                                    productStockTotal.setStockNum(newStockTotalNum);
                                                    pdProductStockTotalMapper.updateStockNum(productStockTotal);
                                                }
                                            }
                                            psii.setPackageId(packageId);
                                            psii.setProductNum(count);
                                            productStockList.add(psii);
                                            //记录扣减用量明细表
                                            exDeductuinDosageService.saveExdeuctuinDosage(psii);
                                            break;
                                        }else{
                                            Double stockNum_i = productStockTotal.getStockNum();
                                            Double newStockNum = stockNum_i - psii.getStockNum();
                                            productStockTotal.setStockNum(newStockNum);
                                            pdProductStockTotalMapper.updateStockNum(productStockTotal);
                                            psii.setPackageId(packageId);
                                            psii.setProductNum(psii.getSpecNum());
                                            count = count - psii.getSpecNum();
                                            psii.setStockNum(0.00);
                                            psii.setSpecNum(0.00);
                                            psii.setNestatStatus(PdConstant.STOCK_NESTAT_STATUS_2);//已用完
                                            pdProductStockMapper.updateStockNum(psii);
                                            productStockList.add(psii);
                                            //记录扣减用量明细表
                                            exDeductuinDosageService.saveExdeuctuinDosage(psii);
                                        }
                                    }
                                }
                            }catch (Exception e){
                                throw new RuntimeException("扣减库存失败，[" + productStocks_i.get(0).getProductName() + "]库存数量不足");
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
}
