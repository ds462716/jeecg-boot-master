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
 * @Description: 用量详情表
 * @Author: jiangxz
 * @Date:   2020-03-13
 * @Version: V1.0
 */
@Data
@TableName("pd_dosage_detail")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="pd_dosage_detail对象", description="用量详情表")
public class PdDosageDetail extends BaseEntity {
    private static final long serialVersionUID = 1L;

	/**主键*/
	@TableId(type = IdType.ID_WORKER_STR)
    @ApiModelProperty(value = "主键")
    private String id;
	/**用量表id*/
	@Excel(name = "用量表id", width = 15)
    @ApiModelProperty(value = "用量表id")
    private String dosageId;
	/**产品id*/
	@Excel(name = "产品id", width = 15)
    @ApiModelProperty(value = "产品id")
    private String productId;
	/**产品条码*/
	@Excel(name = "产品条码", width = 15)
    @ApiModelProperty(value = "产品条码")
    private String productBarCode;
	/**产品批号*/
	@Excel(name = "产品批号", width = 15)
    @ApiModelProperty(value = "产品批号")
    private String batchNo;
	/**产品有效期*/
	@Excel(name = "产品有效期", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "产品有效期")
    private Date expireDate;
	/**用量数量*/
	@Excel(name = "用量数量", width = 15)
    @ApiModelProperty(value = "用量数量")
    private Double dosageCount;
	/**添加用量时库存*/
	@Excel(name = "添加用量时库存", width = 15)
    @ApiModelProperty(value = "添加用量时库存")
    private Double stockNum;
	/**剩余可退数量*/
	@Excel(name = "剩余可退数量", width = 15)
    @ApiModelProperty(value = "剩余可退数量")
    private Double leftRefundNum;
	/**用量金额*/
	@Excel(name = "用量金额", width = 15)
    @ApiModelProperty(value = "用量金额")
    private Double amountMoney;
	/**是否执行收费*/
	@Excel(name = "是否执行收费", width = 15)
    @ApiModelProperty(value = "是否执行收费")
    private String isCharge;
	/**收费项目代码*/
	@Excel(name = "收费项目代码", width = 15)
    @ApiModelProperty(value = "收费项目代码")
    private String chargeCode;
	/**创建人*/
	@Excel(name = "创建人", width = 15)
    @ApiModelProperty(value = "创建人")
    private String createBy;
	/**创建日期*/
	@Excel(name = "创建日期", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "创建日期")
    private Date createTime;
	/**更新人*/
	@Excel(name = "更新人", width = 15)
    @ApiModelProperty(value = "更新人")
    private String updateBy;
	/**更新日期*/
	@Excel(name = "更新日期", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "更新日期")
    private Date updateTime;
	/**备注*/
	@Excel(name = "备注", width = 15)
    @ApiModelProperty(value = "备注")
    private String remarks;
	/**所属部门*/
	@Excel(name = "所属部门", width = 15)
    @ApiModelProperty(value = "所属部门")
    private String sysOrgCode;
	/**所属部门*/
	@Excel(name = "所属部门", width = 15)
    @ApiModelProperty(value = "所属部门")
    private String departId;
	/**所属医院*/
	@Excel(name = "所属医院", width = 15)
    @ApiModelProperty(value = "所属医院")
    private String departParentId;
}
