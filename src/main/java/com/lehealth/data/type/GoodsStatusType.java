package com.lehealth.data.type;

public enum GoodsStatusType {
	online(1,"上线"),
	offline(0,"下线");
	
	private GoodsStatusType(int code,String message) {
		this.code = code;
		this.message = message;
	}
	
	private final int code;
	private final String message;
	
	public int getCode() {
		return code;
	}
	public String getMessage() {
		return message;
	}
}
