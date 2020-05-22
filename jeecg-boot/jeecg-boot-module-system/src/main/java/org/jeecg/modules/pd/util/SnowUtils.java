package org.jeecg.modules.pd.util;


import java.math.BigInteger;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @Author: zxh
 * @Date: 2020年1月2日11:16:11
 */
public class SnowUtils {
	private static final int minNum = 1024;
	private static final int strMinNum = 100;
	private static final AtomicInteger bigAtomic = new AtomicInteger(minNum);
	private static final AtomicInteger strAtomic = new AtomicInteger(strMinNum);
	private static Integer machineCode=null;
	private static Long recordSecond=null;
	private static Long recordTimeMillis=null;

	private static void checkAndInit() {
		if(machineCode==null){
			synchronized (SnowUtils.class){
				if(machineCode==null){
					initData();
				}
			}
		}
	}

	private static void initData(){
		recordSecond = LocalDateTime.now().toEpochSecond(ZoneOffset.of("+8"));
		recordTimeMillis = System.currentTimeMillis();
		try {
			String machineIp = InetAddress.getLocalHost().getHostAddress();
			String codeStr = machineIp.substring(machineIp.length() - 1);
			machineCode = new BigInteger(codeStr).intValue();
		} catch (UnknownHostException e) {
			machineCode = 3;
		}
	}

	/**
	 * 获取15位长度的自增长Key
	 * 每秒可生成8970个不重复编号
	 * @return long
	 */
	public static String bigKey() {
		checkAndInit();
		if (LocalDateTime.now().toEpochSecond(ZoneOffset.of("+8")) != recordSecond) {
			recordSecond = LocalDateTime.now().toEpochSecond(ZoneOffset.of("+8"));
			bigAtomic.set(minNum);
		}
		String differ = String.valueOf(recordSecond);
		int atomic = bigAtomic.getAndIncrement();
		if (atomic == 9999) {
			String text = "bigKey超出阈值:" + LocalDateTime.now().toString();
			throw new RuntimeException(text);
		}
		return differ + atomic + machineCode;
	}

	/**
	 * 获取11位不重复Key
	 * 每毫秒可生成890个不重复的11位编号
	 * @return String
	 */
	public static String strKey() {
		checkAndInit();
		long localTimeMillis=System.currentTimeMillis();
		if(localTimeMillis!=recordTimeMillis){
			recordTimeMillis = localTimeMillis;
			strAtomic.set(strMinNum);
		}
		String differ = String.valueOf(recordTimeMillis);
		int atomic = strAtomic.getAndIncrement();
		long bigNum=new BigInteger(differ+atomic+machineCode).longValue();
		if (atomic == 999) {
			String text = "strKey超出阈值:" + LocalDateTime.now().toString();
			throw new RuntimeException(text);
		}
		return Long.toUnsignedString(bigNum,32).toUpperCase();
	}

	public static void main(String [] args){
		for(int i = 0;i<1;i++){
			//System.out.println(strKey());
			System.out.println(bigKey());
		}
	}
}
