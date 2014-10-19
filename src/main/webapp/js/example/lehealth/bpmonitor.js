define(function(require, exports, module) {

	var $ = require('jquery_mobile');
	exports.render = function() {
		//渲染图表原始界面,先填充默认数据，然后再通过网络请求填充真实数据
		var highcharts = require('highcharts');
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
	            categories: ['1日', '2日', '3日', '4日', '5日', '6日', '7日', '8日', '9日', '10日']
	        },
	        yAxis: {
	            title: {
	                text: ''
	            }
	        },
	        tooltip: {
	            enabled: true,
	            formatter: function() {
	                return '<b>'+ this.series.name +'</b><br>'+this.x +': '+ this.y;
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
	            data: [90, 90, 90, 90, 90, 90, 90, 90, 90, 90]
	        }, {
	            name: '收缩压',
	            data: [120, 120, 120, 120, 120, 120, 120, 120, 120, 120]
	        }],	        
			credits: {
		    	enabled: false
		    }	        
	    }, function(chart) {
	    	//get data from server
	    	var xAxisArr = [];
	    	for(var i = 0; i < 10; i++) {
	    		xAxisArr.push((i + 7) + '日');
	    	}
	    	chart.xAxis[0].setCategories(xAxisArr);
			chart.series[0].setData([78, 81, 83, 85, 71, 100, 99, 87, 94, 99]);
	    	chart.series[1].setData([120, 130, 140, 150, 123, 133, 144, 128, 111, 132]);
	    });
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
		                linearGradient: { x1: 0, y1: 0, x2: 0, y2: 1 },
		                stops: [
		                    [0, '#FFF'],
		                    [1, '#333']
		                ]
		            },
		            borderWidth: 0,
		            outerRadius: '109%'
		        }, {
		            backgroundColor: {
		                linearGradient: { x1: 0, y1: 0, x2: 0, y2: 1 },
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
			point.update(90);

		});
	};

	exports.bindEvent = function() {

	}

});