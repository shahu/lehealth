define(function(require, exports, module) {

	var wx = require('http://res.wx.qq.com/open/js/jweixin-1.0.0.js');
	var $ = require('jquery_mobile');
	var util = require('./common');

	var getOrderInfoUrl = "";

	exports.render = function() {
		
	};

	exports.bindEvent = function() {

		$.mobile.loading( 'show', {
				text: '页面加载中...',
				textVisible: true,
				theme: 'c',
				html: ''
		});		

		$(document).off("pageshow", "#myorder");

		$(document).on("pageshow", "#myorder", function() {

			$("#ordercover").css("display", "none");

			$.mobile.loading('hide');			
			//获取
			$.ajax({
				url: getOrderInfoUrl,
				type: 'GET',
				dataType: 'json',
				data: {
					"orderid": orderId
				},
				success: function(rsp) {
					
				},
				error: function() {
					$("#ordercover").css("display", "none");

					$.mobile.loading('hide');

					util.toast("获取商品信息失败");				
				}
			});
		});
	};

});