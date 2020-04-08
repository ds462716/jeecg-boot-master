package org.jeecg.modules.pd.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.jeecg.common.constant.PdConstant;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

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
    private Date expDate;
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
    private java.math.BigDecimal amountMoney;
    /**是否执行收费*/
    @Excel(name = "是否已经执行收费0是1否", width = 15)
    @ApiModelProperty(value = "是否已经执行收费0是1否2已退回")
    private String hyCharged;

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


    /**产品库存ID*/
    private String productStockId;

    //冗余
    @TableField(exist = false)
    private String outHuoweiName;//出库货位
    private String outHuoweiCode;

    @TableField(exist = false)
    private String inHuoweiName;//入库货位
    private String inHuoweiCode;

    @TableField(exist = false)
    private String productName;//产品名称
    @TableField(exist = false)
    private String productNumber;//产品编号
    @TableField(exist = false)
    private String unitName;//单位
    @TableField(exist = false)
    private String number;//产品编号
    @TableField(exist = false)
    private String spec;//产品规格
    @TableField(exist = false)
    private String version;//产品型号
    @TableField(exist = false)
    private String isCharge;
    @TableField(exist = false)
    private String isChargeText;
    @TableField(exist = false)
    private String hyChargedText;
    @TableField(exist = false)
    @Excel(name = "出库单价", width = 15)
    private java.math.BigDecimal sellingPrice;//出价
    @TableField(exist = false)
    /**入库单价*/
    @Excel(name = "入库单价", width = 15)
    private java.math.BigDecimal purchasePrice;//进价

    public String getHyChargedText() {
        if(this.hyCharged!=null){
            if(this.hyCharged.equals(PdConstant.CHARGE_FLAG_0)){
                return "已收费";
            }else if(this.hyCharged.equals(PdConstant.CHARGE_FLAG_1)){
                return "未收费";
            }else if(this.hyCharged.equals(PdConstant.CHARGE_FLAG_2)){
                return "已退回";
            }
        }
        return hyChargedText;
    }

    public void setHyChargedText(String hyChargedText) {
        this.hyChargedText = hyChargedText;
    }
}
