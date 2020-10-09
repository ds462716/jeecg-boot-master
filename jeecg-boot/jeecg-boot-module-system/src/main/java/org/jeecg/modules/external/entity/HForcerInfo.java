package org.jeecg.modules.external.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.jeecg.modules.pd.entity.BaseEntity;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * @Description: h_forcer_inf
 * @Author: mcb
 * @Date:   2020-07-13
 * @Version: V1.0
 */
@Data
@TableName("h_forcer_info")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class HForcerInfo extends BaseEntity {
    private static final long serialVersionUID = 1L;

	/**主键*/
	@TableId(type = IdType.ID_WORKER_STR)
    @ApiModelProperty(value = "主键")
    private String id;
	/**创建时间*/
	@Excel(name = "创建时间", width = 15, format = "yyyy-MM-dd  HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd  HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd  HH:mm:ss")
    private Date createTime;
	/**创建人*/
	@Excel(name = "创建人", width = 15)
    private String createBy;
	/**更新日期*/
	@Excel(name = "更新日期", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private Date updateTime;
	/**更新人*/
	@Excel(name = "更新人", width = 15)
    private String updateBy;
	/**部门*/
	@Excel(name = "部门", width = 15)
    private String sysOrgCode;
	/**所属机构*/
	@Excel(name = "所属机构", width = 15)
    private String departParentId;
    /**所属科室*/
    @Excel(name = "所属科室", width = 15)
    private String departId;
    /**当前库房*/
    @Excel(name = "当前库房", width = 15)
    private String kfId;
    /**关联科室*/
    @Excel(name = "关联科室", width = 15)
    private String sjkfId;
    /**柜子编号*/
    @Excel(name = "柜子编号", width = 15)
    @ApiModelProperty(value = "柜子编号")
    private String forcerNo;
    /**柜子名称*/
    @Excel(name = "柜子名称", width = 15)
    private String forcerName;
    /**启用状态   0:未启用  1：已启用*/
    @Excel(name = "启用状态", width = 15)
    private String isDisable;
    /**审核人*/
    @Excel(name = "审核人", width = 15)
    private String userId;
    /**设备MAC地址*/
    @Excel(name = "设备MAC地址", width = 15)
    private String macAddress;
    /**联系人*/
    @Excel(name = "联系人", width = 15)
    private String linkMan;
    /**联系人电话*/
    @Excel(name = "联系人电话", width = 15)
    private String linkPhone;
    @TableField(exist = false)
    private String departName;
    @TableField(exist = false)
    private String supDepartName;

}
