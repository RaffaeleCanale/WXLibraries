//package com.wx.tree.visitor;
//
//import com.wx.tree.Leaf;
//import com.wx.tree.Node;
//import com.wx.tree.Tree;
//import static java.lang.Integer.max;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.function.Function;
//
///**
// *
// * @author Raffaele Canale (raffaelecanale@gmail.com)
// * @version 0.1
// */
//public class TreeSize<Type> {
//
//    private final int maxWidth;
//    private final int maxDepth;
//
//    public TreeSize(Tree tree) {
//        this(t -> 1, tree);
//    }
//    
//    public TreeSize(Function<Type, Integer> widthFunction, Tree tree) {
//        Builder builder = new Builder(widthFunction);
//        builder.process(tree);
//        
//        maxDepth = builder.maxLevel;
//        maxWidth = builder.width;
//    }
//
//    public int getMaxDepth() {
//        return maxDepth;
//    }
//
//    public int getMaxWidth() {
//        return maxWidth;
//    }
//
//    private class Builder {
//
//        private final Function<Type, Integer> widthFunction;
//        private final List<Integer> widths = new ArrayList<>();
//        private int maxLevel;
//        private int width;
//
//        public Builder(Function<Type, Integer> widthFunction) {
//            this.widthFunction = widthFunction;
//        }
//
//        public void process(Tree tree) {
//            tree.accept(new Visitor(0));
//            width = widths.stream().reduce(Integer::max).get();            
//        }
//
//        private void add(int level, Tree<Type> tree) {
//            while (widths.size() <= level) {
//                widths.add(0);
//            }
//            widths.set(level, widths.get(level) + widthFunction.apply(tree.getElement()));
//        }
//
//        private final class Visitor implements VoidTreeVisitor<Type> {
//
//            private final int currentLevel;
//
//            public Visitor(int currentLevel) {
//                this.currentLevel = currentLevel;
//                maxLevel = max(maxLevel, currentLevel);
//            }
//
//            @Override
//            public void visitV(Leaf<Type> leaf) {
//                add(currentLevel, leaf);
//            }
//
//            @Override
//            public void visitV(Node<Type> node) {
//                add(currentLevel, node);
//
//                Visitor visitor = new Visitor(currentLevel + 1);
//                for (Tree<Type> n : node.getNodes()) {
//                    n.accept(visitor);
//                }
//            }
//
//        }
//    }
//
//}
