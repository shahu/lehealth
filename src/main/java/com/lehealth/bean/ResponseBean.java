package com.lehealth.bean;

import com.lehealth.type.ErrorCodeType;

public class ResponseBean {
	
	private ErrorCodeType type=ErrorCodeType.normal;
	private Object result="";
	
	public int getErrorcode() {
		return  type.getCode();
	}
	public void setType(ErrorCodeType type) {
		this.type=type;
	}
	public String getErrormsg() {
		return type.getMessage();
	}
	public Object getResult() {
		return result;
	}
	public void setResult(Object result) {
		this.result = result;
	}
}
