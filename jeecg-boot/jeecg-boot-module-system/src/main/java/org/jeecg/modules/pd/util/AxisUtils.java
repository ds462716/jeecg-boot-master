package org.jeecg.modules.pd.util;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import org.apache.axiom.om.*;
import org.apache.axiom.soap.SOAP12Constants;
import org.apache.axis2.AxisFault;
import org.apache.axis2.addressing.EndpointReference;
import org.apache.axis2.client.Options;
import org.apache.axis2.client.ServiceClient;
import org.apache.axis2.transport.http.HTTPConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.*;

/** 
 * 描述：axis2调用webservice工具类
 * @author zxh
 * @version 2017年10月20日 下午2:51:07 
 */
public class AxisUtils {
	private static Logger logger = LoggerFactory.getLogger(AxisUtils.class);
	private static String defaultNamespace = "http://service.ws.hys.thinkgem.com/";
	private static String drugInfoUrl = "http://192.168.1.102:8080/spd/ws/services/sendPurchaseOrderToSpd?wsdl";
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
}
