package org.jeecg.modules.pd.util;

import org.jeecg.common.util.DateUtils;

import java.util.Date;
import java.util.Random;
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


	/**
	 * 根据单号首字母类型生成单号
	 * @return
	 */
	public static String generateOrderNoByType(String type){
		StringBuffer sb = new StringBuffer();
		//首字母
		sb.append(type);
		//时间戳
		sb.append(getCurrentTimeNum());
		//随机数
		sb.append(randomInt(3));
		return sb.toString();
	}


	//获取时间精确到毫秒
	public static String getCurrentTimeNum(){
		return DateUtils.getDate("MMddHHmmss");
	}


	/**
	 * 随机生成你设定长度为n的数字字符串
	 * @return
	 */
	public static String randomInt(int n){
		if(n == 0 || n < 0){
			return null;
		}
		StringBuilder sb = new StringBuilder();
		Random random = new Random();
		for(int i = 0; i < n; i++){
			sb.append(String.valueOf(random.nextInt(10)));
		}
		return sb.toString();
	}


}
