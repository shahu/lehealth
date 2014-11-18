define(function(require, exports, module) {
	
	var $ = require('jquery_mobile');
	var util = require('./common');
	
	var delMedicineConfigUrl = "/lehealth/api/medicinesettingdel.do";

	exports.render = function() {
		$(document).off("pageshow","#medicationdelsetting");
		$(document).on("pageshow","#medicationdelsetting", function() {
			
			$('#medicationdelsettingcover').css("display", "none");

			var username = util.getCookieByKey("loginid");
			var	token = util.getCookieByKey("tk");
			
			var medicinename=util.getParams("medicinename");
			$("#medicinename").text(medicinename);
			
			var configs=JSON.parse(decodeURIComponent(util.getParams("configs")));
			var plan='<ul>';
			for(var i=0;i<configs.length;i++){
				plan+='<li>'
					+'每天于'+configs[i].time
					+'服用'+configs[i].dosage
					+'毫克'
					+'</li>';
			}
			plan+="</ul>";
			$("#plan").append(plan);
			
			var datefrom=util.getParams("datefrom");
			var dateto=util.getParams("dateto");
			$("#datefrom").text(datefrom);
			$("#dateto").text(dateto);
			
			$("#config_delete").off('click');
			$("#config_delete").on('click', function(event) {
				var status_update=$("#status_update").val();
				if(status_update==1){
					$("#status_update").val(0);
					var medicineid=util.getParams("medicineid");
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