package com.cloud.core.utils;

import android.text.TextUtils;

import com.cloud.core.cache.CacheDataItem;
import com.cloud.core.cache.DbCacheDao;
import com.cloud.core.daos.CacheDataItemDao;
import com.cloud.core.logger.Logger;

import org.greenrobot.greendao.query.QueryBuilder;

import java.io.Serializable;
import java.util.HashMap;

/**
 * @Author lijinghuan
 * @Email:ljh0576123@163.com
 * @CreateTime:2018/4/3
 * @Description:
 * @Modifier:
 * @ModifyContent:
 */
public class RxCachePool {

    private static RxCachePool rxCachePool = null;
    private static HashMap<String, Object> objList = new HashMap<String, Object>();
    //模块单独运行时，数据暂存在tempList列表
    private static HashMap<String, String> tempList = new HashMap<String, String>();

    public static RxCachePool getInstance() {
        return rxCachePool == null ? new RxCachePool() : rxCachePool;
    }

    public void putObject(String key, Object object) {
        if (object == null || TextUtils.isEmpty(key)) {
            return;
        }
        objList.put(key, object);
    }

    public Object getObject(String key, boolean isClear) {
        if (TextUtils.isEmpty(key) || !objList.containsKey(key)) {
            return null;
        }
        if (isClear) {
            return objList.remove(key);
        } else {
            return objList.get(key);
        }
    }

    public Object getObject(String key) {
        return getObject(key, false);
    }

    public void putString(String objKey, String value) {
        put(objKey, value);
    }

    public <T extends Serializable> void putEntity(String objKey, T entity) {
        String json = JsonUtils.toStr(entity);
        putString(objKey, json);
    }

    public void putBoolean(String key, boolean value) {
        put(key, value);
    }

    public void putLong(String key, long value) {
        put(key, value);
    }

    public void putInt(String key, int value) {
        put(key, value);
    }

    /**
     * 添加对象值
     *
     * @param objKey
     * @param value  可序列化的对象
     */
    private <T extends Serializable> void put(String objKey, T value) {
        if (value == null || TextUtils.isEmpty(objKey)) {
            return;
        }
        try {
            CacheDataItem dataItem = new CacheDataItem();
            cacheData(objKey, dataItem, value);
            DbCacheDao dbCacheDao = new DbCacheDao();
            CacheDataItemDao cacheDao = dbCacheDao.getCacheDao();
            if (cacheDao != null) {
                CacheDataItemDao.createTable(cacheDao.getDatabase(), true);
                cacheDao.insertOrReplace(dataItem);
            } else {
                //临时缓存数据
                tempList.put(objKey, JsonUtils.toStr(dataItem));
            }
        } catch (Exception e) {
            Logger.L.error(e);
        }
    }

    private CacheDataItem getCacheDataItem(String key) {
        CacheDataItem first = null;
        DbCacheDao dbCacheDao = new DbCacheDao();
        CacheDataItemDao cacheDao = dbCacheDao.getCacheDao();
        if (cacheDao != null) {
            CacheDataItemDao.createTable(cacheDao.getDatabase(), true);
            QueryBuilder<CacheDataItem> builder = cacheDao.queryBuilder();
            QueryBuilder<CacheDataItem> where = builder.where(CacheDataItemDao.Properties.Key.eq(key));
            QueryBuilder<CacheDataItem> limit = where.limit(1);
            if (limit != null) {
                first = limit.unique();
            }
        } else {
            if (tempList.containsKey(key)) {
                String content = tempList.get(key);
                first = JsonUtils.parseT(content, CacheDataItem.class);
            }
        }
        return first;
    }

    /**
     * 获取实体
     *
     * @param key       键
     * @param entityCls 实体类
     * @param isClear   获取后是否清除数据
     * @return
     */
    public <T> T getEntity(String key, Class<T> entityCls, boolean isClear) {
        String entityJson = getString(key, isClear);
        if (TextUtils.isEmpty(entityJson)) {
            return JsonUtils.newNull(entityCls);
        } else {
            return JsonUtils.parseT(entityJson, entityCls);
        }
    }

    /**
     * 获取实体
     *
     * @param key       键
     * @param entityCls 实体类
     * @return
     */
    public <T> T getEntity(String key, Class<T> entityCls) {
        return getEntity(key, entityCls, false);
    }

    /**
     * 获取缓存值
     *
     * @param key     键
     * @param isClear 获取后是否清除数据
     * @return
     */
    public String getString(String key, boolean isClear) {
        CacheDataItem dataItem = getCacheDataItem(key);
        if (dataItem == null) {
            return "";
        }
        String value = dataItem.getValue();
        if (isClear) {
            clear(key);
        }
        return value;
    }

    /**
     * 获取缓存值
     *
     * @param key 键
     * @return
     */
    public String getString(String key) {
        return getString(key, false);
    }

    public boolean getBoolean(String key, boolean isClear) {
        CacheDataItem dataItem = getCacheDataItem(key);
        if (dataItem == null) {
            return false;
        }
        boolean flag = dataItem.getFlag();
        if (isClear) {
            clear(key);
        }
        return flag;
    }

    public boolean getBoolean(String key) {
        return getBoolean(key, false);
    }

    public long getLong(String key, boolean isClear) {
        CacheDataItem dataItem = getCacheDataItem(key);
        if (dataItem == null) {
            return 0;
        }
        long value = dataItem.getLongValue();
        if (isClear) {
            clear(key);
        }
        return value;
    }

    public long getLong(String key) {
        return getLong(key, false);
    }

    public int getInt(String key, boolean isClear) {
        CacheDataItem dataItem = getCacheDataItem(key);
        if (dataItem == null) {
            return 0;
        }
        int value = dataItem.getIniValue();
        if (isClear) {
            clear(key);
        }
        return value;
    }

    public int getInt(String key) {
        return getInt(key, false);
    }

    private <T> void cacheData(String cacheKey, CacheDataItem dataItem, T value) {
        dataItem.setKey(cacheKey);
        if (value instanceof String) {
            dataItem.setValue(String.valueOf(value));
        } else if (value instanceof Boolean) {
            dataItem.setFlag((Boolean) value);
        } else if (value instanceof Integer) {
            dataItem.setIniValue(ConvertUtils.toInt(value));
        } else if (value instanceof Long) {
            dataItem.setLongValue(ConvertUtils.toLong(value));
        }
    }

    /**
     * 清除对象值
     *
     * @param objKey
     */
    public void clear(String objKey) {
        if (TextUtils.isEmpty(objKey)) {
            return;
        }
        DbCacheDao dbCacheDao = new DbCacheDao();
        CacheDataItemDao cacheDao = dbCacheDao.getCacheDao();
        if (cacheDao != null) {
            CacheDataItemDao.createTable(cacheDao.getDatabase(), true);
            cacheDao.deleteByKey(objKey);
        } else {
            //清除临时数据
            if (tempList.containsKey(objKey)) {
                tempList.remove(objKey);
            }
        }
    }

    /**
     * 清除对象值
     *
     * @param objKey
     */
    public void clearObject(String objKey) {
        if (TextUtils.isEmpty(objKey)) {
            return;
        }
        if (objList.containsKey(objKey)) {
            objList.remove(objKey);
        }
    }

    /**
     * 在objList中是否包含key
     *
     * @param key key
     * @return true-包含;false-不包含;
     */
    public boolean containerObjectKey(String key) {
        return objList.containsKey(key);
    }

    /**
     * 在tempList中是否包含key
     *
     * @param key key
     * @return true-包含;false-不包含;
     */
    public boolean containerKey(String key) {
        return tempList.containsKey(key);
    }
}
