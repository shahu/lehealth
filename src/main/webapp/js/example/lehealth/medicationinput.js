define(function(require, exports, module) {

	var $ = require('jquery_mobile');
	var util = require('./common');

	var getMedicineListUrl = "/lehealth/api/medicines.do";
	var submitMedicineRecordUrl = "/lehealth/api/medicinerecord.do";

	exports.render = function() {
		$(document).bind("pageshow", function() {
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
						$('#medacine_name').empty();
						$('#medacine_name').html(html);
						$('#medacine_name').selectmenu("refresh");
					}
				},
				error: function(xhr, errormsg) {
					util.toast("获取数据失败，请刷新界面");
				}
			});

		});
	};

	exports.bindEvent = function() {
		$(document).bind("pageshow", function() {
			$("#record_data").on('click', function(event) {
				var username = util.getCookieByKey("loginid"),
					token = util.getCookieByKey("tk"),
					medicineid = $('#medacine_name').val(),
					amount = $('#medacine_amount').val(),
					timing = $("#medacine_time").val(),
					frequency = $("#medacine_count").val();
				$.ajax({
					url: submitMedicineRecordUrl,
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
								util.toast("请重新登录");
								setTimeout(function() {
									$.mobile.changePage("/lehealth/login.html", "slide");
								}, 2000);
								return;
							}
							util.toast("提交数据失败，请重新提交");
						} else {
							util.toast("提交成功");
							//两秒后隐藏
							setTimeout(function() {
								util.dismissDialog("medicationinput");
							}, 2000);
						}
					},
					error: function(xhr, errormsg) {
						util.toast("提交数据失败，请重新提交");
					}
				});
			});
		});
	}

});