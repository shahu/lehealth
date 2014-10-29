define(function(require, exports, module) {
	
	var $ = require('jquery_mobile');
	var util = require('./common');
	
	var delMedicineConfigUrl = "/lehealth/api/medicinesettingdel.do";

	exports.render = function() {
		$(document).off("pageshow","#medicinesettingdel");
		$(document).bind("pageshow","#medicinesettingdel", function() {
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
			$("#amount").text(amount);
			$("#timing").text(timing);
			$("#frequency").text(frequency);
			$("#datefrom").text(datefrom);
			$("#dateto").text(dateto);
			
			$("#config_delete").off('click');
			$("#config_delete").on('click', function(event) {
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
					}
				});
			});
		});
	};
});