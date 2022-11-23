package com.cloud.core.ebus;

import com.cloud.core.daos.DaoMaster;
import com.cloud.core.daos.DaoSession;
import com.cloud.core.daos.EBusDataItemDao;
import com.cloud.core.greens.DBManager;
import com.cloud.core.greens.RxSqliteOpenHelper;
import com.cloud.core.logger.Logger;

/**
 * Author lijinghuan
 * Email:ljh0576123@163.com
 * CreateTime:2018/10/23
 * Description:
 * Modifier:
 * ModifyContent:
 */
class EBusDataCacheDao {
    private EBusDataItemDao dataItemDao = null;

    public EBusDataItemDao getEBusDataItemDao() {
        try {
            RxSqliteOpenHelper helper = DBManager.getInstance().getHelper();
            if (helper == null) {
                return null;
            }
            DaoMaster daoMaster = new DaoMaster(helper.getWritableDatabase());
            DaoSession daoSession = daoMaster.newSession();
            dataItemDao = daoSession.getEBusDataItemDao();
        } catch (Exception e) {
            Logger.L.error(e);
        }
        return dataItemDao;
    }
}
