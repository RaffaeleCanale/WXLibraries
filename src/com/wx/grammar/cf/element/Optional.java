package com.wx.grammar.cf.element;

import com.wx.grammar.cnf.CNFGrammar;
import com.wx.grammar.symbol.Symbol;
import com.wx.grammar.symbol.GeneratedSymbol;

/**
 *
 * @author Raffaele Canale (raffaelecanale@gmail.com)
 * @version 0.1
 */
public class Optional implements Element {
    
        
    private final Element rule;

    public Optional(Element rule) {
        this.rule = rule;
    }
    
    @Override
    public Symbol[] normalize(CNFGrammar.Builder builder) {
        /*
        R := (A B C)?
        
        is equivalent to
        
        R := O
        O := (A B C)
        O := €
        */
        Symbol[] ruleSymbols = rule.normalize(builder);
        
        Symbol optional = new GeneratedSymbol("optional");
        
        builder.addProductiveRule(optional, ruleSymbols);        
        builder.addEpsilonSymbol(optional);
        
        return new Symbol[]{optional};        
    }

}
