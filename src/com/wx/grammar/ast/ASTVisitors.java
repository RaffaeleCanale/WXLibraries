package com.wx.grammar.ast;

import com.wx.grammar.symbol.Symbol;
import com.wx.lexer.tokens.Token;
import com.wx.tree.Tree;
import com.wx.tree.visitor.TreeVisitor;

/**
 *
 * @author Raffaele Canale (raffaelecanale@gmail.com)
 * @version 0.1
 */
public class ASTVisitors {

//    public static interface Visitor<R> extends TreeVisitor<R, Symbol, Token> {
//    }
//
//    @FunctionalInterface
//    public static interface VN extends TreeVisitor<T> {
//        @Override
//        public default void visitV(Tree.Leaf<Symbol, Token> leaf) {
//            throw new RuntimeException("Expected a node but got a leaf: " + leaf.getElement());
//        }
//    }
//
//    @FunctionalInterface
//    public static interface VL extends TreeVisitor.VoidVisitor<Symbol, Token> {
//        @Override
//        public default void visitV(Tree.Node<Symbol, Token> node) {
//            throw new RuntimeException("Expected a leaf but got a node: " + node.getElement());
//        }
//    }
//
//    @FunctionalInterface
//    public static interface L<R> extends TreeVisitor<R, Symbol, Token> {
//        @Override
//        public default R visit(Tree.Node<Symbol, Token> node) {
//            throw new RuntimeException("Expected a leaf but got a node: " + node.getElement());
//        }
//    }
//
//        
//    public static class Leaf extends ASTree implements TreeInterface.Leaf {
//        private final Token token;
//
//        public Leaf(Token token) {
//            this.token = token;
//        }
//
//        @Override
//        public Token getElement() {
//            return token;
//        }
//    }
//
//    public static class Node extends ASTree implements TreeInterface.Node {
//
//        private final Symbol symbol;
//        private final ASTree[] nodes;
//
//        public Node(Symbol symbol, ASTree[] nodes) {
//            this.symbol = symbol;
//            this.nodes = nodes;
//        }
//
//        @Override
//        public Symbol getElement() {
//            return symbol;
//        }
//
//        @Override
//        public ASTree[] getNodes() {
//            return nodes;
//        }
//
//        @Override
//        public String toString() {
//            return Objects.toString(symbol);
//        }
//    }
}
