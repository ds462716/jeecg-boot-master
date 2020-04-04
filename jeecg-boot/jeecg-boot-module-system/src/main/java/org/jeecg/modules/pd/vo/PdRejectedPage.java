package org.jeecg.modules.pd.vo;

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
import org.jeecg.modules.pd.entity.BaseEntity;
import org.jeecg.modules.pd.entity.PdRejectedDetail;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;

/**
 * @Description: pd_rejected
 * @Author: jiangxz
 * @Date:   2020-03-16
 * @Version: V1.0
 */
@Data
public class PdRejectedPage {
    private static final long serialVersionUID = 1L;

	@Excel(name = "退货单号", width = 15)
    private String rejectedNo;
	@Excel(name = "退货日期", width = 15)
    private String rejectedDate;
	@Excel(name = "科室", width = 15)
    private String departName;
	@Excel(name = "产品编号", width = 15)
    private String productNumber;
	@Excel(name = "产品名称", width = 15)
    private String productName;
    @Excel(name = "产品条码", width = 15)
    private String productBarCode;
    @Excel(name = "退货数量", width = 15)
    private String rejectedCount;
    @Excel(name = "单位", width = 15)
    private String unitName;
    @Excel(name = "规格", width = 15)
    private String spec;
    @Excel(name = "型号", width = 15)
    private String version;
    @Excel(name = "批号", width = 15)
    private String batchNo;
    @Excel(name = "生产日期", width = 15, format = "yyyy-MM-dd")
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private Date produceDate;
    @Excel(name = "有效期", width = 15, format = "yyyy-MM-dd")
    private Date expDate;
    @Excel(name = "生产厂家", width = 15)
    private String venderName;
    @Excel(name = "供应商", width = 15)
    private String supplierName;
    @Excel(name = "注册证号", width = 15)
    private String registration;
    @Excel(name = "操作人", width = 15)
    private String createBy;
}
