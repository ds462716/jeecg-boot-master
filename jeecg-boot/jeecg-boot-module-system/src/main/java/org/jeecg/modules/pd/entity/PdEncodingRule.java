package org.jeecg.modules.pd.entity;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.math.BigDecimal;
import java.util.List;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.jeecg.common.aspect.annotation.Dict;

/**
 * @Description: 编码规则表
 * @Author: zxh
 * @Date:   2019-12-26
 * @Version: V1.0
 */
@Data
@TableName("pd_encoding_rule")
public class PdEncodingRule extends BaseEntity  {
    private static final long serialVersionUID = 1L;
    
	/**主键*/
	@TableId(type = IdType.ID_WORKER_STR)
    private String id;
	/**编码名称*/
	@Excel(name = "编码名称", width = 15)
    private String name;
	/**编码名称拼音简码*/
	@Excel(name = "编码名称拼音简码", width = 15)
    private String py;
	/**编码名称五笔简码*/
	@Excel(name = "编码名称五笔简码", width = 15)
    private String wb;
	/**自定义名称查询码*/
	@Excel(name = "自定义名称查询码", width = 15)
    private String zdy;
	/**规则详情*/
	@Excel(name = "规则详情", width = 15)
    private String codeDetail;
	/**规则描述*/
	@Excel(name = "规则描述", width = 15)
    private String codeDesc;
	/**规则简码 011710*/
	@Excel(name = "规则简码", width = 15)
    private String codeQuery;
	/**总位数*/
	@Excel(name = "总位数", width = 15)
    private Integer totalDigit;
	/**创建人*/
	@Excel(name = "创建人", width = 15)
    private String createBy;
	/**创建时间*/
	@Excel(name = "创建时间", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private Date createTime;
	/**修改人*/
	@Excel(name = "修改人", width = 15)
    private String updateBy;
	/**修改时间*/
	@Excel(name = "修改时间", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private Date updateTime;
	/**备注*/
	@Excel(name = "备注", width = 15)
    private String remarks;

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

	/** 规则详情*/
	/**
	 * 不是数据库字段 2020年1月3日14:50:33 zxh
	 */
	@TableField(exist = false)
    private List<PdEncodingRuleDetail> pdEncodingRuleDetails;
}
