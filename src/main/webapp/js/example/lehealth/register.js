define(function(require, exports, module) {

	var $ = require('jquery_mobile');
	var util = require('./common');

	var loginUrl = "/lehealth/api/login.do";
	var registerUrl = "/lehealth/api/patient/register.do";


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

		$(document).off("pageshow", "#register");
		$(document).on("pageshow", "#register", function() {

			$("body").css("display", "inline");

			$.mobile.loading('hide');			

			$("#doRegister").off("click");
			$("#doRegister").on("click", function doRegisterFn(event) {
				var username = $("#register_username").val(),
					pwd = $("#register_pwd").val(),
					repwd = $("#register_repwd").val();
				if(!/\d{11}/.test(username)) {
					util.toast("手机号不正确");
					return;
				}
				if(!pwd || pwd.length <= 6) {
					util.toast("密码不能低于六位");
					return;
				}
				if(pwd !== repwd) {
					util.toast("两次输入密码不一致");
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
						password2: repwd
					},
					success: function(rspData) {
						if(rspData.errorcode) {
							util.toast("注册失败，" + rspData.errormsg);
						} else {
							util.toast("注册成功, 请登录");
							setTimeout(function() {
								$.mobile.changePage("/lehealth/login.html", {transition: "slide",reverse:"true",changeHash: true});
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