package com.wx.grammar.cf.element;

import com.wx.grammar.cnf.CNFGrammar;
import com.wx.grammar.symbol.Symbol;

/**
 *
 * @author Raffaele Canale (raffaelecanale@gmail.com)
 * @version 0.1
 */
public interface Element {
        
    public Symbol[] normalize(CNFGrammar.Builder builder);
    
//    public default Tree<Object> normalize(Tree<Object> tree, CFGrammar grammar) {        
//        return tree;
//    }
    
}
