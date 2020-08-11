package org.jeecg.modules.pd.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.*;

/**
 * @Description: 套包明细
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
	/**套包id*/
	@Excel(name = "套包id", width = 15)
	private String packageId;
	/**产品id*/
	private String productId;
	/**产品数量*/
	@Excel(name = "产品数量", width = 15)
	private Double count;
	/**备注*/
	@Excel(name = "备注", width = 15)
	private String remarks;
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

    /*本科室库存数量*/
	@TableField(exist = false)
	private Double stockNum;
	/*套包编号*/
	@TableField(exist = false)
	private String packageCode;
	/*套包名称*/
	@TableField(exist = false)
	private String packageName;
	/*产品编号*/
	@TableField(exist = false)
	private String number;
	/*产品名称*/
	@TableField(exist = false)
	private String productName;
	/*产品规格*/
	@TableField(exist = false)
	private String spec;
	/*产品型号*/
	@TableField(exist = false)
	private String version;
	/*单位名称*/
	@TableField(exist = false)
	private String unitName;
	@TableField(exist = false)
	private List<String> packageIds;		//套包id集合

}
