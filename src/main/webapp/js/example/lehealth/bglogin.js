define(function(require, exports, module) {

	var $ = require('jquery');
	var util = require('./bgcommon');

	var loginUrl = "/lehealth/api/login.do";


	exports.render = function() {
		console.info("onclick");
		$('.login_btn').on('click', function(event) {
			var username = $('#login_username').val(),
				pwd = $('#login_pwd').val();
			if(!username) {
				// $('#login_username').popover({
				// 	"placement": "right",
				// 	"html": "请输入用户名"
				// });
				alert('请输入用户名');
				return;
			}
			if(!pwd) {
				// $('#login_pwd').popover({
				// 	"placement": "right",
				// 	"html": "请输入密码"
				// });	
				alert('请输入密码');
				return;				
			}

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
					if (rspData.errorcode) {
						console.error("login error: " + rspData.errormsg);
						alertMsg("登录失败, " + rspData.errormsg);
					} else {
						alertMsg("登录成功");
						util.setCookie("loginid", encodeURIComponent(rspData.result.loginid));
						util.setCookie("tk", encodeURIComponent(rspData.result.token));
						//两秒后跳转指定页面，否则跳转首页
						setTimeout(function() {
							window.location.href = '/lehealth/backendhome.html';
						}, 2000);
					}
				},
				error: function(xhr, errormsg) {
					console.error("login error: " + errormsg);
					alertMsg("登录失败, 网络错误");
				}
			});
		});
	};

	function alertMsg(msg) {
		$('.login_btn').popover({
			"placement": "right",
			"html": msg
		});
	}

});