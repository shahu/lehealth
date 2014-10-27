define(function(require, exports, module) {

	var $ = require('jquery_mobile');
	var util = require('./common');

	var getMedicineUrl = "/lehealth/api/medicinerecords.do";

	exports.render = function() {
		$(document).bind("pageinit", function() {

			util.hideAddressBar();

			var recordchart,
				judgechart,
				chartcount = 2;

			var username = util.getCookieByKey("loginid"),
				token = util.getCookieByKey("tk");
			
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

			function showRecordChart(records) {
				var seriesData = [],
					tmpMap = {};
				for (var i = 0; i < records.length; i++) {
					if (!tmpMap[records[i].medicinename]) {
						tmpMap[records[i].medicinename] = [];
					}
					var dataArr = tmpMap[records[i].medicinename];
					dataArr.push([
						new Date(records[i].date).getDate(),
						records[i].amount * records.frequency
					]);
				}

				for (var i in tmpMap) {
					seriesData.push({
						name: i,
						color: 'rgba(223, 83, 83, .5)',
						data: tmpMap[i]
					});
				}
			}
		});
	};
});