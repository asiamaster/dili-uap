<script>
    var websocket = null;
    if('WebSocket' in window) {
        websocket = new WebSocket("ws://uap.diligrp.com/ws/message");
    } else if('MozWebSocket' in window) {
        websocket = new MozWebSocket("ws://uap.diligrp.com/ws/message");
    }else{
        console.log("浏览器不支持WebSocket");
    }

    //连接发生错误的回调方法
    websocket.onerror = function () {
        console.log("WebSocket连接发生错误");
    };

    //连接成功建立的回调方法
    websocket.onopen = function () {
        console.log("WebSocket连接成功");
    }

    //接收到消息的回调方法
    websocket.onmessage = function (event) {
        let annunciateMessage = JSON.parse(event.data);
        let type = annunciateMessage.type;
        let id = annunciateMessage.id;
        // 消息标题
        var title = "";
        if(type == ${@com.dili.uap.sdk.glossary.AnnunciateMessageType.ANNUNCIATE.getCode()}){
            title = "平台公告";
        }else if(type == ${@com.dili.uap.sdk.glossary.AnnunciateMessageType.TODO.getCode()}){
            title = "待办事宜";
        }else if(type == ${@com.dili.uap.sdk.glossary.AnnunciateMessageType.BUSINESS.getCode()}){
            title = "业务消息";
        }
        //组装消息内容，包含消息内容和未读条数
        let unreadCount = "<div style='float: right; margin:0 auto;' class='red-point cursorPointerTransform' onclick='javascript:showMessages()'>"+annunciateMessage.unreadCount+"</div>";
        var content = annunciateMessage.title.length > 50 ? annunciateMessage.title.substr(0, 60) +"......" : annunciateMessage.title;
        let msg = "<a style='width:246px;height:80px;float:left;' class='cursorPointer' onclick='javascript:showDetail("+id+")'>"+content+"</a>"+ unreadCount;
        //显示时间，默认3秒
        let timeout = annunciateMessage.timeout == null ? 3000 : annunciateMessage.timeout;
        $("#uncreadCount").html(annunciateMessage.unreadCount);
        $.messager.show({
            title:title,
            msg: msg,
            timeout:timeout,
            showSpeed:700,
            height:135,
            showType:'fade'
        });
    }

    //连接关闭的回调方法
    websocket.onclose = function () {
        console.log("WebSocket连接关闭");
    }

    //监听窗口关闭事件，当窗口关闭时，主动去关闭websocket连接，防止连接还没断开就关闭窗口，server端会抛异常。
    window.onbeforeunload = function () {
        closeWebSocket();
    }

    //将消息显示在网页上
    function setMessageInnerHTML(innerHTML) {
        document.getElementById('message').innerHTML += innerHTML + '<br/>';
    }

    //关闭WebSocket连接
    function closeWebSocket() {
        websocket.close();
    }

    //发送消息
    function send() {
        var message = document.getElementById('text').value;
        websocket.send(message);
    }
</script>