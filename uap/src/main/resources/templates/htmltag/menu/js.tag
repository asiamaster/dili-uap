<script type="text/javascript">
    String.prototype.endsWith = function(str){
        if(str==null || str=="" || this.length == 0 ||str.length > this.length){
            return false;
        }
        if(this.substring(this.length - str.length)){
            return true;
        }else{
            return false;
        }
        return true;
    };

    String.prototype.startsWith = function(str){
        if(str == null || str== "" || this.length== 0 || str.length > this.length){
            return false;
        }
        if(this.substr(0,str.length) == str){
            return true;
        }else{
            return false;
        }
        return true;
    };
    /**
 * 页面加载完毕后默认选中菜单树的第一个根节点节点
 * @param node
 * @param data
 */
function onTreeLoadSuccess(node, data) {
    var roots = $('#menuTree').tree('getRoots');
    $('#menuTree').tree("select", roots[0].target);
}

/**
 * 点击菜单树事件
 */
function onSelectTree(node) {
    $("#btnSave1").hide();
    $("#btnCancel1").hide();
    $("#btnSave2").hide();
    $("#btnCancel2").hide();
    cancelEdit("grid1");
    cancelEdit("grid2");
    queryGrid(node);
    //表格1的清空选择
    $("#grid1").datagrid("clearSelections");
    //此时表格2可能还没渲染，无法清空，所以要判断是否有class属性
    if($("#grid2").attr("class") != null){
        $("#grid2").datagrid("clearSelections");
    }
    //清空表格3的数据
    $("#grid3").datagrid("loadData",[]);
}

/**
 * 点击左边菜单，查询右边的两个表格
 * 如果是菜单目录，则只显示上方的菜单列表
 * 如果是菜单链接，则显示上方的权限列表和下方的内链菜单列表
 *  @param node 页面布局左边树形菜单节点数据，根据这个参数来判断是显示menu还是resource
 */
function queryGrid(node) {
    var nodeType = node.attributes.type;
    //点击目录和系统
    if(nodeType == ${@com.dili.uap.glossary.MenuType.DIRECTORY.getCode()}
        || nodeType == ${@com.dili.uap.glossary.MenuType.SYSTEM.getCode()}){
        //东侧面板收缩
        $("#layoutPanel").layout("collapse", "east");
        //中北面板最大化
        $('#centerNorthPanel').panel('maximize');
        queryGridByDir(node);
    }else{//点击链接
        //东侧面板恢复
        // $("#eastPanel").panel("expand");
        $("#layoutPanel").layout("expand", "east");
        //恢复中北子面板的大小
        $('#centerNorthPanel').panel('restore');
        queryGridByLinks(node);
    }
}

/**
 * 根据目录菜单查询列表
 */
function queryGridByDir(node) {
    renderMenuGrid(node, "grid1");
    bindEditMenuGrid(node);
}

/**
 * 根据链接菜单查询列表
 */
function queryGridByLinks(node) {
    renderResourceGrid(node, "grid1");
    bindEditResourceGrid(node);
    renderInternalLinksGrid(node, "grid2")
    bindInternalLinksGrid(node);
    // renderResourceLinkGrid("grid3");
}

//可编辑表格的操作
function openInsert(gridId) {
    $("#"+gridId).dataGridEditor().insert();
}

function openUpdate(gridId) {
    $("#"+gridId).dataGridEditor().update();
}

function del(gridId) {
    $("#"+gridId).dataGridEditor().delete();
}

function endEditing(gridId) {
    $("#"+gridId).dataGridEditor().save();
}

function cancelEdit(gridId) {
    $("#"+gridId).dataGridEditor().cancel();
}

function restore(gridId){
    $("#"+gridId).dataGridEditor().restore();
}

/**
 * 添加资源链接关系
 */
