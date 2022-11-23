package com.cloud.core.ebus;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Index;
import org.greenrobot.greendao.annotation.Property;

/**
 * Author lijinghuan
 * Email:ljh0576123@163.com
 * CreateTime:2018/10/23
 * Description:
 * Modifier:
 * ModifyContent:
 */
@Entity(nameInDb = "rx_ebus_data_list")
public class EBusDataItem {
    /**
     * 主键(className-receiveKey)
     */
    @Id()
    @Property(nameInDb = "key")
    @Index(unique = true)
    private String key = "";
    /**
     * 类型名
     */
    @Property(nameInDb = "className")
    private String className = "";
    /**
     * 接收key
     */
    @Property(nameInDb = "receiveKey")
    private String receiveKey = "";
    /**
     * 线程模式(默认主线程)
     */
    private String threadMode = "MAIN";
    /**
     * 类类型(Activity、Fragment、其它类)
     */
    private String classType = "";

    @Generated(hash = 468134674)
    public EBusDataItem(String key, String className, String receiveKey,
                        String threadMode, String classType) {
        this.key = key;
        this.className = className;
        this.receiveKey = receiveKey;
        this.threadMode = threadMode;
        this.classType = classType;
    }

    @Generated(hash = 1270447682)
    public EBusDataItem() {
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getReceiveKey() {
        return receiveKey;
    }

    public void setReceiveKey(String receiveKey) {
        this.receiveKey = receiveKey;
    }

    public String getThreadMode() {
        return threadMode;
    }

    public void setThreadMode(String threadMode) {
        this.threadMode = threadMode;
    }

    public String getClassType() {
        return classType;
    }

    public void setClassType(String classType) {
        this.classType = classType;
    }
}
