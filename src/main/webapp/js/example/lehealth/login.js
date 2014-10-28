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

		$.mobile.loading( 'show', {
				text: '页面加载中...',
				textVisible: true,
				theme: 'c',
				html: ''
		});		

		$(document).off("pageshow", "#login");
		$(document).on("pageshow", "#login",function() {

			$("body").css("display", "inline");

			$.mobile.loading('hide');			

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
								util.toast("登录失败, " + rspData.errormsg);
							} else {
								var jumpUrl = util.getCookieByKey("jump");
								util.setCookie("jump", "");
								util.toast("登录成功");
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
							util.toast("登录失败, 网络错误");
						}
					});
				} else if(!username){
					util.toast("请填写用户名");
				} else {
					util.toast("请输入密码");
				}
			});

		});
	};

});