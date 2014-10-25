define(function(require, exports, module) {

	var $ = require('jquery_mobile');
	var util = require('./common');

	var submitBpUrl = "/lehealth/api/bprecord.do";

	exports.render = function() {
		$(document).one("pageshow", function() {
			util.hideAddressBar();
		});
	};

	exports.bindEvent = function() {
		// $(document).on("pageshow", function() {
			console.info('init');
			$("#dp_record_bpdata").one('click', function(event) {
				var username = util.getCookieByKey("loginid"),
					token = util.getCookieByKey("tk"),
					sbp = $('#sbp').val(),
					dbp = $('#dbp').val(),
					heartbp = $("#heartbp").val();
				$.ajax({
					url: submitBpUrl,
					type: "POST",
					dataType: "json",
					async: true,
					data: {
						loginid: username,
						token: token,
						sbp: sbp,
						dbp: dbp,
						heartrate: heartbp
					},
					success: function(rspData) {
						if (rspData.errorcode) {
							if (rspData.errorcode === 1) {
								util.setCookie("jump", encodeURIComponent("/lehealth/bpinput.html"));
								setTimeout(function() {
									$.mobile.changePage("/lehealth/login.html", "slide");
								}, 2000);
								return;
							}
							util.showDialog("提交数据失败，请重新提交", "bpinputpage");
						} else {
							//两秒后隐藏
							setTimeout(function() {
								$.mobile.changePage("/lehealth/bpmonitor.html", "slide");
							}, 2000);
						}
					},
					error: function(xhr, errormsg) {
						util.showDialog("提交数据失败，请重新提交", "bpinputpage");
					}
				});
			});
		// });
	};

});