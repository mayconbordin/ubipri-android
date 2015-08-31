package com.gppdi.ubipri.data.dao;

import com.activeandroid.query.Select;
import com.gppdi.ubipri.data.models.Environment;
import com.gppdi.ubipri.data.models.EnvironmentType;

/**
 * @author mayconbordin
 */
public class EnvironmentTypeDAO extends AbstractDAO<EnvironmentType> {

    public EnvironmentTypeDAO() {
        super(EnvironmentType.class);
    }
}
