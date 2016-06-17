package com.wx.grammar.cnf.rule;

import com.wx.grammar.symbol.Symbol;
import com.wx.lexer.tokens.Token;


/**
 * This class represents a terminal rule.
 * 
 * @author Raffaele Canale (raffaelecanale@gmail.com)
 * @version 0.1
 */
public class TerminalRule extends CnfRule {

    private final Token expectedToken;

    /**
     * 
     * @param symbol Rule's symbol
     * @param expectedToken Terminal token of this rule
     */
    public TerminalRule(Symbol symbol, Token expectedToken) {
        super(symbol);
        this.expectedToken = expectedToken;
    }

    /**
     * 
     * @return The token of this rule
     */
    public Token getExpectedToken() {
        return expectedToken;
    }
    
    @Override
    public String toString() {
        return (getSymbol() + " := " + expectedToken).replaceAll("\n", "\\\\n");
    }
}
