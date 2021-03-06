define(function(require, exports, module) {

	var $ = require('jquery_mobile');
	var util = require('./common');

	var getBpRecordUrl = "/lehealth/api/home/data";

	//记住上一次的评估状态，用于做动画
	var laststatus;

	var guardedList = [];

	//根据旋转的角度获取评分指示原点的圆心
	function getPoitorXY(degree, radius, x, y) {
		var _x = x + radius * Math.cos(2 * Math.PI * degree / 360);
		var _y = y + radius * Math.sin(2 * Math.PI * degree / 360);
		return {
			x: _x,
			y: _y
		};
	}


	function showJudgeInternal(degree, heartrate, sbp, dbp) {
		var stateColorArr = [
			'rgb(120, 210, 90)',
			'rgb(240, 180, 90)',
			'rgb(220, 60, 70)'
		];
		var status = getStatusByDegree(degree);
		var monitorStage = new Kinetic.Stage({
			container: "healthInfo",
			width: document.getElementById("healthInfo").offsetWidth,
			height: 220
		});

		var layer = new Kinetic.Layer();
		var arc1 = new Kinetic.Arc({
			x: monitorStage.getWidth() / 2,
			y: monitorStage.getHeight() / 2,
			innerRadius: monitorStage.getHeight() / 2 - 16,
			outerRadius: monitorStage.getHeight() / 2,
			fill: 'rgb(120, 210, 90)',
			angle: 110,
			rotationDeg: 110
		});

		var arc2 = new Kinetic.Arc({
			x: monitorStage.getWidth() / 2,
			y: monitorStage.getHeight() / 2,
			innerRadius: monitorStage.getHeight() / 2 - 16,
			outerRadius: monitorStage.getHeight() / 2,
			fill: 'rgb(240, 180, 90)',
			angle: 110,
			rotationDeg: 220
		});
		var arc3 = new Kinetic.Arc({
			x: monitorStage.getWidth() / 2,
			y: monitorStage.getHeight() / 2,
			innerRadius: monitorStage.getHeight() / 2 - 16,
			outerRadius: monitorStage.getHeight() / 2,
			fill: 'rgb(220, 60, 70)',
			angle: 100,
			rotationDeg: 330
		});

		console.info(degree);
		var poitorXY = getPoitorXY(degree, monitorStage.getHeight() / 2 - 26,
			monitorStage.getWidth() / 2, monitorStage.getHeight() / 2);

		var begin = getPoitorXY(70, monitorStage.getHeight() / 2 - 8,
			monitorStage.getWidth() / 2, monitorStage.getHeight() / 2);
		var end = getPoitorXY(110, monitorStage.getHeight() / 2 - 8,
			monitorStage.getWidth() / 2, monitorStage.getHeight() / 2);

		var circle = new Kinetic.Circle({
			x: poitorXY.x,
			y: poitorXY.y,
			radius: 10,
			fill: stateColorArr[status - 1]
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
			fill: 'rgb(120, 210, 90)'
		});

		var heartrate = new Kinetic.Text({
			x: 0,
			y: 60,
			text: '心率: ' + heartrate,
			fontSize: 20,
			align: 'center',
			width: monitorStage.getWidth(),
			fill: 'rgb(160, 160, 160)'
		});
		var bpdata = new Kinetic.Text({
			x: 0,
			y: 110,
			text: sbp + ' / ' + dbp,
			fontSize: 34,
			fontStyle: 'bold',
			align: 'center',
			width: monitorStage.getWidth(),
			fill: stateColorArr[status - 1]
		});

		//向用户层中添加上面的矩形
		layer.add(arc1);
		layer.add(arc2);
		layer.add(arc3);
		layer.add(circle);
		layer.add(circle2);
		layer.add(circle3);
		layer.add(heartrate);
		layer.add(bpdata);
		// layer.add(time);
		//将上面的用户层添加到舞台上
		monitorStage.add(layer);
		monitorStage.draw();
	}

	function showJudgePannel(status, heartrate, sbp, dbp) {
		var degree = getDegreeByStatus(status);
		if (!laststatus) {
			laststatus = status;
			showJudgeInternal(degree, heartrate, sbp, dbp);
		} else {
			var lastdegree = getDegreeByStatus(laststatus);
			laststatus = status;
			if (lastdegree == degree) {
				showJudgeInternal(degree, heartrate, sbp, dbp);
			} else {
				var diff = degree - lastdegree;
				var unit = degree > lastdegree ? 10 : -10;
				var time = (diff / unit) * 5;
				var cur = lastdegree;
				var animateTimer = setInterval(function() {
					cur += unit;
					showJudgeInternal(cur, heartrate, sbp, dbp);
					if (cur > degree) {
						clearInterval(animateTimer);
					}
				}, 5);
			}
		}


	}

	window.requestAnimFrame = (function(callback) {
		return window.requestAnimationFrame || window.webkitRequestAnimationFrame || window.mozRequestAnimationFrame || window.oRequestAnimationFrame || window.msRequestAnimationFrame ||
			function(callback) {
				window.setTimeout(callback, 10);
			};
	})();

	function getStatusByDegree(degree) {
		if (degree >= 110 && degree < 220) {
			return 1;
		} else if (degree >= 220 && degree < 330) {
			return 2;
		} else if (degree >= 330 && degree < 430) {
			return 3;
		}
	}

	function getDegreeByStatus(status) {
		var degree = 0; //just for test
		if (status == 1) {
			degree = 165;
		} else if (status == 2) {
			degree = 275;
		} else if (status == 3) {
			degree = 380;
		}
		return degree;
	}

	exports.render = function() {

		$.mobile.loading('show', {
			text: '页面加载中...',
			textVisible: true,
			theme: 'c',
			html: ''
		});

		$(document).off("pageshow", "#homepage");

		$(document).on("pageshow", "#homepage", function() {

			console.info('home init');

			guardedList = [];

			$("#homepagecover").css("display", "none");

			$.mobile.loading('hide');

			$('#nav-toggle').off('click');
			$('#nav-toggle').on('click', function() {
				if ($('#ulistwraper').css('display') == 'none') {
					$('.triangle-border').css('display', 'inline-block');
					$('.triangle-bg').css('display', 'inline-block');
					$('#ulistwraper').slideDown('fast', function() {});
				} else {
					$('#ulistwraper').slideUp('fast', function() {
						$('.triangle-bg').css('display', 'none');
						$('.triangle-border').css('display', 'none');
					});
				}
			});

			$('#ulistwraper').on('click', '.uitem', function() {
				var curuser = $('#curuser').text();
				if ($(this).text() !== curuser) {
					$.mobile.loading('show', {
						text: '页面加载中...',
						textVisible: true,
						theme: 'c',
						html: ''
					});
					doRequestBpData($(this).attr('userid'), $(this).text());
				}
				$('#ulistwraper').css('display', 'none');
				$('.triangle-bg').css('display', 'none');
				$('.triangle-border').css('display', 'none');
			});

			$('#todayDate').empty();
			var today = new Date();
			today = today.getFullYear() + '-' + (today.getMonth() + 1) + "-" + today.getDate();
			$('#todayDate').html(today);

			var trendchart,
				// judgechart,
				chartcount = 1;

			showJudgePannel(1, 0, 0, 0);

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
				yAxis: [{
					title: {
						text: 'mmHg',
						margin: 0
					},
					lineWidth: 1
				}, {
					title: {
						text: '次',
						margin: 0
					},
					lineWidth: 1,
					opposite: true
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
					yAxis: 0
				}, {
					name: '收缩压',
					data: [120, 120, 120, 120, 120, 120, 120],
					yAxis: 0
				}, {
					name: '心率',
					data: [80, 80, 80, 80, 80, 80, 80],
					yAxis: 1
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


			function doRequestBpData(userid, showname) {
				var username = util.getCookieByKey("loginid"),
					token = util.getCookieByKey("tk");

				var dayTotal = 15;
				$.ajax({
					url: getBpRecordUrl,
					type: "GET",
					dataType: "json",
					async: true,
					data: {
						loginid: username,
						token: token,
						days: dayTotal,
						user: userid
					},
					success: function(rspData) {
						//隐藏loading界面
						$.mobile.loading('hide');

						if (rspData.errorcode) {
							if (rspData.errorcode == 1) { //用户校验失败
								util.setCookie("jump", "/lehealth/bpmonitor.html");
								$.mobile.changePage("/lehealth/login.html", "slide");
								return;
							}
							util.toast("获取数据失败，请刷新界面");
						} else {
							userid = userid || "";
							if (!userid) {
								$('#ulistwraper').empty();
								//更新用户列表
								guardedList = rspData.result.guardeds || [];
								guardedList.unshift({
									username: "我",
									userid: ""
								});
								for (var i = 0; i < guardedList.length; i++) {
									var div_obj = $('<div class="uitem"></div>');
									div_obj.attr('userid', guardedList[i].userid);
									div_obj.text(guardedList[i].username);
									if (i == (guardedList.length - 1)) {
										div_obj.css('border-bottom', 'none');
									}
									div_obj.appendTo('#ulistwraper');
								}
								//更新top bar 用户名
								$('#curuser').text('我');
							} else {
								//更新top bar 用户名
								$('#curuser').text(showname);
							}
							//更新评价文案
							var judge = rspData.result.status;
							var latestData = rspData.result.records ? rspData.result.records[rspData.result.records.length - 1] : undefined;
							if (latestData) {
								showJudgePannel(judge, latestData.heartrate, latestData.sbp, latestData.dbp);
							}

							var dayInms = 3600 * 1000 * 24;
							//更新趋势图
							var bpDataArr = rspData.result.records || [],
								medicalhistory = rspData.result.history || [],
								newDataArr = [];
							var now = new Date();

							console.info(now.getTime());

							now.setHours(0);
							now.setMinutes(0);
							now.setSeconds(0);
							now.setMilliseconds(0);

							console.info(now.getTime());
							var beginBaseline = now.getTime() - (dayInms * dayTotal);
							//循环筛选数据
							for (var i = 1; i <= dayTotal; i++) {
								var found = false;
								for (var j = 0; j < bpDataArr.length; j++) {
									var bpdate = bpDataArr[j].date;
									var tmpDate = new Date(bpdate);
									tmpDate.setHours(0);
									tmpDate.setMinutes(0);
									tmpDate.setSeconds(0);
									tmpDate.setMilliseconds(0);
									bpDataArr[j].date = bpdate = tmpDate.getTime();
									if (bpdate >= (beginBaseline + i * dayInms) && bpdate < (beginBaseline + (i + 1) * dayInms)) {
										newDataArr.push(bpDataArr[j]);
										console.info('bpdate: ' + bpdate);
										found = true;
										break;
									}
								}
								if (!found) {
									newDataArr.push({
										date: beginBaseline + i * dayInms,
										dbp: null,
										sbp: null,
										heartrate: null
									});
								}
							}

							var xAxisArr = [],
								dbpArr = [],
								sbpArr = [],
								rateArr = [],
								medicalByDate = {};
							for (var i = 0; i < newDataArr.length; i++) {
								var bpobj = newDataArr[i];
								xAxisArr.push((new Date(bpobj.date)).getDate() + '日');
								console.info("date day:" + bpobj.date + ", day: " + (new Date(bpobj.date)).getDate());
								var daynum = (new Date(bpobj.date)).getDate();
								if (!medicalByDate[bpobj.date]) {
									medicalByDate[bpobj.date] = {};
								}
								dbpArr.push(bpobj.dbp);
								sbpArr.push(bpobj.sbp);
								rateArr.push(bpobj.heartrate);
							}
							console.info(medicalByDate);
							//计算背景区段
							for (var item in medicalhistory) {
								var mname = medicalhistory[item]['medicinename'];
								var date = new Date(medicalhistory[item]['date']);
								date.setHours(0);
								date.setMinutes(0);
								date.setSeconds(0);
								date.setMilliseconds(0);
								var dayMap = medicalByDate[date.getTime()];
								if (dayMap) {
									dayMap[mname] = true;
								}
							}
							console.info(medicalByDate);
							var tmpArr = [];
							for (var tm in medicalByDate) {
								var tmpItems = medicalByDate[tm];
								// var fname = "";
								var namearr = [];
								for (var _mname in tmpItems) {
									namearr.push(_mname);
								}

								tmpArr.push({
									tm: parseInt(tm),
									name: namearr,
									day: new Date(parseInt(tm)).getDate()
								});
							}
							tmpArr.sort(function(o1, o2) {
								return o1.tm - o2.tm;
							});
							console.info('tmpArr: ');
							console.info(tmpArr);

							var finalObj = [],
								tips = [];
							var from, to;

							//合并相邻位置
							for (var i = 0; i < tmpArr.length; i++) {
								var tmpobj = tmpArr[i];
								var nextobj = tmpArr[i + 1];
								if (i == 0) {
									var minDay = tmpArr[i].day;
								}

								if (!from) {
									from = tmpobj.day - minDay - 0.5;
								}
								if (!nextobj) {
									to = tmpobj.day + 0.5 - minDay; //?
									finalObj.push({
										from: from,
										to: to,
										label: {
											text: tmpobj.name.join('+')
										},
										color: 'blue'
									});
									tips.push({
										name: tmpobj.name.join('+'),
										color: '#87CEFF'
									});
									from = 0;
									to = 0;
									continue;
								}
								if (nextobj && util.arrayEqual(nextobj.name, tmpobj.name)) {
									continue;
								} else {
									to = tmpobj.day + 0.5 - minDay;
									finalObj.push({
										from: from,
										to: to,
										label: {
											text: tmpobj.name.join('+')
										},
										color: '#87CEFF'
									});
									tips.push({
										name: tmpobj.name.join('+'),
										color: '#87CEFF'
									});
									from = 0;
									to = 0;
									continue;
								}
							}
							console.info(finalObj);

							var color = [
								"#A4D3EE",
								"#B4CDCD",
								"#90EE90",
								"#B4EEB4",
								"#D1D1D1",
								"#CDAA7D",
								"#BCEE68",
								"#EEEE00"
							];

							console.info(tips);

							var colorIdx = 0,
								colorByName = {};
							$('#medicationtips').empty();
							var hasMedicalHistory = false;
							trendchart.xAxis[0].update({
								plotBands: []
							});
							for (var i = 0; i < finalObj.length; i++) {
								if (colorByName[finalObj[i].label.text]) {
									finalObj[i]['color'] = color[i];
									tips[i].color = color[i];
								}

								finalObj[i]['color'] = color[i];
								tips[i].color = color[i];
								if (!finalObj[i].label.text) {
									continue;
								} else {
									hasMedicalHistory = true;
									delete finalObj[i].label;
									$('#medicationtips').append('<div style="height: 20px; line-height: 20px; width: 100%; overflow: auto"><div style="width: 12px; height: 12px; margin: 4px;float:left; background-color: ' + tips[i].color + '"></div> <div style="float:left; margin-left: 8px; font-size: 12px; width: 80%; overflow: auto">' + tips[i].name + '</div><div style="clear:both"></div></div>');
								}
								trendchart.xAxis[0].addPlotBand(finalObj[i]);
							}
							if (!hasMedicalHistory) {
								$('#medicationtips').append('<div style="height: 20px; line-height: 20px; width: 100%; overflow: auto"><div style="float:left; margin-left: 8px; font-size: 12px; width: 80%; overflow: auto">没有您的用药数据</div><div style="clear:both"></div></div>');
							}

							trendchart.xAxis[0].setCategories(xAxisArr);
							trendchart.yAxis[0].update({
								min: 0,
								max: 200
							});
							trendchart.yAxis[1].update({
								min: 0,
								max: 200
							});
							trendchart.series[0].setData(dbpArr);
							trendchart.series[1].setData(sbpArr);
							trendchart.series[2].setData(rateArr);
						}
					},
					error: function(xhr, errormsg) {
						$.mobile.loading('hide');
						util.toast("获取数据失败，请刷新界面");
					}
				});

			}
		});
	};

});