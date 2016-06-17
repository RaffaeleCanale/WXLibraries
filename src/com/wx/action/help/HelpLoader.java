package com.wx.action.help;

import com.wx.action.util.CommandContainer;
import com.wx.action.util.PropertyContainer;
import com.wx.action.command.Command;
import com.wx.action.command.DefaultCommandBuilder;
import com.wx.grammar.ParseException;
import com.wx.io.sp.SPObject;
import com.wx.io.sp.SPObjectParser;
import com.wx.util.representables.string.IntRepr;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Created on 21/11/2014
 *
 * @author Raffaele Canale (raffaelecanale@gmail.com)
 * @version 0.1
 */
public class HelpLoader {

    private static final String KEY_HELP = "help";
    private static final String KEY_PROPERTIES = "properties";
    private static final String KEY_MIN = "min";
    private static final String KEY_MAX = "max";
    private static final String KEY_COUNT = "count";
    private static final String KEY_MARKERS = "markers";
    private static final String KEY_DEFAULT = "default";
    private static final String KEY_TYPE = "type";

    private final Object[] params;

    public HelpLoader(Object... params) {
        this.params = params;
    }

    public CommandContainer[] load(String resourceName) throws IOException, ParseException {
        InputStream in = HelpLoader.class.getResourceAsStream(resourceName);
        if (in == null) {
            throw new IllegalArgumentException("Resource not found: " + resourceName);
        }

        try {
            return load(in);
        } finally {
            in.close();
        }
    }

    public CommandContainer[] load(InputStream in) throws IOException, ParseException {
        SPObject spObject = new SPObjectParser().parse(
                new BufferedInputStream(in));

        CommandContainer[] result = new CommandContainer[spObject.objectsKeySet().size()];

        Map<String, CommandContainer.Builder> builders = new HashMap<>();
        Map<String[], PropertyContainer.Builder> references = new HashMap<>();

        // Parse
        for (String cmd : spObject.objectsKeySet()) {
            CommandContainer.Builder builder = new CommandContainer.Builder();
            builder.setCmdName(cmd);
            parseCommand(spObject.getObject(cmd), builder, references);

            builders.put(cmd, builder);
        }


        // Solve references
        for (Map.Entry<String[], PropertyContainer.Builder> ref : references.entrySet()) {
            String cmd = ref.getKey()[0];
            String prop = ref.getKey()[1];

            CommandContainer.Builder builder = builders.get(cmd);
            if (builder == null) {
                throw new ParseException("Reference " + cmd + "." + prop + " points to an unknown command");
            }
            PropertyContainer.Builder original = builder.property(prop);
            if (original == null) {
                throw new ParseException("Reference " + cmd + "." + prop + " points to an unknown property");
            }
            PropertyContainer tmp = original.build();

            ref.getValue()
                    .setHelpText(tmp.getHelpText())
                    .setMin(tmp.getMin())
                    .setMax(tmp.getMax())
                    .setType(tmp.getType());
            if (tmp.hasDefaults()) {
                for (String def : tmp.getDefaultValues()) {
                    ref.getValue().addDefaultValue(def);
                }
            }
            if (tmp.isMarkedOption()) {
                ref.getValue().addMarkers(tmp.getMarkers());
            }
        }

        // Build
        int i = 0;
        for (CommandContainer.Builder builder : builders.values()) {
            result[i] = builder.build();
            i++;
        }

        return result;
    }

    private void parseCommand(SPObject spObject, CommandContainer.Builder builder, Map<String[], PropertyContainer.Builder> references) throws ParseException {
        builder.setHelp(checkedValue(spObject, KEY_HELP));

        SPObject properties = spObject.getObject(KEY_PROPERTIES);

        if (properties != null) {
            for (String property : properties.objectsKeySet()) {
                SPObject object = properties.getObject(property);

                if (property.contains(".") && object.isEmpty()) {
                    String[] reference = splitReference(property);
                    PropertyContainer.Builder p = builder.createNewProperty(reference[1]);
                    references.put(reference, p);
                } else {
                    parseProperty(
                            object,
                            builder.createNewProperty(property)
                    );
                }
            }
        }
    }

