package org.jeecg.modules.pd.util;


import org.jeecg.common.constant.MessageConstant;
import org.jeecg.common.constant.PdConstant;
import org.jeecg.common.util.DateUtils;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author: zxh
 * @Date: 2020年2月19日15:59:58
 */
public class BarCodeUtil {

	/**
	 * 扫码
	 * @param Barcode1
	 * @param Barcode2
	 */
	public static Map<String,Object> scanCode(String Barcode1, String Barcode2,Map<String,Object> resultMap){
		if((Barcode1.startsWith("+") && Barcode2.startsWith("+")) ||(Barcode1.startsWith("+")  && Barcode2.startsWith("/"))){
			//HIBC扫码
            resultMap = HIBCBarcodeDec(Barcode1, Barcode2,resultMap);
		}else{
			//EAN扫码
            resultMap = EANBarcodeDec(Barcode1, Barcode2,resultMap);
		}
		System.out.println(resultMap);
		return resultMap;
	}

	/**
	 * 获取产品编号   如果修改  需要同步修改barcode.js的getPrdNumber方法  和当前类HIBCBarcodeDec方法  2020年2月22日11:19:57
	 * @param Barcode
	 * @return
	 */
	public static String getPrdNumber(String Barcode){
		String number = "";
		if(Barcode.startsWith("+")){
			int x = getSubCount(Barcode,"+");
			int y = getSubCount(Barcode,"/");
			if(x==1 && y==1 && Barcode.startsWith("+")){
				//如果是 +J123123451/122713C001L5"  一个+一个/
				int i = Barcode.indexOf("/");
				Barcode = Barcode.substring(0, i);
			}else if(x>1 && y==0 && Barcode.startsWith("+")){
				//如果是 +J123123451+122713C001L5"  两个+类型
				Barcode = Barcode.substring(0, Barcode.indexOf("+",Barcode.indexOf("+")+1));
			}else if(x>1 && y==1 && Barcode.endsWith("+")){
				//如果是 +J123123451/122713C001L5+"  两个+一个/
				int i = Barcode.indexOf("/");
				Barcode = Barcode.substring(0, i);
			}else if(x>1 && y==1 && Barcode.endsWith("/")){
				//如果是 +J123123451+122713C001L5"  两个+类型 一个/类型
				Barcode = Barcode.substring(0, Barcode.indexOf("+",Barcode.indexOf("+")+1));
			}else if (x==1 && y>1 && Barcode.endsWith("/")){
				//如果是 +J123123451/122713C001L5/"  一个+两个//
				int i = Barcode.indexOf("/");
				Barcode = Barcode.substring(0, i);
			}
			number = Barcode.substring(1, Barcode.length() - 2);
		}else{
			number = GetEANUPN(Barcode);
		}
		return number;
	}

