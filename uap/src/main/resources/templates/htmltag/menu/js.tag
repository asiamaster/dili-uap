<script type="text/javascript">

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
    queryGrid(node);
}

/**
 * 点击左边菜单，查询右边的两个表格
 * 如果是菜单目录，则只显示上方的菜单列表
 * 如果是菜单链接，则显示上方的权限列表和下方的内链菜单列表
 *  @param node 页面布局左边树形菜单节点数据，根据这个参数来判断是显示menu还是resource
 */
function queryGrid(node) {
    var nodeType = node.attributes.type;
    if(nodeType == ${@com.dili.uap.glossary.MenuType.DIRECTORY.getCode()}
        || nodeType == ${@com.dili.uap.glossary.MenuType.SYSTEM.getCode()}){
        //上面板最大化，下面板最小化
        $('#panel1').panel('maximize');
        queryGridByDir(node);
    }else{
        //恢复右边两个面板的大小
        $('#panel1').panel('restore');
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
            field : 'id',
            title : 'id',
            hidden : true
        },{
            field : 'name',
            title : '菜单名称',
            width : '10%',
            editor : {
                type : 'textbox',
                options : {
                    required : true,
                    validType : 'length[1, 50]',
                    missingMessage : '菜单名称不能为空',
                    invalidMessage : '菜单名称必须是1-50个字符'
                }
            }
        }, {
            field : 'type',
            title : '类型',
            width : '10%',
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
            width : '35%',
            editor : {
                type : 'textbox'
            }
        }, {
            field : 'description',
            title : '描述',
            width : '40%',
            editor : 'text'
        }, {
            field : 'orderNumber',
            title : '排序号',
            width : '5%',
            editor : {
                type : 'numberbox'
            }
        }]]
        //columns 属性结束
    });
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
                    missingMessage : '请输入权限名称'
                }
            }
        }, {
            field : 'code',
            title : '权限代码',
            width : '20%',
            editor : {
                type : 'textbox',
                options : {
                    required : true,
                    missingMessage : '请输入权限代码'
                }
            }
        }, {
            field : 'description',
            title : '描述',
            width : '60%',
            editor : 'textbox'
        }
        ]]
    });
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
                    validType : 'length[1, 50]',
                    missingMessage : '链接名称不能为空',
                    invalidMessage : '链接名称必须是1-50个字符'
                }
            }
        }, {
            field : 'url',
            title : '内部链接地址',
            width : '40%',
            editor : {
                type : 'textbox',
                options : {
                    required : true,
                    validType : 'length[1, 255]',
                    missingMessage : '链接地址不能为空',
                    invalidMessage : '链接地址必须是1-255个字符'
                }
            }
        }, {
            field : 'description',
            title : '描述',
            width : '40%',
            editor : 'text'
        }]]
        //columns 属性结束
    });
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
        onAfterEdit: function () {
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
                row.id=data;
                $("#grid1").datagrid("updateRow",{
                    index: index,
                    row:row
                });
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
            return row.id !== -1;
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
        onBeforeEdit: function () {
            $("#btnSave1").show();
            $("#btnCancel1").show();
        },
        onAfterEdit: function () {
            $("#btnSave1").hide();
            $("#btnCancel1").hide();
        },
        onSaveSuccess: function (index, row, data) {
            //data就是新增后返回的id，没有返回id就是修改
            if(data) {
                row.id=data;
                $("#grid1").datagrid("updateRow",{
                    index: index,
                    row:row
                });
            }
        },
        extendParams: function (row) {
            return {
                menuId: $("#grid1").data('menuId')
            }
        },
        canEdit: function (row) {
            return row.id !== -1;
        }
    });
    //设置当前菜单节点选中的全局变量：menuId， 用于新增和修改
    $("#grid1").data('menuId', node.id);
}

/**
 * 绑定可编辑菜单表格
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
        onAfterEdit: function () {
            $("#btnSave2").hide();
            $("#btnCancel2").hide();

        },
        onSaveSuccess: function (index, row, data) {
            //data就是新增后返回的id，没有返回id就是修改
            if(data) {
                row.id=data;
                $("#grid2").datagrid("updateRow",{
                    index: index,
                    row:row
                });
                //为菜单添加相应的节点
                addMenuNode(row, 2);
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
                systemId: $("#grid2").data('systemId'),
                menuId: $("#grid2").data('menuId'),
                type : 2
            }
        },
        canEdit: function (row) {
            return row.id !== -1;
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
        text : row.name
    })
}
</script>