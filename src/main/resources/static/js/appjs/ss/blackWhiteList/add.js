$(function () {
    var date = new Date();
    var year = date.getFullYear();
    $("#time").val(year);
    $('input[name="forever"]').change(function () {
        var forever = $(this).val();
        if (forever == 1) {
            $("#time").attr("disabled", "disabled");
        } else {
            $("#time").removeAttr("disabled");
            $("#time").val(year);
        }
    })
})
$().ready(function () {
    validateRule();
});

$.validator.setDefaults({
    submitHandler: function () {
        save();
    }
});


function validateRule() {
    var icon = "<i class='fa fa-times-circle'></i> ";
    $("#signupForm").validate({
        rules: {
            name:{
                required: true
            },
            idCard: {
                required: true
            }
        },
        messages: {
            name: {
                required: icon + "请输入用户名"
            },
            idCard: {
                required: icon + "请输入身份证"
            }
        }
    })
}

function save() {
    $.ajax({
        cache: true,
        type: "POST",
        url: "/ss/blackWhiteList/save",
        data: $('#signupForm').serialize(),// 你的formid
        async: false,
        error: function (request) {
            alert("Connection error");
        },
        success: function (data) {
            if (data.code == 0) {
                parent.layer.msg(data.msg);
                parent.reLoad();
                var index = parent.layer.getFrameIndex(window.name); // 获取窗口索引
                parent.layer.close(index);

            } else {
                parent.layer.msg(data.msg);
            }

        }
    });
}