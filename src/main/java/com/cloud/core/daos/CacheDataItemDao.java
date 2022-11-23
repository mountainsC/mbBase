package com.cloud.core.daos;

import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.internal.DaoConfig;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseStatement;

import com.cloud.core.cache.CacheDataItem;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "rx_cache_data".
*/
public class CacheDataItemDao extends AbstractDao<CacheDataItem, String> {

    public static final String TABLENAME = "rx_cache_data";

    /**
     * Properties of entity CacheDataItem.<br/>
     * Can be used for QueryBuilder and for referencing column names.
     */
    public static class Properties {
        public final static Property Key = new Property(0, String.class, "key", true, "key");
        public final static Property Value = new Property(1, String.class, "value", false, "value");
        public final static Property Effective = new Property(2, long.class, "effective", false, "effective");
        public final static Property Flag = new Property(3, boolean.class, "flag", false, "flag");
        public final static Property IniValue = new Property(4, int.class, "iniValue", false, "iniValue");
        public final static Property LongValue = new Property(5, long.class, "longValue", false, "longValue");
    }


    public CacheDataItemDao(DaoConfig config) {
        super(config);
    }
    
    public CacheDataItemDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(Database db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"rx_cache_data\" (" + //
                "\"key\" TEXT PRIMARY KEY NOT NULL ," + // 0: key
                "\"value\" TEXT," + // 1: value
                "\"effective\" INTEGER NOT NULL ," + // 2: effective
                "\"flag\" INTEGER NOT NULL ," + // 3: flag
                "\"iniValue\" INTEGER NOT NULL ," + // 4: iniValue
                "\"longValue\" INTEGER NOT NULL );"); // 5: longValue
        // Add Indexes
        db.execSQL("CREATE UNIQUE INDEX " + constraint + "IDX_rx_cache_data_key ON \"rx_cache_data\"" +
                " (\"key\" ASC);");
    }

    /** Drops the underlying database table. */
    public static void dropTable(Database db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"rx_cache_data\"";
        db.execSQL(sql);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, CacheDataItem entity) {
        stmt.clearBindings();
 
        String key = entity.getKey();
        if (key != null) {
            stmt.bindString(1, key);
        }
 
        String value = entity.getValue();
        if (value != null) {
            stmt.bindString(2, value);
        }
        stmt.bindLong(3, entity.getEffective());
        stmt.bindLong(4, entity.getFlag() ? 1L: 0L);
        stmt.bindLong(5, entity.getIniValue());
        stmt.bindLong(6, entity.getLongValue());
    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, CacheDataItem entity) {
        stmt.clearBindings();
 
        String key = entity.getKey();
        if (key != null) {
            stmt.bindString(1, key);
        }
 
        String value = entity.getValue();
        if (value != null) {
            stmt.bindString(2, value);
        }
        stmt.bindLong(3, entity.getEffective());
        stmt.bindLong(4, entity.getFlag() ? 1L: 0L);
        stmt.bindLong(5, entity.getIniValue());
        stmt.bindLong(6, entity.getLongValue());
    }

    @Override
    public String readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getString(offset + 0);
    }    

    @Override
    public CacheDataItem readEntity(Cursor cursor, int offset) {
        CacheDataItem entity = new CacheDataItem( //
            cursor.isNull(offset + 0) ? null : cursor.getString(offset + 0), // key
            cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1), // value
            cursor.getLong(offset + 2), // effective
            cursor.getShort(offset + 3) != 0, // flag
            cursor.getInt(offset + 4), // iniValue
            cursor.getLong(offset + 5) // longValue
        );
        return entity;
    }
     
    @Override
    public void readEntity(Cursor cursor, CacheDataItem entity, int offset) {
        entity.setKey(cursor.isNull(offset + 0) ? null : cursor.getString(offset + 0));
        entity.setValue(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
        entity.setEffective(cursor.getLong(offset + 2));
        entity.setFlag(cursor.getShort(offset + 3) != 0);
        entity.setIniValue(cursor.getInt(offset + 4));
        entity.setLongValue(cursor.getLong(offset + 5));
     }
    
    @Override
    protected final String updateKeyAfterInsert(CacheDataItem entity, long rowId) {
        return entity.getKey();
    }
    
    @Override
    public String getKey(CacheDataItem entity) {
        if(entity != null) {
            return entity.getKey();
        } else {
            return null;
        }
    }

    @Override
    public boolean hasKey(CacheDataItem entity) {
        return entity.getKey() != null;
    }

    @Override
    protected final boolean isEntityUpdateable() {
        return true;
    }
    
}