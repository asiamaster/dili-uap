<script type="application/javascript">
    //用户所拥有的市场信息
    var firms ='';
    <%if (has(firms)){%>
        firms = ${firms};
    <%}%>
    //用户当前的所属市场
    var firmCode = '${firmCode!}'

    /**
     * datagrid行点击事件
     * 目前用于来判断 启禁用是否可点
     */
    function onClickRow(index,row) {
        var state = row.$_state;
        if (state == ${@com.dili.uap.glossary.UserState.DISABLED.getCode()}
            || state == ${@com.dili.uap.glossary.UserState.LOCKED.getCode()}){
            //当用户状态为 禁用、锁定时，可操作 启用
            $('#play_btn').linkbutton('enable');
            $('#stop_btn').linkbutton('disable');
        }else if(state == ${@com.dili.uap.glossary.UserState.NORMAL.getCode()}){
            //当用户状态为正常时，则只能操作 禁用
            $('#stop_btn').linkbutton('enable');
            $('#play_btn').linkbutton('disable');
        }else{
            //其它情况，按钮不可用
            $('#stop_btn').linkbutton('disable');
            $('#play_btn').linkbutton('disable');
        }
    }

    /**
     * 禁启用操作
     * @param enable 是否启用:true-启用
     */
    function doEnable(enable) {
        var selected = userGrid.datagrid("getSelected");
        if (null == selected) {
            $.messager.alert('警告', '请选中一条数据');
            return;
        }
        var msg = (enable || 'true' == enable) ? '启用' : '禁用';
        msg = msg + '[' + selected.userName + ']';
        $.messager.confirm('确认', '您确认要' + msg + '吗？', function (r) {
            if (r) {
                $.ajax({
                    type: "POST",
                    url: "${contextPath}/user/doEnable.action",
                    data: {id: selected.id, enable: enable},
                    processData: true,
                    dataType: "json",
                    async: true,
                    success: function (ret) {
                        if (ret.success) {
                            userGrid.datagrid("reload");
                            userGrid.datagrid("clearSelections");
                            $('#stop_btn').linkbutton('disable');
                            $('#play_btn').linkbutton('disable');
                        } else {
                            $.messager.alert('错误', ret.result);
                        }
                    },
                    error: function () {
                        $.messager.alert('错误', '远程访问失败');
                    }
                });
            }
        });
    }

    /**
     * 查看用户信息
     */
    function openDetail() {
        var selected = userGrid.datagrid("getSelected");
        if (null == selected) {
            $.messager.alert('警告', '请选中一条数据');
            return;
        }
        $('#viewDlg').dialog('open');
        $('#viewDlg').dialog('center');
        var formData = $.extend({}, selected);
        formData = addKeyStartWith(formData, "_view_");
        $('#_view_form').form('load', formData);
        $('#viewDlg input[class^=easyui-]').textbox("readonly", true);
        $('#viewDlg input[class^=easyui-]').textbox("editable", false);

    }
    
    //打开新增用户的窗口
    function openInsert(){
        $('#editDlg').dialog('open');
        $('#editDlg').dialog('center');
        $('#_form').form('clear');
        formFocus("_form", "_userName");
        $('#_firmCode').combobox("loadData", firms);
        $('#_password').textbox('setValue', '${defaultPass}');
        $('#_password').passwordbox('hidePassword');
        $("#_userName").textbox("enable");
        $("#_password").textbox("enable");
        $("#_firmCode").textbox("enable");
    }

    //打开修改窗口
    function openUpdate(){
        var selected = userGrid.datagrid("getSelected");
        if (null == selected) {
            $.messager.alert('警告', '请选中一条数据');
            return;
        }
        $('#editDlg').dialog('open');
        $('#editDlg').dialog('center');
        formFocus("_form", "_userName");
        var formData = $.extend({}, selected);
        formData = addKeyStartWith(getOriginalData(formData), "_");
        $('#_form').form('load', formData);
        $("#_userName").textbox("disable");
        $("#_password").textbox("disable");
        $("#_firmCode").textbox("disable");
        $("#_firmCode").textbox("initValue", selected.firmCode);
    }
    
    
    /**
     * 根据市场code加载部门信息，并显示到相应的部门控件中
     * @param firmCode 市场code
     */
    function loadDepartments(firmCode,controlId) {
        $.post('${contextPath!}/department/listByCondition.action', {firmCode: firmCode}, function (ret) {
            if (ret) {
                var obj = {id: '', name: '-- 请选择 --'};
                //动态添加'请选择'
                ret.unshift(obj);
                $('#' + controlId).combobox("loadData", ret);
            }
        }, 'json');
    }

    /**
     * 根据市场code加载角色信息
     * @param firmCode 市场code
     */
    function loadRoles(firmCode) {
        $.post('${contextPath!}/role/list.action', {firmCode: firmCode}, function (ret) {
            if (ret) {
                var obj = {id: '', roleName: '-- 请选择 --'};
                //动态添加'请选择'
                ret.unshift(obj);
                $('#roleId').combobox("loadData", ret);
            }
        }, 'json');
    }

    /**
     * 重置密码
     */
    function resetPass() {
        var selected = userGrid.datagrid("getSelected");
        if (null == selected) {
            $.messager.alert('警告','请选中一条数据');
            return;
        }
        var msg = '账号密码重置后将无法再通过旧密码登陆系统，请确认是否为用户[' + selected.userName + ']重置？';
        $.messager.confirm('确认', msg, function (r) {
            if (r) {
                $.ajax({
                    type: "POST",
                    url: "${contextPath}/user/resetPass.action",
                    data: {id: selected.id},
                    processData: true,
                    dataType: "json",
                    async: true,
                    success: function (ret) {
                        if (ret.success) {
                            userGrid.datagrid("reload");
                            userGrid.datagrid("clearSelections");
                            $('#stop_btn').linkbutton('disable');
                            $('#play_btn').linkbutton('disable');
                        } else {
                            $.messager.alert('错误', ret.result);
                        }
                    },
                    error: function () {
                        $.messager.alert('错误', '远程访问失败');
                    }
                });
            }
        });
    }

    /**
     * 根据姓名自动加载邮箱信息
     * 如果姓名为空，或者邮箱本身已有值，则不做更改
     */
    function getEmailByName() {
        var name = $('#_realName').textbox("getValue");
        if (null == name || '' == $.trim(name)) {
            return;
        }
        var email = $('#_email').textbox("getValue");
        if (null == email || '' == $.trim(email)) {
            $.post('${contextPath!}/user/getEmailByName.action', {name: name}, function (ret) {
                if (ret.success) {
                    $('#_email').textbox("setValue", ret.data);
                }
            }, 'json');
        }
    }

    /**
     * 保存或修改数据
     */
    function saveOrUpdate(){
        if(!$('#_form').form("validate")){
            return;
        }
        var _formData = removeKeyStartWith($("#_form").serializeObject(),"_");
        var _url = null;
        //没有id就新增
        if(_formData.id == null || _formData.id==""){
            _url = "${contextPath}/user/insert.action";
        }else{//有id就修改
            _url = "${contextPath}/user/update.action";
        }
        $.ajax({
            type: "POST",
            url: _url,
            data: _formData,
            processData:true,
            dataType: "json",
            async : true,
            success: function (ret) {
                if(ret.success){
                    userGrid.datagrid("reload");
                    $('#editDlg').dialog('close');
                }else{
                    $.messager.alert('错误',ret.result);
                }
            },
            error: function(){
                $.messager.alert('错误','远程访问失败');
            }
        });
    }

    //根据主键删除
    function del() {
        var selected = userGrid.datagrid("getSelected");
        if (null == selected) {
            $.messager.alert('警告','请选中一条数据');
            return;
        }
        $.messager.confirm('确认','您确认想要删除记录吗？',function(r){
            if (r){
                $.ajax({
                    type: "POST",
                    url: "${contextPath}/user/delete",
                    data: {id:selected.id},
                    processData:true,
                    dataType: "json",
                    async : true,
                    success: function (ret) {
                        if(ret.success){
                            userGrid.datagrid("reload");
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
     * 初始化用户列表组件
     */
    function initUserGrid() {
        var pager = userGrid.datagrid('getPager');
        pager.pagination({
            <#controls_paginationOpts/>,
            buttons:[
                {
                    iconCls:'icon-role',
                    text:'分配角色',
                    handler:function(){
                        editUserRole();
                    }
                },
                {
                    iconCls:'icon-data',
                    text:'数据权限',
                    handler:function(){
                        editUserDataAuth();
                    }
                },
                {
                    iconCls:'icon-detail',
                    text:'详情',
                    handler:function(){
                        openDetail();
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
                    iconCls:'icon-reset',
                    text:'密码重置',
                    handler:function(){
                        resetPass();
                    }
                },
                {
                    iconCls:'icon-play',
                    text:'启用',
                    id:'play_btn',
                    disabled:true,
                    handler:function(){
                        doEnable(true);
                    }
                },
                {
                    iconCls:'icon-stop',
                    text:'禁用',
                    id:'stop_btn',
                    disabled:true,
                    handler:function(){
                        doEnable(false);
                    }
                },
                {
                    iconCls:'icon-export',
                    text:'导出',
                    handler:function(){
                        doExport('roleGrid');
                    }
                }
            ]
        });
        //表格仅显示下边框
        userGrid.datagrid('getPanel').removeClass('lines-both lines-no lines-right lines-bottom').addClass("lines-bottom");
    }
    //全局按键事件
    function getKey(e){
        e = e || window.event;
        var keycode = e.which ? e.which : e.keyCode;
        if(keycode == 46){ //如果按下删除键
            var selected = $("#userGrid").datagrid("getSelected");
            if(selected && selected!= null){
                del();
            }
        }
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
        window.userGrid = $('#userGrid');
        /**
         * 加载部门信息
         */
        <% if (has(firms)){ %>
            var obj={code:'',name:'-- 请选择 --'};
            //为了不改变原值，所以复制一遍数组
            var firmData = firms.slice();
            //动态添加'请选择'
            firmData.unshift(obj);
            $("#firmCode").combobox("loadData", firmData);
        <%}else{%>
           loadDepartments(firmCode,'departmentId');
        <%}%>
        
        bindFormEvent("form", "userName", queryGrid);
        bindFormEvent("_form", "_userName", saveOrUpdate, function (){$('#editDlg').dialog('close');});
        if (document.addEventListener) {
            document.addEventListener("keyup",getKey,false);
        } else if (document.attachEvent) {
            document.attachEvent("onkeyup",getKey);
        } else {
            document.onkeyup = getKey;
        }
        initUserGrid();
        queryGrid();
    });

    //表格查询
    function queryGrid() {
        var opts = userGrid.datagrid("options");
        if (null == opts.url || "" == opts.url) {
            opts.url = "${contextPath}/user/listPage.action";
        }
        if(!$('#form').form("validate")){
            return;
        }
        userGrid.datagrid("load", bindGridMeta2Form("userGrid", "form"));
    }

    //表格表头右键菜单
    function headerContextMenu(e, field){
        e.preventDefault();
        if (!cmenu){
            createColumnMenu("userGrid");
        }
        cmenu.menu('show', {
            left:e.pageX,
            top:e.pageY
        });
    }

    //清空表单
    function clearForm() {
        $('#form').form('clear');
    }

    /**
     * 编辑用户的角色信息
     */
    function editUserRole() {
        var selected = userGrid.datagrid("getSelected");
        if (null == selected) {
            $.messager.alert('警告', '请选中一条数据');
            return;
        }
        $('#userRoleDlg').dialog('open');
        $('#userRoleDlg').dialog('center');
        $('#role_userName').textbox("setValue",selected.userName);
        var opts = $('#roleTree').tree("options");
        opts.url = "${contextPath}/user/getUserRolesForTree.action?id=" + selected.id;
        $('#roleTree').tree("reload");
        
    }

    /**
     * 保存用户的角色信息
     */
    function saveUserRoles() {
        var nodes = $('#roleTree').tree('getChecked');
        //节点选中的ID，包括 市场，角色
        var ids = [];
        for (var i = 0; i < nodes.length; i++) {
            ids.push(nodes[i].id);
        }
        var selected = userGrid.datagrid("getSelected");
        $.ajax({
            type: "POST",
            url: "${contextPath}/user/saveUserRoles.action",
            data: {userId: selected.id, roleIds: ids},
            dataType: "json",
            traditional: true,
            async: true,
            success: function (ret) {
                if (ret.success) {
                    $('#userRoleDlg').dialog('close');
                    userGrid.datagrid("load");
                } else {
                    $.messager.alert('错误', ret.result);
                }
            },
            error: function () {
                $.messager.alert('错误', '远程访问失败');
            }
        });
    }

    /**
     * 编辑用户的数据权限
     */
    function editUserDataAuth() {
        var selected = userGrid.datagrid("getSelected");
        if (null == selected) {
            $.messager.alert('警告', '请选中一条数据');
            return;
        }
        $('#userDataDlg').dialog('open');
        $('#userDataDlg').dialog('center');
        $('#data_userName').textbox("setValue",selected.userName);
        /**
         * 获取用户的数据权限
         * 获取用户的数据范围选择项
         */
        $.post('${contextPath!}/user/getUserData.action', {id: selected.id}, function (ret) {
            if (ret && ret.success) {
                //data 中存有 数据权限范围选项，数据权限本身，当前所属的数据权限
                var data = ret.data;
                $('#dataRange').combobox("loadData", data.dataRange);
                $('#dataTree').tree("loadData", data.userDatas);
                if (data.currDataAuth) {
                    $('#dataRange').combobox('select', data.currDataAuth);
                } else {
                    $('#dataRange').combobox('select', data.dataRange[0].id);
                }
            }
        }, 'json');
    }

    /**
     * 保存用户的角色信息
     */
    function saveUserDatas() {
        var nodes = $('#dataTree').tree('getChecked');
        //节点选中的ID，包括 市场，角色
        var ids = [];
        for (var i = 0; i < nodes.length; i++) {
            ids.push(nodes[i].id);
        }
        var dataRange = $('#dataRange').combobox("getValue");
        var selected = userGrid.datagrid("getSelected");
        $.ajax({
            type: "POST",
            url: "${contextPath}/user/saveUserDatas.action",
            data: {userId: selected.id, dataIds: ids,dataRange:dataRange},
            dataType: "json",
            traditional: true,
            async: true,
            success: function (ret) {
                if (ret.success) {
                    $('#userDataDlg').dialog('close');
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