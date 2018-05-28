<script type="text/javascript">
    //用户所拥有的市场信息
    var firms ='';
    <%if (has(firms)){%>
        firms = ${firms};
    <%}%>
    //编辑表格的索引
    var editIndex = undefined;
    // 结束行编辑
    function endEditing() {
        if (editIndex == undefined) {
            return true
        }
        if (roleGrid.datagrid('validateRow', editIndex)) {
            roleGrid.datagrid('endEdit', editIndex);
            editIndex = undefined;
            return true;
        } else {
            return false;
        }
    }

    /**
     * 判断是否真正编辑
     */
    function isEditing() {
        return undefined != editIndex;
    }

    /**
     * 取消行编辑
     */
    function cancelEdit() {
        roleGrid.datagrid('cancelEdit', editIndex);
        editIndex = undefined;
    };

    /**
     * 打开新增信息
     */
    function openInsert() {
        if (isEditing()){
            $.messager.alert('警告', '有数据正在进行编辑');
            roleGrid.datagrid('selectRow', editIndex);
            return;
        }
        if (endEditing()) {
            editIndex = roleGrid.datagrid('getRows').length;
            roleGrid.datagrid('appendRow', {id:''});
            roleGrid.datagrid('selectRow', editIndex);
            roleGrid.datagrid('beginEdit', editIndex);
        }
    }

    /**
     * 开启选中行的编辑模式
     */
    function openUpdate() {
        if (!endEditing()) {
            $.messager.alert('警告', '有数据正在进行编辑');
            roleGrid.datagrid('selectRow', editIndex);
            return;
        }
        var selected = roleGrid.datagrid("getSelected");
        if (!selected) {
            $.messager.alert('警告', '请选中一条数据');
            return;
        }
        var index = roleGrid.datagrid('getRowIndex', selected);
        if (endEditing()) {
            roleGrid.datagrid('selectRow', index).datagrid('beginEdit', index);
            editIndex = index;
        }
    }

    /**
     * 开始编辑行的回调函数
     * @param index 行索引
     * @param row 行数据
     */
    function onBeginEdit(index, row) {
        var editors = roleGrid.datagrid('getEditors', index);
        editors[0].target.trigger('focus');
        setOptBtnDisplay(true);
        //获取市场字段的编辑框
        var ed = roleGrid.datagrid('getEditor', {index:index,field:'firmCode'});
        //存在ID，数据编辑情况下
        if (row.id){
            $(ed.target).datebox('readonly');
            $(ed.target).datebox('disable');
        }else{
            //新增数据时，加载市场信息
            $(ed.target).combobox("loadData", firms);
        }
    }

    /**
     * 市场加载成功后，默认选中第一个
     */
    function firmLoadSuccess() {
        var ed = roleGrid.datagrid('getEditor', {index:editIndex,field:'firmCode'});
        var data = $(ed.target).combobox('getData');
        $(ed.target).combobox('select',data[0].code);
    }

    /**
     * 单击含回调方法，逻辑是结束之前的行编辑模式
     *
     * @param index
     * @param row
     */
    function onClickGridRow(index, row) {
        if (!roleGrid.datagrid('validateRow', editIndex)) {
            return;
        }
        if (editIndex != index) {
            roleGrid.datagrid('endEdit', editIndex);
            editIndex = undefined;
        }
    }

    /**
     * 结束编辑回调函数
     *
     * @param index 行索引
     * @param 行数据
     * @param  changes 当前行被修改的数据
     */
    function onAfterEdit(index, row, changes) {
        insertOrUpdate(index, row, changes);
        setOptBtnDisplay(false);
    }

    /**
     * 取消行编辑回调方法
     * @param  index
     * @param  row
     */
    function onCancelEdit(index, row) {
        setOptBtnDisplay(false);
        if (!row.id) {
            roleGrid.datagrid('deleteRow', index);
        }
    }

    /**
     * 是否显示修改保存-取消操作按钮
     * @param flag true-显示
     */
    function setOptBtnDisplay(flag) {
        if (flag || 'true'==flag){
           $('#save_btn').show();
           $('#cancel_btn').show();
        }else{
            $('#save_btn').hide();
            $('#cancel_btn').hide();
        }
    };

    /**
     * 插入或者修改信息

     * @param index 行索引
     * @param row 行数据
     * @param changes 被修改的数据
     */
    function insertOrUpdate(index, row, changes) {
        var oldRecord;
        var url = '${contextPath!}/role/';
        if (!row.id) {
            url += 'insert.action';
        } else {
            oldRecord = new Object();
            $.extend(true, oldRecord, row);
            url += 'update.action'
        }
        $.post(url, row, function(ret) {
            if (ret.code != 200) {
                if (oldRecord) {
                    roleGrid.datagrid('updateRow', {
                        index : index,
                        row : oldRecord
                    });
                } else {
                    roleGrid.datagrid('deleteRow', index);
                }
                $.messager.alert('提示', ret.result);
                return;
            }
            roleGrid.datagrid('load');
            roleGrid.datagrid('unselectAll');
            setOptBtnDisplay(false);
        }, 'json');
    };

    //表格表头右键菜单
    function headerContextMenu(e, field){
        e.preventDefault();
        if (!cmenu){
            createColumnMenu("roleGrid");
        }
        cmenu.menu('show', {
            left:e.pageX,
            top:e.pageY
        });
    }

    //全局按键事件
    function getKey(e){
        e = e || window.event;
        var keycode = e.which ? e.which : e.keyCode;
        if(keycode == 46){ //如果按下删除键
            var selected = $("#roleGrid").datagrid("getSelected");
            if(selected && selected!= null){
                del();
            }
        }
    };
    //根据主键删除
    function del() {
        var selected = roleGrid.datagrid("getSelected");
        if (null == selected) {
            $.messager.alert('警告','请选中一条数据');
            return;
        }
        $.messager.confirm('确认','您确认想要删除记录吗？',function(r){
            if (r){
                $.ajax({
                    type: "POST",
                    url: "${contextPath}/role/delete.action",
                    data: {id:selected.id},
                    processData:true,
                    dataType: "json",
                    async : true,
                    success: function (ret) {
                        if(ret.code=="200"){
                            roleGrid.datagrid('deleteRow',roleGrid.datagrid('getRowIndex',selected));
                        }else{
                            $.messager.alert('错误',ret.result);
                        }
                    },
                    error: function(){
                        $.messager.alert('错误','远程访问失败');
                    }
                });
            }
        });
    }
    /**
     * 绑定页面回车事件，以及初始化页面时的光标定位
     * @formId
     *          表单ID
     * @elementName
     *          光标定位在指点表单元素的name属性的值
     * @submitFun
     *          表单提交需执行的任务
     */
    $(function () {
        //角色grid对象
        window.roleGrid = $('#roleGrid');
        var obj={};
        obj.code='';
        obj.name='-- 请选择 --';
        //为了不改变原值，所以复制一遍数组
        var firmData = firms.slice();
        //动态添加'请选择'
        firmData.unshift(obj);
        $("#firmCode").combobox("loadData", firmData);
        bindFormEvent("form", "roleName", queryGrid);
        if (document.addEventListener) {
            document.addEventListener("keyup",getKey,false);
        } else if (document.attachEvent) {
            document.attachEvent("onkeyup",getKey);
        } else {
            document.onkeyup = getKey;
        }

        var pager = roleGrid.datagrid('getPager');
        pager.pagination({
            <#controls_paginationOpts/>,
            buttons:[
                {
                    iconCls:'icon-permission',
                    text:'权限',
                    handler:function(){
                        editRoleMenuAndResource();
                    }
                },
                {
                    iconCls:'icon-add',
                    text:'新增',
                    handler:function(){
                        openInsert();
                    }
                },
                {
                    iconCls:'icon-edit',
                    text:'修改',
                    handler:function(){
                        openUpdate();
                    }
                },
                {
                    iconCls:'icon-remove',
                    text:'删除',
                    handler:function(){
                        del();
                    }
                },
                {
                    iconCls:'icon-export',
                    text:'导出',
                    handler:function(){
                        doExport('roleGrid');
                    }
                },
                {
                    id:'save_btn',
                    iconCls:'icon-ok',
                    text:'保存',
                    handler:function(){
                        endEditing();
                    }
                },
                {
                    id:'cancel_btn',
                    iconCls:'icon-clear',
                    text:'取消',
                    handler:function(){
                        cancelEdit();
                    }
                }
            ]
        });
        //表格仅显示下边框
        roleGrid.datagrid('getPanel').removeClass('lines-both lines-no lines-right lines-bottom').addClass("lines-bottom");
        queryGrid();
        setOptBtnDisplay(false);
    });

    //表格查询
    function queryGrid() {
        var opts = roleGrid.datagrid("options");
        if (null == opts.url || "" == opts.url) {
            opts.url = "${contextPath}/role/listPage.action";
        }
        if(!$('#form').form("validate")){
            return;
        }
        roleGrid.datagrid("load", bindGridMeta2Form("roleGrid", "form"));
    };

    //清空表单
    function clearForm() {
        $('#form').form('clear');
    }

    /**
     * 打开权限设置页面
     */
    function editRoleMenuAndResource() {
        var selected = roleGrid.datagrid("getSelected");
        if (null == selected) {
            $.messager.alert('警告', '请选中一条数据');
            return;
        }
        $('#roleMenuAndResourceDlg').dialog('open');
        $('#roleMenuAndResourceDlg').dialog('center');
        var opts = $('#roleMenuAndResourceGrid').treegrid("options");
        opts.url = "${contextPath}/role/getRoleMenuAndResource.action?roleId=" + selected.id;
        $('#roleMenuAndResourceGrid').treegrid("clearChecked");
        $('#roleMenuAndResourceGrid').treegrid("load");
    }

    /**
     * 保存角色-菜单-资源新
     */
    function saveRoleMenuAndResource() {
        var nodes = $('#roleMenuAndResourceGrid').treegrid('getCheckedNodes');
        //节点选中的ID，包括系统、菜单、资源
        var ids = [];
        for (var i = 0; i < nodes.length; i++) {
            ids.push(nodes[i].treeId);
        }
        var selected = roleGrid.datagrid("getSelected");
        $.ajax({
            type: "POST",
            url: "${contextPath}/role/saveRoleMenuAndResource.action",
            data: {roleId: selected.id, resourceIds: ids},
            dataType: "json",
            traditional: true,
            async: true,
            success: function (ret) {
                if (ret.success) {
                    $('#roleMenuAndResourceDlg').dialog('close');
                } else {
                    $.messager.alert('错误', ret.result);
                }
            },
            error: function () {
                $.messager.alert('错误', '远程访问失败');
            }
        });
    }

</script>