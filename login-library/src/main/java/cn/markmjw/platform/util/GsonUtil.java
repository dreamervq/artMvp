package cn.markmjw.platform.util;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;

import java.io.Reader;
import java.lang.reflect.Type;
import java.util.Date;

/**
 * GsonUtils
 *
 * @author markmjw
 * @date 2015-08-16
 */
public final class GsonUtil {
    private static Gson gson;

    public GsonUtil() {
    }

    public static <T> T fromJson(String json, Type type) {
        return gson.fromJson(json, type);
    }

    public static <T> T fromJson(String json, Class<T> clazz) {
        return gson.fromJson(json, clazz);
    }

    public static <T> T fromJson(Reader json, Class<T> clazz) {
        return gson.fromJson(json, clazz);
    }


    public static <T> T fromJson(byte[] bytes, Class<T> clazz) {
        return gson.fromJson(new String(bytes), clazz);
    }

    public static String toJson(Object src) {
        return gson.toJson(src);
    }

    static {
        GsonBuilder builder = new GsonBuilder();
        builder.setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES);
        builder.registerTypeAdapter(Date.class, new JsonDeserializer() {
            public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext
                    context) {
                return json == null ? null : new Date(json.getAsLong() * 1000L);
            }
        });
        gson = builder.create();
    }
}