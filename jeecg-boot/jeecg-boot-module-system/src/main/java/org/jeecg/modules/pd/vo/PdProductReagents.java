package org.jeecg.modules.pd.vo;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.jeecg.modules.pd.entity.BaseEntity;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.List;

/**
 * @Description: pd_product
 * @Author: zxh
 * @Date:   2020-02-05
 * @Version: V1.0
 */
@Data
@TableName("pd_product")
public class PdProductReagents extends BaseEntity {
    private static final long serialVersionUID = 1L;

	/**主键*/
	@TableId(type = IdType.ID_WORKER_STR)
    private String id;
	/**产品名称*/
	@Excel(name = "产品名称", width = 15)
    private String name;
	/**产品编号*/
	@Excel(name = "产品编号", width = 15)
	private String number;
	/**拼音简码*/
	@Excel(name = "拼音简码", width = 15)
    private String py;
	/**五笔简码*/
	@Excel(name = "五笔简码", width = 15)
    private String wb;
	/**产品别名*/
	@Excel(name = "产品别名", width = 15)
    private String bname;
	/**别名拼音简码*/
	@Excel(name = "别名拼音简码", width = 15)
    private String bpy;
	/**别名五笔简码*/
	@Excel(name = "别名五笔简码", width = 15)
    private String bwb;
	/**自定义简码*/
	@Excel(name = "自定义简码", width = 15)
    private String zdy;
	/**规格*/

