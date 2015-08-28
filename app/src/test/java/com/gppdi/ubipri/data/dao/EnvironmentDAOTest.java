package com.gppdi.ubipri.data.dao;

import com.gppdi.ubipri.BuildConfig;
import com.gppdi.ubipri.data.Fixtures;
import com.gppdi.ubipri.data.models.Environment;

import junit.framework.TestCase;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

/**
 * @author mayconbordin
 */
@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21)
public class EnvironmentDAOTest /*extends TestCase*/ {
    private EnvironmentDAO dao;

    public EnvironmentDAOTest() {
        dao = new EnvironmentDAO();
    }

    @Test
    public void testExists() {
        boolean exists = dao.exists(1L);
        assertThat(exists).isFalse();
    }

    @Test
    public void testCreate() {
        Environment e = new Environment();
        e.setExtId(1);
        e.setName("teste");

        dao.createOrUpdate(e);

        assertThat(dao.exists(e.getId())).isTrue();
    }

    @Test
    public void testFindNear() {
        populate();

        List<Environment> results = dao.findNear(-30.071927547454848,-51.12007999420165, 800);
        assertThat(results.size()).isEqualTo(3);
    }

    protected void populate() {
        for (Environment e : Fixtures.ENVIRONMENTS.values()) {
            dao.createOrUpdate(e);
        }
    }
}
