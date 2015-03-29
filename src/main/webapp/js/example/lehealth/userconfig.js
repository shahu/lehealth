define(function(require, exports, module) {

	var $ = require('jquery_mobile');
	var util = require('./common');

	var getUserConfigUrl = "/lehealth/api/panient/info";
	var modifyUserConfigUrl = "/lehealth/api/panient/modify";
	
	exports.bindEvent = function() {

		$(document).off("pageshow", "#userconfigpage");

		$(document).on("pageshow", "#userconfigpage", function() {
			$("#userconfigcover").css("display", "none");

			function getAndSetUserConfigData() {
				var username = util.getCookieByKey("loginid"),
					token = util.getCookieByKey("tk");
				$.ajax({
					url: getUserConfigUrl,
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
							$('#uid').html(util.getCookieByKey("loginid"));
							$('#uname').val(userconfig.username).textinput();
							$('#uweight').val(userconfig.weight).slider('refresh');
							$('#uheight').val(userconfig.height).slider('refresh');
							if (userconfig.gender === 0) {
								$("#male").attr("checked", "checked");
							} else {
								$("#female").attr("checked", "checked");
							}
							var birthDate = new Date(userconfig.birthday);
							var month = (birthDate.getMonth() + 1) < 10 ? ("0" + (birthDate.getMonth() + 1)) :(birthDate.getMonth() + 1);
							var dayInMon =  birthDate.getDate() < 10 ? ("0" + birthDate.getDate()) : birthDate.getDate();
							birthday = birthDate.getFullYear() + '-' + month + "-" + dayInMon;
							console.info(birthday);
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
					url: modifyUserConfigUrl,
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
						setTimeout(function() {
							$.mobile.changePage("/lehealth/config.html", "slide");
						}, 2000);
					},
					error: function(xhr, errormsg) {
						util.toast("提交数据失败，请重新提交");
					}
				});
			});			

		});
	};

});