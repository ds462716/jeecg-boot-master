package org.jeecg.modules.pd.vo;

import java.util.List;
import org.jeecg.modules.pd.entity.PdPackage;
import org.jeecg.modules.pd.entity.PdPackageDetail;
import lombok.Data;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.jeecgframework.poi.excel.annotation.ExcelEntity;
import org.jeecgframework.poi.excel.annotation.ExcelCollection;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;
import java.util.Date;

/**
 * @Description: 定数包
 * @Author: jiangxz
 * @Date:   2020-02-02
 * @Version: V1.0
 */
@Data
public class PdPackagePage {
	
	/**主键*/
	private String id;
	/**定数包编号*/
	@Excel(name = "定数包编号", width = 15)
	private String code;
	/**定数包名称*/
	@Excel(name = "定数包名称", width = 15)
	private String name;
	/**产品总数*/
	@Excel(name = "产品总数", width = 15)
	private Integer sum;
	/**拼音简码*/
	@Excel(name = "拼音简码", width = 15)
	private String py;
	/**五笔简码*/
	@Excel(name = "五笔简码", width = 15)
	private String wb;
	/**自定义码*/
	@Excel(name = "自定义码", width = 15)
	private String zdy;
	/**删除标识*/
//	@Excel(name = "删除标识", width = 15)
//	private String delFlag;
	/**备注*/
	@Excel(name = "备注", width = 15)
	private String remarks;
	/**创建人*/
	@Excel(name = "创建人", width = 15)
	private String createBy;
	/**创建日期*/
	@Excel(name = "创建日期", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private Date createTime;
	/**更新人*/
	@Excel(name = "更新人", width = 15)
	private String updateBy;
	/**更新日期*/
	@Excel(name = "更新日期", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private Date updateTime;
	/**所属部门*/
	@Excel(name = "所属部门", width = 15)
	private String sysOrgCode;
	/**所属部门*/
	private String departId;
	private String departParentId;
	
	@ExcelCollection(name="定数包明细")
	private List<PdPackageDetail> pdPackageDetailList;	
	
}
