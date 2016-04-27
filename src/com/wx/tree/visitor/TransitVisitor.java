//package com.wx.tree.visitor;
//
//import com.wx.grammar.symbol.Symbol;
//import com.wx.tree.Tree;
//import com.wx.tree.Tree.Leaf;
//import com.wx.tree.Tree.Node;
//import com.wx.tree.TreeVisitor;
//
///**
// *
// * @author Raffaele Canale (raffaelecanale@gmail.com)
// * @version 0.1
// */
//public class TransitVisitor<N, L> implements TreeVisitor.VoidVisitor<N, L> {
//
//    private final Node<Symbol, TreeVisitor.VoidVisitor<N, L>> transitTree;
//    private final Symbol nextExpectedElement;
//
//    public TransitVisitor(Node<Symbol, TreeVisitor.VoidVisitor<N, L>> nextTransitTree) {
//        this.transitTree = nextTransitTree;
//        this.nextExpectedElement = transitTree.getElement();
//    }
//
//    @Override
//    public void visitV(Leaf<N, L> leaf) {
//    }
//
//    @Override
//    public void visitV(Node<N, L> node) {
//        if (nextExpectedElement == null || node.getElement().equals(nextExpectedElement)) {
//            for (Tree<Symbol, TreeVisitor.VoidVisitor<N, L>> transit : transitTree.getNodes()) {
//                transit.accept(new TransitNodeFoundVisitor(node));
//            }
//            return;
//        }
//
//        Tree<N, L>[] nodes = node.getNodes();
//        for (Tree<N, L> n : nodes) {
//            n.accept(this);
//        }
//    }
//
//    private class TransitNodeFoundVisitor implements TreeVisitor.VoidVisitor<Symbol, TreeVisitor.VoidVisitor<N, L>> {
//
//        private final Node<N, L> tree;
//
//        public TransitNodeFoundVisitor(Node<N, L> tree) {
//            this.tree = tree;
//        }
//
//        @Override
//        public void visitV(Leaf<Symbol, TreeVisitor.VoidVisitor<N, L>> leaf) {
//            // Is a visitor
//            tree.accept(leaf.getElement());
//        }
//
//        @Override
//        public void visitV(Node<Symbol, TreeVisitor.VoidVisitor<N, L>> node) {
////            System.out.println("Ok, let's go on with that tree: ");
////            new PrintTree().print(node);
////            for (Tree<Object> n : node.getNodes()) {
////                tree.accept(new TransitVisitor(n));
////            }
//
//            for (Tree<N, L> t : tree.getNodes()) {
//                t.accept(new TransitVisitor<>(node));
//            }
//        }
//    }
//
//}
