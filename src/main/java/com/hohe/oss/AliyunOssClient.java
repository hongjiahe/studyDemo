package com.hohe.oss;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.activation.MimetypesFileTypeMap;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hohe.util.HttpRequest;
import com.hohe.util.PropertiesUtil;
import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;


public class AliyunOssClient extends OssClient {

	private String getPostPolicy = "/oss/getPostPolicy.json";
	private String downloadFile = "/oss/downloadFile.json";
	private String downloadZipFile = "/oss/downloadZipFile.json";
	
	public AliyunOssClient() {
    	if (this.ossHost == null) {
    		this.ossHost = PropertiesUtil.getString("ossHost");
    	}
    	if (this.account == null) {
    		this.account = PropertiesUtil.getString("ossAccount");
    	}
    	if (this.pwd == null) {
    		this.pwd = PropertiesUtil.getString("ossPwd");
    	}
    }	
	
	/**
	 * 获取客户端表单提交需要的参数
	 * @param dir 保存在阿里云OSS的文件夹路径,以"/"结尾
	 * @param filename 保存在阿里云OSS的文件名（需要包含文件后缀）
	 * @param token OSS令牌 
	 * 
	 */
	public Map<String, String> getSendFilePolicy(String dir, String filename, String token){
		//test interface for getPostPolicy no need params and token
		Map<String, String> map = null;
		try {
			String filenameEnc = URLEncoder.encode(filename, "UTF-8");
			String result = HttpRequest.sendPost(ossHost + getPostPolicy, "dir="+dir+"&filename="+filenameEnc + "&account=" + this.account);
			map = new Gson().fromJson(result, new TypeToken<HashMap<String,String>>(){}.getType());
		} catch (UnsupportedEncodingException e) {
			logger.error("encode filename failed：" + e.getStackTrace());
			e.printStackTrace();
		}
		return map;
	}	
	
	/**
	 * 获取客户端表单提交需要的参数， dir或fiename如果不设置，系统会根据规则自动生成一个随机的dir或filename
	 * @param dir 保存在阿里云OSS的文件夹路径,以"/"结尾
	 * @param filename 保存在阿里云OSS的文件名（需要包含文件后缀）
	 * @param inputStream 输入的文件流
	 * @param token OSS令牌
	 * @return
	 * @throws Exception
	 */
	@Override
    public AliyunOssFile uploadFile(String dir, String filename, InputStream in, String token){
        HttpURLConnection conn = null;
        String boundary = "9431149156168";
        Map<String, String> formFields = this.getSendFilePolicy(dir, filename, token);
        if(formFields == null){
        	logger.info("get upload file policy failed");
        	return null;
        }
        
        AliyunOssFile ossFile = null;
        
        try {
            URL url = new URL(formFields.get("host"));
            conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(5000);
            conn.setReadTimeout(30000);
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("User-Agent", 
                    "Mozilla/5.0 (Windows; U; Windows NT 6.1; zh-CN; rv:1.9.2.6)");
            conn.setRequestProperty("Content-Type",
                    "multipart/form-data; boundary=" + boundary);
            OutputStream out = new DataOutputStream(conn.getOutputStream());
            
            // text
            if (formFields != null) {
                StringBuffer strBuf = new StringBuffer();
                Iterator<Entry<String, String>> iter = formFields.entrySet().iterator();
                int i = 0;
                
                while (iter.hasNext()) {
                    Entry<String, String> entry = iter.next();
                    String inputName = entry.getKey();
                    String inputValue = entry.getValue();
                    
                    if (inputValue == null) {
                        continue;
                    }
                    
                    if (i == 0) {
                        strBuf.append("--").append(boundary).append("\r\n");
                        strBuf.append("Content-Disposition: form-data; name=\""
                                + inputName + "\"\r\n\r\n");
                        strBuf.append(inputValue);
                    } else {
                        strBuf.append("\r\n").append("--").append(boundary).append("\r\n");
                        strBuf.append("Content-Disposition: form-data; name=\""
                                + inputName + "\"\r\n\r\n");
                        strBuf.append(inputValue);
                    }
                    i++;
                }
                out.write(strBuf.toString().getBytes());
            }

            
			String contentType = new MimetypesFileTypeMap().getContentType(new File(filename));
            if (contentType == null || contentType.equals("")) {
                contentType = "application/octet-stream";
            }

            StringBuffer strBuf = new StringBuffer();
            strBuf.append("\r\n").append("--").append(boundary)
                    .append("\r\n");
            strBuf.append("Content-Disposition: form-data; name=\"file\"; "
                    + "filename=\"" + filename + "\"\r\n");
            strBuf.append("Content-Type: " + contentType + "\r\n\r\n");

            out.write(strBuf.toString().getBytes());
            
            int bytes = 0;
            byte[] bufferOut = new byte[1024];
            while ((bytes = in.read(bufferOut)) != -1) {
                out.write(bufferOut, 0, bytes);
            }
            in.close();

            byte[] endData = ("\r\n--" + boundary + "--\r\n").getBytes();
            out.write(endData);
            out.flush();
            out.close();

            if(200 == conn.getResponseCode()){
            	logger.info("Upload file succeed");
            	ossFile = new AliyunOssFile();
		    	ossFile.setEndPoint(formFields.get("host") + "/" +formFields.get("key"));
		    	ossFile.setInternalEndPoint(formFields.get("internalHost") + "/" + formFields.get("key"));
		    	ossFile.setRequestId(conn.getHeaderField("x-oss-request-id"));           	
            } else {
	            // 读取返回数据
            	String res = "";
	            strBuf = new StringBuffer();
	            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
	            String line = null;
	            while ((line = reader.readLine()) != null) {
	                strBuf.append(line).append("\n");
	            }
	            res = strBuf.toString();
	            logger.info("Upload file failed\n" + res);
	            reader.close();
	            reader = null;
            }
        } catch (Exception e) {
        	logger.error("Send post request exception: " + e);
            e.printStackTrace();
        } finally {
            if (conn != null) {
                conn.disconnect();
                conn = null;
            }
        }
        
        return ossFile;
    }
    
