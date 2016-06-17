package com.wx.grammar.cf.element;

import com.wx.grammar.cnf.CNFGrammar;
import com.wx.grammar.symbol.Symbol;
import com.wx.grammar.symbol.GeneratedSymbol;
import com.wx.lexer.tokens.NumberToken;

/**
 *
 * @author Raffaele Canale (raffaelecanale@gmail.com)
 * @version 0.1
 */
public class NumberLit implements Element {

    @Override
    public Symbol[] normalize(CNFGrammar.Builder builder) {
        Symbol symbol = new GeneratedSymbol("number");
        
        builder.addTerminalRule(symbol, new NumberToken(0.0, null));
        
        return new Symbol[]{symbol};
    }

}
