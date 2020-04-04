package org.jeecg.modules.pd.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * @Description: 供应商
 * @Author: zxh
 * @Date:   2020-01-09
 * @Version: V1.0
 */
@Data
@TableName("pd_supplier")
public class PdSupplier extends BaseEntity {
    private static final long serialVersionUID = 1L;

	/**主键*/
	@TableId(type = IdType.ID_WORKER_STR)
    private String id;
	/**名称*/
	@TableField(value="`name`")
	@Excel(name = "名称", width = 15)
    private String name;
	/**拼音简码*/
	@Excel(name = "拼音简码", width = 15)
    private String py;
	/**五笔简码*/
	@Excel(name = "五笔简码", width = 15)
    private String wb;
	/**自定义码*/
	@Excel(name = "自定义码", width = 15)
    private String zdy;
	/**证照名称0*/
	private String licenceName0;
	/**证照号码0*/
	private String licenceNum0;
	/**证照有效期0*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
	@DateTimeFormat(pattern="yyyy-MM-dd")
	private Date licenceDate0;
	/**证照地址0*/
	private String licenceSite0;
	/**是否过期标识，0未过期，1已过期，2近效期*/
	private String licenceValidity0;
	/**证照名称1*/
    private String licenceName1;
	/**证照号码1*/
    private String licenceNum1;
	/**证照有效期1*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private Date licenceDate1;
	/**证照地址1*/
    private String licenceSite1;
	/**是否过期标识，0未过期，1已过期，2近效期*/
    private String licenceValidity1;
	/**证照名称2*/
    private String licenceName2;
	/**证照号码2*/
    private String licenceNum2;
	/**证照有效期2*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private Date licenceDate2;
	/**证照地址2*/
    private String licenceSite2;
	/**是否过期标识，0未过期，1已过期，2近效期*/
    private String licenceValidity2;
	/**证照名称3*/
    private String licenceName3;
	/**证照号码3*/
    private String licenceNum3;
	/**证照有效期3*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private Date licenceDate3;
	/**证照地址3*/
    private String licenceSite3;
	/**是否过期标识，0未过期，1已过期，2近效期*/
    private String licenceValidity3;
	/**证照名称4*/
    private String licenceName4;
	/**证照号码4*/
    private String licenceNum4;
	/**证照有效期4*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private Date licenceDate4;
	/**证照地址4*/
    private String licenceSite4;
	/**是否过期标识，0未过期，1已过期，2近效期*/
    private String licenceValidity4;
	/**证照名称5*/
    private String licenceName5;
	/**证照号码5*/
    private String licenceNum5;
	/**证照有效期5*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private Date licenceDate5;
	/**证照地址5*/
    private String licenceSite5;
	/**是否过期标识，0未过期，1已过期，2近效期*/
    private String licenceValidity5;
	/**证照名称6*/
    private String licenceName6;
	/**证照号码6*/
    private String licenceNum6;
	/**证照有效期6*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private Date licenceDate6;
	/**证照地址6*/
    private String licenceSite6;
	/**是否过期标识，0未过期，1已过期，2近效期*/
    private String licenceValidity6;
	/**证照名称7*/
    private String licenceName7;
	/**证照号码7*/
    private String licenceNum7;
	/**证照有效期7*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private Date licenceDate7;
	/**证照地址7*/
    private String licenceSite7;
	/**是否过期标识，0未过期，1已过期，2近效期*/
    private String licenceValidity7;
	/**证照名称8*/
    private String licenceName8;
	/**证照号码8*/
    private String licenceNum8;
	/**证照有效期8*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private Date licenceDate8;
	/**证照地址8*/
    private String licenceSite8;
	/** 是否过期标识，0未过期，1已过期，2近效期*/
    private String licenceValidity8;
	/**证照名称9*/
    private String licenceName9;
	/**证照号码9*/
    private String licenceNum9;
	/**证照有效期9*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private Date licenceDate9;
	/**证照地址9*/
    private String licenceSite9;
	/**是否过期标识，0未过期，1已过期，2近效期*/
    private String licenceValidity9;
	/**证照名称10*/
    private String licenceName10;
	/**证照号码10*/
    private String licenceNum10;
	/**证照有效期10*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private Date licenceDate10;
	/**证照地址10*/
    private String licenceSite10;
	/**是否过期标识，0未过期，1已过期，2近效期*/
    private String licenceValidity10;
	/**证照名称11*/
    private String licenceName11;
	/**证照号码11*/
    private String licenceNum11;
	/**证照有效期11*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private Date licenceDate11;
	/**证照地址11*/
    private String licenceSite11;
	/**是否过期标识，0未过期，1已过期，2近效期*/
    private String licenceValidity11;
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
	/**是否过期标识，0未过期，1已过期，2近效期*/
    private String validityFlag;
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