	/**
	 * 获取客户端表单提交需要的参数， dir或fiename如果不设置，系统会根据规则自动生成一个随机的dir或filename
	 * @param dir 保存在阿里云OSS的文件夹路径,以"/"结尾
	 * @param filename 保存在阿里云OSS的文件名（需要包含文件后缀）
	 * @param filePath 本地文件地址
	 * @param token OSS令牌
	 * @return
	 */
	@Override
    public AliyunOssFile uploadFile(String dir, String filename, String filePath, String token){
 		Map<String, String> params = this.getSendFilePolicy(dir, filename, token);
		String aliyunUploadUrl = params.get("host");
		AliyunOssFile ossFile = null;
		
		CloseableHttpClient httpClient = HttpClients.createDefault(); 
        try {
			//把文件转换成流对象FileBody 
			FileBody bin = new FileBody(new File(filePath));
			//把一个普通参数和文件上传给下面这个地址    
			HttpPost httpPost = new HttpPost(aliyunUploadUrl);  
			
            StringBody accessid = new StringBody(params.get("OSSAccessKeyId"), ContentType.create("text/plain", Consts.UTF_8));  
            StringBody policy = new StringBody(params.get("policy"), ContentType.create("text/plain", Consts.UTF_8));
            StringBody signature = new StringBody(params.get("Signature"), ContentType.create("text/plain", Consts.UTF_8));
            StringBody success_action_status = new StringBody(params.get("success_action_status"), ContentType.create("text/plain", Consts.UTF_8));
            StringBody key = new StringBody(params.get("key"), ContentType.create("text/plain", Consts.UTF_8));
            //StringBody contentType = new StringBody("image/png", ContentType.create("text/plain", Consts.UTF_8));//非必须，如果需要请使用mime类型
			
            //表单大小写敏感 
			HttpEntity reqEntity = MultipartEntityBuilder.create()  
			.addPart("OSSAccessKeyId", accessid)  //相当于<input type="text" name="xxx" value=xxx>  
			.addPart("policy", policy)
			.addPart("Signature", signature)
			.addPart("success_action_status", success_action_status)  //成功的话 返回200 没有任何提示 不成功提示到时挺多的
			.addPart("key", key)
			//.addPart("Content-Type", contentType)   //非必须，如果需要请使用mime类型
			.addPart("file", bin)   //相当于<input type="file" name="media"/>,aiyun规定file必须为表单最后一个,人家代码写死了  
			.build();  
			  
			httpPost.setEntity(reqEntity);  
			  
			System.out.println("发起请求的页面地址 " + httpPost.getRequestLine());  
			//发起请求   并返回请求的响应  
			CloseableHttpResponse response = httpClient.execute(httpPost);  
			try {  
			    System.out.println("----------------------------------------");  
			    //打印响应状态  
			    System.out.println(response.getStatusLine());  
			    if(200 == response.getStatusLine().getStatusCode()){
			    	logger.info("Upload file succeed");
			    	ossFile = new AliyunOssFile();
			    	ossFile.setEndPoint(params.get("host") + "/" + params.get("key"));
			    	ossFile.setInternalEndPoint(params.get("internalHost") + "/" + params.get("key"));
			    	ossFile.setRequestId(response.getFirstHeader("x-oss-request-id").getValue());
			    } else {
				    //获取响应对象  
				    HttpEntity resEntity = response.getEntity();  
				    if (resEntity != null) {  
				        //打印响应内容  
				    	logger.info("Upload file failed");
				        logger.info(EntityUtils.toString(resEntity,Charset.forName("UTF-8")));  
				    }  
				    //销毁  
				    EntityUtils.consume(resEntity);  
			    }
			} catch(Exception e){
				logger.error("Upload file to aliyun oss encounter error!");
				e.printStackTrace();
			} finally {  
			    response.close();  
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally{  
            try {
				httpClient.close();
			} catch (IOException e) {
				e.printStackTrace();
			}  

        }
        return ossFile;
    } 
    
    /**
     * method to init the configuration of oss client
     * @param map
     */
	@Override
    public void initProperties(Map<String, Object> map){
    	
    }

	@Override
	public void downloadFile(String filename, String tempFilePath) {
		InputStream is = null;
		FileOutputStream fos = null;
			try {
				is = this.downloadFile(filename);
				fos = new FileOutputStream(tempFilePath);
				
				byte[] buffer = new byte[4096];
				int length = 0;
				while((length = is.read(buffer)) != -1){
					fos.write(buffer, 0, length);
				}
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				if(null != is){
					try {
						is.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				if(null != fos){
					try {
						fos.flush();
						fos.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
	}
	
	@Override
	public void downloadZipFile(List<String> filenameList, String zipName){
		InputStream is = null;
		FileOutputStream fos = null;
			try {
				is = this.downloadZipFile(filenameList);
				fos = new FileOutputStream(zipName);
				
				byte[] buffer = new byte[4096];
				int length = 0;
				while((length = is.read(buffer)) != -1){
					fos.write(buffer, 0, length);
				}
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				if(null != is){
					try {
						is.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				if(null != fos){
					try {
						fos.flush();
						fos.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}		
	}
	
	@Override
	public InputStream downloadZipFile(List<String> filenameList){
        String url = ossHost + downloadZipFile;
        String params = "account=" + this.account + "&";
        for(String filename : filenameList){
        	params += "filenames=" + filename + "&";
        }
        params = params.substring(0, params.length() -1);
		
        PrintWriter writer = null;
        InputStream stream = null;
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
			writer.print(params);
			// flush输出流的缓冲
			writer.flush();
			// 定义BufferedReader输入流来读取URL的响应
			stream = conn.getInputStream();
			
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}		
		return stream;
	}

	@Override
	public InputStream downloadFile(String filename) {
        String url = ossHost + downloadFile;
        String params = "filename=" + filename + "&account=" + this.account;
		
        PrintWriter writer = null;
        InputStream stream = null;
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
			writer.print(params);
			// flush输出流的缓冲
			writer.flush();
			// 定义BufferedReader输入流来读取URL的响应
			stream = conn.getInputStream();
			
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
		return stream;
	}
		
}

