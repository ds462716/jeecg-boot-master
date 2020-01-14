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
 * @Description: 应用标识符表
 * @Author: zxh
 * @Date:   2019-12-25
 * @Version: V1.0
 */
@Data
@TableName("pd_encoding_identifier")
public class PdEncodingIdentifier extends BaseEntity {
    private static final long serialVersionUID = 1L;
    
	/**主键*/
	@TableId(type = IdType.ID_WORKER_STR)
    private String id;
	/**值*/
	@Excel(name = "值", width = 15)
    private String value;
	/**含义*/
	@Excel(name = "含义", width = 15)
    private String meaning;
	/**类型*/
	@Excel(name = "类型", width = 15)
    private String type;
	/**长度*/
	@TableField(strategy = FieldStrategy.IGNORED)//修改更新null字段
	@Excel(name = "长度", width = 15)
    private Integer length;
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

}
