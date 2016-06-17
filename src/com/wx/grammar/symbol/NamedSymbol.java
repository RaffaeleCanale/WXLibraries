package com.wx.grammar.symbol;

/**
 * Symbol attached with a name
 * 
 * @author Raffaele Canale (raffaelecanale@gmail.com)
 * @version 0.1
 */
public class NamedSymbol extends Symbol {
    
    private final String name;

    public NamedSymbol(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }

}
