package com.hohe.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HttpRequest {
	private static Logger logger = LoggerFactory.getLogger(HttpRequest.class);
    /**
     * 向指定URL发送GET方法的请求
     * 
     * @param url发送请求的URL
     * @param parameters 请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
     * @return URL 远程资源的响应结果
     */
    public static String sendGet(String url, String parameters) {
        String result = "";
        BufferedReader reader = null;
        try {
            String urlNameString = url + "?" + parameters;
            URL realUrl = new URL(urlNameString);
            // 打开和URL之间的连接
            URLConnection connection = realUrl.openConnection();
            // 设置通用的请求属性
            connection.setRequestProperty("accept", "*/*");
            connection.setRequestProperty("connection", "Keep-Alive");
            connection.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            connection.setDoOutput(true);
            // 建立实际的连接
            connection.connect();
            // 获取所有响应头字段
            Map<String, List<String>> map = connection.getHeaderFields();
            // 遍历所有的响应头字段
            /*
            for (String key : map.keySet()) {
                
            }
            */
            // 定义 BufferedReader输入流来读取URL的响应
            InputStream stream = connection.getInputStream();
            reader = new BufferedReader(new InputStreamReader(stream, "utf8"));
            String line;
            while ((line = reader.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
        	logger.error("发送GET请求出现异常！" + e);
            e.printStackTrace();
        }
        // 使用finally块来关闭输入流
        finally {
            try {
                if (reader != null) {
                	reader.close();
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        return result;
    }

    /**
     * 向指定 URL 发送POST方法的请求
     * 
     * @param url 发送请求的 URL
     * @param parameters 请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
     * @return 远程资源的响应结果
     */
    public static String sendPost(String url, String parameters) {
        PrintWriter writer = null;
        BufferedReader reader = null;
        String result = "";
        try {
            URL realUrl = new URL(url);
            // 打开和URL之间的连接
            URLConnection conn = realUrl.openConnection();
            // 设置通用的请求属性
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            // 发送POST请求必须设置如下两行
            conn.setDoOutput(true);
            conn.setDoInput(true);
            // 获取URLConnection对象对应的输出流
            writer = new PrintWriter(conn.getOutputStream());
            // 发送请求参数
            writer.print(parameters);
            // flush输出流的缓冲
            writer.flush();
            // 定义BufferedReader输入流来读取URL的响应
            InputStream stream = conn.getInputStream();
            
            reader = new BufferedReader(new InputStreamReader(stream,"utf8"));
            String line;
            while ((line = reader.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
        	logger.error("发送 POST 请求出现异常！"+e);
            e.printStackTrace();
        }
        //使用finally块来关闭输出流、输入流
        finally{
            try{
                if(writer!=null){
                	writer.close();
                }
                if(reader!=null){
                	reader.close();
                }
            }
            catch(IOException ex){
                ex.printStackTrace();
            }
        }
        return result;
    }
   
    public static String getIpAddr(HttpServletRequest request) {  
//        String ip = request.getHeader("X-Forwarded-For");
        String ip = request.getHeader("X-Real-IP");  
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {  
            ip = request.getHeader("Proxy-Client-IP");  
        }  
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {  
            ip = request.getHeader("WL-Proxy-Client-IP");  
        }  
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {  
            ip = request.getHeader("HTTP_CLIENT_IP");  
        }  
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {  
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");  
        }  
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {  
            ip = request.getRemoteAddr();  
        }  
        return ip;  
    }
    

		public static boolean  isMobileDevice(String requestHeader){
		        /**
		         * android : 所有android设备
		         * mac os : iphone ipad
		         * windows phone:Nokia等windows系统的手机
		         */
		        String[] deviceArray = new String[]{"android","mac os","windows phone"};
		        if(requestHeader == null)
		            return false;
		        requestHeader = requestHeader.toLowerCase();
		        for(int i=0;i<deviceArray.length;i++){
		            if(requestHeader.indexOf(deviceArray[i])>0){
		                return true;
		            }
		        }
		        return false;
		}

    
    /**
     * 基于HttpClient 4.3的通用POST方法
     * @param url       提交的URL
     * @param paramsMap 提交<参数，值>Map
     * @return 提交响应
     */
    public static String post(String url, Map<String, String> paramsMap) {
        CloseableHttpClient client = HttpClients.createDefault();
        String responseText = "";
        CloseableHttpResponse response = null;
        try {
            HttpPost method = new HttpPost(url);
            if (paramsMap != null) {
                List<NameValuePair> paramList = new ArrayList<NameValuePair>();
                for (Map.Entry<String, String> param : paramsMap.entrySet()) {
                    NameValuePair pair = new BasicNameValuePair(param.getKey(), param.getValue());
                    paramList.add(pair);
                }
                method.setEntity(new UrlEncodedFormEntity(paramList, "UTF-8"));
            }
            response = client.execute(method);
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                responseText = EntityUtils.toString(entity);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                response.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return responseText;
    }
}