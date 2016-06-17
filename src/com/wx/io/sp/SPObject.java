package com.wx.io.sp;

import com.wx.util.representables.TypeCaster;
import com.wx.util.representables.string.IntRepr;
import com.wx.util.representables.string.NullSafeCaster;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created on 20/11/2014
 *
 * @author Raffaele Canale (raffaelecanale@gmail.com)
 * @version 0.1
 */
public class SPObject {

    private final Map<String, String> properties;
    private final Map<String, SPObject> objects;

    private SPObject(Map<String, String> properties, Map<String, SPObject> objects) {
        this.properties = properties;
        this.objects = objects;
    }

    public String getString(String key) {
        return properties.get(key);
    }

    public Integer getInteger(String key) {
        return getValue(key, new IntRepr());
    }

    public <T> T getValue(String key, TypeCaster<String, T> caster) {
        caster = new NullSafeCaster<>(caster);
        return caster.castOut(getString(key));
    }

    public SPObject getObject(String key) {
        return objects.get(key);
    }

    public Set<String> propertiesKeySet() {
        return properties.keySet();
    }

    public Set<String> objectsKeySet() {
        return objects.keySet();
    }

    public int size() {
        return properties.size() + objects.size();
    }


    @Override
    public String toString() {
        return toString(new StringBuilder(), "").toString();
    }

    private StringBuilder toString(StringBuilder builder, String prefix) {
        for (Map.Entry<String, String> propEntry : properties.entrySet()) {
            builder.append(prefix)
                    .append(propEntry.getKey())
                    .append(" = ");
            if (propEntry.getValue().contains("\n")) {
                // Value is multiline
                builder.append("\n")
                        .append(prefix)
                        .append("\t")
                        .append(propEntry.getValue().replaceAll("\n", "\n\t" + prefix))
                        .append('\n');
            } else {
                builder.append(propEntry.getValue()).append("\n");
            }
        }

        for (Map.Entry<String, SPObject> objEntry : objects.entrySet()) {
            builder.append(prefix)
                    .append(objEntry.getKey())
                    .append(":")
                    .append("\n");
            objEntry.getValue().toString(builder, prefix + "\t");
        }

        return builder;
    }

    public boolean isEmpty() {
        return size() == 0;
    }

    public static class Builder {
        private final Map<String, String> properties = new LinkedHashMap<>();
        private final Map<String, SPObject> objects = new LinkedHashMap<>();

        public Builder put(String key, String property) {
            String oldValue = properties.put(key, property);
            if (oldValue != null) {
                throw new IllegalStateException("Two properties have the same key " + key +
                        "\n\t- " + oldValue +
                        "\n\t- " + property);
            }
            return this;
        }

        public Builder put(String key, SPObject object) {
            SPObject oldValue = objects.put(key, object);
            if (oldValue != null) {
                throw new IllegalStateException("Two objects have the same key " + key +
                        "\n\t- " + oldValue +
                        "\n\t- " + object);
            }
            return this;
        }

        public SPObject build() {
            return new SPObject(properties, objects);
        }
    }
}
