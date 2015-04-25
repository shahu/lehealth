define(function(require, exports, module) {

	var $ = require('jquery');
	var util = require('./bgcommon');
	var util2 = require('./common');

	var loginUrl = "/lehealth/api/login.do";
	var getOrderInfoUrl = "weixin/order/list";

	var username = $('#login_username').val(),
		token = $('#login_pwd').val();

	var roles = {
		"admin": 1,
		"doctor": 2,
		"patient": 4
	};

	var trendchart;

	var isloading = false;

	exports.render = function() {
		$('#patients').on('click', 'a', function() {
			if (isloading) {
				return;
			}
			isloading = true;
			$('#patients li.active').removeClass('active');
			$(this).parent().addClass('active');
			$('#orderpage').css("display", "none");
			$('#patientpage').css("display", "block");
			showPatientInfo($(this).attr('pid'), function() {
				isloading = false;
				$('.mainDiv').css('display', 'block');
			});
		});

		$('#exit').on('click', function() {
			util.setCookie('loginid', '');
			util.setCookie('tk', '');
			window.location.href = "/lehealth/backendlogin.html"
		});

		$("#myorder").on('click', function() {
			$('#orderpage').css("display", "block");
			$('#patientpage').css("display", "none");
			$('.mainDiv').css('display', 'block');
			//获取订单
			var username = util.getCookieByKey("loginid"),
				token = util.getCookieByKey("tk");
			//获取
			$.ajax({
				url: getOrderInfoUrl,
				type: 'GET',
				dataType: 'json',
				data: {
					loginid: username,
					token: token
				},
				success: function(rsp) {
					if (rsp.errorcode) {

						util.toast("获取订单信息失败");
					} else {
						var orderlist = rsp.result;
						$("#orderlistdata").empty();
						for (var i = 0; i < orderlist.length; i++) {
							var ordercard = $('#tr_tmpl').clone();
							ordercard.removeAttr('id');
							var timestr = new Date(orderlist[i].createtime).format("yyyy年 MM月dd日 hh:mm:ss");
							var state = "支付中";
							if (orderlist[i].status == 2) {
								state = "支付完成";
							} else if (orderlist[i].status == 3) {
								state = "支付失败";
							} else if (orderlist[i].status == 4) {
								state = "订单取消";
							}
							ordercard.find("td.ordercode").text(orderlist[i].orderid);
							ordercard.find("td.ordertime").text(timestr);
							ordercard.find("td.orderfee").text("￥" + orderlist[i].fee);
							ordercard.find("td.orderstate").text(state);
							ordercard.find("td.orderdesc").text(orderlist[i].goodsdetail);
							$("#orderlistdata").append(ordercard);
						}
					}
				},
				error: function() {

					util.toast("获取订单信息失败");
				}
			});			

		});

		$('#sideupdown_icon').on('click', function() {
			if ($(this).hasClass('icon-chevron-up')) {
				$(this).removeClass('icon-chevron-up');
				$(this).addClass('icon-chevron-down');
				$('#list_div').slideUp('slow', function() {

				});
			} else if ($(this).hasClass('icon-chevron-down')) {
				$(this).removeClass('icon-chevron-down');
				$(this).addClass('icon-chevron-up');
				$('#list_div').slideDown('slow', function() {

				});
			}
		});

		getUserBasicInfo(function(err, role) {
			if (err) {
				alertMsg("请求失败，请稍后重试");
				return;
			}
			if (role == roles.admin || role == roles.doctor) {
				//显示我的病人标签，同时请求第一个病人数据展示
				getPatients(function(err, patientArr) {
					if (err) {
						alertMsg("获取病人列表失败");
						return;
					}
					$('#patients').empty();
					var html = "";
					for (var i = 0; i < patientArr.length; i++) {
						html += '<li><a href="#" pid="' + patientArr[i].userid + '">' + patientArr[i].username + '</a></li>'
					}
					$('#patients').html(html);
				});
			} else {
				alert("you are not permitted");
			}
		});

	};

	function showPatientInfo(patientId, cb) {
		//渲染图表原始界面,先填充默认数据，然后再通过网络请求填充真实数据
		var highcharts = require('highcharts');
		//渲染血压趋势图
		var gridTheme = require('highcharts_theme').getGridThemeOption();
		highcharts.setOptions(gridTheme);
		$('#medicationInfo').highcharts({
			chart: {
				type: 'line',
				backgroundColor: 'white'
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
		});


		var count = 3;
		getAndRenderPatientInfo(patientId, function(err) {
			count--;
			if (count == 0) {
				cb();
			}
		});
		getAndRenderPatientBpData(patientId, function(err) {
			count--;
			if (count == 0) {
				cb();
			}
		});
		getAndRenderDiseaseHistory(patientId, function(err) {
			count--;
			if (count == 0) {
				cb();
			}
		});
	}

	/**
	 * [getUserBasicInfo 获取登录用户的基础信息]
	 * @param  {Function} cb [description]
	 * @return {[type]}      [description]
	 */
	function getUserBasicInfo(cb) {
		var loginId = util.getCookieByKey("loginid"),
			pwd = util.getCookieByKey("tk");
		$.ajax({
			url: '/lehealth/api/role',
			dataType: 'json',
			type: 'get',
			data: {
				loginid: loginId,
				token: pwd
			},
			success: function(rsp) {
				if (rsp.errorcode) {
					cb(rsp.errorcode);
				} else {
					cb(0, rsp.result.roleid);
				}
			},
			error: function(xhr, errormsg) {
				cb(1);
			}
		});
	}

	/**
	 * [getPatients 获取我的病人信息列表]
	 * @param  {Function} cb [description]
	 * @return {[type]}      [description]
	 */
	function getPatients(cb) {
		var loginId = util.getCookieByKey("loginid"),
			pwd = util.getCookieByKey("tk");
		$.ajax({
			url: '/lehealth/admin/patient/list',
			dataType: 'json',
			type: 'get',
			data: {
				loginid: loginId,
				token: pwd
			},
			success: function(rsp) {
				if (rsp.errorcode) {
					cb(rsp.errorcode);
				} else {
					cb(0, rsp.result);
				}
			},
			error: function(xhr, errorMsg) {
				cb(1);
			}
		});
	}

	/**
	 * [getPatientInfo 获取给定病人的个人信息]
	 * @param  {[type]}   patientId [description]
	 * @param  {Function} cb        [description]
	 * @return {[type]}             [description]
	 */
	function getAndRenderPatientInfo(patientId, cb) {
		var loginId = util.getCookieByKey("loginid"),
			pwd = util.getCookieByKey("tk");
		var url = '/lehealth/admin/patient/info';
		$.ajax({
			url: url,
			type: 'get',
			dataType: 'json',
			data: {
				loginid: loginId,
				token: pwd,
				pid: patientId
			},
			success: function(rsp) {
				if (rsp.errorcode) {
					cb();
				} else {
					var rs = rsp.result;
					$('#username').html(rs.username);
					$('#phonenum').html(loginId);
					$('#gender').html(rs.gender == 0 ? '女' : '男');
					$('#height').html(rs.height + 'cm');
					$('#weight').html(rs.weight + '公斤');
					$('#age').html(rs.age + '岁');
					cb();
				}
			},
			error: function(xhr, errorMsg) {
				cb();
			}
		});
	}

	/**
	 * [getPatientBpData 获取病人的血压和用药信息]
	 * @param  {[type]}   patientId [description]
	 * @param  {Function} cb        [description]
	 * @return {[type]}             [description]
	 */
	function getAndRenderPatientBpData(patientId, cb) {
		var url = '/lehealth/admin/patient/record/list',
			loginId = util.getCookieByKey("loginid"),
			pwd = util.getCookieByKey("tk");

		var dayTotal = 15;
		$.ajax({
			url: url,
			type: "GET",
			dataType: "json",
			async: true,
			data: {
				loginid: loginId,
				token: pwd,
				days: dayTotal,
				pid: patientId
			},
			success: function(rspData) {
				console.info(rspData);
				if (rspData.errorcode) {
					cb();
				} else {
					//更新评价文案
					var judge = rspData.result.status;
					var latestData = rspData.result.records ? rspData.result.records[rspData.result.records.length - 1] : undefined;
					if (latestData) {
						// showJudgePannel(judge, latestData.heartrate, latestData.sbp, latestData.dbp);
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
					cb();
				}
			},
			error: function(xhr, errormsg) {
				cb(0);
			}
		});

	}

	/**
	 * [getDiseaseHistory 获取病人的病史信息]
	 * @param  {[type]}   patientId [description]
	 * @param  {Function} cb        [description]
	 * @return {[type]}             [description]
	 */
	function getAndRenderDiseaseHistory(patientId, cb) {
		var loginId = util.getCookieByKey("loginid"),
			pwd = util.getCookieByKey("tk"),
			url = '/lehealth/admin/patient/disease/list';
		$.ajax({
			url: url,
			type: 'get',
			dataType: 'json',
			data: {
				loginid: loginId,
				token: pwd,
				pid: patientId
			},
			success: function(rsp) {
				if (rsp.errorcode) {
					cb();
				} else {
					var rs = rsp.result;
					$('#deaselist').empty();
					for (var i = 0; i < rs.length; i++) {
						var info = rs[i];
						$('#deaselist').append('<div style="margin-right: 20px; margin-left: 40px;"><div style="background-color:#888888; color: white; font-size: 18px; height: 30px; line-height: 30px;  border-radius:5px;padding-left: 20px;">' + '<span class="deseasename">' + (i + 1) + '.' + info.diseasename + '</span><div style="clear:both"></div></div><div><fieldset class="scheduler-border"><legend class="scheduler-border">病情描述</legend>' + '<p class="deseasedesc">' + info.diseasedescription + '</p></fieldset><fieldset class="scheduler-border"><legend class="scheduler-border">用药不良反应</legend><p class="meditiondesc">' + info.medicinedescription + '</p></fieldset></div></div>');
					}
					cb();
				}
				if (rs.length == 0) {
					$('#deaselist').append('<h4 style="margin-left: 20px; margin-top: 20px; margin-bottom: 20px;">暂无病史信息</h4>');
				}
			},
			error: function(xhr, errorMsg) {
				cb();
			}
		});
	}

	function alertMsg(msg) {
		alert(msg);
	}

});