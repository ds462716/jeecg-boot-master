package org.jeecg.modules.external.ganzhouwuyuan.util;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import org.apache.axiom.om.OMAbstractFactory;
import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.OMFactory;
import org.apache.axiom.om.OMNamespace;
import org.apache.axis2.AxisFault;
import org.apache.axis2.addressing.EndpointReference;
import org.apache.axis2.client.Options;
import org.apache.axis2.client.ServiceClient;
import org.apache.axis2.transport.http.HTTPConstants;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.constant.PdConstant;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.util.DateUtils;
import org.jeecg.modules.pd.entity.PdDosage;
import org.jeecg.modules.pd.entity.PdDosageDetail;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

/** 
 * 描述：axis2调用webservice工具类（赣州五院调接口用）
 * @author zxh
 * @version 2017年10月20日 下午2:51:07 
 */
public class AxisGZWYUtils {
	private static Logger logger = LoggerFactory.getLogger(AxisGZWYUtils.class);
	private static String defaultNamespace = "http://www.mingweisoft.com/";
	private static String drugInfoUrl = "http://192.168.0.128:8086/HisService.asmx?wsdl";
	private static String chargeUrl = "";
	private static String chargeCodeUrl = "";

	private static OMFactory fac = OMAbstractFactory.getOMFactory();

	//默认命名空间
	//public static final String defaultNamespace = "http://tempuri.org/";

	
	/**
	 * 获取发送客户端
	 * @param soapUrl 调用地址
	 * @param actionUrl 方法地址
	 * @return
	 * @throws AxisFault
	 */
	public static ServiceClient getClientSend(String soapUrl, String actionUrl) throws AxisFault{
		EndpointReference targetEPR = new EndpointReference(soapUrl);
		Options options = new Options();
		options.setManageSession(true);
		options.setTimeOutInMilliSeconds(9000L);
		options.setSoapVersionURI("http://schemas.xmlsoap.org/soap/envelope/");// 设定SOAP版本soap1.1
		//options.setSoapVersionURI(SOAP12Constants.SOAP_ENVELOPE_NAMESPACE_URI);// 设定SOAP版本soap1.2
		options.setProperty(HTTPConstants.REUSE_HTTP_CLIENT, true);
        options.setAction("");
        options.setTo(targetEPR);  
        options.setProperty(HTTPConstants.CHUNKED, "false");  
        ServiceClient sender = new ServiceClient();  
	    sender.setOptions(options);  
	    return sender;
	}

	public static void main(String[] args) throws AxisFault, NoSuchAlgorithmException, UnsupportedEncodingException {
		getBaseInfo();
	}
	
	
	public static String getJsonDataFromWebservice(String appointUrl, String appointNamespace, String appointMethod, Map<String,Object> params) throws AxisFault{
		ServiceClient sender = getClientSend(appointUrl, appointNamespace + appointMethod);
		OMNamespace omNs = fac.createOMNamespace(appointNamespace, "");
		OMNamespace omNs1 = fac.createOMNamespace("", "");
		OMElement method = fac.createOMElement(appointMethod, omNs);
		for(String key:params.keySet()){
			OMElement name = fac.createOMElement(key, omNs1);// 设置入参名称
			name.setText((String)params.get(key));
			method.addChild(name);
		}
		method.build();  
	    OMElement response = sender.sendReceive(method);
	    OMElement element = response.getFirstElement();
		return element.getText();
	}


	public static JSONObject getBaseInfo() {
		Map<String,Object> params = new HashMap<>();
		String str = "222";
		//JSONArray jsonObj = JSONArray.parseArray(str);
		params.put("purchaseOrder",str);
		JSONObject json = new JSONObject();
		try {
			String result = getJsonDataFromWebservice(drugInfoUrl, defaultNamespace, "sendPurchaseOrderToSpd", params);
			json = JSONObject.parseObject(result);
			if (!"0".equals(json.get("code"))) {
				json.put("code", "-200");
			}
		} catch (AxisFault e) {
			e.printStackTrace();
			logger.info("******调用HIS查询基础信息接口出现错误！******");
			json.put("code", "-200");
		} catch (JSONException ee) {
			ee.printStackTrace();
			json.put("code", "-200");
			logger.info("******调用HIS查询基础信息接口JSON转换返回信息出现错误！******");
		}
		return json;
	}




