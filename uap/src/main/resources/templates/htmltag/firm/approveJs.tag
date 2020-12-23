<script type="text/javascript">
var firmId = '${firm.id!}';


function approve() {
	$.messager.progress({
				msg : '正在加载数据......'
			});
	if (!$('#_form').form("validate")) {
		$.messager.progress('close');
		return;
	}
	$.messager.progress('close');
	var _formData = $("#_form").serializeObject();
	var _url = "${contextPath}/firm/approve.action";
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