package org.jeecg.modules.external.fengcheng.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
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
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.constant.PdConstant;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.util.DateUtils;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.external.fengcheng.entity.HisChargeFCRMYY;
import org.jeecg.modules.pd.entity.PdDosage;
import org.jeecg.modules.pd.entity.PdDosageDetail;
import org.jeecg.modules.pd.util.AsmxUtils;
import org.jeecg.modules.pd.util.HttpUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

/**
 * @author jiangxz
 * @description 丰城人民医院HIS计费接口
 * @date 2020-5-26
 */
public class HisApiForFCRenminUtils {
    private static Logger logger = LoggerFactory.getLogger(AsmxUtils.class);

    private static final String url = "http://220.1.1.93:8081/SPDInterface.asmx";
    private static final String namespace = "http://tempuri.org/";
    private static final String method = "DoBusiness";
    private static final String tns = "xsd";
    private static final String patient_info_interface = "P_SPD_查询病人信息";
    private static final String charge_code_interface = "P_SPD_查询项目收费";
    private static final String charge_interface = "P_SPD_耗材执行";


    /**
     * 收费接口
     * @param pdDosage
     * @param dosageDetailList
     * @return
     */
    public static JSONObject exeCharge(PdDosage pdDosage, List<PdDosageDetail> dosageDetailList){

        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();

        String result = null;
        JSONObject returnJson = new JSONObject();
        JSONObject requestJson = new JSONObject();

        JSONArray array = new JSONArray();
        for(PdDosageDetail detail : dosageDetailList){
            JSONObject item = new JSONObject();
            item.put("chargeCode",detail.getChargeCode()); //收费项目代码
            item.put("prodNo",detail.getProductNumber());  //产品代码
            item.put("prodNum",detail.getDosageCount());//数量
            item.put("packCode","");  //打包项目代码
            item.put("packNo","");    //打包的编号
            array.add(item);
        }

        requestJson.put("hitaionNo",pdDosage.getInHospitalNo()); // 住院编号
        requestJson.put("operNo",pdDosage.getOperativeNumber()); // 手术编号
        requestJson.put("chargeType","1");                       // 0：正常退费；1：正常收费；2：补计费；
        requestJson.put("operatorName",sysUser.getUsername());   // 操作人（HIS工号）
        requestJson.put("operatorTime", DateUtils.now());        // 操作时间
        requestJson.put("remark",pdDosage.getRemarks() == null ? "" : pdDosage.getRemarks());         // 备注
        requestJson.put("Token",pdDosage.getToken());                             // 防重复提交标识
        requestJson.put("docturId",pdDosage.getSurgeonId());
        requestJson.put("departId",pdDosage.getOprDeptId());
        requestJson.put("data", array);

        String[] pars = {"businessType","inJson"}; // 参数名称
        String[] vals = {charge_interface,requestJson.toJSONString()};        //参数值

        logger.info("webservice.asmx========收费入参========"+requestJson.toJSONString());

        try {
            result = getJsonDataFromWebservice(url, method, namespace, tns, pars, vals);

            returnJson = JSONObject.parseObject(result);
            if(returnJson == null){
                returnJson = new JSONObject();
                logger.error("HIS计费失败，返回null");
                returnJson.put("statusCode", "-200");
                returnJson.put("msg", "HIS计费失败，返回null");
                return returnJson;
            }

            if (!PdConstant.SUCCESS_0.equals(returnJson.getString("code"))) {
                logger.error("HIS计费失败，返回msg:"+returnJson.getString("msg"));
                returnJson.put("statusCode", "-200");
                returnJson.put("msg","HIS计费失败，返回msg:"+returnJson.getString("msg"));
            }else{
                returnJson.put("statusCode", PdConstant.SUCCESS_0);
                logger.info("HIS计费成功");
            }

        } catch (AxisFault axisFault) {
            logger.error("webservice.asmx========调用异常========"+axisFault.getMessage());
            axisFault.printStackTrace();
        }

        return returnJson;
    }

