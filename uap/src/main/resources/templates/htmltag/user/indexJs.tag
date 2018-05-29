<script type="application/javascript">
    //用户所拥有的市场信息
    var firms ='';
    <%if (has(firms)){%>
        firms = ${firms};
    <%}%>

    var firmCode = '${firmCode!}'

    //打开新增用户的窗口
    function openInsert(){
        $('#dlg').dialog('open');
        $('#dlg').dialog('center');
        $('#_form').form('clear');
        formFocus("_form", "_userName");
        $('#_firmCode').combobox("loadData", firms);
        $('#_password').textbox('setValue','${defaultPass}');
        $('#_password').passwordbox('hidePassword');
        $('#resetPass').hide();
    }
    
    /**
     * 根据市场code加载部门信息，并显示到相应的部门控件中
     * @param firmCode 市场code
     */
    function loadDepartments(firmCode,controlId) {
        $.post('${contextPath!}/department/listByCondition.action', {firmCode:firmCode}, function(ret) {
            if (ret) {
                var obj={id:'',name:'-- 请选择 --'};
                //动态添加'请选择'
                ret.unshift(obj);
                $('#'+controlId).combobox("loadData", ret);
            }
        }, 'json');
    }

    /**
     * 根据市场code加载角色信息
     * @param firmCode 市场code
     */
    function loadRoles(firmCode) {
        $.post('${contextPath!}/role/list.action', {firmCode:firmCode}, function(ret) {
            if (ret) {
                var obj={id:'',roleName:'-- 请选择 --'};
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

    }

    /**
     * 根据姓名自动加载邮箱信息
     * 如果姓名为空，或者邮箱本身已有值，则不做更改
     */
    function getEmailByName() {
        debugger;
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
                    $('#dlg').dialog('close');
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
        bindFormEvent("_form", "_userName", saveOrUpdate, function (){$('#dlg').dialog('close');});
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
    
</script>