package com.cloud.core.behavior;

/**
 * @Author lijinghuan
 * @Email:ljh0576123@163.com
 * @CreateTime:2018/1/15
 * @Description:事件统计数据项
 * @Modifier:
 * @ModifyContent:
 */
public class EventItem {
    /**
     * 事件id(R文件中的hashcode值或xml中定义的id名称)
     */
    private String eventId = "";
    /**
     * 第三方平台统计的事件id
     */
    private String behaviorEventId = "";
    /**
     * 描述
     */
    private String describe = "";
    /**
     * 行为类型
     * 0:事件计数统计,
     * 1:统计点击行为各属性被触发的次数,
     * 2:统计数值型变量值的分布
     */
    private int behaviorType = BehaviorType.Count.ordinal();

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public String getBehaviorEventId() {
        return behaviorEventId;
    }

    public void setBehaviorEventId(String behaviorEventId) {
        this.behaviorEventId = behaviorEventId;
    }

    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }

    public int getBehaviorType() {
        return behaviorType;
    }

    public void setBehaviorType(int behaviorType) {
        this.behaviorType = behaviorType;
    }
}
