define(function(require, exports, module) {

	var $ = require('jquery_mobile');
	var util = require('./common');

	var bpConfigUrl = "/api/bpsetting";

	exports.render = function() {
		$(document).bind("pageinit", function() {
			util.hideAddressBar();

			var username = util.getCookieByKey("loginid"),
				token = util.getCookieByKey("tk");
			$.ajax({
				url: submitBpConfigUrl,
				type: "GET",
				dataType: "json",
				async: true,
				data: {
					loginid: username,
					token: token
				},
				success: function(rspData) {
					if (rspData.errorcode) {
						if (rspData.errorcode === 1) {
							util.showDialog("请重新登录", "bpconfigpage");
							setTimeout(function() {
								$.mobile.changePage("/lehealth/login.html", "slide");
							}, 2000);
							return;
						}
						util.showDialog("获取数据失败，请刷新界面", "bpconfigpage");
					} else {
						var bpsettings = rspData.result;
						$('#sbpl').val(bpsettings.sbp1).slider('refresh'),
						$('#sbph').val(bpsettings.sbp2).slider('refresh'),
						$('#dbpl').val(bpsettings.dbp1).slider('refresh'),
						$('#dbph').val(bpsettings.dbp2).slider('refresh');
					}
				},
				error: function(xhr, errormsg) {
					util.showDialog("获取数据失败，请刷新界面", "bpconfigpage");
				}
			});
		});
	};

	exports.bindEvent = function() {
		$(document).bind("pageinit", function() {
			$("#config_submit").on('click', function(event) {
				var username = util.getCookieByKey("loginid"),
					token = util.getCookieByKey("tk"),
					sbp1 = $('#sbpl').val(),
					sbp2 = $('#sbph').val(),
					dbp1 = $('#dbpl').val(),
					dbp2 = $('#dbph').val();
				$.ajax({
					url: submitBpConfigUrl,
					type: "POST",
					dataType: "json",
					async: true,
					data: {
						loginid: username,
						token: token,
						sbp1: sbp1,
						sbp2: sbp2,
						dbp1: dbp1,
						dbp2: dbp2
					},
					success: function(rspData) {
						if (rspData.errorcode) {
							if (rspData.errorcode === 1) {
								util.showDialog("请重新登录", "bpconfigpage");
								setTimeout(function() {
									$.mobile.changePage("/lehealth/login.html", "slide");
								}, 2000);
								return;
							}
							util.showDialog("提交数据失败，请重新提交", "bpconfigpage");
						} else {
							util.showDialog("提交成功", "bpconfigpage");
							//两秒后隐藏
							setTimeout(function() {
								util.dismissDialog("bpconfigpage");
							}, 2000);
						}
					},
					error: function(xhr, errormsg) {
						util.showDialog("提交数据失败，请重新提交", "bpconfigpage");
					}
				});
			});
		});
	};

});