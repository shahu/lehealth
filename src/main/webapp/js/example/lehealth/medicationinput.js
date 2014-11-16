define(function(require, exports, module) {

	var $ = require('jquery_mobile');
	var util = require('./common');

	var submitMedicineRecordUrl = "/lehealth/api/medicinehistory.do";

	exports.render = function() {
		$(document).off("pageshow","#medicationinput");
		$(document).on("pageshow","#medicationinput", function() {
			$("#medicationinputcover").css("display", "none");

			var username = util.getCookieByKey("loginid");
			var	token = util.getCookieByKey("tk");
			var	medicineid = util.getParams("medicineid");
			var	medicinename = util.getParams("medicinename");
			var amount = util.getParams("amount");
			var timing = util.getParams("timing");
			var frequency = util.getParams("frequency");
			var medicineamount = util.getParams("medicineamount");
			
			$("#medicinename").text(medicinename);
			var plan1='每日'+frequency+'次，每次'+amount+'片';
			var plan2=timing+'服用';
			$("#plan1").text(plan1);
			$("#plan2").text(plan2);
			if(!medicineamount){
				medicineamount=0;
			}
			$("#amount").text(medicineamount+"次");
			
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