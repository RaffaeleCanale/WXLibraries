package com.wx.grammar.cf.element;

import com.wx.grammar.cnf.CNFGrammar;
import com.wx.grammar.symbol.Symbol;
import com.wx.grammar.symbol.GeneratedSymbol;
import com.wx.lexer.tokens.StringToken;

/**
 *
 * @author Raffaele Canale (raffaelecanale@gmail.com)
 * @version 0.1
 */
public class Litteral implements Element {
    
//    private final String value;

    public Litteral() {
//        this.value = value;
    }

    @Override
    public Symbol[] normalize(CNFGrammar.Builder builder) {
        Symbol symbol = new GeneratedSymbol("litteral");
        
        builder.addTerminalRule(symbol, new StringToken(null, null));
        
        return new Symbol[]{symbol};
    }
}
