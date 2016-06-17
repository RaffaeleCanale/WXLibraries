package com.wx.grammar.cf.element;

import com.wx.grammar.cnf.CNFGrammar;
import com.wx.grammar.symbol.Symbol;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author Raffaele Canale (raffaelecanale@gmail.com)
 * @version 0.1
 */
public class Concatenation implements Element  {
    
    private final Element rules[];

    public Concatenation(Element... rules) {
        this.rules = rules;
    }

    @Override
    public Symbol[] normalize(CNFGrammar.Builder builder) {
        List<Symbol> concat = new ArrayList<>();
        
        for (Element rule : rules) {
            concat.addAll(Arrays.asList(rule.normalize(builder)));
        }
        
        return concat.toArray(new Symbol[concat.size()]);
    }

//    @Override
//    public Tree<Object> normalize(Tree<Object> tree, CFGrammar grammar) {
//        assert tree.getClass().equals(Node.class);
//        
//        Tree<Object>[] nodes = ((Node<Object>) tree).getNodes();
//        assert nodes.length == rules.length;
//        
//        Tree<Object>[] newNodes = new Tree[nodes.length];
//        
//        for (int i = 0; i < nodes.length; i++) {
////            System.out.println("LOL"  +  rules[i].normalize(nodes[i], grammar).getClass());
//            newNodes[i] = rules[i].normalize(nodes[i], grammar);
//        }
//        
//        return new Node<>(tree.getElement(), newNodes);
//    }
}
