define(function(require, exports, module) {

	var $ = require('jquery_mobile');
	var util = require('./common');

	var getMedicineUrl = "/lehealth/api/medicine/today/list";

	exports.render = function() {
		$(document).off("pageshow", "#medicationrecord");
		$(document).on("pageshow", "#medicationrecord", function() {
			$("#medicationrecordcover").css("display", "none");
			console.info("medicationrecord pageshow init");
			$(".navigation").show();
			console.info("medicationrecord navigation show");
			util.hideAddressBar();
			console.info("medicationrecord hide show");
			var username = util.getCookieByKey("loginid");
			var	token = util.getCookieByKey("tk");
			console.info("medicationrecord get medicinerecordss ,from "+getMedicineUrl);
			$.ajax({
				url: getMedicineUrl,
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
							console.info("medicationrecord get medicinesettings no login");
							util.toast("请重新登录");
							setTimeout(function() {
								$.mobile.changePage("/lehealth/login.html", "slide");
							}, 2000);
							return;
						}
						console.info("medicationrecord get medicinesettings error,status="+rspData.errorcode);
						util.toast("获取数据失败，请刷新界面");
					} else {
						console.info("medicationrecord get medicinesettings success");
						showRecordList(rspData.result);
					}
				},
				error: function(xhr, errormsg) {
					console.info("medicationrecord get medicinesettings exception");
					util.toast("获取数据失败，请刷新界面");
				}
			});

			function showRecordList(records) {
				console.info("medicationrecord set medicationrecordlistwraper null");
				$("#medicationrecordlistwraper").html('');
				for (var i = 0; i < records.length; i++) {
					var html='<li>'
						+'<div style="line-height: 24px;vertical-align: middle;font-size: 12px;color:#333333;">'
						+records[i].medicinename
						+'</div>'
						+'<ul>';;
					for(var j=0;j<records[i].results.length;j++){
						var addurl='/lehealth/medicationinput.html?a=1'
							+'&medicineid='+records[i].medicineid
							+'&medicinename='+records[i].medicinename
							+'&time='+records[i].results[j].time
							+'&dosage='+records[i].results[j].dosage;
						html+='<li style="line-height: 24px;vertical-align: middle;font-size: 12px;color:#333333;">'+records[i].results[j].time+'服用'+records[i].results[j].dosage+'毫克';
						if(records[i].results[j].status=='0'){
							html+='<a href="'+addurl+'"><span style="float:right">记录服用</span></a>';
						}else{
							html+='<span style="float:right">已服用</span>';
						}
						html+='</li>';
					}
					html+='</ul>'
						+'</li>';
					console.info(html);
					$("#medicationrecordlistwraper").append(html);
				}
				console.info("medicationconfig medicationrecordlistwraper refresh");
				$('#medicationrecordlistwraper').listview("refresh");
			}
		});
	};
});