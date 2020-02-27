package org.jeecg.modules.pd.entity;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.jeecg.common.aspect.annotation.Dict;

/**
 * @Description: 货区货位表
 * @Author: jiangxz
 * @Date:   2020-02-11
 * @Version: V1.0
 */
@Data
@TableName("pd_goods_allocation")
public class PdGoodsAllocation  extends BaseEntity {
    private static final long serialVersionUID = 1L;
    
	/**主键*/
	@TableId(type = IdType.ID_WORKER_STR)
    private String id;
	/**父ID(关联货区ID)*/
	@Excel(name = "父ID(关联货区ID)", width = 15)
    private String parentId;
	/**部门/机构ID*/
	@Excel(name = "部门/机构ID", width = 15)
    private String departId;
	/**货区/货位名称*/
	@Excel(name = "货区/货位名称", width = 15)
    private String name;
	/**货区/货位编号*/
	@Excel(name = "货区/货位编号", width = 15)
    private String code;
	/**类型 1-货区；2-货位*/
	@Excel(name = "类型 1-货区；2-货位", width = 15)
    private String areaType;
	/**地址*/
	@Excel(name = "地址", width = 15)
    private String address;
	/**面积*/
	@Excel(name = "面积", width = 15)
    private BigDecimal area;
	/**容积*/
	@Excel(name = "容积", width = 15)
    private BigDecimal volume;
	/**联系人*/
	@Excel(name = "联系人", width = 15)
    private String contacts;
	/**联系方式*/
	@Excel(name = "联系方式", width = 15)
    private String contactsPhone;
	/**下级存放单位编号标识*/
	@Excel(name = "下级存放单位编号标识", width = 15)
    private String subCode;
	/**编号后缀*/
	@Excel(name = "编号后缀", width = 15)
	private String codeSuffix;
	/**下级存放单位数量*/
	@Excel(name = "下级存放单位数量", width = 15)
    private Integer subNum;
	/**状态 0-未启用,1-启用*/
	@Excel(name = "状态 0-未启用,1-启用", width = 15)
    private String state;
	/**拼音码*/
	@Excel(name = "拼音码", width = 15)
    private String py;
	/**五笔码*/
	@Excel(name = "五笔码", width = 15)
    private String wb;
	/**自定义码*/
	@Excel(name = "自定义码", width = 15)
    private String zdy;
	/**备注*/
	@Excel(name = "备注", width = 15)
    private String remarks;
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
	/**父部门*/
	private String departParentId;
}
