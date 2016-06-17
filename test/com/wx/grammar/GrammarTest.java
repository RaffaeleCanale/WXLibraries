
package com.wx.grammar;

import com.wx.grammar.symbol.Symbol;
import com.wx.grammar.cnf.CNFGrammar;
import com.wx.lexer.tokens.SpecialCharToken;
import org.junit.Test;

/**
 *
 * @author Raffaele Canale (raffaelecanale@gmail.com)
 */
public class GrammarTest {
    

    @Test(expected = GrammarException.class)
    public void testUndeclaredStartSymbol() throws GrammarException {
        Symbol startSymbol = new Symbol();
        Symbol ruleSymbol = new Symbol();
                
        CNFGrammar.Builder builder = new CNFGrammar.Builder(startSymbol);
        builder.addTerminalRule(ruleSymbol, new SpecialCharToken('c'));
        
        builder.build();
    }
    
    @Test(expected = GrammarException.class)
    public void testUndeclaredSymbol() throws GrammarException {
        Symbol startSymbol = new Symbol();
        Symbol undeclaredSymbol = new Symbol();
        Symbol otherSymbol = new Symbol();
        
        CNFGrammar.Builder builder = new CNFGrammar.Builder(startSymbol);
        builder.addProductiveRule(startSymbol, undeclaredSymbol, otherSymbol);
        builder.addTerminalRule(otherSymbol, new SpecialCharToken('c'));
        
        builder.build();
    }
    
    @Test(expected = GrammarException.class)
    public void testUnusedSymbol() throws GrammarException {
        Symbol startSymbol = new Symbol();
        Symbol unusedSymbol = new Symbol();
        Symbol otherSymbol = new Symbol();
        
        CNFGrammar.Builder builder = new CNFGrammar.Builder(startSymbol);
        builder.addProductiveRule(startSymbol, otherSymbol);
        builder.addTerminalRule(otherSymbol, new SpecialCharToken('c'));
        builder.addTerminalRule(unusedSymbol, new SpecialCharToken('c'));
        
        builder.build();
    }
    
    @Test(expected = GrammarException.class)
    public void testTooManyTerminals() throws GrammarException {
        Symbol startSymbol = new Symbol();
        Symbol duplicateSymbol = new Symbol();
        Symbol otherSymbol = new Symbol();
        
        CNFGrammar.Builder builder = new CNFGrammar.Builder(startSymbol);
        builder.addProductiveRule(startSymbol, duplicateSymbol, otherSymbol);
        builder.addTerminalRule(otherSymbol, new SpecialCharToken('c'));
        builder.addTerminalRule(duplicateSymbol, new SpecialCharToken('c'));
        builder.addTerminalRule(duplicateSymbol, new SpecialCharToken('o'));
        
        builder.build();
    }
    
}
