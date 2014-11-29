package com.lehealth.sync.service.impl;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.lehealth.sync.dao.SyncBloodpressureDao;
import com.lehealth.sync.entity.YundfRecord;
import com.lehealth.sync.entity.YundfUser;
import com.lehealth.sync.service.SyncBloodpressureService;
import com.lehealth.util.PoolConnectionManager;
import com.lehealth.util.YundfUtils;

@Service("syncBloodpressureService")
public class SyncBloodpressureServiceImpl implements SyncBloodpressureService{

	@Autowired
	@Qualifier("syncBloodpressureDao")
	private SyncBloodpressureDao syncBloodpressureDao;
	
	private static Logger logger = Logger.getLogger(SyncBloodpressureServiceImpl.class);

	@Override
	public void syncFromYundf() {
		String loginStr=YundfUtils.encode(getResponse(YundfUtils.loginUrl,YundfUtils.encode(YundfUtils.login.getLogin().toString())));
		YundfUtils.login.setUid("");
		YundfUtils.login.setToken("");
		String friendListStr=YundfUtils.encode(getResponse(YundfUtils.friendListUrl,YundfUtils.encode(YundfUtils.login.getFriendsRequest().toString())));
		List<YundfUser> userList=new ArrayList<YundfUser>();
		for(YundfUser user:userList){
			YundfUtils.login.setAccid("");
			int index=0;
			for(int i=0;i<=10;i++){
				YundfUtils.login.setIndex(index);
				String recordListStr=YundfUtils.encode(getResponse(YundfUtils.loginUrl,YundfUtils.encode(YundfUtils.login.getRecordsRequest().toString())));
				List<YundfRecord> recordList=new ArrayList<YundfRecord>();
				boolean flag=this.syncBloodpressureDao.saveRecord(recordList);
				index=+50;
				if(recordList.size()<YundfUtils.limitCount){
					break;
				}
			}
		}
		
	}
	
	private String getResponse(String url,String requestBody){
		try {
            //已单例形式初始化链接线程池大小
            PoolConnectionManager connectionManager=PoolConnectionManager.getInstance();
            //new一个HttpClinet
            CloseableHttpClient httpClient=connectionManager.getHttpClient();
            //设置http请求参数
            HttpPost postMethod = new HttpPost(url);
            for(Entry<String,String> e:YundfUtils.getHeaderConfig().entrySet()){
            	postMethod.setHeader(e.getKey(),e.getValue());
            }
            postMethod.setConfig(PoolConnectionManager.requestConfig);
            //请求的body里放入请求的json字符串
            postMethod.setEntity(new StringEntity(requestBody, "UTF-8"));
            //执行请求，获取response
	        CloseableHttpResponse response= httpClient.execute(postMethod);
            try {
                //获取响应内容
                HttpEntity respEntity = response.getEntity();
                String responseStr = EntityUtils.toString(respEntity, "UTF-8");
                if(response.getStatusLine().getStatusCode()!=200){
                    logger.warn("error status code:"+response.getStatusLine().getStatusCode());
                }else{
                    if(!StringUtils.isBlank(responseStr)){
						logger.info("json==>"+responseStr);
						return responseStr;
                    }
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
}
