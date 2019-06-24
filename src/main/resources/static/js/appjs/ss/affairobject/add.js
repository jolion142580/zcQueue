$().ready(function () {
    validateRule();
});

$.validator.setDefaults({
    submitHandler: function () {
        save();
    }
});

function save() {
    $.ajax({
        cache: true,
        type: "POST",
        url: "/ss/affairobject/save",
        data: $('#signupForm').serialize() + "&templateid=" + $("#template").find("option:selected").val(),// 你的formid
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

function validateRule() {
    var icon = "<i class='fa fa-times-circle'></i> ";
    $("#signupForm").validate({
        rules: {
           /* objIndex: {
                required: true
            },
            objName: {
                required: true
            },
            affairId: {
                required: true,
            }*/
        },
        messages: {


            objIndex: {
                required: icon + "不能为空"
            },


            objName: {
                required: icon + "不能为空"
            },
            affairId: {
                required: icon + "不能为空"
            }
        }
    })
}

