package org.jeecg.modules.external.ganzhouwuyuan.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import org.apache.axiom.om.*;
import org.apache.axiom.soap.SOAP12Constants;
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
import org.jeecg.modules.pd.util.AxisUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.namespace.QName;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.*;

/** 
 * 描述：axis2调用webservice工具类（赣州五院调接口用）
 * @author zxh
 * @version 2017年10月20日 下午2:51:07 
 */
public class AxisGZWYUtils {
	private static Logger logger = LoggerFactory.getLogger(AxisUtils.class);
	private static OMFactory fac = OMAbstractFactory.getOMFactory();
	private static String defaultNamespace = "http://www.mingweisoft.com/";
	private static String chargeUrl = "http://192.168.0.128:8086/HisService.asmx?wsdl";
	private static OMElement value=null;

	/**
	 * 获取发送客户端
	 * @param soapUrl 调用地址
	 * @param actionUrl 方法地址
	 * @return
	 * @throws AxisFault
	 */
	public static ServiceClient getClientSend(String soapUrl, String actionUrl) throws AxisFault {
		EndpointReference targetEPR = new EndpointReference(soapUrl);
		Options options = new Options();
		options.setManageSession(true);
		options.setTimeOutInMilliSeconds(9000L);
		options.setSoapVersionURI(SOAP12Constants.SOAP_ENVELOPE_NAMESPACE_URI);// 设定SOAP版本soap1.2
		options.setProperty(HTTPConstants.REUSE_HTTP_CLIENT, true);
		options.setAction(actionUrl);
		options.setTo(targetEPR);
		options.setProperty(HTTPConstants.CHUNKED, "false");
		ServiceClient sender = new ServiceClient();
		sender.setOptions(options);
		return sender;
	}

	public static List<Map<String,String>> getLisData(List<String> list) throws AxisFault {
		String lisUrl="";
		ServiceClient sender = getClientSend(lisUrl, "http://tempuri.org/LisGetRepList");
		OMNamespace omNs = fac.createOMNamespace("http://tempuri.org/", "");
		OMElement method = fac.createOMElement("LisGetRepList", omNs);
		OMElement name = fac.createOMElement("UserName", omNs);// 设置入参名称
		OMElement name2 = fac.createOMElement("UserPwd", omNs);// 设置入参名称
		OMElement name3 = fac.createOMElement("PatName", omNs);// 设置入参名称
		OMElement name4 = fac.createOMElement("PatID", omNs);// 设置入参名称

		name.setText(list.get(0));// 设置入参值
		name2.setText(list.get(1));
		name3.setText(list.get(2));
		name4.setText(list.get(3));

		method.addChild(name);
		method.addChild(name2);
		method.addChild(name3);
		method.addChild(name4);
		method.build();
		OMElement response = sender.sendReceive(method);
		//sender.cleanupTransport();
		OMElement newDataSet = getTheNodeValue(response, "NewDataSet");
		List<Map<String,String>> arrayList = new ArrayList<Map<String,String>>();
		if(newDataSet==null){
			return arrayList;
		}
		Iterator iterator = newDataSet.getChildElements();
		Map<String,String> results=new HashMap<String,String>();
		while (iterator.hasNext()) {
			OMElement element = (OMElement) iterator.next();
			results = getResults(element);
			arrayList.add(results);
			System.out.println(JSON.toJSONString(results));

		}
		return arrayList;
	}


	public static void main(String[] args) throws AxisFault, NoSuchAlgorithmException, UnsupportedEncodingException {
		String result = "{'code':'0','msg':'查询成功','data':[{'手术编号':'11542','所属病区编码':'647','所属病区':'肝胆外科','所属科室编码':'647','所属科室':'肝胆外科','手术科室':'40','申请医生姓名编码':'1214','申请医生姓名':'刘新海','门诊号':'00060847','姓名':'李海宇','住院号':'00060847','床位号':'9830','性别':'男','登记日期':'2019/7/27 18:47:00'},{'手术编号':'11561','所属病区编码':'647','所属病区':'肝胆外科','所属科室编码':'647','所属科室':'肝胆外科','手术科室':'40','申请医生姓名编码':'1214','申请医生姓名':'刘新海','门诊号':'00060847','姓名':'李海宇','住院号':'00060847','床位号':'9830','性别':'男','登记日期':'2019/7/29 14:17:00'}]}";
		JSONObject json = JSONObject.parseObject(result);
		System.out.println(json);
	}



	public static String getJsonDataFromWebservice(String appointUrl, String appointNamespace, String appointMethod, Map<String,Object> params) throws AxisFault {
		ServiceClient sender = getClientSend(appointUrl, appointNamespace + appointMethod);
		OMNamespace omNs = fac.createOMNamespace(appointNamespace, "");
		OMElement method = fac.createOMElement(appointMethod, omNs);
		for(String key:params.keySet()){
			OMElement name = fac.createOMElement(key, omNs);// 设置入参名称
			name.setText((String)params.get(key));
			method.addChild(name);
		}
		method.build();
		OMElement response = sender.sendReceive(method);
		OMElement element = response.getFirstElement();
		return element.getText();
	}


	public static OMElement getTheNodeValue(OMElement omElement, String nodeName) {
		try {
			Iterator desc = omElement.getChildElements();
			value = null;
			while (desc.hasNext()) {
				OMElement element = (OMElement) desc.next();
				OMElement e = element.getFirstChildWithName(new QName(nodeName));
				if (e == null && element.getQName().equals(new QName(nodeName))) {
					e = element;
				}
				if (e != null) {
					value = e;
					break;
				} else {
					if (value == null) {
						getTheNodeValue(element, nodeName);
					}
				}
			}
			return value;

		} catch (Exception e) {
			return null;
		}

	}


