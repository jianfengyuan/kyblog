var CONTEXT_PATH = "/kyblogArticle"
function publish() {
    let title = $("#title").val().trim();
    let content = $("#content").val().trim();
    let tags = $('#tags').val().trim();
    let introduce = $('#info').val().trim();
    // let kind = $('#kind').find('option:selected').text();
    console.log(tags)
    console.log(title)
    if (title == ''){
        swal("警告","标题不能为空！", "warning");
        return;
    }
    $.ajax({
        type: "POST",
        url: CONTEXT_PATH+"/article/publish",
        data: {title:title,content:content,tags:tags},
        dataType: "json",
        success: function(data){
            if (data['code']== 200){
                swal("发表成功", "你现在可以给文章添加一个封面啦！", "success")
                    .then(() => {
                        var location = window.location.href;
                        let strings = location.split("moti-blog");
                        // window.location.href = strings[0]+"moti-blog/edit?id="+data['data'];
                    });
            }else{
                swal("出错啦", "服务器发生了一个错误", "error");
            }
        }
    });
}