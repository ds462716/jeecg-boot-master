package org.jeecg.modules.pd.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;
import org.jeecgframework.poi.excel.annotation.Excel;
import java.util.Date;

/**
 * @Description: 定数包明细
 * @Author: jiangxz
 * @Date:   2020-02-02
 * @Version: V1.0
 */
@Data
@TableName("pd_package_detail")
public class PdPackageDetail extends BaseEntity {
    private static final long serialVersionUID = 1L;
    
	/**主键*/
	@TableId(type = IdType.ID_WORKER_STR)
	private String id;
	/**定数包id*/
	@Excel(name = "定数包id", width = 15)
	private String packageId;
	/**产品id*/
	private String productId;
	/**产品数量*/
	@Excel(name = "产品数量", width = 15)
	private Integer count;
	/**备注*/
	@Excel(name = "备注", width = 15)
	private String remarks;
	/**删除标识，0-正常；1-删除*/
	@Excel(name = "删除标识，0-正常；1-删除", width = 15)
	private String delFlag;
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
	/**父机构*/
	@Excel(name = "父机构", width = 15)
	private String sysOrgParentCode;

}
