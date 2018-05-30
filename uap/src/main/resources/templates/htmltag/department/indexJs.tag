<script type="text/javascript">

    function openInsert() {
        $("#grid").dataGridEditor().insert();
    }

    function openUpdate() {
        $("#grid").dataGridEditor().update();
    }

    function del() {
        $("#grid").dataGridEditor().delete();
    }

    function endEditing() {
        $("#grid").dataGridEditor().save();
    }

    function cancelEdit() {
        $("#grid").dataGridEditor().cancel();
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
            onAfterEdit: function () {
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
                return row.id !== -1;
            }
        });
    });
</script>