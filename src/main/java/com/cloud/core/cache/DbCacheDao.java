package com.cloud.core.cache;

import com.cloud.core.daos.CacheDataItemDao;
import com.cloud.core.daos.DaoMaster;
import com.cloud.core.daos.DaoSession;
import com.cloud.core.greens.DBManager;
import com.cloud.core.greens.RxSqliteOpenHelper;
import com.cloud.core.logger.Logger;

/**
 * Author lijinghuan
 * Email:ljh0576123@163.com
 * CreateTime:2017/11/14
 * Description:
 * Modifier:
 * ModifyContent:
 */
public class DbCacheDao {
    private CacheDataItemDao dataItemDao = null;

    public CacheDataItemDao getCacheDao() {
        try {
            RxSqliteOpenHelper helper = DBManager.getInstance().getHelper();
            if (helper == null) {
                return null;
            }
            DaoMaster daoMaster = new DaoMaster(helper.getWritableDatabase());
            DaoSession daoSession = daoMaster.newSession();
            dataItemDao = daoSession.getCacheDataItemDao();
        } catch (Exception e) {
            Logger.L.error(e);
        }
        return dataItemDao;
    }
}
