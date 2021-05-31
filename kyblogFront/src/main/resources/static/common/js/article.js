function publish() {
    let title = $("#title").val().trim();
    let content = $("#content").val().trim();
    let tags = $('#tags').val().trim();
    let introduce = $('#info').val().trim();
    let kind = $('#kind').find('option:selected').text();
    // let status = 1
    console.log(tags)
    console.log(title)
    if (title == ''){
        swal("警告","标题不能为空！", "warning");
        return;
    }
    $.ajax({
        type: "POST",
        url: "/admin/articles/publish",
        data: {title:title,content:content,tags:tags, introduce:introduce, kind:kind, status:1},
        dataType: "json",
        success: function(data){
            if (data['code']== 200){
                swal("发表成功", "你现在可以给文章添加一个封面啦！", "success")
                    .then(() => {
                        var location = window.location.href;
                        window.location.href ="/admin/articles";
                    });
            }else{
                swal("出错啦", "服务器发生了一个错误", "error");
            }
        }
    });
}

function updatePublish() {
    let id = $('#id').val().trim();
    let title = $('#title').val().trim();
    let content = $('#content').val().trim();
    let tags = $('#tags').val().trim();
    let introduce = $('#info').val().trim();
    let kind = $('#kind').find('option:selected').text();
    let img_head = $('#img_head')[0].currentSrc;
    console.log(title)
    console.log(id)
    console.log(tags)
    console.log(img_head)
    if (title == ''){
        swal("警告","标题不能为空！", "warning");
        return;
    }
    $.ajax({
        type: "POST",
        url: "/admin/articles/update",
        data: {id:id,title:title,content:content,tags:tags,kind:kind,
            introduce:introduce,status:1, background:img_head},
        dataType: "json",
        success: function(data){
            if (data['code']== 200){
                swal("更新成功", "", "success")
                    .then(() => {
                        var location = window.location.href;
                        window.location.href = "/admin/articles/edit?articleId="+id;
                    });
            }else{
            }
        }
    });
}

/**
 * 草稿发布
 */
function draftPublish() {
    let title = $('#title').val().trim();
    let content = $('#content').val().trim();
    let tags = $('#tags').val().trim();
    let introduce = $('#info').val().trim();
    let kind = $('#kind').find('option:selected').text();
    if (title == ''){
        swal("警告","标题不能为空！", "warning");
        return;
    }
    $.ajax({
        type: "POST",
        url: "/admin/articles/publish",
        data: {title:title,content:content,tags:tags,kind:kind,introduce:introduce,status:2},
        dataType: "json",
        success: function(data){
            if (data['code']== 200){
                swal("保存成功", "你可以在草稿箱找到它！", "success")
                    .then(() => {
                        var location = window.location.href;
                        let strings = location.split("kyblogArticle");
                        window.location.href = "/admin/articles";
                    });
            }else{
                swal("出错啦", "服务器发生了一个错误", "error");
            }
        }
    });
}

/**
 * 更新草稿
 */
function updateDraft() {
    let id = $('#id').val().trim();
    let title = $('#title').val().trim();
    let content = $('#content').val().trim();
    let tags = $('#tags').val().trim();
    let introduce = $('#info').val().trim();
    let kind = $('#kind').find('option:selected').text();
    if (title == ''){
        swal("警告","标题不能为空！", "warning");
        return;
    }
    $.ajax({
        type: "POST",
        url: "update",
        data: {id:id,title:title,content:content,tags:tags,kind:kind,introduce:introduce,status:2},
        dataType: "json",
        success: function(data){
            if (data['code']== 200){
                swal("保存草稿成功！", "", "success")
                    .then(() => {
                        var location = window.location.href;
                        window.location.href = "/admin/articles/edit?articleId="+id;
                    });
            }else{
                swal("出错啦", "服务器发生了一个错误", "error");
            }
        }
    });
}