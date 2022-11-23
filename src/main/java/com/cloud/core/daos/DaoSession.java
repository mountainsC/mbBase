package com.cloud.core.daos;

import java.util.Map;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.AbstractDaoSession;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.identityscope.IdentityScopeType;
import org.greenrobot.greendao.internal.DaoConfig;

import com.cloud.core.beans.AppendPositionBean;
import com.cloud.core.beans.BreakPointBean;
import com.cloud.coretest.beans.TestBean;
import com.cloud.core.cache.CacheDataItem;
import com.cloud.core.configs.scheme.SchemeCacheItem;
import com.cloud.core.ebus.EBusDataItem;
import com.cloud.core.picker.AddressItem;
import com.cloud.core.picker.PickerItem;

import com.cloud.core.daos.AppendPositionBeanDao;
import com.cloud.core.daos.BreakPointBeanDao;
import com.cloud.core.daos.TestBeanDao;
import com.cloud.core.daos.CacheDataItemDao;
import com.cloud.core.daos.SchemeCacheItemDao;
import com.cloud.core.daos.EBusDataItemDao;
import com.cloud.core.daos.AddressItemDao;
import com.cloud.core.daos.PickerItemDao;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.

/**
 * {@inheritDoc}
 * 
 * @see org.greenrobot.greendao.AbstractDaoSession
 */
public class DaoSession extends AbstractDaoSession {

    private final DaoConfig appendPositionBeanDaoConfig;
    private final DaoConfig breakPointBeanDaoConfig;
    private final DaoConfig testBeanDaoConfig;
    private final DaoConfig cacheDataItemDaoConfig;
    private final DaoConfig schemeCacheItemDaoConfig;
    private final DaoConfig eBusDataItemDaoConfig;
    private final DaoConfig addressItemDaoConfig;
    private final DaoConfig pickerItemDaoConfig;

    private final AppendPositionBeanDao appendPositionBeanDao;
    private final BreakPointBeanDao breakPointBeanDao;
    private final TestBeanDao testBeanDao;
    private final CacheDataItemDao cacheDataItemDao;
    private final SchemeCacheItemDao schemeCacheItemDao;
    private final EBusDataItemDao eBusDataItemDao;
    private final AddressItemDao addressItemDao;
    private final PickerItemDao pickerItemDao;

    public DaoSession(Database db, IdentityScopeType type, Map<Class<? extends AbstractDao<?, ?>>, DaoConfig>
            daoConfigMap) {
        super(db);

        appendPositionBeanDaoConfig = daoConfigMap.get(AppendPositionBeanDao.class).clone();
        appendPositionBeanDaoConfig.initIdentityScope(type);

        breakPointBeanDaoConfig = daoConfigMap.get(BreakPointBeanDao.class).clone();
        breakPointBeanDaoConfig.initIdentityScope(type);

        testBeanDaoConfig = daoConfigMap.get(TestBeanDao.class).clone();
        testBeanDaoConfig.initIdentityScope(type);

        cacheDataItemDaoConfig = daoConfigMap.get(CacheDataItemDao.class).clone();
        cacheDataItemDaoConfig.initIdentityScope(type);

        schemeCacheItemDaoConfig = daoConfigMap.get(SchemeCacheItemDao.class).clone();
        schemeCacheItemDaoConfig.initIdentityScope(type);

        eBusDataItemDaoConfig = daoConfigMap.get(EBusDataItemDao.class).clone();
        eBusDataItemDaoConfig.initIdentityScope(type);

        addressItemDaoConfig = daoConfigMap.get(AddressItemDao.class).clone();
        addressItemDaoConfig.initIdentityScope(type);

        pickerItemDaoConfig = daoConfigMap.get(PickerItemDao.class).clone();
        pickerItemDaoConfig.initIdentityScope(type);

        appendPositionBeanDao = new AppendPositionBeanDao(appendPositionBeanDaoConfig, this);
        breakPointBeanDao = new BreakPointBeanDao(breakPointBeanDaoConfig, this);
        testBeanDao = new TestBeanDao(testBeanDaoConfig, this);
        cacheDataItemDao = new CacheDataItemDao(cacheDataItemDaoConfig, this);
        schemeCacheItemDao = new SchemeCacheItemDao(schemeCacheItemDaoConfig, this);
        eBusDataItemDao = new EBusDataItemDao(eBusDataItemDaoConfig, this);
        addressItemDao = new AddressItemDao(addressItemDaoConfig, this);
        pickerItemDao = new PickerItemDao(pickerItemDaoConfig, this);

        registerDao(AppendPositionBean.class, appendPositionBeanDao);
        registerDao(BreakPointBean.class, breakPointBeanDao);
        registerDao(TestBean.class, testBeanDao);
        registerDao(CacheDataItem.class, cacheDataItemDao);
        registerDao(SchemeCacheItem.class, schemeCacheItemDao);
        registerDao(EBusDataItem.class, eBusDataItemDao);
        registerDao(AddressItem.class, addressItemDao);
        registerDao(PickerItem.class, pickerItemDao);
    }
    
    public void clear() {
        appendPositionBeanDaoConfig.clearIdentityScope();
        breakPointBeanDaoConfig.clearIdentityScope();
        testBeanDaoConfig.clearIdentityScope();
        cacheDataItemDaoConfig.clearIdentityScope();
        schemeCacheItemDaoConfig.clearIdentityScope();
        eBusDataItemDaoConfig.clearIdentityScope();
        addressItemDaoConfig.clearIdentityScope();
        pickerItemDaoConfig.clearIdentityScope();
    }

    public AppendPositionBeanDao getAppendPositionBeanDao() {
        return appendPositionBeanDao;
    }

    public BreakPointBeanDao getBreakPointBeanDao() {
        return breakPointBeanDao;
    }

    public TestBeanDao getTestBeanDao() {
        return testBeanDao;
    }

    public CacheDataItemDao getCacheDataItemDao() {
        return cacheDataItemDao;
    }

    public SchemeCacheItemDao getSchemeCacheItemDao() {
        return schemeCacheItemDao;
    }

    public EBusDataItemDao getEBusDataItemDao() {
        return eBusDataItemDao;
    }

    public AddressItemDao getAddressItemDao() {
        return addressItemDao;
    }

    public PickerItemDao getPickerItemDao() {
        return pickerItemDao;
    }

}