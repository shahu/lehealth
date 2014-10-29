define(function(require, exports, module) {
	
	var $ = require('jquery_mobile');
	var util = require('./common');
	
	var getMedicineListUrl = "/lehealth/api/medicines.do";
	var submitMedicineConfigUrl = "/lehealth/api/medicinesetting.do";

	exports.render = function() {
		$(document).off("pageshow","#medicationsetting");
		$(document).on("pageshow","#medicationsetting", function() {
			console.info("setting pageshow init");
			util.hideAddressBar();
			var username = util.getCookieByKey("loginid");
			var	token = util.getCookieByKey("tk");
			$.ajax({
				url: getMedicineListUrl,
				type: "GET",
				dataType: "json",
				async: true,
				success: function(rspData) {
					if (rspData.errorcode) {
						if (rspData.errorcode === 1) {
							util.toast("请重新登录");
							setTimeout(function() {
								$.mobile.changePage("/lehealth/login.html", "slide");
							}, 1000);
							return;
						}
						util.toast("获取数据失败，请刷新界面");
					} else {
						var results = rspData.result;
						var html = "";
						for (var i = 0; i < results.length; i++) {
							html += '<optgroup label="' + results[i].catename + '">';
							var medicinesInCate = results[i].medicines;
							for (var j = 0; j < medicinesInCate.length; j++) {
								html += '<option value="' + medicinesInCate[j].id + '">' + medicinesInCate[j].name + '</option>';
							}
							html += '</optgroup>';
						}
						$('#medacine').empty();
						$('#medacine').html(html);
						$('#medacine').selectmenu("refresh");
					}
				},
				error: function(xhr, errormsg) {
					util.toast("获取数据失败，请刷新界面");
				}
			});
			
			$("#config_update").off('click');
			$("#config_update").on('click', function(event) {
				var status_update=$("#status_update").val();
				if(status_update==1){
					$("#status_update").val(0);
					console.info("config_update init");
					var username = util.getCookieByKey("loginid");
					var	token = util.getCookieByKey("tk");
					var	medicineid = $('#medacine').val();
					var	frequency = $("#frequency").val();
					var	amount = $('#amount').val();
					var	timing = $("#timing").val();
					var	datefromStr = $('#datefrom').val().replace(/-/g,'/');
					var datefrom = new Date(datefromStr);
					var	datetoStr = $('#dateto').val().replace(/-/g,'/');
					var dateto = new Date(datetoStr);
					$.ajax({
						url: submitMedicineConfigUrl,
						type: "POST",
						dataType: "json",
						async: true,
						data: {
							loginid: username,
							token: token,
							medicineid: medicineid,
							amount: amount,
							frequency: frequency,
							timing: timing,
							datefrom:datefrom.getTime(),
							dateto:dateto.getTime()
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
								util.toast("提交成功");
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