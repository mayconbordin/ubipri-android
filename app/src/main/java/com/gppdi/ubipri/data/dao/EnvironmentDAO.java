package com.gppdi.ubipri.data.dao;

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
        Point center = GeoUtils.getSpatialContext().makePoint(lon, lat);
        GeoUtils.CircleBBox bbox = GeoUtils.calculateCircleBBox(center, radius);

        return new Select().from(Environment.class)
                .where("Latitude > ?", bbox.bottom.getY())
                .and("Latitude < ?", bbox.top.getY())
                .and("Longitude < ?", bbox.right.getX())
                .and("Longitude > ?", bbox.left.getX())
                .execute();
    }
}
