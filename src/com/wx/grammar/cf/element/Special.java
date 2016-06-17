package com.wx.grammar.cf.element;

import com.wx.grammar.cnf.CNFGrammar;
import com.wx.grammar.symbol.GeneratedSymbol;
import com.wx.grammar.symbol.Symbol;
import com.wx.lexer.tokens.EOLToken;
import com.wx.lexer.tokens.SpecialCharToken;

/**
 *
 * @author Raffaele Canale (raffaelecanale@gmail.com)
 * @version 0.1
 */
public class Special implements Element {

    private final char value;

    public Special(char value) {
        this.value = value;
    }

    @Override
    public Symbol[] normalize(CNFGrammar.Builder builder) {
        Symbol symbol = new GeneratedSymbol("" + value);
        
        builder.addTerminalRule(symbol,
                value == '\n' ? new EOLToken(null)
                : new SpecialCharToken(value, null));

        return new Symbol[]{symbol};
    }

}
