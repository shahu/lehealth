define("example/lehealth/consultlist",["jquery-mobile/1.4.4/jquery.mobile","./common"],function(a,b){var c=a("jquery-mobile/1.4.4/jquery.mobile"),d=a("./common"),e="/lehealth/api/doctor/list";b.render=function(){c(document).off("pageshow","#doctorlist"),c(document).on("pageshow","#doctorlist",function(){var a=d.getCookieByKey("loginid"),b=d.getCookieByKey("tk");c("#doctorlistcover").css("display","none");var a=d.getCookieByKey("loginid"),b=d.getCookieByKey("tk");c.ajax({url:e,type:"GET",dataType:"json",data:{loginid:a,token:b},async:!0,success:function(a){if(a.errorcode)d.toast("获取数据失败，请刷新界面");else{for(var b=a.result,e="",f="",g=0;g<b.length;g++){var h=b[g],i="",j=h.thumbnail?h.thumbnail:"images/person.jpg";0==h.attention&&(e+='<li><a href="/lehealth/expertdetail.html?a=1&status='+status+"&id="+h.id+'" id="'+h.id+'" ><img src="'+j+'" style="height:80px"><h2>'+h.name+"</h2><p>"+h.desc+"</p></a>"+i+"</li>"),1==h.attention&&(f+='<li><a href="/lehealth/expertdetail.html?a=1&status='+status+"&id="+h.id+'" id="'+h.id+'" ><img src="'+j+'" style="height:80px"><h2>'+h.name+"</h2><p>"+h.desc+"</p></a>"+i+"</li>")}c("#listwraper").empty(),c("#listwraper").html(e),c("#listwraper").listview("refresh"),c("#mylistwraper").empty(),c("#mylistwraper").html(f),c("#mylistwraper").listview("refresh")}},error:function(){d.toast("获取数据失败，请刷新界面")}})})}}),define("example/lehealth/common",["jquery-mobile/1.4.4/jquery.mobile"],function(a,b){function c(a){var b,c={};c.href=a;for(var d in e)b=e[d].exec(a),c[d]=b[1],a=b[2],""===a&&(a="/"),"pathname"===d&&(c.pathname=b[1],c.search=b[2],c.hash=b[3]);return c}var d=a("jquery-mobile/1.4.4/jquery.mobile");b.hideAddressBar=function(){};var e={protocol:/([^\/]+:)\/\/(.*)/i,host:/(^[^\:\/]+)((?:\/|:|$)?.*)/,port:/\:?([^\/]*)(\/?.*)/,pathname:/([^\?#]+)(\??[^#]*)(#?.*)/};b.getParams=function(a){var b=window.location.href,d=c(b).search;console.info("params:"+d);var e={};if(d)for(var f=d.split("&"),g=0;g<f.length;g++){var h=f[g].split("=");2==h.length&&(e[h[0]]=decodeURIComponent(h[1]))}return e[a]},b.getCookieByKey=function(a){var b=document.cookie.split("; "),c={};for(var d in b){var e=b[d].split("=");2==e.length&&(c[e[0]]=decodeURIComponent(e[1]))}return c[a]},b.setCookie=function(a,b){var c=new Date;c.setTime(c.getTime()+31536e6),document.cookie=a+"="+escape(b)+";expires="+c.toGMTString()},b.showDialog=function(a,b){d("#"+b+" .dialog_text").html("<p>"+a+"</p>"),d("#"+b+" .dialog").popup(),d("#"+b+" .dialog").popup("open")},b.dismissDialog=function(a){d("#"+a+" .dialog").popup("close")},b.toast=function(a){d("<div class='ui-loader ui-overlay-shadow ui-body-f ui-corner-all' style='background-color:#f9f9f9'><h3>"+a+"</h3></div>").css({display:"block",opacity:.9,position:"fixed",padding:"7px","text-align":"center",width:"270px",left:(d(window).width()-284)/2,top:d(window).height()/2-30}).appendTo(d.mobile.pageContainer).delay(1500).fadeOut(400,function(){d(this).remove()})},b.arrayEqual=function(a,b){if(!a||!b)return a||b?!1:!0;if(a.length==b.length){for(var c=0;c<a.length;c++){for(var d=!1,e=0;e<b.length;e++)a[c]==b[e]&&(d=!0);if(!d)return!1}return!0}return!1}});
