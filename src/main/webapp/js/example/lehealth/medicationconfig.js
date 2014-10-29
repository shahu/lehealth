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
							util.toast("请重新登录");
							setTimeout(function() {
								$.mobile.changePage("/lehealth/login.html", "slide");
							}, 2000);
							return;
						}
						util.toast("获取数据失败，请刷新界面");
					} else {
						showSettingList(rspData.result);
					}
				},
				error: function(xhr, errormsg) {
					util.toast("获取数据失败，请刷新界面");
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
					var delurl='/lehealth/medicationdelsetting.html?a=1'
						+'&medicineid='+settings[i].medicineid
						+'&medicinename='+settings[i].medicinename
						+'&amount='+settings[i].amount
						+'&timing='+timing
						+'&frequency='+settings[i].frequency
						+'&datefrom='+dateFormat(datefrom)
						+'&dateto='+dateFormat(dateto);
					var html='<li id="li_'+settings[i].medicineid+'">'
						+'<div style="line-height: 24px;vertical-align: middle;font-size: 12px;color:#333333;">'+settings[i].medicinename+'</div>'
						+'<div style="line-height: 24px;vertical-align: middle;font-size: 12px;color:#333333;">每日'+settings[i].frequency+'次，每次'+settings[i].amount+'片，'+timing+'服用<a href="'+delurl+'"><img src="images/del.png" style="float:right"></a></div>'
						+'<div style="line-height: 24px;vertical-align: middle;font-size: 12px;">'+dateFormat(datefrom)+'&nbsp;~&nbsp;'+dateFormat(dateto)+'</div>'
						+'</li>';
					$("#listwraper").append(html);
				}
				$('#listwraper').listview("refresh");
				$(".del").off('click');
				$(".del").on('click', function(event) {
					var username = util.getCookieByKey("loginid");
					var	token = util.getCookieByKey("tk");
					var	medicineid = $(this).attr("id").split("_")[1];
					$.ajax({
						url: delMedicineConfigUrl,
						type: "POST",
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
									util.toast("请重新登录");
									setTimeout(function() {
										$.mobile.changePage("/lehealth/login.html", "slide");
									}, 1000);
									return;
								}
								util.toast("提交数据失败，请重新提交");
							} else {
								util.toast("删除成功");
								//两秒后隐藏
								setTimeout(function() {
									$.mobile.changePage("/lehealth/medicationconfig.html", "slide");
								}, 1000);
								$("#li_"+medicineid).remove();
							}
						},
						error: function(xhr, errormsg) {
							util.toast("提交数据失败，请重新提交");
						}
					});
				});
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