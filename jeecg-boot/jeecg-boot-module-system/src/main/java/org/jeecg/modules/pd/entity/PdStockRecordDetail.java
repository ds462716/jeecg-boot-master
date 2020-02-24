package org.jeecg.modules.pd.entity;

import java.io.Serializable;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;
import org.jeecgframework.poi.excel.annotation.Excel;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @Description: 出入库明细表
 * @Author: jiangxz
 * @Date:   2020-02-13
 * @Version: V1.0
 */
@Data
@TableName("pd_stock_record_detail")
public class PdStockRecordDetail extends BaseEntity {
    private static final long serialVersionUID = 1L;
    
	/**主键*/
	@TableId(type = IdType.ID_WORKER_STR)
	private String id;
	/**出入库记录ID*/
	private String recordId;
	/**产品ID*/
	@Excel(name = "产品ID", width = 15)
	private String productId;
	/**产品条码*/
	@Excel(name = "产品条码", width = 15)
	private String productBarCode;
	/**货位ID*/
	@Excel(name = "货区ID", width = 15)
	private String huoquId;
	/**货位ID*/
	@Excel(name = "货位ID", width = 15)
	private String huoweiId;
	/**产品批号*/
	@Excel(name = "产品批号", width = 15)
	private String batchNo;
	/**产品数量（出入库数量）*/
	@Excel(name = "产品数量（出入库数量）", width = 15)
	private Double productNum;
	/**出库单价*/
	@Excel(name = "出库单价", width = 15)
	private java.math.BigDecimal sellingPrice;
	/**入库单价*/
	@Excel(name = "入库单价", width = 15)
	private java.math.BigDecimal purchasePrice;
	/**有效期*/
	@Excel(name = "有效期", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
	private Date limitDate;
	/**定数包ID*/
	@Excel(name = "定数包ID", width = 15)
	private String packageId;
	/**高低值耗材标志 1-高值；2-低值*/
	@Excel(name = "高低值耗材标志 1-高值；2-低值", width = 15)
	private String highLowSupplies;
	/**导入单号*/
	@Excel(name = "导入单号", width = 15)
	private String importNo;
	/**备注*/
	@Excel(name = "备注", width = 15)
	private String remarks;
	/**删除标记（0：正常；1：删除）*/
//	@Excel(name = "删除标记（0：正常；1：删除）", width = 15)
//	private String delFlag;
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
	/**所属父部门*/
	@Excel(name = "所属父部门", width = 15)
	private String sysOrgParentCode;


    /*不是明细表字段*/
	@TableField(exist = false)
	private String productName;//产品名称
	@TableField(exist = false)
	private String number;//产品编号
	@TableField(exist = false)
	private String spec;//产品规格
	@TableField(exist = false)
	private String version;//产品型号
	@TableField(exist = false)
	private String unitName;//单位名称
	@TableField(exist = false)
	private String venderName;//生产厂家名称
	@TableField(exist = false)
	private String supplierName;//供应商名称
	@TableField(exist = false)
	private String registration;//注册证号
	@TableField(exist = false)
	private String inDeptName;//入库科室名称
	@TableField(exist = false)
	private String outDeptName;//出库科室名称
	@TableField(exist = false)
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private Date recordDate;//出入库时间
	@TableField(exist = false)
	private String recordType;//出入库类型
	@TableField(exist = false)
	private BigDecimal pdOutTotalPrice;//出库金额
	@TableField(exist = false)
	private  BigDecimal pdTotalPrice;//入库金额
	@TableField(exist = false)
	private String recordState;//记录状态
	@TableField(exist = false)
	private String deptId;//科室ID
	@TableField(exist = false)
	private String recordNo;//出入库单号
	@TableField(exist = false)
	private String realname;//操作人名称
	@TableField(exist = false)
	private String outType;//出库类型
	@TableField(exist = false)
	private String inType;//入库类型

}
