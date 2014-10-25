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
		$(document).one("pageshow", function() {
			$('#doLogin').one('click', function(event) {
				var username = $('#login_username').val(),
					pwd = $('#login_pwd').val();
				if(username && pwd) {
					$.ajax({
						url: loginUrl,
						type: "GET",
						dataType: "json",
						async: true,
						data: {
							loginid: username,
							password: pwd
						},
						success: function(rspData) {
							if(rspData.errorcode) {
								console.error("login error: " + rspData.errormsg);
								util.showDialog("登录失败, " + rspData.errormsg, "login");
							} else {
								var jumpUrl = util.getCookieByKey("jump");
								util.setCookie("jump", "");
								util.showDialog("登录成功", "login");
								util.setCookie("loginid", encodeURIComponent(rspData.result.loginid));
								util.setCookie("tk", encodeURIComponent(rspData.result.token));
								//两秒后跳转指定页面，否则跳转首页
								setTimeout(function() {
									if(jumpUrl) {
										$.mobile.changePage(jumpUrl, "slide");
									} else {
										$.mobile.changePage("http://312560.m.weimob.com/weisite/home?_tj_twtype=16&_tj_pid=312560&_tt=1&_tj_graphicid=60625&_tj_title=%E6%98%93%E5%81%A5%E5%BA%B7-%E7%A7%BB%E5%8A%A8%E6%85%A2%E7%97%85%E7%AE%A1%E7%90%86%E4%B8%93%E5%AE%B6&_tj_keywords=%E6%98%93%E5%81%A5%E5%BA%B7&pid=312560&bid=492252&wechatid=o1baHsytJOuW-OTMfa_Lkj4iZixA&from=1&wxref=mp.weixin.qq.com", "slide");
									}
								}, 2000);
							}
						},
						error: function(xhr, errormsg) {
							console.error("login error: " + errormsg);
							util.showDialog("登录失败, 网络错误", "login");
						}
					});
				} else if(!username){
					util.showDialog("请填写用户名", "login");
				} else {
					util.showDialog("请输入密码", "login");
				}
			});

			$("#doRegister").on("click", function(event) {
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

	exports.validate = function(cb) {
		var username = util.getCookieByKey("loginid"),
			pwd = util.getCookieByKey("tk");
		if(username && pwd) {
			$.ajax({
				url: loginUrl,
				type: "GET",
				dataType: "json",
				async: true,
				data: {
					loginid: username,
					password: pwd
				},
				success: function(rspData) {
					if(rspData.errorcode) {
						cb(rspData.errorcode, rspData.errormsg);
					} else {
						cb(0);
					}
				},
				error: function(xhr, errormsg) {
					cb(1001, "网络错误");
				}
			});
		} else if(!username){
			cb(1002, "用户名不能为空");
		} else {
			cb(1003, "用户名密码不符");
		}		
	};

});