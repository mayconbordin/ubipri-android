package com.gppdi.ubipri.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.gppdi.ubipri.dao.DaoMaster;
import com.gppdi.ubipri.dao.DaoSession;

import javax.inject.Singleton;

import dagger.Provides;

public class DBModule {
    private Context context;

    public DBModule(Context context) {
        this.context = context;
    }

    @Provides SQLiteDatabase provideDatabase() {
        DaoMaster.DevOpenHelper mHelper = new DaoMaster.DevOpenHelper(context, "sample-database", null);
        return mHelper.getReadableDatabase();
    }

    @Provides DaoSession provideDaoSession(SQLiteDatabase database) {
        DaoMaster daoMaster = new DaoMaster(database);
        return daoMaster.newSession();
    }
}
