<div style="font-size:16px; display: inline;"><img style="margin-right: 8px;vertical-align: top;" src="${contextPath}/resources/images/icon/pos-icon.png" alt="">当前位置:</div>
<%
for(parentMenu in parentMenus){
var url = @parentMenu.getUrl();
url = (url == null || url == "") ? "" : url;
var href = url == "" ? "" : "href=\""+url+"\"";
if(parentMenuLP.last){
%>
<a style="font-size:15px; display: inline;">${@parentMenu.getName()}</a>
<%} else {%>
<a ${href} style="font-size:16px; display: inline;">${@parentMenu.getName()}</a> <div style="font-size:16px; display: inline;"> > </div>
<%}
}%>