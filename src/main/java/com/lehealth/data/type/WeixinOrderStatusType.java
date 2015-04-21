package com.lehealth.data.type;

public enum WeixinOrderStatusType {
	// 新订单
	create(0),
	// 未支付
	prepay(1),
	// 已支付
	pay(2),
	// 已到账
	success(3),
	// 异常结束
	error(4),
	// 关闭
	close(5);
	
	private WeixinOrderStatusType(int code) {
		this.code = code;
	}
	
	private final int code;
	
	public int getCode() {
		return code;
	}
}
