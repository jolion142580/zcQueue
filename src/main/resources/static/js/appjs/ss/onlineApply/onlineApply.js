var prefix = "/ss/onlineApply"
var baseDicUrl = "";
var onlineApplyId = "";

var id = '';
$(function () {
    var id = '';
    // getTreeData();
    load();
});

$("#baseul").change(function () {
    baseDicUrl = $("#baseul").find("option:selected").val();
});


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

function reLoad() {
    $('#exampleTable').bootstrapTable('refresh');
    /*$("#dropdownMenu1").text("请选择");
    baseDicUrl="";*/
}

function edit(id) {
    layer.open({
        type: 2,
        title: '网上办事修改',
        maxmin: true,
        shadeClose: false,
        area: ['800px', '520px'],
        content: prefix + '/edit/' + id // iframe的url
    });
}

function batchRemove() {

    var rows = $('#exampleTable1').bootstrapTable('getSelections'); // 返回所有选择的行，当没有选择的记录时，返回一个空数组
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
            ids[i] = row['id'];
        });
        $.ajax({
            type: 'POST',
            data: {
                "ids": ids
            },
            url: '/ss/fileInfo/batchRemove',
            success: function (r) {
                if (r.code == 0) {
                    layer.msg(r.msg);
                    loadFileInfo(onlineApplyId);
                } else {
                    layer.msg(r.msg);
                }
            }
        });
    }, function () {
    });
}


function remove(id) {
    layer.confirm('确定要删除选中的记录？', {
        btn: ['确定', '取消']
    }, function () {
        $.ajax({
            url: "/ss/fileInfo/remove/" + id,
            type: "post",
            data: {
                'id': id
            },
            success: function (r) {
                if (r.code == 0) {
                    layer.msg(r.msg);
                    loadFileInfo(onlineApplyId);
                } else {
                    layer.msg(r.msg);
                }
            }
        });
    })
}

function loadPDF(id) {
    $.ajax({
            url: "/ss/onlineApply/loadPDF/" + id,
        type: "post",
        data: {
            'id': id
        },
        success: function (r) {
            if (r.code == 0) {
                layer.msg(r.msg);
                loadFileInfo(id);
            } else {
                layer.msg(r.msg);
            }
        }
    });
}

function load() {
    $('#exampleTable')
        .bootstrapTable(
            {
                method: 'get', // 服务器数据的请求方式 get or post
                url: prefix + "/list", // 服务器数据的加载地址
                // showRefresh : true,
                // showToggle : true,
                // showColumns : true,
                iconSize: 'outline',
                toolbar: '#exampleToolbar',
                striped: true, // 设置为true会有隔行变色效果
                dataType: "json", // 服务器返回的数据类型
                pagination: true, // 设置为true会在底部显示分页条
                sortable: true,                     //是否启用排序
                sortOrder: "desc",
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
                        // 说明：传入后台的参数包括offset开始索引，limit步长，sort排序列，order：desc或者,以及所有列的键值对
                        pageSize: params.limit, //每一页的数据行数，默认是上面设置的10(pageSize)
                        pageNumber: params.offset / params.limit + 1, //当前页面,默认是上面设置的1(pageNumber)
                        sort: params.sort,      //排序列名
                        order: params.order, //排位命令（desc，asc）
                        name: $('#name').val(),
                        state:$('#chooseState').val()

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
                        field: 'affairname',
                        title: '名称'
                    },
                    {
                        field: 'objname',
                        title: '事项对象'
                    },
                    {
                        field: 'name',
                        title: '姓名',

                    },
                    {
                        field: 'state',
                        title: '状态',

                    },
                    {
                        field: 'creattime',
                        title: '创建时间',

                    },
                    {
                        field: 'limitdate',
                        title: '有限期限',

                    },

                    {
                        title: '操作',
                        field: 'id',
                        align: 'center',
                        formatter: function (value, row, index) {
                            var e = '<a  class="btn btn-primary btn-sm ' + s_edit_h + '" href="#" mce_href="#" title="编辑" onclick="edit(\''
                                + row.id
                                + '\')"><i class="fa fa-edit "></i></a> ';
                            /* var d = '<a class="btn btn-warning btn-sm ' + s_remove_h + '" href="#" title="删除"  mce_href="#" onclick="remove(\''
                                 + row.id
                                 + '\')"><i class="fa fa-remove"></i></a> ';*/
                            var f = "";
                            if (row.onlinedata != null) {
                                f = '<a class="btn btn-warning btn-sm " href="#" title="生成PDF"  mce_href="#" onclick="loadPDF(\''
                                    + row.id
                                    + '\')"><i class="fa fa-file-word-o"></i></a> ';
                            }

                            return e + f;
                        }
                    }],
                onClickRow: function (row) {
                    //     console.log("click:" + row.playerName)
                    //alert(row.id);

                    loadFileInfo(row.id);
                }
            });


}

