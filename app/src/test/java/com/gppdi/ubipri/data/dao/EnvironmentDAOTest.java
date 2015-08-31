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
    private EnvironmentTypeDAO typeDAO;
    private LocalizationTypeDAO localizationTypeDAO;

    public EnvironmentDAOTest() {
        dao = new EnvironmentDAO();
        typeDAO = new EnvironmentTypeDAO();
        localizationTypeDAO = new LocalizationTypeDAO();
    }

    @Test
    public void testExists() {
        boolean exists = dao.exists(1L);
        assertThat(exists).isFalse();
    }

    @Test
    public void testCreate() {
        assertThat(dao.existsExtId(1)).isFalse();

        Environment e = new Environment();
        e.setExtId(1);
        e.setName("teste");

        dao.createOrUpdate(e);

        assertThat(dao.exists(e.getId())).isTrue();
    }

    @Test
    public void testCreateComplex() {
        assertThat(dao.findAll().size()).isEqualTo(0);

        List<Environment> environments = Fixtures.getEnvironments();

        for (Environment e : environments) {

            if (!typeDAO.existsExtId(e.getEnvironmentType().getExtId())) {
                typeDAO.createOrUpdate(e.getEnvironmentType());
            } else {
                e.setEnvironmentType(typeDAO.findByExtId(e.getEnvironmentType().getExtId()));
            }

            if (!localizationTypeDAO.existsExtId(e.getLocalizationType().getExtId())) {
                localizationTypeDAO.createOrUpdate(e.getLocalizationType());
            } else {
                e.setLocalizationType(localizationTypeDAO.findByExtId(e.getLocalizationType().getExtId()));
            }

            dao.createOrUpdate(e);

            Environment test = dao.findByExtId(e.getExtId());

            assertThat(dao.exists(test.getId())).isTrue();
            assertThat(dao.existsExtId(test.getExtId())).isTrue();
            assertThat(test.getEnvironmentType().getId()).isNotNull();
            assertThat(test.getLocalizationType().getId()).isNotNull();

            System.out.println(test);
        }
    }

    @Test
    public void testFindNear() {
        populate();

        List<Environment> results = dao.findNear(-30.071927547454848, -51.12007999420165, 800);
        assertThat(results.size()).isEqualTo(3);

        results = dao.findNear(-30.071927547454848,-51.12007999420165, 800, 1);
        assertThat(results.size()).isEqualTo(1);
    }

    protected void populate() {
        for (Environment e : Fixtures.ENVIRONMENTS.values()) {
            dao.createOrUpdate(e);
            assertThat(dao.exists(e.getId())).isTrue();
        }
    }
}
