package com.wx.properties;

import com.wx.properties.property.ResourceProperty;
import com.wx.properties.structures.list.IndexedList;
import com.wx.properties.structures.list.ListResource;
import com.wx.properties.structures.list.ListResourceFactory;
import com.wx.properties.structures.map.MapResource;
import com.wx.properties.structures.map.MapResourceFactory;
import com.wx.properties.structures.set.RefSet;
import com.wx.properties.structures.set.SetResource;
import com.wx.properties.structures.set.SetResourceFactory;
import com.wx.util.representables.TypeCaster;
import com.wx.util.representables.string.*;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.wx.properties.StructuresHelper.*;
import static java.util.Objects.requireNonNull;

/**
 * This class enhances any properties map by providing advanced features such as the ability to store many types,
 * including data structures. <p> In order to provide theses features, this manager comes with a set of limitations.
 * Mainly, it uses constructs such as {@code ${...}} to encode commands. Thus, to avoid unexpected behaviour, values
 * should not contains that pattern of characters. <p>
 * <p>
 * Implemented commands are: <p> <lu> <li> <b>{@code param:<N>}</b> <p> Adds a free parameter at this right place. If
 * this command is used in a field, so when calling this field you must place the wanted parameters in the <b>{@code
 * param}</b> array. The number <b>N</b> is the index of the wanted parameter (since a single field can contain several
 * parameters) and the value of this parameter should be found at the same index of the <b>{@code param}</b> array.<br>
 * If this command is found and the given {@code param} is {@code null} or the given {@code param} doesn't contain the
 * index <b>N</b>, then an exception is thrown.</p><br> Example:<br>{@code Test.field1 = my configurable field
 * ${param:0}.}<br> <br> <p> <li> <b>{@code field:<Field Key>}</b> <p> Places at this right place the value of another
 * field. If such a command is found, the replace value is found using this same method <b>{@code getString(Field Key,
 * param)}</b>, which means that the <b>Field Key</b> can also contain special commands in it. It also means that
 * exceptions could be thrown if any error occurs while getting <b>Field Key</b> value.</p><br> Example:<br> {@code
 * Test.field2 = This is ${field:Test.field1}.}<br><br>
 * <p>
 * Created on 15/01/2016
 *
 * @author Raffaele Canale (raffaelecanale@gmail.com)
 * @version 1.0
 */
@SuppressWarnings({"WeakerAccess", "unused"})
public class PropertiesManager {

    protected final Map<String, String> map;


    /**
     * Build a new empty manager.
     */
    public PropertiesManager() {
        this(new HashMap<>());
    }

    /**
     * Build a new manager based on the given properties.
     *
     * @param map Underlying map
     */
    public PropertiesManager(Map<String, String> map) {
        this.map = map;
    }

    /**
     * Tests if the specified key is in this {@code ResourcePage}.
     *
     * @param key possible key
     *
     * @return <code>true</code> if the key is in this page
     */
    public boolean containsKey(String key) {
        return userKeySet().anyMatch(k -> k.equals(key));
    }

    // TODO: 09.06.16 containsValue is problematic because values have undefined types...
//    /**
//     * Tests if the specified value is in this {@code ResourcePage}.
//     *
//     * @param value  possible value
//     * @param caster Caster used to cast the value
//     * @param <E>    Type of the value to find
//     *
//     * @return {@code true} if the value is in this page
//     */
//    public <E> boolean containsValue(E value, TypeCaster<String, E> caster) {
//        return map.values().stream()
//                .map(caster::castOut)
//                .anyMatch(v -> v.equals(value));
//
////        return map.containsValue(caster.castIn(value));
//    }

    /**
     * Gets a string property as a {@link ResourceProperty}. The returned property will be linked to the properties map
     * and any change on it will affect the map, too.
     *
     * @param key The key whose property to get
     *
     * @return The property associated to this key
     */
    public ResourceProperty<String> getProperty(String key) {
        return getProperty(key, new StringRepr());
    }

    /**
     * Gets a property as a {@link ResourceProperty}. The returned property will be linked to the properties map and any
     * change on it will affect the map, too.
     *
     * @param key    The key whose property to get
     * @param caster Caster to cast from the String representation to the target type
     *
     * @return The property associated to this key
     */
    public <Type> ResourceProperty<Type> getProperty(String key, TypeCaster<String, Type> caster) {
        return new ResourceProperty<>(this, key, caster);
    }

    /**
     * Gets an integer property as a {@link ResourceProperty}. The returned property will be linked to the properties
     * map and any change on it will affect the map, too.
     *
     * @param key The key whose property to get
     *
     * @return The property associated to this key
     */
    public ResourceProperty<Integer> getIntProperty(String key) {
        return getProperty(key, new IntRepr());
    }


