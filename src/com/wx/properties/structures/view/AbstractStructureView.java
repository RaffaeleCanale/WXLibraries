package com.wx.properties.structures.view;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by canale on 03.05.16.
 */
public abstract class AbstractStructureView implements StructureView {

    private final String headerKey;
    protected final Map<String, String> map;

    transient volatile Set<Entry<String, String>> entrySet;

    protected AbstractStructureView(String headerKey, Map<String, String> map) {
        this.headerKey = Objects.requireNonNull(headerKey);
        this.map = Objects.requireNonNull(map);

//        if (isKeyInView(headerKey)) {
//            throw new IllegalArgumentException("Header key cannot be included in structure view");
//        }
    }

    protected abstract boolean isKeyInView(String realKey);

    protected abstract String realKeyToView(String realKey);

    protected abstract String viewKeyToReal(String viewKey);

    @Override
    public void setHeader(String value) {
        map.put(headerKey, Objects.requireNonNull(value));
    }

    @Override
    public String getHeader() {
        return map.get(headerKey);
    }

    @Override
    public void removeHeader() {
        map.remove(headerKey);
    }

    @Override
    public int size() {
        return (int) getRealKeysStream().count();
    }

    @Override
    public boolean isEmpty() {
        return !getRealKeysStream().findAny().isPresent();
    }

    @Override
    public boolean containsKey(Object key) {
        return map.containsKey(viewKeyToReal((String) key));
    }

    @Override
    public boolean containsValue(Object value) {
        return getRealKeysStream()
                .map(map::get)
                .anyMatch(v -> Objects.equals(value, v));
    }

    @Override
    public String get(Object key) {
        return map.get(viewKeyToReal((String) key));
    }

    @Override
    public String put(String key, String value) {
        return map.put(viewKeyToReal(key), value);
    }

    @Override
    public String remove(Object key) {
        return map.remove(viewKeyToReal((String) key));
    }

    @Override
    public void putAll(Map<? extends String, ? extends String> m) {
        for (Entry<? extends String, ? extends String> entry : m.entrySet()) {
            put(entry.getKey(), entry.getValue());
        }
    }

    @Override
    public void clear() {
        Iterator<String> it = map.keySet().iterator();
        while (it.hasNext()) {
            if (isKeyInView(it.next())) {
                it.remove();
            }
        }
    }

    @Override
    public Set<String> keySet() {
        return new AbstractSet<String>() {
            @Override
            public Iterator<String> iterator() {
                return new Iterator<String>() {

                    final Iterator<Entry<String, String>> it = entrySet().iterator();

                    @Override
                    public boolean hasNext() {
                        return it.hasNext();
                    }

                    @Override
                    public String next() {
                        return it.next().getKey();
                    }

                    @Override
                    public void remove() {
                        it.remove();
                    }
                };
            }

            @Override
            public boolean contains(Object o) {
                return AbstractStructureView.this.containsKey(o);
            }

            @Override
            public boolean remove(Object o) {
                return AbstractStructureView.this.remove(o) != null;
            }

            @Override
            public void clear() {
                AbstractStructureView.this.clear();
            }

            @Override
            public int size() {
                return AbstractStructureView.this.size();
            }
        };
    }

    @Override
    public Collection<String> values() {
        return new AbstractCollection<String>() {
            @Override
            public Iterator<String> iterator() {
                return new Iterator<String>() {

                    final Iterator<Entry<String, String>> it = entrySet().iterator();

                    @Override
                    public boolean hasNext() {
                        return it.hasNext();
                    }

                    @Override
                    public String next() {
                        return it.next().getValue();
                    }

                    @Override
                    public void remove() {
                        it.remove();
                    }
                };
            }

            @Override
            public boolean contains(Object o) {
                return AbstractStructureView.this.containsValue(o);
            }

            @Override
            public boolean remove(Object o) {
                Optional<BackedEntry> target = getRealKeysStream().map(BackedEntry::new)
                        .filter(e -> e.getValue().equals(o))
                        .findAny();
                if (target.isPresent()) {
                    target.get().remove();
                    return true;
                }
                return false;
            }

            @Override
            public void clear() {
                AbstractStructureView.this.clear();
            }

            @Override
            public int size() {
                return AbstractStructureView.this.size();
            }
        };
    }

    @Override
    public Set<Entry<String, String>> entrySet() {
        if (entrySet == null) {
            entrySet = new AbstractSet<Entry<String, String>>() {
                @Override
                public Iterator<Entry<String, String>> iterator() {
                    return new Iterator<Entry<String, String>>() {

                        final Iterator<BackedEntry> it = collectEntries().iterator();
                        BackedEntry lastEntry;

                        @Override
                        public boolean hasNext() {
                            return it.hasNext();
                        }

                        @Override
                        public Entry<String, String> next() {
                            lastEntry = it.next();
                            return lastEntry;
                        }

                        @Override
                        public void remove() {
                            it.remove();
                            lastEntry.remove();
                        }
                    };
                }

                @Override
                public int size() {
                    return AbstractStructureView.this.size();
                }

                @Override
                public boolean contains(Object o) {
                    if (o instanceof Entry<?, ?>) {
                        Entry<?, ?> entry = (Entry<?, ?>) o;
                        String value = AbstractStructureView.this.get(entry.getKey());
                        return value != null && value.equals(entry.getValue());
                    }

                    return false;
                }

                @Override
                public boolean remove(Object o) {
                    if (o instanceof Entry<?, ?>) {
                        Entry<?, ?> entry = (Entry<?, ?>) o;
                        return AbstractStructureView.this.remove(entry.getKey(), entry.getValue());
                    }

                    return false;
                }

                @Override
                public void clear() {
                    AbstractStructureView.this.clear();
                }
            };
        }
        return entrySet;
    }

    private Collection<BackedEntry> collectEntries() {
        return getRealKeysStream()
                .map(BackedEntry::new)
                .collect(Collectors.toCollection(ArrayList::new));
    }

    private Stream<String> getRealKeysStream() {
        return map.keySet().stream()
                .filter(this::isKeyInView);
    }

    private class BackedEntry implements Entry<String, String> {

        private final String realKey;

        private BackedEntry(String realKey) {
            this.realKey = realKey;
        }

        @Override
        public String getKey() {
            return realKeyToView(realKey);
        }

        @Override
        public String getValue() {
            return map.get(realKey);
        }

        @Override
        public String setValue(String value) {
            return map.put(realKey, value);
        }

        private void remove() {
            map.remove(realKey);
        }
    }

}
