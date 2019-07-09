var prefix = "/ss/blackWhiteList";
$(function () {
    load();
});

function load() {
    $('#exampleTable').bootstrapTable({
        method: 'get', // 服务器数据的请求方式 get or post
        url: prefix + "/list", // 服务器数据的加载地址
        striped: true, // 设置为true会有隔行变色效果
        dataType: "json", // 服务器返回的数据类型
        pagination: true, // 设置为true会在底部显示分页条
        sortable: true,                     //是否启用排序
        sortOrder: "desc",
        singleSelect: false, // 设置为true将禁止多选
        pageSize: 10, // 如果设置了分页，每页数据条数
        pageNumber: 1, // 如果设置了分布，首页页码
        showColumns: false, // 是否显示内容下拉框（选择显示的列）
        sidePagination: "server", // 设置在哪里进行分页，可选值为"client" 或者
        queryParams: function (params) {
            return {
                // 说明：传入后台的参数包括offset开始索引，limit步长，sort排序列，order：desc或者,以及所有列的键值对
                pageSize: params.limit, //每一页的数据行数，默认是上面设置的10(pageSize)
                pageNumber: params.offset / params.limit + 1, //当前页面,默认是上面设置的1(pageNumber)
                sort: params.sort,      //排序列名
                order: params.order, //排位命令（desc，asc）
                name: $('#name').val(),
                idCard: $("#idCard").val()
            };
        },
        columns: [{
            title: '操作',
            align: 'center',
            formatter: function (value, row, index) {
                var e = '<a  class="btn btn-primary btn-sm" href="#" mce_href="#" title="编辑" onclick="edit(\''
                    + row.id
                    + '\')"><i class="fa fa-edit "></i></a> ';
                var f = '<a  class="btn btn-danger btn-sm" href="#" mce_href="#" title="删除" onclick="remove(\''
                    + row.id
                    + '\')"><i class="fa fa-remove "></i></a> ';
                return e + f;
            }
        }, {
            field: 'name',
            align: 'center',
            title: '用户'
        }, {
            field: 'idCard',
            align: 'center',
            title: '身份证'
        }, {
            field: 'phone',
            align: 'center',
            title: '手机号'
        }, {
            field: 'time',
            title: '限制年份',
            align: 'center'
        }, {
            field: 'flag',
            align: 'center',
            title: '黑|白名单',
            formatter: function (value, row, index) {
                if (value == 1) {
                    return "白名单";
                } else if (value == 0) {
                    return "黑名单";
                } else {
                    return "";
                }
            }
        }, {
            field: 'forever',
            align: 'center',
            title: '是否永久',
            formatter: function (value, row, index) {
                if (value == 1) {
                    return "是";
                } else {
                    return "-";
                }
            }
        }]
    });
}

function reLoad() {
    $('#exampleTable').bootstrapTable('refresh');
}

function add() {
    layer.open({
        type : 2,
        title : '编辑',
        maxmin : true,
        shadeClose : false, // 点击遮罩关闭层
        area : [ '800px', '520px' ],
        content : prefix + '/add'
    });
}

function edit(id) {
    layer.open({
        type : 2,
        title : '编辑',
        maxmin : true,
        shadeClose : false, // 点击遮罩关闭层
        area : [ '800px', '520px' ],
        content : prefix + '/edit/' + id // iframe的url
    });
}

function remove(id) {
    layer.confirm('确定要删除选中的记录？', {
        btn: ['确定', '取消']
    }, function () {
        $.ajax({
            url: prefix + "/remove/" + id,
            type: "post",
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