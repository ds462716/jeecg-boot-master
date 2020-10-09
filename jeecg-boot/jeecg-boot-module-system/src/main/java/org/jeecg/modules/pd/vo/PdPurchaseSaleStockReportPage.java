package org.jeecg.modules.pd.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import org.jeecgframework.poi.excel.annotation.Excel;

/**
 * @author jiangxz
 * @description 进销存报表
 * @date 2020-5-9
 */
@Data
public class PdPurchaseSaleStockReportPage {

    @Excel(name = "供应商", width = 15)
    private String supplierName;

    @Excel(name = "科室", width = 15)
    private String departName;

    @Excel(name = "产品编号", width = 15)
    private String productNumber;

    @Excel(name = "产品名称", width = 15)
    private String productName;

    @Excel(name = "规格", width = 15)
    private String spec;

    @Excel(name = "期初库存", width = 15)
    private String firstStockNum;

    @Excel(name = "本期入库", width = 15)
    private String supplierInStockNum;

    @Excel(name = "调入数量", width = 15)
    private String inStockNum;

    @Excel(name = "调出数量", width = 15)
    private String outStockNum;

    @Excel(name = "本期用量", width = 15)
    private String dosageNum;

    @Excel(name = "单价", width = 15)
    private String sellingPrice;

    @Excel(name = "用量金额", width = 15)
    private String dosageMoney;

    @Excel(name = "本期退货", width = 15)
    private String rejectedNum;

    @Excel(name = "当前库存", width = 15)
    private String stockNum;

    private String id;
    private String queryDateStart; //查询日期起始
    private String queryDateEnd;   //查询日期结束
    private String departId;       //部门id
    private String departParentId;
    private String productId;      //产品ID
    private String supplierId;      //供应商ID

}
