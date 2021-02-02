<script type="text/javascript">
/**
 * 绑定页面回车事件，以及初始化页面时的光标定位
 * 
 * @formId 表单ID
 * @elementName 光标定位在指点表单元素的name属性的值
 * @submitFun 表单提交需执行的任务
 */
$(function() {
    editRoleMenuAndResource();
    spaceHidden();

});

function selectAll(){
	 var datas = $("#roleMenuAndResourceGrid").treegrid("getData");
	 $(datas).each(function(index,item){
        $("#roleMenuAndResourceGrid").treegrid("checkNode", item.treeId);	 	
	 });
}

function disselectAll(){
	var datas = $("#roleMenuAndResourceGrid").treegrid("getData");
     $(datas).each(function(index,item){
        $("#roleMenuAndResourceGrid").treegrid("uncheckNode", item.treeId);       
     });
}

/**
 * 打开权限设置页面
 */
function editRoleMenuAndResource() {
	$('#roleMenuAndResourceGrid').treegrid("clearChecked");
	$('#roleMenuAndResourceGrid').treegrid("clearSelections");
	var opts = $('#roleMenuAndResourceGrid').treegrid("options");
	// 设置 关闭 级联检查，不然会默认勾选子节点中的未选中的数据
	opts.cascadeCheck = false;
	if (null == opts.url || "" == opts.url) {
		opts.url = "${contextPath}/role/getRoleMenuAndResource.action";
	}
	$('#roleMenuAndResourceGrid').treegrid("load", {
				roleId : $('#roleId').val()
			});
}

// 重置密码
function resetPassword() {
    var paramArray = $('#_form').serializeArray();
    var userId = paramArray[1].value;
    if (userId == '' || userId == null || typeof(userId) == 'undefined' ) {
        swal('警告', '该市场下还没有设置管理员，清先设置管理员', 'warning');
        return;
    }
    var msg = '账号密码重置后将无法再通过旧密码登陆系统，请确认是否要重置密码？';
    swal({
        title : msg,
        type : 'question',
        showCancelButton : true,
        confirmButtonColor : '#3085d6',
        cancelButtonColor : '#d33',
        confirmButtonText : '确定',
        cancelButtonText : '取消',
        confirmButtonClass : 'btn btn-success',
        cancelButtonClass : 'btn btn-danger'
    }).then(function(flag) {
        if (flag.dismiss == 'cancel') {
            return;
        }
        $.ajax({
            type : "POST",
            url : "${contextPath}/user/resetPass.action",
            data : {id:userId},
            processData : true,
            dataType : "json",
            async : true,
            success : function(data) {
                if (data.success) {
                    swal('提示', data.result, 'success');
                } else {
                    swal('错误', data.result, 'error');
                }
            },
            error : function() {
                swal('错误', '远程访问失败', 'error');
            }
        });
    });

}

/**
 * 保存角色-菜单-资源新
 */
function saveOrUpdate() {
	$.messager.progress({
				msg : '正在加载数据......'
			});
	if (!$('#_form').form("validate")) {
		$.messager.progress('close');
		return;
	}
	$.messager.progress('close');
	// 获取被选中的节点 此方法不会获取"半选"状态值
	var nodes = $('#roleMenuAndResourceGrid').treegrid('getCheckedNodes');
	// 节点选中的ID，包括系统、菜单、资源
	var ids = [];
	for (var i = 0; i < nodes.length; i++) {
		ids.push(nodes[i].treeId);
	}
	/**
	 * node中，有checkState状态，选中：'true',半选：'indeterminate',可是。目前版本中没有像tree那样通过状态获取值的方法
	 * 通过css获取半选状态节点 tree-checkbox2 有子节点被选中的css tree-checkbox1 节点被选中的css
	 * tree-checkbox0 节点未选中的css
	 */
	var childCheckNodes = $('#roleMenuAndResourceGrid').treegrid("getPanel").find(".tree-checkbox2");
	for (var i = 0; i < childCheckNodes.length; i++) {
		ids.push($($(childCheckNodes[i]).closest('tr')[0]).attr("node-id"));
	}
	var paramArray = $('#_form').serializeArray();
	/* 请求参数转json对象 */

	var jsonObj = {};
	$(paramArray).each(function() {
				jsonObj[this.name] = this.value;
			});
	jsonObj.resourceIds = ids;
	// json对象再转换成json字符串
	$.ajax({
				type : "POST",
				url : "${contextPath}/firm/editAdminUser.action",
				contentType : 'application/json',
				data : JSON.stringify(jsonObj),
				dataType : "json",
				traditional : true,
				async : true,
				success : function(ret) {
					$.messager.progress('close');
					if (ret.success) {
						swal('提示', ret.result, 'success');
						window.location.href = '${contextPath!}/firm/index.html';
					} else {
						swal('错误', ret.result, 'error');
					}
				},
				error : function() {
					$.messager.progress('close');
					swal('错误', '远程访问失败', 'error');
				}
			});
}

function roleMenuTreeLoadsuccess() {
	var opts = $('#roleMenuAndResourceGrid').treegrid("options");
	opts.cascadeCheck = true;
}

$(function(){
    $.extend($.fn.validatebox.defaults.rules, {
        checkUserName : {// 验证用户名
            validator : function(value) {
                return /^[A-Za-z0-9_\u4e00-\u9fa5]+$/gi.test(value);
            },
            message : '只能包含中文、英文、数字、下划线'
        }
    });
});
$(function(){
    $.extend($.fn.validatebox.defaults.rules, {
        checkCellphone : {// 验证手机号
            validator : function(value) {
                return /^[0-9]+$/gi.test(value);
            },
            message : '只允许输入数字'
        }
    });
});
// 隐藏空格
function spaceHidden(){
    var serialNumber = document.getElementById('serialNumber').innerHTML;
    if (serialNumber == '' || serialNumber == null || typeof(serialNumber) == 'undefined' ) {
        $("#space").hide();
    }
}
</script>