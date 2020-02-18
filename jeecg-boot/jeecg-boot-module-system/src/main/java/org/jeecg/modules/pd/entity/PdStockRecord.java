package org.jeecg.modules.pd.entity;

import java.io.Serializable;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

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
    private String recordNo;
	/**出入库类型：1-入库；2-出库*/
    private String recodeType;
	/**出库类型 : 1-正常出库，2-调拨出库，3-退货出库*/
    private String outType;
	/**入库类型 : 1-正常入库，2-退货入库，3-调拨入库*/
    private String inType;
	/**采购订单号*/
    private String orderNo;
	/**调拨单号*/
    private String allocationNo;
	/**申领单号*/
    private String applyNo;
	/**退货单号*/
    private String dosagertNo;
	/**操作人*/
    private String recordPeople;
	/**出入库时间*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private Date recordDate;
	/**记录状态 : 1-待审核；2-已通过；3-已拒绝*/
    private String recordState;
	/**驳回原因*/
    private String rejectReason;
	/**备注*/
    private String remarks;
	/**验收结果 : 0-合格；1-不合格*/
    private String testResult;
	/**储运状态 : 0-合格；1-不合格*/
    private String storageResult;
	/**温度*/
    private String temperature;
	/**湿度*/
    private String humidity;
	/**出库部门ID*/
    private String outDepartId;
	/**入库部门ID*/
    private String inDepartId;
	/**供应商ID*/
    private String supplierId;
	/**审核人*/
    private String checkPeople;
	/**审核时间*/
    private String checkTime;
	/**退货单状态*/
    private String returnState;
	/**扩展1*/
    private String extend1;
	/**扩展2*/
    private String extend2;
	/**扩展3*/
    private String extend3;
	/** 删除标记（0：正常；1：删除）*/
//    private String delFlag;
	/**创建人*/
    private String createBy;
	/**创建日期*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date createTime;
	/**更新人*/
    private String updateBy;
	/**更新日期*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date updateTime;
	/**所属部门*/
    private String sysOrgCode;
	/**所属父部门*/
    private String sysOrgParentCode;
}
