$(function () {
    /*   $('.yearpicker').datepicker({
           startView: 'decade',
           minView: 'decade',
           format: 'yyyy',
           maxViewMode: 2,
           minViewMode: 2,
           autoclose: true,
           changeMonth: true,
           changeYear: true,
           yearRange: 'c:c+10'
       });*/
    var date = new Date();
    var year = date.getFullYear();
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
        update();
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

function update() {
    $.ajax({
        cache: true,
        type: "POST",
        url: "/ss/blackWhiteList/update",
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