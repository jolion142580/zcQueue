// 以下为官方示例
$().ready(function () {
    validateRule();
});

$.validator.setDefaults({
    submitHandler: function () {
        update();
    }
});

function update() {
    $.ajax({
        cache: true,
        type: "POST",
        url: "/ss/affairobject/update",
        data: $('#signupForm').serialize() + "&templateid=" + $("#template").find("option:selected").val()
        + "&templateid1=" + $("#template2").find("option:selected").val(),// 你的formid
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


function validateRule() {
    var icon = "<i class='fa fa-times-circle'></i> ";
    $("#signupForm").validate({
        rules: {
            objname: {

            },
            config: {
                // checkAffairMaters: true
            },
            config1: {
                // checkAffairMaters: true
            }
        },
        messages: {

            objname: {

            },
            config: {
                // checkAffairMaters: "请输入正确名称"
            },
            config1: {
                // checkAffairMaters: "请输入正确名称"
            },

        }
    })
}
