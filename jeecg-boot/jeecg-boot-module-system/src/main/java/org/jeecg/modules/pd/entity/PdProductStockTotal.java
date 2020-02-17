package org.jeecg.modules.pd.entity;

import java.util.Date;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * @Description: 库存总表
 * @Author: jeecg-boot
 * @Date:   2020-02-11
 * @Version: V1.0
 */
@Data
@TableName("pd_product_stock_total")
public class PdProductStockTotal extends BaseEntity {
    private static final long serialVersionUID = 1L;
	/**主键*/
	@TableId(type = IdType.ID_WORKER_STR)
    private String id;
	/**创建人*/
    @Excel(name = "创建人", width = 15)
    private String createBy;
	/**创建日期*/
    @Excel(name = "创建日期", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date createTime;
	/**更新人*/
    @Excel(name = "更新人", width = 15)
    private String updateBy;
	/**更新日期*/
    @Excel(name = "更新日期", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date updateTime;
	/**所属部门*/
    @Excel(name = "所属部门", width = 15)
    private String sysOrgCode;
	/**仓库id*/
    @Excel(name = "科室id", width = 15)
    private String deptId;
    /**仓库名称*/
    @Excel(name = "科室名称", width = 15)
    private String deptName;
	/**产品id*/
    @Excel(name = "产品id", width = 15)
    private String productId;
	/**库存数量*/
    @Excel(name = "库存数量", width = 15)
    private Double stockNum;
	/**生产日期*/
    @Excel(name = "生产日期", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private Date produceDate;
	/**产品有效期*/
    @Excel(name = "产品有效期", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private Date validDate;
	/**库存上限*/
    @Excel(name = "库存上限", width = 15)
    private Double limitUp;
	/**库存下限*/
    @Excel(name = "库存下限", width = 15)
    private Double limitDown;
	/**过期标识*/
    @Excel(name = "过期标识", width = 15)
    private String expire;
	/**供应商ID*/
    @Excel(name = "供应商ID", width = 15)
    private String supplierId;
	/**备注*/
    @Excel(name = "备注", width = 15)
    private String remarks;
	/**删除标识 */
    @Excel(name = "删除标识 ", width = 15)
    private String delFlag;
	/**是否永存*/
    @Excel(name = "是否永存", width = 15)
    private String isLong;

    /**产品名称*/
    @TableField(exist = false)
    private String productName;
    /**产品编号*/
    @TableField(exist = false)
    private String productNo;
    /**单位名称*/
    @TableField(exist = false)
    private String unitName;
    /**规格*/
    @TableField(exist = false)
    private String spec;
    /**型号*/
    @TableField(exist = false)
    private String version;
}