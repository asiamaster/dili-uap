<script type="text/javascript">
var ddValueEditIndex = undefined;

// 结束行编辑
function endDdValueGridEditing() {
	if (ddValueEditIndex == undefined) {
		return true
	}
	if (ddValueGrid.datagrid('validateRow', ddValueEditIndex)) {
		ddValueGrid.datagrid('endEdit', ddValueEditIndex);
		ddValueEditIndex = undefined;
		return true;
	} else {
		return false;
	}
}

function cancelEditDdValue() {
	if (!ddValueGrid) {
		return;
	}

	ddValueGrid.datagrid('cancelEdit', ddValueEditIndex);
	ddValueEditIndex = undefined;
}


function onBeginEditDdValue(index, row) {
	 <#resource code="updateDataDictionaryValue">
		var editors = ddValueGrid.datagrid('getEditors', index);
		editors[0].target.trigger('focus');
	    setOptValueBtnDisplay(true);
	</#resource>

}

function onAfterEditDdValue(index, row, changes) {
	var isValid = ddValueGrid.datagrid('validateRow', index);
	if (!isValid) {
		return false;
	}

	insertOrUpdateDdValue(index, row, changes);
	setOptValueBtnDisplay(false);
}

function onCancelEditDdValue(index, row) {
    setOptValueBtnDisplay(false);
	if (!row.id) {
		ddValueGrid.datagrid('deleteRow', index);
	}
}

// 打开新增窗口
function openInsertDdValue() {
	if (!endDdValueGridEditing()) {
		return;
	}

    $("#save_btn_ddValue").linkbutton('enable');
    $("#cancel_btn_ddValue").linkbutton('enable');
	ddValueEditIndex = ddValueGrid.datagrid('getRows').length;
	ddValueGrid.datagrid('appendRow', {
				type : 0
			});
	ddValueGrid.datagrid('selectRow', ddValueEditIndex);
	ddValueGrid.datagrid('beginEdit', ddValueEditIndex);
}

// 打开修改窗口
function openUpdateDdValue() {
	if (!endDdValueGridEditing()) {
		return;
	}

	var selected = ddValueGrid.datagrid("getSelected");
	if (!selected) {
		swal('警告！','请选中一条数据', 'warning');
		return;
	}
	var index = ddValueGrid.datagrid('getRowIndex', selected);
	if (endDdValueGridEditing()) {
        setOptValueBtnDisplay(true);
		ddValueGrid.datagrid('selectRow', index).datagrid('beginEdit', index);
		ddValueEditIndex = index;
	}
}

function onClickDdValueGridRow(index, row) {
    if (!ddValueGrid.datagrid('validateRow', ddValueEditIndex)) {
        return;
    }

    if (ddValueEditIndex != index) {
        ddValueGrid.datagrid('endEdit', ddValueEditIndex);
        ddValueEditIndex = undefined;
    }
}

function insertOrUpdateDdValue(index, row, changes) {
    var oldRecord;
    var url = contextPath + '/dataDictionaryValue/';
    if (!row.id) {
        row.ddCode = ddCode;
        url += 'insert.action';
    } else {
        oldRecord = new Object();
        $.extend(true, oldRecord, row);
        url += 'update.action'
    }
    $.post(url, row, function (data) {
        if (data.code != 200) {
            if (oldRecord) {
                ddValueGrid.datagrid('updateRow', {
                    index: index,
                    row: oldRecord
                });
            } else {
                ddValueGrid.datagrid('deleteRow', index);
            }
            swal('提示', data.result, 'error');
            return;
        }

        ddValueGrid.datagrid('updateRow', {
            index: index,
            row: row
        });
        ddValueGrid.datagrid('refreshRow', index);
    }, 'json');
}

// 根据主键删除
function delDdValue() {
    if (!ddValueGrid) {
        return;
    }

    var selected = ddValueGrid.datagrid("getSelected");
    if (null == selected) {
        swal('警告！','请选中一条数据', 'warning');
        return;
    }
    swal({
        title : '您确认想要删除记录吗？',
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
            url : "${contextPath}/dataDictionaryValue/delete.action",
            data : selected,
            processData : true,
            dataType : "json",
            async : true,
            success : function(data) {
                if (data.success) {
                    ddValueGrid.datagrid('deleteRow', ddValueGrid.datagrid('getRowIndex', selected));
                    $('#dlg').dialog('close');
                } else {
                    swal('修改失败！', '', 'error');
                }
            },
            error : function() {
                swal('错误！', data.result, 'error');
            }
        });
    });
}

// 表格查询
function queryDdValueGrid(selectedNode) {
	var opts = $('#ddValueGrid').datagrid("options");
	if (null == opts.url || "" == opts.url) {
		opts.url = "${contextPath}/dataDictionaryValue/listPage.action";
	}
	var param = bindMetadata("ddValueGrid", true);
	param.ddCode = window.ddCode;
	$('#ddValueGrid').datagrid("load", param);
}

function setOptValueBtnDisplay(show){
    var $btnSave = $("#save_btn_ddValue");
    var $btnCancel = $("#cancel_btn_ddValue");
    if(show){
        $btnSave.show();
        $btnCancel.show();
    }else{
        $btnSave.hide();
        $btnCancel.hide();
    }
}

/**
* 绑定页面回车事件，以及初始化页面时的光标定位
*
* @formId 表单ID
* @elementName 光标定位在指点表单元素的name属性的值
* @submitFun 表单提交需执行的任务
*/
$(function() {

	var pager = $('#ddValueGrid').datagrid('getPager'); // get the pager of treegrid
	pager.pagination({
		<#controls_paginationOpts/>
		,buttons:[
              <#resource code="insertDataDictionaryValue">
			{
				iconCls:'icon-add',
				text:'新增',
				handler:function(){
					openInsertDdValue();
				}
			},
			  </#resource>
            <#resource code="updateDataDictionaryValue">
			{
				iconCls:'icon-edit',
				text:'修改',
				handler:function(){
					openUpdateDdValue();
				}
			},
			  </#resource>
            <#resource code="deleteDataDictionaryValue">
			{
				iconCls:'icon-remove',
				text:'删除',
				handler:function(){
					delDdValue();
				}
			},
			</#resource>
            {
                id:'save_btn_ddValue',
                iconCls:'icon-ok',
                text:'保存',
                handler:function(){endDdValueGridEditing();}
            },
            {
                id:'cancel_btn_ddValue',
                iconCls:'icon-clear',
                text:'取消',
                handler:function(){cancelEditDdValue();}
            }
		]
	});
	//表格仅显示下边框
	$('#ddValueGrid').datagrid('getPanel').removeClass('lines-both lines-no lines-right lines-bottom').addClass("lines-bottom");
	queryDdValueGrid();
	setOptValueBtnDisplay(false);
});
</script>