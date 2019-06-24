// 以下为官方示例
$().ready(function () {
    validateRule();
    // $("#signupForm").validate();
});

$.validator.setDefaults({
    submitHandler: function () {
        update();
    }
});

function validateRule() {
    var icon = "<i class='fa fa-times-circle'></i> ";
    $("#signupForm").validate({
        rules: {
            pwdNew: {
                userrule: true,
                required: true,
                minlength: 8
            }
        },
        messages: {
            pwdNew: {
                userrule: icon + "密码长度为8位以上，并且密码要包含大小字母、数字、符号",
                required: icon + "请输入您的密码",
                minlength: icon + "密码必须8个字符以上"
            }
        }
    })
}


function update() {
    $.ajax({
        cache: true,
        type: "POST",
        url: "/sys/user/adminResetPwd",
        data: $('#signupForm').serialize(),// 你的formid
        async: false,
        error: function (request) {
            parent.layer.msg("系统错误，联系管理员");
        },
        success: function (data) {
            if (data.code == 0) {
                parent.layer.msg(data.msg);
                parent.reLoad();
                var index = parent.layer.getFrameIndex(window.name); //获取窗口索引
                parent.layer.close(index);

            } else {
                parent.layer.msg(data.msg);
            }

        }
    });

}