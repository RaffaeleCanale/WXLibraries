//package com.wx.tree.visitor;
//
//import com.wx.tree.Leaf;
//import com.wx.tree.Node;
//import com.wx.tree.Tree;
//import java.util.Map;
//import java.util.Objects;
//
///**
// *
// * @author Raffaele Canale (raffaelecanale@gmail.com)
// * @version 0.1
// */
//public class SymbolTreeVisitor implements TreeVisitor<Void, Object, Object>{
//    
//    private final Map<Object, TreeVisitor<Boolean, Object>> visitorsMap;
//
//    public SymbolTreeVisitor(Map<Object, TreeVisitor<Boolean, Object>> visitorsMap) {
//        this.visitorsMap = visitorsMap;
//    }
//
//    @Override
//    public Void visit(Leaf<Object> leaf) {
//        return null;
//    }
//
//    @Override
//    public Void visit(Node<Object> node) {
//        TreeVisitor<Boolean, Object> visitor = visitorsMap.get(node.getElement());
//        if (visitor != null) {
//            Boolean stopRec = node.accept(visitor);
//            if (Objects.equals(Boolean.TRUE, stopRec)) {
//                return null; // Stop recursion
//            }
//        }
//        
//        
//        Tree<Object>[] nodes = node.getNodes();
//        for (Tree<Object> child : nodes) {
//            child.accept(this);
//        }
//        return null;
//    }
//    
//    
//
//}
