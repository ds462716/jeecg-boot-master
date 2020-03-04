package org.jeecg.modules.pd.entity;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;
import org.jeecgframework.poi.excel.annotation.Excel;

/**
 * @Description: 产品单位表
 * @Author: jeecg-boot
 * @Date:   2020-01-08
 * @Version: V1.0
 */
@Data
@TableName("pd_unit")
public class PdUnit  extends BaseEntity {
    private static final long serialVersionUID = 1L;
    
	/**主键*/
	@TableId(type = IdType.ID_WORKER_STR)
    private String id;
	/**单位名称*/
	@Excel(name = "单位名称", width = 15)
    private String name;
	/**拼音简码*/
	@Excel(name = "拼音简码", width = 15)
    private String py;
	/**五笔码*/
	@Excel(name = "五笔码", width = 15)
    private String wb;
	/**自定义码*/
	@Excel(name = "自定义码", width = 15)
    private String zdy;
	/**删除标识，0-正常；1-删除*/
//	@Excel(name = "删除标识，0-正常；1-删除", width = 15)
//    private String delFlag;
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

}
