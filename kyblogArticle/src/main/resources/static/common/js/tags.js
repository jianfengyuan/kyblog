var CONTEXT_PATH = "/kyblogArticle"
$(function(){
    $("#publishBtn").click(addTag);
    $("#updateBtn").click(updateTag);
});

// function publish() {
//     $("#publishModal").modal("hide");
//     $("#hintModal").modal("show");
//     // 獲取title 和 content
//     var title = $("#recipient-name").val();
//     var content = $("#message-text").val();
//     $.post(CONTEXT_PATH + "/discuss/add",
//         {"title": title, "content": content},
//         function (data) {
//             data = $.parseJSON(data);
//             // 在提示框中顯示返回信息
//             $("#hintBody").text(data.msg);
//             // 顯示提示框
//             $("#hintBody").modal("show");
//             // 2秒後, 自動隱藏提示框
//             setTimeout(function(){
//                 $("#hintModal").modal("hide");
//                 // 刷新頁面
//                 if (data.code === 200) {
//                     window.location.reload();
//                 }
//             }, 2000);
//         }
//
//     );
// }

/**
 * 添加新标签
 */
function addTag() {
    $("#publishModal").modal("hide");
    $("#hintModal").modal("show");
    let name = $("#recipient-name").val().trim();
    // let introduce = $('#info').val().trim();
    // if (name == ''){
    //     swal("警告","名称不能为空！", "warning");
    //     return;
    // }
    $.ajax({
        type: "POST",
        url: CONTEXT_PATH + "/tags/addTag",
        data: {name:name},
        dataType: "json",
        success: function(data){
            if (data['code']== 200){
                swal("添加成功", "你已经成功创建了名称为《"+name+"》的标签了", "success");
                setInterval(reload, 2000);
            }else if (data['code']== 501){
                swal("添加失败", "你已经创建过名称为《"+name+"》的标签了，请换一个新名称吧~", "error");
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

function updateBtn(id, name) {
    $('#tag-id').val(id)
    $('#tag-name').val(name)
}
/**
 * 更新标签
 */
function updateTag() {
    let name = $('#tag-name').val().trim();
    let id = $('#tag-id').val().trim();
    if (name == ''){
        swal("警告","名称不能为空！", "warning");
        return;
    }
    $.ajax({
        type: "POST",
        url: CONTEXT_PATH +"/tags/updateTag",
        data: {name:name,id:id},
        dataType: "json",
        success: function(data){
            if (data['code']== 200){
                swal("更新成功", "你已经成功更新了这个标签", "success");
                setInterval(reload, 2000);
            }else if (data['code']== 501){
                swal("提示", "你没有修改任何信息", "info");
            }else if (data['code']== 502){
                swal("添加失败", "你已经创建过名称为《"+name+"》的标签了，请换一个新名称吧~", "error");
            }else{
                swal("出错啦", "服务器发生了一个错误", "error");
            }
        }
    });
}

function deleteTag(id) {
    swal({
        title: "确定删除此标签?",
        text: "删除此标签之后，关联的文章不会一起删除！",
        icon: "warning",
        buttons: true,
        dangerMode: true,
    })
        .then((willDelete) => {
            if (willDelete) {
                var location = window.location.href;
                let strings = location.split("moti-blog");
                window.location.href = strings[0]+"moti-blog/deleteTag?id="+id;
            }
        });
}