package com.gppdi.ubipri.data;

import android.app.Application;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.gppdi.ubipri.data.dao.EnvironmentDAO;
import com.gppdi.ubipri.data.dao.EnvironmentTypeDAO;
import com.gppdi.ubipri.data.dao.LocalizationTypeDAO;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module(
    complete = false,
    library = true
)
public class DataModule {
    @Provides @Singleton EnvironmentDAO provideEnvironmentDao() {
        return new EnvironmentDAO();
    }

    @Provides @Singleton EnvironmentTypeDAO provideEnvironmentTypeDao() {
        return new EnvironmentTypeDAO();
    }

    @Provides @Singleton LocalizationTypeDAO provideLocalizationTypeDao() {
        return new LocalizationTypeDAO();
    }
}
