package com.cloud.core.beans;

import android.graphics.Bitmap;

import com.cloud.core.enums.NotifyType;

/**
 * @Author lijinghuan
 * @Email:ljh0576123@163.com
 * @CreateTime:2016/9/26
 * @Description:
 * @Modifier:
 * @ModifyContent:
 */

public class NotifyProperties {
    /**
     * 通知id
     */
    private int notificationId = 0;
    /**
     * 通知栏显示的图标
     */
    private int icon = 0;
    /**
     * 通知时在状态栏显示的内容
     */
    private String tickerText = "";
    /**
     * 通知标题
     */
    private String title = "";
    /**
     * 通知内容
     */
    private String text = "";
    /**
     * 图片
     */
    private Bitmap image = null;
    /**
     * largeIcon
     */
    private Bitmap largeIcon = null;
    /**
     * 通知类型{@link }
     */
    private NotifyType notifyType = NotifyType.normal;
    /**
     * requestCode
     */
    private int requestCode = 0;

    /**
     * 通知栏显示的图标
     */
    public int getIcon() {
        return icon;
    }

    /**
     * 通知栏显示的图标
     *
     * @param icon
     */
    public void setIcon(int icon) {
        this.icon = icon;
    }

    /**
     * 获取通知时在状态栏显示的内容
     */
    public String getTickerText() {
        if (tickerText == null) {
            tickerText = "";
        }
        return tickerText;
    }

    /**
     * 设置通知时在状态栏显示的内容
     *
     * @param tickerText
     */
    public void setTickerText(String tickerText) {
        this.tickerText = tickerText;
    }

    /**
     * 获取通知标题
     */
    public String getTitle() {
        if (title == null) {
            title = "";
        }
        return title;
    }

    /**
     * 设置通知标题
     *
     * @param title
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * 获取通知内容
     */
    public String getText() {
        if (text == null) {
            text = "";
        }
        return text;
    }

    /**
     * 设置通知内容
     *
     * @param text
     */
    public void setText(String text) {
        this.text = text;
    }

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }

    /**
     * 获取largeIcon
     */
    public Bitmap getLargeIcon() {
        return largeIcon;
    }

    /**
     * 设置largeIcon
     *
     * @param largeIcon
     */
    public void setLargeIcon(Bitmap largeIcon) {
        this.largeIcon = largeIcon;
    }

    /**
     * 获取通知id
     */
    public int getNotificationId() {
        return notificationId;
    }

    /**
     * 设置通知id
     *
     * @param notificationId
     */
    public void setNotificationId(int notificationId) {
        this.notificationId = notificationId;
    }

    public NotifyType getNotifyType() {
        return notifyType;
    }

    public void setNotifyType(NotifyType notifyType) {
        this.notifyType = notifyType;
    }

    public int getRequestCode() {
        return requestCode;
    }

    public void setRequestCode(int requestCode) {
        this.requestCode = requestCode;
    }
}
