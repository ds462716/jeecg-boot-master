package org.jeecg.modules.pd.vo;

import lombok.Data;
import org.jeecg.modules.pd.entity.PdProduct;

import java.math.BigDecimal;

/**
 * @author jiangxz
 * @description 产品VO
 * @date 2020-2-6
 */
@Data
public class PdProductPage extends PdProduct {

    /** 产品ID **/
    private String productId;

    /** 产品名称 **/
    private String productName;

    /** 产品编号 **/
    private String productNumber;

    /** 单位名称 **/
    private String unitName;

    /** 厂家名称 **/
    private String venderName;

    /** 供应商名称 **/
    private String supplierName;

    //-------start 用于采购弹出框显示产品明细
    /** 采购明细id **/
    private String purchaseDetailId;

    /**订单编号**/
    private String orderNo;

    /**合并订单编号**/
    private String mergeOrderNo;

    /** 产品采购数量 **/
    private Double orderNum;

    /** 产品到货数量 **/
    private Double arrivalNum;

    /** 采购总价格 **/
    private BigDecimal price;

    /** 产品库存数量 **/
    private Double stockNum;

    /**产品类型名称or普通产品，0产品，1试剂*/
    private java.lang.String productFlagName;
    //-------end 用于采购弹出框显示产品明细
}

