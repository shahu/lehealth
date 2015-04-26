define(function(require, exports, module) {

	var $ = require('jquery_mobile');
	var util = require('./common');
	var highcharts = require('highcharts');

	var getBpRecordUrl = "/lehealth/api/bp/record/list";

	var trendchart;

	var isChartMode = true;

	var timeInterval = 7;

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
					if (!judge) {
						$("#judge_text").html("暂无您的血压状况评估");
					} else {
						switch (judge) {
							case 1:
								$("#judge_text").html("您的血压状况偏低");
								break;
							case 2:
								$("#judge_text").html("您的血压状况正常");
								break;
							case 3:
								$("#judge_text").html("您的血压状况偏高");
								break;
							default:
						}
					}

					var bpDataArr = rspData.result.records || [];
					if (isChartMode) {
						//更新趋势图

						var hasMedicate = false;
						if (bpDataArr != null && bpDataArr.length > 0) {
							var lastestBpDate = new Date(bpDataArr[bpDataArr.length - 1].date);
							var now = new Date();
							if (now.getFullYear() == lastestBpDate.getFullYear() && now.getMonth() == lastestBpDate.getMonth() && now.getDate() == lastestBpDate.getDate()) {
								hasMedicate = true;
							}
						}
						if (!hasMedicate) {
							$('#hasmedicate').html('还未');
						} else {
							$('#hasmedicate').html('已经');
						}

						if (bpDataArr.length == 1) {
							var bpData = bpDataArr[0];
							var tmp = {};
							for (var i in bpData) {
								tmp[i] = bpData[i];
							}
							tmp.date += 1000 * 3600;
							bpDataArr.push(tmp);
						}
						console.info(bpDataArr);
						var isEmpty = (bpDataArr.length == 0 ? true : false);
						var now = new Date().getTime();
						for (var i = 0; i < days; i++) {
							bpDataArr.push({
								date: now - (i * 24 * 3600 * 1000),
								dbp: null,
								sbp: null
							});
						}

						var ranges = [];

						for (var i = 0; i < bpDataArr.length; i++) {
							var bpobj = bpDataArr[i];
							ranges.push([bpobj.date, bpobj.dbp, bpobj.sbp]);
						}
						console.info(ranges);

						var chartwidth = bpDataArr.length * 45 > screen.width ? bpDataArr.length * 45 : screen.width;
						$('#trendchart').css('width', chartwidth + 'px');
						showCharts(ranges, isEmpty);
					} else {
						showList(bpDataArr);
					}
				}
			},
			error: function(xhr, errormsg) {
				util.toast("获取数据失败，请刷新界面");
			}
		});

	}

	function showList(bpDataArr) {
		$('#bplist').show();
		$('#trend').hide();
		$('#bplistul').empty();
		for(var i = bpDataArr.length - 1; i >= 0; i--) {
			var dt = bpDataArr[i];
			var tmplobj = $('#list-tmpl').clone();
			tmplobj.find(".bptime").text(new Date(dt.date).format("MM月dd日 hh:mm"));
			tmplobj.find(".dbpnum").text(dt.dbp);
			tmplobj.find(".sbpnum").text(dt.sbp);
			tmplobj.find(".hratenum").text(dt.heartrate);
			$('#bplistul').append(tmplobj);
		}
		if(bpDataArr.length == 0) {
			var tmplobj = $('#list-tmpl').clone();
			tmplobj.find(".bptime").text("暂无数据");
			tmplobj.find(".datap").hide();
			$('#bplistul').append(tmplobj);
		}
		$('#bplistul').listview("refresh");
	}

	function showCharts(ranges, isEmpty) {
		$('#bplist').hide();
		$('#trend').show();
		$('#trendchart').empty();
		//渲染血压趋势图
		var gridTheme = require('highcharts_theme').getGridThemeOption();
		highcharts.setOptions(gridTheme);
		if (isEmpty) {
			var yopts = [{
				title: {
					text: 'mmHg',
					margin: 0
				},
				lineWidth: 1,
				opposite: false,
				min: 0,
				max: 200
			}];
		} else {
			var yopts = [{
				title: {
					text: 'mmHg',
					margin: 0
				},
				lineWidth: 1,
				opposite: false
			}];
		}


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
				type: 'datetime',
				dateTimeLabelFormats: {
					day: '%m.%e'
				}
			},
			yAxis: yopts,
			legend: {},
			tooltip: {
				dateTimeLabelFormats: {
					minute: '%m月%e日 %H:%M',
					second: '%m月%e日 %H:%M:%S'
				}
			},
			series: [{
				name: '血压',
				data: ranges,
				type: 'arearange',
				lineWidth: 0,
				linkedTo: ':previous',
				color: Highcharts.getOptions().colors[0],
				fillOpacity: 0.3,
				zIndex: 0,
				yAxis: 0,
			}],
			credits: {
				enabled: false
			}
		});
	}

	exports.render = function() {
		$.mobile.loading('show', {
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
				timeInterval = 7;
				doRequestBpData(7);
			});
			$('#monbtn').off("click");
			$('#monbtn').on('click', function() {
				$('#halfmonbtn').removeClass('ui-btn-active');
				$('#sevendaybtn').removeClass('ui-btn-active');
				$('#monbtn').addClass('ui-btn-active');
				timeInterval = 30;
				doRequestBpData(30);
			});
			$('#halfmonbtn').off("click");
			$('#halfmonbtn').on('click', function() {
				$('#halfmonbtn').addClass('ui-btn-active');
				$('#sevendaybtn').removeClass('ui-btn-active');
				$('#monbtn').removeClass('ui-btn-active');
				timeInterval = 15;
				doRequestBpData(15);
			});
			$('#sevendaybtn').addClass('ui-btn-active');
			$('#sevendaybtn').click();

			$('#chartmode').off('click');			
			$('#chartmode').on('click', function() {
				$('#chartmode').addClass('ui-btn-active');
				$('#listmode').removeClass('ui-btn-active');
				isChartMode = true;
				doRequestBpData(timeInterval);
			});

			$('#listmode').off('click');			
			$('#listmode').on('click', function() {
				$('#chartmode').removeClass('ui-btn-active');
				$('#listmode').addClass('ui-btn-active');				
				isChartMode = false;
				doRequestBpData(timeInterval);					
			});			

			$('#chartmode').addClass('ui-btn-active');
			isChartMode = true;
			timeInterval = 7;
			$('#chartmode').click();
		});
	};
});