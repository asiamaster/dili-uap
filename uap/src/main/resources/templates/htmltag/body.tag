<!DOCTYPE html>
<html lang="en">
<head>
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1" />
    <meta charset="utf-8" />
    <title>地利客户关系管理系统</title>
    <meta name="description" content="overview &amp; stats" />
    <link rel="shortcut icon" href="${contextPath}/resources/images/icon/dili-logo.png" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0" />
    <#css/>
    <#js/>
    
    <%
    if(has(pageCss) && pageCss=="true"){
    %>
    <#pageCss/>
    <%
    }
    %>
    
    <% if(has(pageJs) && pageJs=="true"){%>
    <#pageJs/>
    <%}%>
    <script type="text/javascript">
        var contextPath = '${contextPath}';
    </script>

</head>
<body>
<#loadingProgress/>
${tag.body}
</body>
</html>