    /**
     * 退费接口
     * @param pdDosage
     * @param dosageDetailList
     * @return
     */
    public static JSONObject exeRefund(PdDosage pdDosage,List<PdDosageDetail> dosageDetailList){

        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();

        String result = null;
        JSONObject returnJson = new JSONObject();
        JSONObject requestJson = new JSONObject();

        JSONArray array = new JSONArray();
        for(PdDosageDetail detail : dosageDetailList){
            JSONObject item = new JSONObject();
            item.put("chargeCode",detail.getChargeCode()); //收费项目代码
            item.put("prodNo",detail.getProductNumber());  //产品代码
            item.put("prodNum",detail.getDosageCount());//数量
            item.put("packCode","");  //打包项目代码
            item.put("packNo","");    //打包的编号
            array.add(item);
        }

        requestJson.put("hitaionNo",pdDosage.getInHospitalNo()); // 住院编号
        requestJson.put("operNo",pdDosage.getOperativeNumber()); // 手术编号
        requestJson.put("chargeType","0");                       // 0：正常退费；1：正常收费；2：补计费；
        requestJson.put("operatorName",sysUser.getUsername());   // 操作人（HIS工号）
        requestJson.put("operatorTime", DateUtils.now());        // 操作时间
        requestJson.put("remark",pdDosage.getRemarks() == null ? "" : pdDosage.getRemarks());         // 备注
        requestJson.put("Token",pdDosage.getToken());               // 防重复提交标识
        requestJson.put("docturId",pdDosage.getSurgeonId());
        requestJson.put("departId",pdDosage.getOprDeptId());
        requestJson.put("data", array);

        String[] pars = {"businessType","inJson"}; // 参数名称
        String[] vals = {charge_interface,requestJson.toJSONString()};        //参数值

        logger.info("webservice.asmx========退费入参========"+requestJson.toJSONString());

        try {
            result = getJsonDataFromWebservice(url, method, namespace, tns, pars, vals);

            returnJson = JSONObject.parseObject(result);
            if(returnJson == null){
                returnJson = new JSONObject();
                logger.error("HIS退费失败，返回null");
                returnJson.put("statusCode", "-200");
                returnJson.put("msg", "HIS退费失败，返回null");
                return returnJson;
            }

            if (!PdConstant.SUCCESS_0.equals(returnJson.getString("code"))) {
                logger.error("HIS退费失败，返回msg:"+returnJson.getString("msg"));
                returnJson.put("statusCode", "-200");
                returnJson.put("msg","HIS退费失败，返回msg:"+returnJson.getString("msg"));
            }else{
                returnJson.put("statusCode", PdConstant.SUCCESS_0);
                logger.info("HIS退费成功");
            }
        } catch (AxisFault axisFault) {
            logger.error("webservice.asmx========调用异常========"+axisFault.getMessage());
            axisFault.printStackTrace();
        }

        return returnJson;
    }


    /**
     * 查询HIS项目收费代码
     * @param hisCharge
     * @return
     */
    public static JSONObject queryHisChargeCode(HisChargeFCRMYY hisCharge) {
        String result = null;
        JSONObject returnJson = new JSONObject();
        JSONObject requestJson = new JSONObject();
        requestJson.put("chargeCode",hisCharge.getChargeCode() == null ? "" : hisCharge.getChargeCode().trim()); // 收费代码
        requestJson.put("proName",hisCharge.getProName() == null ? "" : hisCharge.getProName().trim());   // 项目名称

        String[] pars = {"businessType","inJson"};                              // 参数名称
        String[] vals = {charge_code_interface,requestJson.toJSONString()};     //参数值

        try {
            result = getJsonDataFromWebservice(url, method, namespace, tns, pars, vals);
            returnJson = JSONObject.parseObject(result);
            if(returnJson == null){
                returnJson = new JSONObject();
                logger.error("HIS查询项目收费代码信息失败，返回null");
                returnJson.put("statusCode", "-200");
                returnJson.put("msg", "HIS查询项目收费代码信息失败，返回null");
                return returnJson;
            }

            if (!PdConstant.SUCCESS_0.equals(returnJson.getString("code"))) {
                logger.error("HIS查询项目收费代码信息失败，返回msg:"+returnJson.getString("msg"));
                returnJson.put("statusCode", "-200");
                returnJson.put("msg", "HIS查询项目收费代码信息失败，返回msg:"+returnJson.getString("msg"));
            }else{
                returnJson.put("statusCode", PdConstant.SUCCESS_0);
            }
        } catch (AxisFault axisFault) {
            logger.error("webservice.asmx========调用异常========"+axisFault.getMessage());
            axisFault.printStackTrace();
        }

        return returnJson;
    }

