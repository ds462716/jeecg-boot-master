package org.jeecg.modules.pd.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;

/**
 * 实体的公共父类
 */
@Data
public abstract class BaseEntity implements Serializable {
    private static final long serialVersionUID = 1L;

	public static final String DEL_FLAG_NORMAL = "0";
	public static final String DEL_FLAG_DELETE = "1";

	public BaseEntity() {
		this.delFlag = DEL_FLAG_NORMAL;
	}

	/**删除标志*/
	@TableLogic
	private String delFlag;
}
