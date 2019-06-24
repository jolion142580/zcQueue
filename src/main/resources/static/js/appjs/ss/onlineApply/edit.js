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
    var url = "";

    if ($("#commitopinion").length > 0) {
        url = "/ss/onlineApplyOpinion/add";
        var nextName = $("#nextName").val();
        var nextUserId = $("#userId").val();
        if (nextName == '') {
            $("#userId").val(" ");
        }
    }
    else {
        url = "/ss/onlineApply/update";
    }
    $.ajax({
        cache: true,
        type: "POST",
        url: url,
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

$("#commitopinion").click(function () {
    update();
});


function validateRule() {
    var icon = "<i class='fa fa-times-circle'></i> ";
    $("#signupForm").validate({
        rules: {
            // departopinion: {
            //     checkAffairMaters: true
            // },
            // opinion: {
            //     checkAffairMaters: true
            // },
            // remark: {
            //     checkAffairMaters: true
            // }
        },
        messages: {
            // departopinion: {
            //     checkAffairMaters: "请输入正确名称"
            // },
            // opinion: {
            //     checkAffairMaters: "请输入正确名称"
            //
            // },
            // remark: {
            //     checkAffairMaters: "请输入正确名称"
            // }
        }
    })
}

var nextUser = function () {
    layer.open({
        type: 2,
        title: "选择部门",
        area : [ '400px', '450px' ],
        content: "/system/sysDept/deptAndUserTreeView"
    })
}

function loadDept(userId, userName) {
    $("#userId").val(userId);
    $("#nextName").val(userName);
}


function sendWxNews() {

    $.ajax({
        cache: true,
        type: "POST",
        url: "/ss/onlineApply/sendWxNews",
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

$("#opinionbutton").click(function () {
    opinionbuttonClick("depart:opinionbutton");
})

$("#opinionfailure").click(function () {
    opinionbuttonClick("pretrial:opinionfailure");
})

$("#opinionidea").click(function () {
    opinionbuttonClick("pretrial:opinionidea");
})


function opinionbuttonClick(opinionIndex) {
    var id = $("#id").val() + ":" + opinionIndex;
    layer.open({
        type: 2,
        title: "选择模板",
        area: ['500px', '320px'],
        content: "/ss/pretrialopinion/view/" + id
    })
}

function reLoad() {
    window.location.reload();
}

laydate({elem: "#limitdate", event: "focus"});

/* --pretrialopinionId-- 模板id*/
function openMaterials(pretrialopinionId){
    /*提示上传资料*/
    if(pretrialopinionId == "6"){
        $("#showMarerials").show();
    }else{
        $("#showMarerials").hide();
    }
}

$("#showMarerials").click(function () {
     layer.open({
           type: 2,
           title: "需提供材料",
           area: ['600px', '320px'],
           content: "/ss/affairmaters/viewMaterials/" + $("#id").val()
       })
})

function setOpinion(text){
    var init = $("#opinionInit").val();
    $("#opinion").val(init+text);
}

