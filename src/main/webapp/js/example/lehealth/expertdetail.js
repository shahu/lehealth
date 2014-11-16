define(function(require, exports, module) {

	var $ = require('jquery_mobile');
	var util = require("./common");

	var doctordetailUrl = "/lehealth/api/doctor.do";

	exports.render = function() {
		$(document).off("pageshow", "#doctordetail");

		$(document).on("pageshow", "#doctordetail", function() {

			$.ajax({
				url: doctordetailUrl,
				type: "GET",
				dataType: "json",
				data:{
					doctorid: util.getParams('id')
				},
				async: true,
				success: function(rspData) {
					if (rspData.errorcode) {
						util.toast("获取数据失败，请刷新界面");
					} else {
						var doctormsg = {
							thumbnail: 'images/xjp.jpg',
							gender: 0,
							name: '张辉耀',
							title: '主任医师',
							hospital: '曙光医院',
							desc: '<div class="card-summary nslog-area clearfix" data-nslog-type="72"><div class="card-summary-content"><div class="para">潘绥铭，出生于1950年，1984年毕业于<a target="_blank" href="/view/4249.htm">东北师范大学</a>历史系,获历史学硕士学位。曾留学美国，主要从事<a target="_blank" href="/view/2673660.htm">社会学研究方法</a>、<a target="_blank" href="/view/1632480.htm">性社会学</a>和性别人类学研究，被誉为“<a target="_blank" href="/view/61891.htm">中国</a>性学第一人” 。</div><div class="para">现任中国人民大学性社会学研究所所长、社会学系教授、<a target="_blank" href="/view/697523.htm">博士生导师</a>，<a target="_blank" href="/view/4401737.htm">中国社会学会</a>副秘书长。</div><div class="para">2012年4月审计发现潘绥铭承担的由卫生部牵头组织实施的“艾滋病和病毒性肝炎等重大传染病防治”重大专项有关子课题弄虚作假套取国家科技重大专项资金，被行政处分。所谓“科研资金使用不明”主要原因在于，科研资金使用涉及到访谈对象必须给钱等学术伦理方面尚有争议之事，因此资金帐户难以达到审计要求<sup>[1]</sup><a class="anchor-inline" name="ref_[1]_1269900">&nbsp;</a>。</div></div></div>'
						};
						doctormsg = rspData.result;
						if(doctormsg.thumbnail) {
							$('#thumbnail').attr("src", doctormsg.thumbnail);
						}
						if(doctormsg.gender == 0) {
							$('#gender').html('男');
						} else {
							$('#gender').html('女');	
						}
						$('#doctorname').html(doctormsg.name);
						$('#doctortitle').html(doctormsg.title);
						$('#hospital').html(doctormsg.hospital);
						$('#description').html(doctormsg.desc);
						$("#doctordetailcover").css("display", "none");
					}
				},
				error: function(xhr, errormsg) {
					util.toast("获取数据失败，请刷新界面");
				}
			});


		});
	};

});