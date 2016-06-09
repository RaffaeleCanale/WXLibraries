package com.wx.properties.page;

import com.wx.properties.PropertiesManager;

import java.io.*;
import java.util.*;

/**
 * This class extends a {@link PropertiesManager} with the ability to save and load the properties to file.
 *
 * @author Raffaele Canale - raffaelecanale@gmail.com
 * @version 3.0
 *          <p>
 *          Project: WXLibraries File: ResourcePage.java (UTF-8) Date: Jul 2, 2013
 */
@SuppressWarnings({"WeakerAccess", "unused"})
public class ResourcePage extends PropertiesManager {

    /**
     * @return A builder for ResourcePage
     */
    public static PageBuilder builder() {
        return new PageBuilder();
    }

    private final InputLink inputLink;
    private final OutputLink outputLink;
    private final boolean useXml;
    private final String encoding;

    ResourcePage(InputLink inputLink, OutputLink outputLink, boolean useXml, String encoding, ResourcePage defaults) {
        super(new PropertiesInterface(defaults));

        this.encoding = Objects.requireNonNull(encoding);
        this.inputLink = inputLink;
        this.outputLink = outputLink;
        this.useXml = useXml;
    }

    /**
     * Saves this page using the corresponding {@link OutputLink}.
     * <p>
     * This resource page watches for changes, and if no change has been detected, this method will not do anything.
     *
     * @return {@code true} if this page was just saved to file
     *
     * @throws IOException
     */
    public boolean save() throws IOException {
        if (((PropertiesInterface) super.map).changed) {
            forceSave();
            return true;
        }
        return false;
    }


    /**
     * Saves this page using the corresponding {@link OutputLink}.
     * <p>
     * This method will save this page into disk regardless to the fact that it has been modified or not.
     *
     * @throws IOException
     */
    public void forceSave() throws IOException {
        PropertiesInterface pi = (PropertiesInterface) super.map;

        try (OutputStream out = getOutput()) {
            if (useXml) {
                pi.properties.storeToXML(out, null, encoding);
            } else {
                pi.properties.store(new OutputStreamWriter(out, encoding), null);
            }

            pi.changed = false;
        }
    }

    /**
     * Load the properties from the file to this object.
     *
     * @throws IOException
     */
    public void load() throws IOException {
        Properties properties = ((PropertiesInterface) super.map).properties;

        try (InputStream in = getInput()) {
            if (useXml) {
                // TODO: 1/15/16 This should consider encoding!
                properties.loadFromXML(in);
            } else {
                properties.load(new InputStreamReader(in, encoding));
            }
        }
    }

    private OutputStream getOutput() throws IOException {
        if (outputLink == null) {
            throw new IllegalArgumentException("Properties are not writable");
        }
        return outputLink.getOutputStream();
    }

    private InputStream getInput() throws IOException {
        if (inputLink == null) {
            throw new IllegalArgumentException("Properties are not readable");
        }
        return inputLink.getInputStream();
    }

    private static class PropertiesInterface implements Map<String, String> {

        private final Properties properties;
        private boolean changed;

        public PropertiesInterface(ResourcePage defaults) {
            this.properties = new Properties(defaults == null ? null : ((PropertiesInterface) defaults.map).properties);
        }

        @Override
        public int size() {
            return properties.size();
        }

        @Override
        public boolean isEmpty() {
            return properties.isEmpty();
        }

        @Override
        public boolean containsKey(Object key) {
            return properties.containsKey(key);
        }

        @Override
        public boolean containsValue(Object value) {
            return properties.containsValue(value);
        }

        @Override
        public String get(Object key) {
            return properties.getProperty((String) key);
        }

        @Override
        public String put(String key, String value) {
            changed = true;
            return (String) properties.setProperty(key, value);
        }

        @Override
        public String remove(Object key) {
            changed = true;
            return (String) properties.remove(key);
        }

        @Override
        public void putAll(Map<? extends String, ? extends String> m) {
            changed = true;
            properties.putAll(m);
        }

        @Override
        public void clear() {
            changed = true;
            properties.clear();
        }

