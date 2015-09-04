package com.gppdi.ubipri.location;

/**
 * @author mayconbordin
 */
public interface LocationConstants {
    // Request code to attempt to resolve Google Play services connection failures.
    int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;

    int LOCATION_REQUEST_INTERVAL         = 15 * 1000;
    int LOCATION_REQUEST_FASTEST_INTERVAL = 15 * 1000;

    // Whether the shape of the environment should be checked
    boolean ENVIRONMENT_SHAPE_CHECK = false;

    // ID of the master geofence
    String MASTER_GEOFENCE_ID = "MasterGeofence";

    // Radius in meters for the retrieval of environments
    double RADIUS_M = 2000; // 2km

    // Margin (in meters) appended to the radius of the geofence
    double GEOFENCE_RADIUS_MARGIN = 150; // append to the geofence radius
}
