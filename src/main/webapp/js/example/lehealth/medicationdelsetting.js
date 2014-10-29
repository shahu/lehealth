define(function(require, exports, module) {
	
	var $ = require('jquery_mobile');
	var util = require('./common');
	
	var delMedicineConfigUrl = "/lehealth/api/medicinesettingdel.do";

	exports.render = function() {
		$(document).off("pageshow","#medicationdelsetting");
		$(document).on("pageshow","#medicationdelsetting", function() {
			console.info("delsetting pageshow init");
			util.hideAddressBar();
			var username = util.getCookieByKey("loginid");
			var	token = util.getCookieByKey("tk");
			
			var medicinename=util.getParams("medicinename");
			var amount=util.getParams("amount");
			var timing=util.getParams("timing");
			var frequency=util.getParams("frequency");
			var datefrom=util.getParams("datefrom");
			var dateto=util.getParams("dateto");
			$("#medicinename").text(medicinename);
			var plan1='每日'+frequency+'次，每次'+amount+'片';
			var plan2=timing+'服用';
			$("#plan1").text(plan1);
			$("#plan2").text(plan2);
			$("#datefrom").text(datefrom);
			$("#dateto").text(dateto);
			
			$("#config_delete").off('click');
			$("#config_delete").on('click', function(event) {
				var status_update=$("#status_update").val();
				if(status_update==1){
					$("#status_update").val(0);
					console.info(window.location.href);
					var medicineid=util.getParams("medicineid");
					console.info("config_delete init,id="+medicineid);
					$.ajax({
						url: delMedicineConfigUrl,
						type: "POST",
						dataType: "json",
						async: true,
						data: {
							loginid: username,
							token: token,
							medicineid: medicineid
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
								$("#status_update").val(1);
							} else {
								util.toast("删除成功");
								//两秒后隐藏
								setTimeout(function() {
									$.mobile.changePage("/lehealth/medicationconfig.html", "slide");
								}, 1000);
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