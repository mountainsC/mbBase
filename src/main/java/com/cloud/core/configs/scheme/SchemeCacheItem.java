package com.cloud.core.configs.scheme;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Index;
import org.greenrobot.greendao.annotation.Property;

/**
 * @Author lijinghuan
 * @Email:ljh0576123@163.com
 * @CreateTime:2018/7/8
 * @Description:scheme缓存
 * @Modifier:
 * @ModifyContent:
 */
@Entity(nameInDb = "scheme_cache_list")
public class SchemeCacheItem {
    /**
     * scheme路径
     */
    @Id
    @Index
    @Property(nameInDb = "schemePath")
    private String schemePath = "";
    /**
     * scheme url完整路径
     */
    @Property(nameInDb = "schemeUrl")
    private String schemeUrl = "";
    /**
     * scheme json {@link com.cloud.core.schemes.SchemeItem}
     */
    @Property(nameInDb = "schemeJson")
    private String schemeJson = "";
    /**
     * scheme version
     */
    @Property(nameInDb = "schemeVersion")
    private int schemeVersion = 0;
    /**
     * 是否需要检测更新
     */
    @Property(nameInDb = "isNeedCheckUpdate")
    private boolean isNeedCheckUpdate = false;
    /**
     * 缓存时间
     */
    @Property(nameInDb = "cacheTime")
    private long cacheTime = 0;

    @Generated(hash = 1634154854)
    public SchemeCacheItem(String schemePath, String schemeUrl, String schemeJson,
            int schemeVersion, boolean isNeedCheckUpdate, long cacheTime) {
        this.schemePath = schemePath;
        this.schemeUrl = schemeUrl;
        this.schemeJson = schemeJson;
        this.schemeVersion = schemeVersion;
        this.isNeedCheckUpdate = isNeedCheckUpdate;
        this.cacheTime = cacheTime;
    }

    @Generated(hash = 411256652)
    public SchemeCacheItem() {
    }

    public String getSchemePath() {
        return schemePath;
    }

    public void setSchemePath(String schemePath) {
        this.schemePath = schemePath;
    }

    public String getSchemeUrl() {
        return schemeUrl;
    }

    public void setSchemeUrl(String schemeUrl) {
        this.schemeUrl = schemeUrl;
    }

    public String getSchemeJson() {
        return schemeJson;
    }

    public void setSchemeJson(String schemeJson) {
        this.schemeJson = schemeJson;
    }

    public int getSchemeVersion() {
        return schemeVersion;
    }

    public void setSchemeVersion(int schemeVersion) {
        this.schemeVersion = schemeVersion;
    }

    public boolean isNeedCheckUpdate() {
        return isNeedCheckUpdate;
    }

    public void setNeedCheckUpdate(boolean needCheckUpdate) {
        isNeedCheckUpdate = needCheckUpdate;
    }

    public long getCacheTime() {
        return cacheTime;
    }

    public void setCacheTime(long cacheTime) {
        this.cacheTime = cacheTime;
    }

    public boolean getIsNeedCheckUpdate() {
        return this.isNeedCheckUpdate;
    }

    public void setIsNeedCheckUpdate(boolean isNeedCheckUpdate) {
        this.isNeedCheckUpdate = isNeedCheckUpdate;
    }
}
