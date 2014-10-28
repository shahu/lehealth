define(function(require, exports, module) {

	var $ = require('jquery_mobile');
	var util = require('./common');

	var getMedicineUrl = "/lehealth/api/medicinerecords.do";

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
						var judge = rspData.result.status;
						switch (judge) {
							case "1":
								$("#judge_text").html("规律");
								break;
							case "2":
								$("#judge_text").html("不规律");
								break;
							default:
						}
						showRecordList(rspData.result.records);
					}
				},
				error: function(xhr, errormsg) {
					util.toast("获取数据失败，请刷新界面");
				}
			});

			function showRecordList(records) {
				$("#listwraper").append('');
				for (var i = 0; i < records.length; i++) {
					var date=new Date(records[i].date);
					var html='<li>'
						+'<div style="line-height: 24px;vertical-align: middle;font-size: 14px;">'+dateFormat(date)+'</div>'
						+'<div style="line-height: 24px;vertical-align: middle;font-size: 12px;color:#333333;">应服用'+records[i].medicinename+records[i].frequency+'次，每次'+records[i].amount+'片（粒）<img class="eat" id="eat_'+records[i].medicineid+'" src="images/medication.png" style="float:right"></div>'
						+'<div style="line-height: 24px;vertical-align: middle;font-size: 12px;">已服用'+records[i].medicinename+records[i].frequency+'次，共'+records[i].frequency*records[i].amount+'片（粒）</div>'
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