$('#exampleTable1')
    .bootstrapTable(
        {
            method: 'get', // 服务器数据的请求方式 get or post
            url: "/ss/fileInfo/list", // 服务器数据的加载地址
            // showRefresh : true,
            // showToggle : true,
            // showColumns : true,
            iconSize: 'outline',
            toolbar: '#exampleToolbar',
            striped: true, // 设置为true会有隔行变色效果
            dataType: "json", // 服务器返回的数据类型
            pagination: true, // 设置为true会在底部显示分页条
            sortable: true,                     //是否启用排序
            sortOrder: "desc",
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
                    // 说明：传入后台的参数包括offset开始索引，limit步长，sort排序列，order：desc或者,以及所有列的键值对
                    pageSize: params.limit, //每一页的数据行数，默认是上面设置的10(pageSize)
                    pageNumber: params.offset / params.limit + 1, //当前页面,默认是上面设置的1(pageNumber)
                    sort: params.sort,      //排序列名
                    order: params.order, //排位命令（desc，asc）
                    onlineapplyid: onlineApplyId

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
                    field: 'remark',
                    title: '名称'
                },
                {
                    title: '操作',
                    field: 'id',
                    align: 'center',
                    formatter: function (value, row, index) {

                        var d = '<a class="btn btn-warning btn-sm ' + f_remove_h + '" href="#" title="删除"  mce_href="#" onclick="remove(\''
                            + row.id
                            + '\')"><i class="fa fa-remove"></i></a> ';


                        return d;
                    }
                }
            ],
            onDblClickRow: function (row) {
                //     console.log("click:" + row.playerName)
                // alert(row.id);
                //iframe层
                parent.layer.open({
                    type: 2,
                    title: '查看附件',
                    shadeClose: true,
                    shade: 0.8,
                    area: ['90%', '90%'],
                    content: '/ss/fileInfo/loadImage/' + row.id //iframe的url
                });

            }
        });


function loadFileInfo(onlineApplyid) {
    onlineApplyId = onlineApplyid;
    var options = $('#exampleTable1').bootstrapTable('refresh', {
        query:
            {
                onlineapplyid: onlineApplyid
            }
    });

}

function submitAffair() {

    var rows = $('#exampleTable1').bootstrapTable('getSelections'); // 返回所有选择的行，当没有选择的记录时，返回一个空数组
    if (rows.length == 0) {
        layer.msg("请选择要上传的数据");
        return;
    }
    layer.confirm("确认要上传选中的'" + rows.length + "'条数据吗?", {
        btn: ['确定', '取消']
        // 按钮
    }, function () {
        var ids = new Array();
        // 遍历所有选择的行数据，取每条数据对应的ID
        $.each(rows, function (i, row) {
            ids[i] = row['id'];
        });
        $.ajax({
            type: 'POST',
            data: {
                "id": onlineApplyId,
                "fileIds": ids
            },
            url: '/ss/onlineApply/submitAffair',
            success: function (r) {
                if (r.code == 0) {
                    layer.msg(r.msg);
                    //loadFileInfo(onlineApplyId);
                } else {
                    layer.msg(r.msg);
                }
            }
        });
    }, function () {
    });
}


function ymsHistory() {
    var rows = $('#exampleTable').bootstrapTable('getSelections'); // 返回所有选择的行，当没有选择的记录时，返回一个空数组
    if (rows.length == 0) {
        layer.msg("请选择要查看的数据");
        return;
    }
    if (rows.length > 1) {
        layer.msg("请选择1条数据进行查看");
        return;
    }
    var index = layer.open({
        type: 2,
        title: '上传一门式记录',
        maxmin: true,
        shadeClose: false,
        area: ['900px', '520px'],
        content: prefix + '/history/' + rows[0].id // iframe的url
    });
    layer.full(index);
}



