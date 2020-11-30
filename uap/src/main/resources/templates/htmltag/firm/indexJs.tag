// 打开新增窗口
        function openInsert(){
            var selected = $("#grid").datagrid("getSelected");
            var url='${contextPath!}/firm/add.html';
            if (selected) {
                url+='?parentId='+selected.id;
            }
            window.location.href=url;
        }

        // 打开修改窗口
        function openUpdate(){
            var selected = $("#grid").datagrid("getSelected");
            if (null == selected) {
                swal('警告','请选中一条数据', 'warning');
                return;
            }
            window.location.href='${contextPath!}/firm/update.html?id='+selected.id;
        }

        // 根据主键删除
        function del() {
            var selected = $("#grid").datagrid("getSelected");
            if (null == selected) {
                swal('警告','请选中一条数据', 'warning');
                return;
            }
            <#swalConfirm swalTitle="您确认想要删除该内容吗？">
                    $.ajax({
                        type: "POST",
                        url: "${contextPath}/firm/logicalDelete.action",
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
        
        function updateAdminUser(){
        	  var selected = $("#grid").datagrid("getSelected");
              if (null == selected) {
                  swal('警告','请选中一条数据', 'warning');
                  return;
              }	
              window.location.href='${contextPath!}/firm/editAdminUser.html?id='+selected.id;
        }

        function doEnable(flag) {
        	 var selected = $("#grid").datagrid("getSelected");
             if (null == selected) {
                 swal('警告','请选中一条数据', 'warning');
                 return;
             }
             
             var msg = (flag || 'true' == flag) ? '商户被开通后，将可继续在各系统使用，请确认是否开通该商户?' : '商户被关闭后，将不可再继续在各系统使用，请确认是否关闭该商户?';
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
             }).then(function(btn) {
                 if (btn.dismiss == 'cancel') {
                     return;
                 }
                 var url="${contextPath}/firm/";
             	if (flag) {
             		url+='enable.action';
             	}else{
             		url+='disable.action';
             	}
             	// json对象再转换成json字符串
         		$.ajax({
					type : "POST",
					url : url,
					data : {id: selected.id},
					dataType : "json",
					processData : true,
					async : true,
					success : function(ret) {
						$.messager.progress('close');
						if (ret.success) {
							swal('提示', ret.result, 'success');
							window.location.href = '${contextPath!}/firm/index.html';
						} else {
							swal('错误', ret.result, 'error');
						}
					},
					error : function() {
						$.messager.progress('close');
						swal('错误', '远程访问失败', 'error');
					}
				});
             });
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
            // 表格仅显示下边框
            $('#grid').datagrid('getPanel').removeClass('lines-both lines-no lines-right lines-bottom').addClass("lines-bottom");
            queryGrid();
        })