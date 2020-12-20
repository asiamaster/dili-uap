<script type="application/javascript">

    // 是否是修改用户。目前用于判断用户编辑页面，部门数据的初始值显示问题
    var isUpdateUser = true;
    // 全部数据权限--true:全选,false:全不选
    var dataAuthButton1 = true;
    // 本部门数据权限--true:全选,false:全不选
    var dataAuthButton2 = true;
    // 部门及以下数据权限--true:全选,false:全不选
    var dataAuthButton3 = true;
    // 其他商户数据权限--true:全选,false:全不选
    var dataAuthButton4 = true;

    /**
	 * datagrid行点击事件 目前用于来判断 启禁用是否可点
	 */
    function onClickRow(index,row) {
        var state = row.$_state;
        if (state == ${@com.dili.uap.glossary.UserState.DISABLED.getCode()}){
            // 当用户状态为 禁用，可操作 启用
            $('#play_btn').linkbutton('enable');
            $('#stop_btn').linkbutton('disable');
            $('#unlock_btn').linkbutton('disable');
        }else if(state == ${@com.dili.uap.glossary.UserState.NORMAL.getCode()}){
            // 当用户状态为正常时，则只能操作 禁用
            $('#stop_btn').linkbutton('enable');
            $('#play_btn').linkbutton('disable');
            $('#unlock_btn').linkbutton('disable');
        }else if(state == ${@com.dili.uap.glossary.UserState.LOCKED.getCode()}){
            // 当用户状态为 锁定时，只能操作 解锁
            $('#play_btn').linkbutton('disable');
            $('#stop_btn').linkbutton('disable');
            $('#unlock_btn').linkbutton('enable');
        } else{
            // 其它情况，按钮不可用
            $('#stop_btn').linkbutton('disable');
            $('#play_btn').linkbutton('disable');
            $('#unlock_btn').linkbutton('disable');
        }
    }

    /**
	 * 禁启用操作
	 * 
	 * @param enable
	 *            是否启用:true-启用
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
            // var index = $('#userListGrid').datagrid("getRowIndex",
			// selectedUser);
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
    
    // 打开新增用户的窗口
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

    // 打开修改窗口
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
	 * 根据姓名自动加载邮箱信息 如果姓名为空，或者邮箱本身已有值，则不做更改
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
        var _formData = removeKeyStartWith($("#_form").serializeObject(true),"_");
        var _url = null;
        // 没有id就新增
        if(_formData.id == null || _formData.id==""){
            _url = "${contextPath}/user/insert.action";
        }else{// 有id就修改
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

    // 根据主键删除
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
        // 如果是窄边距(<1300像素)，使用工具栏
        var narrowWidth = document.body.clientWidth < 1300 ? true : false;
        if(narrowWidth){
            userGrid.datagrid({toolbar: "#toolbar"});
        }else{
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
        }
        // 表格仅显示下边框
        userGrid.datagrid('getPanel').removeClass('lines-both lines-no lines-right lines-bottom').addClass("lines-bottom");
    }
    // 全局按键事件
    function getKey(e){
        e = e || window.event;
        var keycode = e.which ? e.which : e.keyCode;
        if(keycode == 46){ // 如果按下删除键
            var selected = $("#userGrid").datagrid("getSelected");
            if(selected && selected!= null){
                del();
            }
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
        window.userGrid = $('#userGrid');
        /**
		 * 加载部门信息
		 */
        <% if (has(isGroup) && isGroup){ %>
            // var obj={code:"",name:'-- 全部 --'};
            // // 为了不改变原值，所以复制一遍数组
            // var firmData = firms.slice();
            // // 动态添加'请选择'
            // firmData.unshift(obj);
            // $("#firmCode").combobox("loadData", firmData);
            loadRoles(firmCode);
        <%}else{%>
            // loadDepartments(firmCode,'departmentId');
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
        //绑定搜索框键盘改变事件
        $("#searchTree").keyup(function(){
            var searchVal=$("#searchTree").val();
            $("#menuTree").tree("search", searchVal);
        });
    });

    // 表格查询
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
        // 节点选中的ID，包括 市场，角色
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

    	$("#dataTabs").tabs('select',0);
        var selected = userGrid.datagrid("getSelected");
        if (null == selected) {
            swal('警告', '请选中一条数据', 'warning');
            return;
        }
        //新打开一个权限编辑框重置全选/全不选按钮
        dataAuthButton1 = true;
        dataAuthButton2 = true;
        dataAuthButton3 = true;
        dataAuthButton4 = true;
        //先把之前编辑框勾选的清空
        $('#dataTree').tree("loadData",[]);
        $('#userDataDlg').dialog('open');
        $('#userDataDlg').dialog('center');
        $('#data_userName').textbox("setValue",selected.userName);
        /**
		 * 获取用户的数据权限 获取用户的数据范围选择项
		 */
        $.post('${contextPath!}/user/getUserData.action', {id: selected.id}, function (ret) {
            if (ret && ret.success) {
                // data 中存有 数据权限范围选项，数据权限本身，当前所属的数据权限
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
        <#resource code="projectDataAuth">
        $.post('${contextPath!}/user/getUserProjectData.action', {id: selected.id}, function (ret) {
            if (ret && ret.success) {
                // data 中存有 数据权限范围选项，数据权限本身，当前所属的数据权限
                var data = ret.data;
                $('#projectDateTree').tree("loadData", data.dataProject);
            }
         }, 'json');
         </#resource>
         $.post('${contextPath!}/user/getUserTradingDataAuth.action', {id: selected.id}, function (ret) {
            if (ret && ret.success) {
                // data 中存有 数据权限范围选项，数据权限本身，当前所属的数据权限
                var data = ret.data;
                $('#tradingHallTree').tree("loadData", data.tradingHallDataAuth);
            }
         }, 'json');
    }
    
    function onCascadeTreeLoadSuccess(){
    	var opts = $(this).tree("options");
        // 设置 关闭 级联检查，不然会默认勾选子节点中的未选中的数据
        opts.cascadeCheck = true;
    }
    

    /**
	 * 保存用户的角色信息
	 */
    function saveUserDatas() {
    	<#resource code="projectDataAuth">
    	var opts = $('#projectDateTree').tree("options");
        // 设置 关闭 级联检查，不然会默认勾选子节点中的未选中的数据
        opts.cascadeCheck = false;
        </#resource>
        var opts = $('#tradingHallTree').tree("options");
        // 设置 关闭 级联检查，不然会默认勾选子节点中的未选中的数据
        opts.cascadeCheck = false;
        var nodes = $('#dataTree').tree('getChecked');
        <#resource code="projectDataAuth">
        var nodes2 = $('#projectDateTree').tree('getChecked');
        </#resource>
         var nodes3 = $('#tradingHallTree').tree('getChecked');
        // 节点选中的ID，包括 市场，角色
        var ids = [];
        for (var i = 0; i < nodes.length; i++) {
            ids.push(nodes[i].id);
        }
        <#resource code="projectDataAuth">
        for (var i = 0; i < nodes2.length; i++) {
            ids.push(nodes2[i].id);
        }
        </#resource>
        for (var i = 0; i < nodes3.length; i++) {
            ids.push(nodes3[i].id);
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
                	swal('提示', ret.message, 'success');
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
	 * 
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

    /**
	 * 用户数据编辑时，上级数据加载成功后的执行方法
	 */
    function editSuperiorLoadSuccess(node,data) {
        var selected = userGrid.datagrid("getSelected");
        if (null == selected) {
            return;
        }
        // 如果是修改用户，则显示用户已有的职业数据
        if (isUpdateUser || 'true' == isUpdateUser){
            $('#_superiorId').combotree('setValue', selected.$_superiorId);
        }
    }

    /**
	 * 用户数据编辑时，职位数据加载成功后的执行方法
	 */
    function editPositionLoadSuccess(node,data) {
        var selected = userGrid.datagrid("getSelected");
        if (null == selected) {
            return;
        }
        // 如果是修改用户，则显示用户已有的职业数据
        if (isUpdateUser || 'true' == isUpdateUser){
            $('#_positionId').combotree('setValue', selected.$_positionId);
        }
    }


    /**
     * 勾选用户的数据权限
     * @param type 1:全部数据权限; 2:本部门数据权限; 3:部门及以下数据权限; 4:其他商户数据权限;
     */
    function selectUserDataAuth(type){
        var _tree = $('#dataTree');
        var roots = _tree.tree('getRoots');
        if(type == 1){
            for (var i = 0; i < roots.length; i++) {
                easyuiTreeChecked(_tree,dataAuthButton1,roots[i].id);
            }
            dataAuthButton1 = booleanChange(dataAuthButton1);
        }
        if(type == 2){
            var node = _tree.tree('find', departmentId);
            if(dataAuthButton2){
                _tree.tree('check', node.target);
            }else{
                _tree.tree('uncheck', node.target);
            }
            dataAuthButton2 = booleanChange(dataAuthButton2);
        }
        if(type == 3){
            easyuiTreeChecked(_tree,dataAuthButton3,departmentId);
            dataAuthButton3 = booleanChange(dataAuthButton3);
        }
        if(type == 4){
            for (var i = 0; i < roots.length; i++) {
                if("firm_"+firmCode==roots[i].id){
                    continue;
                }
                easyuiTreeChecked(_tree,dataAuthButton4,roots[i].id);
            }
            dataAuthButton4 = booleanChange(dataAuthButton4);
        }
    }

    /**
     * boolean值取反
     * @param value
     */
    function booleanChange(value){
        if(value){
            return false;
        }else{
            return true;
        }
    }

    /**
     * 树全选/全不选
     * @param  _tree 树
     * @param  ifCheck [是否选中true:全选，false:全不选]
     * @param  nodeId  [节点的id]
     */
    function easyuiTreeChecked(_tree,ifCheck, nodeId) {
        var node = _tree.tree('find', nodeId);
        if (ifCheck) {
            _tree.tree('check', node.target);
            var childrenNodes = _tree.tree('getChildren', node.target);
            for (var i = 0; i < childrenNodes.length; i++) {

                var childreNode = _tree.tree('find', childrenNodes[i].id);
                _tree.tree('check', childreNode.target);
                easyuiTreeChecked(_tree,ifCheck,childrenNodes[i].id);

            }
        } else {
            _tree.tree('uncheck', node.target);
            var childrenNodes = _tree.tree('getChildren', node.target);
            for (var i = 0; i < childrenNodes.length; i++) {

                var childreNode = _tree.tree('find', childrenNodes[i].id);
                _tree.tree('uncheck', childreNode.target);
                easyuiTreeChecked(_tree,ifCheck,childrenNodes[i].id);

            }
        }
    }

    function queryTree(node){
        var ids = node.id.split("_");
        var departmentId,firmCode;
        if(ids[0]=="firm"){
            firmCode = ids[1];
        }else if(ids[0]=="department"){
            departmentId = ids[1];
            firmCode = node.attributes.firmCode;
        }else{
            return;
        }
        // $('#firmCode').combobox('setValue', firmCode);
        // $('#departmentId').combotree('setValue', departmentId);
        $('#firmCode').val(firmCode);
        $('#departmentId').val(departmentId);
        loadRoles(firmCode);
        queryGrid();
    }

    /**
     * 1）扩展jquery easyui tree的节点检索方法。使用方法如下：
     * $("#treeId").tree("search", searchText);
     * 其中，treeId为easyui tree的根UL元素的ID，searchText为检索的文本。
     * 如果searchText为空或""，将恢复展示所有节点为正常状态
     */
    (function($) {
        $.extend($.fn.tree.methods, {
            /**
             * 扩展easyui tree的搜索方法
             * @param tree easyui tree的根DOM节点(UL节点)的jQuery对象
             * @param searchText 检索的文本
             * @param this-context easyui tree的tree对象
             */
            search: function(jqTree, searchText) {
                //easyui tree的tree对象。可以通过tree.methodName(jqTree)方式调用easyui tree的方法
                var tree = this;
                //获取所有的树节点
                var nodeList = getAllNodes(jqTree, tree);
                //如果没有搜索条件，则展示所有节点
                searchText = $.trim(searchText);
                if (searchText == "") {
                    for (var i=0; i<nodeList.length; i++) {
                        $(".tree-node-targeted", nodeList[i].target).removeClass("tree-node-targeted");
                        $(nodeList[i].target).show();
                    }
                    //展开所有节点
                    tree.expandAll(jqTree);
                    // 展开已选择的节点（如果之前选择了）
                    // var selectedNode = tree.getSelected(jqTree);
                    // if (selectedNode) {
                    //     tree.expandTo(jqTree, selectedNode.target);
                    // }
                    return;
                }
                //搜索匹配的节点并高亮显示
                var matchedNodeList = [];
                if (nodeList && nodeList.length>0) {
                    var node = null;
                    for (var i=0; i<nodeList.length; i++) {
                        node = nodeList[i];
                        var searchWord = node.text.split("(");
                        if (isMatch(searchText, searchWord[0])) {
                            matchedNodeList.push(node);
                        }
                    }
                    //隐藏所有节点
                    for (var i=0; i<nodeList.length; i++) {
                        $(".tree-node-targeted", nodeList[i].target).removeClass("tree-node-targeted");
                        $(nodeList[i].target).hide();
                    }
                    //折叠所有节点
                    tree.collapseAll(jqTree);
                    //展示所有匹配的节点以及父节点
                    for (var i=0; i<matchedNodeList.length; i++) {
                        showMatchedNode(jqTree, tree, matchedNodeList[i]);
                    }
                }
            },
            /**
             * 展示节点的子节点（子节点有可能在搜索的过程中被隐藏了）
             * @param node easyui tree节点
             */
            showChildren: function(jqTree, node) {
                //easyui tree的tree对象。可以通过tree.methodName(jqTree)方式调用easyui tree的方法
                var tree = this;
                //展示子节点
                if (!tree.isLeaf(jqTree, node.target)) {
                    var children = tree.getChildren(jqTree, node.target);
                    if (children && children.length>0) {
                        for (var i=0; i<children.length; i++) {
                            if ($(children[i].target).is(":hidden")) {
                                $(children[i].target).show();
                            }
                        }
                    }
                }
            },
            /**
             * 将滚动条滚动到指定的节点位置，使该节点可见（如果有滚动条才滚动，没有滚动条就不滚动）
             * @param param {
             *  treeContainer: easyui tree的容器（即存在滚动条的树容器）。如果为null，则取easyui tree的根UL节点的父节点。
             *  targetNode: 将要滚动到的easyui tree节点。如果targetNode为空，则默认滚动到当前已选中的节点，如果没有选中的节点，则不滚动
             * }
             */
            scrollTo: function(jqTree, param) {
                //easyui tree的tree对象。可以通过tree.methodName(jqTree)方式调用easyui tree的方法
                var tree = this;
                //如果node为空，则获取当前选中的node
                var targetNode = param && param.targetNode ? param.targetNode : tree.getSelected(jqTree);
                if (targetNode != null) {
                    //判断节点是否在可视区域
                    var root = tree.getRoot(jqTree);
                    var $targetNode = $(targetNode.target);
                    var container = param && param.treeContainer ? param.treeContainer : jqTree.parent();
                    var containerH = container.height();
                    var nodeOffsetHeight = $targetNode.offset().top - container.offset().top;
                    if (nodeOffsetHeight > (containerH - 30)) {
                        var scrollHeight = container.scrollTop() + nodeOffsetHeight - containerH + 30;
                        container.scrollTop(scrollHeight);
                    }
                }
            }
        });
        /**
         * 展示搜索匹配的节点
         */
        function showMatchedNode(jqTree, tree, node) {
            //展示所有父节点
            $(node.target).show();
            $(".tree-title", node.target).addClass("tree-node-targeted");
            var pNode = node;
            while ((pNode = tree.getParent(jqTree, pNode.target))) {
                $(pNode.target).show();
            }
            //展开到该节点
            tree.expandTo(jqTree, node.target);
            //如果是非叶子节点，需折叠该节点的所有子节点
            if (!tree.isLeaf(jqTree, node.target)) {
                tree.collapse(jqTree, node.target);
            }
        }
        /**
         * 判断searchText是否与targetText匹配
         * @param searchText 检索的文本
         * @param targetText 目标文本
         * @return true-检索的文本与目标文本匹配；否则为false.
         */
        function isMatch(searchText, targetText) {
            return $.trim(targetText)!="" && targetText.indexOf(searchText)!=-1;
        }
        /**
         * 获取easyui tree的所有node节点
         */
        function getAllNodes(jqTree, tree) {
            var allNodeList = jqTree.data("allNodeList");
            // if (!allNodeList) {
                var roots = tree.getRoots(jqTree);
                allNodeList = getChildNodeList(jqTree, tree, roots);
                jqTree.data("allNodeList", allNodeList);
            // }
            return allNodeList;
        }
        /**
         * 定义获取easyui tree的子节点的递归算法
         */
        function getChildNodeList(jqTree, tree, nodes) {
            var childNodeList = [];
            if (nodes && nodes.length>0) {
                var node = null;
                for (var i=0; i<nodes.length; i++) {
                    node = nodes[i];
                    childNodeList.push(node);
                    if (!tree.isLeaf(jqTree, node.target)) {
                        var children = tree.getChildren(jqTree, node.target);
                        childNodeList = childNodeList.concat(getChildNodeList(jqTree, tree, children));
                    }
                }
            }
            return childNodeList;
        }
    })(jQuery);

</script>