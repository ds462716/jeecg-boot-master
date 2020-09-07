package org.jeecg.modules.pd.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @Description: 盘点用导出excel
 * @Author: jeecg-boot
 * @Date:   2020-02-11
 * @Version: V1.0
 */
@Data
public class PdProductAllocationExcel {
    /**科室名称*/
    @Excel(name = "科室名称", width = 15)
    private String deptName;
    /**产品名称*/
    @Excel(name = "产品名称", width = 15)
    private String productName;
    /*@Excel(name = "产品类型", width = 15)
    private String productFlagName;*/
    /**规格*/
    @Excel(name = "规格", width = 15)
    private String spec;
    /**批次号*/
    @Excel(name = "批次号", width = 15)
    private String batchNo;
    /**有效期*/
    @Excel(name = "有效期", width = 15, format = "yyyy-MM-dd")
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private Date expDate;
    /**库存数量*/
    /**单位名称*/
    @Excel(name = "单位名称", width = 15)
    private String unitName;
    @Excel(name = "数量", width = 15)
    private Double stockNum;
    @Excel(name = "单价", width = 15)
    private BigDecimal purchasePrice;
    /**库存占用状态  0:使用中   1:未使用 2:已用完*/
    @Excel(name = "库存占用状态 0:使用中   1:未使用", width = 15)
    private String nestatStatus;
    @Excel(name = "生产厂家", width = 15)
    private String venderName;
    /**条码集合*/
    @Excel(name = "唯一码", width = 15)
    private String refBarCodes;
}


