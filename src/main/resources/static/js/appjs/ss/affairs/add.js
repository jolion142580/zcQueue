$().ready(function () {
    validateRule();
});

$.validator.setDefaults({
    submitHandler: function () {
        save();
    }
});


$("#chooseDept").change(function () {
    var departId = $("#chooseDept").find("option:selected").val();
    $.ajax({
        cache: true,
        type: "get",
        url: "/ss/affairs/choosetemplate",
        data: {"departId": departId},// 你的formid
        async: false,
        error: function (request) {
            parent.layer.alert("Connection error");
        },
        success: function (data) {
            $("#template").html("");
            $("#template").append("<option value='' >请选择</option>");
            // $("#template").isEmpty();
            for (var i = 0; i < data.length; i++) {
                $("#template").append("<option value=" + data[i].id + ">" + data[i].filename + "</option>");
                /* if (data.code == 0) {
                     parent.layer.msg("操作成功");
                     parent.reLoad();
                     var index = parent.layer.getFrameIndex(window.name); // 获取窗口索引
                     parent.layer.close(index);

                 } else {
                     parent.layer.alert(data.msg)
                 }
    */
            }
        }

    });
})

function save() {
    var data = $('#signupForm').serialize();
    $.ajax({
        cache: true,
        type: "POST",
        url: "/ss/affairs/save",
        data: $('#signupForm').serialize() + "&departId=" + $("#chooseDept").find("option:selected").val() + "&sortId=" + $("#chooseLife").find("option:selected").val()
        + "&templateid=" + $("#template").find("option:selected").val(),// 你的formid
        async: false,
        error: function (request) {
            parent.layer.alert("Connection error");
        },
        success: function (data) {
            if (data.code == 0) {
                parent.layer.msg("操作成功");
                parent.reLoad();
                var index = parent.layer.getFrameIndex(window.name); // 获取窗口索引
                parent.layer.close(index);

            } else {
                parent.layer.alert(data.msg)
            }

        }
    });

}

/*
function chooseDept() {
    layer.open({
        type: 2,
        title: '选择部门',
        maxmin: true,
        shadeClose: false, // 点击遮罩关闭层
        area: ['500px', '320px'],
        content: '/ss/affairs/choosedept'
    });
}*/

function validateRule() {
    var icon = "<i class='fa fa-times-circle'></i> ";
    $("#signupForm").validate({
        rules: {
            affairName: {
                // required: true

            },

            level: {

            },
            timeLimitType: {


            },

            timeLimit: {


            },
            code: {

            },

            resultForm: {


            },

        },
        messages: {


            affairName: {

            },
            level: {

            },
            timeLimitType: {

            },

            timeLimit: {


            },

            resultForm: {


            },
            code: {

            },


            isTodo: {


            }
        }
    })
}

