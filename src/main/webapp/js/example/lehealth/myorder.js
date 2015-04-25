define(function(require, exports, module) {

	var $ = require('jquery_mobile');
	var util = require('./common');

	var getOrderInfoUrl = "weixin/order/list";

	exports.render = function() {

	};

	exports.bindEvent = function() {

		$.mobile.loading('show', {
			text: '页面加载中...',
			textVisible: true,
			theme: 'c',
			html: ''
		});

		$(document).off("pageshow", "#myorder");

		$(document).on("pageshow", "#myorder", function() {

			$("#ordercover").css("display", "none");

			$.mobile.loading('hide');

			var username = util.getCookieByKey("loginid"),
				token = util.getCookieByKey("tk");
			//获取
			$.ajax({
				url: getOrderInfoUrl,
				type: 'GET',
				dataType: 'json',
				data: {
					loginid: username,
					token: token
				},
				success: function(rsp) {
					if (rsp.errorcode) {
						$("#ordercover").css("display", "none");

						$.mobile.loading('hide');

						util.toast("获取订单信息失败");
					} else {
						var orderlist = rsp.result;
						$("#orderlist").empty();
						for (var i = 0; i < orderlist.length; i++) {
							var ordercard = $('#ordercard-tmpl').clone();
							ordercard.removeAttr('id');
							ordercard.css("display", "block");
							var timestr = new Date(orderlist[i].createtime).format("yyyy年 MM月dd日 HH:mm:ss");
							var state = "支付中";
							if (orderlist[i].status == 2) {
								state = "支付完成";
							} else if (orderlist[i].status == 3) {
								state = "支付失败";
							} else if (orderlist[i].status == 4) {
								state = "订单取消";
							}
							ordercard.find("td.td_orderId").text(orderlist[i].orderid);
							ordercard.find("td.td_time").text(timestr);
							ordercard.find("td.td_fee").text(orderlist[i].fee);
							ordercard.find("td.td_state").text(state);
							ordercard.find("td.td_desc").text(orderlist[i].goodsdetail);
							$("#orderlist").append(ordercard);
						}
					}
				},
				error: function() {
					$("#ordercover").css("display", "none");

					$.mobile.loading('hide');

					util.toast("获取订单信息失败");
				}
			});
		});
	};

});