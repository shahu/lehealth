define(function(require, exports, module) {

	var $ = require('jquery_mobile');
	var util = require('./common');

	var getMedicineUrl = "/lehealth/api/medicinehistory.do";

	exports.render = function() {
		$(document).bind("pageshow", function() {
			util.hideAddressBar();

			var username = util.getCookieByKey("loginid");
			var	token = util.getCookieByKey("tk");
			
			$.ajax({
				url: getMedicineUrl,
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
						showRecordList(rspData.result);
					}
				},
				error: function(xhr, errormsg) {
					util.toast("获取数据失败，请刷新界面");
				}
			});

			function showRecordList(records) {
				$("#listwraper").append('');
				for (var i = 0; i < records.length; i++) {
					var timing='饭前';
					if(records[i].timing==2){
						timing='饭间';
					}else if(records[i].timing==3){
						timing='饭后';
					}
					var addurl='/lehealth/medicationinput.html?a=1'
						+'&medicineid='+records[i].medicineid
						+'&medicinename='+records[i].medicinename
						+'&amount='+records[i].amount
						+'&timing='+timing
						+'&frequency='+records[i].frequency
						+'&medicineamount='+records[i].medicineamount;
					var html='<li>'
						+'<div style="line-height: 24px;vertical-align: middle;font-size: 12px;color:#333333;">应服用'+records[i].medicinename+records[i].frequency+'次，每次'+timing+'服药'+records[i].amount+'片（粒）<a href="'+addurl+'"><img src="images/medication.png" style="float:right"></a></div>'
						+'<div style="line-height: 24px;vertical-align: middle;font-size: 12px;">已服用'+records[i].medicinename+records[i].medicineamount+'次</div>'
						+'</li>';
					$("#listwraper").append(html);
				}
				$('#listwraper').listview("refresh");
			}
			
		});
	};
});