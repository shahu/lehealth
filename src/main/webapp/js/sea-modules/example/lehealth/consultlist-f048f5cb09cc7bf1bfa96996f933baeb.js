define("example/lehealth/consultlist",["jquery-mobile/1.4.4/jquery.mobile","./common"],function(a,b){var c=a("jquery-mobile/1.4.4/jquery.mobile"),d=a("./common"),e="/api/doctors";b.render=function(){c(document).bind("pageinit",function(){d.hideAddressBar();d.getCookieByKey("loginid"),d.getCookieByKey("tk");c.ajax({url:e,type:"GET",dataType:"json",async:!0,success:function(a){if(a.errorcode)d.showDialog("获取数据失败，请刷新界面","doctorlist");else{for(var b=a.result,e="",f=0;f<b.length;f++){var g=b[f];e+='<li>\n									<a href="/lehealth/expertdetail.html" id="'+g.id+'">\n										<img src="images/person.jpg" style="width:80px; height:80px">\n										<h2>'+g.name+"</h2>\n										<p>"+g.desc+"</p>\n									</a>\n								</li>"}c("#listwraper").empty(),c("#listwraper").html(e),c("#listwraper").listview("refresh")}},error:function(){d.showDialog("获取数据失败，请刷新界面","doctorlist")}})})}}),define("example/lehealth/common",["jquery-mobile/1.4.4/jquery.mobile"],function(a,b){function c(a){var b,c={};c.href=a;for(var d in e)b=e[d].exec(a),c[d]=b[1],a=b[2],""===a&&(a="/"),"pathname"===d&&(c.pathname=b[1],c.search=b[2],c.hash=b[3]);return c}var d=a("jquery-mobile/1.4.4/jquery.mobile");b.hideAddressBar=function(){document.documentElement.scrollHeight<=document.documentElement.clientHeight&&(bodyTag=document.getElementsByTagName("body")[0],bodyTag.style.height=document.documentElement.clientWidth/screen.width*screen.height+"px"),setTimeout(function(){window.scrollTo(0,1)},0)};var e={protocol:/([^\/]+:)\/\/(.*)/i,host:/(^[^\:\/]+)((?:\/|:|$)?.*)/,port:/\:?([^\/]*)(\/?.*)/,pathname:/([^\?#]+)(\??[^#]*)(#?.*)/};b.getParams=function(a){var b=window.location.href,d=c(b).search,e={};if(d)for(var f=d.split("&"),g=0;g<f.length;g++){var h=f[g].split("=");2==h.length&&(e[h[0]]=decodeURIComponent(h[1]))}return e[a]},b.getCookieByKey=function(a){var b=document.cookie.split("; "),c={};for(var d in b){var e=b[d].split("=");2==e.length&&(c[e[0]]=decodeURIComponent(e[1]))}return c[a]},b.setCookie=function(a,b){var c=new Date;c.setTime(c.getTime()+31536e6),document.cookie=name+"="+escape(b)+";expires="+c.toGMTString()},b.showDialog=function(a,b){d("#"+b+" .dialog_text").html("<p>"+a+"</p>"),d("#"+b+" .dialog").popup(),d("#"+b+" .dialog").popup("open")},b.dismissDialog=function(a){d("#"+a+" .dialog").popup("close")}});