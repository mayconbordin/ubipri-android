package com.gppdi.ubipri.data;

import android.app.Application;
import android.content.SharedPreferences;

import com.gppdi.ubipri.api.ApiService;
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

    @Provides @Singleton DataService provideDataService(ApiService apiService, EnvironmentDAO environmentDAO) {
        return new DataService(apiService, environmentDAO);
    }

    @Provides @Singleton DeviceManager provideDeviceManager(Application app, SharedPreferences sharedPreferences) {
        return new DeviceManager(app, sharedPreferences);
    }
}
