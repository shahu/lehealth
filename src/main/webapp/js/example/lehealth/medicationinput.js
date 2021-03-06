define(function(require, exports, module) {

	var $ = require('jquery_mobile');
	var util = require('./common');

	var submitMedicineRecordUrl = "/lehealth/api/medicine/history/add";

	exports.render = function() {
		$(document).off("pageshow","#medicationinput");
		$(document).on("pageshow","#medicationinput", function() {
			$("#medicationinputcover").css("display", "none");

			var username = util.getCookieByKey("loginid");
			var	token = util.getCookieByKey("tk");
			var	medicineid = util.getParams("medicineid");
			var	medicinename = util.getParams("medicinename");
			var time = util.getParams("time");
			var dosage = util.getParams("dosage");
			
			$("#medicinename").text(medicinename);
			$("#time").text(time);
			$("#dosage").text(dosage);
			
			$("#record_update").off('click');
			$("#record_update").on('click', function(event) {
				var status_update=$("#status_update").val();
				if(status_update==1){
					$("#status_update").val(0);
					$.ajax({
						url: submitMedicineRecordUrl,
						type: "POST",
						dataType: "json",
						async: true,
						data: {
							loginid: username,
							token: token,
							medicineid: medicineid,
							time: time,
							dosage: dosage,
							
						},
						success: function(rspData) {
							if (rspData.errorcode) {
								if (rspData.errorcode === 1) {
									util.toast("请重新登录");
									setTimeout(function() {
										$.mobile.changePage("/lehealth/login.html", "slide");
									}, 2000);
									return;
								}
								util.toast("提交数据失败，请重新提交");
								$("#status_update").val(1);
							} else {
								util.toast("提交成功");
								//两秒后隐藏
								setTimeout(function() {
									$.mobile.changePage("/lehealth/medicationrecord.html", "slide");
								}, 2000);
							}
						},
						error: function(xhr, errormsg) {
							util.toast("提交数据失败，请重新提交");
							$("#status_update").val(1);
						}
					});
				}
			});
		});
	};
});