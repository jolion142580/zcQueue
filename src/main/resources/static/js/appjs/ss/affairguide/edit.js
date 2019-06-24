// 以下为官方示例
$().ready(function() {
	validateRule();
});

$.validator.setDefaults({
	submitHandler : function() {
		update();
	}
});
function update() {
	$.ajax({
		cache : true,
		type : "POST",
		url : "/ss/affairguide/update",
		data : $('#signupForm').serialize(),// 你的formid
		async : false,
		error : function(request) {
			alert("Connection error");
		},
		success : function(data) {
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



function validateRule() {var icon = "<i class='fa fa-times-circle'></i> ";
    $("#signupForm").validate({
        rules : {
           /* affairName : {
                required : true
            },
            departId : {
                required : true
            },
            level : {
                required : true,
            },
            timeLimitType:{
                required : true
            },

            timeLimit : {
                required : true,
            },
            sortId : {
                required : true,
            },
            resultForm : {
                required : true,
            },
            isNet : {
                required : true,
            },
            code : {
                required : true,
            },
            isTodo : {
                required : true,
            }*/
        },
        messages : {
            /*departId:{
                required : icon + "不能为空"
            },


            affairName : {
                required : icon +"不能为空"
            },
            level : {
                required : icon +"不能为空"
            },
            timeLimitType:{
                required : icon +"不能为空"
            },

            timeLimit : {
                required : icon +"不能为空"

            },
            sortId : {
                required : icon +"不能为空"
            },
            resultForm : {
                required : icon +"不能为空"
            },
            isNet : {
                required : icon +"不能为空"
            },
            code : {
                required : icon +"不能为空"
            },
            isTodo : {
                required : icon +"不能为空"
            }*/
        }
    })
}
