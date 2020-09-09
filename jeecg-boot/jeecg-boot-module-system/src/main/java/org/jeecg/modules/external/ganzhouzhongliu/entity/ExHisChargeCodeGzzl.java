package org.jeecg.modules.external.ganzhouzhongliu.entity;

import lombok.Data;
import org.jeecg.modules.pd.entity.BaseEntity;

/**
 * @Description: 收费项目基础信息
 * @Author: jeecg-boot
 * @Date:   2020-02-11
 * @Version: V1.0
 */
@Data
public class ExHisChargeCodeGzzl extends BaseEntity {
    private static final long serialVersionUID = 1L;

    private String gyXmbh;//收费项目编码
    private String gyXmmc;//收费项目名称
    private String gySpec;//规格
    private String gyVersion;//型号
    private String gyUnitName;//单位
    private String gyVender;//生产厂家
    private String gyJe;//收费金额
    private String gyXmlb;//收费类别标识
    private String gyPym;//拼音码
    private String gyWbm;//五笔码

}
