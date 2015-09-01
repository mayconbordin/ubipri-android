package com.gppdi.ubipri.ui.activities;

import com.gppdi.ubipri.BuildConfig;
import com.gppdi.ubipri.api.ApiService;
import com.gppdi.ubipri.data.models.Device;

import static org.assertj.core.api.Assertions.*;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Matchers;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricGradleTestRunner;

import static org.mockito.Mockito.*;

import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowLog;

import java.util.HashMap;
import java.util.Map;

import rx.Observable;

/**
 * @author mayconbordin
 */
@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21)
public class MainActivityTest {
    private MainActivity activity;
    private ApiService apiService;

    @BeforeClass
    public static void setUpClass() {
        ShadowLog.stream = System.out;
    }

    @Before
    public void setUp() {
        apiService = mock(ApiService.class);
        when(apiService.registerUserDeviceObservable(Matchers.<Device>anyObject())).thenReturn(Observable.<Map>from(new HashMap()));

        activity = Robolectric.buildActivity(MainActivity.class).create().get();
        activity.apiService = apiService;
    }

    @Test
    public void clickingLogin_shouldStartLoginActivity() {
        activity.checkDeviceRegistered();
        System.out.println(activity.deviceManager.getDevice());

        assertThat(activity.apiService).isEqualTo(apiService);
        verify(apiService, times(1)).registerUserDeviceObservable(activity.deviceManager.getDevice());


        //MainActivity activity = Robolectric.setupActivity(MainActivity.class);

        //activity.findViewById(R.id.login).performClick();

        //Intent expectedIntent = new Intent(activity, MainActivity.class);
        //assertThat(shadowOf(activity).getNextStartedActivity()).isEqualTo(expectedIntent);
    }
}
