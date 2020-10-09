package org.jeecg.modules.external.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

/**
 * @Description: HIS系统检验目的实体类
 * @Author: mcb
 * @Date:   2020-07-23
 * @Version: V1.0
 */
@Data
 public class ExLabPurpose {
    /**仪器编号*/
    @TableField(exist = false)
    private String yq;
    /**检验目的编号*/
    @TableField(exist = false)
    private String purno;
    /**检验目的名称*/
    @TableField(exist = false)
    private String pruna;




}
