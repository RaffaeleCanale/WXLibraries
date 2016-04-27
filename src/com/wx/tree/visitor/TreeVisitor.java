package com.wx.tree.visitor;

import com.wx.tree.Tree;

import java.util.stream.Stream;

/**
 * This can be used as a depth-first tree visitor.
 *
 * @author Raffaele Canale (raffaelecanale@gmail.com)
 * @version 0.5
 */
public abstract class TreeVisitor<T> implements MapReduceOperation<Void, T> {


    @Override
    public final Void compute(Tree.Leaf<T> leaf) {
        visit(leaf);
        return null;
    }

    @Override
    public final Void compute(Tree.Node<T> node) {
        visit(node);
        return null;
    }

    @Override
    public final Void reduce(Void parent, Stream<Void> leaves) {
        leaves.count(); // Force stream consumption
        return null;
    }

    public abstract void visit(Tree.Leaf<T> leaf);
    public abstract void visit(Tree.Node<T> node);
}
