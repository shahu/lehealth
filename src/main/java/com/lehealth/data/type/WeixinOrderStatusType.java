package com.lehealth.data.type;

public enum WeixinOrderStatusType {
	// 新订单
	create(0),
	// 未支付
	prepay(1),
	// 已支付
	success(2),
	// 异常结束
	error(3),
	// 关闭
	close(4);
	
	private WeixinOrderStatusType(int code) {
		this.code = code;
	}
	
	private final int code;
	
	public int getCode() {
		return code;
	}
}
