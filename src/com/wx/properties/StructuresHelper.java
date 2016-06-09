package com.wx.properties;

import com.wx.properties.structures.list.IndexedList;
import com.wx.properties.structures.list.ListResourceFactory;
import com.wx.properties.structures.list.SingleLineList;
import com.wx.properties.structures.map.DirectMap;
import com.wx.properties.structures.map.MapResourceFactory;
import com.wx.properties.structures.map.RefHashMap;
import com.wx.properties.structures.set.RefSet;
import com.wx.properties.structures.set.SetResourceFactory;
import com.wx.properties.structures.set.SingleLineSet;
import com.wx.properties.structures.view.AbstractStructureView;
import com.wx.properties.structures.view.StructureView;
import com.wx.util.pair.Pair;
import com.wx.util.representables.DelimiterEncoder;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * This is a helper class exclusively for {@link PropertiesManager}. It helps manage all structures-related operations
 * <p>
 * Created by canale on 09.05.16.
 */
class StructuresHelper {

    static final Map<String, ListResourceFactory> LIST_FACTORIES = Arrays.asList(
            Pair.of(IndexedList.class.getName(), IndexedList.factory()),
            Pair.of(SingleLineList.class.getName(), SingleLineList.factory())
    ).stream().collect(Collectors.toMap(Pair::get1, Pair::get2));

    static final Map<String, MapResourceFactory> MAP_FACTORIES = Arrays.asList(
            Pair.of(DirectMap.class.getName(), DirectMap.factory()),
            Pair.of(RefHashMap.class.getName(), RefHashMap.factory())
    ).stream().collect(Collectors.toMap(Pair::get1, Pair::get2));

    static final Map<String, SetResourceFactory> SET_FACTORIES = Arrays.asList(
            Pair.of(RefSet.class.getName(), RefSet.factory()),
            Pair.of(SingleLineSet.class.getName(), SingleLineSet.factory())
    ).stream().collect(Collectors.toMap(Pair::get1, Pair::get2));

    private static final String RESERVED_KEY = "rd.";
    private static final String STRUCT_HEADER_KEY = "st.";
    private static final char RESERVED_MARKER = '.';

    private static String getReservedKey(String key) {
        return RESERVED_KEY + key;
    }

    private static String getStructKey(String key) {
        return STRUCT_HEADER_KEY + key;
    }

//    private static String structKeyToUserKey(String structKey) {
//        return structKey.substring(STRUCT_HEADER_KEY.length());
//    }

    private static boolean isReservedKey(String key) {
        return key.startsWith(RESERVED_KEY);
    }

    private static boolean isStructKey(String key) {
        return key.startsWith(STRUCT_HEADER_KEY);
    }

    static Stream<String> filterToUserKeys(Stream<String> keys) {
        return keys.filter(key -> !isReservedKey(key))
                .map(key -> isStructKey(key) ? key.substring(STRUCT_HEADER_KEY.length()) : key);
    }

    static void checkKey(String key) {
        if (isStructKey(key)) {
            throw new IllegalAccessError("Use the corresponding structure method to get the property");
        }
        if (isReservedKey(key)) {
            throw new IllegalAccessError("Can't access that key");
        }
    }

    static void removeStructAt(String userKey, Map<String, String> map) {
        StructSubView view = new StructSubView(userKey, map);
        view.removeHeader();
        view.clear();
    }


    static StructInfo putStructInfo(String userKey, Map<String, String> map, String className, String... options) {
        checkStructKey(userKey);
        StructSubView view = new StructSubView(userKey, map);

        List<String> values = new ArrayList<>(options.length + 1);
        values.add(className);
        values.addAll(Arrays.asList(options));

        String structClass = DelimiterEncoder.autoEncode(values);

        String oldValue = view.setStructClass(structClass);
        if (oldValue != null) {
            throw new AssertionError("Cannot put struct info on existing key");
        }


        return new StructInfo(view, className, options);
    }

    static <F> F getFactory(String className, Map<String, F> factories) {
        F factory = factories.get(className);
        if (factory == null) {
            throw new UnsupportedOperationException("Factory not supported: " + className);
        }

        return factory;
    }


    static Optional<StructInfo> getStructInfo(String userKey, Map<String, String> map) {
        checkStructKey(userKey);

        StructSubView view = new StructSubView(userKey, map);
        String structClass = view.getStructClass();

        if (structClass == null) {
            return Optional.empty();
        }

        List<String> values = DelimiterEncoder.autoDecode(structClass);
        if (values.isEmpty()) {
            throw new ClassCastException(); // TODO: 09.05.16 Message
        }

        String className = values.get(0);
        String[] options = values.subList(1, values.size()).toArray(new String[values.size() - 1]);

        return Optional.of(new StructInfo(view, className, options));
    }


    private static void checkStructKey(String key) {
        checkKey(key);
        if (key.indexOf(RESERVED_MARKER) > 0) {
            throw new IllegalArgumentException("Structure keys cannot contain " + RESERVED_MARKER);
        }
    }


    static class StructInfo {

        final StructureView view;
        final String className;
        final String[] options;

        private StructInfo(StructureView view, String className, String[] options) {
            this.view = view;
            this.className = className;
            this.options = options;
        }
    }

    private static class StructSubView extends AbstractStructureView {


        private final String structClassKey;

        private final String structKeyPrefix;

        private StructSubView(String userKey, Map<String, String> map) {
            super(getStructKey(userKey), map);

            this.structClassKey = getReservedKey("class" + RESERVED_MARKER + userKey) + RESERVED_MARKER;
            this.structKeyPrefix = getReservedKey(userKey) + RESERVED_MARKER;
        }

        private String getStructClass() {
            return map.get(structClassKey);
        }

        private String setStructClass(String value) {
            return map.put(structClassKey, value);
        }

        @Override
        public void removeHeader() {
            super.removeHeader();
            map.remove(structClassKey);
        }

        @Override
        protected boolean isKeyInView(String realKey) {
            return realKey.startsWith(structKeyPrefix);
        }

        @Override
        protected String realKeyToView(String realKey) {
            return realKey.substring(structKeyPrefix.length());
        }

        @Override
        protected String viewKeyToReal(String viewKey) {
            return structKeyPrefix + viewKey;
        }
    }

}
