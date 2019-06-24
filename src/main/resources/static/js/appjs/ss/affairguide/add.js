$().ready(function() {
	validateRule();
});

$.validator.setDefaults({
	submitHandler : function() {
		save();
	}
});
function save() {
	$.ajax({
		cache : true,
		type : "POST",
		url : "/ss/affairguide/save",
		data : $('#signupForm').serialize(),// 你的formid
		async : false,
		error : function(request) {
			parent.layer.alert("Connection error");
		},
		success : function(data) {
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
		rules : {

            accessoryPath : {
                required : true
            },
            according : {
				required : true,
			},
            charge:{
                required : true
			},

            chargegist : {
                required : true,
            },
            condition : {
                required : true,
            },
            inquire : {
                required : true,
            },
            institution : {
                required : false,
            },
            material : {
                required : true,
            },
            procedures : {
                required : true,
            },

            sepcialversion : {
                required : true,
            },
            site : {
                required : true,
            },
            time : {
                required : true,
            },
            xrndomu : {
                required : true,
            },
		},
		messages : {



            accessoryPath : {
                required : icon +"不能为空"
            },
            according : {
                required : icon +"不能为空"
            },
            charge:{
                required : icon +"不能为空"
            },

            chargegist : {
                required : icon +"不能为空"

            },
            condition : {
                required : icon +"不能为空"
            },
            inquire : {
                required : icon +"不能为空"
            },
            material : {
                required : icon +"不能为空"
            },
            procedures : {
                required : icon +"不能为空"
            },
            sepcialversion : {
                required : icon +"不能为空"
            },
            site : {
                required : icon +"不能为空"
            },
            time : {
                required : icon +"不能为空"
            },
            xrndomu : {
                required : icon +"不能为空"
            }
		}
	})
}

