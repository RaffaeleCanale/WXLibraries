package com.wx.grammar.cf.element;

import com.wx.grammar.cnf.CNFGrammar;
import com.wx.grammar.symbol.Symbol;
import com.wx.grammar.symbol.GeneratedSymbol;
import com.wx.lexer.tokens.KeyWordToken;

/**
 *
 * @author Raffaele Canale (raffaelecanale@gmail.com)
 * @version 0.1
 */
public class Variable implements Element {
    
    private final String value;

    public Variable(String value) {
        this.value = value;
    }

    @Override
    public Symbol[] normalize(CNFGrammar.Builder builder) {
        Symbol symbol = new GeneratedSymbol("" + value);
//        System.out.println("AH");
        builder.addTerminalRule(symbol, new KeyWordToken(value, null));
//        System.out.println("HA");
        return new Symbol[]{symbol};
    }

}
