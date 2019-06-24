$().ready(function () {
    validateRule();
});

$.validator.setDefaults({
    submitHandler: function () {
        save();
    }
});

function save() {
    var file = $('#file')[0].files[0];
    var templatefile = {};
    templatefile.departid = $("#departid").val();
    templatefile.name = $("#name").val();
    templatefile.remark = $("#remark").val();

    var formData = new FormData();
    formData.append("file", file);
    formData.append("templatefile", JSON.stringify(templatefile));
    $.ajax({
        cache: true,
        type: "POST",
        url: "/ss/templatefile/save",
        data: formData,// 你的formid
        async: false,
        contentType: false,
        processData: false,
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

