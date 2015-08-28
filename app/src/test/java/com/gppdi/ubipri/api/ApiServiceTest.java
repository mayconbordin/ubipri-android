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
import com.gppdi.ubipri.data.models.Environment;
import com.gppdi.ubipri.utils.rx.EndlessObserver;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

import javax.inject.Inject;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Client;
import retrofit.client.Request;
import retrofit.client.Response;
import retrofit.mime.TypedByteArray;
import rx.Observable;
import rx.Scheduler;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * @author mayconbordin
 */
@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21)
public class ApiServiceTest {
    private ApiService api;

    @Before
    public void setUp() throws Exception {
        api = new MockApiService();
    }

    @Test
    public void testA() throws Exception {
        final AtomicReference<Object> testResult = new AtomicReference<>();

        api.getEnvironments(-30.0722961425781, -51.1776359558105, 10.0).subscribe(new EndlessObserver<List<Environment>>() {
            @Override
            public void onNext(List<Environment> environments) {
                testResult.set(environments);
            }

            @Override
            public void onError(Throwable error) {
                throw new RuntimeException(error);
            }
        });

        List<Environment> results = (List<Environment>) testResult.get();
        assertThat(results.size()).isEqualTo(5);
    }
}
