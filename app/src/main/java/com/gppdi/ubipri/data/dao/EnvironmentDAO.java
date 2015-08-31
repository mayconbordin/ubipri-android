package com.gppdi.ubipri.data.dao;

import com.activeandroid.query.From;
import com.activeandroid.query.Select;
import com.gppdi.ubipri.data.models.Environment;
import com.gppdi.ubipri.utils.GeoUtils;
import com.spatial4j.core.shape.Point;

import java.util.List;

/**
 * @author mayconbordin
 */
public class EnvironmentDAO extends AbstractDAO<Environment> {
    public EnvironmentDAO() {
        super(Environment.class);
    }

    /**
     * Find all points within the circle.
     *
     * @param lat
     * @param lon
     * @param radius
     * @return
     */
    public List<Environment> findNear(double lat, double lon, double radius) {
        return findNear(lat, lon, radius, null);
    }

    /**
     * Find all points within the circle.
     *
     * @param lat
     * @param lon
     * @param radius
     * @param limit
     * @return
     */
    public List<Environment> findNear(double lat, double lon, double radius, Integer limit) {
        Point center = GeoUtils.getSpatialContext().makePoint(lon, lat);
        GeoUtils.CircleBBox bbox = GeoUtils.calculateCircleBBox(center, radius);

        From query = new Select().from(Environment.class)
                .where("Latitude > ?", bbox.bottom.getY())
                .and("Latitude < ?", bbox.top.getY())
                .and("Longitude < ?", bbox.right.getX())
                .and("Longitude > ?", bbox.left.getX());

        if (limit != null) {
            query.limit(limit);
        }

        return query.execute();
    }
}
