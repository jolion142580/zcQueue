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
        url: "/ss/basedic/save",
        data: $('#signupForm').serialize(),
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
            cName: {
                required: true,
                //
            },
            iconPath: {
                required: true,
                //
            },
            baseDicType: {
                required: true,
                //
            }
        },
        messages: {
            cName: {
                required: icon + "请输入名称",

            },
            iconPath: {
                required: icon + "请选择图标",


            },
            baseDicType: {
                required: icon + "请填写类型",



            }
        }
    })
}

