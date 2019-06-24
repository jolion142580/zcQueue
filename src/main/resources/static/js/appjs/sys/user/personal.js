var prefix = "/sys/user"
$(function () {
    var $birth = $("#birth");
    if ($("#birth").size() > 0) {
        laydate({
            elem: '#birth'
        });
    }

});


/*function validateRule() {
    var icon = "<i class='fa fa-times-circle'></i> ";
    $("#modifyPwd").validate({
        rules: {
            pwdNew: {
                userrule: true,
                required: true
            },
            confirm_password: {
                userrule: true,
                equalTo: "#pwdNew",
                required: true,
                // minlength: 2
            }

        },
        messages: {

            pwdNew: {
                userrule: "密码长度为8位以上，并且密码要包含大小字母、数字、符号",

                required: icon + "请输入姓名"
            },
            confirm_password: {
                userrule: "密码长度为8位以上，并且密码要包含大小字母、数字、符号",
                required: icon + "请再次输入密码",
                minlength: icon + "密码必须6个字符以上",
                equalTo: icon + "两次输入的密码不一致"
            }
        }
    })
}*/

/**
 * 基本信息提交
 */
$("#base_save").click(function () {
    var hobbyStr = getHobbyStr();
    $("#hobby").val(hobbyStr);
    if ($("#basicInfoForm").valid()) {
        $.ajax({
            cache: true,
            type: "POST",
            url: "/sys/user/updatePeronal",
            data: $('#basicInfoForm').serialize(),
            async: false,
            error: function (request) {
                laryer.alert("Connection error");
            },
            success: function (data) {
                if (data.code == 0) {
                    parent.layer.msg("更新成功");
                } else {
                    parent.layer.alert(data.msg)
                }
            }
        });
    }

});
$("#pwd_save").click(function () {
    var reg = /^(?=.*[a-zA-Z])(?=.*\d)(?=.*[~!@#$%^&*()_+`\-={}:";'<>?,.\/]).{4,16}$/;
    var icon = "<i class='fa fa-times-circle'></i> ";
    /*&& reg.test($("#pwdNew").val()) && reg.test($("#confirm_password").val())*/
    if ($("#modifyPwd").valid()) {
        $.ajax({
            cache: true,
            type: "POST",
            url: "/sys/user/resetPwd",
            // data: $('#modifyPwd').serialize(),
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
                    parent.layer.alert("更新密码成功");
                    $("#photo_info").click();
                } else {
                    parent.layer.alert(data.msg)
                }
            }
        });
    }
    // else{
    //     $("#pwdNew-error").val("faklsjdflkjasdlkjf");
    //     parent.layer.alert("密码长度为8位以上，并且密码要包含大小字母、数字、符号");
    // }

});

function pwd_save() {
    $.ajax({
        cache: true,
        type: "POST",
        url: "/sys/user/resetPwd",
        data: $('#modifyPwd').serialize(),
        async: false,
        error: function (request) {
            parent.laryer.alert("Connection error");
        },
        success: function (data) {
            if (data.code == 0) {
                parent.layer.alert("更新密码成功");
                $("#photo_info").click();
            } else {
                parent.layer.alert(data.msg)
            }
        }
    });
}

function getHobbyStr() {
    var hobbyStr = "";
    $(".hobby").each(function () {
        if ($(this).is(":checked")) {
            hobbyStr += $(this).val() + ";";
        }
    });
    return hobbyStr;
}