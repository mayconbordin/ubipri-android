package com.gppdi.ubipri.data;

import android.content.SharedPreferences;
import android.location.Location;
import android.util.Log;

import com.google.android.gms.location.Geofence;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.gppdi.ubipri.api.ApiService;
import com.gppdi.ubipri.data.dao.EnvironmentDAO;
import com.gppdi.ubipri.data.models.Action;
import com.gppdi.ubipri.data.models.Device;
import com.gppdi.ubipri.data.models.Environment;
import com.gppdi.ubipri.location.LocationConstants;
import com.gppdi.ubipri.utils.GeoUtils;
import com.spatial4j.core.shape.Point;
import com.spatial4j.core.shape.SpatialRelation;
import com.spatial4j.core.shape.jts.JtsGeometry;

import java.io.IOException;
import java.lang.reflect.Type;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import retrofit.RetrofitError;

/**
 * @author mayconbordin
 */
public class DataService {
    private static final String TAG = "DataService";
    private static final String CURRENT_ENVIRONMENT = "current_environment";
    private static final String CURRENT_ACTIONS     = "current_actions";

    private static final Type actionsListType = new TypeToken<ArrayList<Action>>(){}.getType();

    private ApiService api;
    private EnvironmentDAO environmentDAO;
    private DeviceManager deviceManager;
    private SharedPreferences sharedPreferences;
    private Gson gson;

    private Environment currentEnvironment;
    private List<Action> currentActions;

    /**
     * Create a data service.
     *
     * @param api
     * @param environmentDAO
     * @param deviceManager
     * @param sharedPreferences
     * @param gson
     */
    public DataService(ApiService api, EnvironmentDAO environmentDAO, DeviceManager deviceManager,
                       SharedPreferences sharedPreferences, Gson gson) {
        this.api = api;
        this.environmentDAO = environmentDAO;
        this.deviceManager = deviceManager;
        this.sharedPreferences = sharedPreferences;
        this.gson = gson;
    }

