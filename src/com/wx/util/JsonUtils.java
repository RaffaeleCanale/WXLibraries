package com.wx.util;

import com.wx.util.representables.string.EncodedBytesRepr;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * @author Raffaele Canale (<a href="mailto:raffaelecanale@gmail.com?subject=JSync">raffaelecanale@gmail.com</a>)
 * @version 0.1 - created on 12.05.17.
 */
public class JsonUtils {

    //<editor-fold desc="getters">
    public static JSONObject getObject(JSONObject object, String... path) {
        return getObjectOpt(object, path).orElseThrow(() -> new JSONException("Missing value for: " + Arrays.toString(path)));
    }

    public static String getString(JSONObject object, String... path) {
        return getStringOpt(object, path).orElseThrow(() -> new JSONException("Missing value for: " + Arrays.toString(path)));
    }

    public static float getFloat(JSONObject object, String... path) {
        return getFloatOpt(object, path).orElseThrow(() -> new JSONException("Missing value for: " + Arrays.toString(path)));
    }

    public static int getInt(JSONObject object, String... path) {
        return getIntOpt(object, path).orElseThrow(() -> new JSONException("Missing value for: " + Arrays.toString(path)));
    }

    public static long getLong(JSONObject object, String... path) {
        return getLongOpt(object, path).orElseThrow(() -> new JSONException("Missing value for: " + Arrays.toString(path)));
    }

    public static double getDouble(JSONObject object, String... path) {
        return getDoubleOpt(object, path).orElseThrow(() -> new JSONException("Missing value for: " + Arrays.toString(path)));
    }

    public static byte[] getBytes(JSONObject object, String... path) {
        return getBytesOpt(object, path).orElseThrow(() -> new JSONException("Missing value for: " + Arrays.toString(path)));
    }

    public static boolean getBoolean(JSONObject object, String... path) {
        return getBooleanOpt(object, path).orElseThrow(() -> new JSONException("Missing value for: " + Arrays.toString(path)));
    }

    public static List<String> getStringList(JSONObject object, String... path) {
        return getStringListOpt(object, path).orElseThrow(() -> new JSONException("Missing value for: " + Arrays.toString(path)));
    }

    public static JSONArray getArray(JSONObject object, String... path) {
        return getArrayOpt(object, path).orElseThrow(() -> new JSONException("Missing value for: " + Arrays.toString(path)));
    }
    //</editor-fold>

    //<editor-fold desc="opt getters">
    public static Optional<JSONObject> getObjectOpt(JSONObject object, String... path) {
        if (path.length == 0) {
            return Optional.of(object);
        }
        return get(object, JSONObject::getJSONObject, path);
    }

    public static Optional<String> getStringOpt(JSONObject object, String... path) {
        return get(object, JSONObject::getString, path);
    }

    public static Optional<Float> getFloatOpt(JSONObject object, String... path) {
        return get(object, JSONObject::getFloat, path);
    }

    public static Optional<Integer> getIntOpt(JSONObject object, String... path) {
        return get(object, JSONObject::getInt, path);
    }

    public static Optional<Long> getLongOpt(JSONObject object, String... path) {
        return get(object, JSONObject::getLong, path);
    }

    public static Optional<Double> getDoubleOpt(JSONObject object, String... path) {
        return get(object, JSONObject::getDouble, path);
    }

    public static Optional<byte[]> getBytesOpt(JSONObject object, String... path) {
        return get(object, (obj, key) -> new EncodedBytesRepr().castOut(obj.getString(key)), path);
    }

    public static Optional<Boolean> getBooleanOpt(JSONObject object, String... path) {
        return get(object, JSONObject::getBoolean, path);
    }

    public static Optional<List<String>> getStringListOpt(JSONObject object, String... path) {
        return getArrayOpt(object, path).map(array -> populate(array::getString, array.length()));
    }

    public static Optional<JSONArray> getArrayOpt(JSONObject object, String... path) {
        return get(object, JSONObject::getJSONArray, path);
    }
    //</editor-fold>

    //<editor-fold desc="setters">
    public static void set(JSONObject object, Object value, String... path) {
        String field = field(path);
        JSONObject parent = getOrCreateParent(object, path);

        parent.put(field, value);
    }

    public static void set(JSONObject object, Collection<?> value, String... path) {
        set(object, new JSONArray(value), path);
    }

    public static void set(JSONObject object, byte[] value, String... path) {
        set(object, new EncodedBytesRepr().castIn(value), path);
    }
    //</editor-fold>

    public static void remove(JSONObject object, String... path) {
        getParent(object, path).ifPresent(parent -> parent.remove(field(path)));
    }

    private static <E> List<E> populate(Function<Integer, E> getValue, int length) {
        List<E> result = new ArrayList<>(length);

        for (int i = 0; i < length; i++) {
            result.add(getValue.apply(i));
        }

        return result;
    }

    private static <E> Optional<E> get(JSONObject object, BiFunction<JSONObject, String, E> getter, String... path) {
        return getParent(object, path).flatMap((parent) -> {
            String field = field(path);

            if (parent.has(field)) {
                return Optional.of(getter.apply(parent, field));
            } else {
                return Optional.empty();
            }
        });
    }

    private static JSONObject getOrCreateParent(JSONObject object, String... path) {
        for (int i = 0; i < path.length - 1; i++) {
            String field = path[i];

            if (object.has(field)) {
                object = object.getJSONObject(field);
            } else {
                JSONObject newObject = new JSONObject();
                object.put(field, newObject);

                object = newObject;
            }
        }

        return object;
    }

    private static Optional<JSONObject> getParent(JSONObject object, String... path) {
        for (int i = 0; i < path.length - 1; i++) {
            String field = path[i];

            if (object.has(field)) {
                object = object.getJSONObject(field);
            } else {
                return Optional.empty();
            }
        }

        return Optional.of(object);
    }


    private static String field(String... path) {
        return path[path.length - 1];
    }

    private JsonUtils() {
    }

}
