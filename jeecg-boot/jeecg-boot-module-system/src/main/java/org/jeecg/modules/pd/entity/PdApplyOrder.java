package org.jeecg.modules.pd.entity;



import java.io.Serializable;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * @Description: 申领单主表
 * @Author: jeecg-boot
 * @Date:   2020-02-11
 * @Version: V1.0
 */
@Data
@TableName("pd_apply_order")
public class PdApplyOrder extends BaseEntity {
    private static final long serialVersionUID = 1L;
    
	/**主键*/
	@TableId(type = IdType.ID_WORKER_STR)
    private String id;
	/**创建人*/
    private String createBy;
	/**创建日期*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date createTime;
	/**更新人*/
    private String updateBy;
	/**更新日期*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date updateTime;
	/**所属部门*/
    private String sysOrgCode;
	/**申领单号*/
    private String applyNo;
    /*提交状态*/
    private String submitStart;
	/**申领人*/
    private String applyBy;
	/**申领日期*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private Date applyDate;
	/**申领科室ID*/
    private String deptId;
	/**申领科室名称*/
    private String deptName;
	/**申领总数*/
    private Integer applyNum;
	/**实际领用个数*/
    private Integer factCount;
	/**申领单状态*/
    private String applyStatus;
	/**审核人*/
    private String auditBy;
	/**审核时间*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private Date auditDate;
	/**审核意见*/
    private String refuseReason;
	/**备注*/
    private String remarks;
	/**是否在退货中*/
    private String isInRefund;
	/**删除标识*/
    private String delFlag;
	/**是否完结，1是，0否*/
    private String isEnd;
}
