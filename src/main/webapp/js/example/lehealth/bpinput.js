define(function(require, exports, module) {

	var $ = require('jquery_mobile');
	var util = require('./common');

	var submitBpUrl = "/lehealth/api/bprecord.do";

	exports.render = function() {
		// $(document).one("pageshow", function() {
		// 	util.hideAddressBar();
		// });
	};

	exports.bindEvent = function() {

		$.mobile.loading( 'show', {
				text: '页面加载中...',
				textVisible: true,
				theme: 'c',
				html: ''
		});		

		$(document).off("pageshow", "#bpinputpage");

		$(document).on("pageshow", "#bpinputpage", function() {
			console.info('bp input init');

			$("#bpinputcover").css("display", "none");

			$.mobile.loading('hide');


			$("#dp_record_bpdata").off('click');
			$("#dp_record_bpdata").on('click', function do_record_bpdateFn(event) {
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
								util.setCookie("jump", "/lehealth/bpinput.html");
								setTimeout(function() {
									$.mobile.changePage("/lehealth/login.html", "slide");
								}, 2000);
								return;
							}
							util.toast("提交数据失败，请重新提交");
						} else {
							//两秒后隐藏
							setTimeout(function() {
								$.mobile.changePage("/lehealth/bpmonitor.html", "slide");
							}, 2000);
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