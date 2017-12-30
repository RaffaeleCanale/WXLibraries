package com.wx.util.options;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static com.wx.util.collections.CollectionsUtil.safe;

/**
 * @author Raffaele Canale (<a href="mailto:raffaelecanale@gmail.com?subject=JSync">raffaelecanale@gmail.com</a>)
 * @version 0.1 - created on 27.09.17.
 */
public class Options {

    public static final Options EMPTY = new Options(Collections.emptyMap());

    private final Map<String, Object> options;

    public Options(Map<String, Object> options) {
        this.options = safe(options);
    }

    public boolean has(String key) {
        return options.containsKey(key);
    }

    public <E> E get(String key) {
        return (E) options.get(key);
    }

    public <E> E get(String key, E def) {
        return (E) options.getOrDefault(key, def);
    }

    public Set<String> keySet() {
        return options.keySet();
    }

    public MutableOptions toMutable() {
        return new MutableOptions(options);
    }

    public Map<String, Object> toMap() {
        Map<String, Object> result = new HashMap<>(options.size());
        options.forEach((key, value) -> {
            if (value instanceof Options) {
                value = ((Options) value).toMap();
            }

            result.put(key, value);
        });

        return result;
    }
}
