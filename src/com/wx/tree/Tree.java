package com.wx.tree;

import com.wx.tree.visitor.MapReduceOperation;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.stream.Stream;

import static com.wx.util.collections.CollectionsUtil.safe;

/**
 * This class represents an immutable tree where both the nodes and leaves may contain a value of the same type.
 *
 * @author Raffaele Canale (raffaelecanale@gmail.com)
 * @version 0.5
 */
public abstract class Tree<T> {

    /**
     * Create a tree builder.
     *
     * @param root Value contained in the tree-root
     * @param <T>  Type of the tree values
     *
     * @return The tree-root builder
     */
    public static <T> TreeBuilder.NodeBuilder<T> builder(T root) {
        return TreeBuilder.fromRoot(root);
    }

    private final T element;

    private Tree(T element) {
        this.element = element;
    }

    /**
     * @return This tree value
     */
    public T getElement() {
        return element;
    }

    /**
     * @return {@code true} if this is a leaf
     */
    public abstract boolean isLeaf();

    /**
     * Get this node's children.
     * <p>
     * Note: If this is a leaf, this will throw a NoSuchElementException
     *
     * @return This nodes's children
     */
    public abstract List<Tree<T>> getChildren();

//    public abstract void accept(TreeVisitor<T> v);

    /**
     * Apply a map reduce style operation on this tree.
     *
     * @param operation Operation to apply
     * @param <R>       Return type
     *
     * @return Result computed by the operation
     *
     * @see MapReduceOperation
     */
    public abstract <R> R mapReduce(MapReduceOperation<R, T> operation);

    @Override
    public String toString() {
        return Objects.toString(getElement());
    }


    public static class Leaf<T> extends Tree<T> {

        /**
         * Creates a new Leaf.
         *
         * @param element Value of this leaf
         */
        public Leaf(T element) {
            super(element);
        }

        @Override
        public List<Tree<T>> getChildren() {
            throw new NoSuchElementException();
        }

        @Override
        public boolean isLeaf() {
            return true;
        }

        @Override
        public <R> R mapReduce(MapReduceOperation<R, T> operation) {
            return operation.compute(this);
        }
    }

    public static class Node<T> extends Tree<T> {
        private final List<Tree<T>> nodes;

        /**
         * Creates a new node.
         *
         * @param element Value of this node
         * @param nodes   Children of this node
         */
        public Node(T element, List<Tree<T>> nodes) {
            super(element);
            this.nodes = safe(nodes);
        }

        @Override
        public List<Tree<T>> getChildren() {
            return nodes;
        }

        @Override
        public boolean isLeaf() {
            return false;
        }

        @Override
        public <R> R mapReduce(MapReduceOperation<R, T> operation) {
            Stream<R> children = nodes.stream()
                    .map(c -> c.mapReduce(operation));
            return operation.reduce(operation.compute(this), children);
        }
    }
}
