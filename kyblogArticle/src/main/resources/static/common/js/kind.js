var CONTEXT_PATH = "/kyblogArticle"
function addKind() {
    let name = $('#name').val().trim();
    let introduce = $('#info').val().trim();
    if (name == ''){
        swal("警告","名称不能为空！", "warning");
        return;
    }
    $.ajax({
        type: "POST",
        url: CONTEXT_PATH + "/kind/add",
        data: {name:name,introduce:introduce},
        dataType: "json",
        success: function(data){
            // data = $.parseJSON(data);
            console.log(data)
            if (data["code"] == 200){
                swal("添加成功", "你已经成功创建了名称为《"+name+"》的文集了", "success");
                setInterval(reload, 2000);
            }else if (data["code"] == 501){
                swal("添加失败", "你已经创建过名称为《"+name+"》的文集了，请换一个新名称吧~", "error");
            }else{
                swal("出错啦", "服务器发生了一个错误", "error");
            }
        }
    });
}

/**
 * 刷新页面
 */
function reload() {
    window.location.reload();
}