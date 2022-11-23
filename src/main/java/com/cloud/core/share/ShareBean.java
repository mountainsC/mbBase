package com.cloud.core.share;

import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMVideo;
import com.umeng.socialize.media.UMusic;

/**
 * @Author lijinghuan
 * @Email:ljh0576123@163.com
 * @CreateTime:2016/6/27
 * @Description:
 * @Modifier:
 * @ModifyContent:
 */
public class ShareBean {

    /**
     * 来源标题
     */
    private String sourceTitle = "";
    /**
     * 分享图片
     */
    private UMImage image = null;
    /**
     * 分享音乐
     * 构造函数赋值音频文件url
     * setTitle-标题
     * setThumb-显示缩略图
     * setDescription-描述
     */
    private UMusic music = null;
    /**
     * 分享视频
     * 构造函数赋值视频html地址
     * setThumb-显示缩略图
     */
    private UMVideo video = null;

    /**
     * @return 获取来源标题
     */
    public String getSourceTitle() {
        return sourceTitle;
    }

    /**
     * 设置来源标题
     *
     * @param sourceTitle
     */
    public void setSourceTitle(String sourceTitle) {
        this.sourceTitle = sourceTitle;
    }

    /**
     * @return 获取分享图片
     */
    public UMImage getImage() {
        return image;
    }

    /**
     * 设置分享图片
     *
     * @param image
     */
    public void setImage(UMImage image) {
        this.image = image;
    }

    /**
     * @return 获取分享音乐
     * 构造函数赋值音频文件url
     * setTitle-标题
     * setThumb-显示缩略图
     * setDescription-描述
     */
    public UMusic getMusic() {
        return music;
    }

    /**
     * 设置分享音乐
     * 构造函数赋值音频文件url
     * setTitle-标题
     * setThumb-显示缩略图
     * setDescription-描述
     *
     * @param music
     */
    public void setMusic(UMusic music) {
        this.music = music;
    }

    /**
     * @return 获取分享视频
     * 构造函数赋值视频html地址
     * setThumb-显示缩略图
     */
    public UMVideo getVideo() {
        return video;
    }

    /**
     * 设置分享视频
     * 构造函数赋值视频html地址
     * setThumb-显示缩略图
     *
     * @param video
     */
    public void setVideo(UMVideo video) {
        this.video = video;
    }
}
