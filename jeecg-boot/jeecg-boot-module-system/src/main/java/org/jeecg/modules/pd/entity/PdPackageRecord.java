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
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @Description: pd_package_record
 * @Author: jiangxz
 * @Date:   2020-03-04
 * @Version: V1.0
 */
@Data
@TableName("pd_package_record")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="pd_package_record对象", description="pd_package_record")
public class PdPackageRecord extends BaseEntity {
    private static final long serialVersionUID = 1L;

	/**id*/
	@TableId(type = IdType.ID_WORKER_STR)
    @ApiModelProperty(value = "id")
    private String id;
	/**定数包ID*/
	@Excel(name = "定数包ID", width = 15)
    @ApiModelProperty(value = "定数包ID")
    private String packageId;
	/**定数包名称*/
	@Excel(name = "产品ID", width = 15)
    @ApiModelProperty(value = "产品ID")
    private String productId;
	/**库存ID*/
	@Excel(name = "库存ID", width = 15)
    @ApiModelProperty(value = "库存ID")
    private String stockId;
	/**定数包流水码*/
	@Excel(name = "定数包流水码", width = 15)
    @ApiModelProperty(value = "定数包流水码")
    private String packageBarCode;
	/**批号*/
	@Excel(name = "批号", width = 15)
    @ApiModelProperty(value = "批号")
    private String batchNo;
	/**有效期*/
	@Excel(name = "有效期", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "有效期")
    private Date expDate;
	/**打包数量*/
	@Excel(name = "打包数量", width = 15)
    @ApiModelProperty(value = "打包数量")
    private java.lang.Double packageNum;
	/**打包时间*/
	@Excel(name = "打包时间", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "打包时间")
    private Date packageTime;
	/**打包人*/
	@Excel(name = "打包人", width = 15)
    @ApiModelProperty(value = "打包人")
    private String packageBy;
	/**备注*/
	@Excel(name = "备注", width = 15)
    @ApiModelProperty(value = "备注")
    private String remarks;
	/**创建人*/
	@Excel(name = "创建人", width = 15)
    @ApiModelProperty(value = "创建人")
    private String createBy;
	/**创建日期*/
	@Excel(name = "创建日期", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "创建日期")
    private Date createTime;
	/**更新人*/
	@Excel(name = "更新人", width = 15)
    @ApiModelProperty(value = "更新人")
    private String updateBy;
	/**更新日期*/
	@Excel(name = "更新日期", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "更新日期")
    private Date updateTime;
	/**所属部门*/
	@Excel(name = "所属部门", width = 15)
    @ApiModelProperty(value = "所属部门")
    private String sysOrgCode;
	/**所属父部门*/
	@Excel(name = "所属父部门", width = 15)
    @ApiModelProperty(value = "所属父部门")
    private String departParentId;
	/**所属部门*/
	@Excel(name = "所属部门", width = 15)
    @ApiModelProperty(value = "所属部门")
    private String departId;
}
