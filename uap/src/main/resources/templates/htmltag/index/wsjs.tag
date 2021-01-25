<script>
    var websocket = null;
    //保存右下弹出消息框id，用于关闭
    var messagerShowWinId;
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
        //消息通知
        if(annunciateMessage.eventType == 1){
            showMessage(annunciateMessage);
        }
        //消息撤销
        else if(annunciateMessage.eventType == 2){
            withdrawMessage(annunciateMessage);
        }
    }

    //连接关闭的回调方法
    websocket.onclose = function () {
        console.log("WebSocket连接关闭");
    }

    //监听窗口关闭事件，当窗口关闭时，主动去关闭websocket连接，防止连接还没断开就关闭窗口，server端会抛异常。
    window.onbeforeunload = function () {
        closeWebSocket();
    }

    /**
     * 撤回一条消息
     */
    function withdrawMessage(annunciateMessage) {
        //annunciate的id，用于撤销消息详情
        let id = annunciateMessage.id;
        var unreadCount = parseInt($("#msgUncreadCount").html());
        $("#msgUncreadCount").html(unreadCount-1);
        $("#uncreadCount").html(unreadCount-1);
        $("#annunciateTitle").html("消息已撤销");
    }
    /**
     * 显示推送消息
     * @param annunciateMessage
     */
    function showMessage(annunciateMessage) {
        //消息类型
        let type = annunciateMessage.type;
        //annunciate的id，用于显示消息详情
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
        let unreadCount = "<div id='msgUncreadCount' style='float: right; margin:0 auto;' class='red-point cursorPointerTransform' onclick='javascript:showMessages("+id+",true)'>"+annunciateMessage.unreadCount+"</div>";
        //截取60个字
        var content = annunciateMessage.title.length > 50 ? annunciateMessage.title.substr(0, 60) +"......" : annunciateMessage.title;
        //组装内容和未读数html
        let msg = "<a id='annunciateTitle' style='width:246px;height:80px;float:left;' class='cursorPointer' onclick='javascript:showDetail("+id+", true, true)'>"+content+"</a>"+ unreadCount;
        //显示时间，默认3秒
        let timeout = annunciateMessage.timeout == null ? 3000 : annunciateMessage.timeout;
        $("#uncreadCount").html(annunciateMessage.unreadCount);
        //关闭前一个窗口
        if(messagerShowWinId != null){
            $("#"+messagerShowWinId).window("close");
        }
        messagerShowWinId = "messagerWin"+id;
        $.messager.show({
            id:messagerShowWinId,
            title:title,
            msg: msg,
            timeout:timeout,
            showSpeed:700,
            height:135,
            showType:'fade'
        });
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
    // function send() {
    //     var message = document.getElementById('text').value;
    //     websocket.send(message);
    // }
</script>