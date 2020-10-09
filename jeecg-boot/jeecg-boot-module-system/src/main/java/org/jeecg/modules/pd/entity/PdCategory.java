package org.jeecg.modules.pd.entity;

import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;
import org.jeecgframework.poi.excel.annotation.Excel;

/**
 * @Description: 产品分类
 * @Author: zxh
 * @Date:   2020-01-15
 * @Version: V1.0
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class PdCategory  extends BaseEntity {
    private static final long serialVersionUID = 1L;
    
	/**主键*/
	@TableId(type = IdType.ID_WORKER_STR)
    private java.lang.String id;
	/**名称*/
	@Excel(name = "名称", width = 15)
    private java.lang.String name;
	/**编码名称拼音简码*/
	@Excel(name = "编码名称拼音简码", width = 15)
	private String py;
	/**编码名称五笔简码*/
	@Excel(name = "编码名称五笔简码", width = 15)
	private String wb;
	/**自定义名称查询码*/
	@Excel(name = "自定义名称查询码", width = 15)
	private String zdy;
	/**类型*/
	@Excel(name = "类型", width = 15)
    private java.lang.String type;
	/**父id*/
	@Excel(name = "父id", width = 15)
    private java.lang.String parentId;
	/**
	 * 菜单图标
	 */
	private String icon;
	/**
	 * 是否叶子节点: 1:是  0:不是
	 */
	@TableField(value="is_leaf")
	private boolean leaf;
	/**创建人*/
	@Excel(name = "创建人", width = 15)
    private java.lang.String createBy;
	/**创建日期*/
	@Excel(name = "创建日期", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private java.util.Date createTime;
	/**更新人*/
	@Excel(name = "更新人", width = 15)
    private java.lang.String updateBy;
	/**更新日期*/
	@Excel(name = "更新日期", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private java.util.Date updateTime;
	/**所属部门*/
	@Excel(name = "所属部门", width = 15)
    private java.lang.String sysOrgCode;

	/**
	 * 所属部门
	 */
	@TableField(strategy = FieldStrategy.NOT_EMPTY)
	private String departId;

	/**
	 * 所属顶级部门（医院id）
	 */
	@TableField(strategy = FieldStrategy.NOT_EMPTY)
	private String departParentId;
	/**备注*/
	@Excel(name = "备注", width = 15)
    private java.lang.String remarks;

	/**
	 * 停用或启用0启用，1停用
	 */
	private String status;
}