        @Override
        public Set<String> keySet() {
            return new Set<String>() {
                final Set<Object> keys = properties.keySet();

                @Override
                public int size() {
                    return keys.size();
                }

                @Override
                public boolean isEmpty() {
                    return keys.isEmpty();
                }

                @Override
                public boolean contains(Object o) {
                    return keys.contains(o);
                }

                @Override
                public Iterator<String> iterator() {
                    return new Iterator<String>() {

                        final Iterator<Object> it = keys.iterator();

                        @Override
                        public boolean hasNext() {
                            return it.hasNext();
                        }

                        @Override
                        public String next() {
                            return (String) it.next();
                        }

                        @Override
                        public void remove() {
                            changed = true;
                            it.remove();
                        }
                    };
                }

                @Override
                public Object[] toArray() {
                    return keys.toArray();
                }

                @Override
                public <T> T[] toArray(T[] a) {
                    return keys.toArray(a);
                }

                @Override
                public boolean add(String s) {
                    changed = true;
                    return keys.add(s);
                }

                @Override
                public boolean remove(Object o) {
                    changed = true;
                    return keys.remove(o);
                }

                @Override
                public boolean containsAll(Collection<?> c) {
                    return keys.containsAll(c);
                }

                @Override
                public boolean addAll(Collection<? extends String> c) {
                    changed = true;
                    return keys.addAll(c);
                }

                @Override
                public boolean retainAll(Collection<?> c) {
                    return keys.retainAll(c);
                }

                @Override
                public boolean removeAll(Collection<?> c) {
                    changed = true;
                    return keys.removeAll(c);
                }

                @Override
                public void clear() {
                    changed = true;
                    keys.clear();
                }
            };
        }

        @Override
        public Collection<String> values() {
            return new Collection<String>() {
                final Collection<Object> values = properties.values();

                @Override
                public void clear() {
                    changed = true;
                    values.clear();
                }

                @Override
                public int size() {
                    return values.size();
                }

                @Override
                public boolean isEmpty() {
                    return values.isEmpty();
                }

                @Override
                public boolean contains(Object o) {
                    return values.contains(o);
                }

                @Override
                public Iterator<String> iterator() {
                    return new Iterator<String>() {

                        final Iterator<Object> it = values.iterator();

                        @Override
                        public boolean hasNext() {
                            return it.hasNext();
                        }

                        @Override
                        public String next() {
                            return (String) it.next();
                        }

                        @Override
                        public void remove() {
                            changed = true;
                            it.remove();
                        }
                    };
                }

                @Override
                public Object[] toArray() {
                    return values.toArray();
                }

                @Override
                public <T> T[] toArray(T[] a) {
                    return values.toArray(a);
                }

                public boolean add(String o) {
                    changed = true;
                    return values.add(o);
                }

                @Override
                public boolean remove(Object o) {
                    changed = true;
                    return values.remove(o);
                }

                @Override
                public boolean containsAll(Collection<?> c) {
                    return values.containsAll(c);
                }

                @Override
                public boolean addAll(Collection<? extends String> c) {
                    changed = true;
                    return values.addAll(c);
                }

                @Override
                public boolean retainAll(Collection<?> c) {
                    changed = true;
                    return values.retainAll(c);
                }

                @Override
                public boolean removeAll(Collection<?> c) {
                    changed = true;
                    return values.removeAll(c);
                }
            };
        }

        @Override
        public Set<Entry<String, String>> entrySet() {
            return new Set<Entry<String, String>>() {
                final Set<Entry<Object, Object>> entries = properties.entrySet();

                @Override
                public int size() {
                    return entries.size();
                }

                @Override
                public boolean isEmpty() {
                    return entries.isEmpty();
                }

                @Override
                public boolean contains(Object o) {
                    return entries.contains(o);
                }

                @Override
                public Iterator<Entry<String, String>> iterator() {
                    return new Iterator<Entry<String, String>>() {

                        final Iterator<Entry<Object, Object>> it = entries.iterator();

                        @Override
                        public boolean hasNext() {
                            return it.hasNext();
                        }

                        @Override
                        public Entry<String, String> next() {
                            return new Entry<String, String>() {

                                final Entry<Object, Object> entry = it.next();

                                @Override
                                public String getKey() {
                                    return (String) entry.getKey();
                                }

                                @Override
                                public String getValue() {
                                    return (String) entry.getValue();
                                }

                                @Override
                                public String setValue(String value) {
                                    changed = true;
                                    return (String) entry.setValue(value);
                                }
                            };
                        }
                    };
                }

                @Override
                public Object[] toArray() {
                    return entries.toArray();
                }

                @Override
                public <T> T[] toArray(T[] a) {
                    return entries.toArray(a);
                }

                @Override
                public boolean add(Entry<String, String> objectObjectEntry) {
                    changed = true;
                    throw new UnsupportedOperationException();
                }

                @Override
                public boolean remove(Object o) {
                    changed = true;
                    return entries.remove(o);
                }

                @Override
                public boolean containsAll(Collection<?> c) {
                    return entries.containsAll(c);
                }

                @Override
                public boolean addAll(Collection<? extends Entry<String, String>> c) {
                    throw new UnsupportedOperationException();
                }

                @Override
                public boolean retainAll(Collection<?> c) {
                    changed = true;
                    return entries.retainAll(c);
                }

                @Override
                public boolean removeAll(Collection<?> c) {
                    changed = true;
                    return entries.removeAll(c);
                }

                @Override
                public void clear() {
                    changed = true;
                    entries.clear();
                }
            };
        }
    }
}
