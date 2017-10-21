package com.wx.util.options;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * @author Raffaele Canale (<a href="mailto:raffaelecanale@gmail.com?subject=JSync">raffaelecanale@gmail.com</a>)
 * @version 0.1 - created on 12.10.17.
 */
public class MutableOptions {

    private final Map<String, Object> options;
    private boolean hasChanged = false;

    public MutableOptions(Map<String, Object> options) {
        this.options = new HashMap<>(options);
    }

    public <E> E get(String key) {
        return (E) options.get(key);
    }

    public <E> E get(String key, E def) {
        return (E) options.getOrDefault(key, def);
    }

    public MutableOptions set(String key, Object value) {
        if (!Objects.equals(options.get(key), value)) {
            hasChanged = true;
        }

        options.put(key, value);

        return this;
    }

    public MutableOptions remove(String key) {
        Object oldValue = options.remove(key);

        if (oldValue != null) {
            hasChanged = true;
        }

        return this;
    }

    public boolean hasChanged() {
        return hasChanged;
    }

    public Set<String> keySet() {
        return options.keySet();
    }

    public Options toOptions() {
        return new Options(options);
    }

}
