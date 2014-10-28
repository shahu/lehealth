define(function(require, exports, module) {

	var $ = require('jquery_mobile');
	var util = require('./common');

	var getMedicineListUrl = "/lehealth/api/medicines.do";
	var submitMedicineConfigUrl = "/lehealth/api/medicinesetting.do";

	exports.render = function() {
		$(document).bind("pageinit", function() {
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
						util.showDialog("获取数据失败，请刷新界面", "medicationsetting");
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
					util.showDialog("获取数据失败，请刷新界面", "medicationinput");
				}
			});
			
			$("#config_submit").on('click', function(event) {
				var username = util.getCookieByKey("loginid");
				var	token = util.getCookieByKey("tk");
				var	medicineid = $('#medacine').val();
				var	frequency = $("#frequency").val();
				var	amount = $('#amount').val();
				var	timing = $("#timing").val();
				
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
						timing: timing
					},
					success: function(rspData) {
						if (rspData.errorcode) {
							if (rspData.errorcode === 1) {
								util.showDialog("请重新登录", "medicationinput");
								setTimeout(function() {
									$.mobile.changePage("/lehealth/login.html", "slide");
								}, 2000);
								return;
							}
							util.showDialog("提交数据失败，请重新提交", "medicationinput");
						} else {
							util.showDialog("提交成功", "medicationinput");
							//两秒后隐藏
							setTimeout(function() {
								util.dismissDialog("medicationinput");
							}, 2000);
						}
					},
					error: function(xhr, errormsg) {
						util.showDialog("提交数据失败，请重新提交", "medicationinput");
					}
				});
			});
		});
	};

});