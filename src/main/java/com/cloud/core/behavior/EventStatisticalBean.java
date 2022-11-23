package com.cloud.core.behavior;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author lijinghuan
 * @Email:ljh0576123@163.com
 * @CreateTime:2017/1/12
 * @Description:事件统计信息
 * @Modifier:
 * @ModifyContent:
 */
public class EventStatisticalBean {

    /**
     * 友盟自定义事件id
     */
    private String eventId = "";
    /**
     * 统计参数
     */
    private List<UMStatisticalParameItem> statisticalParames = null;

    /**
     * 获取友盟自定义事件id
     */
    public String getEventId() {
        if (eventId == null) {
            eventId = "";
        }
        return eventId;
    }

    /**
     * 设置友盟自定义事件id
     *
     * @param eventId
     */
    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    /**
     * 获取统计参数
     */
    public List<UMStatisticalParameItem> getStatisticalParames() {
        if (statisticalParames == null) {
            statisticalParames = new ArrayList<UMStatisticalParameItem>();
        }
        return statisticalParames;
    }

    /**
     * 设置统计参数
     *
     * @param statisticalParames
     */
    public void setStatisticalParames(List<UMStatisticalParameItem> statisticalParames) {
        this.statisticalParames = statisticalParames;
    }
}
