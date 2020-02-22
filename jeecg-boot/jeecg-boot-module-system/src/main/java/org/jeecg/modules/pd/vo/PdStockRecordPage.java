package org.jeecg.modules.pd.vo;

import java.util.List;
import org.jeecg.modules.pd.entity.PdStockRecord;
import org.jeecg.modules.pd.entity.PdStockRecordDetail;
import lombok.Data;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.jeecgframework.poi.excel.annotation.ExcelEntity;
import org.jeecgframework.poi.excel.annotation.ExcelCollection;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;
import java.util.Date;

/**
 * @Description: 出入库记录表
 * @Author: jiangxz
 * @Date:   2020-02-13
 * @Version: V1.0
 */
@Data
public class PdStockRecordPage {
	
	/**主键*/
	private String id;
	/**出入库单号*/
	@Excel(name = "出入库单号", width = 15)
	private String recordNo;
	/**出入库类型：1-入库；2-出库*/
	@Excel(name = "出入库类型：1-入库；2-出库", width = 15)
	private String recordType;
	/**出库类型 : 1-正常出库，2-调拨出库，3-退货出库*/
	@Excel(name = "出库类型 : 1-正常出库，2-调拨出库，3-退货出库", width = 15)
	private String outType;
	/**入库类型 : 1-正常入库，2-退货入库，3-调拨入库*/
	@Excel(name = "入库类型 : 1-正常入库，2-退货入库，3-调拨入库", width = 15)
	private String inType;
	/**采购订单号*/
	@Excel(name = "采购订单号", width = 15)
	private String orderNo;
	/**调拨单号*/
	@Excel(name = "调拨单号", width = 15)
	private String allocationNo;
	/**申领单号*/
	@Excel(name = "申领单号", width = 15)
	private String applyNo;
	/**退货单号*/
	@Excel(name = "退货单号", width = 15)
	private String dosagertNo;

	/**操作人*/
	@Excel(name = "操作人", width = 15)
	private String recordPeople;
	/**操作人姓名*/
	private String recordPeopleName;

	/**出入库时间*/
	@Excel(name = "出入库时间", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
	private Date recordDate;
	/**出入库日期 字符串格式*/
	private String recordDateStr;

	/**记录状态 : 1-待审核；2-已通过；3-已拒绝*/
	@Excel(name = "记录状态 : 1-待审核；2-已通过；3-已拒绝", width = 15)
	private String recordState;
	/**驳回原因*/
	@Excel(name = "驳回原因", width = 15)
	private String rejectReason;
	/**备注*/
	@Excel(name = "备注", width = 15)
	private String remarks;
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
	/**审核人*/
	@Excel(name = "审核人", width = 15)
	private String checkPeople;
	/**审核时间*/
	@Excel(name = "审核时间", width = 15)
	private String checkTime;
	/**退货单状态*/
	@Excel(name = "退货单状态", width = 15)
	private String returnState;
	/**扩展1*/
	@Excel(name = "扩展1", width = 15)
	private String extend1;
	/**扩展2*/
	@Excel(name = "扩展2", width = 15)
	private String extend2;
	/**扩展3*/
	@Excel(name = "扩展3", width = 15)
	private String extend3;
	/** 删除标记（0：正常；1：删除）*/
//	@Excel(name = " 删除标记（0：正常；1：删除）", width = 15)
//	private String delFlag;
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
	/**所属父部门*/
	@Excel(name = "所属父部门", width = 15)
	private String sysOrgParentCode;



	@ExcelCollection(name="出入库明细表")
	private List<PdStockRecordDetail> pdStockRecordDetailList;

	/**
	 * 货区货位列表 用于二级联动下拉框
	 */
	List<PdGoodsAllocationPage> goodsAllocationList;

	/**
	 * 1-允许入库量大于订单量；0-不允许入库量大于订单量
	 */
	private String allowInMoreOrder;

	/**
	 * 1-允许非订单产品；0-不允许非订单产品
	 */
	private String allowNotOrderProduct;
	
}
