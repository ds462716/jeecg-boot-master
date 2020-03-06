package org.jeecg.modules.pd.entity;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.math.BigDecimal;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.jeecg.common.aspect.annotation.Dict;

/**
 * @Description: 生产厂家
 * @Author: zxh
 * @Date:   2020-01-09
 * @Version: V1.0
 */
@Data
@TableName("pd_vender")
public class PdVender  extends BaseEntity {
    private static final long serialVersionUID = 1L;
    
	/**主键*/
	@TableId(type = IdType.ID_WORKER_STR)
    private java.lang.String id;
	/**名称*/
	@Excel(name = "名称", width = 15)
    private java.lang.String name;
	/**拼音简码*/
	@Excel(name = "拼音简码", width = 15)
    private java.lang.String py;
	/**五笔简码*/
	@Excel(name = "五笔简码", width = 15)
    private java.lang.String wb;
	/**自定义码*/
	@Excel(name = "自定义码", width = 15)
    private java.lang.String zdy;
	/**证照名称0*/
	@Excel(name = "证照名称0", width = 15)
	private java.lang.String licenceName0;
	/**证照号码0*/
	@Excel(name = "证照号码0", width = 15)
	private java.lang.String licenceNum0;
	/**证照有效期0*/
	@Excel(name = "证照有效期0", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
	@DateTimeFormat(pattern="yyyy-MM-dd")
	private java.util.Date licenceDate0;
	/**证照地址0*/
	@Excel(name = "证照地址0", width = 15)
	private java.lang.String licenceSite0;
	/**是否过期标识，0未过期，1已过期，2近效期*/
	@Excel(name = "是否过期标识，0未过期，1已过期，2近效期", width = 15)
	private java.lang.String licenceValidity0;
	/**证照名称1*/
	@Excel(name = "证照名称1", width = 15)
    private java.lang.String licenceName1;
	/**证照号码1*/
	@Excel(name = "证照号码1", width = 15)
    private java.lang.String licenceNum1;
	/**证照有效期1*/
	@Excel(name = "证照有效期1", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private java.util.Date licenceDate1;
	/**证照地址1*/
	@Excel(name = "证照地址1", width = 15)
    private java.lang.String licenceSite1;
	/**是否过期标识，0未过期，1已过期，2近效期*/
	@Excel(name = "是否过期标识，0未过期，1已过期，2近效期", width = 15)
    private java.lang.String licenceValidity1;
	/**证照名称2*/
	@Excel(name = "证照名称2", width = 15)
    private java.lang.String licenceName2;
	/**证照号码2*/
	@Excel(name = "证照号码2", width = 15)
    private java.lang.String licenceNum2;
	/**证照有效期2*/
	@Excel(name = "证照有效期2", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private java.util.Date licenceDate2;
	/**证照地址2*/
	@Excel(name = "证照地址2", width = 15)
    private java.lang.String licenceSite2;
	/**是否过期标识，0未过期，1已过期，2近效期*/
	@Excel(name = "是否过期标识，0未过期，1已过期，2近效期", width = 15)
    private java.lang.String licenceValidity2;
	/**证照名称3*/
	@Excel(name = "证照名称3", width = 15)
    private java.lang.String licenceName3;
	/**证照号码3*/
	@Excel(name = "证照号码3", width = 15)
    private java.lang.String licenceNum3;
	/**证照有效期3*/
	@Excel(name = "证照有效期3", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private java.util.Date licenceDate3;
	/**证照地址3*/
	@Excel(name = "证照地址3", width = 15)
    private java.lang.String licenceSite3;
	/**是否过期标识，0未过期，1已过期，2近效期*/
	@Excel(name = "是否过期标识，0未过期，1已过期，2近效期", width = 15)
    private java.lang.String licenceValidity3;
	/**证照名称4*/
	@Excel(name = "证照名称4", width = 15)
    private java.lang.String licenceName4;
	/**证照号码4*/
	@Excel(name = "证照号码4", width = 15)
    private java.lang.String licenceNum4;
	/**证照有效期4*/
	@Excel(name = "证照有效期4", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private java.util.Date licenceDate4;
	/**证照地址4*/
	@Excel(name = "证照地址4", width = 15)
    private java.lang.String licenceSite4;
	/**是否过期标识，0未过期，1已过期，2近效期*/
	@Excel(name = "是否过期标识，0未过期，1已过期，2近效期", width = 15)
    private java.lang.String licenceValidity4;
	/**证照名称5*/
	@Excel(name = "证照名称5", width = 15)
    private java.lang.String licenceName5;
	/**证照号码5*/
	@Excel(name = "证照号码5", width = 15)
    private java.lang.String licenceNum5;
	/**证照有效期5*/
	@Excel(name = "证照有效期5", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private java.util.Date licenceDate5;
	/**证照地址5*/
	@Excel(name = "证照地址5", width = 15)
    private java.lang.String licenceSite5;
	/**是否过期标识，0未过期，1已过期，2近效期*/
	@Excel(name = "是否过期标识，0未过期，1已过期，2近效期", width = 15)
    private java.lang.String licenceValidity5;
	/**证照名称6*/
	@Excel(name = "证照名称6", width = 15)
    private java.lang.String licenceName6;
	/**证照号码6*/
	@Excel(name = "证照号码6", width = 15)
    private java.lang.String licenceNum6;
	/**证照有效期6*/
	@Excel(name = "证照有效期6", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private java.util.Date licenceDate6;
	/**证照地址6*/
	@Excel(name = "证照地址6", width = 15)
    private java.lang.String licenceSite6;
	/**是否过期标识，0未过期，1已过期，2近效期*/
	@Excel(name = "是否过期标识，0未过期，1已过期，2近效期", width = 15)
    private java.lang.String licenceValidity6;
	/**证照名称7*/
	@Excel(name = "证照名称7", width = 15)
    private java.lang.String licenceName7;
	/**证照号码7*/
	@Excel(name = "证照号码7", width = 15)
    private java.lang.String licenceNum7;
	/**证照有效期7*/
	@Excel(name = "证照有效期7", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private java.util.Date licenceDate7;
	/**证照地址7*/
	@Excel(name = "证照地址7", width = 15)
    private java.lang.String licenceSite7;
	/**是否过期标识，0未过期，1已过期，2近效期*/
	@Excel(name = "是否过期标识，0未过期，1已过期，2近效期", width = 15)
    private java.lang.String licenceValidity7;
	/**证照名称8*/
	@Excel(name = "证照名称8", width = 15)
    private java.lang.String licenceName8;
	/**证照号码8*/
	@Excel(name = "证照号码8", width = 15)
    private java.lang.String licenceNum8;
	/**证照有效期8*/
	@Excel(name = "证照有效期8", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private java.util.Date licenceDate8;
	/**证照地址8*/
	@Excel(name = "证照地址8", width = 15)
    private java.lang.String licenceSite8;
	/** 是否过期标识，0未过期，1已过期，2近效期*/
	@Excel(name = " 是否过期标识，0未过期，1已过期，2近效期", width = 15)
    private java.lang.String licenceValidity8;
	/**证照名称9*/
	@Excel(name = "证照名称9", width = 15)
    private java.lang.String licenceName9;
	/**证照号码9*/
	@Excel(name = "证照号码9", width = 15)
    private java.lang.String licenceNum9;
	/**证照有效期9*/
	@Excel(name = "证照有效期9", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private java.util.Date licenceDate9;
	/**证照地址9*/
	@Excel(name = "证照地址9", width = 15)
    private java.lang.String licenceSite9;
	/**是否过期标识，0未过期，1已过期，2近效期*/
	@Excel(name = "是否过期标识，0未过期，1已过期，2近效期", width = 15)
    private java.lang.String licenceValidity9;
	/**证照名称10*/
	@Excel(name = "证照名称10", width = 15)
    private java.lang.String licenceName10;
	/**证照号码10*/
	@Excel(name = "证照号码10", width = 15)
    private java.lang.String licenceNum10;
	/**证照有效期10*/
	@Excel(name = "证照有效期10", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private java.util.Date licenceDate10;
	/**证照地址10*/
	@Excel(name = "证照地址10", width = 15)
    private java.lang.String licenceSite10;
	/**是否过期标识，0未过期，1已过期，2近效期*/
	@Excel(name = "是否过期标识，0未过期，1已过期，2近效期", width = 15)
    private java.lang.String licenceValidity10;
	/**证照名称11*/
	@Excel(name = "证照名称11", width = 15)
    private java.lang.String licenceName11;
	/**证照号码11*/
	@Excel(name = "证照号码11", width = 15)
    private java.lang.String licenceNum11;
	/**证照有效期11*/
	@Excel(name = "证照有效期11", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private java.util.Date licenceDate11;
	/**证照地址11*/
	@Excel(name = "证照地址11", width = 15)
    private java.lang.String licenceSite11;
	/**是否过期标识，0未过期，1已过期，2近效期*/
	@Excel(name = "是否过期标识，0未过期，1已过期，2近效期", width = 15)
    private java.lang.String licenceValidity11;
	/**创建人*/
	@Excel(name = "创建人", width = 15)
    private java.lang.String createBy;
	/**创建日期*/
	@Excel(name = "创建日期", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private java.util.Date createTime;
	/**更新人*/
	@Excel(name = "更新人", width = 15)
    private java.lang.String updateBy;
	/**更新日期*/
	@Excel(name = "更新日期", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private java.util.Date updateTime;
	/**所属部门*/
	@Excel(name = "所属部门", width = 15)
    private java.lang.String sysOrgCode;
	/**是否过期标识，0未过期，1已过期，2近效期*/
	@Excel(name = "是否过期标识，0未过期，1已过期，2近效期", width = 15)
    private java.lang.String validityFlag;
	/**
	 * 消息发送状态，近效期1，已过期2，初始值为0
	 */
	private java.lang.String msgSendState;
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
	/**备注*/
	@Excel(name = "备注", width = 15)
	private String remarks;
}
