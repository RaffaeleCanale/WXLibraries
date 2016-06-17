package com.wx.action.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * @author Raffaele Canale (raffaelecanale@gmail.com)
 * @version 0.1
 */
public class PropertyContainer {

    private final String name;
    private final String type;
    private final String helpText;
    private final int id;
    private final int min, max;
    private final String[] markers;
    private final String[] defaultValues;

    private PropertyContainer(String name, String type, String helpText, int id,
                              int min, int max, String[] markers, String[] defaultValues) {
        this.name = name;
        this.type = type;
        this.helpText = helpText;
        this.id = id;
        this.min = min;
        this.max = max;
        this.markers = markers;
        this.defaultValues = defaultValues;

        assert defaultValues == null
                || (defaultValues.length <= max && defaultValues.length >= min)
                || (defaultValues.length == 1 && min == 0 && max == 0 && type.toLowerCase().equals("boolean")):
                "Defaults count does not match conditions (" + defaultValues.length + " not in [" + min + ", " + max + "])" +
                        "\nProperty:\n" + this;
    }

    public boolean isMarkedOption() {
        return markers.length > 0;
    }

    public int getId() {
        return id;
    }

    public int getMin() {
        return min;
    }

    public int getMax() {
        return max;
    }

    public String[] getMarkers() {
        return markers;
    }

    public String getType() {
        return type;
    }

    public String[] getDefaultValues() {
        return defaultValues;
    }

    public String getHelpText() {
        return helpText;
    }

    public String getName() {
        return name;
    }

    public boolean hasDefaults() {
        return defaultValues != null;
    }

    @Override
    public String toString() {
        return "PropertyContainer{" + "id=" + id + ", min=" + min
                + ", max=" + max + ", markers=" + Arrays.toString(markers)
                + ", defaultValues=" + Arrays.toString(defaultValues)
                + ", type=" + type
                + ", helpText=" + helpText + ", name=" + name + '}';
    }

    public static class Builder {

        private final List<String> markers = new ArrayList<>();
        private final List<String> defaultValues = new ArrayList<>();
        private int id = -1;
        private int min = -1;
        private int max = -1;
        private String helpText;
        private String name;
        private String type;

        public Builder setId(int id) {
            check(this.id, "id");
            this.id = id;

            return this;
        }

        public Builder setMin(int min) {
            check(this.min, "min");
            this.min = min;

            return this;
        }

        public Builder setMax(int max) {
            check(this.max, "max");
            this.max = max;

            return this;
        }

        public Builder addMarkers(String... markers) {
            Collections.addAll(this.markers, markers);

            return this;
        }

        public Builder addDefaultValue(String defaultValue) {
            this.defaultValues.add(defaultValue);

            return this;
        }

        public Builder setHelpText(String helpText) {
            check(this.helpText, "help");
            this.helpText = helpText;

            return this;
        }

        public Builder setName(String name) {
            check(this.name, "name");
            this.name = name;

            return this;
        }

        public Builder setType(String type) {
            check(this.type, "type");
            this.type = type;

            return this;
        }

        public PropertyContainer build() {
            return new PropertyContainer(name, type, helpText, id, min, max,
                    toArray(markers), defaultValues.isEmpty() ? null : toArray(defaultValues));
        }

        private void check(String property, String name) {
            if (property != null) {
                throw new IllegalStateException("Property [" + name +
                        "] already set for " + this.name + ": " + property);
            }
        }

        private void check(int property, String name) {
            if (property >= 0) {
                throw new IllegalStateException("Property [" + name +
                        "] already set for " + this.name + ": " + property);
            }
        }

        private String[] toArray(List<String> list) {
            String[] result = new String[list.size()];
            return list.toArray(result);
        }
    }

}
