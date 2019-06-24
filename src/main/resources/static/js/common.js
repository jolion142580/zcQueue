/*
jQuery.validator.addMethod("checkEnter", function (value, element) {
    // var pattern = new RegExp("[`~!@#$^&*=|{}':;',\\[\\]<>《》/?~！@#￥……&*|{}【】‘；：”“'。，、？' ']");
    var pattern = new RegExp("[`~!@#$^&*=|{}''\\[\\]<>?~！@#￥……&*|{}【】‘：”“'？'']");
    var reg = /^([0-9]+)$/;//全部为数字

    if (pattern.test(value)) {
        return false;
    } else if (value.indexOf(" ") != -1) {
        return false;
    } else {
        return true;
    }

}, "不能输入特殊字符!");

jQuery.validator.addMethod("checkAffairMaters", function (value, element) {
    // var pattern = new RegExp("[`~!@#$^&*=|{}':;',\\[\\]<>《》/?~！@#￥……&*|{}【】‘；：”“'。，、？' ']");
    var pattern = new RegExp("`~!@#$^&*=|\\<>?~！@#￥……&*|【】‘；”“'？'");
    var reg = /^([0-9]+)$/;//全部为数字

    if (pattern.test(value)) {
        return false;
    } else if (value.indexOf(" ") != -1) {
        return false;
    } else {
        return true;
    }

}, "不能输入特殊字符!");
*/

//验证密码没有空格 密码是5-10位 至少包含数字和大小写字母中的两种字符
jQuery.validator.addMethod("userrule", function (value, element) {
    var userblank = /^(?=.*[a-zA-Z])(?=.*\d)(?=.*[~!@#$%^&*()_+`\-={}:";'<>?,.\/]).{8,16}$/;
    return this.optional(element) || (userblank.test(value));
}, "需包含数字和大小写字母中至少两种字符的8-10位字符");



function fileChange(data) {
    var id = data.id;
    var fileName = $("#" + id).val();

    if (fileName != '') {
        var idx = fileName.lastIndexOf(".");
        if (idx != -1) {
            ext = fileName.substr(idx + 1);
            // ext = ext.toLowerCase( );
            // alert("ext="+ext);
            if (ext != 'doc' && ext != 'docx' && ext != 'xls') {
                parent.layer.alert("只能上传.doc  .docx  .xls  类型的文件!");
                return;
            }
        }
    }
}



