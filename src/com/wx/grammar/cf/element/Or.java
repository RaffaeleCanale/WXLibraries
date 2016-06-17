package com.wx.grammar.cf.element;

import com.wx.grammar.cnf.CNFGrammar;
import com.wx.grammar.symbol.GeneratedSymbol;
import com.wx.grammar.symbol.Symbol;

/**
 *
 * @author Raffaele Canale (raffaelecanale@gmail.com)
 * @version 0.1
 */
public class Or implements Element {
    
    private final Element[] rules;

    public Or(Element... rules) {
        this.rules = rules;
    }

    @Override
    public Symbol[] normalize(CNFGrammar.Builder builder) {
        Symbol orSymbol = new GeneratedSymbol("or");

        for (Element subRule : rules) {
            Symbol[] subSymbols = subRule.normalize(builder);
            builder.addProductiveRule(orSymbol, subSymbols);
        }
        
        return new Symbol[]{orSymbol};
    }

//    @Override
//    public Tree<Object> normalize(Tree<Object> tree, CFGrammar grammar) {
//        assert tree.getClass().equals(Node.class);
//        
//        System.out.println("This is " + tree.getElement());
//        Tree<Object>[] nodes = ((Node<Object>) tree).getNodes();
//        assert nodes.length == 1;
//        
//        return nodes[0];
//    }
    
    
}
