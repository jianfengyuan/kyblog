function replyBtnEvent(commentId, articleId, name) {
    $('#reply-id').val(commentId)
    $('#article-id').val(articleId)
    $('#reply-to').html(name)
}

function addComment(admin) {
    let readStatus = COMMENT_UNREAD;
    let context = "";
    if (admin) {
        readStatus = COMMENT_READ;
        context = "/admin";
    }
    let name = $('#name').val().trim();
    let type = $('#type').val().trim();
    let replyId = $('#reply-id').val();
    let email = $('#email').val().trim();
    let articleId = $('#article-id').val().trim();
    let content = $('#content').val().trim();
    if (content == ''){
        swal("警告","内容不能为空！", "warning");
        return;
    }
    if (name == ''){
        swal("警告","名称不能为空！", "warning");
        return;
    }
    $.ajax({
        type: "POST",
        url: context+"/comments",
        data: {name:name,replyId:replyId,type:type,
            replyId:replyId, email: email, articleId:articleId,
            content:content, readStatus: readStatus},
        dataType: "json",
        success: function(data){
            // data = $.parseJSON(data);
            console.log(data)
            if (data["code"] == 200){
                if (admin) {
                    swal("回复成功", "已回复: " + name, "success");
                } else {
                    swal("评论成功");
                }
                setInterval(reload, 2000);
            }else{
                swal("出错啦", "服务器发生了一个错误", "error");
            }
        }
    });
}

function deleteComment(id,type, content) {
    $.ajax({
        type: "POST",
        url: CONTEXT_PATH + "/comments/deleteComment",
        data: {id:id, type:type},
        dataType: "json",
        success: function(data){
            // data = $.parseJSON(data);
            console.log(data)
            if (data["code"] == 200){
                swal("删除成功","已删除回复: "+content,"success");
                setInterval(reload, 2000);
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
