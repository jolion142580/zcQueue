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
    var formData = new FormData();
    var affairMaterials = {};

    affairMaterials.id = $("#id").val();
    affairMaterials.affairid = $("#affairid").val();
    affairMaterials.remarks = $("#remarks").val();
    affairMaterials.matname = $("#matname").val();
    affairMaterials.valid = $("#valid").find("option:selected").val();
    affairMaterials.ismust = $("#ismust").find("option:selected").val();
    var file = $('#file')[0].files[0];
    formData.append("affairMaterials", JSON.stringify(affairMaterials));
    formData.append("file", file);
    $.ajax({
        cache: true,
        type: "POST",
        url: "/ss/affairmaters/update",
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
            remarks: {

            },

            matname: {

            },
            valid: {

            },
            ismust: {

            }
        },
        messages: {
            remarks: {

            },
            matname: {

            },
            valid: {

            },
            ismust: {

            }
        }
    })
}
