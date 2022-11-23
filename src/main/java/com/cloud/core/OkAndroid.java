package com.cloud.core;

import android.content.Context;
import android.text.TextUtils;

import com.cloud.core.cache.RxCache;
import com.cloud.core.utils.JsonUtils;
import com.cloud.core.utils.ThreadPoolUtils;

import java.util.HashMap;

/**
 * Author lijinghuan
 * Email:ljh0576123@163.com
 * CreateTime:2018/10/12
 * Description:框架参数配置工具类
 * Modifier:
 * ModifyContent:
 */
public class OkAndroid {

    private static OkAndroid okAndroid = null;
    private OkAndroidBuilder builder = null;

    private OkAndroid() {
        //外部不能直接实例
    }

    public class OkAndroidBuilder {
        private HashMap<String, Object> cacheMap = new HashMap<String, Object>();
        /**
         * 项目构建配置类包名(用于获取基本配置信息)
         */
        private String projectBuildConfigPackgeName = null;
        /**
         * http header参数名
         */
        private String[] httpHeaderParamNames = null;

        private OkAndroidBuilder() {
            //不允许外部实例
        }

        public HashMap<String, Object> getCacheMap() {
            return cacheMap;
        }

        public String getProjectBuildConfigPackgeName() {
            return projectBuildConfigPackgeName;
        }

        public OkAndroidBuilder setProjectBuildConfigPackgeName(String projectBuildConfigPackgeName) {
            this.projectBuildConfigPackgeName = projectBuildConfigPackgeName;
            return this;
        }

        public String[] getHttpHeaderParamNames() {
            return httpHeaderParamNames;
        }

        public OkAndroidBuilder setHttpHeaderParamNames(String... httpHeaderParamNames) {
            this.httpHeaderParamNames = httpHeaderParamNames;
            return this;
        }

        /**
         * 对部分信息持久化
         */
        public void build(Context context) {
            if (builder == null) {
                return;
            }
            ThreadPoolUtils.fixThread().execute(new PersistenceRunnable(context));
        }

        private class PersistenceRunnable implements Runnable {

            private Context context = null;

            public PersistenceRunnable(Context context) {
                this.context = context;
            }

            @Override
            public void run() {
                HashMap<String, Object> cacheMap = builder.getCacheMap();
                //缓存配置类信息
                String buildConfigPackgeName = builder.getProjectBuildConfigPackgeName();
                cacheMap.put("BuildConfigPackgeName", buildConfigPackgeName);
                //http header参数
                cacheMap.put("HttpHeaderParamName", builder.getHttpHeaderParamNames());

                String content = JsonUtils.toStr(cacheMap);
                RxCache.setCacheData(context, "CONFIG_INFO_CACHE_KEY", content);
            }
        }
    }

    /**
     * 获取实例
     *
     * @return
     */
    public static OkAndroid getInstance() {
        return okAndroid == null ? okAndroid = new OkAndroid() : okAndroid;
    }

    /**
     * 初始化参数配置信息
     *
     * @return
     */
    public OkAndroidBuilder initialize() {
        if (builder == null) {
            builder = new OkAndroidBuilder();
        }
        return builder;
    }

    /**
     * 获取信息构建对象
     *
     * @return
     */
    public OkAndroidBuilder getBuilder(Context context) {
        if (builder == null) {
            builder = new OkAndroidBuilder();
            initBuild(context, builder);
        }
        return builder;
    }

    private void initBuild(Context context, OkAndroidBuilder builder) {
        String cacheData = RxCache.getCacheData(context, "CONFIG_INFO_CACHE_KEY");
        if (TextUtils.isEmpty(cacheData)) {
            return;
        }
        HashMap<String, Object> map = JsonUtils.parseT(cacheData, HashMap.class);
        if (ObjectJudge.isNullOrEmpty(map)) {
            return;
        }
        //设置BuildConfig包名
        builder.setProjectBuildConfigPackgeName(getBuildConfigClass(map));
        //http header参数名
        builder.setHttpHeaderParamNames(getHttpHeaderParamNames(map));
    }

    private String[] getHttpHeaderParamNames(HashMap<String, Object> map) {
        if (!map.containsKey("HttpHeaderParamName")) {
            return null;
        }
        String[] headerParamNames = (String[]) map.get("HttpHeaderParamName");
        return headerParamNames;
    }

    private String getBuildConfigClass(HashMap<String, Object> map) {
        if (!map.containsKey("BuildConfigPackgeName")) {
            return null;
        }
        String buildConfigClassPackgeName = String.valueOf(map.get("BuildConfigPackgeName"));
        return buildConfigClassPackgeName;
    }
}
