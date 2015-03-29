package com.lehealth.common.util;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.lehealth.sync.entity.YundfLogin;

public class YundfUtils {

	public static int source=1; 
	public static String loginUrl="https://smartbp.duapp.com/account/login";
	public static String friendListUrl="https://smartbp.duapp.com/account/friend/list";
	public static String recordListUrl="https://smartbp.duapp.com/account/record/list";
	
	public static int limitCount=50;
	
	public static YundfLogin login=new YundfLogin();
	static{
		login.setPhoneNumber("+8618621545318");
		login.setPassword("1234qwer");
		login.setCount(limitCount);
		login.setDirector(true);
	}
	
	public static byte keyByte=0;
	static{
		try {
			String key="Z";
			byte[] temp=key.getBytes("UTF-8");
			if(temp.length>0){
				keyByte=temp[0];
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) throws UnsupportedEncodingException {
		String str="!Pzzx*254?xz`zxqblkblhkonoikbxvPzzx;.#*?xz`zjvPzzx*;))-5(>xz`zxlkc<cc;?<kcnmli?ikil<kbh<bkkn<k>xP'";
		System.out.println(str);
		System.out.println(encode(str));
	}
	
	public static String encode(String str){
		if(StringUtils.isBlank(str)){
			return "";
		}
		try {
			byte[] bArray = str.getBytes("UTF-8");
			byte[] temp=new byte[bArray.length];
			for(int i=0;i<bArray.length;i++){
				temp[i]=(byte) (bArray[i] ^ keyByte);
			}
			return new String(temp,"UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return "";
	}
	
	public static Map<String,String> getHeaderConfig(){
		Map<String,String> headers=new HashMap<String, String>();
		headers.put("APIKey", "N4QAA1U4AX1UVNN42GEYD0M4");
		headers.put("APIVersion", "1.0");
		headers.put("EndPoint", "3");
		headers.put("Content-Type", "application/octet-stream");
		return headers;
	}
}
