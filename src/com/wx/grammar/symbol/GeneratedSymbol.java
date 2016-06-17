package com.wx.grammar.symbol;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Raffaele Canale (raffaelecanale@gmail.com)
 * @version 0.1
 */
public class GeneratedSymbol extends NamedSymbol {
    
    private static final Map<String, Integer> ids = new HashMap<>();
    
    public static void resetIds() {
        ids.clear();
    }
    
    private static String getName(String name) {
        Integer id = ids.get(name);
        if (id == null) {
            id = 0;
        }

        ids.put(name, id + 1);
        
        return name + "_G" + id;
    }

    public GeneratedSymbol(String name) {        
        super(getName(name));
    }
}
