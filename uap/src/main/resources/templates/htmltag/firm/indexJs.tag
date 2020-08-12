// 打开新增窗口
        function openInsert(){
            window.location.href='${contextPath!}/firm/add.html';
        }

        // 打开修改窗口
        function openUpdate(){
            var selected = $("#grid").datagrid("getSelected");
            if (null == selected) {
                swal('警告','请选中一条数据', 'warning');
                return;
            }
            $('#dlg').dialog('open');
            $('#dlg').dialog('center');
            formFocus("_form", "_name");
            var formData = $.extend({},selected);
            formData = addKeyStartWith(getOriginalData(formData),"_");
            $('#_form').form('load', formData);
        }

        function saveOrUpdate(){
            if(!$('#_form').form("validate")){
                return;
            }
            var _formData = removeKeyStartWith($("#_form").serializeObject(),"_");
            var _url = null;
            // 没有id就新增
            if(_formData.id == null || _formData.id==""){
                _url = "${contextPath}/firm/insert.action";
            }else{// 有id就修改
                _url = "${contextPath}/firm/update.action";
            }
            $.ajax({
                type: "POST",
                url: _url,
                data: _formData,
                processData:true,
                dataType: "json",
                async : true,
                success: function (data) {
                    if(data.code=="200"){
                        $("#grid").datagrid("reload");
                        $('#dlg').dialog('close');
                    }else{
                        swal('错误',data.result, 'error');
                    }
                },
                error: function(){
                    swal('错误', '远程访问失败', 'error');
                }
            });
        }

        // 根据主键删除
        function del() {
            var selected = $("#grid").datagrid("getSelected");
            if (null == selected) {
                swal('警告','请选中一条数据', 'warning');
                return;
            }
            <#swalConfirm swalTitle="您确认想要删除记录吗？">
                    $.ajax({
                        type: "POST",
                        url: "${contextPath}/firm/delete.action",
                        data: {id:selected.id},
                        processData:true,
                        dataType: "json",
                        async : true,
                        success: function (data) {
                            if(data.code=="200"){
                                $("#grid").datagrid("reload");
                                $('#dlg').dialog('close');
                            }else{
                                swal('错误',data.result, 'error');
                            }
                        },
                        error: function(){
                            swal('错误', '远程访问失败', 'error');
                        }
                    });
            </#swalConfirm>
        }
        // 表格查询
        function queryGrid() {
            var opts = $("#grid").datagrid("options");
            if (null == opts.url || "" == opts.url) {
                opts.url = "${contextPath}/firm/listPage.action";
            }
            if(!$('#form').form("validate")){
                return;
            }
            $("#grid").datagrid("load", bindGridMeta2Form("grid", "form"));
        }


        // 清空表单
        function clearForm() {
            $('#form').form('clear');
        }

        // 表格表头右键菜单
        function headerContextMenu(e, field){
            e.preventDefault();
            if (!cmenu){
                createColumnMenu("grid");
            }
            cmenu.menu('show', {
                left:e.pageX,
                top:e.pageY
            });
        }

        // 全局按键事件
        function getKey(e){
            e = e || window.event;
            var keycode = e.which ? e.which : e.keyCode;
            if(keycode == 46){ // 如果按下删除键
                var selected = $("#grid").datagrid("getSelected");
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
            bindFormEvent("form", "name", queryGrid);
            bindFormEvent("_form", "_name", saveOrUpdate, function (){$('#dlg').dialog('close');});
            if (document.addEventListener) {
                document.addEventListener("keyup",getKey,false);
            } else if (document.attachEvent) {
                document.attachEvent("onkeyup",getKey);
            } else {
                document.onkeyup = getKey;
            }
            var pager = $('#grid').datagrid('getPager');    // get the pager of
															// treegrid
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
                            doExport('grid');
                        }
                    }
                ]
            });
            // 表格仅显示下边框
            $('#grid').datagrid('getPanel').removeClass('lines-both lines-no lines-right lines-bottom').addClass("lines-bottom");
            queryGrid();
        })