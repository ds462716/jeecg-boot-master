package org.jeecg.modules.pd.util;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
@Slf4j
public class HttpUtil {
	
	/**
	 * post请求（用于请求json格式的参数）
	 * @param url
	 * @param
	 * @return
	 */
	public static JSONObject httpPost(String url, String jsonParam) {
		// post请求返回结果
		CloseableHttpClient httpClient = HttpClients.createDefault();
		JSONObject jsonResult = null;
		HttpPost httpPost = new HttpPost(url);
		// 设置请求和传输超时时间
		RequestConfig requestConfig = RequestConfig.custom()
				.setSocketTimeout(30000).setConnectTimeout(10000).build();
		httpPost.setConfig(requestConfig);
		try {
			if (null != jsonParam) {
				// 解决中文乱码问题
				StringEntity entity = new StringEntity(jsonParam,
						"utf-8");
				entity.setContentEncoding("UTF-8");
				entity.setContentType("application/json");
				httpPost.setEntity(entity);
			}
			CloseableHttpResponse result = httpClient.execute(httpPost);
			//请求发送成功，并得到响应
			if (result.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				String str = "";
				try {
					//读取服务器返回过来的json字符串数据 
					str = EntityUtils.toString(result.getEntity(), "utf-8");
					//把json字符串转换成json对象 
					jsonResult = JSONObject.parseObject(str);
				} catch (Exception e) {
					log.error("post请求提交失败:" + url, e);
				}
			}
		} catch (IOException e) {
			jsonResult =  new JSONObject();
			jsonResult.put("statusCode", "500");
			jsonResult.put("msg", "服务器连接失败");
			log.error("post请求提交失败:" + url, e);
			return jsonResult;
		} finally {
			httpPost.releaseConnection();
		}
		return jsonResult;
	}

	/**
	 * jiangxz
	 * 上传文件到服务器
	 * @param url  服务器响应地址
	 * @param filePath 文件路径
	 * @return
	 */
	/*public static JSONObject httpPostUpload(String url, String filePath){
		JSONObject jsonResult = new JSONObject();
		HttpPost httpPost = new HttpPost(url);
		try {
			// 1. 创建上传需要的元素类型
			// 1.1 装载本地上传图片的文件
			File imageFile = new File(filePath);
			FileBody imageFileBody = new FileBody(imageFile);

			// 2. 将所有需要上传元素打包成HttpEntity对象
			HttpEntity reqEntity = MultipartEntityBuilder.create()
					.addPart("imageFile",imageFileBody).build();

			// 3. 创建HttpPost对象，用于包含信息发送post消息
			httpPost.setEntity(reqEntity);

			// 4. 创建HttpClient对象，传入httpPost执行发送网络请求的动作
			CloseableHttpClient httpClient = HttpClients.createDefault();
			CloseableHttpResponse result = httpClient.execute(httpPost);

			// 5. 获取返回的实体内容对象并解析内容
			//HttpEntity resultEntity = response.getEntity();
			if (result.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				String str = "";
				try {
					//读取服务器返回过来的json字符串数据
					str = EntityUtils.toString(result.getEntity(), "utf-8");
					//把json字符串转换成json对象
					if(StringUtils.isNotEmpty(str)){
						jsonResult.put("statusCode", "200");
						jsonResult.put("msg", "文件同步成功");
					}else{
						jsonResult.put("statusCode", "500");
						jsonResult.put("msg", "文件同步失败");
						logger.error("文件同步失败:");
					}
				} catch (Exception e) {
					logger.error("post请求提交失败:" + url, e);
				}
			}

		} catch (Exception e) {
			jsonResult.put("statusCode", "500");
			jsonResult.put("msg", "服务器连接失败");
			logger.error("post请求提交失败:" + url, e);
			return jsonResult;
		} finally {
			httpPost.releaseConnection();
		}

		return jsonResult;

	}*/


	/**
	 * 多个参数的http请求
	 * @param url
	 * @param map
	 * @param charset
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static JSONObject doPost(String url, Map<String,String> map, String charset){
	    CloseableHttpClient httpClient = null;
	    HttpPost httpPost = null;
	    String result = null;
	    JSONObject jsonResult = null;
	    try{
	        httpClient = HttpClients.createDefault();
	        httpPost = new HttpPost(url);
	        //设置参数
	        List<NameValuePair> list = new ArrayList<NameValuePair>();
	        Iterator iterator = map.entrySet().iterator();
	        while(iterator.hasNext()){
	            Map.Entry<String, String> elem = (Map.Entry<String, String>) iterator.next();
	            list.add(new BasicNameValuePair(elem.getKey(),elem.getValue()));
	        }
	        if(list.size() > 0){
	            UrlEncodedFormEntity entity = new UrlEncodedFormEntity(list,charset);
	            httpPost.setEntity(entity);
	        }
	        HttpResponse response = httpClient.execute(httpPost);
	        if(response != null && response.getStatusLine().getStatusCode() == HttpStatus.SC_OK){
	            HttpEntity resEntity = response.getEntity();
	            if(resEntity != null){
	                result = EntityUtils.toString(resEntity,charset);
	                jsonResult = JSONObject.parseObject(result);
	            }
	        }
	    }catch(Exception ex){
	        jsonResult =  new JSONObject();
			jsonResult.put("statusCode", "500");
			jsonResult.put("msg", "服务器连接失败");
			log.error("post请求提交失败:" + url, ex);
			return jsonResult;
	    }
	    return jsonResult;
	}
	
	/*public static void main(String args[]){
		PdConsumablesOrder pdConsumablesOrder = new PdConsumablesOrder();
		pdConsumablesOrder.setNumber("1111111");
		pdConsumablesOrder.setHospital("国药");
		pdConsumablesOrder.setOrderQuantity(1000);
		pdConsumablesOrder.setOrderAmount(50000.00);
		List<PdConsumablesOrderDetail> pdConsumablesOrderDetails = new ArrayList<PdConsumablesOrderDetail>();
		for(int i = 0 ;i<3;i++){
			PdConsumablesOrderDetail pdConsumablesOrderDetail = new PdConsumablesOrderDetail();
			pdConsumablesOrderDetail.setPrice(500.00*(i+1));
			pdConsumablesOrderDetail.setNumber("9645823156");
			pdConsumablesOrderDetail.setOrderQuantity(10*(i+1));
			pdConsumablesOrderDetail.setAmount(pdConsumablesOrderDetail.getPrice()*pdConsumablesOrderDetail.getOrderQuantity());
			pdConsumablesOrderDetails.add(pdConsumablesOrderDetail);
		}
		pdConsumablesOrder.setPdConsumablesOrderDetails(pdConsumablesOrderDetails);
		String jsonPar = JSONObject.toJSONString(pdConsumablesOrder);
		JSONObject json = httpPost("http://localhost:8088/spd-CP/admin/hys/pdConsumablesOrder/saveConsumablesOrder", jsonPar);
		System.out.println(json);
	}*/
}
