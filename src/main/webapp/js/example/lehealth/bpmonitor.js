define(function(require, exports, module) {

	var $ = require('jquery_mobile');
	var util = require('./common');
	var highcharts = require('highcharts');

	var getBpRecordUrl = "/lehealth/api/bp/record/list";

	var trendchart;

	function doRequestBpData(days) {
		var username = util.getCookieByKey("loginid"),
			token = util.getCookieByKey("tk");
		$.ajax({
			url: getBpRecordUrl,
			type: "GET",
			dataType: "json",
			async: true,
			data: {
				loginid: username,
				token: token,
				days: days
			},
			success: function(rspData) {
				if (rspData.errorcode) {
					if (rspData.errorcode == 1) { //用户校验失败
						util.setCookie("jump", "/lehealth/bpmonitor.html");
						$.mobile.changePage("/lehealth/login.html", "slide");
						return;
					}

					util.toast("获取数据失败，请刷新界面");
				} else {
					//更新评价分数
					var score = rspData.result.score;
					// var point = judgechart.series[0].points[0];
					// point.update(score);
					//更新评价文案
					var judge = rspData.result.status;
					switch (judge) {
						case 1:
							$("#judge_text").html("偏低");
							break;
						case 2:
							$("#judge_text").html("正常");
							break;
						case 3:
							$("#judge_text").html("偏高");
							break;						
						default:
					}
					//更新趋势图
					var bpDataArr = rspData.result.records || [];

					var hasMedicate = false;
					if(bpDataArr != null && bpDataArr.length > 0) {
						var lastestBpDate = new Date(bpDataArr[bpDataArr.length - 1].date);
						var now = new Date();
						if(now.getFullYear() == lastestBpDate.getFullYear() 
							&& now.getMonth() == lastestBpDate.getMonth()
							&& now.getDate() == lastestBpDate.getDate()) {
							hasMedicate = true;
						}
					}
					if(!hasMedicate) {
						$('#hasmedicate').html('还未');
					} else {
						$('#hasmedicate').html('已经');
					}					

					if(bpDataArr.length == 1) {
						var bpData = bpDataArr[0];
						var tmp = {};
						for(var i in bpData) {
							tmp[i] = bpData[i];
						}
						tmp.date += 1000 * 3600;
						bpDataArr.push(tmp);
					}
					console.info(bpDataArr);
					var now = new Date().getTime();
					for(var i = 0; i < days; i++) {
						bpDataArr.push({
							date: now - (i * 24 * 3600 * 1000),
							dbp: null,
							sbp: null
						});
					}

					var ranges = [];
					var	averages = [];
					
					for (var i = 0; i < bpDataArr.length; i++) {
						var bpobj = bpDataArr[i];
						ranges.push([bpobj.date,bpobj.dbp,bpobj.sbp]);
						averages.push([bpobj.date,bpobj.heartrate]);
					}
					console.info(ranges);
					console.info(averages);
					
					var chartwidth = bpDataArr.length * 45 > screen.width ? bpDataArr.length * 45 : screen.width;
					$('#trendchart').css('width', chartwidth+ 'px');
					showCharts(averages,ranges);
				}
			},
			error: function(xhr, errormsg) {
				util.toast("获取数据失败，请刷新界面");
			}
		});

	}	

	function showCharts(averages,ranges) {
		$('#trendchart').empty();
		//渲染血压趋势图
		var gridTheme = require('highcharts_theme').getGridThemeOption();
		highcharts.setOptions(gridTheme);
		$('#trendchart').highcharts({
			title: {
				text: ''
			},
			subtitle: {
				text: ''
			},
			xAxis: {
				type: 'datetime'
			},
			yAxis: [{
				title: {
					text: '次',
					margin:0
				},
				lineWidth : 1,
			},{
				title: {
					text: 'mmHg',
					margin: 0
				},
				lineWidth : 1,
				opposite:true
			}],
			legend: {},
			series: [{
				name: '血压',
				data: ranges,
				type: 'arearange',
				lineWidth: 0,
				linkedTo: ':previous',
				color: Highcharts.getOptions().colors[0],
				fillOpacity: 0.3,
				zIndex: 0,
	            yAxis:0,
			},{
				name: '心率',
				data: averages,
				zIndex: 1,
				marker: {
					fillColor: 'white',
					lineWidth: 2,
					lineColor: Highcharts.getOptions().colors[0]
				},
	            yAxis:1,
			}],
			credits: {
				enabled: false
			}
		});
	}

	exports.render = function() {
		$.mobile.loading( 'show', {
				text: '页面加载中...',
				textVisible: true,
				theme: 'c',
				html: ''
		});
		$(document).off("pageshow", "#bpmonitor");
		$(document).on("pageshow", "#bpmonitor", function() {
			console.info('bpmonitor init');
			$("#bpmonitorcover").css("display", "none");
			$.mobile.loading('hide');
			$('#sevendaybtn').off("click");
			$('#sevendaybtn').on('click', function() {
				$('#halfmonbtn').removeClass('ui-btn-active');
				$('#sevendaybtn').addClass('ui-btn-active');
				$('#monbtn').removeClass('ui-btn-active');					
				doRequestBpData(7);
			});
			$('#monbtn').off("click");
			$('#monbtn').on('click', function() {
				$('#halfmonbtn').removeClass('ui-btn-active');
				$('#sevendaybtn').removeClass('ui-btn-active');
				$('#monbtn').addClass('ui-btn-active');				
				doRequestBpData(30);
			});			
			$('#halfmonbtn').off("click");
			$('#halfmonbtn').on('click', function() {
				$('#halfmonbtn').addClass('ui-btn-active');
				$('#sevendaybtn').removeClass('ui-btn-active');
				$('#monbtn').removeClass('ui-btn-active');
				doRequestBpData(15);
			});	
			$('#sevendaybtn').addClass('ui-btn-active');
			$('#sevendaybtn').click();	
		});
	};
});