	/**
	 * 根据住院号或申请号查询病人信息
	 * @param pdDosage
	 * @return
	 */
	public static JSONObject getBaseInfoByInHospital(PdDosage pdDosage) {
		JSONObject json = new JSONObject();
		if(StringUtils.isEmpty(pdDosage.getInHospitalNo()) &&  StringUtils.isEmpty(pdDosage.getApplicationNumber())){
			json.put("code", "-200");
			json.put("msg","住院号或申请号不可为空");
			return json;
		}
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("cxbm", pdDosage.getInHospitalNo());
		params.put("cxappno", pdDosage.getApplicationNumber());
		try {
			//String result = getJsonDataFromWebservice(chargeUrl, defaultNamespace, "GetSurgInfo", params);
			 String result = getJsonDataFromWebservice(chargeUrl, defaultNamespace, "GetApplication", params);
			//String result="{'code':'0','msg':'查询成功','data':[{'手术编号':'12684','所属病区编码':'624','手术名称':'深静脉置管术','所属病区':'肝病科二区','所属科室编码':'624','所属科室':'肝病科二区','手术科室':'手术室','申请医生姓名编码':'749','申请医生姓名':'胡江玲','门诊号':'00065299','姓名':'刘荣祜','住院号':'00065299','床位号':'9451','性别':'男','登记日期':'2019/12/2 23:09:00','申请编号':'12684','住院标识':'1'},{'手术编号':'12696','所属病区编码':'624','手术名称':'无痛胃镜介入治疗','所属病区':'肝病科二区','所属科室编码':'624','所属科室':'肝病科二区','手术科室':'手术室','申请医生姓名编码':'749','申请医生姓名':'胡江玲','门诊号':'00065299','姓名':'刘荣祜','住院号':'00065299','床位号':'9451','性别':'男','登记日期':'2019/12/3 12:52:00','申请编号':'12696','住院标识':'1'}]}";
			//String result="{'code':'0','msg':'查询成功','data':[{'手术编号':'1124080','所属病区编码':'649','手术名称':'','所属病区':'内科三区','所属科室编码':'649','所属科室':'内科三区','手术科室':'','申请医生姓名编码':'413','申请医生姓名':'叶远飞','门诊号':'1500017512','姓名':'舒启凡','住院号':'1500017512','床位号':'','性别':'男','登记日期':'2019/12/4 8:16:52','申请编号':'2019120400022','住院标识':'2'}]}";
			//String result="{'code':'0','msg':'查询成功','data':[{'手术编号':'','所属病区编码':'','手术名称':'','所属病区':'','所属科室编码':'','所属科室':'','手术科室':'','申请医生姓名编码':'','申请医生姓名':'','门诊号':'00070278','姓名':'许金连','住院号':'00070278','床位号':'9938','性别':'女','登记日期':'','申请编号':'','住院标识':'1'}]}";
			json = JSONObject.parseObject(result);
			if (!"0".equals(json.get("code"))) {
				json.put("code", "-200");
				json.put("msg","查询失败，未找到记录");
			}
		}catch (AxisFault e) {
			e.printStackTrace();
			logger.info("******调用HIS根据住院号查询病人信息接口出现错误！******");
			json.put("code", "-200");
			json.put("msg","调用HIS根据住院号查询病人信息接口出现错误！");
		} catch  (JSONException ee) {
			ee.printStackTrace();
			json.put("code", "-200");
			logger.info("******调用HIS根据住院号查询病人信息接口JSON转换返回信息出现错误！******");
			json.put("msg","调用HIS根据住院号查询病人信息接口JSON转换返回信息出现错误！");
		}
		return json;
	}/**/


