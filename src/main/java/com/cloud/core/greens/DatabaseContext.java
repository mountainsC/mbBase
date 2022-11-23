package com.cloud.core.greens;

import android.content.Context;
import android.content.ContextWrapper;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;

import com.cloud.core.utils.StorageUtils;

import java.io.File;
import java.io.IOException;

/**
 * @Author lijinghuan
 * @Email:ljh0576123@163.com
 * @CreateTime:2018/8/7
 * @Description:数据上下文,在初始化时传入
 * @Modifier:
 * @ModifyContent:
 */
public class DatabaseContext extends ContextWrapper {

    private OnDatabasePathListener onDatabasePathListener = null;

    public DatabaseContext(Context base, OnDatabasePathListener listener) {
        super(base);
        this.onDatabasePathListener = listener;
    }

    private File getDataDirectory(String databaseName) {
        File dir = StorageUtils.getDataCachesDir();
        File file = new File(dir, databaseName);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return file;
    }

    @Override
    public File getDatabasePath(String name) {
        if (onDatabasePathListener == null) {
            return getDataDirectory(name);
        } else {
            File file = onDatabasePathListener.onDatabasePath();
            if (file == null) {
                return getDataDirectory(name);
            } else {
                return file;
            }
        }
    }

    @Override
    public SQLiteDatabase openOrCreateDatabase(String name, int mode, SQLiteDatabase.CursorFactory factory) {
        return SQLiteDatabase.openOrCreateDatabase(getDatabasePath(name), null);
    }

    @Override
    public SQLiteDatabase openOrCreateDatabase(String name, int mode, SQLiteDatabase.CursorFactory factory, DatabaseErrorHandler errorHandler) {
        return SQLiteDatabase.openOrCreateDatabase(getDatabasePath(name), null);
    }
}
