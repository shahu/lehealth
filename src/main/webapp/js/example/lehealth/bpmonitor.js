define(function(require, exports, module) {

	var $ = require('jquery_mobile');
	var util = require('./common');
	var highcharts = require('highcharts');

	var getBpRecordUrl = "/lehealth/api/bprecords.do";

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
					var bpDataArr = rspData.result.records;

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


					var xAxisArr = [],
						dbpArr = [],
						sbpArr = [],
						rateArr = [];

					var chartwidth = bpDataArr.length * 45 > screen.width ? bpDataArr.length * 45 : screen.width;
					$('#trendchart').css('width', chartwidth+ 'px');
					showCharts(days, function(trendchart) {
						for (var i = 0; i < bpDataArr.length; i++) {
							var bpobj = bpDataArr[i];
							xAxisArr.push((new Date(bpobj.date)).getDate() + '日');
							dbpArr.push(bpobj.dbp);
							sbpArr.push(bpobj.sbp);
							rateArr.push(bpobj.heartrate);
						}						
						trendchart.xAxis[0].setCategories(xAxisArr);
						trendchart.series[0].setData(dbpArr);
						trendchart.series[1].setData(sbpArr);
						trendchart.series[2].setData(rateArr);						
					});
				}
			},
			error: function(xhr, errormsg) {
				util.toast("获取数据失败，请刷新界面");
			}
		});

	}	

	function showCharts(days, cb) {
			
		$('#trendchart').empty();
		//渲染血压趋势图
		var gridTheme = require('highcharts_theme').getGridThemeOption();
		highcharts.setOptions(gridTheme);
		$('#trendchart').highcharts({
			chart: {
				type: 'line',
				backgroundColor: '#f9f9f9'
			},
			title: {
				text: ''
			},
			subtitle: {
				text: ''
			},
			xAxis: {
				categories: ['1日', '2日', '3日', '4日', '5日', '6日', '7日']
			},
			yAxis: [{
				title: {
					text: 'mmHg',
					margin: 0
				},
				lineWidth : 1
			},
			{
				title: {
					text: '次',
					margin:0
				},
				lineWidth : 1,
				opposite:true
			}],
			tooltip: {
				enabled: true,
				formatter: function() {
					return '<b>' + this.series.name + '</b><br>' + this.x + ': ' + this.y;
				}
			},
			plotOptions: {
				line: {
					dataLabels: {
						enabled: true
					},
					enableMouseTracking: false
				}
			},
			//default data
			series: [{
				name: '舒张压',
				data: [90, 90, 90, 90, 90, 90, 90],
				yAxis:0
			}, {
				name: '收缩压',
				data: [120, 120, 120, 120, 120, 120, 120],
				yAxis:0
			}, {
				name: '心率',
				data: [80, 80, 80, 80, 80, 80, 80],
				yAxis:1
			}],
			credits: {
				enabled: false
			}
		}, function(chart) {
			trendchart = chart;
			cb(chart);
			// doRequestBpData(days);
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