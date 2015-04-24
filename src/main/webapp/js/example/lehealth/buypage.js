define(function(require, exports, module) {

	var wx = require('http://res.wx.qq.com/open/js/jweixin-1.0.0.js');
	var $ = require('jquery_mobile');
	var util = require('./common');

	var getGoodInfoUrl = "/lehealth/api/goods/detail",
		getWxConfigUrl = "/lehealth/weixin/signature",
		generateOrderUrl = "weixin/pre/pay",
		getOrderRsUrl = "order/info",
		getAccessCodeUrl = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=APPID&secret=SECRET&code=CODE&grant_type=authorization_code";

	exports.render = function() {
		
	};

	exports.bindEvent = function() {

		$.mobile.loading( 'show', {
				text: '页面加载中...',
				textVisible: true,
				theme: 'c',
				html: ''
		});		

		$(document).off("pageshow", "#buypage");

		$(document).on("pageshow", "#buypage", function() {

			var openId,
				queryRsTimeoutHandler,
				queryRsIntervalHandler;

			var goodsId = util.parseUri(window.location.href).getQueryParameter('id');
			var code = util.parseUri(window.location.href).getQueryParameter('code');
			var username = util.getCookieByKey("loginid"),
				token = util.getCookieByKey("tk");				
			if(!code) {
				util.toast("页面加载失败");
				return;	
			}
			//获取
			$.ajax({
				url: getGoodInfoUrl,
				type: 'GET',
				dataType: 'json',
				data: {
					"goodsid": goodsId
				},
				success: function(rsp) {
					if (!rsp.errorcode) {

						$('#goodsname').text(rsp.result.name);
						$('#goodsdetail').text(rsp.result.detail);
						$('#goodsfee').text(rsp.result.fee);

						//通过code获取openid
						var tm = parseInt((new Date()).getTime()/1000);
						$.ajax({
							url: getWxConfigUrl,
							type: 'GET',
							dataType: 'json',
							data: {
								loginid: username,
								token: token,
								timestamp: tm,
								url: window.location.href
							},
							success: function(rsp) {
								if(rsp.errcode) {
									util.toast("获取商品信息失败");
									return;
								}
								wx.config({
									debug: true,
									appId: rsp.result.appid,
									timestamp: tm,
									nonceStr: rsp.result.nonceStr,
									signature: rsp.result.signature,
									jsApiList: ["chooseWXPay"]
								});
							},
							error: function() {
								util.toast("获取商品信息失败");
							}
						});

					} else {
						$("#buypagecover").css("display", "none");

						$.mobile.loading('hide');

						util.toast("获取商品信息失败");							
					}
				},
				error: function() {
					$("#buypagecover").css("display", "none");

					$.mobile.loading('hide');

					util.toast("获取商品信息失败");				
				}
			});

			wx.ready(function(res) {
				$("#buypagecover").css("display", "none");
				$.mobile.loading('hide');
				//绑定点击购买事件
				$('#dobuy').off("click");
				$('#dobuy').on("click", function() {
					$.mobile.loading( 'show', {
						text: '支付中...',
						textVisible: true,
						theme: 'c',
						html: ''
					});
					var weixinCallbackSuccess = false,
						serverCbSuccess = false;

					$.ajax({
						url: generateOrderUrl,
						type: "GET",
						dataType: 'json',
						data: {
							loginid: username,
							token: token,
							goodsid: goodsId,
							code: code
						},
						success: function(rsp) {
							if(rsp.errorcode) {
								$.mobile.loading('hide');
								util.toast("创建订单失败，请重新支付");									
							} else {
								var data = rsp.result;
								var orderId = data.orderid;
								wx.chooseWXPay({
									timestamp: data.timestamp,
									nonceStr: data.noncestr,
									package: data.package,
									signType: data.signtype,
									paySign: data.paysign.toUpperCase(),
									success: function(res) {
										weixinCallbackSuccess = true;
										if(weixinCallbackSuccess && serverCbSuccess) {
											$.mobile.changePage("buysuccess.html?id=" + orderId, "slide");	
										}	
									},
									fail: function(res) {
										//go to 支付完成页面
										$.mobile.changePage("buysuccess.html?id=" + orderId, "slide");
									},
									cancel: function() {
										$.mobile.loading('hide');
										if(queryRsTimeoutHandler) {
											clearTimeout(queryRsTimeoutHandler);
										}
										if(queryRsIntervalHandler) {
											clearInterval(queryRsIntervalHandler);
										}										
									}
								});
								queryRsTimeoutHandler = setTimeout(function() {
									//go to 支付完成页面
									$.mobile.changePage("buysuccess.html?id=" + orderId, "slide");
								}, 30000);
								queryRsIntervalHandler = setInterval(function() {
									//query bill lastest info
									$.ajax({
										url: getOrderRsUrl,
										type: "get",
										dataType: "json",
										data: {
											orderid: orderId,
											loginid: username,
											token: token
										},
										succss: function(res) {
											if(!res.errorcode) {
												var orderDetail = res.result;
												if(orderDetail.status == 2) {
													serverCbSuccess = true;
													if(serverCbSuccess && weixinCallbackSuccess) {
														$.mobile.changePage("buysuccess.html?id=" + orderId, "slide");	
													}
												} else if(orderDetail.status == 3) {
													$.mobile.changePage("buysuccess.html?id=" + orderId, "slide");
												} else if(orderDetail.status == 4) {
													$.mobile.changePage("buysuccess.html?id=" + orderId, "slide");	
												}												
											}
										}
									});
								}, 3000);															
							}
						},
						error: function(e) {
							$.mobile.loading('hide');
							util.toast("创建订单失败，请重新支付");	
						}
					})

				});
			});

			wx.error(function(res) {
				//出错了，可以更新签名
				$("#buypagecover").css("display", "none");
				$.mobile.loading('hide');
				util.toast("微信验证失败");	
				console.info(res);			
			});
			
		});

		$(document).off("pagehide", "#buypage");

		$(document).on("pagehide", "#buypage", function() {
			if(queryRsTimeoutHandler) {
				clearTimeout(queryRsTimeoutHandler);
			}
			if(queryRsIntervalHandler) {
				clearInterval(queryRsIntervalHandler);
			}

			$.mobile.loading('hide');
		});
	};

});