    //<editor-fold desc="Getters">

    /**
     * Get the value mapped with the given key.
     *
     * @param key    The key associated with the value to return
     * @param caster Function that cast the raw string value to the expected type
     * @param params Array containing the parameters if a {@code ${param:&lt;N&gt;}} command is found
     * @param <E>    Type of the value to return
     *
     * @return The value associated with the given key if any
     */
    public <E> Optional<E> getValue(String key, Function<String, E> caster, Object... params) {
        checkKey(key);
        return Optional.ofNullable(map.get(key)).map(v -> decodeValue(v, params)).map(caster);
    }

    private <E> Optional<E> getValue(String key, TypeCaster<String, E> caster) {
        return getValue(key, caster::castOut);
    }


    /**
     * Returns the {@code String} value mapped to the given key.
     *
     * @param key   The key associated with the value to return
     * @param param Array containing the parameters if a {@code ${param:&lt;N&gt;}} command is found
     *
     * @return The value associated with the given key if any
     */
    public Optional<String> getString(String key, Object... param) {
        return getValue(key, Function.identity(), param);
    }


    /**
     * Returns the {@code char} value mapped to the given key.
     *
     * @param key The key associated with the value to return
     *
     * @return The value associated with the given key if any
     */
    public Optional<Character> getChar(String key) {
        return getValue(key, new CharRepr());
    }

    /**
     * Returns a {@code boolean} representation of the value mapped to the given field key.
     *
     * @param key The key associated with the value to return
     *
     * @return The value associated with the given key if any
     */
    public Optional<Boolean> getBoolean(String key) {
        return getValue(key, new BooleanRepr());
    }

    /**
     * Returns an {@code int} representation of the value mapped to the given field key.
     *
     * @param key The key associated with the value to return
     *
     * @return The value associated with the given key if any
     */
    public Optional<Integer> getInt(String key) {
        return getValue(key, new IntRepr());
    }

    /**
     * Returns a {@code long} representation of the value mapped to the given field key.
     *
     * @param key The key associated with the value to return
     *
     * @return The value associated with the given key if any
     */
    public Optional<Long> getLong(String key) {
        return getValue(key, new LongRepr());
    }


    /**
     * Returns a {@code double} representation of the value mapped to the given field key.
     *
     * @param key The key associated with the value to return
     *
     * @return The value associated with the given key if any
     */
    public Optional<Double> getDouble(String key) {
        return getValue(key, new DoubleRepr());
    }


    /**
     * Returns a {@code byte[]} representation of the value mapped to the given field key.
     *
     * @param key The key associated with the value to return
     *
     * @return The value associated with the given key if any
     */
    public Optional<byte[]> getBytes(String key) {
        return getValue(key, new EncodedBytesRepr());
    }
    //</editor-fold>


    //<editor-fold desc="Setters">

    /**
     * Sets a property.
     *
     * @param <E>    Type of the property
     * @param key    Key of the property
     * @param value  Value to be set to this key
     * @param caster Caster to represent the value into a {@code String}
     *
     * @return the previous raw value if any
     */
    public <E> Optional<String> setProperty(String key, E value,
                                            Function<E, String> caster) {
        checkKey(key);
        String stringValue = requireNonNull(caster.apply(value));
        return Optional.ofNullable(map.put(key, stringValue));
    }

    private <E> Optional<String> setProperty(String key, E value,
                                             TypeCaster<String, E> caster) {
        return setProperty(key, value, caster::castIn);
    }

    /**
     * Sets a property.
     *
     * @param key   Key of the property
     * @param value Value to be set to this key
     *
     * @return the previous raw value if any
     */
    public Optional<String> setProperty(String key, String value) {
        return setProperty(key, value, Function.identity());
    }

    /**
     * Sets an {@code int} property
     *
     * @param key   Key of the property
     * @param value Value to be set
     *
     * @return the previous raw value if any
     */
    public Optional<String> setProperty(String key, int value) {
        return setProperty(key, value, new IntRepr());
    }

    /**
     * Sets a {@code double} property
     *
     * @param key   Key of the property
     * @param value Value to be set
     *
     * @return the previous raw value if any
     */
    public Optional<String> setProperty(String key, double value) {
        return setProperty(key, value, new DoubleRepr());
    }

    /**
     * Sets a {@code boolean} property
     *
     * @param key   Key of the property
     * @param value Value to be set
     *
     * @return the previous raw value if any
     */
    public Optional<String> setProperty(String key, boolean value) {
        return setProperty(key, value, new BooleanRepr());
    }

