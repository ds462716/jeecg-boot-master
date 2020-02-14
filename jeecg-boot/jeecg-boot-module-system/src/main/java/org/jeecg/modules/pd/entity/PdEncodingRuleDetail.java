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
 * @Description: 编码规则详情表
 * @Author: zxh
 * @Date:   2019-12-26
 * @Version: V1.0
 */
@Data
@TableName("pd_encoding_rule_detail")
public class PdEncodingRuleDetail extends BaseEntity  {
    private static final long serialVersionUID = 1L;
    
	/**主键*/
	@TableId(type = IdType.ID_WORKER_STR)
    private String id;
	/**编码规则表id*/
	@Excel(name = "编码规则表id", width = 15)
    private String codeId;
	/**应用标识符id*/
	@Excel(name = "应用标识符id", width = 15)
    private String identifier;
	/**长度*/
	@Excel(name = "长度", width = 15)
    private Integer length;
	/**顺序*/
	@Excel(name = "顺序", width = 15)
    private String codeOrder;
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

	/**标识符内容*/
	@TableField(exist = false)
	private String value;

	/**标识符类型*/
	@TableField(exist = false)
	private String type;

	/**
	 * 标识符对象
	 */
	@TableField(exist = false)
	private PdEncodingIdentifier pdEncodingIdentifier;

	//冗余
	private String Meaning;//标识符的含义
	private String size;//标识符固定长度

}
