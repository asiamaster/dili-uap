<script type="text/javascript">
var firmId = '${firm.id!}';

function saveOrUpdate() {
	$.messager.progress({
				msg : '正在加载数据......'
			});
	if (!$('#_form').form("validate")) {
		$.messager.progress('close');
		return;
	}
	$.messager.progress('close');
	if ($('#failureTime').datebox('getValue') < $('#effectTime').datebox('getValue')) {
		swal('错误', '生效日期不能大于失效日期', 'error');
		return;
	}
	var _formData = $("#_form").serializeObject();
	//判断是否为1，为给true，否则不处理
    if(_formData.longTermEffictive && _formData.longTermEffictive=='1'){
        _formData.longTermEffictive='true';
    }
	var _url = "${contextPath}/firm/update.action";
	$.ajax({
				type : "POST",
				url : _url,
				data : _formData,
				processData : true,
				dataType : "json",
				async : true,
				success : function(data) {
					if (data.code == "200") {
						window.location.href = '${contextPath!}/firm/index.html';
					} else {
						swal('错误', data.result, 'error');
						$.messager.progress('close');
					}
				},
				error : function() {
					swal('错误', '远程访问失败', 'error');
					$.messager.progress('close');
				}
			});
}

/**
 * 绑定页面回车事件，以及初始化页面时的光标定位
 * 
 * @formId 表单ID
 * @elementName 光标定位在指点表单元素的name属性的值
 * @submitFun 表单提交需执行的任务
 */
$(function() {
		});

</script>