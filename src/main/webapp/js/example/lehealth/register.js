define(function(require, exports, module) {

	var $ = require('jquery_mobile');
	var util = require('./common');

	var loginUrl = "/lehealth/api/login";
	var registerUrl = "/lehealth/api/patient/register";
	var obtainCodeUrl = "/lehealth/api/identifyingcode";


	exports.render = function() {
		// $(document).one("pageshow", function() {
		// 	util.hideAddressBar();
		// });
	};

	exports.bindEvent = function() {

		$.mobile.loading('show', {
			text: '页面加载中...',
			textVisible: true,
			theme: 'c',
			html: ''
		});

		$(document).off("pageshow", "#register");
		$(document).on("pageshow", "#register", function() {

			$("body").css("display", "inline");

			$.mobile.loading('hide');
			$("#obtainCode").off("click");
			$('#obtainCode').on("click", function() {
				var username = $("#register_username").val();
				if (!/^(13[0-9]|15[0-9]|14[7|5]|17[0-9]|18[0-9])\d{8}$/.test(username)) {
					util.toast("手机号不正确");
					return;
				}
				$('#obtainCode').prop('disabled', true).addClass('ui-disabled');
				$.ajax({
					url: obtainCodeUrl,
					type: 'GET',
					dataType: 'json',
					data: {
						phone: username
					},
					success: function(rsp) {
						if (rsp.errorcode) {
							util.toast(rsp.errormsg);
							$('#obtainCode').prop('disabled', false).removeClass('ui-disabled');
						} else {
							$('#obtainCode').prop('disabled', true).addClass('ui-disabled');
							$('#obtainCode').text("60");
							var countDown = setInterval(function() {
								var timeInSec = parseInt($('#obtainCode').text());
								timeInSec--;
								console.info(timeInSec);
								if (timeInSec < 0) {
									clearInterval(countDown);
									$('#obtainCode').prop('disabled', false).removeClass('ui-disabled');
									$('#obtainCode').text("获取");
								} else {
									$('#obtainCode').text(timeInSec < 10 ? ("0" + timeInSec) : (timeInSec + ""));
								}
							}, 1000);
						}
					},
					error: function(rsp) {
						util.toast("获取验证码失败");
						$('#obtainCode').prop('disabled', false).removeClass('ui-disabled');
					}
				});
			});

			$("#doRegister").off("click");
			$("#doRegister").on("click", function doRegisterFn(event) {
				var username = $("#register_username").val(),
					pwd = $("#register_pwd").val(),
					repwd = $("#register_repwd").val(),
					verifyCode = $('#verifyCode').val();
				if (!/^(13[0-9]|15[0-9]|14[7|5]|17[0-9]|18[0-9])\d{8}$/.test(username)) {
					util.toast("手机号不正确");
					return;
				}
				if (!pwd || pwd.length <= 6) {
					util.toast("密码不能低于六位");
					return;
				}
				if (pwd !== repwd) {
					util.toast("两次输入密码不一致");
					return;
				}
				if (!verifyCode) {
					util.toast("请输入验证码");
					return;
				}
				$.ajax({
					url: registerUrl,
					type: "POST",
					dataType: "json",
					async: true,
					data: {
						loginid: username,
						password: pwd,
						password2: repwd,
						identifyingcode: verifyCode
					},
					success: function(rspData) {
						if (rspData.errorcode) {
							util.toast("注册失败，" + rspData.errormsg);
						} else {
							util.toast("注册成功, 请登录");
							setTimeout(function() {
								$.mobile.changePage("/lehealth/login.html", {
									transition: "slide",
									reverse: "true",
									changeHash: true
								});
							}, 2000);
						}
					},
					error: function(xhr, errormsg) {
						// util.showDialog("网络错误", "register");
						util.toast("网络错误");
						// $("#doRegister").unbind('click').one('click', doRegisterFn);
					}
				});
			});
		});
	};

});