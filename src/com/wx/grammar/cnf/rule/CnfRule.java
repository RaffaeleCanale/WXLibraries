package com.wx.grammar.cnf.rule;

import com.wx.grammar.symbol.Symbol;


/**
 * Represents an abstract rule named by its symbol
 * 
 * @author Raffaele Canale (raffaelecanale@gmail.com)
 * @version 0.1
 */
public abstract class CnfRule {
    
    private final Symbol symbol;

    /**
     * 
     * @param symbol Symbol of the rule
     */
    public CnfRule(Symbol symbol) {
        this.symbol = symbol;
    }
    
    /**
     * 
     * @return This rule's symbol
     */
    public Symbol getSymbol() {
        return symbol;
    }

}
