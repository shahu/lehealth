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
			var html_year = '<option value="0">请选择年份</option>';
			var html_month = '<option value="0">请选择月份</option>';
			var html_day = '<option value="0">请选择日期</option>';
			for (var i = 1; i <= 31; i++) {
				html_year += '<option value="' + (i+2013) + '">' + (i+2013) + '</option>';
				if(i<=12){
					html_month += '<option value="' + i + '">' + i + '</option>';
				}
				html_day += '<option value="' + i + '">' + i + '</option>';
			}
			$('#datefrom_year').empty();
			$('#datefrom_year').html(html_year);
			$('#datefrom_year').selectmenu("refresh");
			$('#datefrom_month').empty();
			$('#datefrom_month').html(html_month);
			$('#datefrom_month').selectmenu("refresh");
			$('#datefrom_day').empty();
			$('#datefrom_day').html(html_day);
			$('#datefrom_day').selectmenu("refresh");
			$('#dateto_year').empty();
			$('#dateto_year').html(html_year);
			$('#dateto_year').selectmenu("refresh");
			$('#dateto_month').empty();
			$('#dateto_month').html(html_month);
			$('#dateto_month').selectmenu("refresh");
			$('#dateto_day').empty();
			$('#dateto_day').html(html_day);
			$('#dateto_day').selectmenu("refresh");
			
			$("#config_update").off('click');
			$("#config_update").on('click', function(event) {
				var status_update=$("#status_update").val();
				if(status_update==1){
					$("#status_update").val(0);
					if(!($('#datefrom_year').val()!=0
							&&$('#datefrom_month').val()!=0
							&&$('#datefrom_day').val()!=0
							&&$('#dateto_year').val()!=0
							&&$('#dateto_month').val()!=0
							&&$('#dateto_day').val()!=0)){
						alert('请选择日期');
						$("#status_update").val(1);
						return;
					}
					console.info("config_update init");
					var username = util.getCookieByKey("loginid");
					var	token = util.getCookieByKey("tk");
					var	medicineid = $('#medacine').val();
					var	frequency = $("#frequency").val();
					var	amount = $('#amount').val();
					var	timing = $("#timing").val();
					var	datefromStr = $('#datefrom_year').val()+'/'+$('#datefrom_month').val()+'/'+$('#datefrom_day').val();
					console.info(datefromStr);
					var datefrom = new Date(datefromStr);
					console.info(datefrom);
					console.info(datefrom.getTime());
					var	datetoStr = $('#dateto_year').val()+'/'+$('#dateto_month').val()+'/'+$('#dateto_day').val();
					console.info(datetoStr);
					var dateto = new Date(datetoStr);
					console.info(dateto);
					console.info(dateto.getTime());
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