$().ready(function () {
    validateRule();
});

$.validator.setDefaults({
    submitHandler: function () {
        save();
    }
});


function save() {
    var data = $('#signupForm').serialize();
    var file = $('#file')[0].files[0];

    var affairMaterials = {};
    affairMaterials.remarks = $("#remarks").val();
    affairMaterials.matname = $("#matname").val();
    affairMaterials.valid = $("#valid").find("option:selected").val();
    affairMaterials.ismust = $("#ismust").find("option:selected").val();


    var formData = new FormData();
    formData.append("affairMaterials", JSON.stringify(affairMaterials));
    formData.append("objId", $("#objId").val());
    formData.append("file", file);

    console.log(formData);

    $.ajax({
        cache: true,
        type: "POST",
        url: "/ss/affairmaters/save",
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

