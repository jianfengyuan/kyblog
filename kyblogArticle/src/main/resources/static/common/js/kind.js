function addKind() {
    let name = $('#name').val().trim();
    let introduce = $('#info').val().trim();
    if (name == ''){
        swal("警告","名称不能为空！", "warning");
        return;
    }
    $.ajax({
        type: "POST",
        url: CONTEXT_PATH + "/kind/addKind",
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

/**
 * 删除文集
 */
function deleteKind(id, name) {
    swal({
        title: "确定删除此文集?",
        text: "删除此文集之后，关联的文章不会一起删除！",
        icon: "warning",
        buttons: true,
        dangerMode: true,
    })
        .then((willDelete) => {
            if (willDelete) {
                $.ajax({
                    type: "POST",
                    url: CONTEXT_PATH +"/kind/deleteKind",
                    data: {id:id,name:name},
                    dataType: "json",
                    success: function(data){
                        if (data['code']== 200){
                            swal("删除成功", "你已经成功删除了这个标签", "success");
                            setInterval(reload, 2000);
                        }else if(data['code']==501){
                            swal("删除失败", "默认类别不能删除", "error");
                        }
                    }
                });
            }
        });
}

/**
 * 更新文集
 */
function updateKind() {
    let name = $('#kind-name').val().trim();
    let id = $('#kind-id').val().trim();
    let introduce = $('#kind-introduction').val().trim();
    if (name == ''){
        swal("警告","名称不能为空！", "warning");
        return;
    }
    $.ajax({
        type: "POST",
        url: CONTEXT_PATH + "/kind/updateKind",
        data: {name:name,introduce:introduce,id:id},
        dataType: "json",
        success: function(data){
            if (data['code']== 200){
                swal("更新成功", "你已经成功更新了这个文集", "success");
                setInterval(reload, 2000);
            }else if (data['code']== 501){
                swal("修改失败", "不能修改默认类别", "info");
            }else if (data['code']== 502){
                swal("添加失败", "你已经创建过名称为《"+name+"》的文集了，请换一个新名称吧~", "error");
            }else{
                swal("出错啦", "服务器发生了一个错误", "error");
            }
        }
    });
}

function updateBtn(id, name, introduce) {
    $('#kind-id').val(id)
    $('#kind-name').val(name)
    $('#kind-introduction').val(introduce)
}
$(function(){
    // $("#publishBtn").click(addTag);
    $("#updateBtn").click(updateKind);
});