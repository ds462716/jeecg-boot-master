package org.jeecg.modules.pd.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.constant.PdConstant;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.util.DateUtils;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.message.util.PushMsgUtil;
import org.jeecg.modules.pd.entity.*;
import org.jeecg.modules.pd.mapper.*;
import org.jeecg.modules.pd.service.*;
import org.jeecg.modules.pd.util.UUIDUtil;
import org.jeecg.modules.pd.vo.PdGoodsAllocationPage;
import org.jeecg.modules.pd.vo.RpInAndOutReportPage;
import org.jeecg.modules.system.entity.SysDepart;
import org.jeecg.modules.system.entity.SysPermission;
import org.jeecg.modules.system.service.ISysDepartService;
import org.jeecg.modules.system.service.ISysPermissionService;
import org.springframework.beans.BeanUtils;
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
    private PdPackageRecordMapper pdPackageRecordMapper;
    @Autowired
    private IPdProductStockUniqueCodeService pdProductStockUniqueCodeService;
    @Autowired
    private IPdProductStockTotalService pdProductStockTotalService;
    @Autowired
    private IPdProductStockService pdProductStockService;
    @Autowired
    private IPdStockLogService pdStockLogService;
    @Autowired
    private IPdStockRecordDetailService pdStockRecordDetailService;
    @Autowired
    private IPdGoodsAllocationService pdGoodsAllocationService;
    @Autowired
    private IPdPurchaseOrderMergeDetailService pdPurchaseOrderMergeDetailService;
    @Autowired
    private IPdDepartService pdDepartService;
    @Autowired
    private IPdApplyDetailService pdApplyDetailService;
    @Autowired
    private IPdAllocationDetailService pdAllocationDetailService;
    @Autowired
    private IPdOnOffService pdOnOffService;
    @Autowired
    private ISysPermissionService sysPermissionService;
    @Autowired
    private PushMsgUtil pushMsgUtil;
    @Autowired
    private IPdProductService pdProductService;
    @Autowired
    private IPdPackageRecordService pdPackageRecordService;
    @Autowired
    private IPdPackageRecordDetailService pdPackageRecordDetailService;
    @Autowired
    private ISysDepartService sysDepartService;
    @Autowired
    private IPdSupplierService pdSupplierService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String saveMain(PdStockRecord pdStockRecord, List<PdStockRecordDetail> pdStockRecordDetailList, String recordType) {
        String recordId = "";
        // 修改前先删除数据
        if (oConvertUtils.isNotEmpty(pdStockRecord.getId())) {
            this.delMain(pdStockRecord.getId());
        }
        pdStockRecord.setCreateTime(DateUtils.getDate());
        if (PdConstant.RECODE_TYPE_1.equals(recordType)) {
            pdStockRecord.setRecordType(PdConstant.RECODE_TYPE_1); // 入库
            pdStockRecord.setSubmitStatus(PdConstant.SUBMIT_STATE_1); // 待提交
//            pdStockRecord.setAuditStatus(PdConstant.AUDIT_STATE_1);   // 待审核
            recordId = this.saveInStockRecord(pdStockRecord, pdStockRecordDetailList, "");
        } else if (PdConstant.RECODE_TYPE_2.equals(recordType)) {
            pdStockRecord.setRecordType(PdConstant.RECODE_TYPE_2); // 出库
            pdStockRecord.setSubmitStatus(PdConstant.SUBMIT_STATE_1); // 待提交
//            pdStockRecord.setAuditStatus(PdConstant.AUDIT_STATE_1);   // 待审核
            recordId = this.saveOutStockRecord(pdStockRecord, pdStockRecordDetailList);
        } else if (PdConstant.RECODE_TYPE_3.equals(recordType)){
            pdStockRecord.setRecordType(PdConstant.RECODE_TYPE_3); // 初始化库存
            pdStockRecord.setSubmitStatus(PdConstant.SUBMIT_STATE_1); // 待提交
            recordId = this.saveInitStockRecord(pdStockRecord, pdStockRecordDetailList, "");
        }
        return recordId;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String submit(PdStockRecord pdStockRecord, List<PdStockRecordDetail> pdStockRecordDetailList, String recordType) {
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        String recordId = "";
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
            recordId = this.saveInStockRecord(pdStockRecord, pdStockRecordDetailList, "");

            PdOnOff query = new PdOnOff();
            query.setDepartParentId(sysUser.getDepartParentId());
            //开关-是否需要入库审批   1-是；0-否
            query.setCode(PdConstant.ON_OFF_STOCK_IN_AUDIT);
            PdOnOff stockInAudit = pdOnOffService.getOne(query);
            if (stockInAudit != null && stockInAudit.getValue() == PdConstant.ON_OFF_STOCK_IN_AUDIT_0) {
                // 自动审批
                PdStockRecord auditEntity = new PdStockRecord();
                BeanUtils.copyProperties(pdStockRecord,auditEntity);
                auditEntity.setAuditStatus(PdConstant.AUDIT_STATE_2);
                auditEntity.setRefuseReason("系统自动审批通过");
                this.auditIn(auditEntity,pdStockRecord);
            }else{
                // 推送消息
                this.sendMsg(pdStockRecord,PdConstant.AUDIT_MENU_1,PdConstant.STOCK_RECORD_IN_SUBMIT_MSG);
            }
        } else if (PdConstant.RECODE_TYPE_2.equals(recordType)) {
            pdStockRecord.setRecordType(PdConstant.RECODE_TYPE_2); // 出库
            pdStockRecord.setSubmitStatus(PdConstant.SUBMIT_STATE_2); // 已提交
            pdStockRecord.setAuditStatus(PdConstant.AUDIT_STATE_1);   // 待审核
            recordId = this.saveOutStockRecord(pdStockRecord, pdStockRecordDetailList);

            PdOnOff query = new PdOnOff();
            query.setDepartParentId(sysUser.getDepartParentId());
            //开关-是否需要出库审批   1-是；0-否
            query.setCode(PdConstant.ON_OFF_STOCK_OUT_AUDIT);
            PdOnOff stockOutAudit = pdOnOffService.getOne(query);
            if (stockOutAudit != null && stockOutAudit.getValue() == PdConstant.ON_OFF_STOCK_OUT_AUDIT_0) {
                // 自动审批
                PdStockRecord auditEntity = new PdStockRecord();
                BeanUtils.copyProperties(pdStockRecord,auditEntity);
                auditEntity.setAuditStatus(PdConstant.AUDIT_STATE_2);
                auditEntity.setRefuseReason("系统自动审批通过");
                this.auditOut(auditEntity,pdStockRecord);
            }else{
                // 推送消息
                this.sendMsg(pdStockRecord,PdConstant.AUDIT_MENU_2,PdConstant.STOCK_RECORD_OUT_SUBMIT_MSG);
            }
        } else if (PdConstant.RECODE_TYPE_3.equals(recordType)) {
            pdStockRecord.setRecordType(PdConstant.RECODE_TYPE_3); // 初始化库存
            pdStockRecord.setSubmitStatus(PdConstant.SUBMIT_STATE_2); // 已提交
            pdStockRecord.setAuditStatus(PdConstant.AUDIT_STATE_1);   // 待审核
            recordId = this.saveInitStockRecord(pdStockRecord, pdStockRecordDetailList, "");
            // 推送消息
//            this.sendMsg(pdStockRecord,PdConstant.AUDIT_MENU_1,PdConstant.STOCK_RECORD_IN_SUBMIT_MSG);
        }

        return recordId;
    }

    /**
     * 入库
     * @param pdStockRecord
     * @param pdStockRecordDetailList
     * @param outType
     * @return
     */
    private String saveInStockRecord(PdStockRecord pdStockRecord, List<PdStockRecordDetail> pdStockRecordDetailList, String outType) {

        List<PdStockRecordDetail> newDetailList = new ArrayList<>();

        if (oConvertUtils.isNotEmpty(outType)) {
            // 调拨出库 或 申领出库 自动生成入库单 （出库入库）
            String outRecordNo = pdStockRecord.getRecordNo(); //出库单号
            if (PdConstant.OUT_TYPE_3.equals(outType)) {
                pdStockRecord.setInType(PdConstant.IN_TYPE_3);
            } else if(PdConstant.OUT_TYPE_4.equals(outType)){
                pdStockRecord.setInType(PdConstant.IN_TYPE_2);
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
            pdStockRecord.setExtend1(outRecordNo); // 本字段用于存 出库入库的 出库单号， 用于退货出库时 按出库单号查询库存

            if(PdConstant.CODE_PRINT_TYPE_1.equals(pdStockRecord.getBarCodeType())){
                // 唯一码出库入库，需要合并入库明细
                Set<String> setIds = new HashSet<>();
                for (PdStockRecordDetail main : pdStockRecordDetailList) {
                    Double productNum = 0D;
                    StringBuilder setId = new StringBuilder();
                    setId.append(main.getProductStockId()).append(main.getInHuoweiCode());
                    String mainHuoweCode = main.getInHuoweiCode() == null ? "" : main.getInHuoweiCode();
                    if(setIds.add(setId.toString())){
                        List<String> refBarCodes = new ArrayList<>();
                        for (PdStockRecordDetail entity : pdStockRecordDetailList) {
                            String entityHuoweCode = entity.getInHuoweiCode() == null ? "" : entity.getInHuoweiCode();
                            if(main.getProductStockId().equals(entity.getProductStockId()) && mainHuoweCode.equals(entityHuoweCode)){
                                refBarCodes.add(entity.getRefBarCode());
                                productNum = productNum + entity.getProductNum();
                            }
                        }
                        main.setProductNum(productNum);
                        main.setRefBarCode(String.join(",", refBarCodes));// 合并后记录唯一码（用于入库后更新条码表）
                        main.setExtend1(outRecordNo); // 本字段用于存 出库入库的 出库单号， 用于退货出库时 按出库单号查询库存
                        newDetailList.add(main);
                    }
                }
            }else{
                // 非唯一码出库入库，不需要合并入库明细
                for (PdStockRecordDetail main : pdStockRecordDetailList) {
                    main.setExtend1(outRecordNo); // 本字段用于存 出库入库的 出库单号， 用于退货出库时 按出库单号查询库存
                }
                newDetailList = pdStockRecordDetailList;
            }

        }else{
            // 供应商入库
            if (CollectionUtils.isNotEmpty(pdStockRecordDetailList)) {
                Set<String> setIds = new HashSet<>();
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
                        String batchNo = main.getBatchNo()==null?"":main.getBatchNo().trim();
                        main.setProductBarCode("01" + main.getProductNumber().trim() + "17" + expDate + "10" + batchNo);
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
                        main.setDistributorId(pdStockRecord.getDistributorId());
                        main.setProductNum(productNum);
                        newDetailList.add(main);
                    }
                }
            }
        }

        pdStockRecordMapper.insert(pdStockRecord);

        if(CollectionUtils.isNotEmpty(newDetailList)){
            //开关-是否允许出入库时可修改进价和出价
            PdOnOff query = new PdOnOff();
            LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
            query.setDepartParentId(sysUser.getDepartParentId());
            query.setCode(PdConstant.ON_OFF_ALLOW_EDIT_PRICE);
            PdOnOff pdOnOff = pdOnOffService.getOne(query);
            Integer allowEditPrice = null;
            if (pdOnOff != null && pdOnOff.getValue() != null) {
                allowEditPrice = pdOnOff.getValue();
            }
            for (PdStockRecordDetail detail : newDetailList) {

//                PdProductStock stock = pdProductStockService.getById(detail.getProductStockId());

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
//                if (oConvertUtils.isEmpty(outType)) {
//                    // 从供货商入库 则生成REF码
//                    detail.setRefBarCode(SnowUtils.bigKey());
//                }
                pdStockRecordDetailMapper.insert(detail);
                // 修改产品进价
                if(allowEditPrice == PdConstant.ON_OFF_ALLOW_EDIT_PRICE_1){
                    PdProduct pdProduct = new PdProduct();
                    pdProduct.setId(detail.getProductId());
                    pdProduct.setPurchasePrice(detail.getPurchasePrice());
                    pdProductService.updateProduct(pdProduct);
                }
            }
        }

        return pdStockRecord.getId();
    }

    /**
     * 出库
     * @param pdStockRecord
     * @param pdStockRecordDetailList
     * @return
     */
    private String saveOutStockRecord(PdStockRecord pdStockRecord, List<PdStockRecordDetail> pdStockRecordDetailList) {
        String outType = pdStockRecord.getOutType();
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        SysDepart sysDepart = pdDepartService.getById(sysUser.getCurrentDepartId());
        String departId = null;
        if(PdConstant.DEPART_TYPE_1.equals(sysDepart.getDepartType()) && PdConstant.OUT_TYPE_4.equals(outType)){
            //一级库房 && 退货出库  所属库房id设置为出库库房id
            departId = pdStockRecord.getOutDepartId();
        }
        pdStockRecord.setDepartId(departId);
        pdStockRecordMapper.insert(pdStockRecord);
        if (CollectionUtils.isNotEmpty(pdStockRecordDetailList)) {
            //关-是否允许出入库时可修改进价和出价
            PdOnOff query = new PdOnOff();
            query.setDepartParentId(sysUser.getDepartParentId());
            query.setCode(PdConstant.ON_OFF_ALLOW_EDIT_PRICE);
            PdOnOff pdOnOff = pdOnOffService.getOne(query);
            Integer allowEditPrice = null;
            if (pdOnOff != null && pdOnOff.getValue() != null) {
                allowEditPrice = pdOnOff.getValue();
            }

            for (PdStockRecordDetail entity : pdStockRecordDetailList) {
                entity.setId(null);//初始化ID (从前端传过来会自带页面列表行的ID)
                entity.setRecordId(pdStockRecord.getId());//外键设置
                entity.setDelFlag(PdConstant.DEL_FLAG_0);
                entity.setDepartId(departId);
                pdStockRecordDetailMapper.insert(entity);

                // 修改产品进价
                if(allowEditPrice == PdConstant.ON_OFF_ALLOW_EDIT_PRICE_1){
                    PdProduct pdProduct = new PdProduct();
                    pdProduct.setId(entity.getProductId());
                    pdProduct.setSellingPrice(entity.getSellingPrice());
                    pdProductService.updateProduct(pdProduct);
                }
            }
        }
        return pdStockRecord.getId();
    }

    /**
     * 初始化库存
     * @param pdStockRecord
     * @param pdStockRecordDetailList
     * @param outType
     * @return
     */
    private String saveInitStockRecord(PdStockRecord pdStockRecord, List<PdStockRecordDetail> pdStockRecordDetailList, String outType) {

        List<PdStockRecordDetail> newDetailList = new ArrayList<>();

        if (CollectionUtils.isNotEmpty(pdStockRecordDetailList)) {
            Set<String> setIds = new HashSet<>();
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
                    String batchNo = main.getBatchNo()==null?"":main.getBatchNo().trim();
                    main.setProductBarCode("01" + main.getProductNumber().trim() + "17" + expDate + "10" + batchNo);
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

        pdStockRecordMapper.insert(pdStockRecord);

        if(CollectionUtils.isNotEmpty(newDetailList)){
            for (PdStockRecordDetail detail : newDetailList) {
                detail.setId(null);//初始化ID (从前端传过来会自带页面列表行的ID)
                detail.setRecordId(pdStockRecord.getId());//外键设置
                detail.setDelFlag(PdConstant.DEL_FLAG_0);
                detail.setProductStockId(null); //清空库存ID
                detail.setCreateTime(DateUtils.getDate());
                pdStockRecordDetailMapper.insert(detail);
            }
        }

        return pdStockRecord.getId();
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
//            pdStockRecord.setOutDepartId(sysUser.getCurrentDepartId());
            if(oConvertUtils.isEmpty(pdStockRecord.getOnlyReturn())){
                // 查非退货出库列表
                pdStockRecord.setOutDepartId(sysUser.getCurrentDepartId());
            }
        }else if(PdConstant.RECODE_TYPE_3.equals(recodeType)){
            pdStockRecord.setInDepartId(sysUser.getCurrentDepartId());
        }
        return pdStockRecordMapper.selectList(pdStockRecord);
    }

    @Override
    public IPage<PdStockRecord> queryList(Page<PdStockRecord> pageList, PdStockRecord pdStockRecord, String recodeType) {
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        pdStockRecord.setDepartParentId(sysUser.getDepartParentId());
//        pdStockRecord.setDepartId(sysUser.getCurrentDepartId());
        if(PdConstant.RECODE_TYPE_1.equals(recodeType)){
            pdStockRecord.setInDepartId(sysUser.getCurrentDepartId());
        }else if(PdConstant.RECODE_TYPE_2.equals(recodeType)){
            if(oConvertUtils.isEmpty(pdStockRecord.getOnlyReturn())){
                // 查非退货出库列表
                pdStockRecord.setOutDepartId(sysUser.getCurrentDepartId());
            }else{
                // 查退货出库列表
                if(PdConstant.DEPART_TYPE_1.equals(sysUser.getDepartType())){
                    //一级科室查所有科室
                    if(oConvertUtils.isEmpty(pdStockRecord.getOutDepartId())){
                        pdStockRecord.setOutDepartId(null);
                    }
                }else{
                    //非一级科室 查询当前科室
                    pdStockRecord.setOutDepartId(sysUser.getCurrentDepartId());
                }
            }
        }else if(PdConstant.RECODE_TYPE_3.equals(recodeType)){
            pdStockRecord.setInDepartId(sysUser.getCurrentDepartId());
        }
        return pdStockRecordMapper.selectList(pageList,pdStockRecord);
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
        } else if (PdConstant.RECODE_TYPE_3.equals(recordType)) {
            result = this.auditInit(auditEntity, pdStockRecord);
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

            PdStockRecordDetail pdStockRecordDetail = new PdStockRecordDetail();
            pdStockRecordDetail.setRecordId(pdStockRecord.getId());
            List<PdStockRecordDetail> pdStockRecordDetailList = pdStockRecordDetailMapper.selectByMainId(pdStockRecordDetail);
            pdStockRecord.setPdStockRecordDetailList(pdStockRecordDetailList);

            if (PdConstant.IN_TYPE_1.equals(inType)) {  //正常入库
                if (CollectionUtils.isNotEmpty(pdStockRecordDetailList)) {
                    Set<String> setIds = new HashSet<>();

                    for (PdStockRecordDetail detail : pdStockRecordDetailList) {
                        Double sum = 0D;
                        PdProduct pdProduct = pdProductService.getById(detail.getProductId());
                        // 紧急产品处理：
                        if(PdConstant.IS_URGENT_0.equals(pdProduct.getIsUrgent())){

                            StringBuilder setId = new StringBuilder();
                            setId.append(detail.getProductId());

                            if(setIds.add(setId.toString())){
                                //紧急产品已采购数量
                                Double purchasedQuantity = pdProduct.getPurchasedQuantity() == null ? 0D : pdProduct.getPurchasedQuantity();
                                // 紧急产品需要采购数量
                                Double upQuantity = pdProduct.getUpQuantity() == null ? 0D : pdProduct.getUpQuantity();

                                for (PdStockRecordDetail detail2 : pdStockRecordDetailList) {
                                    if(detail.getProductId().equals(detail2.getProductId())){
                                        sum = sum + detail2.getProductNum();
                                    }
                                }
                                if(sum > upQuantity - purchasedQuantity){
                                    throw new RuntimeException("紧急产品["+detail.getProductName()+"]入库数量["+sum+"]，不能大于需采购数量["+(upQuantity - purchasedQuantity)+"]");
                                }else{
                                    // 更新已采购数量
                                    PdProduct update = new PdProduct();
                                    update.setId(detail.getProductId());
                                    update.setPurchasedQuantity(purchasedQuantity + sum);
                                    pdProductService.updateProduct(update);
                                }
                            }
                        }
                    }
                }

            }else if (PdConstant.IN_TYPE_2.equals(inType)) {  //退货入库

            }else if (PdConstant.IN_TYPE_3.equals(inType)) {  //调拨入库

            }

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
                this.saveStockLog(pdStockRecord);
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
//            pdStockRecord.setPdStockRecordDetailList(pdStockRecordDetailList);
            List<PdStockRecordDetail> pdStockRecordDetailListWithOutPackage = new ArrayList<>();

            Set<String> packageRecordIdSet = new HashSet<>();

            if (CollectionUtils.isNotEmpty(pdStockRecordDetailList)) {

                Double arrivalApplyCount = 0D;
                Double arrivalAllocationCount = 0D;
                boolean bool = true;
                List<String> message = new ArrayList<>();
                for (PdStockRecordDetail entity : pdStockRecordDetailList) {
                    //0.0校验唯一码是否已被出库
                    if (PdConstant.CODE_PRINT_TYPE_1.equals(pdStockRecord.getBarCodeType())) {
                        PdProductStockUniqueCode code = new PdProductStockUniqueCode();
                        code.setId(entity.getRefBarCode());
                        code.setProductStockId(entity.getProductStockId());
                        List<PdProductStockUniqueCode> codeList = pdProductStockUniqueCodeService.selectList(code);
                        if (codeList == null || codeList.size() <= 0) {
                            bool = false;
                            message.add(entity.getRefBarCode());
                        }
                    }

                    //0.1更新申领单发货数量
                    if (oConvertUtils.isNotEmpty(pdStockRecord.getApplyNo())) {
                        PdApplyDetail pdApplyDetail = new PdApplyDetail();
                        pdApplyDetail.setProductId(entity.getProductId());
                        pdApplyDetail.setApplyNo(pdStockRecord.getApplyNo());
                        pdApplyDetail.setArrivalNum(entity.getProductNum());
                        pdApplyDetailMapper.additionArrivalNum(pdApplyDetail);
                        arrivalApplyCount = arrivalApplyCount + entity.getProductNum();
                    }

                    //0.2更新调拨单发货数量
                    if (oConvertUtils.isNotEmpty(pdStockRecord.getAllocationNo())) {
                        PdAllocationDetail pdAllocationDetail = new PdAllocationDetail();
                        pdAllocationDetail.setProductId(entity.getProductId());
                        pdAllocationDetail.setAllocationNo(pdStockRecord.getAllocationNo());
                        pdAllocationDetail.setArrivalNum(entity.getProductNum());
                        pdAllocationDetailMapper.additionArrivalNum(pdAllocationDetail);
                        arrivalAllocationCount = arrivalAllocationCount + entity.getProductNum();
                    }

                    //0.3取定数包打包记录ID、分离定数包的出库明细，（定数包明细已扣库存 下面无需重复扣减）
                    if(oConvertUtils.isNotEmpty(entity.getPackageRecordId())){
                        packageRecordIdSet.add(entity.getPackageRecordId());
                    }else{
                        pdStockRecordDetailListWithOutPackage.add(entity);
                    }
                }
                if(!bool){
                    result.put("code", PdConstant.FAIL_500);
                    result.put("message", "唯一码[" + String.join(",", message) + "]已被出库，不能再次出库！");
                    return result;
                }
//                for (PdStockRecordDetail entity : pdStockRecordDetailList) {
//                }

                //0.4更新申领单发货总数量
                if (oConvertUtils.isNotEmpty(pdStockRecord.getApplyNo())) {
                    PdApplyOrder pdApplyOrder = new PdApplyOrder();
                    pdApplyOrder.setApplyNo(pdStockRecord.getApplyNo());
                    pdApplyOrder.setArrivalCount(arrivalApplyCount);
                    pdApplyOrderMapper.additionArrivalCount(pdApplyOrder);
                }

                //0.5更新调拨单发货总数量
                if (oConvertUtils.isNotEmpty(pdStockRecord.getAllocationNo())) {
                    PdAllocationRecord pdAllocationRecord = new PdAllocationRecord();
                    pdAllocationRecord.setAllocationNo(pdStockRecord.getAllocationNo());
                    pdAllocationRecord.setArrivalCount(arrivalAllocationCount);
                    pdAllocationRecordMapper.additionArrivalCount(pdAllocationRecord);
                }

            }

            //1.处理出库库存
            String inStr = PdConstant.TRUE;
            if(CollectionUtils.isNotEmpty(pdStockRecordDetailListWithOutPackage)){
                PdStockRecord outRecord = new PdStockRecord();
                BeanUtils.copyProperties(pdStockRecord, outRecord);
                outRecord.setPdStockRecordDetailList(pdStockRecordDetailListWithOutPackage);
                inStr = pdProductStockTotalService.updateOutStock(outRecord);
            }

            pdStockRecord.setPdStockRecordDetailList(pdStockRecordDetailList);
            //2.保存出库日志记录
            this.saveStockLog(pdStockRecord);

            //3.更新申领单 调拨单出库状态  这步暂时没有

            //4.生成入库单
            String recordId = this.saveInStockRecord(pdStockRecord, pdStockRecordDetailList, outType);

            // 查询入库单
            PdStockRecord query = new PdStockRecord();
            query.setId(recordId);
            PdStockRecord inRecord = this.getOne(query);
            PdStockRecordDetail queryDetail = new PdStockRecordDetail();
//            queryDetail.setRecordId(pdStockRecord.getId());
            queryDetail.setRecordId(recordId);
            List<PdStockRecordDetail> inRecordDetailList = pdStockRecordDetailMapper.selectByMainId(queryDetail);
            inRecord.setPdStockRecordDetailList(inRecordDetailList);

            //5.处理入库库存
            String inStr2 = pdProductStockTotalService.updateInStock(inRecord);

            //6.处理订书包记录（修改状态为已出库）
            if(packageRecordIdSet != null && packageRecordIdSet.size() > 0){
                List<String> packageRecordIdList = new ArrayList<>(packageRecordIdSet);
                for (String id : packageRecordIdList) {
                    PdPackageRecord pdPackageRecord = new PdPackageRecord();
                    pdPackageRecord.setId(id);
                    pdPackageRecord.setStatus(PdConstant.PACKAGE_RECORD_STATUS_0);//已出库
                    pdPackageRecordMapper.updateById(pdPackageRecord);
                }
            }

            //7.保存入库日志记录
            this.saveStockLog(inRecord);

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




    /**
     * 出库审核(一体机接口)
     *
     * @param auditEntity
     * @param pdStockRecord
     * @return
     */
    private Map<String, String> auditOutInterface(PdStockRecord auditEntity, PdStockRecord pdStockRecord) {
        Map<String, String> result = new HashMap<>();
            String outType = pdStockRecord.getOutType(); //出库类型
            PdStockRecordDetail pdStockRecordDetail = new PdStockRecordDetail();
            pdStockRecordDetail.setRecordId(pdStockRecord.getId());
            List<PdStockRecordDetail> pdStockRecordDetailList = pdStockRecordDetailMapper.selectByMainId(pdStockRecordDetail);
            pdStockRecord.setPdStockRecordDetailList(pdStockRecordDetailList);
            Set<String> packageRecordIdSet = new HashSet<>();
            if (CollectionUtils.isNotEmpty(pdStockRecordDetailList)) {
                //0.0校验唯一码是否已被出库
                boolean bool = true;
                List<String> message = new ArrayList<>();
                for (PdStockRecordDetail entity : pdStockRecordDetailList) {
                    if (PdConstant.CODE_PRINT_TYPE_1.equals(pdStockRecord.getBarCodeType())) {
                        PdProductStockUniqueCode code = new PdProductStockUniqueCode();
                        code.setId(entity.getRefBarCode());
                        code.setProductStockId(entity.getProductStockId());
                        List<PdProductStockUniqueCode> codeList = pdProductStockUniqueCodeService.selectList(code);
                        if (codeList == null || codeList.size() <= 0) {
                            bool = false;
                            message.add(entity.getRefBarCode());
                        }
                    }
                }
                if(!bool){
                    result.put("code", PdConstant.FAIL_500);
                    result.put("message", "唯一码[" + String.join(",", message) + "]已被出库，不能再次出库！");
                    return result;
                }
            }

            //1.处理出库库存
            String inStr = pdProductStockTotalService.updateOutStock(pdStockRecord);

            //2.保存出库日志记录
            this.saveStockLog(pdStockRecord);

            //3.更新申领单 调拨单出库状态  这步暂时没有

            //4.生成入库单
            String recordId = this.saveInStockRecordInterface(pdStockRecord, pdStockRecordDetailList, outType);

            // 查询入库单
            PdStockRecord query = new PdStockRecord();
            query.setId(recordId);
            PdStockRecord inRecord = this.getOne(query);
            PdStockRecordDetail queryDetail = new PdStockRecordDetail();
            queryDetail.setRecordId(recordId);
            List<PdStockRecordDetail> inRecordDetailList = pdStockRecordDetailMapper.selectByMainId(queryDetail);
            inRecord.setPdStockRecordDetailList(inRecordDetailList);

            //5.处理入库库存
            String inStr2 = pdProductStockTotalService.updateInStock(inRecord);

            //6.处理订书包记录（修改状态为已出库）
            if(packageRecordIdSet != null && packageRecordIdSet.size() > 0){
                List<String> packageRecordIdList = new ArrayList<>(packageRecordIdSet);
                for (String id : packageRecordIdList) {
                    PdPackageRecord pdPackageRecord = new PdPackageRecord();
                    pdPackageRecord.setId(id);
                    pdPackageRecord.setStatus(PdConstant.PACKAGE_RECORD_STATUS_0);//已出库
                    pdPackageRecordMapper.updateById(pdPackageRecord);
                }
            }

            //7.保存入库日志记录
            this.saveStockLog(inRecord);

            if (PdConstant.TRUE.equals(inStr) && PdConstant.TRUE.equals(inStr2)) {
                result.put("code", PdConstant.SUCCESS_200);
                result.put("message", "审核成功！");
            } else {
                result.put("code", PdConstant.FAIL_500);
                result.put("message", inStr);
                throw new RuntimeException(inStr);
            }


        //6.变更审批意见 以及 审批状态
        auditEntity.setAuditDate(DateUtils.getDate());
        //auditEntity.setAuditBy(sysUser.getId());
        pdStockRecordMapper.updateById(auditEntity);

        return result;
    }


    /**
     * 初始化库存审核
     * @param auditEntity
     * @param pdStockRecord
     * @return
     */
    private Map<String, String> auditInit(PdStockRecord auditEntity, PdStockRecord pdStockRecord) {
        Map<String, String> result = new HashMap<>();
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();

        if (PdConstant.AUDIT_STATE_2.equals(auditEntity.getAuditStatus())) {
            //审核通过
            String inType = pdStockRecord.getInType();

            PdStockRecordDetail pdStockRecordDetail = new PdStockRecordDetail();
            pdStockRecordDetail.setRecordId(pdStockRecord.getId());
            List<PdStockRecordDetail> pdStockRecordDetailList = pdStockRecordDetailMapper.selectByMainId(pdStockRecordDetail);
            pdStockRecord.setPdStockRecordDetailList(pdStockRecordDetailList);

            //处理库存
            String inStr = pdProductStockTotalService.updateInStock(pdStockRecord);

            if (PdConstant.TRUE.equals(inStr)) {
                //保存出入库记录日志
                this.saveStockLog(pdStockRecord);
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
//            List<PdStockRecordDetail> uniqueList = new ArrayList<>();
            BigDecimal totalPrice = new BigDecimal(0);//总金额	@TableField(exist = false)
            Double totalSum = new Double(0);//总数量
//            int i = 0;
            for (PdStockRecordDetail item : pdStockRecordDetailList) {
                totalSum = totalSum + item.getProductNum();
                BigDecimal purchasePrice = item.getPurchasePrice() == null ? new BigDecimal(0) : item.getPurchasePrice();
                totalPrice = totalPrice.add(purchasePrice.multiply(BigDecimal.valueOf(item.getProductNum())).setScale(4, BigDecimal.ROUND_HALF_UP));
//                if(PdConstant.CODE_PRINT_TYPE_1.equals(pdStockRecord.getBarCodeType())){
//                    //唯一码 拆分，用于唯一码列表显示
//                    List<String> refBarCodeList = Arrays.asList(item.getRefBarCode().split(","));
//                    for(String refBarCode : refBarCodeList){
//                        i += 1;
//                        PdStockRecordDetail unique = new PdStockRecordDetail();
//                        BeanUtils.copyProperties(item, unique);
//                        unique.setRefBarCode(refBarCode);
//                        unique.setProductNum(1D);
//                        unique.setId(i+"");
//                        uniqueList.add(unique);
//                    }
//                }
            }
            pdStockRecord.setTotalSum(totalSum);
            pdStockRecord.setInTotalPrice(totalPrice);
            pdStockRecord.setPdStockRecordDetailList(pdStockRecordDetailList);
//            pdStockRecord.setPdStockRecordDetailUniqueList(uniqueList);

            if (StringUtils.isNotEmpty(pdStockRecord.getMergeOrderNo())) {
                //查订单列表
                PdPurchaseOrderMergeDetail orderMergeDetail = new PdPurchaseOrderMergeDetail();
                orderMergeDetail.setMergeOrderNo(pdStockRecord.getMergeOrderNo());
                List<PdPurchaseOrderMergeDetail> pdPurchaseDetailList = pdPurchaseOrderMergeDetailService.queryPdPurchaseMergeDetail(orderMergeDetail);
                pdStockRecord.setPdPurchaseOrderMergeDetail(pdPurchaseDetailList);
            }
        } else {  // 新增页面

            SysDepart depart = sysDepartService.getById(sysUser.getCurrentDepartId());
            //部门id
            pdStockRecord.setInDepartId(sysUser.getCurrentDepartId());
            pdStockRecord.setInDepartName(depart.getDepartName());
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

            //查出库单明细
            PdStockRecordDetail pdStockRecordDetail = new PdStockRecordDetail();
            pdStockRecordDetail.setRecordId(id);
            List<PdStockRecordDetail> pdStockRecordDetailList = pdStockRecordDetailService.selectByMainId(pdStockRecordDetail);
            List<PdStockRecordDetail> pdStockRecordDetailListWithOutPackage = new ArrayList<>();
            Set<String> packageRecordIdSet = new HashSet<>();
            BigDecimal inTotalPrice = new BigDecimal(0);//总金额
            BigDecimal outTotalPrice = new BigDecimal(0);//总金额
            Double totalSum = new Double(0);//总数量
            for (PdStockRecordDetail item : pdStockRecordDetailList) {
                totalSum = totalSum + item.getProductNum();
                BigDecimal sellingPrice = item.getSellingPrice() == null ? new BigDecimal(0) : item.getSellingPrice();
                BigDecimal purchasePrice = item.getPurchasePrice() == null ? new BigDecimal(0) : item.getPurchasePrice();
                outTotalPrice = outTotalPrice.add(sellingPrice.multiply(BigDecimal.valueOf(item.getProductNum())).setScale(4, BigDecimal.ROUND_HALF_UP));
                inTotalPrice = inTotalPrice.add(purchasePrice.multiply(BigDecimal.valueOf(item.getProductNum())).setScale(4, BigDecimal.ROUND_HALF_UP));

                // 分离定数包的出库明细，页面上需分开展示
                if(oConvertUtils.isEmpty(item.getPackageRecordId())){
                    pdStockRecordDetailListWithOutPackage.add(item);
                }else{
                    packageRecordIdSet.add(item.getPackageRecordId());
                }
            }

            pdStockRecord.setTotalSum(totalSum);
            pdStockRecord.setInTotalPrice(inTotalPrice);
            pdStockRecord.setOutTotalPrice(outTotalPrice);
//            pdStockRecord.setPdStockRecordDetailList(pdStockRecordDetailList);
            pdStockRecord.setPdStockRecordDetailList(pdStockRecordDetailListWithOutPackage);
            //定数包 出库明细
            if(packageRecordIdSet.size() > 0){
                PdPackageRecord pdPackageRecord = new PdPackageRecord();
                pdPackageRecord.setDepartParentId(sysUser.getDepartParentId());
                pdPackageRecord.setIdList(new ArrayList<>(packageRecordIdSet));
                List<PdPackageRecord> pdPackageRecordList = pdPackageRecordService.queryList(pdPackageRecord);
                for (PdPackageRecord record : pdPackageRecordList) {
                    List<PdPackageRecordDetail> detailList = pdPackageRecordDetailService.selectByMainId(record.getId());
                    record.setPdPackageRecordDetailList(detailList);
                }
                pdStockRecord.setPdPackageRecordList(pdPackageRecordList);
            }

            //查申领单列表
            if (oConvertUtils.isNotEmpty(pdStockRecord.getApplyNo())) {
                PdApplyDetail applyDetail=new PdApplyDetail();
                applyDetail.setApplyNo(pdStockRecord.getApplyNo());
                List<PdApplyDetail> pdApplyDetailList = pdApplyDetailService.selectByApplyNo(applyDetail);
                pdStockRecord.setPdApplyDetailList(pdApplyDetailList);
            }
            //查调拨单列表
            if (oConvertUtils.isNotEmpty(pdStockRecord.getAllocationNo())) {
                PdAllocationDetail allocationDetail=new PdAllocationDetail();
                allocationDetail.setAllocationNo(pdStockRecord.getAllocationNo());
                List<PdAllocationDetail> pdAllocationDetailList = pdAllocationDetailService.selectByAllocationNo(allocationDetail);
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

        PdOnOff query2 = new PdOnOff();
        query2.setDepartParentId(sysUser.getDepartParentId());
        //开关-是否需要出库审批   1-是；0-否
        query2.setCode(PdConstant.ON_OFF_STOCK_OUT_AUDIT);
        PdOnOff stockOutAudit = pdOnOffService.getOne(query2);
        if (stockOutAudit != null && stockOutAudit.getValue() != null) {
            // 自动审批
            pdStockRecord.setAllowStockOutAudit(stockOutAudit.getValue().toString());
        }

        //关-是否允许出入库时可修改进价和出价
        query2.setCode(PdConstant.ON_OFF_ALLOW_EDIT_PRICE);
        PdOnOff allowEditPrice = pdOnOffService.getOne(query2);
        if (allowEditPrice != null && allowEditPrice.getValue() != null) {
            pdStockRecord.setAllowEditPrice(allowEditPrice.getValue().toString());
        }

        //开关-是否显示二级条码框（入库、出库、退货）   1-显示；0-不显示
        query2.setCode(PdConstant.ON_OFF_SHOW_S_BARCODE);
        PdOnOff showSBarcode = pdOnOffService.getOne(query2);
        if (showSBarcode != null && showSBarcode.getValue() != null) {
            pdStockRecord.setShowSBarcode(showSBarcode.getValue().toString());
        }

        //开关-是否显示入库单抬头   1-是；0-否
        query2.setCode(PdConstant.ON_OFF_STOCK_OUT_TEXT);
        query2.setValue(PdConstant.ON_OFF_STOCK_OUT_TEXT_1);
        PdOnOff stockOutText = pdOnOffService.getOne(query2);
        if (stockOutText != null) {
            pdStockRecord.setStockOutText(stockOutText.getDescription());
        }else{
            pdStockRecord.setStockOutText("");
        }

        return pdStockRecord;
    }

    @Override
    public PdStockRecord initInitModal(String id) {
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
//            List<PdStockRecordDetail> uniqueList = new ArrayList<>();
            BigDecimal totalPrice = new BigDecimal(0);//总金额	@TableField(exist = false)
            Double totalSum = new Double(0);//总数量
//            int i = 0;
            for (PdStockRecordDetail item : pdStockRecordDetailList) {
                totalSum = totalSum + item.getProductNum();
                BigDecimal purchasePrice = item.getPurchasePrice() == null ? new BigDecimal(0) : item.getPurchasePrice();
                totalPrice = totalPrice.add(purchasePrice.multiply(BigDecimal.valueOf(item.getProductNum())).setScale(4, BigDecimal.ROUND_HALF_UP));
//                if(PdConstant.CODE_PRINT_TYPE_1.equals(pdStockRecord.getBarCodeType())){
//                    //唯一码 拆分，用于唯一码列表显示
//                    List<String> refBarCodeList = Arrays.asList(item.getRefBarCode().split(","));
//                    for(String refBarCode : refBarCodeList){
//                        i += 1;
//                        PdStockRecordDetail unique = new PdStockRecordDetail();
//                        BeanUtils.copyProperties(item, unique);
//                        unique.setRefBarCode(refBarCode);
//                        unique.setProductNum(1D);
//                        unique.setId(i+"");
//                        uniqueList.add(unique);
//                    }
//                }
            }
            pdStockRecord.setTotalSum(totalSum);
            pdStockRecord.setInTotalPrice(totalPrice);
            pdStockRecord.setPdStockRecordDetailList(pdStockRecordDetailList);
//            pdStockRecord.setPdStockRecordDetailUniqueList(uniqueList);

            if (StringUtils.isNotEmpty(pdStockRecord.getMergeOrderNo())) {
                //查订单列表
                PdPurchaseOrderMergeDetail orderMergeDetail = new PdPurchaseOrderMergeDetail();
                orderMergeDetail.setMergeOrderNo(pdStockRecord.getMergeOrderNo());
                List<PdPurchaseOrderMergeDetail> pdPurchaseDetailList = pdPurchaseOrderMergeDetailService.queryPdPurchaseMergeDetail(orderMergeDetail);
                pdStockRecord.setPdPurchaseOrderMergeDetail(pdPurchaseDetailList);
            }
        } else {  // 新增页面

            SysDepart depart = sysDepartService.getById(sysUser.getCurrentDepartId());
            //部门id
            pdStockRecord.setInDepartId(sysUser.getCurrentDepartId());
            pdStockRecord.setInDepartName(depart.getDepartName());
            //获取入库单号
            pdStockRecord.setRecordNo(UUIDUtil.generateOrderNoByType(PdConstant.ORDER_NO_FIRST_LETTER_CSKC));
            //获取当前日期
            pdStockRecord.setSubmitDateStr(DateUtils.formatDate());
            pdStockRecord.setSubmitDate(DateUtils.getDate());
            //登录人姓名
            pdStockRecord.setSubmitBy(sysUser.getId());
            pdStockRecord.setSubmitByName(sysUser.getRealname());
            //默认入库类型
//            pdStockRecord.setInType(PdConstant.IN_TYPE_1);
            //初始化库存供应商ID
//            pdStockRecord.setSupplierId(PdConstant.INIT_STOCK_SUPPLIER_ID);
        }

        //库区库位下拉框
        pdStockRecord.setGoodsAllocationList(goodsAllocationList);

        return pdStockRecord;
    }

    @Override
    public PdStockRecord initReturnModal(String id) {
        PdStockRecord pdStockRecord = new PdStockRecord();

        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        SysDepart sysDepart = pdDepartService.getById(sysUser.getCurrentDepartId());

        SysDepart firstDepart = sysDepartService.getFirstById(sysUser.getCurrentDepartId());//pdDepartService.getById(sysUser.getFirstDepartId());

        //部门列表
        SysDepart query = new SysDepart();
        query.setDepartParentId(sysUser.getDepartParentId());
        query.setDepartId(sysUser.getCurrentDepartId());

        if (oConvertUtils.isNotEmpty(id)) { // 查看页面
            pdStockRecord = this.getById(id);

            if(oConvertUtils.isEmpty(pdStockRecord.getAuditBy())){
                pdStockRecord.setAuditByName(sysUser.getRealname());
            }

            //查出库单明细
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

        } else {  // 新增页面
            //退货出库科室（当前科室） 当前科室是则器械科不传值
            if(!PdConstant.DEPART_TYPE_1.equals(sysDepart.getDepartType())){
                pdStockRecord.setOutDepartId(sysDepart.getId());
                pdStockRecord.setOutDepartName(sysDepart.getDepartName());
            }
            //退货入库科室（器械科）
            pdStockRecord.setInDepartId(firstDepart.getId());
            pdStockRecord.setInDepartName(firstDepart.getDepartName());
            //获取出库单号
            pdStockRecord.setRecordNo(UUIDUtil.generateOrderNoByType(PdConstant.ORDER_NO_FIRST_LETTER_THCK));
            //获取当前日期
            pdStockRecord.setSubmitDateStr(DateUtils.formatDate());
            pdStockRecord.setSubmitDate(DateUtils.getDate());
            //登录人姓名
            pdStockRecord.setSubmitBy(sysUser.getId());
            pdStockRecord.setSubmitByName(sysUser.getRealname());
        }

        pdStockRecord.setDepartType(sysDepart.getDepartType());//部门类型，1：1级库房，2二级库房

        //库区库位下拉框
        PdGoodsAllocation pdGoodsAllocation = new PdGoodsAllocation();
        pdGoodsAllocation.setDepartId(firstDepart.getId());
        pdGoodsAllocation.setAreaType(PdConstant.GOODS_ALLCATION_AREA_TYPE_2);
        List<PdGoodsAllocationPage> goodsAllocationList = pdGoodsAllocationService.getOptionsForSelect(pdGoodsAllocation);
        pdStockRecord.setGoodsAllocationList(goodsAllocationList);

        PdOnOff query2 = new PdOnOff();
        query2.setDepartParentId(sysUser.getDepartParentId());
        //开关-是否需要出库审批   1-是；0-否
        query2.setCode(PdConstant.ON_OFF_STOCK_OUT_AUDIT);
        PdOnOff stockOutAudit = pdOnOffService.getOne(query2);
        if (stockOutAudit != null && stockOutAudit.getValue() != null) {
            // 自动审批
            pdStockRecord.setAllowStockOutAudit(stockOutAudit.getValue().toString());
        }

        //关-是否允许出入库时可修改进价和出价
        query2.setCode(PdConstant.ON_OFF_ALLOW_EDIT_PRICE);
        PdOnOff allowEditPrice = pdOnOffService.getOne(query2);
        if (allowEditPrice != null && allowEditPrice.getValue() != null) {
            pdStockRecord.setAllowEditPrice(allowEditPrice.getValue().toString());
        }

        //开关-是否显示二级条码框（入库、出库、退货）   1-显示；0-不显示
        query2.setCode(PdConstant.ON_OFF_SHOW_S_BARCODE);
        PdOnOff showSBarcode = pdOnOffService.getOne(query2);
        if (showSBarcode != null && showSBarcode.getValue() != null) {
            pdStockRecord.setShowSBarcode(showSBarcode.getValue().toString());
        }

        //开关-是否显示入库单抬头   1-是；0-否
        query2.setCode(PdConstant.ON_OFF_STOCK_OUT_TEXT);
        query2.setValue(PdConstant.ON_OFF_STOCK_OUT_TEXT_1);
        PdOnOff stockOutText = pdOnOffService.getOne(query2);
        if (stockOutText != null) {
            pdStockRecord.setStockOutText(stockOutText.getDescription());
        }else{
            pdStockRecord.setStockOutText("");
        }

        return pdStockRecord;
    }

    /**
     * 保存出入库记录日志
     *
     * @param pdStockRecord
     */
    private void saveStockLog(PdStockRecord pdStockRecord) {
        //日志
        List<PdStockRecordDetail> detail = pdStockRecord.getPdStockRecordDetailList();
        List<PdStockLog> logList = new ArrayList<PdStockLog>();
        String recordType = pdStockRecord.getRecordType();
        String inType = pdStockRecord.getInType();
        String outType = pdStockRecord.getOutType();
        PdStockLog stockLog;
        for (PdStockRecordDetail psd : detail) {
            stockLog = new PdStockLog();
            stockLog.setDepartId(pdStockRecord.getDepartId());
            stockLog.setDepartParentId(pdStockRecord.getDepartParentId());
            stockLog.setInvoiceNo(pdStockRecord.getRecordNo());
            stockLog.setProductId(psd.getProductId());
            stockLog.setProductBarCode(psd.getProductBarCode());
            stockLog.setBatchNo(psd.getBatchNo());
            stockLog.setProductNum(psd.getProductNum());
            stockLog.setExpDate(psd.getExpDate());
            if (StringUtils.isNotEmpty(pdStockRecord.getSupplierId())) {
                PdSupplier pdSupplier = pdSupplierService.getById(pdStockRecord.getSupplierId());
                stockLog.setInFrom(pdSupplier.getName());
            } else {
                if(oConvertUtils.isEmpty(pdStockRecord.getOutDepartName()) && oConvertUtils.isNotEmpty(pdStockRecord.getOutDepartId())){
                    SysDepart depart = pdDepartService.getById(pdStockRecord.getOutDepartId());
                    pdStockRecord.setOutDepartName(depart.getDepartName());
                }
                stockLog.setInFrom(pdStockRecord.getOutDepartName());
            }
            if(oConvertUtils.isEmpty(pdStockRecord.getInDepartName()) && oConvertUtils.isNotEmpty(pdStockRecord.getInDepartId())){
                SysDepart depart = pdDepartService.getById(pdStockRecord.getInDepartId());
                pdStockRecord.setInDepartName(depart.getDepartName());
            }
            stockLog.setOutTo(pdStockRecord.getInDepartName());
            if(PdConstant.RECODE_TYPE_1.equals(recordType)){ // 入库
                if (PdConstant.IN_TYPE_2.equals(inType)) {
                    stockLog.setLogType(PdConstant.STOCK_LOG_TYPE_5); //退货入库
                } else {
                    stockLog.setLogType(PdConstant.STOCK_LOG_TYPE_1); // 入库
                }
            }else if(PdConstant.RECODE_TYPE_2.equals(recordType)){ // 出库
                if (PdConstant.OUT_TYPE_4.equals(outType)) {
                    stockLog.setLogType(PdConstant.STOCK_LOG_TYPE_5); //退货出库
                } else {
                    stockLog.setLogType(PdConstant.STOCK_LOG_TYPE_2); // 出库
                }
            }else if(PdConstant.RECODE_TYPE_3.equals(recordType)){ // 初始化库存
                stockLog.setLogType(PdConstant.STOCK_LOG_TYPE_13);
            }else if(PdConstant.RECODE_TYPE_4.equals(recordType)){ // 盘点
                if (PdConstant.IN_TYPE_4.equals(inType)) {
                    stockLog.setLogType(PdConstant.STOCK_LOG_TYPE_11); //盘盈入库
                }
                if (PdConstant.OUT_TYPE_5.equals(inType)) {
                    stockLog.setLogType(PdConstant.STOCK_LOG_TYPE_12); //盘亏出库
                }
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

    @Override
    public Page<PdStockRecord> stockRecordReportQuery(Page<PdStockRecord> pageList, PdStockRecord pdStockRecord) {
        return pdStockRecordMapper.stockRecordReportQuery(pageList,pdStockRecord);
    }

    @Override
    public List<PdStockRecord> stockRecordReportQuery(PdStockRecord pdStockRecord) {
        return  pdStockRecordMapper.stockRecordReportQuery(pdStockRecord);
    }

    /**
     * 消息推送
     * @param stockRecord
     * @return
     */
    public boolean sendMsg(PdStockRecord stockRecord, String menuName, String templateCode) {
        Map<String, Object> map = new HashMap<>();
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        List<String> userIdList =pdDepartService.findMenuUser(sysUser.getCurrentDepartId(),menuName);

        String url = "";
        QueryWrapper<SysPermission> queryWrapper = new QueryWrapper<SysPermission>();
        queryWrapper.eq("name",menuName);
        List<SysPermission> permissionList = sysPermissionService.list(queryWrapper);
        if(CollectionUtils.isNotEmpty(permissionList)){
            url = permissionList.get(0).getUrl();
        }

        if (CollectionUtils.isNotEmpty(userIdList)) {
            String userIds = String.join(",", userIdList);
            Map<String, String> strMap = new HashMap<>();
            //模板注入参数
            strMap.put("userName", sysUser.getRealname());
            strMap.put("recordNo", stockRecord.getRecordNo());
            strMap.put("url", url);
            map.put("map", strMap);
            //需要发送消息的用户id
            map.put("userIds", userIds + ",");
            //短信模板标识
            map.put("templateCode", templateCode);
            return pushMsgUtil.newSendMessage(map);
        }
        return false;
    }

    @Override
    public List<HashMap> queryRecordViewMoney(PdStockRecord stockRecord) {
        return pdStockRecordMapper.queryRecordViewMoney(stockRecord);
    }
    @Override
    public List<HashMap> queryRecordViewCount(PdStockRecord stockRecord) {
        return pdStockRecordMapper.queryRecordViewCount(stockRecord);
    }

    /**
     * 入库明细非分页--耗材柜接口查询用
     * @param pdStockRecord
     * @return
     */
    @Override
    public List<Map<String,Object>> findOutQueryList(PdStockRecord pdStockRecord) {
        return pdStockRecordMapper.findOutQueryList(pdStockRecord);
    }

    /**
     * 获取开关
     * @return
     */
    @Override
    public PdStockRecord getOnOff() {
        PdStockRecord pdStockRecord = new PdStockRecord();

        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();

        PdOnOff query = new PdOnOff();
        query.setDepartParentId(sysUser.getDepartParentId());
        //开关-是否允许入库量大于订单量   1-允许入库量大于订单量；0-不允许入库量大于订单量
        query.setCode(PdConstant.ON_OFF_ALLOW_IN_MORE_ORDER);
        PdOnOff allowInMoreOrder = pdOnOffService.getOne(query);
        if (allowInMoreOrder != null && allowInMoreOrder.getValue() != null) {
            pdStockRecord.setAllowInMoreOrder(allowInMoreOrder.getValue().toString());
        }

        //开关-是否允许入库非订单产品     1-允许非订单产品；0-不允许非订单产品
        query.setCode(PdConstant.ON_OFF_ALLOW_NOT_ORDER_PRODUCT);
        PdOnOff allowNotOrderProduct = pdOnOffService.getOne(query);
        if (allowNotOrderProduct != null && allowNotOrderProduct.getValue() != null) {
            pdStockRecord.setAllowNotOrderProduct(allowNotOrderProduct.getValue().toString());
        }

        //开关-是否允许入库非本供应商产品
        query.setCode(PdConstant.ON_OFF_ALLOW_SUPPLIER);
        PdOnOff allowSupplier = pdOnOffService.getOne(query);
        if (allowSupplier != null && allowSupplier.getValue() != null) {
            pdStockRecord.setAllowSupplier(allowSupplier.getValue().toString());
        }

        //关-是否允许出入库时可修改进价和出价
        query.setCode(PdConstant.ON_OFF_ALLOW_EDIT_PRICE);
        PdOnOff allowEditPrice = pdOnOffService.getOne(query);
        if (allowEditPrice != null && allowEditPrice.getValue() != null) {
            pdStockRecord.setAllowEditPrice(allowEditPrice.getValue().toString());
        }

        //开关-是否需要入库审批   1-是；0-否
        query.setCode(PdConstant.ON_OFF_STOCK_IN_AUDIT);
        PdOnOff stockInAudit = pdOnOffService.getOne(query);
        if (stockInAudit != null && stockInAudit.getValue() != null) {
            // 自动审批
            pdStockRecord.setAllowStockInAudit(stockInAudit.getValue().toString());
        }

        //开关-是否允许入库证照过期的产品   1-是；0-否
        query.setCode(PdConstant.ON_OFF_STOCK_IN_EXP_PRODUCT);
        PdOnOff stockInExpProduct = pdOnOffService.getOne(query);
        if (stockInAudit != null && stockInAudit.getValue() != null) {
            // 自动审批
            pdStockRecord.setAllowStockInExpProduct(stockInExpProduct.getValue().toString());
        }

        //开关-是否允许入库证照过期的供应商   1-是；0-否
        query.setCode(PdConstant.ON_OFF_STOCK_IN_EXP_SUPPLIER);
        PdOnOff stockInExpSupplier = pdOnOffService.getOne(query);
        if (stockInAudit != null && stockInAudit.getValue() != null) {
            // 自动审批
            pdStockRecord.setAllowStockInExpSupplier(stockInExpSupplier.getValue().toString());
        }

        //开关-是否显示二级条码框（入库、出库、退货）   1-显示；0-不显示
        query.setCode(PdConstant.ON_OFF_SHOW_S_BARCODE);
        PdOnOff showSBarcode = pdOnOffService.getOne(query);
        if (showSBarcode != null && showSBarcode.getValue() != null) {
            pdStockRecord.setShowSBarcode(showSBarcode.getValue().toString());
        }

        //开关-是否显示入库单抬头   1-是；0-否
        query.setCode(PdConstant.ON_OFF_STOCK_IN_TEXT);
        query.setValue(PdConstant.ON_OFF_STOCK_IN_TEXT_1);
        PdOnOff stockInText = pdOnOffService.getOne(query);
        if (stockInText != null) {
            pdStockRecord.setStockInText(stockInText.getDescription());
        }else{
            pdStockRecord.setStockInText("");
        }
        return pdStockRecord;
    }


    @Override
    public IPage<PdStockRecord> querySupplierCountPageList(Page<PdStockRecord> pageList, PdStockRecord pdStockRecord) {
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        pdStockRecord.setDepartParentId(sysUser.getDepartParentId());
        pdStockRecord.setDepartId(sysUser.getCurrentDepartId());
        pdStockRecord.setInDepartId(sysUser.getCurrentDepartId());
        pdStockRecord.setRecordType(PdConstant.RECODE_TYPE_1);
        pdStockRecord.setAuditStatus(PdConstant.AUDIT_STATE_2);
        IPage<PdStockRecord>  recordList=  pdStockRecordMapper.querySupplierCountPageList(pageList,pdStockRecord);
        List<PdStockRecord> list=recordList.getRecords();
        if(CollectionUtils.isNotEmpty(list)){
            for(PdStockRecord record : list){
                PdStockRecord stockRecord=new PdStockRecord();
                stockRecord.setProductId(record.getProductId());
                stockRecord.setSupplierId(record.getSupplierId());
                stockRecord.setRecordType(PdConstant.RECODE_TYPE_2);
                stockRecord.setOutDepartId(sysUser.getCurrentDepartId());
                stockRecord.setOutType(PdConstant.OUT_TYPE_2);
                PdStockRecord record1= pdStockRecordMapper.queryApplyPriceList(stockRecord);
                record.setOutTotalPrice(record1.getOutTotalPrice());
                record.setOutProductNum(record1.getOutProductNum());
            }
        }
        return recordList;
    }


    @Override
    public List<PdStockRecord> querySupplierCountList(PdStockRecord pdStockRecord) {
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        pdStockRecord.setDepartParentId(sysUser.getDepartParentId());
        pdStockRecord.setDepartId(sysUser.getCurrentDepartId());
        pdStockRecord.setInDepartId(sysUser.getCurrentDepartId());
        pdStockRecord.setRecordType(PdConstant.RECODE_TYPE_1);
        pdStockRecord.setAuditStatus(PdConstant.AUDIT_STATE_2);
        List<PdStockRecord> list= pdStockRecordMapper.querySupplierCountPageList(pdStockRecord);
        if(CollectionUtils.isNotEmpty(list)){
            for(PdStockRecord record : list){
                PdStockRecord stockRecord=new PdStockRecord();
                stockRecord.setProductId(record.getProductId());
                stockRecord.setSupplierId(record.getSupplierId());
                stockRecord.setRecordType(PdConstant.RECODE_TYPE_2);
                stockRecord.setOutDepartId(sysUser.getCurrentDepartId());
                stockRecord.setOutType(PdConstant.OUT_TYPE_2);
                PdStockRecord record1= pdStockRecordMapper.queryApplyPriceList(stockRecord);
                record.setOutTotalPrice(record1.getOutTotalPrice());
                record.setOutProductNum(record1.getOutProductNum());
            }
        }
        return list;
    }

    /**
     * 盘盈入库
     * @param pdProductStockCheck
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public String addInForStockCheck(PdProductStockCheck pdProductStockCheck, List<PdProductStockCheckChild> inChildList) {
        // 封装入库单
        PdStockRecord pdStockRecord = getStockRecordForStockCheck(pdProductStockCheck,inChildList,PdConstant.IN_TYPE_4,null);
        pdStockRecordMapper.insert(pdStockRecord);

        // 封装入库单明细
        List<PdStockRecordDetail> detailList = getStockRecordDetailForStockCheck(pdStockRecord,inChildList,PdConstant.IN_TYPE_4,null);
        pdStockRecordDetailService.saveBatch(detailList);

        // 3.处理库存
        pdStockRecord.setPdStockRecordDetailList(detailList);
        String inStr = pdProductStockTotalService.updateInStock(pdStockRecord);
        if (!PdConstant.TRUE.equals(inStr)) {
            throw new RuntimeException(inStr);
        }

        // 4.保存出入库记录日志
        this.saveStockLog(pdStockRecord);

        return pdStockRecord.getId();
    }

    /**
     * 盘亏出库
     * @param pdProductStockCheck
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public String addOutForStockCheck(PdProductStockCheck pdProductStockCheck, List<PdProductStockCheckChild> outChildList) {
        // 封装出库单
        PdStockRecord pdStockRecord = getStockRecordForStockCheck(pdProductStockCheck,outChildList,null,PdConstant.OUT_TYPE_5);
        pdStockRecordMapper.insert(pdStockRecord);

        // 封装出库单明细
        List<PdStockRecordDetail> detailList = getStockRecordDetailForStockCheck(pdStockRecord,outChildList,null,PdConstant.OUT_TYPE_5);
        pdStockRecordDetailService.saveBatch(detailList);

        // 3.处理出库库存
        pdStockRecord.setPdStockRecordDetailList(detailList);
        String inStr = pdProductStockTotalService.updateOutStock(pdStockRecord);
        if (!PdConstant.TRUE.equals(inStr)) {
            throw new RuntimeException(inStr);
        }

        //4.保存出库日志记录
        this.saveStockLog(pdStockRecord);

        return pdStockRecord.getId();
    }


    private PdStockRecord getStockRecordForStockCheck(PdProductStockCheck pdProductStockCheck, List<PdProductStockCheckChild> childList, String inType, String outType){
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        PdStockRecord pdStockRecord = new PdStockRecord();
        // 1. 封装出入库单
        Date date = DateUtils.getDate();
        pdStockRecord.setRecordType(PdConstant.RECODE_TYPE_4); //盘点出入库单
        pdStockRecord.setSubmitStatus(PdConstant.SUBMIT_STATE_2); // 已提交
        pdStockRecord.setAuditStatus(PdConstant.AUDIT_STATE_2);   // 审核通过
        pdStockRecord.setExtend2(pdProductStockCheck.getCheckNo());//盘点单号
        pdStockRecord.setSubmitBy(sysUser.getId());
        pdStockRecord.setSubmitDate(date);
        pdStockRecord.setAuditBy(sysUser.getId());
        pdStockRecord.setAuditDate(date);
        pdStockRecord.setUpdateTime(date);
        pdStockRecord.setCreateBy(sysUser.getRealname());
        pdStockRecord.setCreateTime(date);
        if(oConvertUtils.isNotEmpty(inType)){
            pdStockRecord.setInDepartId(sysUser.getCurrentDepartId());
            pdStockRecord.setRecordNo(UUIDUtil.generateOrderNoByType(PdConstant.ORDER_NO_FIRST_LETTER_PDRK)); //生成入库单号
            pdStockRecord.setInType(inType);
            pdStockRecord.setRefuseReason("盘盈入库自动审批");//审批意见
            pdStockRecord.setRemarks("盘盈入库");
        }
        if(oConvertUtils.isNotEmpty(outType)){
            pdStockRecord.setOutDepartId(sysUser.getCurrentDepartId());
            pdStockRecord.setRecordNo(UUIDUtil.generateOrderNoByType(PdConstant.ORDER_NO_FIRST_LETTER_PDCK)); //生成出库单号
            pdStockRecord.setOutType(outType);
            pdStockRecord.setRefuseReason("盘亏出库自动审批");//审批意见
            pdStockRecord.setRemarks("盘亏出库");
        }
        return pdStockRecord;
    }
    private List<PdStockRecordDetail> getStockRecordDetailForStockCheck(PdStockRecord pdStockRecord, List<PdProductStockCheckChild> childList, String inType, String outType){
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        Date date = DateUtils.getDate();
        // 2.封装出入库明细
        List<PdStockRecordDetail> detailList = new ArrayList<>();
        for(PdProductStockCheckChild child : childList){
            PdProductStock stock = pdProductStockService.getById(child.getStockId());

            PdStockRecordDetail detail = new PdStockRecordDetail();
            detail.setRecordId(pdStockRecord.getId());
            detail.setProductStockId(stock.getId());
            detail.setProductId(stock.getProductId());
            detail.setProductBarCode(stock.getProductBarCode());
            detail.setBatchNo(stock.getBatchNo());
            detail.setExpDate(stock.getExpDate());
            detail.setSellingPrice(stock.getSellingPrice());
            detail.setPurchasePrice(stock.getPurchasePrice());
            detail.setSpecUnitId(stock.getSpecUnitId());
            detail.setSpecQuantity(stock.getSpecQuantity());
            detail.setProductNum(Math.abs(child.getProfitLossCount()));
//            detail.setInHuoweiCode(stock.getHuoweiCode());
            detail.setRegistration(stock.getRegistration());
//            detail.setRefBarCode(stock.getRefBarCode());
            detail.setSupplierId(stock.getSupplierId());
            detail.setDistributorId(stock.getDistributorId());
            detail.setProduceDate(stock.getProduceDate());
//            detail.setBarCodeType(pdStockRecord.getBarCodeType());
//            detail.setRemarks(remarks);
            detail.setExtend2(child.getId());
            detail.setUpdateTime(date);
            detail.setCreateBy(sysUser.getRealname());
            detail.setCreateTime(date);
            if(oConvertUtils.isNotEmpty(inType)){
                detail.setRemarks("盘盈入库");
            }
            if(oConvertUtils.isNotEmpty(outType)){
                detail.setRemarks("盘亏出库");
            }
            detailList.add(detail);
        }
        return detailList;
    }

    /**
     * 一体机终端出库接口实现 （只适配唯一码）
     * @param pdStockRecord
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public String addOutForTerminal(PdStockRecord pdStockRecord, List<PdProductStock> stockList) {
        // 1、封装出库单信息
        pdStockRecord.setRecordNo(UUIDUtil.generateOrderNoByType(PdConstant.ORDER_NO_FIRST_LETTER_CK));
        pdStockRecord.setOutType(PdConstant.OUT_TYPE_2);
        pdStockRecord.setSubmitDate(DateUtils.getDate());// 提交日期
        pdStockRecord.setAuditDate(DateUtils.getDate()); // 审核日期
        pdStockRecord.setCreateTime(DateUtils.getDate());
        pdStockRecord.setRecordType(PdConstant.RECODE_TYPE_2); // 出库
        pdStockRecord.setSubmitStatus(PdConstant.SUBMIT_STATE_2); // 已提交
        pdStockRecord.setAuditStatus(PdConstant.AUDIT_STATE_2);//审核通过
        pdStockRecord.setRemarks("一体机终端07出库");
        pdStockRecord.setBarCodeType(PdConstant.CODE_PRINT_TYPE_1);
        // 2、封装出库明细
        List<PdStockRecordDetail> detailList = new ArrayList<>();
        for (PdProductStock stock : stockList) {
            PdStockRecordDetail detail = new PdStockRecordDetail();
            detail.setProductStockId(stock.getId());
            detail.setProductId(stock.getProductId());
            detail.setProductBarCode(stock.getProductBarCode());
            detail.setBatchNo(stock.getBatchNo());
            detail.setExpDate(stock.getExpDate());
            detail.setSellingPrice(stock.getSellingPrice());
            detail.setPurchasePrice(stock.getPurchasePrice());
            detail.setSpecUnitId(stock.getSpecUnitId());
            detail.setSpecQuantity(stock.getSpecQuantity());
            detail.setProductNum(1D);
            detail.setStockNum(stock.getStockNum());
            detail.setOutHuoweiCode(stock.getHuoweiCode());
            detail.setRegistration(stock.getRegistration());
            detail.setRefBarCode(stock.getRefBarCode());
            detail.setSupplierId(stock.getSupplierId());
            detail.setDistributorId(stock.getDistributorId());
            detail.setProduceDate(stock.getProduceDate());
            detail.setBarCodeType(pdStockRecord.getBarCodeType());
            detail.setRemarks("一体机终端出库");
            detailList.add(detail);
        }
        // 4、保存出库
        String recordId = this.saveOutStockRecordInterface(pdStockRecord, detailList);
        // 5、自动审批 + 自动增减库存
        PdStockRecord auditEntity = new PdStockRecord();
        BeanUtils.copyProperties(pdStockRecord,auditEntity);
        auditEntity.setAuditStatus(PdConstant.AUDIT_STATE_2);
        auditEntity.setRefuseReason("系统自动审批通过");
        this.auditOutInterface(auditEntity,pdStockRecord);
        return recordId;
    }

    /**
     * 出入库统计报表 分页查询
     * @param page
     * @param rpInAndOutReportPage
     * @return
     */
    @Override
    public IPage<RpInAndOutReportPage> rpInAndOutReport(Page<RpInAndOutReportPage> page, RpInAndOutReportPage rpInAndOutReportPage) {
        IPage<RpInAndOutReportPage> pageList = pdStockRecordMapper.rpInAndOutReport(page,rpInAndOutReportPage);
//        List<RpInAndOutReportPage> list = pageList.getRecords();
//        for(RpInAndOutReportPage vo : list){
//            rpInAndOutReportPage.setDepartId(vo.getDepartId());
//            //入库数据
//            RpInAndOutReportPage inVo = pdStockRecordMapper.getInTotalData(rpInAndOutReportPage);
//            if(inVo != null){
//                vo.setInProductNum(inVo.getInProductNum());
//                vo.setInTotalPrice(inVo.getInTotalPrice());
//            }else{
//                vo.setInProductNum(0D);
//                vo.setInTotalPrice(new BigDecimal(0));
//            }
//            //出库数据
//            RpInAndOutReportPage outVo = pdStockRecordMapper.getOutTotalData(rpInAndOutReportPage);
//            if(outVo != null){
//                vo.setOutProductNum(outVo.getOutProductNum());
//                vo.setOutTotalPrice(outVo.getOutTotalPrice());
//            }else{
//                vo.setOutProductNum(0D);
//                vo.setOutTotalPrice(new BigDecimal(0));
//            }
//        }

        return pageList;
    }

    /**
     * 出入库统计报表
     * @param rpInAndOutReportPage
     * @return
     */
    @Override
    public List<RpInAndOutReportPage> rpInAndOutReport(RpInAndOutReportPage rpInAndOutReportPage) {
        List<RpInAndOutReportPage> list = pdStockRecordMapper.rpInAndOutReport(rpInAndOutReportPage);
//        for(RpInAndOutReportPage vo : list){
//            rpInAndOutReportPage.setDepartId(vo.getDepartId());
//            //入库数据
//            RpInAndOutReportPage inVo = pdStockRecordMapper.getInTotalData(rpInAndOutReportPage);
//            if(inVo != null){
//                vo.setInProductNum(inVo.getInProductNum());
//                vo.setInTotalPrice(inVo.getInTotalPrice());
//            }else{
//                vo.setInProductNum(0D);
//                vo.setInTotalPrice(new BigDecimal(0));
//            }
//            //出库数据
//            RpInAndOutReportPage outVo = pdStockRecordMapper.getOutTotalData(rpInAndOutReportPage);
//            if(outVo != null){
//                vo.setOutProductNum(outVo.getOutProductNum());
//                vo.setOutTotalPrice(outVo.getOutTotalPrice());
//            }else{
//                vo.setOutProductNum(0D);
//                vo.setOutTotalPrice(new BigDecimal(0));
//            }
//        }
        return list;
    }

    /**
     * 一体机试剂出库（接口过来的）
     * @param pdStockRecord
     * @param pdStockRecordDetailList
     * @return
     */
    private String saveOutStockRecordInterface(PdStockRecord pdStockRecord, List<PdStockRecordDetail> pdStockRecordDetailList) {
        SysDepart sysDepart = pdDepartService.getById(pdStockRecord.getOutDepartId());
        String departId = pdStockRecord.getOutDepartId();
        pdStockRecord.setDepartId(departId);
        pdStockRecord.setDepartParentId(sysDepart.getDepartParentId());
        pdStockRecordMapper.insert(pdStockRecord);
        if (CollectionUtils.isNotEmpty(pdStockRecordDetailList)) {
            for (PdStockRecordDetail entity : pdStockRecordDetailList) {
                entity.setId(null);//初始化ID (从前端传过来会自带页面列表行的ID)
                entity.setRecordId(pdStockRecord.getId());//外键设置
                entity.setDelFlag(PdConstant.DEL_FLAG_0);
                entity.setDepartId(departId);
                entity.setDepartParentId(sysDepart.getDepartParentId());
                pdStockRecordDetailMapper.insert(entity);
            }
        }
        return pdStockRecord.getId();
    }



    /**
     * 一体机试剂入库（接口过来的）
     * @param pdStockRecord
     * @param pdStockRecordDetailList
     * @param outType
     * @return
     */
    private String saveInStockRecordInterface(PdStockRecord pdStockRecord, List<PdStockRecordDetail> pdStockRecordDetailList, String outType) {

        List<PdStockRecordDetail> newDetailList = new ArrayList<>();
        if (oConvertUtils.isNotEmpty(outType)) {
            // 调拨出库 或 申领出库 自动生成入库单 （出库入库）
            String outRecordNo = pdStockRecord.getRecordNo(); //出库单号
            if (PdConstant.OUT_TYPE_3.equals(outType)) {
                pdStockRecord.setInType(PdConstant.IN_TYPE_3);
            } else if(PdConstant.OUT_TYPE_4.equals(outType)){
                pdStockRecord.setInType(PdConstant.IN_TYPE_2);
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
            pdStockRecord.setExtend1(outRecordNo); // 本字段用于存 出库入库的 出库单号， 用于退货出库时 按出库单号查询库存

            if(PdConstant.CODE_PRINT_TYPE_1.equals(pdStockRecord.getBarCodeType())){
                // 唯一码出库入库，需要合并入库明细
                Set<String> setIds = new HashSet<>();
                for (PdStockRecordDetail main : pdStockRecordDetailList) {
                    Double productNum = 0D;
                    StringBuilder setId = new StringBuilder();
                    setId.append(main.getProductStockId()).append(main.getInHuoweiCode());
                    String mainHuoweCode = main.getInHuoweiCode() == null ? "" : main.getInHuoweiCode();
                    if(setIds.add(setId.toString())){
                        List<String> refBarCodes = new ArrayList<>();
                        for (PdStockRecordDetail entity : pdStockRecordDetailList) {
                            String entityHuoweCode = entity.getInHuoweiCode() == null ? "" : entity.getInHuoweiCode();
                            if(main.getProductStockId().equals(entity.getProductStockId()) && mainHuoweCode.equals(entityHuoweCode)){
                                refBarCodes.add(entity.getRefBarCode());
                                productNum = productNum + entity.getProductNum();
                            }
                        }
                        main.setProductNum(productNum);
                        main.setRefBarCode(String.join(",", refBarCodes));// 合并后记录唯一码（用于入库后更新条码表）
                        main.setExtend1(outRecordNo); // 本字段用于存 出库入库的 出库单号， 用于退货出库时 按出库单号查询库存
                        newDetailList.add(main);
                    }
                }
            }
        }
        pdStockRecordMapper.insert(pdStockRecord);
        if(CollectionUtils.isNotEmpty(newDetailList)){
            for (PdStockRecordDetail detail : newDetailList) {
                detail.setId(null);//初始化ID (从前端传过来会自带页面列表行的ID)
                detail.setRecordId(pdStockRecord.getId());//外键设置
                detail.setDepartId(pdStockRecord.getOutDepartId());
                detail.setDepartParentId(pdStockRecord.getDepartParentId());
                detail.setDelFlag(PdConstant.DEL_FLAG_0);
                detail.setProductStockId(null); //清空库存ID
                detail.setCreateTime(DateUtils.getDate());
                pdStockRecordDetailMapper.insert(detail);
            }
        }
        return pdStockRecord.getId();
    }
}