    /**
     * Get list of environments from the server, save them locally (overwriting if necessary).
     *
     * @param center
     * @param radius
     * @return
     */
    public List<Environment> getEnvironments(Location center, double radius) throws RetrofitError {
        List<Environment> environments = api.getEnvironments(center.getLatitude(), center.getLongitude(), radius);

        Log.i(TAG, "Found " + environments.size() + " environments within a radius of " + radius + "m");

        for (Environment environment : environments) {
            Environment temp = environmentDAO.findByExtId(environment.getExtId());

            // if environment already exists
            if (temp != null) {
                // replace if has a new version
                if (environment.getVersion() > temp.getVersion()) {
                    environmentDAO.delete(temp);
                    environmentDAO.createOrUpdate(environment);
                }
            } else {
                environmentDAO.createOrUpdate(environment);
            }
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
    public List<Action> updateLocation(Location location, List<Geofence> geofences, boolean exiting) throws RetrofitError {
        if (geofences.isEmpty()) {
            Log.w(TAG, "Received an empty list of geofences.");
            return null;
        }

        Device device = deviceManager.getDevice();
        Map<Integer, Environment> environments = loadEnvironments(geofences);
        currentEnvironment = findCurrentEnvironment(location, environments, exiting);

        Log.i(TAG, "Current environment: " + currentEnvironment);

        for (Environment e : environments.values()) {
            // will only check this if the shape check is enabled and is entering the environment
            if (LocationConstants.ENVIRONMENT_SHAPE_CHECK && !exiting && !isUserWithin(location, e)) {
                Log.i(TAG, location + " not within " + e);
                continue;
            }

            List<Action> actions = sendLog(e, device, exiting);

            // will only apply the actions for the narrowest environment
            // it only applies for when user is entering the environment
            if (!exiting && e.getExtId() == currentEnvironment.getExtId()) {
                currentActions = actions;
            }
        }

        // if entering, just save current environment and its actions
        if (!exiting) {
            saveCurrentEnvironment(currentEnvironment, currentActions);
        }
        // if exiting and has a current environment (not within the list of geofences)
        else if (currentEnvironment != null) {
            currentActions = sendLog(currentEnvironment, device, false);
            saveCurrentEnvironment(currentEnvironment, currentActions);
        }
        // in this case the user is not inside any registered environment
        else {
            clearCurrentEnvironment();
        }

        return currentActions;
    }

    /**
     * Get the current environment.
     * @return
     */
    public Environment getCurrentEnvironment() {
        if (currentEnvironment == null) {
            Integer id = sharedPreferences.getInt(CURRENT_ENVIRONMENT, -1);

            if (id == -1) {
                return null;
            }

            currentEnvironment = environmentDAO.findByExtId(id);
        }

        return currentEnvironment;
    }

    /**
     * Get the set of actions of the current environment.
     * @return
     */
    public List<Action> getCurrentActions() {
        if (currentActions == null) {
            String json = sharedPreferences.getString(CURRENT_ACTIONS, "");

            if (json.length() == 0) {
                return null;
            }

            currentActions = gson.fromJson(json, actionsListType);
        }

        return currentActions;
    }

    /**
     * Send log for check-in/out of environment.
     *
     * @param environment
     * @param device
     * @param exiting
     * @return
     */
    private List<Action> sendLog(Environment environment, Device device, boolean exiting) {
        Log.i(TAG, "Check-" + (exiting ? "out" : "in") + " " + environment);

        try {
            com.gppdi.ubipri.data.models.Log log = new com.gppdi.ubipri.data.models.Log();
            log.setDeviceCode(device.getCode());
            log.setEnvironmentId(environment.getExtId());
            log.setExiting(exiting);

            return api.updateUserLocation(log);
        } catch (RetrofitError e) {
            Log.e(TAG, "Unable to check-in/out of environment(s).", e);
        }

        return null;
    }

    /**
     * Save the current environment and list of actions for future retrieval.
     *
     * @param environment
     * @param actions
     */
    private void saveCurrentEnvironment(Environment environment, List<Action> actions) {
        String json = gson.toJson(actions, actionsListType);

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(CURRENT_ENVIRONMENT, environment.getExtId());
        editor.putString(CURRENT_ACTIONS, json);
        editor.apply();
    }

    /**
     * Clear the current environment variables.
     */
    private void clearCurrentEnvironment() {
        currentEnvironment = null;
        currentActions = null;

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(CURRENT_ENVIRONMENT, -1);
        editor.putString(CURRENT_ACTIONS, "");
        editor.apply();
    }

    /**
     * If the shape check is enabled, transforms the location into a
     * point and the environment shape into a polygon, then check if the point is within the polygon
     * in order to certify that the user entered the location. Otherwise, uses the environment location
     * and operating range to check if the user location is within the environment radius.
     *
     * @param location
     * @param environment
     * @return
     */
    private boolean isUserWithin(Location location, Environment environment) {
        try {
            Point point = GeoUtils.getSpatialContext().makePoint(location.getLongitude(), location.getLatitude());

            if (LocationConstants.ENVIRONMENT_SHAPE_CHECK) {
                JtsGeometry shape = (JtsGeometry) GeoUtils.getSpatialContext().getFormats().getWktReader().read(environment.getShape());
                return (point.relate(shape) == SpatialRelation.WITHIN);
            } else {
                Point envPoint = GeoUtils.getSpatialContext().makePoint(environment.getLongitude(), environment.getLatitude());
                double distance = GeoUtils.DistanceUnit.DEGREES.toKm(GeoUtils.distanceHarvesine(envPoint, point)) * 1000;
                return (distance <= environment.getOperatingRange());
            }
        } catch (IOException | ParseException e) {
            Log.e(TAG, "Unable to read shape.", e);
        }

        return true;
    }

    /**
     * Find the current environment the user is in.
     *
     * @param location
     * @param environments
     * @param exiting
     * @return
     */
    private Environment findCurrentEnvironment(Location location, Map<Integer, Environment> environments, boolean exiting) {
        Environment currentEnvironment = findNarrowestEnvironment(environments);

        // If is entering an environment, the narrowest one is the current
        // Also, if there is no parent environment, there is nothing to search
        if (!exiting || currentEnvironment == null || currentEnvironment.getParentId() == null) {
            return currentEnvironment;
        }

        Environment temp = currentEnvironment;

        // find parent environment and check if user is within it
        while (temp != null || temp.getParentId() != null) {
            temp = environmentDAO.findByExtId(temp.getParentId());

            if (temp != null && isUserWithin(location, temp)) {
                break;
            }
        }

        return temp;
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
            Environment environment = environmentDAO.findByExtId(Integer.valueOf(geofence.getRequestId()));
            environments.put(environment.getExtId(), environment);
        }

        return environments;
    }
}
