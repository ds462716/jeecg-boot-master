package org.jeecg.modules.external.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.jeecg.modules.pd.entity.BaseEntity;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * @Description: 试剂用量扣减记录表
 * @Author: jiangxz
 * @Date:   2020-05-22
 * @Version: V1.0
 */
@Data
@TableName("ex_deductuin_dosage")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class ExDeductuinDosage extends BaseEntity {
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
	/**科室ID*/
	@Excel(name = "科室ID", width = 15)
    private String departId;
	/**机构ID*/
	@Excel(name = "机构ID", width = 15)
    private String departParentId;
	/**产品ID*/
	@Excel(name = "产品ID", width = 15)
    private String productId;
	/**产品有效期*/
	@Excel(name = "产品有效期", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private Date expDate;
	/**产品批次号*/
	@Excel(name = "产品批次号", width = 15)
    private String batchNo;
	/**产品条码*/
	@Excel(name = "产品条码", width = 15)
    private String productBarCode;
	/**病人姓名*/
	@Excel(name = "病人姓名", width = 15)
    private String patientName;
	/**住院号*/
	@Excel(name = "住院号", width = 15)
    private String inHospitalNo;
	/**门诊号*/
	@Excel(name = "门诊号", width = 15)
    private String outpatientNumber;
	/**扣减来源*/
	@Excel(name = "扣减来源", width = 15)
    private String deductuinType;
	/**规格单位*/
	@Excel(name = "规格单位", width = 15)
    private String specUnitId;
	/**规格数量*/
	@Excel(name = "规格数量", width = 15)
    private Double specQuantity;
	/**扣减规格数量*/
	@Excel(name = "扣减规格数量", width = 15)
    private Double specNum;
	/**扣减日期*/
	@Excel(name = "扣减日期", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private Date specDate;
	/**对应库存明细ID*/
	@Excel(name = "对应库存明细ID", width = 15)
    private String stockId;
	/**操作人*/
	@Excel(name = "操作人", width = 15)
    private String personName;


}
