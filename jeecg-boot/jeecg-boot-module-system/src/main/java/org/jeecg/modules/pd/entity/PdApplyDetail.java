package org.jeecg.modules.pd.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * @Description: 申领单明细表
 * @Author: jeecg-boot
 * @Date:   2020-02-11
 * @Version: V1.0
 */
@Data
@TableName("pd_apply_detail")
public class PdApplyDetail extends BaseEntity {
    private static final long serialVersionUID = 1L;
    
	/**主键*/
	@TableId(type = IdType.ID_WORKER_STR)
	private String id;
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
	/**申领单号*/
	@Excel(name = "申领单号", width = 15)
	private String applyNo;
	/**产品ID*/
	private String productId;
	/**批号*/
	private String batchNo;
	/**所属定数包ID*/
	private String packageId;
	/**定数包打包记录id*/
	private String packageRecordId;
	/**'申领时定数包产品数量'*/
	private Double packageNum;
	/**产品属性：1、产品 2、定数包*/
	private String productAttr;
	/**有效期*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
	private Date expDate;
	/**申领产品个数*/
	@Excel(name = "申领产品数量", width = 15)
	private Double applyNum;
	/**出库科室库存数量*/
	@Excel(name = "出库科室库存数量", width = 15)
	private Double stockNum;
	/**实际发货数量*/
	@Excel(name = "实际发货数量", width = 15)
	private Double arrivalNum;
	/**备注*/
	private String remarks;
	/**本科室库存数量*/
	@Excel(name = "本科室库存数量", width = 15)
	private Double currentStockNum;

	/*字段不在PdApplyDetail中**/
	@TableField(exist = false)
	@Excel(name = "产品名称", width = 15)
	private String productName;//产品名称
	@TableField(exist = false)
	@Excel(name = "产品编号", width = 15)
	private String number;//产品编号
	@TableField(exist = false)
	@Excel(name = "产品规格", width = 15)
	private String spec;//产品规格
	@TableField(exist = false)
	@Excel(name = "产品型号", width = 15)
	private String version;//产品型号
	@TableField(exist = false)
	@Excel(name = "单位名称", width = 15)
	private String unitName;//单位名称
	@TableField(exist = false)
	private String packageName;//定数包名称
	@TableField(exist = false)
	private String packageCode;//定数包编号
	@TableField(exist = false)
	private String packageBarCode;//定数包条码
	@TableField(exist = false)
	private String packageSum;//定数包产品总数


}





