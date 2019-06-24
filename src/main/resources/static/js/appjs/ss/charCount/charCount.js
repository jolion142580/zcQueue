var prefix = "/ss/chart"
$(function () {
    load();
    // charInit();
    getCharData();
});

function load() {
    $('#exampleTable')
        .bootstrapTable(
            {
                method: 'get', // 服务器数据的请求方式 get or post
                url: prefix + "/queryList", // 服务器数据的加载地址
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
                /*   pageSize: 10, // 如果设置了分页，每页数据条数
                   pageNumber: 1, // 如果设置了分布，首页页码*/

                limit: 10, // 如果设置了分页，每页数据条数
                offset: 1, // 如果设置了分布，首页页码
                // search : true, // 是否显示搜索框
                showColumns: false, // 是否显示内容下拉框（选择显示的列）
                sidePagination: "server", // 设置在哪里进行分页，可选值为"client" 或者
                queryParams: function (params) {
                    return {
                        limit: params.limit, //每一页的数据行数，默认是上面设置的10(pageSize)
                        offset: params.offset / params.limit + 1, //当前页面,默认是上面设置的1(pageNumber)
                        sort: params.sort,      //排序列名
                        order: params.sortOrder, //排位命令（desc，asc）
                        receiverName: $("#searchName").val()
                    };
                },

                columns: [
                    {
                        checkbox: true
                    },
                    {
                        field: 'registerUserCount',
                        title: '注册人数',
                    },
                    {
                        field: 'commitAffairCount',
                        title: '办事量',
                    },
                    {
                        field: 'handlingMattersCount',
                        title: '办结量',
                    }
                ]
            });
}

function reLoad() {
    $('#exampleTable').bootstrapTable('refresh');
}


function confirm() {

    var startTime = $("#startTime").val();
    var endTime = $("#endTime").val();

    if (startTime != null && startTime != '') {
        if (endTime == null || endTime == '') {
            layer.alert("请选择正确的日期格式");
            return;
        }
    }

    if (endTime != null && endTime != '') {
        if (startTime == null || startTime == '') {
            layer.alert("请选择正确的日期格式");
            return;
        }
    }

    var opt = {
        query: {
            startTime: startTime,
            endTime: endTime
        }
    }
    $('#exampleTable').bootstrapTable('refresh', opt);
    getCharData(startTime, endTime);
}


//一异步获取图表数据
function getCharData(startTime, endTime) {
    var barChart = echarts.init(document.getElementById("echarts-bar-chart"));

    // barChart.showLoading();
    $.ajax({
        cache: false,
        type: "get",
        url: prefix+"/calculateDateTotal",
        data: {"startTime": startTime, "endTime": endTime},
        async: false,
        error: function (request) {
            alert("Connection error");
        },
        success: function (data) {
            console.log(JSON.stringify(data));
            if (data.code == 0) {
                var affairName = [];
                var degreeOfSatisfaction = [];
                if(data.data.length<1){
                    layer.msg("没有相关数据");
                    return;
                }
                for (var i = 0; i < data.data.length; i++) {
                    affairName.push(data.data[i].affairName);
                    degreeOfSatisfaction.push(data.data[i].affairCount);
                }
                setOption(affairName, degreeOfSatisfaction);
            }
            else {
                layer.msg(data.msg);
            }

        }
    });
}


function setOption(affairName, degreeOfSatisfaction) {
    console.log("affairname:"+affairName);
    var barChart = echarts.init(document.getElementById("echarts-bar-chart"));

    barChart.setOption({

        title: {
            text: '前五办事量',
        },
        tooltip: {
            trigger: 'axis'
        },
        legend: {
            data: ['办事量']
        },
        toolbox: {
            show: true,
            feature: {
                mark: {show: true},
                dataView: {show: true, readOnly: false},
                magicType: {show: true, type: ['line', 'bar']},
                restore: {show: true},
                saveAsImage: {show: true}
            }
        },
        calculable: true,
        xAxis: [
            {
                type: 'category',
                data: affairName,
                axisLabel: {//坐标轴刻度标签的相关设置。
                    formatter: function (params) {
                        var newParamsName = "";// 最终拼接成的字符串
                        var paramsNameNumber = params.length;// 实际标签的个数
                        var provideNumber = 15;// 每行能显示的字的个数
                        var rowNumber = Math.ceil(paramsNameNumber / provideNumber);// 换行的话，需要显示几行，向上取整
                        /**
                         * 判断标签的个数是否大于规定的个数， 如果大于，则进行换行处理 如果不大于，即等于或小于，就返回原标签
                         */
                        // 条件等同于rowNumber>1
                        if (paramsNameNumber > provideNumber) {
                            /** 循环每一行,p表示行 */
                            for (var p = 0; p < rowNumber; p++) {
                                var tempStr = "";// 表示每一次截取的字符串
                                var start = p * provideNumber;// 开始截取的位置
                                var end = start + provideNumber;// 结束截取的位置
                                // 此处特殊处理最后一行的索引值
                                if (p == rowNumber - 1) {
                                    // 最后一次不换行
                                    tempStr = params.substring(start, paramsNameNumber);
                                } else {
                                    // 每一次拼接字符串并换行
                                    tempStr = params.substring(start, end) + "\n";
                                }
                                newParamsName += tempStr;// 最终拼成的字符串
                            }

                        } else {
                            // 将旧标签的值赋给新标签
                            newParamsName = params;
                        }
                        //将最终的字符串返回
                        return newParamsName
                    },
                    interval:0,
                }
            },
        ],
        yAxis: [
            {
                type: 'value'
            }
        ],
        grid: {//直角坐标系内绘图网格
            // show: true,//是否显示直角坐标系网格。[ default: false ]
            left:"20%",//grid 组件离容器左侧的距离。
            bottom: "30%", //
            // right:"30px",
            borderColor: "#c45455",//网格的边框颜色
        },

        series: [
            {
                name: '办事量',
                type: 'bar',
                barWidth: 30,
                data: degreeOfSatisfaction,
                markPoint: {
                    data: [
                        {type: 'max', name: '最大值'},
                        {type: 'min', name: '最小值'}
                    ]
                },
            }
        ]

    });


}


