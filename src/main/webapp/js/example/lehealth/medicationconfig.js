define(function(require, exports, module) {

	var $ = require('jquery_mobile');
	var util = require('./common');

	var getMedicineConfigsUrl = "/lehealth/api/medicinesetting.do";
	var delMedicineConfigUrl = "/lehealth/api/medicinesettingdel.do";
	
	exports.render = function() {
		$(document).off("pageshow", "#medicationconfig");
		$(document).on("pageshow", "#medicationconfig", function() {
			console.info("config pageshow init");
			util.hideAddressBar();
			var username = util.getCookieByKey("loginid");
			var	token = util.getCookieByKey("tk");
			$.ajax({
				url: getMedicineConfigsUrl,
				type: "GET",
				dataType: "json",
				async: true,
				data: {
					loginid: username,
					token: token
				},
				success: function(rspData) {
					if (rspData.errorcode) {
						if (rspData.errorcode == 1) { //用户校验失败
							util.showDialog("请重新登录", "medicationconfig");
							setTimeout(function() {
								$.mobile.changePage("/lehealth/login.html", "slide");
							}, 2000);
							return;
						}
						util.showDialog("获取数据失败，请刷新界面", "medicationconfig");
					} else {
						showSettingList(rspData.result);
					}
				},
				error: function(xhr, errormsg) {
					util.showDialog("获取数据失败，请刷新界面", "medicationconfig");
				}
			});

			function showSettingList(settings) {
				$("#listwraper").html('');
				for (var i = 0; i < settings.length; i++) {
					var datefrom=new Date(settings[i].datefrom);
					var dateto=new Date(settings[i].dateto);
					var timing='饭前';
					if(settings[i].timing==2){
						timing='饭间';
					}else if(settings[i].timing==3){
						timing='饭后';
					}
					var html='<li>'
						+'<div style="line-height: 48px;vertical-align: middle;font-size: 16px;">'+settings[i].medicinename+'</div>'
						+'<div style="line-height: 48px;vertical-align: middle;font-size: 14px;">每日'+settings[i].frequency+'次，每次'+settings[i].amount+'片（粒），'+timing+'服用<img class="del" id="del_'+settings[i].medicineid+'" src="images/del.jpg" style="float:right"></div>'
						+'<div style="line-height: 48px;vertical-align: middle;font-size: 12px;color:#333333;">'+dateFormat(datefrom)+'&nbsp;~&nbsp;'+dateFormat(dateto)+'</div>'
						+'</li>';
					$("#listwraper").append(html);
				}
				$('#listwraper').listview("refresh");
			}
			
			function dateFormat(date){
				if(date){
					var year=date.getFullYear();
					var month=date.getMonth()+1;
					if(month<=9){
						month="0"+month;
					}
					var day=date.getDate();
					if(day<=9){
						day="0"+day;
					}
					return year+"年"+month+"月"+day+"日";
				}else{
					return "";
				}
			}
			
			//TODO 删除用药
			$(".del").off('click');
			$(".del").on('click', function(event) {
				var username = util.getCookieByKey("loginid");
				var	token = util.getCookieByKey("tk");
				var	medicineid = $(this).attr("id").split("_")[1];
				$.ajax({
					url: delMedicineConfigUrl,
					type: "GET",
					dataType: "json",
					async: true,
					data: {
						loginid: username,
						token: token,
						medicineid: medicineid,
					},
					success: function(rspData) {
						if (rspData.errorcode) {
							if (rspData.errorcode === 1) {
								util.showDialog("请重新登录", "medicationsetting");
								setTimeout(function() {
									$.mobile.changePage("/lehealth/login.html", "slide");
								}, 1000);
								return;
							}
							util.showDialog("提交数据失败，请重新提交", "medicationsetting");
						} else {
							util.showDialog("提交成功", "medicationsetting");
							//两秒后隐藏
							setTimeout(function() {
								$.mobile.changePage("/lehealth/medicationconfig.html", "slide");
							}, 1000);
						}
					},
					error: function(xhr, errormsg) {
						util.showDialog("提交数据失败，请重新提交", "medicationsetting");
					}
				});
			});
		});
	};
});