define(function(require, exports, module) {

	var $ = require('jquery_mobile');
	var util = require('./common');

	var userConfigUrl = "/lehealth/api/userinfo.do";

	exports.bindEvent = function() {

		$(document).off("pageshow", "#userconfigpage");

		$(document).on("pageshow", "#userconfigpage", function() {
			$("#userconfigcover").css("display", "none");

			function getAndSetUserConfigData() {
				var username = util.getCookieByKey("loginid"),
					token = util.getCookieByKey("tk");
				$.ajax({
					url: userConfigUrl,
					type: "GET",
					dataType: "json",
					async: true,
					data: {
						loginid: username,
						token: token
					},
					success: function(rspData) {
						if (rspData.errorcode) {
							if (rspData.errorcode === 1) {
								util.setCookie("jump", "/lehealth/config.html");
								$.mobile.changePage("/lehealth/login.html", "slide");
								return;
							}
							if (rspData.errorcode == 3) {
								return;//服务端无数据，采用默认数据
							}
							util.toast("获取数据失败，请刷新界面");
						} else {
							var userconfig = rspData.result;
							$('#uname').val(userconfig.username).textinput();
							$('#uweight').val(userconfig.weight).slider('refresh');
							$('#uheight').val(userconfig.height).slider('refresh');
							if (userconfig.gender === 0) {
								$("#male").attr("checked", "checked");
							} else {
								$("#female").attr("checked", "checked");
							}
							var birthDate = new Date(userconfig.birthday);
							birthday = birthDate.getFullYear() + '-' + (birthDate.getMonth() + 1) + "-" + birthDate.getDate();
							$('#ubirthday').val(birthday);
						}
					},
					error: function(xhr, errormsg) {
						util.toast("获取数据失败，请刷新界面");
					}
				});
			}
			getAndSetUserConfigData();

			$("#unregister").off('click');
			$("#unregister").on('click', function(event) {
				util.setCookie('loginid', '');
				util.setCookie('tk', '');
				$.mobile.changePage("/lehealth/login.html", "slide");
			});

			$("#save_userconfig").off('click');
			$("#save_userconfig").on('click', function(event) {
				var loginid = util.getCookieByKey("loginid"),
					token = util.getCookieByKey("tk"),
					username = $('#uname').val(),
					gender = $('#male').attr('checked')? 0 : 1,
					birthday = $('#ubirthday').val(),
					height = $('#uheight').val(),
					weight = $('#uweight').val();
				if(!username || !birthday) {
					util.toast("请填写完整的用户信息");	
					return;
				}
				birthday = new Date(birthday).getTime();

				$.ajax({
					url: userConfigUrl,
					type: "POST",
					dataType: "json",
					async: true,
					data: {
						loginid: loginid,
						token: token,
						username: username,
						gender: gender,
						birthday: birthday,
						height: height,
						weight: weight,
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
					},
					error: function(xhr, errormsg) {
						util.toast("提交数据失败，请重新提交");
					}
				});
			});			

		});
	};

});