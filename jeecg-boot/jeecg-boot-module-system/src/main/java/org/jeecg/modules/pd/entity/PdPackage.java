package org.jeecg.modules.pd.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * @Description: 套包
 * @Author: jiangxz
 * @Date:   2020-02-02
 * @Version: V1.0
 */
@Data
@TableName("pd_package")
public class PdPackage extends BaseEntity {
    private static final long serialVersionUID = 1L;
    
	/**主键*/
	@TableId(type = IdType.ID_WORKER_STR)
    private String id;
	/**套包编号*/
    private String packageCode;
	/**套包名称*/
    private String packageName;
	/**产品总数*/
    private Double packageSum;
	/**拼音简码*/
    private String py;
	/**五笔简码*/
    private String wb;
	/**自定义码*/
    private String zdy;
	/**备注*/
    private String remarks;
	/**创建人*/
    private String createBy;
	/**创建日期*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date createTime;
	/**更新人*/
    private String updateBy;
	/**更新日期*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date updateTime;
	/**所属部门*/
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

    @TableField(exist = false)
    private List<PdPackageDetail> pdPackageDetailList;
    @TableField(exist = false)
    private String departName;
    @TableField(exist = false)
    private String departType; //部门类型 0医院1一级库房2二级库房
}
