define(function(require, exports, module) {

	var $ = require('jquery_mobile');
	var util = require('./common');

	var getMedicineConfigsUrl = "/lehealth/api/medicinesetting.do";
	
	exports.render = function() {
		$(document).off("pageshow", "#medicationconfig");
		$(document).on("pageshow", "#medicationconfig", function() {
			console.info("medicationconfig pageshow init");
			$(".navigation").show();
			console.info("medicationconfig navigation show");
			util.hideAddressBar();
			console.info("medicationconfig hide show");
			var username = util.getCookieByKey("loginid");
			var	token = util.getCookieByKey("tk");
			console.info("medicationconfig get medicinesettings ,from "+getMedicineConfigsUrl);
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
							console.info("medicationconfig get medicinesettings no login");
							util.toast("请重新登录");
							setTimeout(function() {
								$.mobile.changePage("/lehealth/login.html", "slide");
							}, 2000);
							return;
						}
						console.info("medicationconfig get medicinesettings error,status="+rspData.errorcode);
						util.toast("获取数据失败，请刷新界面");
					} else {
						console.info("medicationconfig get medicinesettings success");
						showSettingList(rspData.result);
					}
				},
				error: function(xhr, errormsg) {
					console.info("medicationconfig get medicinesettings exception");
					util.toast("获取数据失败，请刷新界面");
				}
			});

			function showSettingList(settings) {
				console.info("medicationconfig set listwraper null");
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
				console.info("medicationconfig listwraper refresh");
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