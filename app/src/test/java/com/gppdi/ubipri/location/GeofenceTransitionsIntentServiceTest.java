package com.gppdi.ubipri.location;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Location;
import android.support.v4.content.LocalBroadcastManager;
import android.widget.TextView;

import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingEvent;
import com.google.android.gms.location.internal.ParcelableGeofence;
import com.google.common.collect.ImmutableList;
import com.gppdi.ubipri.BuildConfig;
import com.gppdi.ubipri.R;
import com.gppdi.ubipri.TestUbiPriApplication;
import com.gppdi.ubipri.api.ApiService;
import com.gppdi.ubipri.data.DeviceManager;
import com.gppdi.ubipri.data.Fixtures;
import com.gppdi.ubipri.data.dao.EnvironmentDAO;
import com.gppdi.ubipri.data.models.Log;
import com.gppdi.ubipri.ui.activities.MainActivity;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.Shadows;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowActivity;
import org.robolectric.shadows.ShadowIntent;
import org.robolectric.shadows.ShadowLog;
import org.robolectric.shadows.ShadowService;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import static com.gppdi.ubipri.location.LocationConstants.ENVIRONMENT_NAME;
import static com.gppdi.ubipri.location.LocationConstants.EVENT_ENVIRONMENT_CHANGED;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

/**
 * @author mayconbordin
 */
@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21, application = TestUbiPriApplication.class)
public class GeofenceTransitionsIntentServiceTest {
    private GeofenceTransitionsIntentService intentService;

    @Inject ApiService apiService;
    @Inject EnvironmentDAO environmentDAO;

    @BeforeClass
    public static void setUpClass() {
        ShadowLog.stream = System.out;
    }

    @Before
    public void setUp() {
        intentService = Robolectric.buildService(GeofenceTransitionsIntentService.class).create().get();

        ((TestUbiPriApplication)intentService.getApplication()).inject(this);
    }

    @Test
    public void testOnHandleIntent() {
        // Set result of mock methods
        when(apiService.updateUserLocation(any(Log.class))).thenReturn(Fixtures.getActions());
        when(environmentDAO.findByExtId(anyInt())).thenAnswer(new Answer<Object>() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                Integer id = (Integer) invocation.getArguments()[0];
                return Fixtures.getEnvironment(id);
            }
        });

        // Set results for geofencing event
        GeofencingEvent event = mock(GeofencingEvent.class);
        when(event.hasError()).thenReturn(false);
        when(event.getGeofenceTransition()).thenReturn(Geofence.GEOFENCE_TRANSITION_ENTER);
        when(event.getTriggeringLocation()).thenReturn(Fixtures.LOCATION_LAB205);
        when(event.getTriggeringGeofences()).thenReturn(Fixtures.getEnvironmentsAsGeofencesFiltered(ImmutableList.of(4)));

        // Expect to receive a local broadcast with the name of current environment
        LocalBroadcastManager.getInstance(intentService).registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                assertThat(intent.hasExtra(ENVIRONMENT_NAME)).isTrue();
                assertThat(intent.getStringExtra(ENVIRONMENT_NAME)).isEqualTo(Fixtures.getEnvironment(5).getName());
            }
        }, new IntentFilter(EVENT_ENVIRONMENT_CHANGED));

        // Call method to be tested
        intentService.onHandleIntent(event);

        // Api service should be called four times
        verify(apiService, times(4)).updateUserLocation(any(Log.class));
        verify(environmentDAO, times(4)).findByExtId(anyInt());
    }

}
