//package com.wx.grammar.cf;
//
//import com.wx.grammar.GrammarException;
//import com.wx.grammar.ast.ASTVisitors.Visitor;
//import com.wx.grammar.cnf.CNFTreeParser;
//import com.wx.grammar.symbol.GeneratedSymbol;
//import com.wx.grammar.symbol.Symbol;
//import com.wx.lexer.tokens.Token;
//import com.wx.tree.Tree;
//import com.wx.tree.Tree.Leaf;
//import com.wx.tree.Tree.Node;
//import com.wx.util.future.IoIterator;
//
//import java.io.IOException;
//import java.util.Arrays;
//import java.util.LinkedList;
//import java.util.List;
//
///**
// *
// * @author Raffaele Canale (raffaelecanale@gmail.com)
// * @version 0.1
// */
//public class CFTreeParser {
//
////    private final CFGrammar grammar;
////    private final CNFGrammar normalizedGrammar;
//    private final CNFTreeParser cnfTreeParser;
//
//    public CFTreeParser(CFGrammar grammar) throws GrammarException {
////        this.grammar = grammar;
////        normalizedGrammar = grammar.normalize();
//        cnfTreeParser = new CNFTreeParser(grammar.normalize());
//    }
//
//    public Tree<Symbol, Token> parse(IoIterator<Token> tokens) throws GrammarException, IOException {
//        Tree<Symbol, Token> cnfTree = cnfTreeParser.parse(tokens);
//
////        return grammar.getStartProduction().normalize(cnfTree, grammar);
//        return cnfTree.accept(new TreeNormalizer());
//    }
//
//    private static class TreeNormalizer implements Visitor<Tree<Symbol, Token>> {
//
//        @Override
//        public Tree<Symbol, Token> visit(Leaf<Symbol, Token> leaf) {
//            return leaf;
//        }
//
//        @Override
//        public Tree<Symbol, Token> visit(Node<Symbol, Token> node) {
//            List<Tree<Symbol, Token>> result = node.accept(new TreeNormalizer0());
//            assert result.size() == 1;
//            return result.get(0);
//        }
//    }
//    private static class TreeNormalizer0 implements Visitor<List<Tree<Symbol, Token>>> {
//
//        @Override
//        public List<Tree<Symbol, Token>> visit(Leaf<Symbol, Token> leaf) {
//            return Arrays.asList(leaf);
//        }
//
//        @Override
//        public List<Tree<Symbol, Token>> visit(Node<Symbol, Token> node) {
//            Tree<Symbol, Token>[] nodes = node.getNodes();
//            List<Tree<Symbol, Token>> newNodes = new LinkedList<>();
//
//            for (Tree<Symbol, Token> n : nodes) {
//                newNodes.addAll(n.accept(this));
//            }
//
//            if (node.getElement().getClass() == GeneratedSymbol.class) {
//                return newNodes;
//            } else {
//                Tree<Symbol, Token>[] nodesArray = new Tree[newNodes.size()];
//                newNodes.toArray(nodesArray);
////
//                return Arrays.asList(new Node<>(node.getElement(), nodesArray));
//            }
//        }
//    }
//}
