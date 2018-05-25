/**
 *  封装可编辑 EasyUi dataGrid 插件 1.0.0
 */
;
(function ($, window, document, undefined) {


    var defaults = {
        target: 'datagrid', // 目标类型，默认为 datagrid,如果是 treegrid 请传入次参数
        insertUrl: '', // 新增数据 url
        updateUrl: '', // 修改数据 url
        deleteUrl: '', // 删除数据 url
        extendParams: undefined, // 默认新增和修改会把 row 的数据发送给服务端,如果需新增参数,需提供此方法并返回要扩展参数的 json 对象.方法入参为当前操作的 row 对象
        onBeforeEdit: undefined, // 开启编辑模式前回调方法
        onAfterEdit: undefined, // 关闭编辑模式后回调方法
        onSaveSuccess: undefined, //新增或修改成功后回调方法,方法入参为修改后的数据
        canEdit: undefined // 控制 row 是否可以编辑,返回 false 取消编辑动作,方法入参为被编辑的 row 对象
    };


    function DataGridEditor(element, options) {
        this.element = element;
        this.options = $.extend({}, defaults, options);
        this.init();
    }

    DataGridEditor.prototype = {

        init: function () {
            var that = this;
            this.editId = undefined;
            this.element[this.options.target]({
                onClickRow: function () {
                    that.endEditing();
                },
                onDblClickRow: function () {
                    that.openUpdate();
                },
                onAfterEdit: function (index, row, changes) {
                    if (!changes) {
                        row = index;
                    }
                    that._onAfterEdit(index, row);
                },
                onBeforeEdit: function (row) {
                    that._onBeforeEdit(row);
                },
                onCancelEdit: function (index, row) {
                    if (!row) {
                        row = index;
                    }
                    that._onCancelEdit(index, row);
                },
                onEndEdit: function () {
                    that._onEndEdit();
                }
            });
        },
        isEditing: function () {
            return undefined != this.editId;
        },
        // 结束行编辑
        endEditing: function () {
            if (this.editId == undefined) {
                return true
            }
            var index = this.editId;

            if (this.options.target == 'datagrid') {
                index = this.editIndex;
            }

            if (this.element[this.options.target]('validateRow', index)) {
                this.element[this.options.target]('endEdit', index);
                this.editId = undefined;
                this.editIndex = undefined;
                return true;
            } else {
                return false;
            }
        },
        cancelEdit: function () {
            this.oldRecord = undefined;
            if (this.editId == undefined) {
                return;
            }
            var index = this.editId;
            if (this.options.target == 'datagrid') {
                index = this.element.datagrid('getRowIndex', this.element.datagrid('getSelected'));
            }
            this.element[this.options.target]('cancelEdit', index);
        },
        // 开启选中行的编辑模式
        openUpdate: function () {

            var selected = this.element[this.options.target]("getSelected");
            if (!selected) {
                $.messager.alert('警告', '请选中一条数据');
                return;
            }

            if (this.options.canEdit) {
                if (!this.options.canEdit(selected)) {
                    return;
                }
            }

            if (this.endEditing()) {
                if (this.options.target == 'datagrid') {
                    var index = this.element.datagrid('getRowIndex', selected);
                    this.editIndex = index;
                    this.element.datagrid('selectRow', index).datagrid('beginEdit', index);
                } else {
                    this.element.treegrid('select', selected.id).treegrid('beginEdit', selected.id);
                }

                this.editId = selected.id;
            }
        },
        _onAfterEdit: function (index, row) {
            var _index;
            if (this.options.target != 'datagrid') {
                _index = row.id;
            } else {
                _index = index;
            }
            var isValid = this.element[this.options.target]('validateRow', _index);
            if (!isValid) {
                return false;
            }
            this.insertOrUpdate(index, row);
            this.oldRecord = undefined;
        },
        _onBeforeEdit: function (row) {
            if (row.id != 'temp') {
                this.oldRecord = new Object();
                $.extend(true, this.oldRecord, row);
            }
            if (this.options.onBeforeEdit) {
                this.options.onBeforeEdit();
            }
        },
        _onCancelEdit: function (index, row) {
            this.editId = undefined;

            if (this.options.target != 'datagrid') {
                if (row.id == 'temp') {
                    this.element[this.options.target]('remove', row.id);
                }
            } else {
                if (row.id == 'temp') {
                    this.element[this.options.target]('deleteRow', index);
                }
            }

            this._onEndEdit();
        },
        _onEndEdit: function () {
            if (this.options.onAfterEdit) {
                this.options.onAfterEdit();
            }
        },
        insertOrUpdate: function (index, row) {
            var that = this;
            var oldRecord = this.oldRecord;
            var postData = new Object();

            var params = {};
            if (that.options.extendParams) {
                params = that.options.extendParams(row);
            }

            $.extend(true, postData, row, params);

            var url = "";
            if (postData.id == 'temp') {
                postData.id = undefined;
                url = this.options.insertUrl;
            } else {
                url = this.options.updateUrl;
            }
            $.post(url, postData, function (data) {
                if (!data || data.code != 200) {
                    if (oldRecord) {
                        if (that.options.target != 'datagrid') {
                            that.element[that.options.target]('update', {
                                id: row.id,
                                row: oldRecord
                            });
                        } else {
                            that.element[that.options.target]('updateRow', {
                                index: index,
                                row: oldRecord
                            });
                        }

                    } else {
                        if (that.options.target != 'datagrid') {
                            that.element[that.options.target]('remove', row.id);
                        } else {
                            that.element[that.options.target]('deleteRow', index);
                        }

                    }
                    $.messager.alert('提示', data.result);
                    return;
                }
                //成功
                //新增
                if (postData.id == undefined) {
                    if (that.options.target != 'datagrid') {
                        that.element[that.options.target]('remove', 'temp');
                        that.element[that.options.target]('append', {
                            parent: data.data.parentId,
                            data: [data.data]
                        });
                    } else {
                        row.id = data.data.id;
                        that.element[that.options.target]('updateRow', {
                            index: index,
                            row: row
                        });
                    }
                } else {
                    if (that.options.target != 'datagrid') {
                        that.element[that.options.target]('update', {
                            id: postData.id,
                            row: data.data
                        });
                    } else {
                        that.element[that.options.target]('updateRow', {
                            index: index,
                            row: row
                        });
                    }
                }
                if (that.options.onSaveSuccess) {
                    that.options.onSaveSuccess(data.data);
                }
            }, 'json');
        },
        delete: function () {
            var that = this;
            var selected = this.element[this.options.target]("getSelected");
            if (null == selected) {
                $.messager.alert('警告', '请选中一条数据');
                return;
            }
            $.messager.confirm('确认', '您确认想要删除记录吗？', function (r) {
                if (r) {
                    $.ajax({
                        type: "POST",
                        url: that.options.deleteUrl,
                        data: {
                            id: selected.id
                        },
                        processData: true,
                        dataType: "json",
                        async: true,
                        success: function (data) {
                            if (data.code == "200") {
                                if (that.options.target != 'datagrid') {
                                    that.element[that.options.target]('remove', selected.id);
                                } else {
                                    that.element[that.options.target]('deleteRow', that.element[that.options.target]('getRowIndex', selected));
                                }
                            } else {
                                $.messager.alert('错误', data.result);
                            }
                        },
                        error: function () {
                            $.messager.alert('错误', '远程访问失败');
                        }
                    });
                }
            });
        },
        openInsert: function () {
            if (!this.endEditing()) {
                $.messager.alert('警告', '有数据正在编辑');
                return;
            }
            var node = this.element[this.options.target]('getSelected');
            if (!node && this.options.target != 'datagrid') {
                $.messager.alert('警告', '请选择一条数据');
                return;
            }

            this.editId = 'temp';
            if (this.options.target == 'datagrid') {
                var index = this.element.datagrid('getRows').length;
                this.editIndex = index;
                this.element.datagrid('appendRow', {
                    id: 'temp'
                });

                this.element[this.options.target]('selectRow', index);
                this.element[this.options.target]('beginEdit', index);
            } else {
                this.element[this.options.target]('append', {
                    parent: node.id,
                    data: [{
                        id: 'temp'
                    }]
                });

                this.element[this.options.target]('select', 'temp');
                this.element[this.options.target]('beginEdit', 'temp');
            }
        }

    };


    $.fn.dataGridEditor = function (options) {

        if (!this.data('plugin_dataGridEditor')) {
            this.data("plugin_dataGridEditor", new DataGridEditor(this, options));
        }
        var that = this;
        this.extend({
            insert: function () {
                that.data('plugin_dataGridEditor').openInsert();
            },
            update: function () {
                that.data('plugin_dataGridEditor').openUpdate();
            },
            cancel: function () {
                that.data('plugin_dataGridEditor').cancelEdit();
            },
            delete: function () {
                that.data('plugin_dataGridEditor').delete();
            },
            save: function () {
                that.data('plugin_dataGridEditor').endEditing();
            }
        });

        return this;
    }
})(jQuery, window, document);