define(function(require, exports, module) {

	var $ = require('jquery_mobile');
	var util = require('./common');

	var getMedicineConfigsUrl = "/lehealth/api/medicinesetting.do";
	
	exports.render = function() {
		$(document).off("pageshow", "#medicationconfig");
		$(document).on("pageshow", "#medicationconfig", function() {
			$('#medicationconfigcover').css("display", "none");

			console.info("medicationconfig pageshow init");
			$(".navigation").show();
			console.info("medicationconfig navigation show");
			util.hideAddressBar();
			console.info("medicationconfig hide show");
			var username = util.getCookieByKey("loginid");
			var	token = util.getCookieByKey("tk");
			console.info("medicationconfig get medicinesettings ,from "+getMedicineConfigsUrl);
			console.info("test0:"+$("#medicationconfiglistwraper").html());
			$.ajax({
				url: getMedicineConfigsUrl,
				type: "GET",
				dataType: "json",
				async: false,
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
						console.info("test1:"+rspData.result);
						showSettingList(rspData.result);
					}
				},
				error: function(xhr, errormsg) {
					console.info("medicationconfig get medicinesettings exception");
					util.toast("获取数据失败，请刷新界面");
				}
			});
			console.info("test7:"+$("#medicationconfiglistwraper").html());

			function showSettingList(settings) {
				console.info("medicationconfig set medicationconfiglistwraper null");
				$("#medicationconfiglistwraper").html('');
				console.info("test2:"+settings);
				for (var i = 0; i < settings.length; i++) {
					console.info("test3:"+settings[i]);
					console.info("test4:"+$("#medicationconfiglistwraper").html());
					var datefrom=new Date(settings[i].datefrom);
					var dateto=new Date(settings[i].dateto);
					var delurl='/lehealth/medicationdelsetting.html?a=1'
						+'&medicineid='+settings[i].medicineid
						+'&medicinename='+settings[i].medicinename
						+'&configs='+encodeURIComponent(JSON.stringify(settings[i].configs))
						+'&datefrom='+dateFormat(datefrom)
						+'&dateto='+dateFormat(dateto);
					var html='<li id="li_'+settings[i].medicineid+'">'
						+'<div style="line-height: 24px;vertical-align: middle;font-size: 12px;color:#333333;">'
						+settings[i].medicinename
						+'<a href="'+delurl+'">'
						+'<span style="float:right">删除</span>'
						+'</a>'
						+'</div>'
						+'<div style="line-height: 24px;vertical-align: middle;font-size: 12px;color:#333333;">'
						+'<ul>';
					for(var j=0;j<settings[i].configs.length;j++){
						html+='<li>每日于'+settings[i].configs[j].time+'服用'+settings[i].configs[j].dosage+'毫克</li>';
					}
					html+='</ul>'
						+'</div>'
						+'<div style="line-height: 24px;vertical-align: middle;font-size: 12px;">'+dateFormat(datefrom)+'&nbsp;~&nbsp;'+dateFormat(dateto)+'</div>'
						+'</li>';
					$("#medicationconfiglistwraper").append(html);
					console.info("test5:"+$("#medicationconfiglistwraper").html());
				}
				console.info("medicationconfig medicationconfiglistwraper refresh");
				$('#medicationconfiglistwraper').listview("refresh");
				console.info("test6:"+$("#medicationconfiglistwraper").html());
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