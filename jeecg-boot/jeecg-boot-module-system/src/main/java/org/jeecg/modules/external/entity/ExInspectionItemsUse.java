package org.jeecg.modules.external.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.jeecg.modules.pd.entity.PdProductStock;
import org.jeecg.modules.pd.entity.PdUsePackageDetail;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;

/**
 * @Description: 检验项目使用表
 * @Author: jiangxz
 * @Date:   2020-05-11
 * @Version: V1.0
 */
@Data
@TableName("ex_inspection_items_use")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="ex_inspection_items_use对象", description="检验项目使用表")
public class ExInspectionItemsUse  {
    private static final long serialVersionUID = 1L;

	/**主键*/
	@TableId(type = IdType.ID_WORKER_STR)
    @ApiModelProperty(value = "主键")
    private String id;
	/**关联病人信息id*/
	@Excel(name = "关联病人信息id", width = 15)
    @ApiModelProperty(value = "关联病人信息id")
    private String refId;
	/**检验项目类型1复检，2质控，3测试，4空白阴阳对照，5其他*/
	@Excel(name = "检验项目类型1复检，2质控，3测试，4空白阴阳对照，5其他", width = 15)
    @ApiModelProperty(value = "检验项目类型1复检，2质控，3测试，4空白阴阳对照，5其他")
    private String itemType;
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
	/**所属部门*/
	@Excel(name = "所属部门", width = 15)
    @ApiModelProperty(value = "所属部门")
    private String sysOrgCode;
    /**备注*/
    @Excel(name = "备注", width = 15)
    private java.lang.String remarks;
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
    @TableField(exist = false)
    private List<PdProductStock> exInspectionItemsUseDetails;

    @TableField(exist = false)
    private List<PdUsePackageDetail> pdUsePackageDetails;

    @TableField(exist = false)
    private List<ExInspectionItemsUseDetail> pakageUseDetailList;

    @TableField(exist = false)
    private List<ExInspectionItemsUseDetail> useDetailList;
}
