package com.lehealth.data.type;

public enum WeixinOrderStatusType {
	create(0),
	prepay(1),
	pay(2),
	close(3);
	
	private WeixinOrderStatusType(int code) {
		this.code = code;
	}
	
	private final int code;
	
	public int getCode() {
		return code;
	}
}
