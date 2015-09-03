package com.gppdi.ubipri.data;

import android.content.SharedPreferences;
import android.location.Location;

import com.google.android.gms.location.Geofence;
import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import com.google.common.collect.Lists;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.internal.bind.DateTypeAdapter;
import com.gppdi.ubipri.BuildConfig;
import com.gppdi.ubipri.api.ApiService;
import com.gppdi.ubipri.api.json.EnvironmentDeserializer;
import com.gppdi.ubipri.data.dao.EnvironmentDAO;
import com.gppdi.ubipri.data.models.Action;
import com.gppdi.ubipri.data.models.Device;
import com.gppdi.ubipri.data.models.Environment;
import com.gppdi.ubipri.data.models.Log;

import static org.assertj.core.api.Assertions.*;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowLog;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import static org.mockito.Mockito.*;

/**
 * @author mayconbordin
 */
@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21)
public class DataServiceTest {

    private DataService dataService;
    private ApiService apiService;
    private EnvironmentDAO environmentDAO;
    private DeviceManager deviceManager;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private Gson gson;

    @BeforeClass
    public static void setUpClass() {
        ShadowLog.stream = System.out;
    }

    @Before
    public void setUp() {
        gson = new GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .registerTypeAdapter(Date.class, new DateTypeAdapter())
                .registerTypeAdapter(Environment.class, new EnvironmentDeserializer())
                .enableComplexMapKeySerialization()
                .serializeNulls()
                .create();

        apiService = mock(ApiService.class);
        environmentDAO = mock(EnvironmentDAO.class);
        deviceManager = mock(DeviceManager.class);
        sharedPreferences = mock(SharedPreferences.class);

        editor = mock(SharedPreferences.Editor.class);
        when(sharedPreferences.edit()).thenReturn(editor);

        dataService = new DataService(apiService, environmentDAO, deviceManager, sharedPreferences, gson);

        Device device = new Device();
        device.setCode("12345");
        device.setName("Samsung S3");
        when(deviceManager.getDevice()).thenReturn(device);
    }

    @Test
    public void testGetEnvironments() {
        List<Environment> environmentList = Fixtures.getEnvironments();
        Location center = new Location("mocked");
        center.setLatitude(-30.072296142578118);
        center.setLongitude(-51.17763595581054);

        double radius = 20.0;

        when(apiService.getEnvironments(center.getLatitude(), center.getLongitude(), radius)).thenReturn(environmentList);
        when(environmentDAO.findByExtId(anyInt())).thenReturn(null);

        for (Environment e : environmentList) {
            when(environmentDAO.createOrUpdate(e)).thenReturn(e);
        }

        List<Environment> environments = dataService.getEnvironments(center, radius);

        assertThat(environments.size()).isEqualTo(environmentList.size());
    }

    @Test
    public void testUpdateLocation() {
        // List of environments the user will enter
        Collection<Environment> environmentList = Collections2.filter(Fixtures.getEnvironments(), new Predicate<Environment>() {
            @Override public boolean apply(Environment environment) {
                return environment.getExtId() != 4;
            }
        });

        // create geofences for these environments
        List<Geofence> geofences = new ArrayList<>();
        for (Environment e : environmentList) {
            geofences.add(e.toGeofence());
        }

        // return the environment by ext id
        for (Environment e : environmentList) {
            when(environmentDAO.findByExtId(e.getExtId())).thenReturn(e);
        }

        when(apiService.updateUserLocation(any(Log.class))).thenReturn(Fixtures.getActions());
        when(editor.putInt("current_environment", 5)).thenReturn(editor);
        when(editor.putString(anyString(), anyString())).thenReturn(editor);

        // current user location that triggered the geofences
        Location location = new Location("mocked");
        location.setLatitude(-30.06858398802797);
        location.setLongitude(-51.12058103084564);

        List<Action> actions = dataService.updateLocation(location, geofences, false);

        assertThat(actions.size()).isEqualTo(1);

        verify(deviceManager, times(1)).getDevice();
        verify(environmentDAO, times(environmentList.size())).findByExtId(anyInt());
        verify(apiService, times(environmentList.size())).updateUserLocation(any(Log.class));

        verify(editor).putInt("current_environment", 5);
        verify(editor).putString(anyString(), anyString());
        verify(editor).apply();
    }
}
