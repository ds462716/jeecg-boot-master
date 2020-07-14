package org.jeecg.modules.external.entity;

import com.baomidou.mybatisplus.annotation.IdType;
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
 * @Description: h_user_finger_face
 * @Author: mcb
 * @Date:   2020-07-13
 * @Version: V1.0
 */
@Data
@TableName("h_user_finger_face")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class HUserFingerFace extends BaseEntity {
    private static final long serialVersionUID = 1L;

	/**主键*/
	@TableId(type = IdType.ID_WORKER_STR)
    @ApiModelProperty(value = "主键")
    private String id;
	/**创建时间*/
	@Excel(name = "创建时间", width = 15, format = "yyyy-MM-dd  HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd  HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd  HH:mm:ss")
    @ApiModelProperty(value = "创建时间")
    private Date createTime;
	/**创建人*/
	@Excel(name = "创建人", width = 15)
    @ApiModelProperty(value = "创建人")
    private String createBy;
	/**更新日期*/
	@Excel(name = "更新日期", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "更新日期")
    private Date updateTime;
	/**更新人*/
	@Excel(name = "更新人", width = 15)
    @ApiModelProperty(value = "更新人")
    private String updateBy;
	/**部门*/
	@Excel(name = "部门", width = 15)
    @ApiModelProperty(value = "部门")
    private String sysOrgCode;
	/**所属机构*/
	@Excel(name = "所属机构", width = 15)
    @ApiModelProperty(value = "所属机构")
    private String departParentId;
    /**所属科室*/
    @Excel(name = "所属科室", width = 15)
    @ApiModelProperty(value = "所属科室")
    private String departId;
    /**用户id*/
    @Excel(name = "用户id", width = 15)
    @ApiModelProperty(value = "用户id")
    private String userId;
    /**是否启用 0：未启用 1：已启用*/
    @Excel(name = "是否启用", width = 15)
    @ApiModelProperty(value = "是否启用")
    private String isDisable;
    /**人脸或指纹图片*/
    @Excel(name = "人脸或指纹图片", width = 15)
    @ApiModelProperty(value = "人脸或指纹图片")
    private byte[] image;
    /**类型   0：指纹信息    1：人脸信息*/
    @Excel(name = "类型", width = 15)
    @ApiModelProperty(value = "类型")
    private String type;

}
