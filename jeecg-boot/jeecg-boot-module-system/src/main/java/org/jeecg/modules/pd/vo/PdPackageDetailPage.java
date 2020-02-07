package org.jeecg.modules.pd.vo;

import lombok.Data;
import org.jeecg.modules.pd.entity.PdPackageDetail;

/**
 * @author jiangxz
 * @description
 * @date 2020-2-6
 */
@Data
public class PdPackageDetailPage extends PdPackageDetail {
    /**产品编号*/
    private String number;
    /** 产品名称 **/
    private String productName;
    /** 单位名称 **/
    private String unitName;
    /** 厂家名称 **/
    private String venderName;
    /** 供应商名称 **/
    private String supplierName;
    /**规格*/
    private String spec;
    /**型号*/
    private String version;
}
