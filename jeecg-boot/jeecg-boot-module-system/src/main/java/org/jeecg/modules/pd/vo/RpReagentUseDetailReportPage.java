package org.jeecg.modules.pd.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.jeecg.modules.pd.entity.PdProductStock;
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
public class RpReagentUseDetailReportPage {

    /**主键*/
    @TableId(type = IdType.ID_WORKER_STR)
    @ApiModelProperty(value = "主键")
    private String id;
    /**创建人*/
    @ApiModelProperty(value = "创建人")
    private String createBy;
    /**创建日期*/
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "创建日期")
    private Date createTime;
    /**更新人*/
    @ApiModelProperty(value = "更新人")
    private String updateBy;
    /**更新日期*/
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "更新日期")
    private Date updateTime;
    /**所属部门*/
    @ApiModelProperty(value = "所属部门")
    private String sysOrgCode;
    /**开瓶操作人*/
    //@Excel(name = "开瓶操作人", width = 15)
    @ApiModelProperty(value = "开瓶操作人")
    private String boottleBy;
    @Excel(name = "所属部门", width = 15)

    private String departName;
    @Excel(name = "产品名称", width = 15)

    private String productName;
    /**试剂对应条码*/
    @Excel(name = "试剂对应唯一码", width = 15)
    @ApiModelProperty(value = "试剂对应唯一码")
    private String refBarCode;
    /**开瓶时间*/
    @Excel(name = "开瓶时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "开瓶时间")
    private Date boottleDate;
    /**对应库存明细ID*/
    @ApiModelProperty(value = "对应库存明细ID")
    private String stockId;
    @ApiModelProperty(value = "使用状态")
    private String status; //使用状态
    /**闭瓶时间*/
    //@Excel(name = "闭瓶时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "闭瓶时间")
    private Date closeDate;
    /**闭瓶操作人*/
    //@Excel(name = "闭瓶操作人", width = 15)
    @ApiModelProperty(value = "闭瓶操作人")
    private String closeBy;
    @ApiModelProperty(value = "试剂金额")
    private BigDecimal purchasePrice;//试剂金额
    /**备注*/
    @ApiModelProperty(value = "备注")
    private String remarks;
    /**闭瓶原因*/
    @Excel(name = "闭瓶原因  0:已用完  1:已过期", width = 15)
    @ApiModelProperty(value = "闭瓶原因")
    private String closeRemarks;
    /**所属部门*/
    @ApiModelProperty(value = "所属部门")
    private String departId;
    /**所属机构*/
    @ApiModelProperty(value = "所属机构")
    private String departParentId;
    /**检验仪器代号*/
    //@Excel(name = "检验仪器代号", width = 15)
    @ApiModelProperty(value = "检验仪器代号")
    private String instrCode;

    private String bottleType;//操作类型   1：开瓶   2：闭瓶

    private List<PdProductStock> pdProductStockList;

    private String number;//产品编号

    private String bidingNumber;//中标号

    private String productId;//产品ID
    @Excel(name = "单位名称", width = 15)

    private String unitName;
    /*产品规格数量*/
    @Excel(name = "产品规格数量", width = 15)

    private Double specQuantity;
    /**使用規格數量*/
    @Excel(name = "使用規格數量", width = 15)
    @ApiModelProperty(value = "使用規格數量")
    private Double specNum;
    @Excel(name = "剩余规格数量", width = 15)
    private Double sySpecNum; //剩余规格数量

    private String venderName; //生产厂家

    private String venderId; //生产厂家Id

    private String supplierId; //供应商ID
    /**批次号*/
    @Excel(name = "批次号", width = 15)

    private String batchNo;
    /**有效期*/
    @Excel(name = "有效期", width = 15, format = "yyyy-MM-dd")
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")

    private Date expDate;
    /*多个部门集合*/

    private List<String> departIdList;

    private String departIds; //批量查询用

    private Double num; //试剂使用数量

    private String specUnitName; //规格单位名称

    private BigDecimal totalPrice;//使用总金额

    private String nestatStatus; //产品使用状态
    /**检验仪器名称*/

    private String instrName;
    /*是否过滤已闭瓶的数据  有值则过滤**/

    private String filterType;

    private String instrCodes; //批量查询仪器用

    private String month; //使用月份

    private Double itemNum; //检验项目数量

    private BigDecimal itemPrice; //检验项目总费用
    /** 查询日期起始 **/

    private String queryDateStart;
    /** 查询日期结束 **/

    private String queryDateEnd;

    private String spec; //产品规格
    private String productNumber; //编号
    private String version; //型号

    private String yearMonth;
    private String queryExpDateStart;
    private String queryExpDateEnd;
    private String registration;

}