function addResourceLink() {
    var selected1 = $("#grid1").datagrid("getSelected");
    if(selected1 == null || selected1.code == null){
        swal('警告','请选中一条权限', 'warning');
        return;
    }
    var selected2 = $("#grid2").datagrid("getSelected");
    if(selected2 == null || selected2.id == null){
        swal('警告','请选中一条内部链接', 'warning');
        return;
    }
    var grid3Data = $("#grid3").datagrid("getData");
    var rows = grid3Data.rows;
    for(var row in rows){
        if(rows[row]["$_resourceCode"] == selected1.code && rows[row]["$_menuId"] == selected2.id){
            swal('错误', '已经绑定了资源链接', 'error');
            return;
        }
    }
    $.ajax({
        type: "POST",
        url: "${contextPath}/resource/addResourceLink.action",
        data: {resourceCode: selected1.code, menuId: selected2.id},
        processData: true,
        dataType: "json",
        async: true,
        success: function (output) {
            //新加的resource可能没有为资源链接列表grid添加url，所以这里需要判断，并添加
            var opts = $("#grid3").datagrid("options");
            if (null == opts.url || "" == opts.url) {
                opts.url = "${contextPath}/resource/listResourceLink.action";
            }
            $("#grid3").datagrid("load", bindGridMeta2Data("grid3", {resourceCode:selected1.code}));
        },
        error: function () {
            swal('错误', '远程访问失败', 'error');
        }
    });
}

/**
 * 删除资源链接关系
 */
function deleteResourceLink() {
    var selected3 = $("#grid3").datagrid("getSelected");
    if(selected3 == null){
        swal('警告','请选中一条资源链接', 'warning');
        return;
    }
    <#swalConfirm swalTitle="您确认想要删除记录吗？">
    $.ajax({
        type: "POST",
        url: "${contextPath}/resource/deleteResourceLink.action",
        data: {id: selected3.id},
        processData: true,
        dataType: "json",
        async: false,
        success: function (output) {
            $("#grid3").datagrid("clearSelections");
            $("#grid3").datagrid("reload");
        },
        error: function () {
            swal('错误', '远程访问失败', 'error');
        }
    });
    </#swalConfirm>
}

// ======================  私有方法分割线  ======================

/**
 * 渲染菜单列表
 * @param node
 * @param gridId
 */
function renderMenuGrid(node, gridId) {
    //渲染上方列表
    $("#"+gridId).datagrid({
        title : "菜单列表",
        height : '100%',
        url : '${contextPath!}/menu/list.action',
        toolbar : '#grid1Toolbar',
        queryParams : {
            menuId : node.id
        },
        columns : [[{
            width:'0%',
            field : 'id',
            title : 'id',
            hidden : true
        },{
            field : 'name',
            title : '菜单名称',
            width : '13%',
            editor : {
                type : 'textbox',
                options : {
                    required : true,
                    validType : 'length[2, 7]',
                    missingMessage : '菜单名称不能为空',
                    invalidMessage : '菜单名称必须是2-7个字符'
                }
            }
        }, {
            field : 'type',
            title : '类型',
            width : '6%',
            formatter : function(value, row, index) {
                if (value == 0) {
                    return '目录';
                } else if (value == 1) {
                    return '链接';
                } else if (value == 2) {
                    return '内链';
                }
            },
            editor : {
                type : 'combobox',
                options : {
                    required : true,
                    missingMessage : '请选择连接类型',
                    valueField : 'value',
                    textField : 'name',
                    editable : false,
                    panelHeight:'auto',
                    data : [{
                        name : '目录',
                        value : 0
                    }, {
                        name : '链接',
                        value : 1
                    }]
                }
            }
        }, {
            field : 'url',
            title : '菜单链接地址',
            width : '30%',
            editor : {
                type : 'textbox',
                options : {
                    validType : 'length[0, 120]',
                    invalidMessage : '链接地址必须小于120个字符'
                }
            }
        },{
            field : 'description',
            title : '描述',
            width : '38%',
            editor : {
                type : 'textbox',
                options : {
                    validType : 'length[0, 20]',
                    invalidMessage : '描述必须小于20个字符'
                }
            }
        }, {
            field : 'shortcut',
            title : '快捷菜单',
            width : '7%',
            formatter : function(value, row, index) {
                if (value == 0) {
                    return '否';
                } else if (value == 1) {
                    return '是';
                }
            },
            editor : {
                type : 'combobox',
                options : {
                    required : true,
                    missingMessage : '请选择',
                    valueField : 'value',
                    textField : 'name',
                    editable : false,
                    panelHeight:'auto',
                    data : [{
                        name : '否',
                        value : "0"
                    }, {
                        name : '是',
                        value : "1"
                    }]
                }
            }
        }, {
            field : 'orderNumber',
            title : '排序号',
            width : '7%',
            editor : {
                type : 'numberbox',
                options : {
                    validType : 'length[0, 6]',
                    invalidMessage : '排序号必须小于6位数'
                }
            }
        }]]
        //columns 属性结束
    });
    $("#"+gridId).datagrid('getPanel').removeClass('lines-both lines-no lines-right lines-bottom').addClass("lines-bottom");
}

