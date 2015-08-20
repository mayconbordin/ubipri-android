package com.gppdi.ubipri.api.json;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.gppdi.ubipri.api.models.Environment;
import com.gppdi.ubipri.api.models.EnvironmentType;
import com.gppdi.ubipri.api.models.LocalizationType;

import java.lang.reflect.Type;

/**
 * @author mayconbordin
 */
public class EnvironmentDeserializer implements JsonDeserializer<Environment> {
    @Override
    public Environment deserialize(JsonElement json, Type type, JsonDeserializationContext context) throws JsonParseException {
        JsonObject o = (JsonObject) json;

        Environment e = new Environment();
        e.setId(o.get("id").getAsInt());
        e.setName(o.get("name").getAsString());
        e.setOperatingRange(o.get("operating_range").getAsDouble());
        e.setVersion(o.get("version").getAsInt());
        e.setDistance(o.get("distance").getAsDouble());

        e.setParent((Environment) context.deserialize(o.get("parent"), Environment.class));
        e.setEnvironmentType((EnvironmentType) context.deserialize(o.get("environment_type"), EnvironmentType.class));
        e.setLocalizationType((LocalizationType) context.deserialize(o.get("localization_type"), LocalizationType.class));

        parseLocation(o, e);

        return null;
    }

    private void parseLocation(JsonObject o, Environment e) throws JsonParseException {
        JsonObject location = o.getAsJsonObject("location");

        if (!location.get("type").getAsString().equals("Point")) {
            throw new JsonParseException("Location type of Environment has to be a Point.");
        }

        JsonArray coord = location.getAsJsonArray("coordinates");

        if (coord.size() < 2) {
            throw new JsonParseException("Point must have at least two coordinates.");
        }

        e.setLatitude(coord.get(0).getAsDouble());
        e.setLongitude(coord.get(1).getAsDouble());
    }
}
