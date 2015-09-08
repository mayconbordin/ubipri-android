package com.gppdi.ubipri.ui.activities;

import com.gppdi.ubipri.BuildConfig;
import com.gppdi.ubipri.TestUbiPriApplication;
import com.gppdi.ubipri.api.ApiService;
import com.gppdi.ubipri.data.DeviceManager;
import com.gppdi.ubipri.data.models.Device;

import static org.assertj.core.api.Assertions.*;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Matchers;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricGradleTestRunner;

import static org.mockito.Mockito.*;

import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowLog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import retrofit.Callback;
import retrofit.client.Header;
import retrofit.client.Response;
import rx.Observable;

/**
 * @author mayconbordin
 */
@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21, application = TestUbiPriApplication.class)
public class MainActivityTest {
    private MainActivity activity;

    @Inject DeviceManager deviceManager;
    @Inject ApiService apiService;

    @BeforeClass
    public static void setUpClass() {
        ShadowLog.stream = System.out;
    }

    @Before
    public void setUp() {
        activity = Robolectric.buildActivity(MainActivity.class).create().get();

        ((TestUbiPriApplication)activity.getApplication()).inject(this);
    }

    @Test
    public void testRegisterDevice() {
        // Assert state before registering device
        assertThat(deviceManager.isDeviceRegistered()).isFalse();

        ArgumentCaptor<Callback> callbackArgumentCaptor = ArgumentCaptor.forClass(Callback.class);

        // Call activity method
        activity.checkDeviceRegistered();

        // Verify that the api service was called
        verify(apiService).registerUserDevice(any(Device.class), callbackArgumentCaptor.capture());

        // retrieve the callback object
        Callback cb = callbackArgumentCaptor.getValue();

        // call the success method of the callback
        Response response = new Response("", 200, "", new ArrayList<Header>(), null);
        cb.success(new HashMap(), response);

        // Make the assertions
        assertThat(cb).isNotNull();
        assertThat(deviceManager.isDeviceRegistered()).isTrue();
    }
}
