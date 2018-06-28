<script type="text/javascript">
// 编辑行索引
var ddValueGrid = undefined;

function onBeforeLoad(){
	 setOptBtnDisplay(false);
}

// 表格查询
function queryDdGrid() {
	var opts = ddGrid.datagrid("options");
	if (null == opts.url || "" == opts.url) {
		opts.url = "${contextPath}/dataDictionary/listPage.action";
	}
	if (!$('#form').form("validate")) {
		return;
	}
	var param = bindMetadata("grid", true);
	var formData = $("#form").serializeObject();
	$.extend(formData, param);
	ddGrid.datagrid("load", formData);
}

// 全局按键事件
function getKey(e) {
	e = e || window.event;
	var keycode = e.which ? e.which : e.keyCode;
	switch (keycode) {
		case 46 :
			ddValueGrid ? delDdValue() : delDd();
			break;
		case 13 :
			ddValueGrid ? endDdValueGridEditing() : endDdGridEditing();
			break;
		case 27 :
			ddValueGrid ? cancelEditDdValue() : cancelDdGridEdit();
			break;
		case 38 :
			if (ddValueGrid) {
				if (!endDdValueGridEditing()) {
					return;
				}
				var selected = ddValueGrid.datagrid("getSelected");
				if (!selected) {
					return;
				}
				var selectedIndex = ddValueGrid.datagrid('getRowIndex', selected);
				if (selectedIndex <= 0) {
					return;
				}
				endDdValueGridEditing();
				ddValueGrid.datagrid('selectRow', --selectedIndex);
			} else if (ddGrid) {
				if (!endDdGridEditing()) {
					return;
				}
				var selected = ddGrid.datagrid("getSelected");
				if (!selected) {
					return;
				}
				var selectedIndex = ddGrid.datagrid('getRowIndex', selected);
				if (selectedIndex <= 0) {
					return;
				}
				endDdGridEditing();
				ddGrid.datagrid('selectRow', --selectedIndex);
			}
			break;
		case 40 :
			if (ddValueGrid) {
				if (!endDdGridEditing()) {
					return;
				}
				if (ddValueGrid.datagrid('getRows').length <= 0) {
					openInsertDdValue();
					return;
				}
				var selected = ddValueGrid.datagrid("getSelected");
				if (!selected) {
					ddValueGrid.datagrid('selectRow', 0);
					return;
				}
				var selectedIndex = ddValueGrid.datagrid('getRowIndex', selected);
				if (selectedIndex == ddValueGrid.datagrid('getRows').length - 1) {
					openInsertDdValue();
				} else {
					ddValueGrid.datagrid('selectRow', ++selectedIndex);
				}
			} else if (ddGrid) {
				if (!endDdGridEditing()) {
					return;
				}
				if (ddGrid.datagrid('getRows').length <= 0) {
					openInsertDd();
					return;
				}
				var selected = ddGrid.datagrid("getSelected");
				if (!selected) {
					ddGrid.datagrid('selectRow', 0);
					return;
				}
				var selectedIndex = ddGrid.datagrid('getRowIndex', selected);
				if (selectedIndex == ddGrid.datagrid('getRows').length - 1) {
					openInsertDd();
				} else {
					ddGrid.datagrid('selectRow', ++selectedIndex);
				}
			}
			break;
	}
}


function formatName(value, row, index) {
	return '<a href="javascript:void(0);" data-value="' + row.code + '" onclick="openDdValueWindow(this);">' + value + '</a>';
}


// 是否显示编辑框的确定取消按钮
function setOptBtnDisplay(show){
    var $btnSave = $("#save_btn");
    var $btnCancel = $("#cancel_btn");
    if(show){
        $btnSave.show();
        $btnCancel.show();
    }else{
        $btnSave.hide();
        $btnCancel.hide();
    }
}


function openDdValueWindow(obj) {
    var code = $(obj).attr("data-value");
	$('#win').window({
				title : '数据字典值列表',
				minimizable : false,
				maximizable : false,
				width : 1200,
				height : 500,
				modal : true,
				collapsible : false,
				href : '${contextPath}/dataDictionaryValue/list.html?ddCode=' + code,
				onLoad : function() {
					window.ddCode = code;
					ddValueGrid = $('#ddValueGrid');
				},
				onClose : function() {
					ddValueGrid = undefined;
				}
			});
}

// 清空表单
function clearForm() {
	$('#form').form('clear');
}

/**
 * 绑定页面回车事件，以及初始化页面时的光标定位
 *
 * @formId 表单ID
 * @elementName 光标定位在指点表单元素的name属性的值
 * @submitFun 表单提交需执行的任务
 */
$(function() {
			window.ddGrid = $('#grid');
			if (document.addEventListener) {
				document.addEventListener("keyup", getKey, false);
			} else if (document.attachEvent) {
				document.attachEvent("onkeyup", getKey);
			} else {
				document.onkeyup = getKey;
			}

    $("#grid").dataGridEditor({
        insertUrl: "${contextPath!}/dataDictionary/insert.action",
        updateUrl: "${contextPath!}/dataDictionary/update.action",
        deleteUrl: '${contextPath!}/dataDictionary/delete.action',
        onBeforeEdit: function () {
            setOptBtnDisplay(true)
        },
        onEndEdit: function () {
            setOptBtnDisplay(false);
        },canEdit:function(){
        	 <#resource code="updateDataDictionary">
        	  if(1==1){
        		  return true;  
        	  }
        	 </#resource>
        	 return false;
        }/*,
        onSaveSuccess: function (row, data) {
            row.created = UAP_TOOLS.getNowStr();
            row.modified = UAP_TOOLS.getNowStr();
            $("#grid").datagrid('updateRow', $("#grid").datagrid('getRowIndex', row), row);
            $("#grid").datagrid('refreshRow', $("#grid").datagrid('getRowIndex', row));
        }*/
    });

			var pager = $('#grid').datagrid('getPager');    // get the pager of treegrid
			pager.pagination({
				<#controls_paginationOpts/>,
				buttons:[
					 <#resource code="insertDataDictionary">
					{
						iconCls:'icon-add',
						text:'新增',
						handler:function(){
                            $("#grid").dataGridEditor().insert();
						}
					},
					  </#resource>
	                <#resource code="updateDataDictionary">
					{
						iconCls:'icon-edit',
						text:'修改',
						handler:function(){
                            $("#grid").dataGridEditor().update();
						}
					},
					  </#resource>
	                <#resource code="deleteDataDictionary">
					{
						iconCls:'icon-remove',
						text:'删除',
						handler:function(){
                            $("#grid").dataGridEditor().delete();
						}
					},
					</#resource>
					{
						id:'save_btn',
                        text:'保存',
						iconCls:'icon-ok',
						handler:function(){  $("#grid").dataGridEditor().save();}
					},
                    {
                        id:'cancel_btn',
                        text:'取消',
                        iconCls:'icon-clear',
                        handler:function(){  $("#grid").dataGridEditor().cancel();}
                    }
				]
			});
			//表格仅显示下边框
			$('#grid').datagrid('getPanel').removeClass('lines-both lines-no lines-right lines-bottom').addClass("lines-bottom");
			queryDdGrid();
			setOptBtnDisplay(false);
		});
</script>