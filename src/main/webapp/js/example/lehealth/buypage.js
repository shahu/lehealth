define(function(require, exports, module) {

	var jssdk = require('http://res.wx.qq.com/open/js/jweixin-1.0.0.js');
	var $ = require('jquery_mobile');
	var util = require('./common');

	var getGoodInfoUrl = "/lehealth/api/goods/detail",
		getWxConfigUrl = "",
		generateOrderUrl = "",
		getOrderRsUrl = "",
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

			var openId;

			var goodsId = util.parseUri(window.location.href).getQueryParameter('id');
			var code = util.parseUri(window.location.href).getQueryParameter('code');
			if(!code) {
				util.toast("页面加载失败");
				return;	
			}
			$.ajax({
				url: getGoodInfoUrl,
				type: 'GET',
				dataType: 'json',
				data: {
					"goodsid": goodsId
				},
				success: function(rsp) {
					if (rsp.errorcode) {

						//通过code获取openid
						$.ajax({
							url: getAccessCodeUrl,
							type: 'GET',
							dataType: 'json',
							data: {
								appid: '',
								secret: '',
								code: code
							},
							success: function(rsp) {
								if(rsp.errcode) {
									util.toast("获取商品信息失败");
									return;
								}
								openId = rsp.openid;

								//绑定点击事件
								

							},
							error: function() {
								util.toast("获取商品信息失败");
							}
						})

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
			
		});
	};

});