/**
 * 点击资源列表时，查询资源链接
 */
function clickResource(index, row) {
    var opts = $("#grid3").datagrid("options");
    if (null == opts.url || "" == opts.url) {
        opts.url = "${contextPath}/resource/listResourceLink.action";
    }
    //渲染权限列表
    $("#grid3").datagrid("load", bindGridMeta2Data("grid3", {resourceCode:row.code}));
    $("#grid3").datagrid('getPanel').removeClass('lines-both lines-no lines-right lines-bottom').addClass("lines-bottom");
}
/**
 * 渲染资源列表
 * @param node
 * @param gridId
 */
function renderResourceGrid(node, gridId) {
    //渲染权限列表
    $("#"+gridId).datagrid({
        title : "权限列表",
        url : '${contextPath!}/resource/list.action',
        height : '100%',
        toolbar : '#grid1Toolbar',
        queryParams : {
            menuId : node.id
        },
        columns : [[{
            field : 'id',
            title : 'id',
            hidden : true
        }, {
            field : 'name',
            title : '权限名称',
            width : '20%',
            editor : {
                type : 'textbox',
                options : {
                    required : true,
                    validType : 'length[1, 20]',
                    missingMessage : '请输入权限名称',
                    invalidMessage : '权限名称必须是1-20个字符'
                }
            }
        }, {
            field : 'code',
            title : '权限代码',
            width : '25%',
            editor : {
                type : 'textbox',
                options : {
                    required : true,
                    validType : 'length[1, 50]',
                    missingMessage : '请输入权限代码',
                    invalidMessage : '权限代码必须是1-50个字符'
                }
            }
        }, {
            field : 'description',
            title : '描述',
            width : '55%',
            editor : {
                type : 'textbox',
                options : {
                    validType : 'length[0, 120]',
                    invalidMessage : '描述必须小于120个字符'
                }
            }
        }
        ]]
    });
    $("#"+gridId).datagrid('getPanel').removeClass('lines-both lines-no lines-right lines-bottom').addClass("lines-bottom");
}

/**
 * 渲染内部链接列表
 * @param node
 * @param gridId
 */
function renderInternalLinksGrid(node, gridId) {
    //渲染内部链接列表
    $("#"+gridId).datagrid({
        title : "内部链接列表",
        height : '100%',
        url : '${contextPath!}/menu/listInternalLinks.action',
        toolbar : '#grid2Toolbar',
        queryParams : {
            menuId : node.id,
            type : 2
        },
        columns : [[{
            width: '0%',
            field : 'id',
            title : 'id',
            hidden : true
        },{
            field : 'name',
            title : '链接名称',
            width : '15%',
            editor : {
                type : 'textbox',
                options : {
                    required : true,
                    validType : 'length[1, 20]',
                    missingMessage : '链接名称不能为空',
                    invalidMessage : '链接名称必须是1-20个字符'
                }
            }
        }, {
            field : 'url',
            title : '内部链接地址',
            width : '43%',
            editor : {
                type : 'textbox',
                options : {
                    required : true,
                    validType : 'length[1, 120]',
                    missingMessage : '链接地址不能为空',
                    invalidMessage : '链接地址必须是1-120个字符'
                }
            }
        }, {
            field : 'description',
            title : '描述',
            width : '42%',
            editor : {
                type : 'textbox',
                options : {
                    validType : 'length[0, 20]',
                    invalidMessage : '描述必须小于20个字符'
                }
            }
        }]]
        //columns 属性结束
    });
    $("#"+gridId).datagrid('getPanel').removeClass('lines-both lines-no lines-right lines-bottom').addClass("lines-bottom");
}

