package org.jeecg.modules.pd.entity;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.jeecg.common.aspect.annotation.Dict;

/**
 * @Description: pd_product
 * @Author: zxh
 * @Date:   2020-02-05
 * @Version: V1.0
 */
@Data
@TableName("pd_product")
public class PdProduct  extends BaseEntity {
    private static final long serialVersionUID = 1L;
    
	/**主键*/
	@TableId(type = IdType.ID_WORKER_STR)
    private java.lang.String id;
	/**产品编号*/
	@Excel(name = "产品编号", width = 15)
    private java.lang.String number;
	/**产品名称*/
	@Excel(name = "产品名称", width = 15)
    private java.lang.String name;
	/**拼音简码*/
	@Excel(name = "拼音简码", width = 15)
    private java.lang.String py;
	/**五笔简码*/
	@Excel(name = "五笔简码", width = 15)
    private java.lang.String wb;
	/**产品别名*/
	@Excel(name = "产品别名", width = 15)
    private java.lang.String bname;
	/**别名拼音简码*/
	@Excel(name = "别名拼音简码", width = 15)
    private java.lang.String bpy;
	/**别名五笔简码*/
	@Excel(name = "别名五笔简码", width = 15)
    private java.lang.String bwb;
	/**自定义简码*/
	@Excel(name = "自定义简码", width = 15)
    private java.lang.String zdy;
	/**规格*/
	@Excel(name = "规格", width = 15)
    private java.lang.String spec;
	/**型号*/
	@Excel(name = "型号", width = 15)
    private java.lang.String version;
	/**单位*/
	@Excel(name = "单位", width = 15)
    private java.lang.String unitId;
	@TableField(exist = false)
	private java.lang.String unitName;
	/**一级分类*/
	@Excel(name = "一级分类", width = 15)
    private java.lang.String categoryOne;
	@TableField(exist = false)
	private java.lang.String categoryOneName;
	/**二级分类*/
	@Excel(name = "二级分类", width = 15)
    private java.lang.String categoryTwo;
	@TableField(exist = false)
	private java.lang.String categoryTwoName;
	/**产品组别*/
	@Excel(name = "产品组别", width = 15)
    private java.lang.String groupId;
	@TableField(exist = false)
	private java.lang.String groupName;
	/**生产厂家*/
	@Excel(name = "生产厂家", width = 15)
    private java.lang.String venderId;
	@TableField(exist = false)
	private java.lang.String venderName;
	/**是否计费*/
	@Excel(name = "是否计费", width = 15)
    private java.lang.String isCharge;
	/**供应商*/
	@Excel(name = "供应商", width = 15)
    private java.lang.String supplierId;
	@TableField(exist = false)
	private java.lang.String supplierName;
	/**进价*/
	@Excel(name = "进价", width = 15)
    private java.math.BigDecimal purchasePrice;
	/**出价*/
	@Excel(name = "出价", width = 15)
    private java.math.BigDecimal sellingPrice;
	/**注册证*/
	@Excel(name = "注册证", width = 15)
    private java.lang.String registration;
	/**描述*/
	@Excel(name = "描述", width = 15)
    private java.lang.String description;
	/**证照名称*/
	@Excel(name = "证照名称", width = 15)
    private java.lang.String licenceName0;
	/**证照号码*/
	@Excel(name = "证照号码", width = 15)
    private java.lang.String licenceNum0;
	/**证照号码*/
	@Excel(name = "证照号码", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private java.util.Date licenceDate0;
	/**证照地址*/
	@Excel(name = "证照地址", width = 15)
    private java.lang.String licenceSite0;
	/**是否过期标识，0未过期，1已过期，2近效期*/
	@Excel(name = "是否过期标识，0未过期，1已过期，2近效期", width = 15)
    private java.lang.String licenceValidity0;
	/**产品授权书*/
	@Excel(name = "产品授权书", width = 15)
    private java.lang.String licenceName1;
	/**证照号码*/
	@Excel(name = "证照号码", width = 15)
    private java.lang.String licenceNum1;
	/**有效期*/
	@Excel(name = "有效期", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private java.util.Date licenceDate1;
	/**证照地址*/
	@Excel(name = "证照地址", width = 15)
    private java.lang.String licenceSite1;
	/**是否过期标识，0未过期，1已过期，2近效期*/
	@Excel(name = "是否过期标识，0未过期，1已过期，2近效期", width = 15)
    private java.lang.String licenceValidity1;
	/**证照名称*/
	@Excel(name = "证照名称", width = 15)
    private java.lang.String licenceName2;
	/**证照号码*/
	@Excel(name = "证照号码", width = 15)
    private java.lang.String licenceNum2;
	/**有效期*/
	@Excel(name = "有效期", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private java.util.Date licenceDate2;
	/**证照地址*/
	@Excel(name = "证照地址", width = 15)
    private java.lang.String licenceSite2;
	/**是否过期标识，0未过期，1已过期，2近效期*/
	@Excel(name = "是否过期标识，0未过期，1已过期，2近效期", width = 15)
    private java.lang.String licenceValidity2;
	/**证照名称*/
	@Excel(name = "证照名称", width = 15)
    private java.lang.String licenceName3;
	/**证照号码*/
	@Excel(name = "证照号码", width = 15)
    private java.lang.String licenceNum3;
	/**证照有效期*/
	@Excel(name = "证照有效期", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private java.util.Date licenceDate3;
	/**证照地址*/
	@Excel(name = "证照地址", width = 15)
    private java.lang.String licenceSite3;
	/**是否过期标识，0未过期，1已过期，2近效期*/
	@Excel(name = "是否过期标识，0未过期，1已过期，2近效期", width = 15)
    private java.lang.String licenceValidity3;
	/**证照名称*/
	@Excel(name = "证照名称", width = 15)
    private java.lang.String licenceName4;
	/**证照号码*/
	@Excel(name = "证照号码", width = 15)
    private java.lang.String licenceNum4;
	/**证照有效期*/
	@Excel(name = "证照有效期", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private java.util.Date licenceDate4;
	/**证照地址*/
	@Excel(name = "证照地址", width = 15)
    private java.lang.String licenceSite4;
	/**是否过期标识，0未过期，1已过期，2近效期*/
	@Excel(name = "是否过期标识，0未过期，1已过期，2近效期", width = 15)
    private java.lang.String licenceValidity4;
	/**证照名称*/
	@Excel(name = "证照名称", width = 15)
    private java.lang.String licenceName5;
	/**证照号码*/
	@Excel(name = "证照号码", width = 15)
    private java.lang.String licenceNum5;
	/**证照有效期*/
	@Excel(name = "证照有效期", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private java.util.Date licenceDate5;
	/**证照地址5*/
	@Excel(name = "证照地址5", width = 15)
    private java.lang.String licenceSite5;
	/**是否过期标识，0未过期，1已过期，2近效期*/
	@Excel(name = "是否过期标识，0未过期，1已过期，2近效期", width = 15)
    private java.lang.String licenceValidity5;
	/**证照名称*/
	@Excel(name = "证照名称", width = 15)
    private java.lang.String licenceName6;
	/**证照号码*/
	@Excel(name = "证照号码", width = 15)
    private java.lang.String licenceNum6;
	/**证照有效期*/
	@Excel(name = "证照有效期", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private java.util.Date licenceDate6;
	/**证照地址*/
	@Excel(name = "证照地址", width = 15)
    private java.lang.String licenceSite6;
	/**是否过期标识，0未过期，1已过期，2近效期*/
	@Excel(name = "是否过期标识，0未过期，1已过期，2近效期", width = 15)
    private java.lang.String licenceValidity6;
	/**证照名称*/
	@Excel(name = "证照名称", width = 15)
    private java.lang.String licenceName7;
	/**证照号码*/
	@Excel(name = "证照号码", width = 15)
    private java.lang.String licenceNum7;
	/**证照有效期*/
	@Excel(name = "证照有效期", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private java.util.Date licenceDate7;
	/**证照地址*/
	@Excel(name = "证照地址", width = 15)
    private java.lang.String licenceSite7;
	/**是否过期标识，0未过期，1已过期，2近效期*/
	@Excel(name = "是否过期标识，0未过期，1已过期，2近效期", width = 15)
    private java.lang.String licenceValidity7;
	/**证照名称*/
	@Excel(name = "证照名称", width = 15)
    private java.lang.String licenceName8;
	/**证照号码*/
	@Excel(name = "证照号码", width = 15)
    private java.lang.String licenceNum8;
	/**证照有效期*/
	@Excel(name = "证照有效期", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private java.util.Date licenceDate8;
	/**证照地址*/
	@Excel(name = "证照地址", width = 15)
    private java.lang.String licenceSite8;
	/** 是否过期标识，0未过期，1已过期，2近效期*/
	@Excel(name = " 是否过期标识，0未过期，1已过期，2近效期", width = 15)
    private java.lang.String licenceValidity8;
	/**证照名称*/
	@Excel(name = "证照名称", width = 15)
    private java.lang.String licenceName9;
	/**证照号码*/
	@Excel(name = "证照号码", width = 15)
    private java.lang.String licenceNum9;
	/**证照有效期*/
	@Excel(name = "证照有效期", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private java.util.Date licenceDate9;
	/**证照地址*/
	@Excel(name = "证照地址", width = 15)
    private java.lang.String licenceSite9;
	/**是否过期标识，0未过期，1已过期，2近效期*/
	@Excel(name = "是否过期标识，0未过期，1已过期，2近效期", width = 15)
    private java.lang.String licenceValidity9;
	/**证照名称*/
	@Excel(name = "证照名称", width = 15)
    private java.lang.String licenceName10;
	/**证照号码*/
	@Excel(name = "证照号码", width = 15)
    private java.lang.String licenceNum10;
	/**证照有效期*/
	@Excel(name = "证照有效期", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private java.util.Date licenceDate10;
	/**证照地址*/
	@Excel(name = "证照地址", width = 15)
    private java.lang.String licenceSite10;
	/**是否过期标识，0未过期，1已过期，2近效期*/
	@Excel(name = "是否过期标识，0未过期，1已过期，2近效期", width = 15)
    private java.lang.String licenceValidity10;
	/**创建人*/
	@Excel(name = "创建人", width = 15)
    private java.lang.String createBy;
	/**创建日期*/
	@Excel(name = "创建日期", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private java.util.Date createTime;
	/**更新人*/
	@Excel(name = "更新人", width = 15)
    private java.lang.String updateBy;
	/**更新日期*/
	@Excel(name = "更新日期", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private java.util.Date updateTime;
	/**所属部门*/
	@Excel(name = "所属部门", width = 15)
    private java.lang.String sysOrgCode;
	/**是否过期标识，0未过期，1已过期，2近效期*/
	@Excel(name = "是否过期标识，0未过期，1已过期，2近效期", width = 15)
    private java.lang.String validityFlag;
}
