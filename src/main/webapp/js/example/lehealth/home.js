define(function(require, exports, module) {

	var $ = require('jquery_mobile');
	var util = require('./common');

	var getBpRecordUrl = "/lehealth/api/homedata.do";

	//根据旋转的角度获取评分指示原点的圆心
	function getPoitorXY(degree, radius, x, y) {
		var _x = x + radius * Math.cos(2 * Math.PI * degree / 360);
		var _y = y + radius * Math.sin(2 * Math.PI * degree / 360);
		return {
			x: _x,
			y: _y
		};
	}

	exports.render = function() {

		$.mobile.loading( 'show', {
				text: '页面加载中...',
				textVisible: true,
				theme: 'c',
				html: ''
		});

		$(document).off("pageshow", "#homepage");

		$(document).on("pageshow", "#homepage", function() {			
			

			console.info('home init');

			$("#homepagecover").css("display", "none");

			$.mobile.loading('hide');

			$('#todayDate').empty();
			var today = new Date();
			today = today.getFullYear() + '-' + (today.getMonth() + 1) + "-" + today.getDate();			
			$('#todayDate').html(today);

			var trendchart,
				// judgechart,
				chartcount = 1;

			//渲染图表原始界面,先填充默认数据，然后再通过网络请求填充真实数据
			var highcharts = require('highcharts');
			//渲染血压趋势图
			var gridTheme = require('highcharts_theme').getGridThemeOption();
			highcharts.setOptions(gridTheme);
			$('#medicationInfo').highcharts({
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
				yAxis: {
					title: {
						text: ''
					}
				},
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
					data: [90, 90, 90, 90, 90, 90, 90]
				}, {
					name: '收缩压',
					data: [120, 120, 120, 120, 120, 120, 120]
				}, {
					name: '心率',
					data: [80, 80, 80, 80, 80, 80, 80]
				}],
				credits: {
					enabled: false
				}
			}, function(chart) {
				trendchart = chart;
				chartcount--;
				if (chartcount == 0) {
					doRequestBpData();
				}
			});
			
			var monitorStage = new Kinetic.Stage({
				container: "healthInfo",
				width: document.getElementById("healthInfo").offsetWidth,
				height: 220
			});

			var layer = new Kinetic.Layer();
			var arc1 = new Kinetic.Arc({
				x: monitorStage.getWidth()/2,
				y: monitorStage.getHeight()/2,
				innerRadius: monitorStage.getHeight()/2 - 16,
				outerRadius: monitorStage.getHeight()/2,
				fill: 'rgb(60, 130, 220)',
				angle: 60,
				rotationDeg: 110
			});
			var arc2 = new Kinetic.Arc({
				x: monitorStage.getWidth()/2,
				y: monitorStage.getHeight()/2,
				innerRadius: monitorStage.getHeight()/2 - 16,
				outerRadius: monitorStage.getHeight()/2,
				fill: 'rgb(120, 210, 90)',
				angle: 70,
				rotationDeg: 160
			});
			var arc3 = new Kinetic.Arc({
				x: monitorStage.getWidth()/2,
				y: monitorStage.getHeight()/2,
				innerRadius: monitorStage.getHeight()/2 - 16,
				outerRadius: monitorStage.getHeight()/2,
				fill: 'rgb(90, 200, 70)',
				angle: 50,
				rotationDeg: 230
			});
			var arc4 = new Kinetic.Arc({
				x: monitorStage.getWidth()/2,
				y: monitorStage.getHeight()/2,
				innerRadius: monitorStage.getHeight()/2 - 16,
				outerRadius: monitorStage.getHeight()/2,
				fill: 'rgb(240, 180, 90)',
				angle: 40,
				rotationDeg: 280
			});							
			var arc5 = new Kinetic.Arc({
				x: monitorStage.getWidth()/2,
				y: monitorStage.getHeight()/2,
				innerRadius: monitorStage.getHeight()/2 - 16,
				outerRadius: monitorStage.getHeight()/2,
				fill: 'rgb(230, 90, 90)',
				angle: 60,
				rotationDeg: 320
			});
			var arc6 = new Kinetic.Arc({
				x: monitorStage.getWidth()/2,
				y: monitorStage.getHeight()/2,
				innerRadius: monitorStage.getHeight()/2 - 16,
				outerRadius: monitorStage.getHeight()/2,
				fill: 'rgb(220, 60, 70)',
				angle: 50,
				rotationDeg: 20
			});


			var degree = -10;//just for test
			var poitorXY = getPoitorXY(degree, monitorStage.getHeight()/2 - 26, 
				monitorStage.getWidth()/2, monitorStage.getHeight()/2);

			var begin = getPoitorXY(70, monitorStage.getHeight()/2 - 8, 
				monitorStage.getWidth()/2, monitorStage.getHeight()/2);			
			var end = getPoitorXY(110, monitorStage.getHeight()/2 - 8, 
				monitorStage.getWidth()/2, monitorStage.getHeight()/2);				

			var circle = new Kinetic.Circle({
				x: poitorXY.x,
				y: poitorXY.y,
				radius: 10,
				fill: 'rgb(120, 210, 90)'
			});
			var circle2 = new Kinetic.Circle({
				x: begin.x,
				y: begin.y,
				radius: 8,
				fill: 'rgb(220, 60, 70)'
			});			
			var circle3 = new Kinetic.Circle({
				x: end.x,
				y: end.y,
				radius: 8,
				fill: 'rgb(60, 130, 220)'
			});

			var heartrate = new Kinetic.Text({
				  x: 0,
				  y: 60,
				  text: '心率: 70',
				  fontSize: 18,
				  align: 'center',
				  width: monitorStage.getWidth(),
				  fill: 'rgb(160, 160, 160)'
			});
			var bpdata = new Kinetic.Text({
				  x: 0,
				  y: 95,
				  text: '101 / 75',
				  fontSize: 34,
				  fontStyle: 'bold',
				  align: 'center',
				  width: monitorStage.getWidth(),
				  fill: 'rgb(120, 210, 90)'
			});	

			var time = new Kinetic.Text({
				  x: 0,
				  y: 145,
				  text: '11:58',
				  fontSize: 20,
				  fontStyle: 'bold',
				  align: 'center',
				  width: monitorStage.getWidth(),
				  fill: 'rgb(142, 142, 142)'
			});						
			
			//向用户层中添加上面的矩形
			layer.add(arc1);
			layer.add(arc2);
			layer.add(arc3);
			layer.add(arc4);
			layer.add(arc5);
			layer.add(arc6);
			layer.add(circle);
			layer.add(circle2);
			layer.add(circle3);
			layer.add(heartrate);
			layer.add(bpdata);
			layer.add(time);
			//将上面的用户层添加到舞台上
			monitorStage.add(layer);
			monitorStage.draw();

			function doRequestBpData() {
				var username = util.getCookieByKey("loginid"),
					token = util.getCookieByKey("tk");
				$.ajax({
					url: getBpRecordUrl,
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
								case "1":
									$("#judge_text").html("稳定");
									break;
								case "2":
									$("#judge_text").html("波动");
									break;
								default:
							}
							//更新趋势图
							var bpDataArr = rspData.result.records;
							var xAxisArr = [],
								dbpArr = [],
								sbpArr = [],
								rateArr = [];
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
						}
					},
					error: function(xhr, errormsg) {
						util.toast("获取数据失败，请刷新界面");
					}
				});

			}
		});
	};

});