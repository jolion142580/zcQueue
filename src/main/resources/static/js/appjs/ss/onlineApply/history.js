var prefix = "/ss/onlineApply";
var currAffaircode = '';
$(function () {
    load();
});

function load() {
    $('#exampleTable')
        .bootstrapTable(
            {
                method: 'get', // 服务器数据的请求方式 get or post
                url: prefix + "/historyByRecord", // 服务器数据的加载地址
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
                        onlineApplyId: $('#onlineAppId').val()

                    };
                },

                columns: [
                    {
                        checkbox: true
                    },
                    {
                        field: 'currAffaircode',
                        title: '编号'
                    },
                    {
                        field: 'auditOpinion',
                        title: '预审意见'
                    },
                    {
                        field: 'statueDesc',
                        title: '结果',

                    }],
                /*{
                    title: '操作',
                    field: 'id',
                    align: 'center',
                    formatter: function (value, row, index) {
                        var e = '<a  class="btn btn-primary btn-sm ' + s_edit_h + '" href="#" mce_href="#" title="编辑" onclick="edit(\''
                            + row.id
                            + '\')"><i class="fa fa-edit "></i></a> ';
                        /!* var d = '<a class="btn btn-warning btn-sm ' + s_remove_h + '" href="#" title="删除"  mce_href="#" onclick="remove(\''
                             + row.id
                             + '\')"><i class="fa fa-remove"></i></a> ';*!/
                        var f = "";
                        if (row.onlinedata != null) {
                            f = '<a class="btn btn-warning btn-sm " href="#" title="生成PDF"  mce_href="#" onclick="loadPDF(\''
                                + row.id
                                + '\')"><i class="fa fa-file-word-o"></i></a> ';
                        }

                        return e + f;
                    }
                }],*/
                onClickRow: function (row) {
                    currAffaircode = row.currAffaircode;
                    loadRecord(currAffaircode);
                }
            });
}

function loadRecord(onlineApplyid) {
    var options = $('#exampleTable1').bootstrapTable('refresh', {
        query:
            {
                currAffaircode: currAffaircode
            }
    });

}


$('#exampleTable1')
    .bootstrapTable(
        {
            method: 'get', // 服务器数据的请求方式 get or post
            url: prefix + "/ymsFileList", // 服务器数据的加载地址
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
                    currAffaircode: currAffaircode

                };
            },
            columns: [
                {
                    checkbox: true
                },
                {
                    field: 'filename',
                    title: '名称'
                }],
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
        })



