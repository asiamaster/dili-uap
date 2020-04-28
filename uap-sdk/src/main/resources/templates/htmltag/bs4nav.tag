<div class="row">
    <nav class="col-12">
        <ol class="breadcrumb"  data-toggle="collapse" data-target="#_nav" aria-controls="_nav">
            <li class="breadcrumb-item active">
                <div style="font-size:14px; display: inline;"><img style="margin-right: 8px;vertical-align: top;" src="${contextPath}/resources/images/icon/pos-icon.png" alt="">当前位置:</div>
                <%
                    if(has(parentMenus)){
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
                        }//end of for
                    }//end of if%>
                <a href="javascript:;"><i class="fa fa-angle-double-down fa-lg" aria-hidden="true"></i></a>
            </li>
        </ol>

        <div id="_nav" class="collapse show">
            ${tag.body}
        </div>
    </nav>
</div>

