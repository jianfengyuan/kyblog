/**
 * 更新基本信息
 */
function updateBasic() {
    let name = $('#name').val().trim();
    let uid = $('#uid').val().trim();
    let header = $('#img_head')[0].currentSrc;
    console.log(header)
    if (name == ''){
        swal("请输入昵称", "昵称为必填项", "warning");
    }else {
        $.ajax({
            type: "POST",
            url: "/admin/profile",
            dataType: "json",
            data: {name:name,uid:uid,header:header,flag:'basic'},
            // contentType: "application/json",
            success: function(data){
                if (data['code']== 200){
                    swal("成功", "你已成功修改了基本信息", "success");
                }else if (data['code']== 501){
                    swal("提示", "你没有修改任何信息", "info");
                }else if (data['code']== 500){
                    swal("错误", "服务器发生了一个错误", "error");
                }
            }
        });
    }
}

/**
 * 更新其他信息
 */
function updateOther() {
    let info = $('#info').val().trim();
    let background = $('#img_background')[0].currentSrc;
    // 处理选填字段
    info = info == ''?' ':info;
    let uid = $('#uid').val().trim();
    $.ajax({
        type: "POST",
        url: "/admin/profile",
        data: {info:info,flag:'other',uid:uid, background:background},
        dataType: "json",
        success: function(data){
            if (data['code']== 200){
                swal("成功", "你已成功修改了其他信息", "success");
            }else if (data['code']== 501){
                swal("提示", "你没有修改任何信息", "info");
            }else if (data['code']== 500){
                swal("错误", "服务器发生了一个错误", "error");
            }
        }
    });
}

/**
 * 更新联系方式
 */
function updateContact() {
    let email = $('#email').val().trim();
    let uid = $('#uid').val().trim();
    // 处理选填字段
    email = email == ''?' ':email;
    // 正则验证格式
    var emailReg = /^([\.a-zA-Z0-9_-])+@([a-zA-Z0-9_-])+(\.[a-zA-Z0-9_-])+/;
    if (email != ' '){
        if(!emailReg.test(email)){
            swal("警告", "请输入正确的邮箱格式", "warning");
            return;
        }
    }

    $.ajax({
        type: "POST",
        url: "/admin/profile",
        data: {email:email,flag:'contact',uid:uid},
        dataType: "json",
        success: function(data){
            if (data['code']== 200){
                swal("成功", "你已成功修改了联系方式", "success");
            }else if (data['code']== 501){
                swal("提示", "你没有修改任何信息", "info");
            }else if (data['code']== 500){
                swal("错误", "服务器发生了一个错误", "error");
            }
        }
    });
}

/**
 * 上传头像
 */
function uploadImg() {
    var formdata=new FormData();
    formdata.append("image",$("#img").get(0).files[0]);
    formdata.append("flag",1);
    $.ajax({
        async: false,
        type: "POST",
        url: "http://127.0.0.1:7500/image/upload",
        dataType: "json",
        data: formdata,
        contentType:false,//ajax上传图片需要添加
        processData:false,//ajax上传图片需要添加
        success: function (data) {
            if (data['code']== 200){
                $('#img_head').attr("src",data.img);
                // $('#img_head1').attr("src",data['data']);
                swal("成功", "上传成功", "success");
            }else {
                swal("错误", "文件过大，请上传小于1M的图片", "error");
            }
        },
        error: function (e) {
        }
    });
}
/**
 * 上传背景
 */
function uploadBackground() {
    var formdata=new FormData();
    formdata.append("image",$("#backgroundInput").get(0).files[0]);
    formdata.append("flag",1);
    $.ajax({
        async: false,
        type: "POST",
        url: "http://127.0.0.1:7500/image/upload",
        dataType: "json",
        data: formdata,
        contentType:false,//ajax上传图片需要添加
        processData:false,//ajax上传图片需要添加
        success: function (data) {
            if (data['code']== 200){
                $('#img_background').attr("src",data.img);
                swal("成功", "上传成功", "success");
            }else {
                swal("错误", "文件过大，请上传小于1M的图片", "error");
            }
        },
        error: function (e) {
        }
    });
}

