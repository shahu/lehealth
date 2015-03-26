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

import com.lehealth.api.service.BloodpressureService;
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
	
	@Autowired
	@Qualifier("bloodpressureService")
	private BloodpressureService bloodpressureService;
	
	private static Logger logger = Logger.getLogger(SyncBloodpressureServiceImpl.class);

	@Override
	public void syncFromYundf() {
		boolean readySync=false;
		logger.info("readySync yundf begin ");
		//通过手机号和密码访问登录接口
		JSONObject loginObj=getDataFromYundf(YundfUtils.loginUrl,YundfUtils.login.getLogin());
		//获取登录接口返回
		if(loginObj!=null){
			JSONObject errorObj=loginObj.getJSONObject("error");
			int code=errorObj.getInt("code");
			if(code==0){
				JSONObject accountObj=loginObj.getJSONObject("account");
				YundfUtils.login.setUid(StringUtils.trimToEmpty(accountObj.getString("uid")));
				YundfUtils.login.setToken(StringUtils.trimToEmpty(loginObj.getString("token")));
				logger.info("get yundf login uid="+StringUtils.trimToEmpty(accountObj.getString("uid"))
						+",token="+StringUtils.trimToEmpty(loginObj.getString("token")));
				readySync=true;
			}
		}
		logger.info("readySync yundf is "+readySync);
		Map<String,YundfUser> users=new HashMap<String, YundfUser>();
		if(readySync){
			//通过登录接口返回的uid和token获取好友列表
			JSONObject friendsObj=getDataFromYundf(YundfUtils.friendListUrl, YundfUtils.login.getFriendsRequest());
			//获取好友列表返回
			if(friendsObj!=null){
				JSONObject errorObj=friendsObj.getJSONObject("error");
				int code=errorObj.getInt("code");
				if(code==0){
					JSONArray friendArray=friendsObj.getJSONArray("list");
					for(int i=0;i<friendArray.size();i++){
						JSONObject friendObj=friendArray.getJSONObject(i);
						if(friendObj.getBoolean("access")){
							String phone=StringUtils.trimToEmpty(friendObj.getString("phone"));
							if(phone.length()!=14){
								logger.info("yundf error phone:"+phone);
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
		logger.info("yundf friendList size="+users.size());
		if(users.size()>0){
			//查询用户上次更新到的条数，没有则认为是0
			Map<String,YundfUser> existPhones=this.syncBloodpressureDao.getLastRids(users.keySet());
			logger.info("get yundf user last rids");
			for(Entry<String, YundfUser> e:users.entrySet()){
				// 过滤非系统用户
				if(!existPhones.containsKey(e.getKey())){
					logger.info("phone:"+e.getKey()+" is not lehealth user");
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
				logger.info("sync yundf accid:"+accId+",phone:"+e.getKey()+",userid:"+userId+" record from "+lastRid);
				JSONObject recordsObj=getDataFromYundf(YundfUtils.recordListUrl, YundfUtils.login.getRecordsRequest());
				if(recordsObj!=null){
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
								// 短信通知
								this.bloodpressureService.checkBloodpressureConfig(yundfRecord.getSystolic(), yundfRecord.getDiastolic(), yundfRecord.getPulse(), e.getKey());
							}
						}else{
							e.getValue().setLastRid(-1);
						}
					}else{
						logger.info("yundf get recode list failed,accid="+recordsObj.getInt("acc_id")+",phone="+e.getKey());
						continue;
					}
				}
			}
			logger.info("yundf recordlist save to db");
			List<YundfUser> userList=new ArrayList<YundfUser>();
			Set<String> userIds=new HashSet<String>();
			for(Entry<String,YundfUser> e:users.entrySet()){
				if(e.getValue().getLastRid()<=0){
					logger.info("sync yundf recodelist,user:"+e.getValue().getUserId()+" no new record");
					continue;
				}
				//掺入记录
				this.syncBloodpressureDao.insertRecord(e.getValue().getUserId(),e.getValue().getRecords());
				if(userIds.add(e.getValue().getUserId())){
					userList.add(e.getValue());
				}
			}
			if(userIds.size()>0){
				//先删除后插入
				this.syncBloodpressureDao.deleteSyncRid(userIds);
				this.syncBloodpressureDao.insertSyncRid(userList);
			}
			logger.info("yundf recordlist save to db end");
		}
		logger.info("yundf sync end");
	}
	
	private static JSONObject getDataFromYundf(String url,JSONObject requestObj){
		if(requestObj==null){
			return null;
		}
		logger.info("yundf request uri:"+url);
		logger.info("yundf request queryString:"+requestObj.toString());
		String requestBody=YundfUtils.encode(requestObj.toString());
		String responseBody=YundfUtils.encode(getResponse(url,requestBody));
		logger.info("yundf request response:"+responseBody);
		if(StringUtils.isBlank(responseBody)){
			logger.info("yundf request response is empty or error");
			return null;
		}
		try{
			JSONObject resultObj=JSONObject.fromObject(responseBody);
			return resultObj;
		}catch(Exception e){
			logger.info("yundf request response parse json exception,",e);
			return null;
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
                    logger.warn("error status code:"+response.getStatusLine().getStatusCode()+",from url:"+url);
                }else{
                    if(!StringUtils.isBlank(responseStr)){
//                    	System.out.println("url="+url);
//                    	System.out.println("request decode="+YundfUtils.encode(requestBody));
//                    	System.out.println("reponse decode="+YundfUtils.encode(responseStr));
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
