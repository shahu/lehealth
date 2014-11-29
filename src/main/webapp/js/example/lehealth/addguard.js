define(function(require, exports, module) {

	var $ = require('jquery_mobile');
	var util = require('./common');

	var guardianApi = "/lehealth/api/guardianinfo.do";

	exports.bindEvent = function() {

		$(document).off("pageshow", "#guardianpage");

		$(document).on("pageshow", "#guardianpage", function() {
			$("#guardiancover").css("display", "none");

			var username = util.getCookieByKey("loginid"),
				token = util.getCookieByKey("tk");			

			$("#save_guardian").off('click');
			$("#save_guardian").on('click', function(event) {
				var guardianname = $('#uname').val(),
					guardiannumber = $('#uphone').val();

				if(!guardianname || !guardiannumber || !/\d{11}/.test(guardiannumber)) {
					util.toast("请填写完整数据");	
					return;
				}

				$.ajax({
					url: guardianApi,
					type: "POST",
					dataType: "json",
					async: true,
					data: {
						loginid: username,
						token: token,
						guardianname: guardianname,
						guardiannumber: guardiannumber
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
						setTimeout(function() {
							$.mobile.changePage("/lehealth/guardianlist.html", "slide");
						}, 2000);						
					},
					error: function(xhr, errormsg) {
						util.toast("提交数据失败，请重新提交");
					}
				});					
			});

		});
	};

});