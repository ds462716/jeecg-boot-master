package org.jeecg.modules.pd.vo;

import lombok.Data;
import org.jeecgframework.poi.excel.annotation.Excel;

import java.math.BigDecimal;

/**
 * @Description: 出入库记录表
 * @Author: jiangxz
 * @Date:   2020-02-13
 * @Version: V1.0
 */
@Data
public class PdStockRecordInExcle{
    @Excel(name = "产品编号", width = 15)
    private String number;//产品编号
    @Excel(name = "产品名称", width = 15)
    private String productName;//产品名称
    @Excel(name = "产品规格", width = 15)
    private String spec;//产品规格
    /**产品类型名称or普通产品，0产品，1试剂*/
    @Excel(name = "产品类型", width = 15)
    private String productFlagName;
    @Excel(name = "总数量", width = 15)
    private Double totalSum;//总数量
    @Excel(name = "单位", width = 15)
    private String unitName;//单位
    @Excel(name = "总金额", width = 15)
    private BigDecimal inTotalPrice;//总金额
    @Excel(name = "生产厂家", width = 15)
    private String venderName;//生产厂家名称













}
