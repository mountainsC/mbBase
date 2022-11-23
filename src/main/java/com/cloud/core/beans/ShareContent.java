package com.cloud.core.beans;

import android.graphics.Bitmap;


import com.cloud.core.enums.ShareType;

import java.io.File;

/**
 * @Author lijinghuan
 * @Email:ljh0576123@163.com
 * @CreateTime:2017/1/21
 * @Description:分享内容
 * @Modifier:
 * @ModifyContent:
 */
public class ShareContent {
    /**
     * 分享标题
     */
    private String title = "";
    /**
     * 分享内容
     */
    private String content = "";
    /**
     * 分享logo
     * (若本地资源可通过ResUtils.getResourcesUri()获取)
     */
    private String logo = "";
    /**
     * url
     */
    private String url = "";
    /**
     * 文件(图片、其它文件)
     */
    private File file = null;
    /**
     * 文件分享时显示缩略图
     */
    private Bitmap fileThumImage = null;
    /**
     * 微信appid(文件分享时有用)
     */
    private String wxAppId = "";
    /**
     * 分享链接
     */
    private ShareType shareType = ShareType.link;
    String wxmpPath;
    String originalId;

    public String getWxmpPath() {
        return wxmpPath;
    }

    public void setWxmpPath(String wxmpPath) {
        this.wxmpPath = wxmpPath;
    }

    public String getOriginalId() {
        return originalId;
    }

    public void setOriginalId(String originalId) {
        this.originalId = originalId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public Bitmap getFileThumImage() {
        return fileThumImage;
    }

    public void setFileThumImage(Bitmap fileThumImage) {
        this.fileThumImage = fileThumImage;
    }

    public String getWxAppId() {
        return wxAppId;
    }

    public void setWxAppId(String wxAppId) {
        this.wxAppId = wxAppId;
    }

    public ShareType getShareType() {
        return shareType;
    }

    public void setShareType(ShareType shareType) {
        this.shareType = shareType;
    }
}
