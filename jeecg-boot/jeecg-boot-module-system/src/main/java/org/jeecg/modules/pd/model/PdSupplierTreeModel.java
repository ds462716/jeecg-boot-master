package org.jeecg.modules.pd.model;

import org.jeecg.modules.pd.entity.PdSupplier;
import org.jeecg.modules.system.entity.SysDepart;

import java.io.Serializable;
import java.util.*;

/**
 * <p>
 * 供应商结构树实体
 * <p>
 * 
 * @Author Steve
 * @Since 2019-01-22 
 */
public class PdSupplierTreeModel implements Serializable{

    private static final long serialVersionUID = 1L;

    /** 对应PdSupplier中的id字段,前端数据树中的key*/
    private String key;

    /** 对应PdSupplier中的id字段,前端数据树中的value*/
    private String value;

    /** 对应OdSupplier_name字段,前端数据树中的title*/
    private String title;

    private boolean isLeaf;
    // 以下所有字段均与PdSupplier相同

    private String id;

    private String parentId;

    private Object description;

    private String status;

    private String delFlag;

    private String createBy;

    private Date createTime;

    private String updateBy;

    private Date updateTime;

    /**拼音码*/
    private String py;
    /**五笔码*/
    private String wb;
    /**自定义码*/
    private String zdy;

    private List<PdSupplierTreeModel> children = new ArrayList<>();


    /**
     * 将PdSupplier对象转换成PdSupplierTreeModel对象
     * @param PdSupplier
     */
	public PdSupplierTreeModel(PdSupplier PdSupplier) {
		this.key = PdSupplier.getId();
        this.value = PdSupplier.getId();
        this.title = PdSupplier.getName();
        this.id = PdSupplier.getId();
        this.parentId = PdSupplier.getParentId();
        this.py = PdSupplier.getPy();
        this.wb = PdSupplier.getWb();
        this.zdy = PdSupplier.getZdy();
        this.description = PdSupplier.getRemarks();
        this.status = PdSupplier.getStatus();
        this.delFlag = PdSupplier.getDelFlag();
        this.createBy = PdSupplier.getCreateBy();
        this.createTime = PdSupplier.getCreateTime();
        this.updateBy = PdSupplier.getUpdateBy();
        this.updateTime = PdSupplier.getUpdateTime();
    }

    public PdSupplierTreeModel() {

    }


    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isLeaf() {
        return isLeaf;
    }

    public void setLeaf(boolean leaf) {
        isLeaf = leaf;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public Object getDescription() {
        return description;
    }

    public void setDescription(Object description) {
        this.description = description;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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

    public List<PdSupplierTreeModel> getChildren() {
        return children;
    }

    public void setChildren(List<PdSupplierTreeModel> children) {
        this.children = children;
    }
}
