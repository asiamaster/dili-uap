var UAP_TOOLS = {
    getNowStr: function () {
        var now = new Date();
        return UAP_TOOLS.formatDateToString(now);
    },
    formatDateToString: function (dateObj) {
        var year = dateObj.getFullYear();
        var month = dateObj.getMonth()+1 ;
        var date = dateObj.getDate();
        
        var hour= dateObj.getHours();
        var minute = dateObj.getMinutes();
        var second = dateObj.getSeconds();
        
        var yearStr=year;
        var monthStr=month>= 10? month : '0' +month;
        var dateStr = date >= 10 ? date : '0' + date;
        
        var hourStr =hour >= 10 ? hour : '0' + hour;
        var minuteStr = minute >= 10 ? minute : '0' + minute;
        var secondStr = second >= 10 ? second : '0' +second;
        return yearStr + "-" + monthStr + "-" + dateStr + " " + hourStr + ":" + minuteStr + ":" + secondStr;
    }
};

$(function () {
    //去掉输入框中的前后空格
    $(document).on('change', 'input[type=text], textarea', function () {
        $(this).val($.trim($(this).val()));
    });
    //过滤某些特殊字符，不能被输入
    $(document).on('keydown', 'input[type=text], textarea', function (e) {
        var isContainsSpecialChar = /[(\`)(\~)(\^)(\<)(\>)(\$)(\—)]+/g;
        if (isContainsSpecialChar.test(e.key)) {
            e.preventDefault();
        }
    });

    /**
     * 扩展easyui的验证框架，可加入一些自定义的验证
     */
    $.extend($.fn.validatebox.defaults.rules, {
        //验证手机号规则
        phoneNum: {
            validator: function(value, param){
                return /^1[3-8]\d{9}$/.test(value);
            },
            message: '请输入正确的手机号码。'
        },
        //验证只能是中文汉字
        isChinese:{
            validator:function (value,param) {
                return  /^[\u4e00-\u9fa5]+$/.test(value);
            },
            message:'只能输入中文汉字'
        },
        //字符验证，只能包含中文、英文、数字、下划线等字符。
        isWord:{
            validator:function (value,param) {
                return /^[a-zA-Z0-9\u4e00-\u9fa5_]+$/.test(value);
            },
            message:'只能包含中文、英文、数字、下划线'
        },
        //验证银行卡号规则：目前只要求是数字，且长度在12-22位之间即可
        bankCard: {
            validator: function(value, param){
                return /^[1-9]\d{11,21}$/.test(value);
            },
            message: '请输入正确的卡号。'
        },
    });
})




