<script type="application/javascript">
    //用户所拥有的市场信息
    var firms ='';
    <%if (has(firms)){%>
        firms = ${firms};
    <%}%>
    //用户当前的所属市场
    var firmCode = '${firmCode!}'


    /**
     * 根据市场code加载部门信息，并显示到相应的部门控件中
     * @param firmCode 市场code
     */
    function loadDepartments(firmCode,controlId) {
        var obj = {id: '', name: '-- 请选择 --','parentId':null};
        if (firmCode){
            $.post('${contextPath!}/department/listByCondition.action', {firmCode: firmCode}, function (ret) {
                if (ret) {
                    //动态添加'请选择'
                    ret.unshift(obj);
                    $('#' + controlId).combotree("clear");
                    $('#' + controlId).combotree("loadData", ret);
                    // $('#' + controlId).combotree("setValue", obj);
                }
            }, 'json');
        }else{
            $('#' + controlId).combotree("clear");
            $('#' + controlId).combotree("loadData", {});
            // $('#' + controlId).combotree("setValue", obj);
        }

    }

    /**
     * 根据市场code加载角色信息
     * @param firmCode 市场code
     */
    function loadRoles(firmCode) {
        var obj = {id: null, roleName: '-- 请选择 --'};
        if (firmCode){
            $.post('${contextPath!}/role/list.action', {firmCode: firmCode}, function (ret) {
                if (ret) {

                    //动态添加'请选择'
                    ret.unshift(obj);
                    $('#roleId').combobox("clear");
                    $('#roleId').combobox("loadData", ret);
                }
            }, 'json');
        }else{
            $('#roleId').combobox("clear");
            $('#roleId').combobox("loadData", {});
        }


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