define(function(require, exports, module) {
	
	var $ = require('jquery_mobile');
	var util = require('./common');
	
	var getMedicineListUrl = "/lehealth/api/medicines.do";
	var submitMedicineConfigUrl = "/lehealth/api/medicine/setting/modify";

	exports.render = function() {
		$(document).off("pageshow","#medicationsetting");
		$(document).on("pageshow","#medicationsetting", function() {

			$("#medicationsettingcover").css("display", "none");
			$("#status_update").val(1);
			$('#datefrom').val('');
			$('.dosage').val('');
			$(".config").css("display","none");
			$(".config").removeClass("show_config");
			$("#config_1").css("display","block");
			$("#config_1").addClass("show_config");
			$("#config_count").val("1");
			
			var username = util.getCookieByKey("loginid");
			var	token = util.getCookieByKey("tk");
			var houroption='';
			for (var i = 0; i < 24; i++) {
				if(i<10){
					i='0'+i;
				}
				houroption += '<option value="' + i + '">' + i + '</option>';
			}
			var minuteoption='';
			for (var i = 0; i < 4; i++) {
				var j=i*15;
				if(j<10){
					j='0'+j;
				}
				minuteoption += '<option value="' + j + '">' + j + '</option>';
			}
			
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
						
						$('select.time_hour').empty();
						$('select.time_hour').html(houroption);
						$('select.time_hour').selectmenu("refresh");
						$('select.time_minute').empty();
						$('select.time_minute').html(minuteoption);
						$('select.time_minute').selectmenu("refresh");
						
					}
				},
				error: function(xhr, errormsg) {
					util.toast("获取数据失败，请刷新界面");
				}
			});
			
			$(".add_icon").off('click');
			$(".add_icon").on('click', function() {
				var count=$("#config_count").val();
				if(count>10){
					alert('暂不支持更多时间');
					return;
				}
				count++;
				for(var i=2;i<=count;i++){
					$("#config_"+i).css("display","block");
					$("#config_"+i).addClass("show_config");
				}
				$("#config_count").val(count);
			});
			
			$(".del_icon").off('click');
			$(".del_icon").on('click', function() {
				var count=$("#config_count").val();
				count--;
				$("#config_count").val(count);
				$(this).parent().parent().css("display","none");
				$(this).parent().parent().removeClass("show_config");
			});
			
			$("#config_update").off('click');
			$("#config_update").on('click', function(event) {
				var status_update=$("#status_update").val();
				if(status_update==1){
					$("#status_update").val(0);
					if(!($("#datefrom").val()!=0)){
						alert('请选择日期');
						$("#status_update").val(1);
						return;
					}
					var username = util.getCookieByKey("loginid");
					var	token = util.getCookieByKey("tk");
					var	medicineid = $('#medacine').val();
					var	datefromStr = $('#datefrom').val().replace(/-/g,"/");
					var datefrom = new Date(datefromStr);
					var configs=[];
					var flag=1;
					$.each($((".show_config")),function(){
						if($(this).find("input.dosage").val()>0&&$(this).find("span.time_hour").text()!=''&&$(this).find("span.time_minute").text()!=''){
							var config={};
							config.time=$(this).find("select.time_hour").val()+":"+$(this).find("select.time_minute").val();
							config.dosage=$(this).find("input.dosage").val();
							var isExist=0;
							for(var i=0;i<configs.length;i++){
								if(configs[i].time==config.time){
									isExist=1;
								}
							}
							if(isExist==0){
								configs.push(config);
							}else{
								flag=2;
							}
						}else{
							console.info($(this).find("input.dosage").val());
							console.info($(this).find("span.time_hour").text());
							console.info($(this).find("span.time_minute").text());
							flag=0;
						}
					});
					if(flag==0){
						alert('请填写正确的用药计划');
						$("#status_update").val(1);
						return;
					}else if(flag==2){
						alert('请勿填写重复时间的用药计划');
						$("#status_update").val(1);
						return;
					}
					
					$.ajax({
						url: submitMedicineConfigUrl,
						type: "POST",
						dataType: "json",
						async: true,
						data: {
							loginid: username,
							token: token,
							medicineid: medicineid,
							datefrom:datefrom.getTime(),
							configs:JSON.stringify(configs),
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
								$("#status_update").val(1);
								$('#datefrom').val('');
								$('.dosage').val('');
								$(".config").css("display","none");
								$(".config").removeClass("show_config");
								$("#config_1").css("display","block");
								$("#config_1").addClass("show_config");
								$("#config_count").val("1");
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