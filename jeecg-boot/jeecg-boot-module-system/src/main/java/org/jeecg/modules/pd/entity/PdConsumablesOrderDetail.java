/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package org.jeecg.modules.pd.entity;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 耗材订单详情表Entity
 * @author mcb
 * @version 2020-02-16
 */
@Data
public class PdConsumablesOrderDetail {
	
	private static final long serialVersionUID = 1L;
	private String orderId;		// 药品订单id
	private String number;		// 耗材编码
	private Double orderQuantity;		// 订货数
	private BigDecimal price;		// 单价
	private BigDecimal amount;		// 金额
	private String supplierName;//供应商名称
	//冗余
	private String name;//耗材名称
	private String spec;//规格
	private String unit; //单位
	private String venderName; //生产厂家

}