package com.wx.grammar.symbol;

import com.wx.grammar.cf.element.Element;
import com.wx.grammar.cnf.CNFGrammar;

/**
 *
 * This class represents a rule symbol.
 * A symbol is simply a reference
 * 
 * @author Raffaele Canale (raffaelecanale@gmail.com)
 * @version 0.1
 */
public class Symbol implements Element {

    @Override
    public Symbol[] normalize(CNFGrammar.Builder builder) {
        return new Symbol[]{this};
    }


}
