package com.gppdi.ubipri.api.json;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.gppdi.ubipri.data.models.Environment;
import com.gppdi.ubipri.data.models.EnvironmentType;
import com.gppdi.ubipri.data.models.LocalizationType;
import com.gppdi.ubipri.utils.GeoUtils;
import com.spatial4j.core.io.ShapeReader;
import com.spatial4j.core.shape.Shape;

import java.io.IOException;
import java.lang.reflect.Type;
import java.text.ParseException;

/**
 * @author mayconbordin
 */
public class EnvironmentDeserializer implements JsonDeserializer<Environment> {
    @Override
    public Environment deserialize(JsonElement json, Type type, JsonDeserializationContext context) throws JsonParseException {
        if (json == null) {
            return null;
        }

        JsonObject o = (JsonObject) json;

        Environment e = new Environment();
        e.setExtId(o.get("id").getAsInt());
        e.setName(o.get("name").getAsString());
        e.setOperatingRange(o.get("operatingRange").getAsDouble());
        e.setVersion(o.get("version").getAsInt());
        e.setDistance(o.get("distance").getAsDouble());
        e.setEnvironmentType((EnvironmentType) context.deserialize(o.get("environmentType"), EnvironmentType.class));
        e.setLocalizationType((LocalizationType) context.deserialize(o.get("localizationType"), LocalizationType.class));

        JsonElement level = o.get("level");
        e.setLevel((!level.isJsonNull()) ? level.getAsInt() : null);

        JsonElement parentId = o.get("parentId");
        e.setParentId((!parentId.isJsonNull()) ? parentId.getAsInt() : null);

        parseLocation(o, e);
        parseShape(o, e);

        return e;
    }

    private void parseLocation(JsonObject o, Environment e) throws JsonParseException {
        if (o == null || !o.has("location") || o.get("location").isJsonNull()) return;

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

    private void parseShape(JsonObject o, Environment e) throws JsonParseException {
        if (o == null || !o.has("shape") || o.get("shape").isJsonNull()) return;

        if (!o.getAsJsonObject("shape").get("type").getAsString().equals("Polygon")) {
            throw new JsonParseException("Shape type of Environment has to be a Polygon.");
        }

        String shapeStr = o.get("shape").toString();
        ShapeReader reader = GeoUtils.getSpatialContext().getFormats().getGeoJsonReader();

        try {
            Shape shape = reader.read(shapeStr);
            e.setShape(shape.toString());
        } catch (IOException e1) {
            throw new JsonParseException("Unable to read Environment shape.");
        } catch (ParseException e1) {
            throw new JsonParseException("Unable to parse Environment shape.");
        }
    }
}