    /**
     * Sets a byte array property.
     *
     * @param key   Key of the property
     * @param value Value to be set to this key
     *
     * @return the previous raw value if any
     */
    public Optional<String> setProperty(String key, byte[] value) {
        return setProperty(key, value, new EncodedBytesRepr());
    }

    /**
     * Sets {@code long} a property.
     *
     * @param key   Key of the property
     * @param value Value to be set to this key
     *
     * @return the previous raw value if any
     */
    public Optional<String> setProperty(String key, long value) {
        return setProperty(key, value, new LongRepr());
    }

    /**
     * Set a whole map of properties. This will add all the properties contained in the given map.
     * <p>
     * <b>Note:</b> Do not confuse this with {@link #setMap} that will associate a map to a single key.
     *
     * @param <E>        Type of the properties (must be the same for all properties)
     * @param properties The keys - values to set.
     * @param caster     Caster to represent the value into a {@code String}
     */
    public <E> void setProperties(Map<String, E> properties,
                                  Function<E, String> caster) {
        properties.forEach((k, v) -> setProperty(k, v, caster));
    }

    /**
     * Set a whole map of properties. This will add all the properties contained in the given map.
     * <p>
     * <b>Note:</b> Do not confuse this with {@link #setMap} that will associate a map to a single key.
     *
     * @param properties The keys - values to set.
     */
    public void setProperties(Map<String, String> properties) {
        setProperties(properties, Function.identity());
    }
    //</editor-fold>


    //<editor-fold desc="Structures">
    public ListResource<String> getList(String key) {
        return getList(key, new StringRepr());
    }

    public <E> ListResource<E> getList(String key, TypeCaster<String, E> caster) {
        Optional<StructInfo> structInfo = getStructInfo(key, map);

        if (!structInfo.isPresent()) {
            return null;
        }

        ListResourceFactory factory = getFactory(structInfo.get().className, LIST_FACTORIES);
        return factory.loadList(structInfo.get().view, caster, (Object[]) structInfo.get().options);
    }

    public ListResource<String> setList(String key, List<String> list) {
        return setList(key, list, new StringRepr(), IndexedList.class);
    }

    public <E> ListResource<E> setList(String key, List<E> list,
                                       TypeCaster<String, E> caster, Class<? extends ListResource> cls,
                                       String... options) {
        ListResourceFactory factory = getFactory(cls.getName(), LIST_FACTORIES);


        removeStructAt(key, map);
        StructInfo structInfo = putStructInfo(key, map, cls.getName(), options);

        return factory.createList(structInfo.view, caster, list, (Object[]) options);
    }

    public <K, V> MapResource<K, V> getMap(String key,
                                           TypeCaster<String, K> kCaster, TypeCaster<String, V> vCaster) {
        Optional<StructInfo> structInfo = getStructInfo(key, map);

        if (!structInfo.isPresent()) {
            return null;
        }

        MapResourceFactory factory = getFactory(structInfo.get().className, MAP_FACTORIES);
        return factory.loadMap(structInfo.get().view, kCaster, vCaster, (Object[]) structInfo.get().options);
    }

    public <K, V> MapResource<K, V> setMap(String key, Map<K, V> mapToSet,
                                           TypeCaster<String, K> kCaster, TypeCaster<String, V> vCaster,
                                           Class<? extends MapResource> cls, String... options) {
        MapResourceFactory factory = getFactory(cls.getName(), MAP_FACTORIES);

        removeStructAt(key, map);
        StructInfo structInfo = putStructInfo(key, map, cls.getName(), options);

        return factory.createMap(structInfo.view, kCaster, vCaster, mapToSet, (Object[]) options);
    }

    public SetResource<String> getSet(String key) {
        return getSet(key, new StringRepr());
    }

    public <E> SetResource<E> getSet(String key, TypeCaster<String, E> caster) {
        Optional<StructInfo> structInfo = getStructInfo(key, map);

        if (!structInfo.isPresent()) {
            return null;
        }

        SetResourceFactory factory = getFactory(structInfo.get().className, SET_FACTORIES);
        return factory.loadSet(structInfo.get().view, caster, (Object[]) structInfo.get().options);
    }

    public SetResource<String> setSet(String key, Set<String> set) {
        return setSet(key, set, new StringRepr(), RefSet.class);
    }

    public <E> SetResource<E> setSet(String key, Set<E> set,
                                     TypeCaster<String, E> caster, Class<? extends SetResource> cls,
                                     String... options) {
        SetResourceFactory factory = getFactory(cls.getName(), SET_FACTORIES);


        removeStructAt(key, map);
        StructInfo structInfo = putStructInfo(key, map, cls.getName(), options);

        return factory.createSet(structInfo.view, caster, set, (Object[]) options);
    }
    //</editor-fold>


