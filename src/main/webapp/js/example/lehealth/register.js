define(function(require, exports, module) {

	var $ = require('jquery_mobile');
	var util = require('./common');

	var loginUrl = "/lehealth/api/login.do";
	var registerUrl = "/lehealth/api/register.do";


	exports.render = function() {
		$(document).one("pageshow", function() {
			util.hideAddressBar();
		});
	};

	exports.bindEvent = function() {
		$(document).on("pageshow", function() {

			$("#doRegister").one("click", function(event) {
				var username = $("#register_username").val(),
					pwd = $("#register_pwd").val(),
					repwd = $("#register_repwd").val();
				if(!username) {
					util.showDialog("注册名不能为空", "register");
					return;
				}
				if(!pwd || pwd.length <= 6) {
					util.showDialog("密码不能低于六位", "register");
					return;
				}
				if(pwd !== repwd) {
					util.showDialog("两次输入密码不一致", "register");
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
							util.showDialog("注册失败，" + rspData.errormsg, "register");
						} else {
							console.info("b");
							util.showDialog("注册成功, 请登录", "register");
							setTimeout(function() {
								$.mobile.changePage("#login", {transition: "slide",reverse:"true",changeHash: true});
							}, 2000);
						}
					},
					error: function(xhr, errormsg) {
						util.showDialog("网络错误", "register");
					}
				});				
			});		
		});
	};

});