package com.gppdi.ubipri.utils;

import com.spatial4j.core.context.SpatialContext;
import com.spatial4j.core.context.jts.JtsSpatialContext;
import com.spatial4j.core.distance.CartesianDistCalc;
import com.spatial4j.core.distance.DistanceCalculator;
import static com.spatial4j.core.distance.DistanceUtils.*;
import com.spatial4j.core.distance.GeodesicSphereDistCalc;
import com.spatial4j.core.shape.Point;

/**
 *
 * @author mayconbordin
 */
public class GeoUtils {
    public static final int EARTH_RADIUS_M = (int) (EARTH_MEAN_RADIUS_KM * 1000);
            
    private static SpatialContext spatialContext;

    /**
     * Calculates the end-point from a given source at a given range (meters)
     * and bearing (degrees). This methods uses simple geometry equations to
     * calculate the end-point.
     * 
     * @param point Point of origin
     * @param range Range in meters
     * @param bearing Bearing in degrees
     * @return End-point from the source given the desired range and bearing.
     */
    public static Point calculateDerivedPosition(Point point, double range, double bearing) {
        double latA = Math.toRadians(point.getY());
        double lonA = Math.toRadians(point.getX());
        double angularDistance = range / EARTH_RADIUS_M;
        double trueCourse = Math.toRadians(bearing);

        double lat = Math.asin(
                Math.sin(latA) * Math.cos(angularDistance) +
                        Math.cos(latA) * Math.sin(angularDistance)
                        * Math.cos(trueCourse));

        double dlon = Math.atan2(
                Math.sin(trueCourse) * Math.sin(angularDistance)
                        * Math.cos(latA),
                Math.cos(angularDistance) - Math.sin(latA) * Math.sin(lat));

        double lon = ((lonA + dlon + Math.PI) % (Math.PI * 2)) - Math.PI;

        lat = Math.toDegrees(lat);
        lon = Math.toDegrees(lon);

        Point newPoint = spatialContext.makePoint((double) lon, (double) lat);

        return newPoint;
    }
    
    /**
     * Calculate four point thresholds around a central point with a given radius.
     * 
     * strWhere =  " WHERE "
     *   + COL_X + " > " + String.valueOf(p3.x) + " AND "
     *   + COL_X + " < " + String.valueOf(p1.x) + " AND "
     *   + COL_Y + " < " + String.valueOf(p2.y) + " AND "
     *   + COL_Y + " > " + String.valueOf(p4.y)
     * 
     * @param center
     * @param radius
     * @return 
     */
    public static CircleBBox calculateCircleBBox(Point center, double radius) {
        final double mult = 1; // mult = 1.1; is more reliable
        Point top    = calculateDerivedPosition(center, mult * radius, 0);
        Point right  = calculateDerivedPosition(center, mult * radius, 90);
        Point bottom = calculateDerivedPosition(center, mult * radius, 180);
        Point left   = calculateDerivedPosition(center, mult * radius, 270);
        
        return new CircleBBox(center, top, right, bottom, left);
    }

    public static double distance(Point from, Point to, DistanceCalculator distCalc) {
        return distCalc.distance(from, to);
    }
    
    public static double distanceHarvesine(Point from, Point to) {
        return distance(from, to, new GeodesicSphereDistCalc.Haversine());
    }
    
    public static double distanceVincenty(Point from, Point to) {
        return distance(from, to, new GeodesicSphereDistCalc.Vincenty());
    }
    
    public static double distanceLawOfCosines(Point from, Point to) {
        return distance(from, to, new GeodesicSphereDistCalc.LawOfCosines());
    }
    
    public static double distanceCartesian(Point from, Point to) {
        return distance(from, to, new CartesianDistCalc());
    }

    public static SpatialContext getSpatialContext() {
        if (spatialContext == null) {
            spatialContext = JtsSpatialContext.GEO;
        }
        return spatialContext;
    }
    
    public static void setSpatialContext(SpatialContext spatialContext) {
        GeoUtils.spatialContext = spatialContext;
    }
    
    public static class CircleBBox {
        public Point center;
        public Point top;
        public Point right;
        public Point bottom;
        public Point left;

        public CircleBBox(Point center, Point top, Point right, Point bottom, Point left) {
            this.center = center;
            this.top = top;
            this.right = right;
            this.bottom = bottom;
            this.left = left;
        }

        @Override
        public String toString() {
            return "CircleBBox{" +
                    "center=" + center +
                    ", top=" + top +
                    ", right=" + right +
                    ", bottom=" + bottom +
                    ", left=" + left +
                    '}';
        }
    }
    
    public static enum DistanceUnit {
        DEGREES {
            public double toDegrees(double d) { return d; }
            public double toRadians(double d) { return d * DEGREES_TO_RADIANS; }
            public double toKm(double d) { return d * DEG_TO_KM; }
            public double toMiles(double d) { return d * DEG_TO_MILES; }
        },
        RADIANS {
            public double toDegrees(double d) { return d * RADIANS_TO_DEGREES; }
            public double toRadians(double d) { return d; }
            public double toKm(double d) { return d * RAD_TO_KM; }
            public double toMiles(double d) { return d * RAD_TO_MILES; }
        },
        KM {
            public double toDegrees(double d) { return d * KM_TO_DEG; }
            public double toRadians(double d) { return d * KM_TO_RAD; }
            public double toKm(double d) { return d; }
            public double toMiles(double d) { return d * KM_TO_MILES; }
        },
        MILES {
            public double toDegrees(double d) { return d * MILES_TO_DEG; }
            public double toRadians(double d) { return d * MILES_TO_RAD; }
            public double toKm(double d) { return d * MILES_TO_KM; }
            public double toMiles(double d) { return d; }
        };
        
        static final double DEG_TO_MILES = DEG_TO_KM * KM_TO_MILES;
        static final double RAD_TO_KM    = RADIANS_TO_DEGREES * DEG_TO_KM;
        static final double RAD_TO_MILES = RADIANS_TO_DEGREES * DEG_TO_KM * KM_TO_MILES;
        static final double KM_TO_RAD    = KM_TO_DEG * DEGREES_TO_RADIANS;
        static final double MILES_TO_DEG = MILES_TO_KM * KM_TO_DEG;
        static final double MILES_TO_RAD = MILES_TO_KM * KM_TO_RAD;
        
        public double toDegrees(double distance) {
            throw new AssertionError();
        }
        
        public double toRadians(double distance) {
            throw new AssertionError();
        }
        
        public double toKm(double distance) {
            throw new AssertionError();
        }
        
        public double toMiles(double distance) {
            throw new AssertionError();
        }
    }
}
