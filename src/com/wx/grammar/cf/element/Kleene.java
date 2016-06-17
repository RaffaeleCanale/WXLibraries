package com.wx.grammar.cf.element;

import com.wx.grammar.cnf.CNFGrammar;
import com.wx.grammar.symbol.GeneratedSymbol;
import com.wx.grammar.symbol.Symbol;

/**
 *
 * @author Raffaele Canale (raffaelecanale@gmail.com)
 * @version 0.1
 */
public class Kleene implements Element {
    
    private final Element rule;

    public Kleene(Element rule) {
        this.rule = rule;
    }

    @Override
    public Symbol[] normalize(CNFGrammar.Builder builder) {
        /*
        R := (A B C)*
        
        is equivalent to
        
        R := K
        K := A B C K
        K := €
        
        */
        Symbol K = new GeneratedSymbol("kleene");
        Symbol[] subSymbols = rule.normalize(builder);
        Symbol[] KSymbols = new Symbol[subSymbols.length + 1];
        
        System.arraycopy(subSymbols, 0, KSymbols, 0, subSymbols.length);
        KSymbols[KSymbols.length - 1] = K;
        
        builder.addProductiveRule(K, KSymbols);
        builder.addEpsilonSymbol(K);
        
        return new Symbol[]{K};
    }
    
    
}