    private String specUnitId;
    @TableField(exist = false)
	@Excel(name = "规格单位", width = 15)
    private String specUnitName;
	/**规格*/
	@Excel(name = "规格数量", width = 15)
    private Double specQuantity;
	/**规格*/
	@Excel(name = "规格", width = 15)
    private String spec;
	/**型号*/
	@Excel(name = "型号", width = 15)
    private String version;
	/**单位*/
    private String unitId;
	@TableField(exist = false)
	@Excel(name = "单位", width = 15)
	private String unitName;
	/**产品权限*/
	@Excel(name = "产品权限", width = 15)
	private String power;
	/**一级分类*/
    private String categoryOne;
	@TableField(exist = false)
	private String categoryOneName;
	/**二级分类*/
    private String categoryTwo;
	@TableField(exist = false)
	private String categoryTwoName;
	/**产品组别*/
    private String groupId;
	@TableField(exist = false)
	private String groupName;
	/**生产厂家*/
    private String venderId;
	@TableField(exist = false)
	@Excel(name = "生产厂家", width = 15)
	private String venderName;
	/**是否计费*/
	@Excel(name = "是否计费", width = 15)
    private String isCharge;
	/**供应商*/
    private String supplierId;
	@TableField(exist = false)
	@Excel(name = "供应商", width = 15)
	private String supplierName;
	/**进价*/
	@Excel(name = "进价", width = 15)
    private java.math.BigDecimal purchasePrice;
	/**出价*/
	@Excel(name = "出价", width = 15)
    private java.math.BigDecimal sellingPrice;
	/**注册证*/
	@Excel(name = "注册证", width = 15)
    private String registration;
	/**描述*/
	@Excel(name = "描述", width = 15)
    private String description;
	/**证照名称*/
    private String licenceName0;
	/**证照号码*/
    private String licenceNum0;
	/**证照号码*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private java.util.Date licenceDate0;
	/**证照地址*/
    private String licenceSite0;
	/**是否过期标识，0未过期，1已过期，2近效期*/
    private String licenceValidity0;
	/**产品授权书*/
    private String licenceName1;
	/**证照号码*/
    private String licenceNum1;
	/**有效期*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private java.util.Date licenceDate1;
	/**证照地址*/
    private String licenceSite1;
	/**是否过期标识，0未过期，1已过期，2近效期*/
    private String licenceValidity1;
	/**证照名称*/
    private String licenceName2;
	/**证照号码*/
    private String licenceNum2;
	/**有效期*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private java.util.Date licenceDate2;
	/**证照地址*/
    private String licenceSite2;
	/**是否过期标识，0未过期，1已过期，2近效期*/
    private String licenceValidity2;
	/**证照名称*/
    private String licenceName3;
	/**证照号码*/
    private String licenceNum3;
	/**证照有效期*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private java.util.Date licenceDate3;
	/**证照地址*/
    private String licenceSite3;
	/**是否过期标识，0未过期，1已过期，2近效期*/
    private String licenceValidity3;
	/**证照名称*/
    private String licenceName4;
	/**证照号码*/
    private String licenceNum4;
	/**证照有效期*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private java.util.Date licenceDate4;
	/**证照地址*/
    private String licenceSite4;
	/**是否过期标识，0未过期，1已过期，2近效期*/
    private String licenceValidity4;
	/**证照名称*/
    private String licenceName5;
	/**证照号码*/
    private String licenceNum5;
	/**证照有效期*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private java.util.Date licenceDate5;
	/**证照地址5*/
    private String licenceSite5;
	/**是否过期标识，0未过期，1已过期，2近效期*/
    private String licenceValidity5;
	/**证照名称*/
    private String licenceName6;
	/**证照号码*/
    private String licenceNum6;
	/**证照有效期*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private java.util.Date licenceDate6;
	/**证照地址*/
    private String licenceSite6;
	/**是否过期标识，0未过期，1已过期，2近效期*/
    private String licenceValidity6;
	/**证照名称*/
    private String licenceName7;
	/**证照号码*/
    private String licenceNum7;
	/**证照有效期*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private java.util.Date licenceDate7;
	/**证照地址*/
    private String licenceSite7;
	/**是否过期标识，0未过期，1已过期，2近效期*/
    private String licenceValidity7;
	/**证照名称*/
    private String licenceName8;
	/**证照号码*/
    private String licenceNum8;
	/**证照有效期*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private java.util.Date licenceDate8;
	/**证照地址*/
    private String licenceSite8;
	/** 是否过期标识，0未过期，1已过期，2近效期*/
    private String licenceValidity8;
	/**证照名称*/
    private String licenceName9;
	/**证照号码*/
    private String licenceNum9;
	/**证照有效期*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private java.util.Date licenceDate9;
	/**证照地址*/
    private String licenceSite9;
	/**是否过期标识，0未过期，1已过期，2近效期*/
    private String licenceValidity9;
	/**证照名称*/
    private String licenceName10;
	/**证照号码*/
    private String licenceNum10;
	/**证照有效期*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private java.util.Date licenceDate10;
	/**证照地址*/
    private String licenceSite10;
	/**是否过期标识，0未过期，1已过期，2近效期*/
    private String licenceValidity10;
	/**证照名称*/
	private String licenceName11;
	/**证照号码*/
	private String licenceNum11;
	/**证照有效期*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
	@DateTimeFormat(pattern="yyyy-MM-dd")
	private java.util.Date licenceDate11;
	/**证照地址*/
	private String licenceSite11;
	/**是否过期标识，0未过期，1已过期，2近效期*/
	private String licenceValidity11;
	/**创建人*/
    private String createBy;
	/**创建日期*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private java.util.Date createTime;
	/**更新人*/
    private String updateBy;
	/**更新日期*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private java.util.Date updateTime;
	@Excel(name = "备注", width = 15)
	private String remarks;
	/**所属部门*/
    private String sysOrgCode;
	/**是否过期标识，0产品，1产品*/
	private String productFlag;
	/**是否过期标识，0未过期，1已过期，2近效期*/
    private String validityFlag;
	/**
	 * 消息发送状态，近效期1，已过期2，初始值为0
	 */
	private String msgSendState;
	/**产品收费代码*/
	@Excel(name = "产品收费代码", width = 15)
    private String chargeCode;
	/**
	 * 是否是紧急产品
	 */
	@Excel(name = "是否紧急产品", width = 15)
	private String isUrgent;

	/**
	 * 紧急产品需要采购数量
	 */
	private Double upQuantity;

	/**
	 * 紧急产品已采购数量
	 */
	private Double purchasedQuantity;

    /**
     * 器械分类（0,1,2,3）
	 */
    @Excel(name = "器械分类", width = 15)
	private String deviceClassification;

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

	/**
	 * 产品关联的编码规则
	 */
	@TableField(exist = false)
	private List<String> pdProductRules;

	/**
	 * 库存科室Id
	 */
	@TableField(exist = false)
	private  String  stockDepartId;

	/**
	 * jde编码
	 */
	private String jdeCode;
}