/**
 * 绑定可编辑菜单表格
 * @param node
 */
function bindEditMenuGrid(node) {
    $("#grid1").dataGridEditor({
        insertUrl: "${contextPath!}/menu/insert.action",
        updateUrl: "${contextPath!}/menu/update.action",
        deleteUrl: '${contextPath!}/menu/delete.action',
        onBeforeEdit: function () {
            $("#btnSave1").show();
            $("#btnCancel1").show();
        },
        onEndEdit: function (index, row) {
            $("#btnSave1").hide();
            $("#btnCancel1").hide();
        },
        onBeforeLoad : function () {
            $("#btnSave1").hide();
            $("#btnCancel1").hide();
        },
        onSaveSuccess: function (index, row, data) {
            //data就是新增后返回的id，没有返回id就是修改
            if(data) {
                // row.id=data;
                // $("#grid1").datagrid("updateRow",{
                //     index: index,
                //     row:row
                // });
                //为菜单添加相应的节点
                addMenuNode(row, row["type"]);
            }else{
                updateMenuNode(row);
            }
        },
        onDeleteSuccess: function (row, data) {
            var node = $('#menuTree').tree('find', "menu_"+row.id);
            $("#menuTree").tree("remove", node.target);
        },
        extendParams: function (row) {
            return {
                systemId: $("#grid1").data('systemId'),
                menuId: $("#grid1").data('menuId')
            }
        },
        canEdit: function (row) {
            <%if(hasResource("updateMenu")) {%>
            return true;
            <%}else{%>
            return false;
            <%}%>
        }
    });
    //设置当前菜单节点选中的全局变量：systemId和menuId， 用于新增和修改
    $("#grid1").data('systemId', node.attributes.systemId);
    $("#grid1").data('menuId', node.id);
}

/**
 * 绑定可编辑资源表格
 * @param node
 */
function bindEditResourceGrid(node) {
    $("#grid1").dataGridEditor({
        insertUrl: "${contextPath!}/resource/insert.action",
        updateUrl: "${contextPath!}/resource/update.action",
        deleteUrl: '${contextPath!}/resource/delete.action',
        onClickRow: clickResource,
        onBeforeEdit: function () {
            $("#btnSave1").show();
            $("#btnCancel1").show();
        },
        onEndEdit: function () {
            $("#btnSave1").hide();
            $("#btnCancel1").hide();
        },
        onSaveSuccess: function (index, row, data) {
            // $("#grid1").datagrid("clearSelections");
            //data就是新增后返回的id，没有返回id就是修改
            if(data) {
                row.id=data;
                // $("#grid1").datagrid("updateRow",{
                //     index: index,
                //     row:row
                // });
            }
        },
        onDeleteSuccess:function(){
            $("#grid3").datagrid("loadData", { total: 0, rows: [] });
            $("#grid3").datagrid("clearSelections");
            $("#grid1").datagrid("clearSelections");
        },
        extendParams: function (row) {
            return {
                menuId: $("#grid1").data('menuId')
            }
        },
        canEdit: function (row) {
            <%if(hasResource("updateMenu")) {%>
            return true;
            <%}else{%>
            return false;
            <%}%>
        }
    });
    //设置当前菜单节点选中的全局变量：menuId， 用于新增和修改
    $("#grid1").data('menuId', node.id);
}

/**
 * 绑定内链菜单表格
 * @param node
 */
