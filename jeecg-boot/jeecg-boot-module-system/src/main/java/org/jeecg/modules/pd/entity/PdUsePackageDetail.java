package org.jeecg.modules.pd.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * @Description: 定数包明细
 * @Author: zxh
 * @Date:   2020年4月21日09:18:57
 * @Version: V1.0
 */
@Data
@TableName("pd_use_package_detail")
public class PdUsePackageDetail extends BaseEntity {
    private static final long serialVersionUID = 1L;
    
	/**主键*/
	@TableId(type = IdType.ID_WORKER_STR)
	private String id;
	/**检验项目id*/
	@Excel(name = "检验项目id", width = 15)
	private String packageId;
	/**产品id*/
	private String productId;
	/**产品数量*/
	@Excel(name = "产品数量", width = 15)
	private Integer count;
	/**备注*/
	@Excel(name = "备注", width = 15)
	private String remarks;
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

	/**
	 * 所属部门
	 */
	@TableField(strategy = FieldStrategy.NOT_EMPTY)
	private String departId;

	/**
	 * 所属顶级部门（医院id）
	 */
	@TableField(strategy = FieldStrategy.NOT_EMPTY)
	private String departParentId;

    /*本科室库存数量*/
	@TableField(exist = false)
	private Double stockNum;
	/*检验项目编号*/
	@TableField(exist = false)
	private String code;
	/*检验项目名称*/
	@TableField(exist = false)
	private String name;
	/*产品编号*/
	@TableField(exist = false)
	private String productNumber;
	/*产品名称*/
	@TableField(exist = false)
	private String productName;
	/*产品规格*/
	@TableField(exist = false)
	private String spec;
	/*产品型号*/
	@TableField(exist = false)
	private String version;
	/*单位名称*/
	@TableField(exist = false)
	private String unitName;

	/** 厂家名称 **/
	@TableField(exist = false)
	private String venderName;
	/** 供应商名称 **/
	@TableField(exist = false)
	private String supplierName;
	//规格单位
	@TableField(exist = false)
	private String specUnitName;
	//规格数量
	@TableField(exist = false)
	private String specQuantity;
	//产品类型
	@TableField(exist = false)
	private String productFlag;
	//产品类型
	@TableField(exist = false)
	private String productFlagName;



}
