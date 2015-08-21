package com.gppdi.ubipri.data.dao;

import com.gppdi.ubipri.BuildConfig;
import com.gppdi.ubipri.data.models.Environment;

import junit.framework.TestCase;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import java.util.List;

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
        dao.createOrUpdate(new Environment("Porto Alegre", -30.072296142578118,-51.17763595581054));
        dao.createOrUpdate(new Environment("Campus Vale UFRGS", -30.071927547454848,-51.12007999420165));
        dao.createOrUpdate(new Environment("Prédio Informática 72", -30.06849765777585,-51.12047672271728));
        dao.createOrUpdate(new Environment("Apartamento do Borges", -30.039857387542725,-51.20896339416505));
        dao.createOrUpdate(new Environment("Laboratório 205", -30.0686149597168,-51.1206169128418));
    }
}
