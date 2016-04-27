package com.wx.tree.visitor;

import com.wx.tree.Tree;

import java.util.stream.Stream;

/**
 * This interface describes a map-reduce that computes a value R for all the leaves/nodes and reduces them into a single
 * return value.
 * <p>
 * Created on 13/04/2016
 *
 * @author Raffaele Canale (raffaelecanale@gmail.com)
 * @version 0.5
 */
public interface MapReduceOperation<R, T> {

    /**
     * Compute the value of the given leaf.
     *
     * @param leaf Leaf to compute
     *
     * @return Value for this leaf
     */
    R compute(Tree.Leaf<T> leaf);

    /**
     * Compute the value of the given node.
     *
     * @param node Node to compute
     *
     * @return Value for this node
     */
    R compute(Tree.Node<T> node);

    /**
     * Reduce the result of a node with the result of all its children.
     * <p>
     * <b>Note:</b> The children's result are computed using lazy-streams, thus, if not consumed, those computations
     * (ie. the recursion) will not take place.
     *
     * @param parent   Value of the parent node
     * @param children Values for the children of the parent node
     *
     * @return The combined value for the parent node and its children
     */
    R reduce(R parent, Stream<R> children);

}
