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
        $('#panel2').panel('minimize');
        queryGridByDir(node);
    }else{
        //恢复右边两个面板的大小
        $('#panel1').panel('restore');
        $('#panel2').panel('restore');
        queryGridByLinks(node);
    }
}

/**
 * 根据目录菜单查询列表
 */
function queryGridByDir(node) {
    //渲染上方列表
    $("#grid1").datagrid({
        title : "菜单列表",
        sortName : "orderNumber",
        height : '100%',
        url : '${contextPath!}/menu/list.action',
        toolbar : '#menuToolbar',
        queryParams : {
            parentId : node.id
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
    //生成可编辑表格
    $("#grid1").dataGridEditor({
        insertUrl: "${contextPath!}/menu/insert.action",
        updateUrl: "${contextPath!}/menu/update.action",
        deleteUrl: '${contextPath!}/menu/delete.action',
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
                systemId: $("#grid1").data('systemId'),
                parentId: $("#grid1").data('parentId')
            }
        },
        canEdit: function (row) {
            return row.id !== -1;
        }
    });
    //设置当前菜单节点选中的全局变量：systemId和parentId， 用于新增和修改
    $("#grid1").data('systemId', node.attributes.systemId);
    $("#grid1").data('parentId', node.id);
}

/**
 * 根据链接菜单查询列表
 */
function queryGridByLinks(node) {

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

</script>