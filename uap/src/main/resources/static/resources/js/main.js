var UAP_TOOLS = {
    getNowStr: function () {
        var now = new Date();
        var year = now.getFullYear();
        var month = now.getMonth() > 9 ? now.getMonth() + 1 : '0' + (now.getMonth() + 1);
        var date = now.getDate();
        var hour = now.getHours() > 10 ? now.getHours() : '0' + now.getHours();
        var minute = now.getMinutes() > 10 ? now.getMinutes() : '0' + now.getMinutes();
        var second = now.getSeconds() > 10 ? now.getSeconds() : '0' + now.getSeconds();
        return year + "-" + month + "-" + date + " " + hour + ":" + minute + ":" + second;
    }
};

$(function () {
    /**
     * 扩展验证框架
     */
    $.extend($.fn.validatebox.defaults.rules, {
        //验证手机号
        phoneNum: {
            validator: function(value, param){
                return /^1[3-8]+\d{9}$/.test(value);
            },
            message: '请输入正确的手机号码。'
        }
    });
})

