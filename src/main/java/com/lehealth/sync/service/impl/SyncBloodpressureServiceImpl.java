package com.lehealth.sync.service.impl;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

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
		boolean readySync=false;
		//通过手机号和密码访问登录接口
		String loginStr=YundfUtils.encode(getResponse(YundfUtils.loginUrl,YundfUtils.encode(YundfUtils.login.getLogin().toString())));
		//获取登录接口返回
		if(StringUtils.isNotBlank(loginStr)){
			JSONObject loginObj=JSONObject.fromObject(loginStr);
			JSONObject errorObj=loginObj.getJSONObject("error");
			int code=errorObj.getInt("code");
			if(code==0){
				JSONObject accountObj=loginObj.getJSONObject("account");
				YundfUtils.login.setUid(StringUtils.trimToEmpty(accountObj.getString("uid")));
				YundfUtils.login.setToken(StringUtils.trimToEmpty(loginObj.getString("token")));
				readySync=true;
			}
		}
		Map<String,YundfUser> users=new HashMap<String, YundfUser>();
		if(readySync){
			//通过登录接口返回的uid和token获取好友列表
			String friendListStr=YundfUtils.encode(getResponse(YundfUtils.friendListUrl,YundfUtils.encode(YundfUtils.login.getFriendsRequest().toString())));
			//获取好友列表返回
			if(StringUtils.isNotBlank(friendListStr)){
				JSONObject friendsObj=JSONObject.fromObject(friendListStr);
				JSONObject errorObj=friendsObj.getJSONObject("error");
				int code=errorObj.getInt("code");
				if(code==0){
					JSONArray friendArray=friendsObj.getJSONArray("list");
					for(int i=0;i<friendArray.size();i++){
						JSONObject friendObj=friendArray.getJSONObject(i);
						if(friendObj.getBoolean("access")){
							String phone=StringUtils.trimToEmpty(friendObj.getString("phone"));
							if(phone.length()!=14){
								logger.info("error phone:"+phone);
								continue;
							}
							YundfUser yundfUser=new YundfUser();
							yundfUser.setAccId(StringUtils.trimToEmpty(friendObj.getString("acc_id")));
							yundfUser.setPhone(phone.substring(3));
							users.put(yundfUser.getPhone(),yundfUser);
						}
					}
				}
			}
		}
		if(users.size()>0){
			//查询用户上次更新到的条数，没有则认为是0
			Map<String,YundfUser> existPhones=this.syncBloodpressureDao.getLastRids(users.keySet());
			for(Entry<String, YundfUser> e:users.entrySet()){
				if(!existPhones.containsKey(e.getKey())){
					logger.info("phone:"+e.getKey()+" is not our user");
					continue;
				}
				String accId=e.getValue().getAccId();
				int lastRid=existPhones.get(e.getKey()).getLastRid();
				String userId=existPhones.get(e.getKey()).getUserId();
				//每次获取50个数据
				YundfUtils.login.setAccid(accId);
				YundfUtils.login.setIndex(lastRid);
				e.getValue().setLastRid(lastRid);
				e.getValue().setUserId(userId);
				
				String recordListStr=YundfUtils.encode(getResponse(YundfUtils.recordListUrl,YundfUtils.encode(YundfUtils.login.getRecordsRequest().toString())));
				if(StringUtils.isNotBlank(recordListStr)){
					JSONObject recordsObj=JSONObject.fromObject(recordListStr);
					JSONObject errorObj=recordsObj.getJSONObject("error");
					int code=errorObj.getInt("code");
					if(code==0){
						JSONArray recordArray=recordsObj.getJSONArray("records");
						if(recordArray.size()>0){
							for(int i=0;i<recordArray.size();i++){
								JSONObject recordObj=recordArray.getJSONObject(i);
								YundfRecord yundfRecord=new YundfRecord();
								int rid=recordObj.getInt("rid");
								yundfRecord.setDiastolic(recordObj.getInt("diastolic"));
								yundfRecord.setPulse(recordObj.getInt("pulse"));
								yundfRecord.setRdatetime(StringUtils.trimToEmpty(recordObj.getString("rdatetime")));
								yundfRecord.setSystolic(recordObj.getInt("systolic"));
								yundfRecord.setRid(rid);
								if(rid>e.getValue().getLastRid()){
									e.getValue().setLastRid(rid);
								}
								e.getValue().addRecord(yundfRecord);
							}
						}else{
							e.getValue().setLastRid(-1);
						}
					}else{
						logger.info("yundf accid="+recordsObj.getInt("acc_id")+",phone="+e.getKey());
						continue;
					}
				}
			}
			List<YundfUser> userList=new ArrayList<YundfUser>();
			Set<String> userIds=new HashSet<String>();
			for(Entry<String,YundfUser> e:users.entrySet()){
				if(e.getValue().getLastRid()==-1){
					continue;
				}
				//掺入记录
				this.syncBloodpressureDao.insertRecord(e.getValue().getUserId(),e.getValue().getRecords());
				if(userIds.add(e.getValue().getUserId())){
					userList.add(e.getValue());
				}
			}
			//先删除后插入
			this.syncBloodpressureDao.deleteSyncRid(userIds);
			this.syncBloodpressureDao.insertSyncRid(userList);
		}
	}
	
	private static String getResponse(String url,String requestBody){
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
                    	System.out.println("url="+url);
                    	System.out.println("request decode="+YundfUtils.encode(requestBody));
                    	System.out.println("reponse decode="+YundfUtils.encode(responseStr));
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
