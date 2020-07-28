package org.jeecg.modules.pd.entity;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.jeecg.common.util.DateUtils;
import org.springframework.format.annotation.DateTimeFormat;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.jeecg.common.aspect.annotation.Dict;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @Description: pd_stock_log
 * @Author: jiangxz
 * @Date:   2020-02-28
 * @Version: V1.0
 */
@Data
@TableName("pd_stock_log")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="pd_stock_log对象", description="pd_stock_log")
public class PdStockLog extends BaseEntity {
    private static final long serialVersionUID = 1L;

	/**主键*/
	@TableId(type = IdType.ID_WORKER_STR)
    @ApiModelProperty(value = "主键")
    private String id;
	/**产品主键*/
	@Excel(name = "产品主键", width = 15)
    @ApiModelProperty(value = "产品主键")
    private String productId;
	/**批次号*/
	@Excel(name = "批次号", width = 15)
    @ApiModelProperty(value = "批次号")
    private String batchNo;
	/**产品条形码*/
	@Excel(name = "产品条形码", width = 15)
    @ApiModelProperty(value = "产品条形码")
    private String productBarCode;
	/**产品数量*/
	@Excel(name = "产品数量", width = 15)
    @ApiModelProperty(value = "产品数量")
    private Double productNum;
	/**来源*/
	@Excel(name = "来源", width = 15)
    @ApiModelProperty(value = "来源")
    private String inFrom;
	/**去向*/
	@Excel(name = "去向", width = 15)
    @ApiModelProperty(value = "去向")
    private String outTo;
	/**记录业务类型 数据字典：stock_log_type*/
	@Excel(name = "记录业务类型 数据字典：stock_log_type", width = 15)
    @ApiModelProperty(value = "记录业务类型 数据字典：stock_log_type")
    private String logType;
	/**单号*/
	@Excel(name = "单号", width = 15)
    @ApiModelProperty(value = "单号")
    private String invoiceNo;
	/**病人信息*/
	@Excel(name = "病人信息", width = 15)
    @ApiModelProperty(value = "病人信息")
    private String patientInfo;
	/**收费科室*/
	@Excel(name = "收费科室", width = 15)
    @ApiModelProperty(value = "收费科室")
    private String chargeDeptName;
	/**记录时间*/
	@Excel(name = "记录时间", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "记录时间")
    private Date recordTime;
	/**创建者 : 创建者*/
	@Excel(name = "创建者 : 创建者", width = 15)
    @ApiModelProperty(value = "创建者 : 创建者")
    private String createBy;
	/**创建时间 : 创建时间*/
	@Excel(name = "创建时间 : 创建时间", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "创建时间 : 创建时间")
    private Date createTime;
	/**更新者 : 更新者*/
	@Excel(name = "更新者 : 更新者", width = 15)
    @ApiModelProperty(value = "更新者 : 更新者")
    private String updateBy;
	/**更新时间 : 更新时间*/
	@Excel(name = "更新时间 : 更新时间", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "更新时间 : 更新时间")
    private Date updateTime;
	/**备注信息 : 备注信息*/
	@Excel(name = "备注信息 : 备注信息", width = 15)
    @ApiModelProperty(value = "备注信息 : 备注信息")
    private String remarks;
	/**extend1*/
	@Excel(name = "extend1", width = 15)
    @ApiModelProperty(value = "extend1")
    private String extend1;
	/**extend2*/
	@Excel(name = "extend2", width = 15)
    @ApiModelProperty(value = "extend2")
    private String extend2;
	/**extend3*/
	@Excel(name = "extend3", width = 15)
    @ApiModelProperty(value = "extend3")
    private String extend3;
	/**所属父部门*/
	@Excel(name = "所属父部门", width = 15)
    @ApiModelProperty(value = "所属父部门")
    private String departParentId;
	/**所属部门*/
	@Excel(name = "所属部门", width = 15)
    @ApiModelProperty(value = "所属部门")
    private String departId;
    /**有效期*/
    @Excel(name = "有效期", width = 15, format = "yyyy-MM-dd")
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private Date expDate;

    private String refBarCode;//唯一码

    @TableField(exist = false)
    private String[] timeStr = new String[]{"","",""};
    //前台做展示用
    public String[] getTimeStr() {
        if(this.recordTime != null){
            this.timeStr[0] = DateUtils.formatDate(this.recordTime, "yyyy-MM-dd");
            this.timeStr[1] = DateUtils.getDayOfWeek(this.recordTime);
            this.timeStr[2] = DateUtils.formatDate(this.recordTime, "HH:mm:ss");
        }
        return timeStr;
    }
}
