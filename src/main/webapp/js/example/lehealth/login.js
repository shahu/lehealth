define(function(require, exports, module) {

	var $ = require('jquery_mobile');
	var util = require('./common');

	var loginUrl = "/lehealth/api/login.do";
	var registerUrl = "/lehealth/api/register.do";


	exports.render = function() {
		// $(document).one("pageshow", function() {
		// 	util.hideAddressBar();
		// });
	};

	exports.bindEvent = function() {
		$(document).off("pageshow", "#login");
		$(document).on("pageshow", "#login",function() {
			$("#doLogin").off('click');
			$('#doLogin').on('click', function doLoginFn(event) {
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
								// $('#doLogin').unbind('click').on('click', doLoginFn);
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
										window.location.href = "http://312560.m.weimob.com/weisite/home?_tj_twtype=16&_tj_pid=312560&_tt=1&_tj_graphicid=60625&_tj_title=%E6%98%93%E5%81%A5%E5%BA%B7-%E7%A7%BB%E5%8A%A8%E6%85%A2%E7%97%85%E7%AE%A1%E7%90%86%E4%B8%93%E5%AE%B6&_tj_keywords=%E6%98%93%E5%81%A5%E5%BA%B7&pid=312560&bid=492252&wechatid=o1baHsytJOuW-OTMfa_Lkj4iZixA&from=1&wxref=mp.weixin.qq.com";
									}
								}, 2000);
							}
						},
						error: function(xhr, errormsg) {
							console.error("login error: " + errormsg);
							util.showDialog("登录失败, 网络错误", "login");
							// $('#doLogin').unbind('click').on('click', doLoginFn);
						}
					});
				} else if(!username){
					util.showDialog("请填写用户名", "login");
					// $('#doLogin').unbind('click').on('click', doLoginFn);
				} else {
					util.showDialog("请输入密码", "login");
					// $('#doLogin').unbind('click').on('click', doLoginFn);
				}
			});

		});
	};

});