package org.jeecg.modules.pd.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * @Description: 收费项目基础信息
 * @Author: jeecg-boot
 * @Date:   2020-02-11
 * @Version: V1.0
 */
@Data
@TableName("pd_auto_order_inf")
public class PdAutoOrderInf extends BaseEntity {
    private static final long serialVersionUID = 1L;
    
	/**主键*/
	@TableId(type = IdType.ID_WORKER_STR)
    private String id;
	/**创建人*/
    @Excel(name = "创建人", width = 15)
    private String createBy;
	/**创建日期*/
    @Excel(name = "创建日期", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date createTime;
	/**更新人*/
    @Excel(name = "更新人", width = 15)
    private String updateBy;
	/**更新日期*/
    @Excel(name = "更新日期", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date updateTime;
	/**所属部门*/
    @Excel(name = "所属部门", width = 15)
    private String sysOrgCode;
    /**'补货申请日期'*/
    @Excel(name = "'补货申请日期'", width = 15, format = "yyyy-MM-dd")
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date autoDate;
    private Double autoNum; //自动补货数量
    private Double limitDown; //库存下限数量
    private Double stockNum; //补货时库存数量
    private String productId;//补货产品id
    private String departId;//补货科室id
    private String departParentId;//机构ID
    private String orderType;//生成订单类型 0:申领单  1：采购单
    private String orderNo;//对应订单编号
}