    /**
     * 查询HIS病人信息
     * @param pdDosage
     * @return
     */
    public static JSONObject queryHisPatientInfo(PdDosage pdDosage) {
        String result = null;
        JSONObject returnJson = new JSONObject();
        JSONObject requestJson = new JSONObject();
        requestJson.put("hitaionNo",pdDosage.getInHospitalNo()); // 住院编号
        requestJson.put("operNo",pdDosage.getOperativeNumber() == null ? "" : pdDosage.getOperativeNumber()); // 手术编号
//        requestJson.put("aplyNo",pdDosage.getOutpatientNumber());// 门诊编号

        String[] pars = {"businessType","inJson"}; // 参数名称
        String[] vals = {patient_info_interface,requestJson.toJSONString()};        //参数值

        try {
            result = getJsonDataFromWebservice(url, method, namespace, tns, pars, vals);
            returnJson = JSONObject.parseObject(result);
            if (!PdConstant.SUCCESS_0.equals(returnJson.getString("code"))) {
                logger.error("HIS查询病人信息失败，返回msg:"+returnJson.getString("msg"));
                returnJson.put("statusCode", "-200");
                returnJson.put("msg","HIS查询病人信息失败，返回msg:"+returnJson.getString("msg"));
            }else{
                returnJson.put("statusCode", PdConstant.SUCCESS_0);
            }
        } catch (AxisFault axisFault) {
            logger.error("webservice.asmx========调用异常========"+axisFault.getMessage());
            axisFault.printStackTrace();
        }

        return returnJson;
    }



    public static OMElement getOMMethod(String methodStr, String namespace, String tns, String[] pars, String[] vals){
        //有抽象OM工厂获取OM工厂，创建request SOAP包
        OMFactory fac = OMAbstractFactory.getOMFactory();
        //创建命名空间
        OMNamespace nms = fac.createOMNamespace(namespace, tns);
        //创建OMElement方法 元素，并指定其在nms指代的名称空间中
        OMElement method = fac.createOMElement(methodStr, nms);
        //添加方法参数名和参数值
        for(int i=0;i<pars.length;i++){
            //创建方法参数OMElement元素
            OMElement param = fac.createOMElement(pars[i],nms);
            //设置键值对 参数值
            param.setText(vals[i]);
            //讲方法元素 添加到method方法元素中
            method.addChild(param);
        }
        logger.info("webservice.asmx========入参========"+method);
        return method;
    }

    public static Options getClientOptions(String action, String url){    //端点引用 指接口位置
        EndpointReference targetEpr = new EndpointReference(url);
        //创建request soap包 请求选项
        Options options = new Options();
        //设置options的soapAction
        options.setAction(action);
        //设置request soap包的端点引用(接口地址)
        options.setTo(targetEpr);
        //如果报错提示Content-Length，请求内容长度
        options.setProperty(HTTPConstants.CHUNKED,"false");//把chunk关掉后，会自动加上Content-Length。
        return options;
    }

    public static String getJsonDataFromWebservice(String url,String methodStr,String namespace,String tns,String[] pars,String[] vals) throws AxisFault {
        OMElement result = null;
        ServiceClient client = new ServiceClient();
//            client.setOptions(getClientOptions(action));
        client.setOptions(getClientOptions(namespace+methodStr,url));
        result =  client.sendReceive(getOMMethod(methodStr,namespace,tns,pars,vals));
        logger.info("webservice.asmx========出参========"+result);
        OMElement element = result.getFirstElement();
        return element.getText();
    }

    public static void main(String[] args) {

        //查询病人信息
//        PdDosage pd = new PdDosage();
//        JSONObject object1 = queryHisPatientInfo(pd);
//        pd.setInHospitalNo("1668289");

        //查询项目收费代码
        HisChargeFCRMYY hisCharge = new HisChargeFCRMYY();
        hisCharge.setChargeCode("120400002c");
        hisCharge.setProName("血球五项采血管");
        JSONObject object2 = queryHisChargeCode(hisCharge);

    }
}
