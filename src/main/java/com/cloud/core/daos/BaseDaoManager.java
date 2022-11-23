package com.cloud.core.daos;

import com.cloud.core.events.Action1;
import com.cloud.core.greens.DBManager;
import com.cloud.core.greens.RxSqliteOpenHelper;
import com.cloud.core.logger.Logger;

/**
 * @Author lijinghuan
 * @Email:ljh0576123@163.com
 * @CreateTime:2017/11/15
 * @Description:
 * @Modifier:
 * @ModifyContent:
 */
public class BaseDaoManager {
    protected void getDao(final Action1<DaoSession> action) {
        try {
            if (action == null) {
                return;
            }
            RxSqliteOpenHelper helper = DBManager.getInstance().getHelper();
            if(helper==null){
                return;
            }
            DaoMaster daoMaster = new DaoMaster(helper.getWritableDatabase());
            if (daoMaster == null) {
                return;
            }
            DaoSession daoSession = daoMaster.newSession();
            if (daoSession == null) {
                return;
            }
            action.call(daoSession);
        } catch (Exception e) {
            Logger.L.error(e);
        }
    }
}
