define(function(require, exports, module) {

	var $ = require('jquery_mobile');
	var util = require('./common');

	var getMedicineUrl = "/api/medicinerecords";

	exports.render = function() {
		$(document).bind("pageinit", function() {

			util.hideAddressBar();

			var recordchart,
				judgechart,
				chartcount = 2;

			//渲染图表原始界面,先填充默认数据，然后再通过网络请求填充真实数据
			var highcharts = require('highcharts');
			//渲染用药记录图
			var gridTheme = require('highcharts_theme').getGridThemeOption();
			highcharts.setOptions(gridTheme);

			var username = util.getCookieByKey("loginid"),
				token = util.getCookieByKey("tk");
			$.ajax({
				url: getMedicineUrl,
				type: "GET",
				dataType: "json",
				async: true,
				data: {
					loginid: username,
					token: token
				},
				success: function(rspData) {
					if (rspData.errorcode) {
						if (rspData.errorcode == 1) { //用户校验失败
							util.showDialog("请重新登录", "medicationrecord");
							setTimeout(function() {
								$.mobile.changePage("/lehealth/login.html", "slide");
							}, 2000);
							return;
						}

						util.showDialog("获取数据失败，请刷新界面", "medicationrecord");
					} else {

						//更新评价文案
						var judge = rspData.result.status;
						switch (judge) {
							case "1":
								$("#judge_text").html("规律");
								break;
							case "2":
								$("#judge_text").html("不规律");
								break;
							default:
						}
						showRecordChart(rspData.result.records);
						showJudgeChart(rspData.result.score);
					}
				},
				error: function(xhr, errormsg) {
					util.showDialog("获取数据失败，请刷新界面", "medicationrecord");
				}
			});

			function showRecordChart(records) {
				var seriesData = [],
					tmpMap = {};
				for (var i = 0; i < records.length; i++) {
					if (!tmpMap[records[i].medicinename]) {
						tmpMap[records[i].medicinename] = [];
					}
					var dataArr = tmpMap[records[i].medicinename];
					dataArr.push([
						new Date(records[i].date).getDate(),
						records[i].amount * records.frequency
					]);
				}

				for (var i in tmpMap) {
					seriesData.push({
						name: i,
						color: 'rgba(223, 83, 83, .5)',
						data: tmpMap[i]
					});
				}


				$('#recordchart').highcharts({
					chart: {
						type: 'scatter',
						zoomType: 'xy',
						backgroundColor: '#f9f9f9'
					},
					title: {
						text: ''
					},
					subtitle: {
						text: ''
					},
					xAxis: {
						title: {
							enabled: true,
							text: ''
						},
						startOnTick: true,
						endOnTick: true,
						showLastLabel: true
					},
					yAxis: {
						title: {
							text: ''
						}
					},
					// legend: {                                                                            
					//     layout: 'vertical',                                                              
					//     align: 'left',                                                                   
					//     verticalAlign: 'top',                                                            
					//     x: 100,                                                                          
					//     y: 70,                                                                           
					//     floating: true,                                                                  
					//     backgroundColor: '#FFFFFF',                                                      
					//     borderWidth: 1                                                                   
					// },                                                                                   
					plotOptions: {
						scatter: {
							marker: {
								radius: 5,
								states: {
									hover: {
										enabled: true,
										lineColor: 'rgb(100,100,100)'
									}
								}
							},
							states: {
								hover: {
									marker: {
										enabled: false
									}
								}
							},
							tooltip: {
								headerFormat: '<b>{series.name}</b><br>',
								pointFormat: '{point.x} 日, {point.y} 用量'
							}
						}
					},
					series: seriesData,
					credits: {
						enabled: false
					}
				});
			}

			function showJudgeChart(score) {
				//渲染系统评价图
				$('#judgechartcontainer').highcharts({
					chart: {
						type: 'gauge',
						plotBackgroundColor: null,
						plotBackgroundImage: null,
						plotBorderWidth: 0,
						plotShadow: false,
						backgroundColor: '#f9f9f9'
					},
					title: {
						text: ''
					},
					pane: {
						startAngle: -150,
						endAngle: 150,
						background: [{
							backgroundColor: {
								linearGradient: {
									x1: 0,
									y1: 0,
									x2: 0,
									y2: 1
								},
								stops: [
									[0, '#FFF'],
									[1, '#333']
								]
							},
							borderWidth: 0,
							outerRadius: '109%'
						}, {
							backgroundColor: {
								linearGradient: {
									x1: 0,
									y1: 0,
									x2: 0,
									y2: 1
								},
								stops: [
									[0, '#333'],
									[1, '#FFF']
								]
							},
							borderWidth: 1,
							outerRadius: '107%'
						}, {
							// default background
						}, {
							backgroundColor: '#DDD',
							borderWidth: 0,
							outerRadius: '105%',
							innerRadius: '103%'
						}],
						credits: {
							enabled: false
						}
					},
					// the value axis
					yAxis: {
						min: 0,
						max: 100,

						minorTickInterval: 'auto',
						minorTickWidth: 1,
						minorTickLength: 10,
						minorTickPosition: 'inside',
						minorTickColor: '#666',

						tickPixelInterval: 30,
						tickWidth: 2,
						tickPosition: 'inside',
						tickLength: 10,
						tickColor: '#666',
						labels: {
							step: 2,
							rotation: 'auto'
						},
						title: {
							text: '分'
						},
						plotBands: [{
							from: 0,
							to: 60,
							color: '#DF5353' // green
						}, {
							from: 60,
							to: 80,
							color: '#DDDF0D' // yellow
						}, {
							from: 80,
							to: 100,
							color: '#55BF3B' // red
						}]
					},

					series: [{
						name: '系统评估',
						data: [0],
						tooltip: {
							valueSuffix: ' 分'
						}
					}],
					credits: {
						enabled: false
					}
				}, function(chart) {
					//get data from server
					var point = chart.series[0].points[0];
					point.update(score);

				});
			}

		});

	};

});