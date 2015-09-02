package com.gppdi.ubipri.data;

import android.location.Location;

import com.google.android.gms.location.Geofence;
import com.gppdi.ubipri.api.ApiService;
import com.gppdi.ubipri.data.dao.EnvironmentDAO;
import com.gppdi.ubipri.data.models.Action;
import com.gppdi.ubipri.data.models.Environment;
import com.gppdi.ubipri.data.models.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

/**
 * @author mayconbordin
 */
public class DataService {
    private ApiService api;
    private EnvironmentDAO environmentDAO;
    private DeviceManager deviceManager;

    public DataService(ApiService api, EnvironmentDAO environmentDAO, DeviceManager deviceManager) {
        this.api = api;
        this.environmentDAO = environmentDAO;
        this.deviceManager = deviceManager;
    }

    /**
     * Get list of environments from the server, save them locally (overwriting if necessary).
     *
     * @param center
     * @param radius
     * @return
     */
    public List<Environment> getEnvironments(Location center, double radius) {
        List<Environment> environments = api.getEnvironments(center.getLatitude(), center.getLongitude(), radius);

        for (Environment environment : environments) {
            Environment temp = environmentDAO.findByExtId(environment.getExtId());

            if (temp != null && environment.getVersion() > temp.getVersion()) {
                environmentDAO.delete(temp);
            }

            environmentDAO.createOrUpdate(environment);
        }

        return environments;
    }

    /**
     * Update the user location on the server for all the environments and return the actions for the
     * narrowest environment.
     *
     * @param location
     * @param geofences
     */
    public List<Action> updateLocation(Location location, List<Geofence> geofences, boolean exiting) {
        Map<Integer, Environment> environments = loadEnvironments(geofences);
        Environment narrowestEnvironment = findNarrowestEnvironment(environments);

        List<Action> actions = null;

        for (Environment e : environments.values()) {
            Log log = new Log();
            log.setDeviceCode(deviceManager.getDevice().getCode());
            log.setEnvironmentId(e.getExtId());
            log.setExiting(exiting);

            List<Action> tmpActions = api.updateUserLocation(log);

            if (e.getExtId() == narrowestEnvironment.getExtId()) {
                actions = tmpActions;
            }
        }

        return actions;
    }

    /**
     * Find the narrowest environment in the list of environments based on its level attribute.
     *
     * @param environments
     * @return
     */
    private Environment findNarrowestEnvironment(Map<Integer, Environment> environments) {
        Environment narrowestEnvironment = null;

        for (Environment e : environments.values()) {
            if (narrowestEnvironment == null || e.getLevel() > narrowestEnvironment.getLevel()) {
                narrowestEnvironment = e;
            }
        }

        return narrowestEnvironment;
    }

    /**
     * Load the environments from  the geofences.
     *
     * @param geofences
     * @return
     */
    private Map<Integer, Environment> loadEnvironments(List<Geofence> geofences) {
        Map<Integer, Environment> environments = new HashMap<>(geofences.size());

        for (Geofence geofence : geofences) {
            Environment environment = environmentDAO.find(Long.valueOf(geofence.getRequestId()));
            environments.put(environment.getExtId(), environment);
        }

        return environments;
    }
}