	/**
	 * HIBC条码解析
	 * @param Barcode1
	 * @param Barcode2
	 * @return
	 */
	public static   Map<String,Object>  HIBCBarcodeDec(String Barcode1, String Barcode2,Map<String,Object> result){
		result.put("code",MessageConstant.CODE_STATE_200);
		result.put("msg",MessageConstant.CODE_MESSAGE_2);
		//条码扫了两遍
		if (Barcode1.equals(Barcode2)){
			int x = getSubCount(Barcode1,"+");
			int y = getSubCount(Barcode1,"/");
			if(x==1 && y==1 && Barcode1.startsWith("+")) {
				//如果是 +J123123451/122713C001L5"  一个+一个/
				int i = Barcode1.indexOf("/");
				Barcode2 = Barcode1.substring(i + 1, Barcode1.length());
				Barcode2 = "+" + Barcode2;
				Barcode1 = Barcode1.substring(0, i);
				//解析产品批号和条码
				result = GetHIBCLot(Barcode2, result);
			}else if(x>1 && y==0 && Barcode1.startsWith("+")){
				//如果是 +J123123451+122713C001L5"  两个+类型
				Barcode2 = Barcode1.substring(Barcode1.indexOf("+",Barcode1.indexOf("+")+1), Barcode1.length());
				Barcode1 = Barcode1.substring(0, Barcode1.indexOf("+",Barcode1.indexOf("+")+1));
				//解析产品批号和条码
				result = GetHIBCLot(Barcode2,result);
			}else if(x>1 && y==1 && Barcode1.endsWith("+")){
				//如果是 +J123123451/122713C001L5+"  两个+一个/
				int i = Barcode1.indexOf("/");
				Barcode2 =Barcode1.substring(i+1,Barcode1.length());
				Barcode2 = "+" +Barcode2;
				Barcode1 = Barcode1.substring(0, i);
				//解析产品批号和条码
				result = GetHIBCLot(Barcode2,result);
			}else if(x>1 && y==1 && Barcode1.endsWith("/")){
				//如果是 +J123123451+122713C001L5/"  两个+一个/
				Barcode2 = Barcode1.substring(Barcode1.indexOf("+",Barcode1.indexOf("+")+1), Barcode1.length());
				Barcode1 = Barcode1.substring(0, Barcode1.indexOf("+",Barcode1.indexOf("+")+1));
				//解析产品批号和条码
				result = GetHIBCLot(Barcode2,result);
			}else if (x==1 && y>1 && Barcode1.endsWith("/")){
				//如果是 +J123123451/122713C001L5/"  一个+两个//
				int i = Barcode1.indexOf("/");
				Barcode2 =Barcode1.substring(i+1,Barcode1.length());
				Barcode2 = "+" +Barcode2;
				Barcode1 = Barcode1.substring(0, i);
				//解析产品批号和条码
				result = GetHIBCLot(Barcode2,result);
			}
			//解析产品编号
			result.put("number",getHIBCUPN(Barcode1));
		}else{
			//解析产品编号
			result.put("number",getHIBCUPN(Barcode1));
			if(Barcode2.startsWith("/")){
				Barcode2 = "+"+Barcode2.substring(1,Barcode2.length());
			}
			//解析产品批号和条码
			result = GetHIBCLot(Barcode2,result);
		}
		return result;
	}

	/**
	 * 去除+号
	 * @param HIBCUPNBarcode
	 * @return
	 */
	public static  String getHIBCUPN(String HIBCUPNBarcode){
		return HIBCUPNBarcode.substring(1, HIBCUPNBarcode.length()-2);
	}

	/**
	 * gs1条码解析
	 * @param Barcode1
	 * @param Barcode2
	 * @return
	 */
	public static  Map<String,Object> EANBarcodeDec(String Barcode1, String Barcode2,Map<String,Object> result){
		//条码正常
		result.put("code",MessageConstant.CODE_STATE_200);
        result.put("msg",MessageConstant.CODE_MESSAGE_2);
		//Barcode1 = 01XXXXXXXXXXXXXX17XXXXXX10XXXXXX   ,Barcode2 = 01XXXXXXXXXXXXXX17XXXXXX10XXXXXX
		//Barcode1 = 01XXXXXXXXXXXXXX, Barcode2 = 17XXXXXX10XXXXXX
		if (Barcode1.equals(Barcode2)){
			if("01".equals(Barcode1.substring(0, 2)) || "02".equals(Barcode1.substring(0, 2))){
				//大于24位则可能包含产品编号和生产日期或者有效期 //01(14)17(6)   2+14+2+6 = 24  如果大于24位
				if(Barcode1.length() >= 24){
					Barcode2 = Barcode1.substring(16, Barcode2.length());
					Barcode1 = Barcode1.substring(0, 16);
				}
				//解析产品有效期及批号
				result = GetEANLot(Barcode2,result);
			}else if("00".equals(Barcode1.substring(0, 2))){
				//Barcode1 = 01XXXXXXXXXXXXXXXXXX17XXXXXX10XXXXXX   ,Barcode2 = 01XXXXXXXXXXXXXXXXXX17XXXXXX10XXXXXX
				//Barcode1 = 01XXXXXXXXXXXXXXXXXX, Barcode2 = 17XXXXXX10XXXXXX
				if(Barcode1.length() >= 28){
					//大于28位则可能包含产品编号和生产日期或者有效期
					Barcode2 = Barcode1.substring(20, Barcode2.length());
					Barcode1 = Barcode1.substring(0, 20);
				}
				//解析产品有效期及批号
				result = GetEANLot(Barcode2,result);
			}
			//解析产品编号
			result.put("number",GetEANUPN(Barcode1));
		}else{
			if("01".equals(Barcode1.substring(0, 2))  || "02".equals(Barcode1.substring(0, 2))){
				if(Barcode1.length() >= 16){
					String lsCode1 = Barcode1;
					Barcode1 = Barcode1.substring(0, 16);
					Barcode2 = lsCode1.substring(16, lsCode1.length())+Barcode2;
				}
				//解析产品批号和条码
				result = GetEANLot(Barcode2,result);
			}else if("00".equals(Barcode1.substring(0, 2))){
				//Barcode1 = 01XXXXXXXXXXXXXXXXXX17XXXXXX10XXXXXX   ,Barcode2 = 01XXXXXXXXXXXXXXXXXX17XXXXXX10XXXXXX
				//Barcode1 = 01XXXXXXXXXXXXXXXXXX, Barcode2 = 17XXXXXX10XXXXXX
				if(Barcode1.length() >= 20){
					String lsCode1 = Barcode1;
					Barcode1 = Barcode1.substring(0, 20);
					Barcode2 = lsCode1.substring(20, lsCode1.length())+Barcode2;
				}
				//解析产品批号和条码
				result = GetEANLot(Barcode2,result);
			}
			//解析产品编号
			result.put("number",GetEANUPN(Barcode1));
		}
		return result;
	}

