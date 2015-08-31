package com.gppdi.ubipri.data;

import com.google.common.collect.ImmutableMap;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.internal.bind.DateTypeAdapter;
import com.google.gson.reflect.TypeToken;
import com.gppdi.ubipri.api.json.EnvironmentDeserializer;
import com.gppdi.ubipri.data.models.Action;
import com.gppdi.ubipri.data.models.Environment;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author mayconbordin
 */
public class Fixtures {
    public static final Map<Integer, Environment> ENVIRONMENTS = ImmutableMap.of(
            1, new Environment(1, "Porto Alegre", -30.072296142578118, -51.17763595581054),
            2, new Environment(2, "Campus Vale UFRGS", -30.071927547454848, -51.12007999420165),
            3, new Environment(3, "Prédio Informática 72", -30.06849765777585, -51.12047672271728),
            4, new Environment(4, "Apartamento do Borges", -30.039857387542725, -51.20896339416505),
            5, new Environment(5, "Laboratório 205", -30.0686149597168, -51.1206169128418)
    );

    public static final String ENVIRONMENTS_JSON = "[{\"id\":1,\"name\":\"Porto Alegre\",\"location\":{\"type\":\"Point\",\"coordinates\":[-30.072296142578118,-51.17763595581054,10.0]},\"shape\":{\"type\":\"Polygon\",\"coordinates\":[[[-51.198184967041,-29.9612808227539,0.0],[-51.2952117919922,-30.1073989868164,0.0],[-51.216136932373,-30.2264022827148,0.0],[-51.0650444030762,-30.0949935913086,0.0],[-51.1136016845703,-29.9714050292969,0.0],[-51.198184967041,-29.9612808227539,0.0]]]},\"operatingRange\":17550.786,\"version\":1,\"localizationType\":{\"id\":1,\"name\":\"GPS\",\"precision\":600.0,\"metric\":\"m2\"},\"environmentType\":{\"id\":3,\"name\":\"Public\"},\"parentId\":null,\"level\":0,\"customActions\":[],\"distance\":0.0},{\"id\":2,\"name\":\"Campus Vale UFRGS\",\"location\":{\"type\":\"Point\",\"coordinates\":[-30.071927547454848,-51.12007999420165,0.0]},\"shape\":{\"type\":\"Polygon\",\"coordinates\":[[[-51.1212463378906,-30.0655059814453,0.0],[-51.1259727478027,-30.076099395752,0.0],[-51.1150588989258,-30.0787830352783,0.0],[-51.1180419921875,-30.0673217773438,0.0],[-51.1212463378906,-30.0655059814453,0.0]]]},\"operatingRange\":903.524,\"version\":1,\"localizationType\":{\"id\":1,\"name\":\"GPS\",\"precision\":600.0,\"metric\":\"m2\"},\"environmentType\":{\"id\":3,\"name\":\"Public\"},\"parentId\":1,\"level\":1,\"customActions\":[],\"distance\":0.0},{\"id\":3,\"name\":\"Prédio Informática 72\",\"location\":{\"type\":\"Point\",\"coordinates\":[-30.06849765777585,-51.12047672271728,0.0]},\"shape\":{\"type\":\"Polygon\",\"coordinates\":[[[-51.1204299926758,-30.0682048797607,0.0],[-51.1207160949707,-30.0687103271484,0.0],[-51.1205253601074,-30.068790435791,0.0],[-51.1202354431152,-30.0682849884033,0.0],[-51.1204299926758,-30.0682048797607,0.0]]]},\"operatingRange\":33.178,\"version\":1,\"localizationType\":{\"id\":2,\"name\":\"RFID\",\"precision\":3.0,\"metric\":\"m2\"},\"environmentType\":{\"id\":2,\"name\":\"Private\"},\"parentId\":2,\"level\":2,\"customActions\":[],\"distance\":0.0},{\"id\":4,\"name\":\"Apartamento do Borges\",\"location\":{\"type\":\"Point\",\"coordinates\":[-30.039857387542725,-51.20896339416505,0.0]},\"shape\":{\"type\":\"Polygon\",\"coordinates\":[[[-51.2090530395508,-30.0400276184082,0.0],[-51.2088470458984,-30.0399284362793,0.0],[-51.2088775634766,-30.039737701416,0.0],[-51.2090759277344,-30.0397357940674,0.0],[-51.2090530395508,-30.0400276184082,0.0]]]},\"operatingRange\":20.826,\"version\":2,\"localizationType\":{\"id\":2,\"name\":\"RFID\",\"precision\":3.0,\"metric\":\"m2\"},\"environmentType\":{\"id\":2,\"name\":\"Private\"},\"parentId\":1,\"level\":1,\"customActions\":[],\"distance\":0.0},{\"id\":5,\"name\":\"Laboratório 205\",\"location\":{\"type\":\"Point\",\"coordinates\":[-30.0686149597168,-51.1206169128418,0.0]},\"shape\":{\"type\":\"Polygon\",\"coordinates\":[[[-51.1206091940403,-30.068530136730356,0.0],[-51.12065076828003,-30.06859745311464,0.0],[-51.12055420875549,-30.06863459316914,0.0],[-51.120511293411255,-30.068573079946315,0.0],[-51.1206091940403,-30.068530136730356,0.0]]]},\"operatingRange\":5.0,\"version\":2,\"localizationType\":{\"id\":2,\"name\":\"RFID\",\"precision\":3.0,\"metric\":\"m2\"},\"environmentType\":{\"id\":2,\"name\":\"Private\"},\"parentId\":3,\"level\":3,\"customActions\":[],\"distance\":0.0}]";
    public static final String ACTIONS_JSON = "[{\"id\":57,\"accessLevel\":{\"id\":8,\"impactFactor\":0.0,\"accessType\":{\"id\":5,\"name\":\"Administrative\"},\"environmentType\":{\"id\":3,\"name\":\"Public\"}},\"functionality\":{\"id\":5,\"name\":\"Wi-Fi\"},\"environment\":null,\"action\":\"on\",\"startDate\":null,\"endDate\":null,\"startDailyInterval\":null,\"durationInterval\":null,\"args\":[]}]";

    private static Gson gson;

    public static Gson getGson() {
        if (gson == null) {
            gson = new GsonBuilder()
                    .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                    .registerTypeAdapter(Date.class, new DateTypeAdapter())
                    .registerTypeAdapter(Environment.class, new EnvironmentDeserializer())
                    .enableComplexMapKeySerialization()
                    .serializeNulls()
                    .create();
        }

        return gson;
    }

    public static List<Environment> getEnvironments() {
        Type listType = new TypeToken<ArrayList<Environment>>() {}.getType();
        return getGson().fromJson(ENVIRONMENTS_JSON, listType);
    }

    public static List<Action> getActions() {
        Type listType = new TypeToken<ArrayList<Action>>() {}.getType();
        return getGson().fromJson(ACTIONS_JSON, listType);
    }

}
