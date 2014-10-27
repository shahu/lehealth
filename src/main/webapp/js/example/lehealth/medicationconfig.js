define(function(require, exports, module) {

	var $ = require('jquery_mobile');
	var util = require('./common');

	var medicineConfigUrl = "/lehealth/api/medicinesetting.do";

	exports.render = function() {
		$(document).bind("pageinit", function() {
			util.hideAddressBar();

			var username = util.getCookieByKey("loginid");
			var	token = util.getCookieByKey("tk");
			
			$.ajax({
				url: medicineConfigUrl,
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
							util.showDialog("请重新登录", "medicationrecord");
							setTimeout(function() {
								$.mobile.changePage("/lehealth/login.html", "slide");
							}, 2000);
							return;
						}
						util.showDialog("获取数据失败，请刷新界面", "medicationrecord");
					} else {
						showSettingList(rspData.result);
					}
				},
				error: function(xhr, errormsg) {
					util.showDialog("获取数据失败，请刷新界面", "medicationrecord");
				}
			});

			function showSettingList(settings) {
				for (var i = 0; i < settings.length; i++) {
					var datefrom=new Date(settings[i].datefrom);
					var dateto=new Date(settings[i].dateto);
					var timing='餐前';
					if(settings[i].timing==2){
						timing='餐时';
					}else if(settings[i].timing==3){
						timing='餐后';
					}
					var html='<li>'
						+'<div style="line-height: 48px;vertical-align: middle;font-size: 16px;">'+settings[i].medicinename+'</div>'
						+'<div style="line-height: 48px;vertical-align: middle;font-size: 14px;">每日'+settings[i].frequency+'次，每次'+settings[i].amount+'片（粒），'+timing+'服用<img src="images/del.jpg" style="float:right"></div>'
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
		});
	};
});