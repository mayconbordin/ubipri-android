package com.gppdi.ubipri;

import de.greenrobot.daogenerator.DaoGenerator;
import de.greenrobot.daogenerator.Entity;
import de.greenrobot.daogenerator.Property;
import de.greenrobot.daogenerator.Schema;

/**
 *
 * @author mayconbordin
 */
public class UbiPriDaoGenerator {
    private static final int VERSION = 1;
    private static final String PROJECT_DIR = System.getProperty("user.dir").replace("\\", "/");
    private static final String OUT_DIR = PROJECT_DIR + "/../app/src/main/java/";
    
    public static void main(String[] args) throws Exception {
        Schema schema = new Schema(VERSION, "com.gppdi.ubipri.data.dao");
        
        addTables(schema);
        
        new DaoGenerator().generateAll(schema, OUT_DIR);
    }
    
    private static void addTables(Schema schema) {
        Entity environmentType = addEnvironment(schema);
        Entity localizationType = addLocalizationType(schema);
        Entity environment = addEnvironment(schema);
        
        Property environmentTypeProperty = environment.addLongProperty("environmentTypeId").notNull().getProperty();
        Property localizationTypeProperty = environment.addLongProperty("localizationTypeId").notNull().getProperty();
        Property parentProperty = environment.addLongProperty("parentId").getProperty();
        
        environment.addToOne(environmentType, environmentTypeProperty);
        environment.addToOne(localizationType, localizationTypeProperty);
        environment.addToOne(environment, parentProperty, "parent");
    }
    
    private static Entity addEnvironmentType(Schema schema) {
        Entity environmentType = schema.addEntity("EnvironmentType");
        environmentType.addIdProperty();
        environmentType.addStringProperty("name").notNull();
        return environmentType;
    }
    
    private static Entity addLocalizationType(Schema schema) {
        Entity localizationType = schema.addEntity("LocalizationType");
        localizationType.addIdProperty();
        localizationType.addStringProperty("name").notNull();
        localizationType.addDoubleProperty("precision").notNull();
        localizationType.addStringProperty("metric").notNull();
        return localizationType;
    }
    
    private static Entity addEnvironment(Schema schema) {
        Entity environment = schema.addEntity("Environment");
        environment.addIdProperty();
        environment.addStringProperty("name").notNull();
        environment.addDoubleProperty("latitude").notNull().index();
        environment.addDoubleProperty("longitude").notNull().index();
        environment.addDoubleProperty("operatingRange").notNull();
        environment.addIntProperty("version").notNull();
        environment.addDoubleProperty("distance");
        environment.addStringProperty("shape").notNull();
        return environment;
    }
}
