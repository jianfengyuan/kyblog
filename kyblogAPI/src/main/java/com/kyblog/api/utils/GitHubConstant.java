package com.kyblog.api.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public interface GitHubConstant {
    String OWNER = "jianfengyuan";
    String REPO_NAME = "figurebed";
//    String ACCESS_TOKEN = "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";
    String ACCESS_TOKEN = "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";
    String CREATE_REPOS_MESSAGE = "(ง ˙o˙)ว";

    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    String IMG_FILE_DEST_PATH = sdf.format(new Date()) + "/";
    String CREATE_REPOS_URL = "https://api.github.com/repos/%s/%s/contents/img/%s";
    String GITPAGE_REQUEST_URL = "https://" + OWNER + ".github.io/"+ REPO_NAME +"/img/";
}
