define(function(require, exports, module) {

	var $ = require('jquery_mobile');
	var util = require('./common');

	var bpConfigUrl = "/lehealth/api/bpsetting.do";

	exports.render = function() {

		$(document).off("pageshow", "#bpconfigpage");
		$(document).on("pageshow", "#bpconfigpage", function() {

			$("body").css("display", "inline");

			util.hideAddressBar();

			var username = util.getCookieByKey("loginid"),
				token = util.getCookieByKey("tk");
			$.ajax({
				url: bpConfigUrl,
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
							util.setCookie("jump", "/lehealth/config.html");
							$.mobile.changePage("/lehealth/login.html", "slide");
							return;
						}
						util.toast("获取数据失败，请刷新界面");
					} else {
						var bpsettings = rspData.result;
						$('#sbpl').val(bpsettings.sbp1).slider('refresh'),
						$('#sbph').val(bpsettings.sbp2).slider('refresh'),
						$('#dbpl').val(bpsettings.dbp1).slider('refresh'),
						$('#dbph').val(bpsettings.dbp2).slider('refresh');
					}
				},
				error: function(xhr, errormsg) {
					util.toast("获取数据失败，请刷新界面");
				}
			});
			$("#config_submit").off('click');
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
								util.setCookie("jump", "/lehealth/bpconfig.html");
								$.mobile.changePage("/lehealth/login.html", "slide");
								return;
							}
							util.toast("提交数据失败，请重新提交");
						} else {
							util.toast("提交成功");
						}
					},
					error: function(xhr, errormsg) {
						util.toast("提交数据失败，请重新提交");
					}
				});
			});			
		});
	};


});