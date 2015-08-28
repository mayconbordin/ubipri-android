package com.gppdi.ubipri.api.json;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.internal.bind.DateTypeAdapter;
import com.google.gson.reflect.TypeToken;
import com.gppdi.ubipri.BuildConfig;
import com.gppdi.ubipri.data.Fixtures;
import com.gppdi.ubipri.data.models.Environment;
import com.gppdi.ubipri.utils.GeoUtils;
import com.spatial4j.core.shape.Shape;
import com.spatial4j.core.shape.jts.JtsGeometry;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author mayconbordin
 */
@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21)
public class EnvironmentDeserializerTest {
    private Gson gson;

    @Before
    public void setUp() {
        gson = new GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .registerTypeAdapter(Date.class, new DateTypeAdapter())
                .registerTypeAdapter(Environment.class, new EnvironmentDeserializer())
                .enableComplexMapKeySerialization()
                .serializeNulls()
                .create();
    }

    @Test
    public void testDeserialize() throws Exception {
        Type listType = new TypeToken<ArrayList<Environment>>() {}.getType();

        List<Environment> environments = gson.fromJson(Fixtures.ENVIRONMENTS_JSON, listType);

        assertThat(environments.size()).isEqualTo(5);

        for (Environment e : environments) {
            assertThat(e).isNotNull();
            System.out.println(e);

            JtsGeometry shape = (JtsGeometry) GeoUtils.getSpatialContext().getFormats().getWktReader().read(e.getShape());
            System.out.println("Shape: " + shape);
        }
    }
}
