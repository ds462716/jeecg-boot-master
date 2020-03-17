/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package org.jeecg.modules.pd.entity;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * 耗材订单表Entity
 * @author mcb
 * @version 2020-03-16
 */
@Data
public class PdConsumablesOrder {
	
	private static final long serialVersionUID = 1L;
	private String number;		// 订单编号
	private String hospital;		// 下单医院
	private Double orderQuantity;		// 订货量
	private BigDecimal orderAmount;	// 订单金额
	private String orderState;		// 订单状态
	private String orderDate;		// 订单日期
	//冗余
	private String productType; //耗材类型
	private String enterprise; //生产企业
	private String supplierName;//供应商
	private List<PdConsumablesOrderDetail> pdConsumablesOrderDetails; //耗材订单详情





	
	
}