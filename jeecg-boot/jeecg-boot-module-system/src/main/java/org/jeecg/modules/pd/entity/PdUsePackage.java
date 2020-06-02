package org.jeecg.modules.pd.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.jeecgframework.poi.excel.annotation.ExcelCollection;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;

/**
 * @Description: 检验项目
 * @Author: zxh
 * @Date:   2020年4月21日09:21:28
 * @Version: V1.0
 */
@Data
@TableName("pd_use_package")
public class PdUsePackage extends BaseEntity {
    private static final long serialVersionUID = 1L;
    
	/**主键*/
	@TableId(type = IdType.ID_WORKER_STR)
    private String id;
	/**检验项目编号*/
    private String code;
	/**检验项目名称*/
    private String name;
	/**产品总数*/
    private Double sum;
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
    /**检验科室ID*/
    private String testDepartId;
    /**扣减类型*/
    private String deductuinType;

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
    @ExcelCollection(name="检验项目明细")
    private List<PdUsePackageDetail> pdUsePackageDetailList;

    @TableField(exist = false)
    private List<String> idList;

    @TableField(exist = false)
    private String number;

    @TableField(exist = false)
    private String productName;

    /**检验科室名称*/
    @TableField(exist = false)
    private String testDepartName;

    @TableField(exist = false)
    private String departIds; //批量查询用

    /*多个部门集合*/
    @TableField(exist = false)
    private List<String> departIdList;
}
