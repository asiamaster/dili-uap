<script type="text/javascript">

    function openInsert() {
    	 <#resource code="insertDepartment">
        $("#grid").dataGridEditor().insert();
        </#resource>
    }

    function openUpdate() {
    	 <#resource code="updateDepartment">
        $("#grid").dataGridEditor().update();
        </#resource>
    }

    function del() {
    	<#resource code="deleteDepartment">
        	$("#grid").dataGridEditor().delete();
        </#resource>
    }

    function endEditing() {
        $("#grid").dataGridEditor().save();
    }

    function cancelEdit() {
        $("#grid").dataGridEditor().cancel();
    }
 // 是否显示编辑框的确定取消按钮
    function setOptBtnDisplay(show){
        var $btnSave = $("#btnSave");
        var $btnCancel = $("#btnCancel");
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
    $(function () {

        $("#grid").dataGridEditor({
            insertUrl: "${contextPath!}/department/insert.action",
            updateUrl: "${contextPath!}/department/update.action",
            deleteUrl: '${contextPath!}/department/delete.action',
            target: 'treegrid',
            onBeforeEdit: function () {
                $("#btnSave").show();
                $("#btnCancel").show();
            },
            onEndEdit: function () {
                $("#btnSave").hide();
                $("#btnCancel").hide();
            },
            extendParams: function (row) {
                return {
                    firmCode: window.firmCode,
                    parentId: row._parentId
                }
            },
            canEdit: function (row) {
              	 <#resource code="updateDepartment">
              	  if(1==1){
              		 return row.id.indexOf('firm_')<0;
              	  }
              	 </#resource>
              	 return false;  
            }
        });
        $('#grid').datagrid('getPanel').removeClass('lines-both lines-no lines-right lines-bottom').addClass("lines-bottom");
    });
</script>