    public Optional<String> removeProperty(String key) {
        checkKey(key);
        return Optional.ofNullable(map.remove(key));
    }

    public boolean removeProperties(Collection<String> keys) {
        boolean changed = false;
        for (String key : keys) {
            if (removeProperty(key).isPresent()) {
                changed = true;
            }
        }

        return changed;
    }


    public Set<String> keySet() {
        return userKeySet().collect(Collectors.toSet());
    }

    public void clear() {
        map.clear();
    }

    @Override
    public String toString() {
        StringBuilder desc = new StringBuilder();

        if (map.keySet().isEmpty()) {
            desc.append("  Empty, no properties stored\n");
        } else {
            desc.append("  Properties:\n");
            TreeSet<String> sortedKeys = new TreeSet<>(map.keySet());
            for (String key : sortedKeys) {
                desc.append("    ").append(key).append(" = ")
                        .append(map.get(key))
                        .append("\n");
            }
        }

        return desc.toString();
    }

    private Stream<String> userKeySet() {
        return filterToUserKeys(map.keySet().stream());
    }


    /**
     * This method performs all commands contained in a field if any. It works specifically with the {@link
     * #getString(String, Object[]) }, so if some commands lead to other type fields, it could result with unexpected
     * results.<br> Actual implemented commands are: <p> <lu> <li> <b>{@code param:<N>}</b> <p> Adds a free parameter at
     * this right place. If this command is used in a field, so when calling this field you must place the wanted
     * parameters in the <b>{@code param}</b> array. The number <b>N</b> is the index of the wanted parameter (since a
     * single field can contain several parameters) and the value of this parameter should be found at the same index of
     * the <b>{@code param}</b> array.<br> If this command is found and the given {@code param} is {@code null} or the
     * given {@code param} doesn't contain the index <b>N</b>, then an exception is thrown.</p><br> Example:<br>{@code
     * Test.field1 = my configurable field ${param:0}.}<br> <br> <p> <li> <b>{@code field:<Field Key>}</b> <p> Places at
     * this right place the value of another field. If such a command is found, the replace value is found using this
     * same method <b>{@code getString(Field Key, param)}</b>, which means that the <b>Field Key</b> can also contain
     * special commands in it. It also means that exceptions could be thrown if any error occurs while getting <b>Field
     * Key</b> value.</p><br> Example:<br> {@code Test.field2 = This is ${field:Test.field1}.}<br><br> <p> <li>
     * <b>{@code null}</b> <p> Permits to store {@code null} values. Just place this command in the field and it will
     * always return a {@code null} value.</p><br> Example:<br> {@code Test.field3 = ${null}} </lu><br><br>
     *
     * @param fieldValue The field to update.
     * @param param      Array containing the parameters if a {@code ${param:<N>}} command is found.
     *
     * @return The value of field with all commands properly replaced by the wanted value or the exact same field if no
     * command is found.
     */
    private String decodeValue(String fieldValue, final Object... param) {
        StringBuilder tmp = new StringBuilder(fieldValue);

        int startIndex;
        while ((startIndex = tmp.indexOf("${")) != -1) {
            int lastIndex = tmp.indexOf("}");
            if (lastIndex == -1 || lastIndex < startIndex) {
                throw new IllegalArgumentException("Illegal property "
                        + "construction. Character '}' missing or doubled.");
            }

            String innerCommand = tmp.substring(startIndex + 2, lastIndex);
            String replaceValue;

            if (innerCommand.startsWith("param:")) {
                int paramIndex = Integer.parseInt(innerCommand.substring(6));
                if (param == null || paramIndex >= param.length) {
                    throw new IllegalArgumentException("Tried to access a"
                            + " parameter field with invalid parameters."
                            + "\nNeed index " + paramIndex + " but given "
                            + "parameters " + (param == null ? "is null"
                            : "size is " + param.length + "\nField: "
                            + fieldValue));
                }

                replaceValue = param[paramIndex].toString();
            } else if (innerCommand.startsWith("field:")) {

                //KeyType key = keyStrategy.getUserKeyFromString(innerCommand.substring(6));
                String key = innerCommand.substring(6);

                replaceValue = getString(key, param).orElseThrow(() ->
                        new IllegalArgumentException("Referenced field '" + key + "' cannot be found"));

            } else if (innerCommand.equals("null")) {
                return null;
            } else if (innerCommand.equals("space")) {

                replaceValue = " ";

            } else {
                throw new IllegalArgumentException("An unknown command has been "
                        + "found: " + innerCommand + "\nField: " + fieldValue);
            }

            tmp.replace(startIndex, lastIndex + 1, replaceValue);
        }

        return tmp.toString();
    }

}
