package org.jeecg.modules.pd.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.jeecg.common.aspect.annotation.Dict;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * @Description: pd_package_record
 * @Author: jiangxz
 * @Date:   2020-04-22
 * @Version: V1.0
 */
@ApiModel(value="pd_package_record对象", description="pd_package_record")
@Data
@TableName("pd_package_record")
public class PdPackageRecord extends BaseEntity {
    private static final long serialVersionUID = 1L;

	/**id*/
	@TableId(type = IdType.ID_WORKER_STR)
    @ApiModelProperty(value = "id")
    private String id;
	/**套包ID*/
	@Excel(name = "套包ID", width = 15)
    @ApiModelProperty(value = "套包ID")
    private String packageId;
	/**科室ID*/
	@Excel(name = "科室ID", width = 15)
    @ApiModelProperty(value = "科室ID")
    private String departId;
    private String departParentId;
    @Excel(name = "打包编号", width = 15)
    @ApiModelProperty(value = "打包编号")
    private String recordNo;
	/**套包流水码*/
	@Excel(name = "套包流水码", width = 15)
    @ApiModelProperty(value = "套包流水码")
    private String packageBarCode;
	/**出库状态：0-已出库；1-未出库*/
	@Excel(name = "出库状态：0-已出库；1-未出库", width = 15)
    @ApiModelProperty(value = "出库状态：0-已出库；1-未出库")
    private String status;
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
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
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

    @TableField(exist = false)
    private List<PdPackageRecordDetail> pdPackageRecordDetailList;

    @TableField(exist = false)
    private String packageCode;          //套包编号
    @TableField(exist = false)
    private String packageName;          //套包名称
    @TableField(exist = false)
    private String packageSum;           //套包产品总数
    @TableField(exist = false)
    private List<String> idList;

    @TableField(exist = false)
    private String productNumber; // 产品编号 查询用
    @TableField(exist = false)
    private String productName; // 产品名称 查询用
    @TableField(exist = false)
    private Integer packageCount; //打包数量
    /**开关-是否显示二级条码框**/
    @TableField(exist = false)
    private String showSBarcode;
    @TableField(exist = false)
    private String departName;
}
