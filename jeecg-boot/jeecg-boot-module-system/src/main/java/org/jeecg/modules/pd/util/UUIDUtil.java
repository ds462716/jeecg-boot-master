package org.jeecg.modules.pd.util;

import java.util.UUID;

/**
 * @Author: zxh
 * @Date: 2020年1月2日11:16:11
 */
public class UUIDUtil {

	/**
	 * 获取uuid
	 * @return
	 */
	public static String  getUuid(){
		return UUID.randomUUID().toString().replace("-", "");
	}
}