function bindInternalLinksGrid(node) {
    $("#grid2").dataGridEditor({
        insertUrl: "${contextPath!}/menu/insert.action",
        updateUrl: "${contextPath!}/menu/update.action",
        deleteUrl: '${contextPath!}/menu/delete.action',
        onBeforeEdit: function () {
            $("#btnSave2").show();
            $("#btnCancel2").show();
        },
        onEndEdit: function () {
            $("#btnSave2").hide();
            $("#btnCancel2").hide();

        },
        onSaveSuccess: function (index, row, data) {
            //data就是新增后返回的id，没有返回id就是修改
            if(data) {
                row.id = data.id;
            }
            // if(data) {
            //     //为菜单添加相应的节点
            //     addMenuNode(row, 2);
            // }else{
            //     updateMenuNode(row);
            // }
        },
        onDeleteSuccess: function (row, data) {
            $("#grid3").datagrid("loadData", { total: 0, rows: [] });
            $("#grid3").datagrid("clearSelections");
            $("#grid2").datagrid("clearSelections");
            // var node = $('#menuTree').tree('find', "menu_"+row.id);
            // $("#menuTree").tree("remove", node.target);
        },
        extendParams: function (row) {
            return {
                systemId: $("#grid2").data('systemId'),
                menuId: $("#grid2").data('menuId'),
                type : 2
            }
        },
        canEdit: function (row) {
            <%if(hasResource("updateMenu")) {%>
            return true;
            <%}else{%>
            return false;
            <%}%>
        }
    });
    //设置当前菜单节点选中的全局变量：systemId和menuId， 用于新增和修改
    $("#grid2").data('systemId', node.attributes.systemId);
    $("#grid2").data('menuId', node.id);
}

/**
 * 在当前选中的节点添加菜单树子节点
 * @param row 必需id和name字段
 * @param menuType 菜单类型
 */
function addMenuNode(row, menuType) {
    var selected = $('#menuTree').tree('getSelected');
    $("#menuTree").tree("append",{
        parent : selected.target,
        data : [{
            id : "menu_"+row.id,
            name : row.name,
            attributes : {type: menuType, systemId : $("#grid1").data('systemId')}
        }]
    })
}

/**
 * 修改指定菜单树节点
 * @param row 必需id和name字段
 */
function updateMenuNode(row) {
    var node = $('#menuTree').tree('find', "menu_"+row.id);
    $("#menuTree").tree("update",{
        target : node.target,
        text : row.name,
        attributes : {type: row.type, systemId : $("#grid1").data('systemId')}
    })
}

/**
 * 拖动完菜单时触发
 * @param target the target node element to be dropped.
 * @param source the source node being dragged.
 */
function dragMenu(target, source, point) {
    endEditing("grid1");
    endEditing("grid2");
    //只允许拖到某个节点下面
    if(point != "append"){
        return false;
    }
    //不允许拖最顶层的系统
    if(source.id.startsWith("sys_")){
        return false;
    }
    var targetNode = $('#menuTree').tree('getNode', target);
    //目标节点可以是系统或菜单
    if(targetNode.id.startsWith("sys_") || targetNode.id.startsWith("menu_")){
        var msg = "是否要移动["+source.text+"]到["+targetNode.text+"]下面?";
        //除了原生confirm，其它第三方都无法阻塞。。。
        if(confirm(msg)){
            //是否拖动成功
            var flag = false;
            $.ajax({
                type: "POST",
                url: "${contextPath}/menu/shiftMenu.action",
                data: {sourceId: source.id, targetId: targetNode.id},
                processData: true,
                dataType: "json",
                async: false,
                success: function (ret) {
                    if (ret.success) {
                        swal('成功',ret.result, 'success');
                        flag = true;
                    } else {
                        swal('错误',ret.result, 'error');
                        flag = false;
                    }
                },
                error: function () {
                    swal('错误', '远程访问失败', 'error');
                    flag = false;
                }
            });
            return flag;
        }else{
            return false;
        }
    }
    return false;
}
</script>