	/**
	 * 收费接口
	 * @param pdDosage  chargeType    1：收费；0：退费
	 * @return
	 */
	public static JSONObject exeCharge(PdDosage pdDosage,  PdDosageDetail dosageDetail) {
		LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
		JSONObject json = new JSONObject();
		//参数校验
		//库房id,收费代码，产品编号，产品数量，住院号，收费类型,HIS用户编码，当前时间，备注，唯一号，手术编号
		String departId=sysUser.getCurrentDepartId();
		String chargeCode=dosageDetail.getChargeCode();
		String inHospitalNo=pdDosage.getInHospitalNo();
		Double productNum=dosageDetail.getDosageCount();
		 if(chargeCode==null ||"".equals(chargeCode)|| productNum==null ||"".equals(productNum) ||
				inHospitalNo==null ||"".equals(inHospitalNo)){
			json.put("code", "-200");
			json.put("msg", "参数异常");
			return json;
		}
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("cxbm", chargeCode);
		params.put("cxlx", dosageDetail.getProductNumber());
		params.put("cxsl", productNum);
		params.put("zyh", inHospitalNo);
		if("d8c060c562f8485eb9c3a639674c32e6".equals(departId)
				|| "65caa20e7fab4b8b9a6b23806a8dfde8".equals(departId)
				|| "11eeda15c89f421fa656fa74879edf67".equals(departId)){
			params.put("ssbm", "0");//传0的话，HIS系统会记录到手工记账那里，而不是手术项目收费那里；
		}else{
			params.put("ssbm", pdDosage.getOperativeNumber());
		}
		params.put("sflx", PdConstant.IS_CHARGE_TYPE_1);//收费类型
		params.put("czr", sysUser.getWorkNo());
		params.put("czsj", DateUtils.date2Str(DateUtils.datetimeFormat.get()));
		params.put("remark", "");
		params.put("token",System.currentTimeMillis());
		String result = null;
		try {
			result = getJsonDataFromWebservice(chargeUrl, defaultNamespace, "Charges", params);
			json = JSONObject.parseObject(result);
			if (!"0".equals(json.get("code"))) {
				json.put("code", "-200");
				json.put("msg", "执行收费接口出错");
			}
		} catch (AxisFault e) {
			e.printStackTrace();
			logger.info("******调用HIS收费接口出现错误！--->{}******", result);
			json.put("code", "-200");
			json.put("msg", "调用HIS收费接口出现错误");
		} catch (Exception ee) {
			ee.printStackTrace();
			logger.info("******调用HIS收费接口JSON转换返回信息出现错误！--->{}******", result);
			json.put("code", "-200");
			json.put("msg", "调用HIS收费接口JSON转换返回信息出现错误！");
		}

		return json;
	}





	/**
	 * 退费接口
	 chargeType    1：收费；0：退费
	 * @return
	 */
	public static JSONObject exeRefund(PdDosage pdDosage,  PdDosageDetail dosageDetail) {
		//参数校验
		LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
		JSONObject json = new JSONObject();
		//参数校验
		String departId=sysUser.getCurrentDepartId();
		String chargeCode=dosageDetail.getChargeCode();
		String inHospitalNo=pdDosage.getInHospitalNo();
		Double productNum=dosageDetail.getDosageCount();
		if(chargeCode==null ||"".equals(chargeCode)|| productNum==null ||"".equals(productNum) ||
				inHospitalNo==null ||"".equals(inHospitalNo)){
			json.put("code", "-200");
			json.put("msg", "参数异常");
			return json;
		}
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("cxbm", chargeCode);
		params.put("cxlx", dosageDetail.getProductNumber());
		params.put("cxsl", "-"+productNum);
		params.put("zyh", inHospitalNo);

		if("d8c060c562f8485eb9c3a639674c32e6".equals(departId)
				|| "65caa20e7fab4b8b9a6b23806a8dfde8".equals(departId)
				|| "11eeda15c89f421fa656fa74879edf67".equals(departId)){
			params.put("ssbm", "0");//传0的话，HIS系统会记录到手工记账那里，而不是手术项目收费那里；
		}else{
			params.put("ssbm",  pdDosage.getOperativeNumber());
		}
		params.put("sflx",PdConstant.IS_CHARGE_TYPE_0);//收费类型
		params.put("czr", sysUser.getWorkNo());
		params.put("czsj", DateUtils.date2Str(DateUtils.datetimeFormat.get()));
		params.put("remark", "");
		params.put("token",System.currentTimeMillis()+(Math.random()*100));
		String result = null;
		try {
			result = getJsonDataFromWebservice(chargeUrl, defaultNamespace, "Charges", params);
			json = JSONObject.parseObject(result);
			if (!"0".equals(json.get("code"))) {
				json.put("code", "-200");
			}
		} catch (AxisFault e) {
			e.printStackTrace();
			json.put("code", "-200");
			logger.info("******调用HIS退费接口出现错误！--->{}******", result);
		} catch (Exception ee) {
			ee.printStackTrace();
			logger.info("******调用HIS退费接口JSON转换返回信息出现错误！--->{}******", result);
			json.put("code", "-200");
		}
		return json;
	}
}
