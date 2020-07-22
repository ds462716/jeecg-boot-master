package org.jeecg.modules.pd.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.jeecg.modules.pd.vo.PdGoodsAllocationPage;
import org.jeecg.modules.system.entity.SysDepart;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.jeecgframework.poi.excel.annotation.ExcelCollection;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @Description: 出入库记录表
 * @Author: jiangxz
 * @Date:   2020-02-13
 * @Version: V1.0
 */
@Data
@TableName("pd_stock_record")
public class PdStockRecord extends BaseEntity {
    private static final long serialVersionUID = 1L;
    
	/**主键*/
	@TableId(type = IdType.ID_WORKER_STR)
    private String id;
    /**出入库单号*/
    @Excel(name = "出入库单号", width = 15)
    private String recordNo;
    /**出入库类型：1-入库；2-出库*/
    @Excel(name = "出入库类型：1-入库；2-出库", width = 15)
    private String recordType;
    /**出库类型 : 1-申领出库; 2-科室出库; 3-调拨出库; 4-退货出库*/
    @Excel(name = "出库类型 : 1-申领出库; 2-科室出库; 3-调拨出库; 4-退货出库", width = 15)
    private String outType;
    /**入库类型 : 1-正常入库，2-退货入库，3-调拨入库*/
    @Excel(name = "入库类型 : 1-正常入库，2-退货入库，3-调拨入库", width = 15)
    private String inType;
    /**采购订单号*/
    @Excel(name = "采购订单号", width = 15)
    private String orderNo;
    /**合并采购订单号*/
    @Excel(name = "合并采购订单号", width = 15)
    private String mergeOrderNo;
    /**调拨单号*/
    @Excel(name = "调拨单号", width = 15)
    private String allocationNo;
    /**申领单号*/
    @Excel(name = "申领单号", width = 15)
    private String applyNo;
    /**退货单号*/
    @Excel(name = "退货单号", width = 15)
    private String dosagertNo;
    /**驳回原因*/
    @Excel(name = "驳回原因", width = 15)
    private String refuseReason;
    /**备注*/
    @Excel(name = "备注", width = 15)
    private String remarks;
    @Excel(name = "业态", width = 15)
    private String format;
    /**验收结果 : 0-合格；1-不合格*/
    @Excel(name = "验收结果 : 0-合格；1-不合格", width = 15)
    private String testResult;
    /**储运状态 : 0-合格；1-不合格*/
    @Excel(name = "储运状态 : 0-合格；1-不合格", width = 15)
    private String storageResult;
    /**温度*/
    @Excel(name = "温度", width = 15)
    private String temperature;
    /**湿度*/
    @Excel(name = "湿度", width = 15)
    private String humidity;
    /**出库部门ID*/
    @Excel(name = "出库部门ID", width = 15)
    private String outDepartId;
    /**入库部门ID*/
    @Excel(name = "入库部门ID", width = 15)
    private String inDepartId;
    /**供应商ID*/
    @Excel(name = "供应商ID", width = 15)
    private String supplierId;
    /**供应商ID*/
    @Excel(name = "配送商ID", width = 15)
    private String distributorId;
    /**操作人*/
    @Excel(name = "操作人", width = 15)
    private String submitBy;
    /**出入库时间*/
    @Excel(name = "出入库时间", width = 15, format = "yyyy-MM-dd")
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private Date submitDate;
    /**记录状态 : 1-待提交； 2-已提交； 3-已撤回*/
    @Excel(name = "状态", width = 15)
    private String submitStatus;
    /**审核人*/
    @Excel(name = "审核人", width = 15)
    private String auditBy;
    /**审核时间*/
    @Excel(name = "审核时间", width = 15, format = "yyyy-MM-dd")
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private Date auditDate;
    /**审核状态  审核状态 1-待审核； 2-审核通过； 3-已拒绝**/
    @Excel(name = "审核状态", width = 15)
    private String auditStatus;
    /**退货单状态*/
    @Excel(name = "退货单状态", width = 15)
    private String returnStatus;
    /**领用人**/
    private String applyBy;
    /**扩展1*/
    @Excel(name = "扩展1", width = 15)
    private String extend1;
    /**扩展2*/
    @Excel(name = "扩展2", width = 15)
    private String extend2;
    /**扩展3*/
    @Excel(name = "扩展3", width = 15)
    private String extend3;
    /**创建人*/
    @Excel(name = "创建人", width = 15)
    private String createBy;
    /**创建日期*/
    @Excel(name = "创建日期", width = 20, format = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date createTime;
    /**更新人*/
    @Excel(name = "更新人", width = 15)
    private String updateBy;
    /**更新日期*/
    @Excel(name = "更新日期", width = 20, format = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date updateTime;
    /**所属部门*/
    @Excel(name = "所属部门", width = 15)
    private String sysOrgCode;
    /**所属部门*/
    private String departId;
    private String departParentId;
    private String barCodeType;

    // pd_stock_record表外字段 ↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓
    @ExcelCollection(name="出入库明细表")
    @TableField(exist = false)
    private List<PdStockRecordDetail> pdStockRecordDetailList;
    // 唯一码列表
    @TableField(exist = false)
    private List<PdStockRecordDetail> pdStockRecordDetailUniqueList;
    // 采购订单明细
    @TableField(exist = false)
    private List<PdPurchaseOrderMergeDetail> pdPurchaseOrderMergeDetail;
    // 申领单明细
    @TableField(exist = false)
    private List<PdApplyDetail> pdApplyDetailList;
    // 调拨单明细
    @TableField(exist = false)
    private List<PdAllocationDetail> pdAllocationDetailList;
    // 定数包记录
    @TableField(exist = false)
    private List<PdPackageRecord> pdPackageRecordList;

    /**出入库日期 字符串格式*/
    @TableField(exist = false)
    private String submitDateStr;
    /** 货区货位列表 用于二级联动下拉框 */
    @TableField(exist = false)
    List<PdGoodsAllocationPage> goodsAllocationList;
    /** 部门列表 用户部门下拉框 **/
    @TableField(exist = false)
    List<SysDepart> sysDepartList;
    /** 1-允许入库量大于订单量；0-不允许入库量大于订单量 */
    @TableField(exist = false)
    private String allowInMoreOrder;
    /** 1-允许非订单产品；0-不允许非订单产品 */
    @TableField(exist = false)
    private String allowNotOrderProduct;
    /**是否允许入库非本供应商产品**/
    @TableField(exist = false)
    private String allowSupplier;
    /**是否允许出入库时可修改进价和出价**/
    @TableField(exist = false)
    private String allowEditPrice;
    /**开关-是否需要入库审批**/
    @TableField(exist = false)
    private String allowStockInAudit;
    /**开关-是否需要出库审批**/
    @TableField(exist = false)
    private String allowStockOutAudit;
    /**开关-是否允许入库证照过期的产品**/
    @TableField(exist = false)
    private String allowStockInExpProduct;
    /**开关-是否允许入库证照过期的供应商**/
    @TableField(exist = false)
    private String allowStockInExpSupplier;
    /**开关-是否显示二级条码框（入库、出库、退货）**/
    @TableField(exist = false)
    private String showSBarcode;
    /**出库单抬头**/
    @TableField(exist = false)
    private String stockOutText;
    /**入库单抬头**/
    @TableField(exist = false)
    private String stockInText;
    /** 入库部门名称 **/
    @TableField(exist = false)
    private String inDepartName;
    /** 出库部门名称 **/
    @TableField(exist = false)
    private String outDepartName;
    /** 供应商名称 **/
    @TableField(exist = false)
    private String supplierName;
    /** 配送商名称 **/
    @TableField(exist = false)
    private String distributorName;
    /** 生产厂家名称 **/
    @TableField(exist = false)
    private String venderName;
    /** 生产厂家Id **/
    @TableField(exist = false)
    private String venderId;
    /** 申请人姓名 **/
    @TableField(exist = false)
    private String submitByName;
    /** 审批人姓名 **/
    @TableField(exist = false)
    private String auditByName;
    @TableField(exist = false)
    private BigDecimal inTotalPrice;//总金额	@TableField(exist = false)
    @TableField(exist = false)
    private BigDecimal outTotalPrice;// 出库总金额	@TableField(exist = false)
    @TableField(exist = false)
    private Double totalSum;//总数量
    @TableField(exist = false)
    private String productName;//产品名称
    @TableField(exist = false)
    private String productId;//产品id
    @TableField(exist = false)
    private String number;//产品编号
    @TableField(exist = false)
    private String spec;//产品规格
    @TableField(exist = false)
    private String unitName;//单位
    @TableField(exist = false)
    private String version;//产品型号
    @TableField(exist = false)
    private String batchNo;//产品批号
    @TableField(exist = false)
    private String productBarCode;//产品条码
    @TableField(exist = false)
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private Date expDate;//有效期
    @TableField(exist = false)
    private String productNum;//产品数量
    @TableField(exist = false)
    private String productFlag;//产品类型
    /** 查询日期起始 **/
    @TableField(exist = false)
    private String queryDateStart;
    /** 查询日期结束 **/
    @TableField(exist = false)
    private String queryDateEnd;
    @TableField(exist = false)
    private String hospitalCode;
    /**产品类型名称or普通产品，0产品，1试剂*/
    @TableField(exist = false)
    private String productFlagName;
    /**注册证*/
    @TableField(exist = false)
    private String registration;

    @TableField(exist = false)
    private String departType;//部门类型，1：1级库房，2二级库房

    @TableField(exist = false)
    private String onlyReturn;//只查退货出库
    @TableField(exist = false)
    private String exceptReturn;//除了退货出库


    @TableField(exist = false)
    private Double stockNum; //库存数量

    @TableField(exist = false)
    private BigDecimal stockPrice; //库存总金额

    @TableField(exist = false)
    private BigDecimal purchasePrice; //产品单价
    @TableField(exist = false)
    private String bidingNumber; //中标号
    @TableField(exist = false)
    private String categoryOne; //一级分类
    @TableField(exist = false)
    private String chargeCode;//收费代号
    @TableField(exist = false)
    private Double outProductNum; //出库数量
    @TableField(exist = false)
    private List<String> uniqueCode;// 唯一码 用于一体终端机唯一码出库
}
