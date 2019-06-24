var prefix = "/ss/affairs"
$(function () {

    load();
});

function load() {
    $('#exampleTable')
        .bootstrapTable(
            {
                method: 'get', // 服务器数据的请求方式 get or post
                url: prefix + "/list", // 服务器数据的加载地址
                // showRefresh : true,
                // showToggle : true,
                // showColumns : true,
                sortable: true,                     //是否启用排序
                sortOrder: "desc",
                iconSize: 'outline',
                toolbar: '#exampleToolbar',
                striped: true, // 设置为true会有隔行变色效果
                dataType: "json", // 服务器返回的数据类型
                pagination: true, // 设置为true会在底部显示分页条
                // queryParamsType : "limit",
                // //设置为limit则会发送符合RESTFull格式的参数
                singleSelect: false, // 设置为true将禁止多选
                // contentType : "application/x-www-form-urlencoded",
                // //发送到服务器的数据编码类型
                pageSize: 10, // 如果设置了分页，每页数据条数
                pageNumber: 1, // 如果设置了分布，首页页码
                // search : true, // 是否显示搜索框
                showColumns: false, // 是否显示内容下拉框（选择显示的列）
                sidePagination: "server", // 设置在哪里进行分页，可选值为"client" 或者
                // "server"
                queryParams: function (params) {
                    return {
                        pageSize: params.limit, //每一页的数据行数，默认是上面设置的10(pageSize)
                        pageNumber: params.offset / params.limit + 1, //当前页面,默认是上面设置的1(pageNumber)
                        sort: params.sort,      //排序列名
                        order: params.sortOrder, //排位命令（desc，asc）
                        affairName: $('#lifeName').val(),
                        departName: $('#departName').val()

                    };
                },
                // //请求服务器数据时，你可以通过重写参数的方式添加一些额外的参数，例如 toolbar 中的参数 如果
                // queryParamsType = 'limit' ,返回参数必须包含
                // limit, offset, search, sort, order 否则, 需要包含:
                // pageSize, pageNumber, searchText, sortName,
                // sortOrder.
                // 返回false将会终止请求
                columns: [
                    {
                        checkbox: true
                    },
                    {
                        field: 'affairName', // 列字段名
                        title: '事项名' // 列标题
                    },
                    {
                        field: 'departName', // 列字段名
                        title: '部门' // 列标题
                    },
                    {
                        field: 'timeLimitType',
                        title: '时限类型',
                    },
                    {
                        field: 'timeLimit',
                        title: '办事时限',
                    },
                    {
                        field: 'lifeName',
                        title: '分类',
                    },
                   /* {
                        field: 'resultForm',
                        title: '结果形式',
                    },*/
                    /*  {
                          field: 'isNet',
                          title: '是否网上可办',
                      },*/
                    {
                        field: 'code',
                        title: '事项编号',
                    },
                    {
                        field: 'isTodo',
                        title: '是否即办',
                        formatter: function (value, row, index) {
                            if (value == '1') {
                                return '即办';
                            } else if (value == '2') {
                                return '流办';
                            }
                        }
                    },
                    {
                        title: '操作',
                        field: 'affairId',
                        align: 'center',
                        formatter: function (value, row, index) {
                            var c = '<a  class="btn btn-success btn-sm ' + s_edit_h + '" href="#" mce_href="#" title="办事指南" onclick="affairGuide(\''
                                + row.affairId
                                + '\')"><i class="fa fa-eye "></i></a> ';

                            var e = '<a  class="btn btn-primary btn-sm ' + s_edit_h + '" href="#" mce_href="#" title="编辑" onclick="edit(\''
                                + row.affairId
                                + '\')"><i class="fa fa-edit "></i></a> ';
                            var d = '<a class="btn btn-warning btn-sm ' + s_remove_h + '" href="#" title="删除"  mce_href="#" onclick="remove(\''
                                + row.affairId
                                + '\')"><i class="fa fa-remove"></i></a> ';
                            /*var f = '<a class="btn btn-success btn-sm " href="#" title="查看详情"  mce_href="#" onclick="detail(\''
                                + row.affairId
                                + '\')"><i class="fa fa-eye"></i></a> ';*/
                            return c + e + d;
                        }
                    }]
            });
}

function reLoad() {
    $('#exampleTable').bootstrapTable('refresh');
}

function affairGuide(affairId) {

    layer.open({
        type: 2,
        title: '办事指南',
        maxmin: true,
        shadeClose: false, // 点击遮罩关闭层
        area: ['900px', '520px'],
        content: '/ss/affairguide/edit/' + affairId
    });
}


function add() {
    // iframe层
    layer.open({
        type: 2,
        title: '增加',
        maxmin: true,
        shadeClose: false, // 点击遮罩关闭层
        area: ['800px', '520px'],
        content: prefix + '/add'
    });
}

function remove(affairid) {
    layer.confirm('确定要删除选中的记录？', {
        btn: ['确定', '取消']
    }, function () {
        $.ajax({
            url: "/ss/affairs/remove/" + affairid,
            type: "post",
            data: {
                'affairid': affairid
            },
            success: function (r) {
                if (r.code == 0) {
                    layer.msg(r.msg);
                    reLoad();
                } else {
                    layer.msg(r.msg);
                }
            }
        });
    })
}

function edit(affairid) {
    layer.open({
        type: 2,
        title: '修改',
        maxmin: true,
        shadeClose: false,
        area: ['800px', '520px'],
        content: prefix + '/edit/' + affairid // iframe的url
    });
}

function batchRemove() {
    var rows = $('#exampleTable').bootstrapTable('getSelections'); // 返回所有选择的行，当没有选择的记录时，返回一个空数组
    if (rows.length == 0) {
        layer.msg("请选择要删除的数据");
        return;
    }
    layer.confirm("确认要删除选中的'" + rows.length + "'条数据吗?", {
        btn: ['确定', '取消']
        // 按钮
    }, function () {
        var ids = new Array();
        // 遍历所有选择的行数据，取每条数据对应的ID
        $.each(rows, function (i, row) {
            ids[i] = row['affairId'];
        });
        $.ajax({
            type: 'POST',
            data: {
                "ids": ids
            },
            url: prefix + '/batchRemove',
            success: function (r) {
                if (r.code == 0) {
                    layer.msg(r.msg);
                    reLoad();
                } else {
                    layer.msg(r.msg);
                }
            }
        });
    }, function () {
    });
}
