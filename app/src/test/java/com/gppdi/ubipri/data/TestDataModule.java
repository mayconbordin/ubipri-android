package com.gppdi.ubipri.data;

import android.app.Application;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.gppdi.ubipri.api.ApiService;
import com.gppdi.ubipri.data.dao.EnvironmentDAO;
import com.gppdi.ubipri.data.dao.EnvironmentTypeDAO;
import com.gppdi.ubipri.data.dao.LocalizationTypeDAO;
import com.gppdi.ubipri.functionality.FunctionalityManager;

import org.mockito.Mockito;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module(
    complete = false,
    library = true
)
public class TestDataModule {
    @Provides @Singleton EnvironmentDAO provideEnvironmentDao() {
        return Mockito.mock(EnvironmentDAO.class);
    }

    @Provides @Singleton EnvironmentTypeDAO provideEnvironmentTypeDao() {
        return Mockito.mock(EnvironmentTypeDAO.class);
    }

    @Provides @Singleton LocalizationTypeDAO provideLocalizationTypeDao() {
        return Mockito.mock(LocalizationTypeDAO.class);
    }

    @Provides @Singleton DataService provideDataService(ApiService apiService, EnvironmentDAO environmentDAO,
                                                        DeviceManager deviceManager, SharedPreferences sharedPreferences,
                                                        Gson gson) {
        return new DataService(apiService, environmentDAO, deviceManager, sharedPreferences, gson);
    }

    @Provides @Singleton DeviceManager provideDeviceManager(Application app, SharedPreferences sharedPreferences) {
        return new DeviceManager(app, sharedPreferences);
    }

    @Provides @Singleton FunctionalityManager provideFunctionalityManager(Application app) {
        return new FunctionalityManager(app);
    }
}