	/**
	 * HIBC条码解析
	 * @param Barcode1
	 * @param Barcode2
	 * @return
	 */
	public static Map<String,Object> GetHIBCLot(String LotBarcode,Map<String,Object> result){
		//有效期
		String expDate = "";
		//批号
		String batchNo = "";
		String yeal = "";
		String mDate = "";
		try{
			//标志位解析
			if ("+$$".equals(LotBarcode.substring(0, 3))) {
				String _QtyFormatChar = "";
				_QtyFormatChar = LotBarcode.substring(3, 4);
				switch (_QtyFormatChar) {
					case "2":
						//+$$20215123C001L/   有效期:2012-02-15   批号:3C001   链接字符:L   校验字符:/  日期格式:MMDDYY
						expDate = "20" + LotBarcode.substring(8, 10) + "-" + LotBarcode.substring(4, 6) + "-" + LotBarcode.substring(6, 8);
						batchNo = LotBarcode.substring(10, LotBarcode.length()-2);
						break;
					case "3":
						//+$$31202153C001L+    有效期:2012-02-15   批号:3C001   链接字符:L   校验字符:+  日期格式:YYMMDD
						expDate = "20" + LotBarcode.substring(4, 6) + "-" + LotBarcode.substring(6, 8) + "-" + LotBarcode.substring(8, 10);
						batchNo = LotBarcode.substring(10, LotBarcode.length()-2);
						break;
					case "4":
						//+$$4120216113C001L2  有效期:2012-02-16   批号:3C001   链接字符:L   校验字符:2  日期格式:YYMMDDHH
						expDate = "20" + LotBarcode.substring(4, 6) + "-" + LotBarcode.substring(6, 8) + "-" + LotBarcode.substring(8, 10);
						batchNo = LotBarcode.substring(12, LotBarcode.length()-2);
						break;
					case "5":
						//+$$5122713C001L2     有效期:2012-09-27   批号:3C001   链接字符:L   校验字符:2  日期格式:YYJJJ
						yeal = 20+ LotBarcode.substring(4, 6);
						mDate = ZlrToDateUtil.getDateStr(yeal,LotBarcode.substring(6, 9));
						expDate = "20" + LotBarcode.substring(4, 6) +"-" +  mDate;
						batchNo = LotBarcode.substring(9, LotBarcode.length()-2);
						break;
					case "6":
						//+$$612271113C001L5     有效期:2012-09-27   批号:3C001   链接字符:L   校验字符:5  日期格式:YYJJJHH
						yeal = 20 + LotBarcode.substring(4, 6);
						mDate = ZlrToDateUtil.getDateStr(yeal,LotBarcode.substring(6, 9));
						expDate = "20" + LotBarcode.substring(4, 6) +"-" +  mDate;
						batchNo = LotBarcode.substring(11, LotBarcode.length()-2);
						break;
					case "7":
						//+$$73C001LY     有效期:   批号:3C001   链接字符:L   校验字符:Y  日期格式:
						expDate = "";
						batchNo = LotBarcode.substring(4, LotBarcode.length()-2);
						break;
					case "8":
						String expDateFlag = LotBarcode.substring(6, 7);
						switch (expDateFlag){
							case "2":
								//+$$82420216123C001LC  有效期:2012-02-16   批号:3C001   链接字符:L   校验字符:C  日期格式:MMDDYY
								expDate = "20" + LotBarcode.substring(11, 13) + "-" + LotBarcode.substring(7, 9) + "-" + LotBarcode.substring(9, 11);
								batchNo = LotBarcode.substring(13, LotBarcode.length());
								break;
							case "3":
								//+$$82431202163C001LD  有效期:2012-02-16   批号:3C001   链接字符:L   校验字符:D  日期格式:YYMMDD
								expDate = "20" + LotBarcode.substring(7, 9) + "-" + LotBarcode.substring(9, 11) + "-" + LotBarcode.substring(11, 13);
								batchNo = LotBarcode.substring(13, LotBarcode.length()-2);
								break;
							case "4":
								//+$$8244120216183C001LN  有效期:2012-02-16   批号:3C001   链接字符:L   校验字符:N  日期格式:YYMMDDHH
								expDate = "20" + LotBarcode.substring(7, 9) + "-" + LotBarcode.substring(9, 11) + "-" + LotBarcode.substring(11, 13);
								batchNo = LotBarcode.substring(15, LotBarcode.length()-2);
								break;
							case "5":
								//+$$8245122713C001LG   有效期:2012-09-27   批号:3C001   链接字符:L   校验字符:G  日期格式:YYJJJ
								yeal = 20+ LotBarcode.substring(7, 9);
								mDate = ZlrToDateUtil.getDateStr(yeal,LotBarcode.substring(9, 12));
								expDate = "20" + LotBarcode.substring(7, 9) +"-" +  mDate;
								batchNo = LotBarcode.substring(12, LotBarcode.length()-2);
								break;
							case "6":
								//+$$824612271183C001LQ   有效期:2012-09-27   批号:3C001   链接字符:L   校验字符:Q  日期格式:YYJJJHH
								yeal = 20 + LotBarcode.substring(7, 9);
								mDate = ZlrToDateUtil.getDateStr(yeal,LotBarcode.substring(9, 12));
								expDate = "20" + LotBarcode.substring(7, 9) +"-" +  mDate;
								batchNo = LotBarcode.substring(14, LotBarcode.length()-2);
								break;
							case "7":
								//+$$82473C001L5    有效期:   批号:3C001   链接字符:L   校验字符:5  日期格式:
								expDate = "";
								batchNo = LotBarcode.substring(7, LotBarcode.length()-2);
								break;
							default:
								if(LotBarcode.length()>8){
									//+$$82402123C001L3     有效期:2012-02-01   批号:3C001   链接字符:L   校验字符:3  日期格式:MMYY
									expDate = "20" + LotBarcode.substring(8, 10) + "-" + LotBarcode.substring(6, 8) +"-01";
									batchNo = LotBarcode.substring(10, LotBarcode.length()-2);
								}else{
									//+$$824LP  效期:   批号:   链接字符:L   校验字符:P  日期格式:
									//不包含有效期及批号
								}
						}
						break;
					case "9":
						//+$$90010009953C001LH    有效期:2012-02-01   批号:3C001   链接字符:L   校验字符:H  日期格式:MMYY
						expDate = "20" + LotBarcode.substring(11, 13) + "-" + LotBarcode.substring(9, 11) +"-01";
						batchNo = LotBarcode.substring(13, LotBarcode.length()-2);
						break;
					default:
						//+$$12123C001LW     有效期:2012-12-01   批号:3C001   链接字符:L   校验字符:W  日期格式:MMYY
						expDate = "20" + LotBarcode.substring(5, 7) + "-" + LotBarcode.substring(3, 5) +"-01";
						batchNo = LotBarcode.substring(7, LotBarcode.length()-2);
						break;
				}
			} else if ("+$".equals(LotBarcode.substring(0, 2))) {
				//+$3C001LV     有效期   批号3C001   L链接字符   V校验字符
				batchNo = LotBarcode.substring(2, LotBarcode.length()-2);
				expDate = "";
			} else if ("+".equals(LotBarcode.substring(0, 1))) {
				//+122713C001L5     有效期 2012-09-27   批号3C001   L链接字符   5校验字符
				yeal = 20 + LotBarcode.substring(1, 3);
				mDate = ZlrToDateUtil.getDateStr(yeal,LotBarcode.substring(3, 6));
				expDate = "20" + LotBarcode.substring(1, 3) +"-" +  mDate;
				batchNo = LotBarcode.substring(6, LotBarcode.length()-2);
			}
			if(expDate!=null && !"".equals(expDate)){
				long difDays = dateDiff(expDate);
				//效期提醒时间
				if (difDays < PdConstant.REMINDER_TIME){
					if (difDays < 0) {
						//产品过期
						result.put("code",MessageConstant.CODE_STATE_201);
						result.put("msg", MessageConstant.CODE_MESSAGE_6);
					}else{
						//近有效期
						result.put("code",MessageConstant.ICODE_STATE_203);
						result.put("msg",MessageConstant.CODE_MESSAGE_7);
					}
				}
			}
			result.put("expDate",expDate);
			result.put("batchNo",batchNo);
		}catch (Exception e){
			result.put("code",MessageConstant.CODE_STATE_500);
			result.put("msg",MessageConstant.CODE_MESSAGE_8);
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * gs1码解析产品编号
	 * @param EANUPNBarcode
	 * @return
	 */
	public static String  GetEANUPN(String EANUPNBarcode){
		String UPN = "";
		if ("01" .equals(EANUPNBarcode.substring(0, 2)) || "02".equals(EANUPNBarcode.substring(0, 2))) {
			UPN = EANUPNBarcode.substring(2, 16);
		}else if("00".equals(EANUPNBarcode.substring(0, 2))){
			UPN = EANUPNBarcode.substring(2, 20);
		} else if("93".equals(EANUPNBarcode.substring(0, 2))){
			UPN = EANUPNBarcode.substring(0, 19);
		}else if(EANUPNBarcode.length()==13){
			UPN = EANUPNBarcode.substring(0, 13);
		}else{
			UPN = EANUPNBarcode;
		}
		return UPN;
	}

	/**
	 * gs1解析有效期和产品批号
	 * @param LotBarcode
	 */
	public static Map<String,Object> GetEANLot(String LotBarcode,Map<String,Object> result){
		String batchNo = "";
		try{
			//有效期
			if ((LotBarcode.length() > 2) && ("17".equals(LotBarcode.substring(0, 2)))) {
				String ri = LotBarcode.substring(6, 8);
				if ("00".equals(ri)) ri = "01";
				String expDate = "20" + LotBarcode.substring(2, 4) + "-" + LotBarcode.substring(4, 6) + "-" + ri;
				result.put("expDate",expDate);
				//17 121100 30 01 10 091222
				LotBarcode = LotBarcode.substring(8, LotBarcode.length());
				if(expDate!=null && !"".equals(expDate)){
					long difDays = dateDiff(expDate);
					//效期提醒时间
					if (difDays < PdConstant.REMINDER_TIME){
                        if (difDays < 0) {
                            //产品过期
                            result.put("code",MessageConstant.CODE_STATE_201);
                            result.put("msg", MessageConstant.CODE_MESSAGE_6);
                        }else{
                            //近有效期
                            result.put("code",MessageConstant.ICODE_STATE_203);
                            result.put("msg",MessageConstant.CODE_MESSAGE_7);
                        }
					}
				}
			}

			//生产日期
			if ((LotBarcode.length() > 2) && "11".equals(LotBarcode.substring(0, 2))) {
				LotBarcode = LotBarcode.substring(8, LotBarcode.length());
			}
			//其他日期
			if ((LotBarcode.length()>2) && (("13".equals(LotBarcode.substring(0, 2))) || ("15".equals(LotBarcode.substring(0, 2))))) {
				LotBarcode = LotBarcode.substring(8, LotBarcode.length());
			}

			if ((LotBarcode.length() > 3) && ("23".equals(LotBarcode.substring(0, 2)))) {
				batchNo = "";
			}
			if (LotBarcode.indexOf("10") < 0) {
				batchNo = "";
			} else {
				//3a00110091222
				int x = LotBarcode.indexOf("10");
				batchNo = LotBarcode.substring(x + 2, LotBarcode.length());
				if(batchNo.length() < 4) {
					batchNo = "";
				}
			}
			if ("".equals(batchNo) && LotBarcode.indexOf("21") >= 0) {
				int y = LotBarcode.indexOf("21");
				batchNo = LotBarcode.substring(y + 2, LotBarcode.length());
			}
			if (batchNo.length() >= 10) {
				int mx = batchNo.lastIndexOf("91");
				if (mx > 0) {
					if ((batchNo.length() - mx) == 6) {
						batchNo = batchNo.substring(0, mx);
					}
				}
			}
			result.put("batchNo",batchNo.toUpperCase());
		}catch(Exception e){
            result.put("code",MessageConstant.CODE_STATE_500);
            result.put("msg",MessageConstant.CODE_MESSAGE_8);
			e.printStackTrace();
		}
		return result;
	}

	//计算传入时间与现在时间天数差
	public static long  dateDiff(String sDate){
		Date d1 = DateUtils.str2Date(sDate,DateUtils.date_sdf.get());
		Date now = new Date();
		long days = d1.getTime() - now.getTime();
		long time = (days / (1000 * 60 * 60 * 24));
		return time;
	}

	/**
	 * 解析产品编号
	 * @param resultMap
	 * @param tempMap
	 * @return
	 */
	public static Map<String,Object> getProductNumber(Map<String, Object> resultMap, Map<String, String> tempMap){
		//如果是01开头的规则
		if(tempMap.containsKey("01")&&!tempMap.containsKey("00")&&!tempMap.containsKey("#")){
			resultMap.put("number",tempMap.get("01"));
		}else if(tempMap.containsKey("00")&&!tempMap.containsKey("01")&&!tempMap.containsKey("#")){
			//00开头
			resultMap.put("number",tempMap.get("00"));
		}else if(tempMap.containsKey("#")&&!tempMap.containsKey("01")&&!tempMap.containsKey("00")){
			//#开头
			resultMap.put("number",tempMap.get("#"));
		}else{
			//不包含产品编号 出错
			resultMap.put("code",MessageConstant.CODE_STATE_500);
			resultMap.put("msg",MessageConstant.CODE_MESSAGE_4);
		}
		return resultMap;
	}

	/**
	 * 解析产品批号
	 * @param resultMap
	 * @param tempMap
	 * @return
	 */
	public static Map<String,Object> getProductBatchNo(Map<String,Object> resultMap,Map<String,String> tempMap){
		//如果是10开头的批号
		if(tempMap.containsKey("10")&&!tempMap.containsKey("21")){
			resultMap.put("batchNo",tempMap.get("10"));
		}else if(tempMap.containsKey("21")&&!tempMap.containsKey("10")){
			//21开头
			resultMap.put("batchNo",tempMap.get("21"));
		}else if(tempMap.containsKey("10")&&tempMap.containsKey("21")){
			//有10又有21、
			resultMap.put("number",tempMap.get("10")+tempMap.get("21"));
		}else{
			//不包含产品编号 出错
            resultMap.put("code",MessageConstant.CODE_STATE_500);
            resultMap.put("msg",MessageConstant.CODE_MESSAGE_4);
		}
		return resultMap;
	}

	/**
	 * 解析产品有效期
	 * @param resultMap
	 * @param tempMap
	 * @return
	 */
	public static Map<String,Object> getProductExpDate(Map<String,Object> resultMap,Map<String,String> tempMap){
		if(tempMap.containsKey("17")){
			String expDate = "";
			expDate = tempMap.get("17");
			expDate = "20" + expDate.substring(0, 2) + "-" + expDate.substring(2, 4) + "-" + expDate.substring(4, 6);  //   20YY-MM-DD
			resultMap.put("expDate",expDate);
            if(expDate!=null && !"".equals(expDate)){
                long difDays = dateDiff(expDate);
                //效期提醒时间
                if (difDays < PdConstant.REMINDER_TIME){
                    if (difDays < 0) {
                        //产品过期
                        resultMap.put("code",MessageConstant.CODE_STATE_201);
                        resultMap.put("msg", MessageConstant.CODE_MESSAGE_6);
                    }else{
                        //近有效期
                        resultMap.put("code",MessageConstant.ICODE_STATE_203);
                        resultMap.put("msg",MessageConstant.CODE_MESSAGE_7);
                    }
                }
            }
		}
		return resultMap;
	}

	/**
	 * 字符串去空格
	 * @param str
	 * @return
	 */
	public static String trimStr(String str){
		//字符串去空格
		return str.replaceAll(" ", "");
	}

	/**
	 * 是否包含多个字符串
	 * @param str
	 * @param key
	 * @return
	 */
	public static int getSubCount(String str, String key) {
		if(str!=null && !"".equals(str) && key !=null && !"".equals(key)){
			int count = 0;
			int index = 0;
			while ((index = str.indexOf(key, index)) != -1) {
				index = index + key.length();

				count++;
			}
			return count;
		}else{
			return 0;
		}
	}

	public static void main(String [] args  ){

	}
}