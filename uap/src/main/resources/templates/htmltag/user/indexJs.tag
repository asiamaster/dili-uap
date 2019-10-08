<script type="application/javascript">

    //是否是修改用户。目前用于判断用户编辑页面，部门数据的初始值显示问题
    var isUpdateUser = true;

    /**
     * datagrid行点击事件
     * 目前用于来判断 启禁用是否可点
     */
    function onClickRow(index,row) {
        var state = row.$_state;
        if (state == ${@com.dili.uap.glossary.UserState.DISABLED.getCode()}){
            //当用户状态为 禁用，可操作 启用
            $('#play_btn').linkbutton('enable');
            $('#stop_btn').linkbutton('disable');
            $('#unlock_btn').linkbutton('disable');
        }else if(state == ${@com.dili.uap.glossary.UserState.NORMAL.getCode()}){
            //当用户状态为正常时，则只能操作 禁用
            $('#stop_btn').linkbutton('enable');
            $('#play_btn').linkbutton('disable');
            $('#unlock_btn').linkbutton('disable');
        }else if(state == ${@com.dili.uap.glossary.UserState.LOCKED.getCode()}){
            //当用户状态为 锁定时，只能操作 解锁
            $('#play_btn').linkbutton('disable');
            $('#stop_btn').linkbutton('disable');
            $('#unlock_btn').linkbutton('enable');
        } else{
            //其它情况，按钮不可用
            $('#stop_btn').linkbutton('disable');
            $('#play_btn').linkbutton('disable');
            $('#unlock_btn').linkbutton('disable');
        }
    }

    /**
     * 禁启用操作
     * @param enable 是否启用:true-启用
     */
    function doEnable(enable) {
        var selected = userGrid.datagrid("getSelected");
        if (null == selected) {
            swal('警告','请选中一条数据', 'warning');
            return;
        }
        var msg = (enable || 'true' == enable) ? '账号被启用后，将可继续在各系统使用，请确认是否启用' : '账号被禁用后，将不可再继续在各系统使用，请确认是否禁用';
        msg = msg + '[' + selected.userName + ']？';
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
                url : "${contextPath}/user/doEnable.action",
                data : {id: selected.id, enable: enable},
                processData : true,
                dataType : "json",
                async : true,
                success : function(data) {
                    if (data.success) {
                        userGrid.datagrid("reload");
                        userGrid.datagrid("clearSelections");
                        $('#stop_btn').linkbutton('disable');
                        $('#play_btn').linkbutton('disable');
                        $('#unlock_btn').linkbutton('disable');
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
     * 解锁操作
     */
    function doUnlock() {
        var selected = userGrid.datagrid("getSelected");
        if (null == selected) {
            swal('警告', '请选中一条数据', 'warning');
            return;
        }
        var msg = '账号被解锁后，用户密码输入错误次数被清空，请确定是否解锁';
        msg = msg + '[' + selected.userName + ']？';
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
            // var index = $('#userListGrid').datagrid("getRowIndex", selectedUser);
            $.ajax({
                type: "POST",
                url: "${contextPath}/user/unlock.action",
                data: {id: selected.id},
                dataType: "json",
                async: true,
                success : function(data) {
                    if (data.success) {
                        userGrid.datagrid("reload");
                        userGrid.datagrid("clearSelections");
                        $('#stop_btn').linkbutton('disable');
                        $('#play_btn').linkbutton('disable');
                        $('#unlock_btn').linkbutton('disable');
                    } else {
                        swal('错误', data.result, 'error');
                    }
                },
                error : function() {
                    swal('错误！', '远程访问失败', 'error');
                }
            });
        });
    }

    /**
     * 查看用户信息
     */
    function openDetail() {
        var selected = userGrid.datagrid("getSelected");
        if (null == selected) {
            swal('警告', '请选中一条数据', 'warning');
            return;
        }
        $('#viewDlg').dialog('open');
        $('#viewDlg').dialog('center');
        var formData = $.extend({}, selected);
        formData = addKeyStartWith(formData, "_view_");
        $('#_view_form').form('clear');
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
        $("#_userName").textbox("enable");
        $("#_firmCode").textbox("enable");
        isUpdateUser = false;
    }

    //打开修改窗口
    function openUpdate(){

        <%if(!hasResource("updateUser")) {%>
            return;
        <%}%>

        var selected = userGrid.datagrid("getSelected");
        if (null == selected) {
            swal('警告', '请选中一条数据', 'warning');
            return;
        }
        $('#editDlg').dialog('open');
        $('#editDlg').dialog('center');
        formFocus("_form", "_userName");
        var formData = $.extend({}, selected);
        formData = addKeyStartWith(getOriginalData(formData), "_");
        $('#_form').form('clear');
        $('#_form').form('load', formData);
        $("#_userName").textbox("disable");
        // $("#_password").textbox("disable");
        $("#_firmCode").textbox("disable");
        $("#_firmCode").textbox("initValue", selected.firmCode);
        isUpdateUser = true;
    }

    /**
     * 重置密码
     */
    function resetPass() {
        var selected = userGrid.datagrid("getSelected");
        if (null == selected) {
            swal('警告', '请选中一条数据', 'warning');
            return;
        }
        var msg = '账号密码重置后将无法再通过旧密码登陆系统，请确认是否为用户[' + selected.userName + ']重置？';
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
                data : {id:selected.id},
                processData : true,
                dataType : "json",
                async : true,
                success : function(data) {
                    if (data.success) {
                        userGrid.datagrid("reload");
                        userGrid.datagrid("clearSelections");
                        $('#stop_btn').linkbutton('disable');
                        $('#play_btn').linkbutton('disable');
                        $('#unlock_btn').linkbutton('disable');
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
        $("#saveUser").linkbutton("disable");
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
                $("#saveUser").linkbutton("enable");
                if(ret.success){
                    userGrid.datagrid("reload");
                    $('#editDlg').dialog('close');
                }else{
                    swal('错误', ret.result, 'error');
                }
            },
            error: function(){
                $("#saveUser").linkbutton("enable");
                swal('错误', '远程访问失败', 'error');
            }
        });
    }

    //根据主键删除
    function del() {
        var selected = userGrid.datagrid("getSelected");
        if (null == selected) {
            swal('警告', '请选中一条数据', 'warning');
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
                url : "${contextPath}/user/delete.action",
                data : {id:selected.id},
                processData : true,
                dataType : "json",
                async : true,
                success : function(data) {
                    if (data.success) {
                        userGrid.datagrid("reload");
                    } else {
                        swal('错误', data.message, 'error');
                    }
                },
                error : function() {
                    swal('错误', '远程访问失败', 'error');
                }
            });
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
                <#resource code="editUserRole">
                {
                    iconCls:'icon-role',
                    text:'分配角色',
                    handler:function(){
                        editUserRole();
                    }
                },
                </#resource>
                <#resource code="editUserData">
                {
                    iconCls:'icon-data',
                    text:'数据权限',
                    handler:function(){
                        editUserDataAuth();
                    }
                },
                </#resource>
                <#resource code="viewUser">
                {
                    iconCls:'icon-detail',
                    text:'详情',
                    handler:function(){
                        openDetail();
                    }
                },
                </#resource>
                <#resource code="insertUser">
                {
                    iconCls:'icon-add',
                    text:'新增',
                    handler:function(){
                        openInsert();
                    }
                },
                </#resource>
                <#resource code="updateUser">
                {
                    iconCls:'icon-edit',
                    text:'修改',
                    handler:function(){
                        openUpdate();
                    }
                },
                </#resource>
                <#resource code="deleteUser">
                {
                    iconCls:'icon-remove',
                    text:'删除',
                    handler:function(){
                        del();
                    }
                },
                </#resource>
                <#resource code="resetPass">
                {
                    iconCls:'icon-reset',
                    text:'密码重置',
                    handler:function(){
                        resetPass();
                    }
                },
                </#resource>
                <#resource code="enabledUser">
                {
                    iconCls:'icon-play',
                    text:'启用',
                    id:'play_btn',
                    disabled:true,
                    handler:function(){
                        doEnable(true);
                    }
                },
                </#resource>
                <#resource code="disabledUser">
                {
                    iconCls:'icon-stop',
                    text:'禁用',
                    id:'stop_btn',
                    disabled:true,
                    handler:function(){
                        doEnable(false);
                    }
                },
                </#resource>
                <#resource code="unlockUser">
                {
                    iconCls:'icon-unlock',
                    text:'解锁',
                    id:'unlock_btn',
                    disabled:true,
                    handler:function(){
                        doUnlock();
                    }
                },
                </#resource>
                <#resource code="exportUser">
                {
                    iconCls:'icon-export',
                    text:'导出',
                    handler:function(){
                        doExport('userGrid');
                    }
                }
                </#resource>
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
        <% if (has(isGroup) && isGroup){ %>
            var obj={code:"",name:'-- 全部 --'};
            //为了不改变原值，所以复制一遍数组
            var firmData = firms.slice();
            //动态添加'请选择'
            firmData.unshift(obj);
            $("#firmCode").combobox("loadData", firmData);
        <%}else{%>
            loadDepartments(firmCode,'departmentId');
            loadRoles(firmCode);
        <%}%>
        
        bindFormEvent("form", "firmCode", queryGrid);
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
        userGrid.datagrid("load", bindGridMeta2Form("userGrid", "form"));
    }

    /**
     * 编辑用户的角色信息
     */
    function editUserRole() {
        var selected = userGrid.datagrid("getSelected");
        if (null == selected) {
            swal('警告', '请选中一条数据', 'warning');
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
                    swal('错误！', ret.result, 'error');
                }
            },
            error: function () {
                swal('错误！', '远程访问失败', 'error');
            }
        });
    }

    /**
     * 编辑用户的数据权限
     */
    function editUserDataAuth() {
        var selected = userGrid.datagrid("getSelected");
        if (null == selected) {
            swal('警告', '请选中一条数据', 'warning');
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
                $('#dataTree').tree("loadData", data.userDatas);
                var output = [];
                var checkedId = 1;
                if (typeof(data.currDataAuth) != "undefined" && '' != data.currDataAuth) {
                    checkedId = data.currDataAuth;
                }
                $.each(data.dataRange, function (i, item) {
                    if (parseInt(checkedId) == parseInt(item["value"])) {
                        output.push('<input type="radio" name="dataRange" checked value="' + item["value"] + '">' + item["name"] + '&nbsp;&nbsp;');
                    } else {
                        output.push('<input type="radio" name="dataRange" value="' + item["value"] + '">' + item["name"] + '&nbsp;&nbsp;');
                    }
                });
                $('#dataRangeDiv').html(output.join(''));
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
        var dataRange=$('input:radio[name="dataRange"]:checked').val();
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
                    swal('错误', ret.result, 'error');
                }
            },
            error: function () {
                swal('错误！', '远程访问失败', 'error');
            }
        });
    }

    /**
     * 用户编辑，真实姓名验证时的触发事件
     * @param v
     */
    function realNameValidate(v) {
        if (v || 'true' == v){
            getEmailByName();
        }
    }

    /**
     * 用户数据编辑时，部门数据加载成功后的执行方法
     */
    function editDepartmentLoadSuccess(node,data) {
        var selected = userGrid.datagrid("getSelected");
        if (null == selected) {
            return;
        }
        // 如果是修改用户，则显示用户已有的部门数据
        if (isUpdateUser || 'true' == isUpdateUser){
            $('#_departmentId').combotree('setValue', selected.$_departmentId);
        }
    }

</script>