package com.cloud.core.greens;

import android.content.Context;
import android.text.TextUtils;

import com.cloud.core.configs.BuildConfig;
import com.cloud.core.daos.CacheDataItemDao;
import com.cloud.core.logger.Logger;
import com.cloud.core.utils.ConvertUtils;

import org.greenrobot.greendao.AbstractDao;

import java.util.HashMap;

/**
 * Author lijinghuan
 * Email:ljh0576123@163.com
 * CreateTime:2017/11/14
 * Description:数据库管理类
 * Modifier:
 * ModifyContent:
 */
public class DBManager {

    private static DBManager dbManager = null;
    private String databaseNameKey = "";
    private Context context;
    private HashMap<String, RxSqliteOpenHelper> helperHashMap = new HashMap<String, RxSqliteOpenHelper>();

    public static DBManager getInstance() {
        return dbManager == null ? dbManager = new DBManager() : dbManager;
    }

    /**
     * 初始化数据库
     *
     * @param context            上下文
     * @param databaseName       数据名称
     * @param listener           数据库目录回调
     * @param isDefDatabase-true 默认数据库;false-在获取helper时要传入数据库名称
     * @param daoClasses         表对应的dao类
     */
    @SafeVarargs
    public final void initializeBaseDb(Context context,
                                       String databaseName,
                                       OnDatabasePathListener listener,
                                       boolean isDefDatabase,
                                       Class<? extends AbstractDao<?, ?>>... daoClasses) {
        try {
            this.context = context;
            Class<? extends AbstractDao<?, ?>>[] array = ConvertUtils.toJoinArray(CacheDataItemDao.class, daoClasses);
            RxSqliteOpenHelper mhelper = new RxSqliteOpenHelper(context, databaseName, listener, array);
            if (isDefDatabase) {
                helperHashMap.put(BuildConfig.getInstance().isRelease(context) ? "1fc7bc8f102a441f9fd311ab34b190d1" : "6fe86c4bdad24c0f88789073ef132c99", mhelper);
            } else {
                helperHashMap.put(databaseName, mhelper);
            }
        } catch (Exception e) {
            Logger.L.error(e);
        }
    }

    /**
     * 初始化数据库
     *
     * @param context      上下文
     * @param databaseName 数据名称
     * @param daoClasses   表对应的dao类
     */
    @SafeVarargs
    public final void initializeBaseDb(Context context,
                                       String databaseName,
                                       boolean isDefDatabase,
                                       Class<? extends AbstractDao<?, ?>>... daoClasses) {
        initializeBaseDb(context, databaseName, null, isDefDatabase, daoClasses);
    }

    /**
     * 获取sqlite帮助类
     *
     * @param databaseName 数据库名称
     * @return
     */
    public RxSqliteOpenHelper getHelper(String databaseName) {
        String key = "";
        if (TextUtils.isEmpty(databaseName)) {
            key = BuildConfig.getInstance().isRelease(context) ? "1fc7bc8f102a441f9fd311ab34b190d1" : "6fe86c4bdad24c0f88789073ef132c99";
        } else {
            key = databaseName;
        }
        if (!helperHashMap.containsKey(key)) {
            return null;
        }
        return helperHashMap.get(key);
    }

    /**
     * 获取sqlite帮助类
     *
     * @return
     */
    public RxSqliteOpenHelper getHelper() {
        return getHelper("");
    }
}
