package org.jeecg.modules.pd.entity;

import java.io.Serializable;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;
import org.jeecgframework.poi.excel.annotation.Excel;
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
	@Excel(name = "货位ID", width = 15)
	private String goodsAllocationId;
	/**产品批号*/
	@Excel(name = "产品批号", width = 15)
	private String batchNo;
	/**产品数量（出入库数量）*/
	@Excel(name = "产品数量（出入库数量）", width = 15)
	private Double productNum;
	/**出库单价*/
	@Excel(name = "出库单价", width = 15)
	private java.math.BigDecimal outPrice;
	/**入库单价*/
	@Excel(name = "入库单价", width = 15)
	private java.math.BigDecimal inPrice;
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
}
