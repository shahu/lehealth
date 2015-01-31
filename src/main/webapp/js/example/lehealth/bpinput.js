define(function(require, exports, module) {

	var $ = require('jquery_mobile');
	var util = require('./common');

	var submitBpUrl = "/lehealth/api/bp/record/add";

	exports.render = function() {
		// $(document).one("pageshow", function() {
		// 	util.hideAddressBar();
		// });
	};

	exports.bindEvent = function() {

		$.mobile.loading( 'show', {
				text: '页面加载中...',
				textVisible: true,
				theme: 'c',
				html: ''
		});		

		$(document).off("pageshow", "#bpinputpage");

		$(document).on("pageshow", "#bpinputpage", function() {
			console.info('bp input init');

			$("#bpinputcover").css("display", "none");

			$.mobile.loading('hide');

			$('#sbp').on('change', function() {
				var val = $('#sbp').val();
				if(val >= 140 && val < 160) {
					$('#sbp').parent().find('div.ui-btn-active').css('background-color', 'rgb(240, 180, 90)');
				} else if(val >= 160) {
					$('#sbp').parent().find('div.ui-btn-active').css('background-color', 'rgb(220, 60, 70)');
				} else if(val >= 120 && val < 140) {
					$('#sbp').parent().find('div.ui-btn-active').css('background-color', '#38c');	
				} else if(val < 120) {
					$('#sbp').parent().find('div.ui-btn-active').css('background-color', '#38c');		
				}
			});

			$('#dbp').on('change', function() {
				var val = $('#dbp').val();
				if(val >= 90 && val < 100) {
					$('#dbp').parent().find('div.ui-btn-active').css('background-color', 'rgb(240, 180, 90)');
				} else if(val >= 100) {
					$('#dbp').parent().find('div.ui-btn-active').css('background-color', 'rgb(220, 60, 70)');
				} else if(val >= 50 && val < 90) {
					$('#dbp').parent().find('div.ui-btn-active').css('background-color', '#38c');	
				} else if(val < 50) {
					$('#dbp').parent().find('div.ui-btn-active').css('background-color', '#38c');		
				}
			});

			// $('#heartbp').on('change', function() {
			// 	var val = $('#heartbp').val();
			// 	if(val > 80) {
			// 		$('#heartbp').css('color', 'red');
			// 		$('#heartbp').css('font-size', '20px');
			// 	} else {
			// 		$('#heartbp').css('color', 'green');
			// 		$('#heartbp').css('font-size', '14px');						
			// 	}
			// });					

			$("#dp_record_bpdata").off('click');
			$("#dp_record_bpdata").on('click', function do_record_bpdateFn(event) {
				var username = util.getCookieByKey("loginid"),
					token = util.getCookieByKey("tk"),
					sbp = $('#sbp').val(),
					dbp = $('#dbp').val(),
					heartbp = $("#heartbp").val(),
					dosed = $('#dosed').val();
				$.ajax({
					url: submitBpUrl,
					type: "POST",
					dataType: "json",
					async: true,
					data: {
						loginid: username,
						token: token,
						sbp: sbp,
						dbp: dbp,
						heartrate: heartbp,
						dosed: dosed
					},
					success: function(rspData) {
						if (rspData.errorcode) {
							if (rspData.errorcode === 1) {
								util.setCookie("jump", "/lehealth/bpinput.html");
								setTimeout(function() {
									$.mobile.changePage("/lehealth/login.html", "slide");
								}, 2000);
								return;
							}
							util.toast("提交数据失败，请重新提交");
						} else {
							//两秒后隐藏
							setTimeout(function() {
								$.mobile.changePage("/lehealth/bpmonitor.html", "slide");
							}, 2000);
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