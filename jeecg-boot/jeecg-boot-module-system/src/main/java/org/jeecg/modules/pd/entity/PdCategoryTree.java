package org.jeecg.modules.pd.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PdCategoryTree implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * id
	 */
	private String id;

	private String key;
	private String title;

	/**
	 * 父id
	 */
	private String parentId;

	/**
	 * 菜单名称
	 */
	private String name;

	/**
	 * 拼音简码
	 */
	private String py;

	/**
	 * 五笔简码
	 */
	private String wb;

	/**
	 * 自定义简码
	 */
	private String zdy;

	/**
	 * 菜单图标
	 */
	private String icon;

	/**
	 * 类型（0：一级菜单；1：子菜单 ；2：按钮权限）
	 */
	private Integer type;

	/**
	 * 是否叶子节点: 1:是 0:不是
	 */
	private boolean isLeaf;


	/**
	 * 删除状态 0正常 1已删除
	 */
	private String delFlag;

	/**
	 * 创建人
	 */
	private String createBy;

	/**
	 * 创建时间
	 */
	private Date createTime;

	/**
	 * 更新人
	 */
	private String updateBy;

	private String remarks;

	/**
	 * 更新时间
	 */
	private Date updateTime;

	private List<PdCategoryTree> children;

	public PdCategoryTree() {
	}

	public PdCategoryTree(PdCategory pdCategory) {
		this.key = pdCategory.getId();
		this.id = pdCategory.getId();
		this.createBy = pdCategory.getCreateBy();
		this.createTime = pdCategory.getCreateTime();
		this.delFlag = pdCategory.getDelFlag();
		this.icon = pdCategory.getIcon();
		this.isLeaf = pdCategory.isLeaf();
		this.type = Integer.parseInt(pdCategory.getType());
		this.name = pdCategory.getName();
		this.py = pdCategory.getPy();
		this.wb = pdCategory.getWb();
		this.zdy = pdCategory.getZdy();
		this.parentId = pdCategory.getParentId();
		this.updateBy = pdCategory.getUpdateBy();
		this.updateTime = pdCategory.getUpdateTime();
		this.title = pdCategory.getName();
		this.remarks = pdCategory.getRemarks();
		if (!pdCategory.isLeaf()) {
			this.children = new ArrayList<PdCategoryTree>();
		}
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public boolean getIsLeaf() {
		return isLeaf;
	}

	public void setLeaf(boolean leaf) {
		isLeaf = leaf;
	}

	public String getDelFlag() {
		return delFlag;
	}

	public void setDelFlag(String delFlag) {
		this.delFlag = delFlag;
	}

	public String getCreateBy() {
		return createBy;
	}

	public void setCreateBy(String createBy) {
		this.createBy = createBy;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getUpdateBy() {
		return updateBy;
	}

	public void setUpdateBy(String updateBy) {
		this.updateBy = updateBy;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public List<PdCategoryTree> getChildren() {
		return children;
	}

	public void setChildren(List<PdCategoryTree> children) {
		this.children = children;
	}

	public String getPy() {
		return py;
	}

	public void setPy(String py) {
		this.py = py;
	}

	public String getWb() {
		return wb;
	}

	public void setWb(String wb) {
		this.wb = wb;
	}

	public String getZdy() {
		return zdy;
	}

	public void setZdy(String zdy) {
		this.zdy = zdy;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
}