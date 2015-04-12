define("example/lehealth/register",["jquery-mobile/1.4.4/jquery.mobile","./common"],function(a,b){var c,d=a("jquery-mobile/1.4.4/jquery.mobile"),e=a("./common"),f="/lehealth/api/patient/register",g="/lehealth/api/identifyingcode";b.render=function(){},b.bindEvent=function(){d.mobile.loading("show",{text:"页面加载中...",textVisible:!0,theme:"c",html:""}),d(document).off("pagehide","#register"),d(document).on("pagehide","#register",function(){c&&(clearInterval(c),d("#obtainCode").prop("disabled",!1).removeClass("ui-disabled"),d("#obtainCode").text("获取"))}),d(document).off("pageshow","#register"),d(document).on("pageshow","#register",function(){d("body").css("display","inline"),d.mobile.loading("hide"),d("#obtainCode").off("click"),d("#obtainCode").on("click",function(){var a=d("#register_username").val();return/^(13[0-9]|15[0-9]|14[7|5]|17[0-9]|18[0-9])\d{8}$/.test(a)?(d("#obtainCode").prop("disabled",!0).addClass("ui-disabled"),void d.ajax({url:g,type:"GET",dataType:"json",data:{phone:a},success:function(a){a.errorcode?(e.toast(a.errormsg),d("#obtainCode").prop("disabled",!1).removeClass("ui-disabled")):(d("#obtainCode").prop("disabled",!0).addClass("ui-disabled"),d("#obtainCode").text("60"),c=setInterval(function(){var a=parseInt(d("#obtainCode").text());a--,console.info(a),0>a?(clearInterval(c),d("#obtainCode").prop("disabled",!1).removeClass("ui-disabled"),d("#obtainCode").text("获取")):d("#obtainCode").text(10>a?"0"+a:a+"")},1e3))},error:function(){e.toast("获取验证码失败"),d("#obtainCode").prop("disabled",!1).removeClass("ui-disabled")}})):void e.toast("手机号不正确")}),d("#doRegister").off("click"),d("#doRegister").on("click",function(){var a=d("#register_username").val(),b=d("#register_pwd").val(),c=d("#register_repwd").val(),g=d("#verifyCode").val();return/^(13[0-9]|15[0-9]|14[7|5]|17[0-9]|18[0-9])\d{8}$/.test(a)?!b||b.length<=6?void e.toast("密码不能低于六位"):b!==c?void e.toast("两次输入密码不一致"):g?void d.ajax({url:f,type:"POST",dataType:"json",async:!0,data:{loginid:a,password:b,password2:c,identifyingcode:g},success:function(a){a.errorcode?e.toast("注册失败，"+a.errormsg):(e.toast("注册成功, 请登录"),setTimeout(function(){d.mobile.changePage("/lehealth/login.html",{transition:"slide",reverse:"true",changeHash:!0})},2e3))},error:function(){e.toast("网络错误")}}):void e.toast("请输入验证码"):void e.toast("手机号不正确")})})}}),define("example/lehealth/common",["jquery-mobile/1.4.4/jquery.mobile"],function(a,b){function c(a){var b,c={};c.href=a;for(var d in e)b=e[d].exec(a),c[d]=b[1],a=b[2],""===a&&(a="/"),"pathname"===d&&(c.pathname=b[1],c.search=b[2],c.hash=b[3]);return c}var d=a("jquery-mobile/1.4.4/jquery.mobile");b.hideAddressBar=function(){};var e={protocol:/([^\/]+:)\/\/(.*)/i,host:/(^[^\:\/]+)((?:\/|:|$)?.*)/,port:/\:?([^\/]*)(\/?.*)/,pathname:/([^\?#]+)(\??[^#]*)(#?.*)/};b.getParams=function(a){var b=window.location.href,d=c(b).search;console.info("params:"+d);var e={};if(d)for(var f=d.split("&"),g=0;g<f.length;g++){var h=f[g].split("=");2==h.length&&(e[h[0]]=decodeURIComponent(h[1]))}return e[a]},b.getCookieByKey=function(a){var b=document.cookie.split("; "),c={};for(var d in b){var e=b[d].split("=");2==e.length&&(c[e[0]]=decodeURIComponent(e[1]))}return c[a]},b.setCookie=function(a,b){var c=new Date;c.setTime(c.getTime()+31536e6),document.cookie=a+"="+escape(b)+";expires="+c.toGMTString()},b.showDialog=function(a,b){d("#"+b+" .dialog_text").html("<p>"+a+"</p>"),d("#"+b+" .dialog").popup(),d("#"+b+" .dialog").popup("open")},b.dismissDialog=function(a){d("#"+a+" .dialog").popup("close")},b.toast=function(a){d("<div class='ui-loader ui-overlay-shadow ui-body-f ui-corner-all' style='background-color:#f9f9f9'><h3>"+a+"</h3></div>").css({display:"block",opacity:.9,position:"fixed",padding:"7px","text-align":"center",width:"270px",left:(d(window).width()-284)/2,top:d(window).height()/2-30}).appendTo(d.mobile.pageContainer).delay(1500).fadeOut(400,function(){d(this).remove()})},b.arrayEqual=function(a,b){if(!a||!b)return a||b?!1:!0;if(a.length==b.length){for(var c=0;c<a.length;c++){for(var d=!1,e=0;e<b.length;e++)a[c]==b[e]&&(d=!0);if(!d)return!1}return!0}return!1}});
