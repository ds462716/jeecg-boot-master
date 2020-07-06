package org.jeecg.modules.pd.vo;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import org.jeecg.modules.pd.entity.BaseEntity;
import org.jeecgframework.poi.excel.annotation.Excel;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @Description: pd_invoice_detail
 * @Author: jiangxz
 * @Date: 2020-06-24
 * @Version: V1.0
 */
@ApiModel(value = "pd_invoice对象", description = "pd_invoice")
@Data
@TableName("pd_invoice_detail")
public class PdInvoiceDetailPage extends BaseEntity {
    private static final long serialVersionUID = 1L;

    // 发票信息==========================
    @Excel(name = "发票登记号", width = 15)
    private String invoiceRegNo;//发票登记号
    @Excel(name = "发票号", width = 15)
    private String invoiceNo;//发票号
    @Excel(name = "发票代码", width = 15)
    private String invoiceCode;//发票代码
    @Excel(name = "发票日期", width = 15, format = "yyyy-MM-dd")
    private Date invoiceData;//发票日期
    @Excel(name = "发票金额", width = 15)
    private BigDecimal invoiceMoney;//发票金额
    @Excel(name = "状态", width = 15)
    private String status;
    @Excel(name = "供应商", width = 15)
    private String supplierName;//供应商名称
    @Excel(name = "入库单号", width = 15)
    private String billNo;
    @Excel(name = "入库日期", width = 15, format = "yyyy-MM-dd")
    private Date billDate;
    @Excel(name = "产品编号", width = 15)
    private String productNumber;//产品编号
    @Excel(name = "产品名称", width = 15)
    private String productName;//产品名称
    @Excel(name = "规格", width = 15)
    private String spec;//产品规格
    @Excel(name = "单位", width = 15)
    private String unitName;//单位名称
    @Excel(name = "单价", width = 15)
    private BigDecimal price;
    @Excel(name = "入库数量", width = 15)
    private Double num;
    @Excel(name = "金额", width = 15)
    private BigDecimal money;
    @Excel(name = "库存数量", width = 15)
    private Double stockNum;//库存数量
    @Excel(name = "批号", width = 15)
    private String batchNo;//产品批号
    @Excel(name = "有效期", width = 15, format = "yyyy-MM-dd")
    private Date expDate;//有效期
    @Excel(name = "生产厂家", width = 15)
    private String venderName;//生产厂家名称
    @Excel(name = "备注", width = 15)
    private String remarks;
}