    private String[] splitReference(String reference) throws ParseException {
        String[] split = reference.split("\\.");
        if (split.length != 2) {
            throw new ParseException("Not a valid reference: " + reference);
        }

        return split;
    }

    private void parseProperty(SPObject object, PropertyContainer.Builder builder) throws ParseException {
        builder.setHelpText(checkedValue(object, KEY_HELP));

        boolean hasMarkers = addMarkers(builder, object);
        boolean hasNoParams = addMinMax(builder, object, hasMarkers);

        addDefaults(builder, object, hasMarkers && hasNoParams);
        addType(builder, object, hasMarkers && hasNoParams);


        /*
        DEFAULT TYPE = {
            String
            Boolean if MARKERS and 0 0
        }

        DEFAULT MIN = {
            1
            0 if MARKERS and Boolean
        }

        DEFAULT MAX = {
            1
            0 if MARKERS and Boolean
        }

        DEFAULT DEFAULT = {
            false if MARKERS and 0 0
        }

        min
        max
        type
        markers
        defaults
         */


    }

    private void addType(PropertyContainer.Builder builder, SPObject object, boolean booleanAsDefault) {
        String typeValue = value(object, KEY_TYPE);

        if (typeValue == null) {
            builder.setType(booleanAsDefault ? "Boolean" : "String");
        } else {
            builder.setType(typeValue);
        }
    }

    private boolean addMinMax(PropertyContainer.Builder builder, SPObject object, boolean hasMarkers) throws ParseException {
        Integer min, max;

        String countValue = value(object, KEY_COUNT);
        if (countValue == null) {
            min = object.getInteger(KEY_MIN);
            if (min == null) {
                min = hasMarkers ? 0 : 1;
            }

            max = object.getInteger(KEY_MAX);
            if (max == null) {
                max = hasMarkers ? 0 : 1;
            }

        } else {
            if (value(object, KEY_MIN) != null) throw new ParseException("min defined twice");
            if (value(object, KEY_MAX) != null) throw new ParseException("max defined twice");

            String[] split = countValue.split(" ");
            if (split.length != 2) throw new ParseException("count needs exactly 2 values");

            IntRepr caster = new IntRepr();
            min = caster.castOut(split[0]);
            max = caster.castOut(split[1]);
        }

        builder.setMin(min).setMax(max);

        return min == 0 && max == 0;
    }

    private boolean addMarkers(PropertyContainer.Builder builder, SPObject object) {
        String value = value(object, KEY_MARKERS);

        if (value != null) {
            builder.addMarkers(value.split(" "));

            return true;
        }

        return false;
    }

    private void addDefaults(PropertyContainer.Builder builder, SPObject object, boolean falseAsDefault) throws ParseException {
        String value = value(object, KEY_DEFAULT);
        if (value == null) {
            if (falseAsDefault) {
                builder.addDefaultValue("false");
            }
            return;
        }

        try {
            Command cmd = new DefaultCommandBuilder().build("dummy " + value);

            for (int i = 0; i < cmd.length(); i++) {
                builder.addDefaultValue(cmd.param(i));
            }
        } catch (IllegalArgumentException e) {
            throw new ParseException(e.getMessage());
        }
    }

    private String value(SPObject object, String key) {
        String value = object.getString(key);

        if (value != null) {
            for (int i = 0; i < params.length; i++) {
                if (params[i] != null) {
                    value = value.replaceAll("\\$" + (i+1), params[i].toString());
                }
            }

            if (value.matches("\\$[0-9]+") || value.contains("$")) {
                throw new IllegalArgumentException("Unmatched parameter at: " + value);
            }
        }

        return value;
    }

    private String checkedValue(SPObject object, String key) throws ParseException {
        String property = value(object, key);
        if (property == null) {
            throw new ParseException("Missing key: " + key);
        }


        return property;
    }


}
