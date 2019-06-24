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
    var templatefile = {};
    templatefile.id = $("#id").val();
    templatefile.name = $("#name").val();
    templatefile.remark = $("#remark").val();

    var formData = new FormData();
    var file = $('#file')[0].files[0];
    formData.append("file", file);
    formData.append("templatefile", JSON.stringify(templatefile));

    $.ajax({
        cache: true,
        type: "POST",
        url: "/ss/templatefile/update",
        data: formData,// 你的formid
        async: false,
        contentType: false,
        processData: false,
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
            name: {

            },

            remark: {

            }
        },
        messages: {
            name: {

            },
            remark: {

            }
        }
    })
}
