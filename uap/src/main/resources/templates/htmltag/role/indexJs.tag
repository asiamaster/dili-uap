<script type="text/javascript">
    // 用户所拥有的市场信息
    var firms ='';
    <%if (has(firms)){%>
        firms = ${firms};
    <%}%>

    /**
	 * 是否显示修改保存-取消操作按钮
	 * 
	 * @param flag
	 *            true-显示
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

    // 表格表头右键菜单
    function headerContextMenu(e, field){
        e.preventDefault();
        if (!cmenu){
            createColumnMenu("roleGrid");
        }
        cmenu.menu('show', {
            left:e.pageX,
            top:e.pageY
        });
    };

    // 全局按键事件
    function getKey(e){
        e = e || window.event;
        var keycode = e.which ? e.which : e.keyCode;
        if(keycode == 46){ // 如果按下删除键
            var selected = $("#roleGrid").treegrid("getSelected");
            if(selected && selected!= null){
                del();
            }
        }
    };

    /**
	 * 绑定页面回车事件，以及初始化页面时的光标定位
	 * 
	 * @formId 表单ID
	 * @elementName 光标定位在指点表单元素的name属性的值
	 * @submitFun 表单提交需执行的任务
	 */
    $(function () {
        // 角色grid对象
        window.roleGrid = $('#roleGrid');
        var obj={};
        obj.code='';
        obj.name='-- 全部 --';
        // 为了不改变原值，所以复制一遍数组
        var firmData = firms.slice();
        // 动态添加'全部'
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
        initRoleGrid();
        // 表格仅显示下边框
        roleGrid.treegrid('getPanel').removeClass('lines-both lines-no lines-right lines-bottom').addClass("lines-bottom");
        queryGrid();
        // 初始化用户列表的grid
        initUserListGrid();
    });

    function onBeforeLoad() {
        setOptBtnDisplay(false);
    }

    /**
	 * 初始化角色列表的grid
	 */
    function initRoleGrid() {
        // 设置grid的可编辑信息
        $("#roleGrid").dataGridEditor({
            insertUrl: "${contextPath!}/role/insert.action",
            updateUrl: "${contextPath!}/role/update.action",
            deleteUrl: '${contextPath!}/role/delete.action',
            target: 'treegrid',
            onLoadSuccess:onLoadSuccess,
            onBeginEdit: function (row) {
                var editors = $("#roleGrid").treegrid('getEditors', row.id);
                editors[0].target.trigger('focus');
                // 获取市场字段的编辑框
                var ed = $("#roleGrid").treegrid('getEditor', {id:row.id,field:'firmCode'});
                // 存在ID，数据编辑情况下,市场信息不可更改
                debugger
                var parentNode = $("#roleGrid").treegrid('getParent',row.id);
                if (parentNode && typeof (parentNode.id) != 'string') {
                	row.parentId=parentNode.id;
                }
                row.firmCode = parentNode.$_firmCode?parentNode.$_firmCode:parentNode.id;
                setOptBtnDisplay(true);
            },
            onEndEdit: function (row,data) {
            	var parentNode = $("#roleGrid").treegrid('getParent',row.id);
            	if (parentNode && !typeof parentNode.id != 'string') {
                	row.parentId=null;
                }
                setOptBtnDisplay(false);
            },
            onSaveSuccess: function (row, data) {
                roleGrid.treegrid("reload");
            },
            canEdit:function (row) {
            	if (isNaN(row.id)) {
            		return false;
            	}
                <%if(hasResource("updateRole")) {%>
                    return true;
                <%}else{ %>
                    return false;
                <%}%>
            }
        });

    }

    
    // 表格查询
    function queryGrid(queryModel) {
        var opts = roleGrid.treegrid("options");
        if (null == opts.url || "" == opts.url) {
            opts.url = "${contextPath}/role/listTreeGrid.action";
        }
        if (queryModel) {
        	opts.url += '?queryModel=true'
        }
        if(!$('#form').form("validate")){
            return;
        }
        roleGrid.treegrid("load", bindGridMeta2Form("roleGrid", "form"));
    };

    // 清空表单
    function clearForm() {
        $('#form').form('clear');
        queryGrid();
    }

    /**
	 * 打开权限设置页面
	 */
    function editRoleMenuAndResource() {
        var selected = roleGrid.treegrid("getSelected");
        if (null == selected) {
            swal('警告','请选中一条数据', 'warning');
            return;
        }
        $('#roleMenuAndResourceDlg').dialog('open');
        $('#roleMenuAndResourceDlg').dialog('center');
        $('#roleMenuAndResourceGrid').treegrid("clearChecked");
        $('#roleMenuAndResourceGrid').treegrid("clearSelections");
        var opts = $('#roleMenuAndResourceGrid').treegrid("options");
        // 设置 关闭 级联检查，不然会默认勾选子节点中的未选中的数据
 		opts.cascadeCheck = false;
        if (null == opts.url || "" == opts.url) {
            opts.url = "${contextPath}/role/getRoleMenuAndResource.action";
        }
        $('#roleMenuAndResourceGrid').treegrid("load",{roleId:selected.id});
    }


    /**
	 * 保存角色-菜单-资源新
	 */
    function saveRoleMenuAndResource() {
        $("#saveRoleMenuAndResourceBtn").linkbutton("disable");
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
        for (var i = 0; i< childCheckNodes.length;i++){
            ids.push($($(childCheckNodes[i]).closest('tr')[0]).attr("node-id"));
        }
        var selected = roleGrid.treegrid("getSelected");
        $.ajax({
            type: "POST",
            url: "${contextPath}/role/saveRoleMenuAndResource.action",
            data: {roleId: selected.id, resourceIds: ids},
            dataType: "json",
            traditional: true,
            async: true,
            success: function (ret) {
                $("#saveRoleMenuAndResourceBtn").linkbutton("enable");
                if (ret.success) {
                    $('#roleMenuAndResourceDlg').dialog('close');
                     swal('提示',ret.result, 'success');
                } else {
                    swal('错误',ret.result, 'error');
                }
            },
            error: function () {
                $("#saveRoleMenuAndResourceBtn").linkbutton("enable");
                swal('错误', '远程访问失败', 'error');
            }
        });
    }

    /**
	 * 角色用户列表
	 */
    function onUserList() {
        var selected = roleGrid.treegrid("getSelected");
        if (null == selected) {
            swal('警告','请选中一条数据', 'warning');
            return;
        }
        var opts = $('#userListGrid').treegrid("options");
        if (null == opts.url || "" == opts.url) {
            opts.url = "${contextPath!}/user/findUserByRole.action";
        }
        $("#userListGrid").treegrid("load",bindGridMeta2Data("userListGrid", {roleId:selected.id}));
        $('#userListDlg').dialog('open');
        $('#userListDlg').dialog('center');
    }

    /**
	 * 用户列表grid初始化
	 */
    function initUserListGrid() {
        $('#userListGrid').treegrid({
            toolbar: [
                {
                    iconCls:'icon-remove',
                    text:'解除绑定',
                    handler:function(){
                        unbindRoleUser();
                    }
                }
            ]
        });
        $('#userListGrid').treegrid('getPanel').removeClass('lines-both lines-no lines-right lines-bottom').addClass("lines-bottom");
    }

    /**
	 * 解除角色绑定
	 */
    function unbindRoleUser() {
        // 获取选择的角色信息
        var selectedRole = roleGrid.treegrid("getSelected");
        // 选择的用户
        var selectedUser = $("#userListGrid").treegrid("getSelected");
        if (null == selectedUser) {
            swal('警告','请选中一条数据', 'warning');
            return;
        }
        swal({
            title : '您确认想要解绑该用户吗？',
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
            var index = $('#userListGrid').treegrid("getRowIndex", selectedUser);
            $.ajax({
                type: "POST",
                url: "${contextPath!}/role/unbindRoleUser.action",
                data: {roleId: selectedRole.id, userId: selectedUser.id},
                dataType: "json",
                async: true,
                success : function(data) {
                    if (data.success) {
                        $('#userListGrid').treegrid("deleteRow", index);
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
    
    function roleMenuTreeLoadsuccess(){
        var opts = $('#roleMenuAndResourceGrid').treegrid("options");
 		opts.cascadeCheck = true;
    }

    /**
	 * 分配角色的grid，加载完成后，设置 开启 级联检查
	 */
    function onLoadSuccess(row, data) {
    	$('#footerbar').text($(this).treegrid('getFooterRows')[0].roleName);
    	$(this).treegrid("options").url = "${contextPath}/role/listTreeGrid.action";
        $(this).treegrid("clearSelections");
        setOptBtnDisplay(false);
    }
    
    function renderChildren(row){
    	$("#roleGrid").treegrid("options").url ="${contextPath}/role/expand.action?parentId="+row.id;
    }

</script>