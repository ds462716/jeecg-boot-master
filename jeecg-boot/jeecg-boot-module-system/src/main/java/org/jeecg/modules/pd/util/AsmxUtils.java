package org.jeecg.modules.pd.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.axiom.om.OMAbstractFactory;
import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.OMFactory;
import org.apache.axiom.om.OMNamespace;
import org.apache.axis2.AxisFault;
import org.apache.axis2.addressing.EndpointReference;
import org.apache.axis2.client.Options;
import org.apache.axis2.client.ServiceClient;
import org.apache.axis2.transport.http.HTTPConstants;

/**
 * webservice asmx 客户端接口，调天气接口demo
 * @author jiangxz
 * @description webservice.asmx接口
 * @date 2020-6-10
 */
public class AsmxUtils {
    private static Logger logger = LoggerFactory.getLogger(AsmxUtils.class);

    public static OMElement getOMMethod(String methodStr,String namespace,String tns,String[] pars,String[] vals){
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

    public static Options getClientOptions(String action,String url){    //端点引用 指接口位置
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

//    public static void main(String[] args) {
////        String action  = "http://WebXml.com.cn/getWeather";
//        String url = "http://ws.webxml.com.cn/WebServices/WeatherWS.asmx";
//        String methodStr = "getWeather";
//        String namespace = "http://WebXml.com.cn/";
//        String tns = "xsd";
//        String[] pars = {"theCityCode"}; // 参数名称
//        String[] vals = {"2013"};        //参数值
//        String result = null;
//        try {
//            result = getJsonDataFromWebservice(url, methodStr, namespace, tns, pars, vals);
//        } catch (AxisFault axisFault) {
//            logger.error("webservice.asmx========调用异常========"+axisFault.getMessage());
//            axisFault.printStackTrace();
//        }
//        System.out.println("result===================="+result);
//    }

}
