package com.lehealth.common.util;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;

public class HttpUtils {
	
	private static Logger logger = Logger.getLogger(HttpUtils.class);
	
	public static String getPostResponse(String url, String requestBody){
		return getPostResponse(url, requestBody, null);
	}
	
	public static String getPostResponse(String url, String requestBody, Map<String, String> header){
		try {
            //已单例形式初始化链接线程池大小
            PoolConnectionManager connectionManager = PoolConnectionManager.getInstance();
            //new一个HttpClinet
            CloseableHttpClient httpClient = connectionManager.getHttpClient();
            //设置http请求参数
            HttpPost postMethod = new HttpPost(url);
            if(header != null && !header.isEmpty()){
            	for(Entry<String,String> e : header.entrySet()){
                	postMethod.setHeader(e.getKey(), e.getValue());
                }
            }
            postMethod.setConfig(PoolConnectionManager.requestConfig);
            //请求的body里放入请求的json字符串
            postMethod.setEntity(new StringEntity(requestBody, "UTF-8"));
            //执行请求，获取response
	        CloseableHttpResponse response = httpClient.execute(postMethod);
            try {
            	if(response != null
                		&& response.getStatusLine() != null
                		&& response.getStatusLine().getStatusCode()==200){
                	//获取响应内容
                    HttpEntity respEntity = response.getEntity();
                    String responseStr = EntityUtils.toString(respEntity, "UTF-8");
                    if(!StringUtils.isBlank(responseStr)){
						return responseStr;
                    }
                }else{
                	logger.warn("error status code:"+response.getStatusLine().getStatusCode()+",from url:"+url);
                }
            } finally{
            	response.close();
            	postMethod.releaseConnection();
            }
        } catch (UnsupportedEncodingException e) {
        	logger.error(">>>>>>>>>>>>>>>>>>>UnsupportedEncodingException:"+e);
        } catch (ClientProtocolException e1) {
            logger.error(">>>>>>>>>>>>>>>>>>>ClientProtocolException:"+e1);
        } catch (IOException e1) {
        	logger.error(">>>>>>>>>>>>>>>>>>>IOException:"+e1);
        } catch (ParseException e) {
        	logger.error(">>>>>>>>>>>>>>>>>>>ParseException:"+e);
        } catch (Exception e) {
        	logger.error(">>>>>>>>>>>>>>>>>>>Exception:"+e);
        }
		return "";
	}
	
	public static String getGetResponse(String url){
		return getGetResponse(url, null);
	}
	
	public static String getGetResponse(String url, Map<String, String> header){
		try {
            //已单例形式初始化链接线程池大小
            PoolConnectionManager connectionManager = PoolConnectionManager.getInstance();
            //new一个HttpClinet
            CloseableHttpClient httpClient = connectionManager.getHttpClient();
            //设置http请求参数
            HttpGet getMethod = new HttpGet(url);
            if(header != null && !header.isEmpty()){
            	for(Entry<String,String> e : header.entrySet()){
            		getMethod.setHeader(e.getKey(), e.getValue());
                }
            }
            getMethod.setConfig(PoolConnectionManager.requestConfig);
            //执行请求，获取response
	        CloseableHttpResponse response = httpClient.execute(getMethod);
            try {
                if(response != null
                		&& response.getStatusLine() != null
                		&& response.getStatusLine().getStatusCode()==200){
                	//获取响应内容
                    HttpEntity respEntity = response.getEntity();
                    String responseStr = EntityUtils.toString(respEntity, "UTF-8");
                    if(!StringUtils.isBlank(responseStr)){
						return responseStr;
                    }
                }else{
                	logger.warn("error status code:"+response.getStatusLine().getStatusCode()+",from url:"+url);
                }
            } finally{
            	response.close();
            	getMethod.releaseConnection();
            }
        } catch (UnsupportedEncodingException e) {
        	logger.error(">>>>>>>>>>>>>>>>>>>UnsupportedEncodingException:"+e);
        } catch (ClientProtocolException e1) {
            logger.error(">>>>>>>>>>>>>>>>>>>ClientProtocolException:"+e1);
        } catch (IOException e1) {
        	logger.error(">>>>>>>>>>>>>>>>>>>IOException:"+e1);
        } catch (ParseException e) {
        	logger.error(">>>>>>>>>>>>>>>>>>>ParseException:"+e);
        } catch (Exception e) {
        	logger.error(">>>>>>>>>>>>>>>>>>>Exception:"+e);
        }
		return "";
	}
}