	/**
	 * 遍历全部节点，将节点放入Map返回
	 *
	 * @param element
	 * @return
	 */
	public static Map<String,String> getResults(OMElement element) {
		if (element == null) {
			return null;
		}
		Iterator iter = element.getChildElements();
		Map<String,String> map = new HashMap<String,String>();
		while (iter.hasNext()) {
			OMNode omNode = (OMNode) iter.next();
			if (omNode.getType() == OMNode.ELEMENT_NODE) {
				OMElement omElement = (OMElement) omNode;
				String key = omElement.getLocalName().trim();
				//System.out.println("sta: " + key);
				String value = omElement.getText().trim();
				map.put(key, value);
			}
		}
		return map;
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
			 //String result="{'code':'0','msg':'查询成功','data':[{'手术编号':'','所属病区编码':'','手术名称':'','所属病区':'','所属科室编码':'','所属科室':'','手术科室':'','申请医生姓名编码':'','申请医生姓名':'','门诊号':'00070278','姓名':'许金连','住院号':'00070278','床位号':'9938','性别':'女','登记日期':'','申请编号':'','住院标识':'1'}]}";
            //String result="{'code':'0','msg':'查询成功','data':[{'手术编号':'15266','所属病区编码':'469','手术名称':'无痛内科胸腔镜检查','所属病区':'结核科一病区','所属科室编码':'469','所属科室':'结核科一病区','手术科室':'手术室','申请医生姓名编码':'410','申请医生姓名':'蒋凛','门诊号':'00071815','姓名':'蔡金生','住院号':'00071815','床位号':'9931','性别':'男','登记日期':'2020/7/28 12:13:00'},{'手术编号':'15591','所属病区编码':'390','手术名称':'胸腔镜下胸膜病损切除术','所属病区':'胸外科','所属科室编码':'390','所属科室':'胸外科','手术科室':'手术室','申请医生姓名编码':'417','申请医生姓名':'王传才','门诊号':'00071815','姓名':'蔡金生','住院号':'00071815','床位号':'9931','性别':'男','登记日期':'2020/8/12 10:07:00'}]}";
            json = JSONObject.parseObject(result);
			if (!"0".equals(json.get("code"))) {
				json.put("code", "-200");
				json.put("msg","查询失败，未找到记录");
			}
		} catch (AxisFault e) {
			e.printStackTrace();
			logger.info("******调用HIS根据住院号查询病人信息接口出现错误！******");
			json.put("code", "-200");
			json.put("msg","调用HIS根据住院号查询病人信息接口出现错误！");
		}  catch  (JSONException ee) {
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
		if("ed88fae1d871426ba8df883dedcb9bd9".equals(departId)
				|| "2c7f3452b92f40c3a88f0cfa40d438ad".equals(departId)
				|| "12c7393a5ddc4737ac73de425fd805a7".equals(departId)){
			params.put("ssbm", "0");//传0的话，HIS系统会记录到手工记账那里，而不是手术项目收费那里；
		}else{
			if(pdDosage.getOperativeNumber()==null ||"".equals(pdDosage.getOperativeNumber())){
				json.put("code", "-200");
				json.put("msg", "参数异常");
				return json;
			}else{
			params.put("ssbm",  pdDosage.getOperativeNumber());
			}
		}
		params.put("sflx", PdConstant.IS_CHARGE_TYPE_1);//收费类型
		params.put("czr", sysUser.getWorkNo());
		params.put("czsj", DateUtils.date2Str(DateUtils.datetimeFormat.get()));
		params.put("remark", "");
		params.put("token",System.currentTimeMillis()+sysUser.getWorkNo());
		String result = null;
		try {
			result = getJsonDataFromWebservice(chargeUrl, defaultNamespace, "Charges", params);
           // result="{'code':'0','msg':'查询成功'}";
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
		}  catch (Exception ee) {
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

		if("ed88fae1d871426ba8df883dedcb9bd9".equals(departId)
				|| "2c7f3452b92f40c3a88f0cfa40d438ad".equals(departId)
				|| "12c7393a5ddc4737ac73de425fd805a7".equals(departId)){
			params.put("ssbm", "0");//传0的话，HIS系统会记录到手工记账那里，而不是手术项目收费那里；
		}else{
			if(pdDosage.getOperativeNumber()==null ||"".equals(pdDosage.getOperativeNumber())){
				json.put("code", "-200");
				json.put("msg", "参数异常");
				return json;
			}else{
				params.put("ssbm",  pdDosage.getOperativeNumber());
			}
		}
		params.put("sflx",PdConstant.IS_CHARGE_TYPE_0);//收费类型
		params.put("czr", sysUser.getWorkNo());
		params.put("czsj", DateUtils.date2Str(DateUtils.datetimeFormat.get()));
		params.put("remark", "");
		//params.put("token",System.currentTimeMillis()+(Math.random()*100));
		params.put("token",System.currentTimeMillis()+sysUser.getWorkNo());
		String result = null;
		try {
			 result = getJsonDataFromWebservice(chargeUrl, defaultNamespace, "Charges", params);
            //result="{'code':'0','msg':'查询成功'}";
			json = JSONObject.parseObject(result);
			if (!"0".equals(json.get("code"))) {
				json.put("code", "-200");
			}
		} catch (AxisFault e) {
			e.printStackTrace();
			json.put("code", "-200");
			logger.info("******调用HIS退费接口出现错误！--->{}******", result);
		}  catch (Exception ee) {
			ee.printStackTrace();
			logger.info("******调用HIS退费接口JSON转换返回信息出现错误！--->{}******", result);
			json.put("code", "-200");
		}
		return json;
	}
}
