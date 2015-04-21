/*
 *  Copyright (c) 2014 The CCP project authors. All Rights Reserved.
 *
 *  Use of this source code is governed by a Beijing Speedtong Information Technology Co.,Ltd license
 *  that can be found in the LICENSE file in the root of the web site.
 *
 *   http://www.yuntongxun.com
 *
 *  An additional intellectual property rights grant can be found
 *  in the file PATENTS.  All contributing project authors may
 *  be found in the AUTHORS file in the root of the source tree.
 */
package com.lehealth.common.sdk;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.BasicHttpEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import com.lehealth.common.util.Constant;
import com.lehealth.common.util.PoolConnectionManager;

public class CCPRestSDK {

	private static final String TemplateSMS = "SMS/TemplateSMS";
	public static final String SERVER_PORT = "8883";
	public static final String PROTOCOL = "TLS";
	public static final String SCHEME = "https";

	/**
	 * 发送短信模板请求
	 * 
	 * @param to
	 *            必选参数 短信接收端手机号码集合，用英文逗号分开，每批发送的手机号数量不得超过100个
	 * @param templateId
	 *            必选参数 模板Id
	 * @param datas
	 *            可选参数 内容数据，用于替换模板中{序号}
	 * @return
	 */
	public JSONObject sendTemplateSMS(String to, String templateId, String[] datas, String domain,String appId,String sid, String token) {
		JSONObject errorInfo = this.accountValidate(domain, appId, sid, token);
		if(errorInfo != null){
			return errorInfo;
		}
			
		if (StringUtils.isBlank(to) 
				|| StringUtils.isBlank(appId) 
				|| StringUtils.isBlank(templateId)){
			throw new IllegalArgumentException("必选参数:" + (StringUtils.isBlank(to) ? " 手机号码 " : "") + (StringUtils.isBlank(templateId) ? " 模板Id " : "") + "为空");
		}
			
		String timestamp = DateFormatUtils.format(new Date(), Constant.dateFormat_yyyymmddhhmmss);
		String acountName = sid;
		String sig = sid + token + timestamp;
		String acountType = "Accounts";
		String signature = DigestUtils.md5Hex(sig);
		String url = getBaseUrl(domain).append("/" + acountType + "/").append(acountName).append("/" + TemplateSMS + "?sig=").append(signature).toString();
		
		String src = acountName + ":" + timestamp;
		String auth = Base64.encodeBase64String(src.getBytes());
		Map<String, String> header = new HashMap<String, String>();
		header.put("Accept", "application/json");
		header.put("Content-Type", "application/json;charset=utf-8");
		header.put("Authorization", auth);
		
		JSONObject json = new JSONObject();
		json.accumulate("appId", appId);
		json.accumulate("to", to);
		json.accumulate("templateId", templateId);
		if (datas != null) {
			JSONArray Jarray = new JSONArray();
			for (String s : datas) {
				Jarray.add(s);
			}
			json.accumulate("datas", Jarray);
		}
		String requestBody = json.toString();
		
		DefaultHttpClient httpclient = null;
		HttpPost postMethod = null;
		try{
			httpclient = new CcopHttpClient().registerSSL(domain, NumberUtils.toInt(SERVER_PORT), "TLS", "https");
			postMethod = new HttpPost(url);
			if(header != null && !header.isEmpty()){
				for(Entry<String,String> e:header.entrySet()){
		        	postMethod.setHeader(e.getKey(),e.getValue());
		        }
			}
	        postMethod.setConfig(PoolConnectionManager.requestConfig);
	        BasicHttpEntity requestEntity = new BasicHttpEntity();
	        requestEntity.setContent(new ByteArrayInputStream(requestBody.getBytes("UTF-8")));
	        requestEntity.setContentLength(requestBody.getBytes("UTF-8").length);
			postMethod.setEntity(requestEntity);
			
			HttpResponse response = httpclient.execute(postMethod);
			
			HttpEntity entity = response.getEntity();
			if (entity != null){
				String result = EntityUtils.toString(entity, "UTF-8");
				EntityUtils.consume(entity);
				return JSONObject.fromObject(result);
			}
		} catch(UnsupportedEncodingException e){
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e){
			e.printStackTrace();
		} finally {
			if(postMethod != null){
				postMethod.releaseConnection();
			}
			if(httpclient != null){
				httpclient.getConnectionManager().shutdown();
			}
		}
		return getMyError("999999", "错误");
	}


	private StringBuffer getBaseUrl(String domain) {
		StringBuffer sb = new StringBuffer("https://");
		sb.append(domain).append(":").append(SERVER_PORT);
		sb.append("/2013-12-26");
		return sb;
	}
	
	private JSONObject accountValidate(String domain, String appId, String sid, String token) {
		if ((StringUtils.isBlank(domain))) {
			return getMyError("172004", "IP为空");
		}
		if ((StringUtils.isBlank(SERVER_PORT))) {
			return getMyError("172005", "端口错误");
		}
		if ((StringUtils.isBlank(sid))) {
			return getMyError("172006", "主帐号为空");
		}
		if ((StringUtils.isBlank(token))) {
			return getMyError("172007", "主帐号令牌为空");
		}
		if ((StringUtils.isBlank(appId))) {
			return getMyError("172012", "应用ID为空");
		}
		return null;
	}
	
	private JSONObject getMyError(String code, String msg) {
		JSONObject json = new JSONObject();
		json.accumulate("statusCode", code);
		json.accumulate("statusMsg", msg);
		return json;
	}
	
}