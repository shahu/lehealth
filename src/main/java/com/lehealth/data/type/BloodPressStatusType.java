package com.lehealth.data.type;


public enum BloodPressStatusType {
	low(1),
	normal(2),
	high(3);
	
	private BloodPressStatusType(int code) {
		this.code = code;
	}
	
	private final int code;

	public int getCode() {
		return code;
	}
	
}
