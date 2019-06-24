var prefix = '/ss/pretrialopinion';
$(function () {
    var id = '';
    // getTreeData();
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
                        opinionindex: $('#opinionIndex').val()

                    };
                },
                columns: [
                    /* {
                         checkbox: true
                     },*/
                    {
                        field: 'opiniontext',
                        title: '',

                    }
                ],
                onClickRow: function (row) {
                    $.ajax({
                        type: 'POST',
                        data: {
                            "id": row.id,
                            "onlineApplyId": $("#onlineapplyid").val(),
                            "event": $("#event").val()

                        },
                        url: '/ss/pretrialopinion/submit',
                        success: function (r) {
                            if (r.code == 0) {
                                // parent.reLoad();
                                var buttonId = r.buttonId;
                                var index = parent.layer.getFrameIndex(window.name);// 获取窗口索引
                                chooseopinion(buttonId, r.context);
                                parent.$("#pretrialopinionId").val(row.id);
                                parent.openMaterials(row.id);
                                parent.layer.close(index);
                                //loadFileInfo(onlineApplyId);
                            }
                        }
                    });
                }
            });
}


function chooseopinion(buttonId, text) {
    //部门意见模板
    if (buttonId == 'opinionbutton') {
        parent.$("#departopinion").val(text);
    }
    //审核不通过意见模板
    if (buttonId == 'opinionfailure') {
        parent.$("#opinion").val(text);
        parent.$("#opinionInit").val(text);
    }
    //审核意见模板
    if (buttonId == 'opinionidea') {
        parent.$("#remark").val(text);
    }

}

/*
$("#submit").click(function () {
    var rows = $('#exampleTable').bootstrapTable('getSelections'); // 返回所有选择的行，当没有选择的记录时，返回一个空数组
    if (rows.length == 0) {
        layer.msg("请选择要使用的模板");
        return;
    }
    if (rows.length > 1) {
        layer.msg("只能选择一个模板");
        return;
    }
    layer.confirm("确认选中这个模板？", {
        btn: ['确定', '取消']
        // 按钮
    }, function () {

        $.ajax({
            type: 'POST',
            data: {
                "id": rows[0].id,
                "onlineApplyId": $("#onlineapplyid").val()
            },
            url: '/ss/pretrialopinion/submit',
            success: function (r) {
                if (r.code == 0) {
                    parent.reLoad();
                    var index = parent.layer.getFrameIndex(window.name); // 获取窗口索引
                    parent.layer.close(index);
                    //loadFileInfo(onlineApplyId);
                } else {
                    layer.msg(r.msg);
                }
            }
        });
    }, function () {
    });

})*/
