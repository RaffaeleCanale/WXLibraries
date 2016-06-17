package com.wx.grammar;

import com.wx.grammar.cnf.CNFGrammar;
import com.wx.grammar.cf.element.Concatenation;
import com.wx.grammar.symbol.Symbol;
import com.wx.grammar.cf.element.Element;
import java.util.Arrays;

/**
 *
 * @author Raffaele Canale (raffaelecanale@gmail.com)
 * @version 0.1
 */
public class zGrammarBuilder {
    
    private final CNFGrammar.Builder builder;
    
    public zGrammarBuilder(Symbol startSymbol) {
        builder = new CNFGrammar.Builder(startSymbol);
    }
    
    public Symbol[] declareRule(Symbol sym, Element... rules) {
        Element rule = rules.length == 1 ? rules[0]
                : new Concatenation(rules);
        
        Symbol[] normalized = rule.normalize(builder);
        builder.addProductiveRule(sym, normalized);
        
        return normalized;
    }
    
    public CNFGrammar toCnf() throws GrammarException {
        return builder.build();
    }

}
