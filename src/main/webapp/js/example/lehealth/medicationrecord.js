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
							util.showDialog("请重新登录", "medicationrecord");
							setTimeout(function() {
								$.mobile.changePage("/lehealth/login.html", "slide");
							}, 2000);
							return;
						}
						util.showDialog("获取数据失败，请刷新界面", "medicationrecord");
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
					util.showDialog("获取数据失败，请刷新界面", "medicationrecord");
				}
			});

			function showRecordList(records) {
				var html = "";
				for (var i = 0; i < records.length; i++) {
					var date=new Date(records[i].date);
					html +='<li>'
						+'<p>'+dateFormat(date)+'服用'+records[i].medicinename+records[i].frequency+'次，剂量'+records[i].amount*records[i].frequency+'毫克</p>'
						+'</li>';
					
				}
				// $("#listwraper").html(html);
				// $('#listwraper').listview("refresh");
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