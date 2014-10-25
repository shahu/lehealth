define(function(require, exports, module) {

	var $ = require('jquery_mobile');
	var util = require('./common');

	var getDoctorListUrl = "/lehealth/api/doctors.do";

	exports.render = function() {
		$(document).bind("pageinit", function() {
			util.hideAddressBar();

			var username = util.getCookieByKey("loginid"),
				token = util.getCookieByKey("tk");
			$.ajax({
				url: getDoctorListUrl,
				type: "GET",
				dataType: "json",
				async: true,
				success: function(rspData) {
					if (rspData.errorcode) {
						util.showDialog("获取数据失败，请刷新界面", "doctorlist");
					} else {
						var results = rspData.result;
						var html = "";
						for (var i = 0; i < results.length; i++) {
							var doctor = results[i];
							html += 
								'<li>
									<a href="/lehealth/expertdetail.html" id="' + doctor.id + '">
										<img src="images/person.jpg" style="width:80px; height:80px">
										<h2>' + doctor.name + '</h2>
										<p>' + doctor.desc + '</p>
									</a>
								</li>';
						}
						$('#listwraper').empty();
						$('#listwraper').html(html);
						$('#listwraper').listview("refresh");
					}
				},
				error: function(xhr, errormsg) {
					util.showDialog("获取数据失败，请刷新界面", "doctorlist");
				}
			});


		});
	};

});