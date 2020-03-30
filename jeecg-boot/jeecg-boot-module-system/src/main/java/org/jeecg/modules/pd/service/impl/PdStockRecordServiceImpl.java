package org.jeecg.modules.pd.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.constant.PdConstant;
import org.jeecg.common.system.vo.DictModel;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.util.DateUtils;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.pd.entity.*;
import org.jeecg.modules.pd.mapper.*;
import org.jeecg.modules.pd.service.*;
import org.jeecg.modules.pd.util.UUIDUtil;
import org.jeecg.modules.pd.vo.PdGoodsAllocationPage;
import org.jeecg.modules.system.entity.SysDepart;
import org.jeecg.modules.system.service.ISysDictService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.*;

/**
 * @Description: 出入库记录表
 * @Author: jiangxz
 * @Date: 2020-02-13
 * @Version: V1.0
 */
@Service
public class PdStockRecordServiceImpl extends ServiceImpl<PdStockRecordMapper, PdStockRecord> implements IPdStockRecordService {

    @Autowired
    private PdStockRecordMapper pdStockRecordMapper;
    @Autowired
    private PdStockRecordDetailMapper pdStockRecordDetailMapper;
    @Autowired
    private PdPurchaseOrderMergeDetailMapper pdPurchaseOrderMergeDetailMapper;
    @Autowired
    private PdApplyOrderMapper pdApplyOrderMapper;
    @Autowired
    private PdApplyDetailMapper pdApplyDetailMapper;
    @Autowired
    private PdAllocationRecordMapper pdAllocationRecordMapper;
    @Autowired
    private PdAllocationDetailMapper pdAllocationDetailMapper;
    @Autowired
    private IPdProductStockTotalService pdProductStockTotalService;
    @Autowired
    private IPdStockLogService pdStockLogService;
    @Autowired
    private IPdStockRecordDetailService pdStockRecordDetailService;
    @Autowired
    private IPdGoodsAllocationService pdGoodsAllocationService;
    @Autowired
    private ISysDictService sysDictService;
    @Autowired
    private IPdPurchaseOrderMergeDetailService pdPurchaseOrderMergeDetailService;
    @Autowired
    private IPdDepartService pdDepartService;
    @Autowired
    private IPdApplyDetailService pdApplyDetailService;
    @Autowired
    private IPdAllocationDetailService pdAllocationDetailService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveMain(PdStockRecord pdStockRecord, List<PdStockRecordDetail> pdStockRecordDetailList, String recordType) {
        // 修改前先删除数据
        if (oConvertUtils.isNotEmpty(pdStockRecord.getId())) {
            this.delMain(pdStockRecord.getId());
        }
        pdStockRecord.setCreateTime(DateUtils.getDate());
        if (PdConstant.RECODE_TYPE_1.equals(recordType)) {
            pdStockRecord.setRecordType(PdConstant.RECODE_TYPE_1); // 入库
            pdStockRecord.setSubmitStatus(PdConstant.SUBMIT_STATE_1); // 待提交
//            pdStockRecord.setAuditStatus(PdConstant.AUDIT_STATE_1);   // 待审核
            this.saveInStockRecord(pdStockRecord, pdStockRecordDetailList, "");
        } else if (PdConstant.RECODE_TYPE_2.equals(recordType)) {
            pdStockRecord.setRecordType(PdConstant.RECODE_TYPE_2); // 出库
            pdStockRecord.setSubmitStatus(PdConstant.SUBMIT_STATE_1); // 待提交
//            pdStockRecord.setAuditStatus(PdConstant.AUDIT_STATE_1);   // 待审核
            this.saveOutStockRecord(pdStockRecord, pdStockRecordDetailList);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void submit(PdStockRecord pdStockRecord, List<PdStockRecordDetail> pdStockRecordDetailList, String recordType) {
        // 修改前先删除数据
        if (oConvertUtils.isNotEmpty(pdStockRecord.getId())) {
            this.delMain(pdStockRecord.getId());
        }
        pdStockRecord.setRefuseReason("");//清空审批意见
        pdStockRecord.setCreateTime(DateUtils.getDate());
        if (PdConstant.RECODE_TYPE_1.equals(recordType)) {
            pdStockRecord.setRecordType(PdConstant.RECODE_TYPE_1); // 入库
            pdStockRecord.setSubmitStatus(PdConstant.SUBMIT_STATE_2); // 已提交
            pdStockRecord.setAuditStatus(PdConstant.AUDIT_STATE_1);   // 待审核
            this.saveInStockRecord(pdStockRecord, pdStockRecordDetailList, "");
        } else if (PdConstant.RECODE_TYPE_2.equals(recordType)) {
            pdStockRecord.setRecordType(PdConstant.RECODE_TYPE_2); // 出库
            pdStockRecord.setSubmitStatus(PdConstant.SUBMIT_STATE_2); // 已提交
            pdStockRecord.setAuditStatus(PdConstant.AUDIT_STATE_1);   // 待审核
            this.saveOutStockRecord(pdStockRecord, pdStockRecordDetailList);
        }
    }

    private String saveInStockRecord(PdStockRecord pdStockRecord, List<PdStockRecordDetail> pdStockRecordDetailList, String outType) {
        if (oConvertUtils.isNotEmpty(outType)) {
            // 调拨出库 或 申领出库 自动生成入库单
            if (PdConstant.OUT_TYPE_3.equals(outType)) {
                pdStockRecord.setInType(PdConstant.IN_TYPE_3);
            } else {
                pdStockRecord.setInType(PdConstant.IN_TYPE_1);
            }
            Date date = DateUtils.getDate();
            pdStockRecord.setRecordType(PdConstant.RECODE_TYPE_1);
            pdStockRecord.setOutType(null);
            pdStockRecord.setId(null); // 清空ID
            pdStockRecord.setRecordNo(UUIDUtil.generateOrderNoByType(PdConstant.ORDER_NO_FIRST_LETTER_RK)); //生成入库单号
            pdStockRecord.setAuditStatus(PdConstant.AUDIT_STATE_2);   // 审核通过
            pdStockRecord.setSubmitDate(date);
            pdStockRecord.setAuditDate(date);
            pdStockRecord.setUpdateTime(date);
            pdStockRecord.setCreateTime(date);
        }
        pdStockRecordMapper.insert(pdStockRecord);

        List<PdStockRecordDetail> newDetailList = new ArrayList<>();
        Set<String> setIds = new HashSet<>();

        if (CollectionUtils.isNotEmpty(pdStockRecordDetailList)) {
            //相同产品合并
            for (PdStockRecordDetail main : pdStockRecordDetailList) {
                String expDate = DateUtils.date2Str(main.getExpDate(), DateUtils.yyMMdd.get());

                // 1. 如果产品ID、批号、效期一样，则赋值条码
                if(oConvertUtils.isEmpty(main.getProductBarCode())){
                    for (PdStockRecordDetail entity : pdStockRecordDetailList) {
                        if(main.getProductId().equals(entity.getProductId()) && main.getBatchNo().equals(entity.getBatchNo()) && main.getExpDate().equals(entity.getExpDate())
                                && oConvertUtils.isNotEmpty(entity.getProductBarCode())){
                            main.setProductBarCode(entity.getProductBarCode());
                            break;
                        }
                    }
                }

                // 2. 如果第1步没有赋值到条码，则自动拼条码
                if(oConvertUtils.isEmpty(main.getProductBarCode())){
                    main.setProductBarCode("01" + main.getProductNumber() + "17" + expDate + "10" + main.getBatchNo());
                }

                StringBuilder setId = new StringBuilder();
                setId.append(main.getProductId()).append(main.getBatchNo()).append(main.getExpDate()).append(main.getInHuoweiCode());
                Double productNum = 0D;
                String mainHuoweCode = main.getInHuoweiCode() == null ? "" : main.getInHuoweiCode();
                // 3.合并数量
                if(setIds.add(setId.toString())){
                    for (PdStockRecordDetail entity : pdStockRecordDetailList) {
                        String entityHuoweCode = entity.getInHuoweiCode() == null ? "" : entity.getInHuoweiCode();
                        if(main.getProductId().equals(entity.getProductId()) && main.getBatchNo().equals(entity.getBatchNo())
                                && main.getExpDate().equals(entity.getExpDate()) && mainHuoweCode.equals(entityHuoweCode)){
                            productNum = productNum + entity.getProductNum();
                        }
                    }
                    if(oConvertUtils.isNotEmpty(pdStockRecord.getSupplierId()) && oConvertUtils.isEmpty(main.getSupplierId())){
                        main.setSupplierId(pdStockRecord.getSupplierId());
                    }
                    main.setProductNum(productNum);
                    newDetailList.add(main);
                }
            }
        }

        if(CollectionUtils.isNotEmpty(newDetailList)){
            for (PdStockRecordDetail detail : newDetailList) {
                detail.setId(null);//初始化ID (从前端传过来会自带页面列表行的ID)
                detail.setRecordId(pdStockRecord.getId());//外键设置
                detail.setDelFlag(PdConstant.DEL_FLAG_0);
                detail.setProductStockId(null); //清空库存ID
                detail.setCreateTime(DateUtils.getDate());
                // 如果是申领或调拨入库
                if (PdConstant.OUT_TYPE_1.equals(outType)) {
                    detail.setImportNo(pdStockRecord.getApplyNo());
                } else if (PdConstant.OUT_TYPE_3.equals(outType)) {
                    detail.setImportNo(pdStockRecord.getAllocationNo());
                }
                pdStockRecordDetailMapper.insert(detail);
            }
        }

        return pdStockRecord.getId();
    }

    private void saveOutStockRecord(PdStockRecord pdStockRecord, List<PdStockRecordDetail> pdStockRecordDetailList) {
        pdStockRecordMapper.insert(pdStockRecord);

        if (CollectionUtils.isNotEmpty(pdStockRecordDetailList)) {
            for (PdStockRecordDetail entity : pdStockRecordDetailList) {
                entity.setId(null);//初始化ID (从前端传过来会自带页面列表行的ID)
                entity.setRecordId(pdStockRecord.getId());//外键设置
                entity.setDelFlag(PdConstant.DEL_FLAG_0);
                pdStockRecordDetailMapper.insert(entity);
            }
        }
    }

    @Override
    public void updateStatus(PdStockRecord pdStockRecord) {
        PdStockRecord update = new PdStockRecord();
        update.setId(pdStockRecord.getId());
        update.setSubmitStatus(PdConstant.SUBMIT_STATE_3); //已撤销
        update.setAuditStatus("");
        pdStockRecordMapper.updateById(update);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delMain(String id) {
        pdStockRecordDetailMapper.deleteByMainId(id);
        pdStockRecordMapper.deleteById(id);
    }

    @Override
    public void delMainByDelFlag(String id) {
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();

        PdStockRecordDetail pdStockRecordDetail = new PdStockRecordDetail();
        pdStockRecordDetail.setRecordId(id);
        pdStockRecordDetail.setUpdateBy(sysUser.getRealname());
        pdStockRecordDetail.setUpdateTime(DateUtils.getDate());
        pdStockRecordDetailMapper.deleteByDelFlag(pdStockRecordDetail);

        PdStockRecord pdStockRecord = new PdStockRecord();
        pdStockRecord.setId(id);
        pdStockRecord.setUpdateBy(sysUser.getRealname());
        pdStockRecord.setUpdateTime(DateUtils.getDate());
        pdStockRecordMapper.deleteByDelFlag(pdStockRecord);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delBatchMain(Collection<? extends Serializable> idList) {
//		for(Serializable id:idList) {
//			pdStockRecordDetailMapper.deleteByMainId(id.toString());
//			pdStockRecordMapper.delete(id);
//		}
    }

    @Override
    public List<PdStockRecord> queryList(PdStockRecord pdStockRecord,String recodeType) {
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        pdStockRecord.setDepartParentId(sysUser.getDepartParentId());
//        pdStockRecord.setDepartId(sysUser.getCurrentDepartId());
        if(PdConstant.RECODE_TYPE_1.equals(recodeType)){
            pdStockRecord.setInDepartId(sysUser.getCurrentDepartId());
        }else if(PdConstant.RECODE_TYPE_2.equals(recodeType)){
            pdStockRecord.setOutDepartId(sysUser.getCurrentDepartId());
        }
        return pdStockRecordMapper.selectList(pdStockRecord);
    }

    @Override
    public Page<PdStockRecord> queryList(Page<PdStockRecord> pageList, PdStockRecord pdStockRecord,String recodeType) {
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        pdStockRecord.setDepartParentId(sysUser.getDepartParentId());
//        pdStockRecord.setDepartId(sysUser.getCurrentDepartId());
        if(PdConstant.RECODE_TYPE_1.equals(recodeType)){
            pdStockRecord.setInDepartId(sysUser.getCurrentDepartId());
        }else if(PdConstant.RECODE_TYPE_2.equals(recodeType)){
            pdStockRecord.setOutDepartId(sysUser.getCurrentDepartId());
        }
        return pageList.setRecords(pdStockRecordMapper.selectList(pdStockRecord));
    }

    @Override
    public PdStockRecord getOne(PdStockRecord pdStockRecord) {
        return pdStockRecordMapper.getOne(pdStockRecord);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map<String, String> audit(PdStockRecord auditEntity, PdStockRecord pdStockRecord, String recordType) {
        Map<String, String> result = new HashMap<>();
        if (PdConstant.RECODE_TYPE_1.equals(recordType)) {
            result = this.auditIn(auditEntity, pdStockRecord);
        } else if (PdConstant.RECODE_TYPE_2.equals(recordType)) {
            result = this.auditOut(auditEntity, pdStockRecord);
        }
        return result;
    }

    /**
     * 入库审核
     *
     * @param auditEntity
     * @param pdStockRecord
     * @return
     */
    private Map<String, String> auditIn(PdStockRecord auditEntity, PdStockRecord pdStockRecord) {
        Map<String, String> result = new HashMap<>();
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();

        if (PdConstant.AUDIT_STATE_2.equals(auditEntity.getAuditStatus())) {
            //审核通过
            String inType = pdStockRecord.getInType();

            if (PdConstant.IN_TYPE_1.equals(inType)) {  //正常入库
                // TODO 紧急产品处理逻辑
            }
//            else if (PdConstant.IN_TYPE_2.equals(inType)) {  //退货入库
//            } else if (PdConstant.IN_TYPE_3.equals(inType)) {  //调拨入库
//            }

            PdStockRecordDetail pdStockRecordDetail = new PdStockRecordDetail();
            pdStockRecordDetail.setRecordId(pdStockRecord.getId());
            List<PdStockRecordDetail> pdStockRecordDetailList = pdStockRecordDetailMapper.selectByMainId(pdStockRecordDetail);
            pdStockRecord.setPdStockRecordDetailList(pdStockRecordDetailList);

            // 更新采购单到货数量
            if (CollectionUtils.isNotEmpty(pdStockRecordDetailList)) {
                PdPurchaseOrderMergeDetail pdPurchaseDetail = null;
                for (PdStockRecordDetail entity : pdStockRecordDetailList) {
                    if (oConvertUtils.isNotEmpty(entity.getMergeOrderNo())) {
                        pdPurchaseDetail = new PdPurchaseOrderMergeDetail();
                        pdPurchaseDetail.setMergeOrderNo(entity.getMergeOrderNo());
                        pdPurchaseDetail.setProductId(entity.getProductId());
                        pdPurchaseDetail.setArrivalNum(entity.getProductNum());
                        pdPurchaseOrderMergeDetailMapper.additionArrivalNum(pdPurchaseDetail);
                    }
                }
            }

            //处理库存
            String inStr = pdProductStockTotalService.updateInStock(pdStockRecord);

            if (PdConstant.TRUE.equals(inStr)) {
                //保存出入库记录日志
                this.saveStockLog(pdStockRecord, inType, "");
                result.put("code", PdConstant.SUCCESS_200);
                result.put("message", "审核成功！");
            } else {
                result.put("code", PdConstant.FAIL_500);
                result.put("message", inStr);
                throw new RuntimeException(inStr);
            }
        } else if (PdConstant.AUDIT_STATE_3.equals(auditEntity.getAuditStatus())) {
            //驳回
            auditEntity.setSubmitStatus(PdConstant.SUBMIT_STATE_1); // 待提交
            result.put("code", PdConstant.SUCCESS_200);
            result.put("message", "驳回成功！");
        }

        // 变更审批意见 以及 审批状态
        auditEntity.setAuditDate(DateUtils.getDate());
        auditEntity.setAuditBy(sysUser.getId());
        pdStockRecordMapper.updateById(auditEntity);

        return result;
    }

    /**
     * 出库审核
     *
     * @param auditEntity
     * @param pdStockRecord
     * @return
     */
    private Map<String, String> auditOut(PdStockRecord auditEntity, PdStockRecord pdStockRecord) {
        Map<String, String> result = new HashMap<>();
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();

        if (PdConstant.AUDIT_STATE_2.equals(auditEntity.getAuditStatus())) {      //审核通过

            String outType = pdStockRecord.getOutType(); //出库类型

            PdStockRecordDetail pdStockRecordDetail = new PdStockRecordDetail();
            pdStockRecordDetail.setRecordId(pdStockRecord.getId());
            List<PdStockRecordDetail> pdStockRecordDetailList = pdStockRecordDetailMapper.selectByMainId(pdStockRecordDetail);
            pdStockRecord.setPdStockRecordDetailList(pdStockRecordDetailList);

            // 0.更新申领单或调拨单 发货数量
            if (CollectionUtils.isNotEmpty(pdStockRecordDetailList)) {
                Double arrivalApplyCount = 0D;
                Double arrivalAllocationCount = 0D;
                for (PdStockRecordDetail entity : pdStockRecordDetailList) {
                    //更新申领单发货数量
                    if (oConvertUtils.isNotEmpty(pdStockRecord.getApplyNo())) {
                        PdApplyDetail pdApplyDetail = new PdApplyDetail();
                        pdApplyDetail.setProductId(entity.getProductId());
                        pdApplyDetail.setApplyNo(pdStockRecord.getApplyNo());
                        pdApplyDetail.setArrivalNum(entity.getProductNum());
                        pdApplyDetailMapper.additionArrivalNum(pdApplyDetail);
                        arrivalApplyCount = arrivalApplyCount + entity.getProductNum();
                    }

                    //更新调拨单发货数量
                    if (oConvertUtils.isNotEmpty(pdStockRecord.getAllocationNo())) {
                        PdAllocationDetail pdAllocationDetail = new PdAllocationDetail();
                        pdAllocationDetail.setProductId(entity.getProductId());
                        pdAllocationDetail.setAllocationNo(pdStockRecord.getAllocationNo());
                        pdAllocationDetail.setArrivalNum(entity.getProductNum());
                        pdAllocationDetailMapper.additionArrivalNum(pdAllocationDetail);
                        arrivalAllocationCount = arrivalAllocationCount + entity.getProductNum();
                    }
                }

                //更新申领单发货总数量
                if (oConvertUtils.isNotEmpty(pdStockRecord.getApplyNo())) {
                    PdApplyOrder pdApplyOrder = new PdApplyOrder();
                    pdApplyOrder.setApplyNo(pdStockRecord.getApplyNo());
                    pdApplyOrder.setArrivalCount(arrivalApplyCount);
                    pdApplyOrderMapper.additionArrivalCount(pdApplyOrder);
                }

                //更新调拨单发货总数量
                if (oConvertUtils.isNotEmpty(pdStockRecord.getAllocationNo())) {
                    PdAllocationRecord pdAllocationRecord = new PdAllocationRecord();
                    pdAllocationRecord.setAllocationNo(pdStockRecord.getAllocationNo());
                    pdAllocationRecord.setArrivalCount(arrivalAllocationCount);
                    pdAllocationRecordMapper.additionArrivalCount(pdAllocationRecord);
                }

            }

            //1.处理出库库存
            String inStr = pdProductStockTotalService.updateOutStock(pdStockRecord);
            //2.保存出库日志记录
            this.saveStockLog(pdStockRecord, "", outType);

            //3.更新申领单 调拨单出库状态  这步暂时没有

            //4.生成入库单
            String recordId = this.saveInStockRecord(pdStockRecord, pdStockRecordDetailList, outType);

            // 查询入库单
            PdStockRecord query = new PdStockRecord();
            query.setId(recordId);
            PdStockRecord inRecord = this.getOne(query);
            PdStockRecordDetail queryDetail = new PdStockRecordDetail();
            queryDetail.setRecordId(pdStockRecord.getId());
            List<PdStockRecordDetail> inRecordDetailList = pdStockRecordDetailMapper.selectByMainId(queryDetail);
            inRecord.setPdStockRecordDetailList(inRecordDetailList);

            //5.处理入库库存
            String inStr2 = pdProductStockTotalService.updateInStock(inRecord);
            //6.保存入库日志记录
            this.saveStockLog(inRecord, PdConstant.IN_TYPE_1, "");

            if (PdConstant.TRUE.equals(inStr) && PdConstant.TRUE.equals(inStr2)) {
                result.put("code", PdConstant.SUCCESS_200);
                result.put("message", "审核成功！");
            } else {
                result.put("code", PdConstant.FAIL_500);
                result.put("message", inStr);
                throw new RuntimeException(inStr);
            }
        } else if (PdConstant.AUDIT_STATE_3.equals(auditEntity.getAuditStatus())) {   //驳回

            auditEntity.setSubmitStatus(PdConstant.SUBMIT_STATE_1); // 待提交
            result.put("code", PdConstant.SUCCESS_200);
            result.put("message", "驳回成功！");
        }

        //6.变更审批意见 以及 审批状态
        auditEntity.setAuditDate(DateUtils.getDate());
        auditEntity.setAuditBy(sysUser.getId());
        pdStockRecordMapper.updateById(auditEntity);

        return result;
    }

    @Override
    public PdStockRecord initInModal(String id) {
        PdStockRecord pdStockRecord = new PdStockRecord();

        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();

        PdGoodsAllocation pdGoodsAllocation = new PdGoodsAllocation();
        pdGoodsAllocation.setDepartId(sysUser.getCurrentDepartId());
        pdGoodsAllocation.setAreaType(PdConstant.GOODS_ALLCATION_AREA_TYPE_2);
        List<PdGoodsAllocationPage> goodsAllocationList = pdGoodsAllocationService.getOptionsForSelect(pdGoodsAllocation);

        if (StringUtils.isNotEmpty(id)) { // 查看页面
            pdStockRecord = this.getById(id);

            //查入库单明细
            PdStockRecordDetail pdStockRecordDetail = new PdStockRecordDetail();
            pdStockRecordDetail.setRecordId(id);
            List<PdStockRecordDetail> pdStockRecordDetailList = pdStockRecordDetailService.selectByMainId(pdStockRecordDetail);
            BigDecimal totalPrice = new BigDecimal(0);//总金额	@TableField(exist = false)
            Double totalSum = new Double(0);//总数量
            for (PdStockRecordDetail item : pdStockRecordDetailList) {
                totalSum = totalSum + item.getProductNum();
                BigDecimal purchasePrice = item.getPurchasePrice() == null ? new BigDecimal(0) : item.getPurchasePrice();
                totalPrice = totalPrice.add(purchasePrice.multiply(BigDecimal.valueOf(item.getProductNum())).setScale(4, BigDecimal.ROUND_HALF_UP));
            }
            pdStockRecord.setTotalSum(totalSum);
            pdStockRecord.setInTotalPrice(totalPrice);
            pdStockRecord.setPdStockRecordDetailList(pdStockRecordDetailList);

            if (StringUtils.isNotEmpty(pdStockRecord.getMergeOrderNo())) {
                //查订单列表
                PdPurchaseOrderMergeDetail orderMergeDetail = new PdPurchaseOrderMergeDetail();
                orderMergeDetail.setMergeOrderNo(pdStockRecord.getMergeOrderNo());
                List<PdPurchaseOrderMergeDetail> pdPurchaseDetailList = pdPurchaseOrderMergeDetailService.queryPdPurchaseMergeDetail(orderMergeDetail);
                pdStockRecord.setPdPurchaseOrderMergeDetail(pdPurchaseDetailList);
            }
        } else {  // 新增页面
            //开关-是否允许入库量大于订单量   1-允许入库量大于订单量；0-不允许入库量大于订单量
            List<DictModel> allowInMoreOrder = sysDictService.queryDictItemsByCode(PdConstant.ON_OFF_ALLOW_IN_MORE_ORDER);
            //开关-是否允许入库非订单产品     1-允许非订单产品；0-不允许非订单产品
            List<DictModel> allowNotOrderProduct = sysDictService.queryDictItemsByCode(PdConstant.ON_OFF_ALLOW_NOT_ORDER_PRODUCT);
            if (CollectionUtils.isNotEmpty(allowInMoreOrder)) {
                pdStockRecord.setAllowInMoreOrder(allowInMoreOrder.get(0).getValue());
            }
            if (CollectionUtils.isNotEmpty(allowNotOrderProduct)) {
                pdStockRecord.setAllowNotOrderProduct(allowNotOrderProduct.get(0).getValue());
            }

            //部门id
            pdStockRecord.setInDepartId(sysUser.getCurrentDepartId());
            //获取入库单号
            pdStockRecord.setRecordNo(UUIDUtil.generateOrderNoByType(PdConstant.ORDER_NO_FIRST_LETTER_RK));
            //获取当前日期
            pdStockRecord.setSubmitDateStr(DateUtils.formatDate());
            pdStockRecord.setSubmitDate(DateUtils.getDate());
            //登录人姓名
            pdStockRecord.setSubmitBy(sysUser.getId());
            pdStockRecord.setSubmitByName(sysUser.getRealname());
            //默认入库类型
            pdStockRecord.setInType(PdConstant.IN_TYPE_1);

        }

        //库区库位下拉框
        pdStockRecord.setGoodsAllocationList(goodsAllocationList);

        return pdStockRecord;
    }

    @Override
    public PdStockRecord initOutModal(String id) {
        PdStockRecord pdStockRecord = new PdStockRecord();

        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        SysDepart sysDepart = pdDepartService.getById(sysUser.getCurrentDepartId());

        //部门列表
        SysDepart query = new SysDepart();
        query.setDepartParentId(sysUser.getDepartParentId());
        query.setDepartId(sysUser.getCurrentDepartId());

        if (oConvertUtils.isNotEmpty(id)) { // 查看页面
            pdStockRecord = this.getById(id);

            if(oConvertUtils.isEmpty(pdStockRecord.getAuditBy())){
                pdStockRecord.setAuditByName(sysUser.getRealname());
            }

            //查入库单明细
            PdStockRecordDetail pdStockRecordDetail = new PdStockRecordDetail();
            pdStockRecordDetail.setRecordId(id);
            List<PdStockRecordDetail> pdStockRecordDetailList = pdStockRecordDetailService.selectByMainId(pdStockRecordDetail);
            BigDecimal inTotalPrice = new BigDecimal(0);//总金额
            BigDecimal outTotalPrice = new BigDecimal(0);//总金额
            Double totalSum = new Double(0);//总数量
            for (PdStockRecordDetail item : pdStockRecordDetailList) {
                totalSum = totalSum + item.getProductNum();
                BigDecimal sellingPrice = item.getSellingPrice() == null ? new BigDecimal(0) : item.getSellingPrice();
                BigDecimal purchasePrice = item.getPurchasePrice() == null ? new BigDecimal(0) : item.getPurchasePrice();
                outTotalPrice = outTotalPrice.add(sellingPrice.multiply(BigDecimal.valueOf(item.getProductNum())).setScale(4, BigDecimal.ROUND_HALF_UP));
                inTotalPrice = inTotalPrice.add(purchasePrice.multiply(BigDecimal.valueOf(item.getProductNum())).setScale(4, BigDecimal.ROUND_HALF_UP));
            }
            pdStockRecord.setTotalSum(totalSum);
            pdStockRecord.setInTotalPrice(inTotalPrice);
            pdStockRecord.setOutTotalPrice(outTotalPrice);
            pdStockRecord.setPdStockRecordDetailList(pdStockRecordDetailList);

            //查申领单列表
            if (oConvertUtils.isNotEmpty(pdStockRecord.getApplyNo())) {
                List<PdApplyDetail> pdApplyDetailList = pdApplyDetailService.selectByApplyNo(pdStockRecord.getApplyNo());
                pdStockRecord.setPdApplyDetailList(pdApplyDetailList);
            }
            //查调拨单列表
            if (oConvertUtils.isNotEmpty(pdStockRecord.getAllocationNo())) {
                List<PdAllocationDetail> pdAllocationDetailList = pdAllocationDetailService.selectByAllocationNo(pdStockRecord.getAllocationNo());
                pdStockRecord.setPdAllocationDetailList(pdAllocationDetailList);
            }

            //库区库位下拉框
            PdGoodsAllocation pdGoodsAllocation = new PdGoodsAllocation();
            pdGoodsAllocation.setDepartId(pdStockRecord.getInDepartId());
            pdGoodsAllocation.setAreaType(PdConstant.GOODS_ALLCATION_AREA_TYPE_2);
            List<PdGoodsAllocationPage> goodsAllocationList = pdGoodsAllocationService.getOptionsForSelect(pdGoodsAllocation);
            pdStockRecord.setGoodsAllocationList(goodsAllocationList);
        } else {  // 新增页面

            pdStockRecord.setOutDepartId(sysDepart.getId());
            pdStockRecord.setOutDepartName(sysDepart.getDepartName());
            //获取出库单号
            pdStockRecord.setRecordNo(UUIDUtil.generateOrderNoByType(PdConstant.ORDER_NO_FIRST_LETTER_CK));
            //获取当前日期
            pdStockRecord.setSubmitDateStr(DateUtils.formatDate());
            pdStockRecord.setSubmitDate(DateUtils.getDate());
            //登录人姓名
            pdStockRecord.setSubmitBy(sysUser.getId());
            pdStockRecord.setSubmitByName(sysUser.getRealname());
        }

        return pdStockRecord;
    }

    /**
     * 保存出入库记录日志
     *
     * @param pdStockRecord
     */
    private void saveStockLog(PdStockRecord pdStockRecord, String inType, String outType) {
        //日志
        List<PdStockRecordDetail> detail = pdStockRecord.getPdStockRecordDetailList();
        List<PdStockLog> logList = new ArrayList<PdStockLog>();
        PdStockLog stockLog;
        for (PdStockRecordDetail psd : detail) {
            stockLog = new PdStockLog();

            stockLog.setInvoiceNo(pdStockRecord.getRecordNo());
            stockLog.setProductId(psd.getProductId());
            stockLog.setProductBarCode(psd.getProductBarCode());
            stockLog.setBatchNo(psd.getBatchNo());
            stockLog.setProductNum(psd.getProductNum());
            if (StringUtils.isNotEmpty(pdStockRecord.getSupplierId())) {
                stockLog.setInFrom(pdStockRecord.getSupplierName());
            } else {
                stockLog.setInFrom(pdStockRecord.getOutDepartName());
            }
            stockLog.setOutTo(pdStockRecord.getInDepartName());
            if (oConvertUtils.isNotEmpty(inType)) { // 入库
                if (PdConstant.IN_TYPE_2.equals(inType)) {
                    stockLog.setLogType(PdConstant.STOCK_LOG_TYPE_5);
                } else {
                    stockLog.setLogType(PdConstant.STOCK_LOG_TYPE_1);
                }
            } else if (oConvertUtils.isNotEmpty(outType)) { //出库
                stockLog.setLogType(PdConstant.STOCK_LOG_TYPE_2);
            }
            stockLog.setRecordTime(DateUtils.getDate());
            logList.add(stockLog);
        }
        pdStockLogService.saveBatch(logList);
    }

    @Override
    public Page<PdStockRecord> selectTransferList(Page<PdStockRecord> pageList, PdStockRecord pdStockRecord) {
        return pageList.setRecords(pdStockRecordMapper.selectTransferList(pdStockRecord));
    }
}
