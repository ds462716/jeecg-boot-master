package org.jeecg.modules.pd.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @author jiangxz
 * @description 出入库统计报表明细
 * @date 2020-8-5
 */
@Data
public class RpInDetailReportExcel {

    @Excel(name = "入库单号", width = 15)
    private String recordNo;//出入库单号
    @Excel(name = "入库日期", width = 15, format = "yyyy-MM-dd")
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private Date auditDate;//出入库时间
    @Excel(name = "入库类型", width = 15)
    private String inType;//入库类型
    @Excel(name = "出库科室", width = 15)
    private String outDepartName;//出库科室名称
    @Excel(name = "入库科室", width = 15)
    private String inDepartName;//入库科室名称

    @Excel(name = "产品编号", width = 15)
    private String productNumber;//产品编号
    @Excel(name = "产品名称", width = 15)
    private String productName;//产品名称
    @Excel(name = "规格", width = 15)
    private String spec;//产品规格
    @Excel(name = "型号", width = 15)
    private String version;//产品型号
    @Excel(name = "批号", width = 15)
    private String batchNo;

    @Excel(name = "生产日期", width = 15, format = "yyyy-MM-dd")
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private Date produceDate;
    @Excel(name = "有效期", width = 15, format = "yyyy-MM-dd")
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private Date expDate;

    @Excel(name = "入库数量", width = 15)
    private Double productNum;
    @Excel(name = "库存数量", width = 15)
    private Double stockNum;// 库存数量
    @Excel(name = "单位", width = 15)
    private String unitName;//单位名称
    @Excel(name = "入库单价", width = 15)
    private BigDecimal purchasePrice;
    @Excel(name = "入库金额", width = 15)
    private BigDecimal inTotalPrice;// 入库金额

    private BigDecimal outTotalPrice;//出库金额
    @Excel(name = "规格数量", width = 15)
    private Double specQuantity;// 规格数量
    @Excel(name = "规格单位", width = 15)
    private String specUnitName;// 规格单位名称

    @Excel(name = "生产厂家", width = 15)
    private String venderName;//生产厂家名称
    @Excel(name = "供应商", width = 15)
    private String supplierName;//供应商名称
    @Excel(name = "配送商", width = 15)
    private String distributorName;//配送商名称

    @Excel(name = "注册证", width = 15)
    private String productRegistration; //产品表中的注册证
    @Excel(name = "发票号", width = 15)
    private String invoiceNo; // 发票号
    @Excel(name = "发票代码", width = 15)
    private String invoiceCode; //发票代码
    @Excel(name = "发票日期", width = 15, format = "yyyy-MM-dd")
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private Date invoiceData; //发票日期

    @Excel(name = "备注", width = 15)
    private String remarks;

    @Excel(name = "收费代码", width = 15)
    private String chargeCode;//收费代号

    @Excel(name = "中标号", width = 15)
    private String bidingNumber;//中标号
    @Excel(name = "省标码", width = 15)
    private String dartCode;// 省标码

    @Excel(name = "产品JDE编号", width = 15)
    private String jdeCode;// JDE编号
    @Excel(name = "生产厂家JDE编号", width = 15)
    private String venderJdeCode; // 厂家JDEcode
    @Excel(name = "供应商JDE编号", width = 15)
    private String supplierJdeCode;//供应商JDEcode
    @Excel(name = "操作人", width = 15)
    private String realname;//操作人名称
}
