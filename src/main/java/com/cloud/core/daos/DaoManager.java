package com.cloud.core.daos;


import com.cloud.core.events.Action1;

/**
 * @Author lijinghuan
 * @Email:ljh0576123@163.com
 * @CreateTime:2017/11/15
 * @Description:
 * @Modifier:
 * @ModifyContent:
 */
public class DaoManager extends BaseDaoManager {
    private static DaoManager daoManager = null;

    public static DaoManager getInstance() {
        return daoManager == null ? daoManager = new DaoManager() : daoManager;
    }

    //获取scheme缓存数据项
    public SchemeCacheItemDao getSchemeCacheItemDao() {
        final SchemeCacheItemDao[] itemDaos = new SchemeCacheItemDao[1];
        super.getDao(new Action1<DaoSession>() {
            @Override
            public void call(DaoSession daoSession) {
                SchemeCacheItemDao beanDao = daoSession.getSchemeCacheItemDao();
                if (beanDao == null) {
                    return;
                }
                SchemeCacheItemDao.createTable(daoSession.getDatabase(), true);
                itemDaos[0] = beanDao;
            }
        });
        return itemDaos[0];
    }
    public void getBreakPointBeanDao(final Action1<BreakPointBeanDao> action) {
        if (action == null) {
            return;
        }
        super.getDao(new Action1<DaoSession>() {
            @Override
            public void call(DaoSession daoSession) {
                BreakPointBeanDao beanDao = daoSession.getBreakPointBeanDao();
                if (beanDao == null) {
                    return;
                }
                BreakPointBeanDao.createTable(daoSession.getDatabase(), true);
                action.call(beanDao);
            }
        });
    }

    public void getAppendPositionBeanDao(final Action1<AppendPositionBeanDao> action) {
        if (action == null) {
            return;
        }
        super.getDao(new Action1<DaoSession>() {
            @Override
            public void call(DaoSession daoSession) {
                AppendPositionBeanDao beanDao = daoSession.getAppendPositionBeanDao();
                if (beanDao == null) {
                    return;
                }
                AppendPositionBeanDao.createTable(daoSession.getDatabase(), true);
                action.call(beanDao);
            }
        });
    }

    public void getAddressItemDao(final Action1<AddressItemDao> action) {
        if (action == null) {
            return;
        }
        super.getDao(new Action1<DaoSession>() {
            @Override
            public void call(DaoSession daoSession) {
                AddressItemDao itemDao = daoSession.getAddressItemDao();
                if (itemDao == null) {
                    return;
                }
                AddressItemDao.createTable(daoSession.getDatabase(), true);
                action.call(itemDao);
            }
        });
    }

    public void getAddressListDao(final Action1<PickerItemDao> action) {
        if (action == null) {
            return;
        }
        super.getDao(new Action1<DaoSession>() {
            @Override
            public void call(DaoSession daoSession) {
                PickerItemDao itemDao = daoSession.getPickerItemDao();
                if (itemDao == null) {
                    return;
                }
                PickerItemDao.createTable(daoSession.getDatabase(), true);
                action.call(itemDao);
            }
        });
    }
    public void getOptionsItemDao(final Action1<OptionsItemDao> action) {
        if (action == null) {
            return;
        }
        super.getDao(new Action1<DaoSession>() {
            @Override
            public void call(DaoSession daoSession) {
                // TODO: 2021/12/19  
//                OptionsItemDao itemDao = daoSession.getOptionsItemDao();
//                if (itemDao == null) {
//                    return;
//                }
//                OptionsItemDao.createTable(daoSession.getDatabase(), true);
//                action.call(itemDao);
            }
        });
    }
}
