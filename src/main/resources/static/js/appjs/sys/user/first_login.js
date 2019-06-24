$().ready(function () {
    validateRule();
});

$.validator.setDefaults({
    submitHandler: function () {
        firstResetPwd();
    }
});

function firstResetPwd() {
    $.ajax({
        type: "POST",
        url: "/sys/user/firstResetPwd",
        data: {
            "userDO.userId": $("#currentUser").val(), "pwdOld": Encrypt($("#pwdOld").val()),
            "pwdNew": Encrypt($("#pwdNew").val()), "confirm_password": Encrypt($("#confirm_password").val())
        },
        async: false,
        error: function (request) {
            parent.laryer.alert("Connection error");
        },
        success: function (data) {
            if (data.code == 0) {
                parent.layer.msg(data.msg);
                // parent.reLoad();
                var index = parent.layer.getFrameIndex(window.name); // 获取窗口索引
                parent.layer.close(index);

            } else {
                layer.alert(data.msg)
            }
        }
    });
}

function validateRule() {
    var icon = "<i class='fa fa-times-circle'></i> ";
    $("#signupForm").validate({
        rules: {
            pwdOld: {
                required: true,
                minlength: 6
                //
            },
            pwdNew: {
                userrule: true,
                required: true,
                //
            },
            confirm_password: {
                userrule: true,
                required: true,
                equalTo: "#pwdNew"
                //
            }
        },
        messages: {
            pwdOld: {
                minlength: icon + "旧密码必须六个字符以上",
                required: icon + "请输入名称",
                //
            },
            pwdNew: {
                userrule: "密码长度为8位以上，并且密码要包含大小字母、数字、符号",
                required: icon + "请选择图标",
                //

            },
            confirm_password: {
                userrule: "密码长度为8位以上，并且密码要包含大小字母、数字、符号",
                required: icon + "请填写类型",
                equalTo: icon + "两次输入的密码不一致"
            }
        }
    })
}
