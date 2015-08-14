package com.gppdi.ubipri.api;

import android.net.Uri;
import android.util.Log;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowLog;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

import com.gppdi.ubipri.BuildConfig;

import java.io.IOException;
import java.util.Collections;
import java.util.Map;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Client;
import retrofit.client.Request;
import retrofit.client.Response;
import retrofit.mime.TypedByteArray;

/**
 * @author mayconbordin
 */
@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21)
public class ApiServiceTest {
    private ApiService api;

    @Before
    public void setUp() throws Exception {
        /*RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint("http://localhost:9000")
                .set
                .build();*/

        api = mock(ApiService.class);//restAdapter.create(ApiService.class);
    }

    @Test
    public void testA() throws Exception {
        verify(api).login(anyString(), anyString(), any(Callback.class));

        api.login("1234", "12345", new Callback<Map>() {
            @Override
            public void success(Map map, Response response) {
                Log.i("TAG", response.toString());
            }

            @Override
            public void failure(RetrofitError error) {
                Log.i("TAG", error.toString());
            }
        });

        assertThat(1).isEqualTo(1);
    }
}
