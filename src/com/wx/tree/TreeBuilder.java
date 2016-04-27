package com.wx.tree;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Stream;

import static com.wx.tree.Tree.Leaf;
import static com.wx.tree.Tree.Node;
import static java.util.stream.Collectors.toList;

/**
 * This builder allows to create trees.
 *
 * @author Raffaele Canale (raffaelecanale@gmail.com)
 * @version 0.5
 */
public abstract class TreeBuilder<T> {

    /**
     * Create a tree builder.
     *
     * @param root Value contained in the tree-root
     * @param <T>  Type of the tree values
     *
     * @return The tree-root builder
     */
    public static <T> NodeBuilder<T> fromRoot(T root) {
        return new NodeBuilder<>(root);
    }

    private TreeBuilder() {
    }

    abstract Tree<T> build();

    private static class LeafBuilder<T> extends TreeBuilder<T> {
        private final T element;

        public LeafBuilder(T element) {
            this.element = element;
        }

        @Override
        public Leaf<T> build() {
            return new Leaf<>(element);
        }
    }

    public static class NodeBuilder<T> extends TreeBuilder<T> {

        private final T element;
        private final List<TreeBuilder<T>> children;
//        private final List<NodeBuilder<T>> nodes;
//        private final List<LeafBuilder<T>> leaves;

        /**
         * Create a new node builder.
         *
         * @param element Value of this node
         */
        public NodeBuilder(T element) {
            this.element = element;
            this.children = new LinkedList<>();
        }

        /**
         * Add a leaf to this node.
         *
         * @param element Value of the added leaf
         *
         * @return {@code this} for chained calls
         */
        public NodeBuilder<T> addLeaf(T element) {
            children.add(new LeafBuilder<>(element));

            return this;
        }

        /**
         * Add a new empty node as child of this node.
         *
         * @param element Value of the added node
         *
         * @return A reference to the builder of the newly added children
         */
        public NodeBuilder<T> addNode(T element) {
            NodeBuilder<T> node = new NodeBuilder<>(element);
            children.add(node);

            return node;
        }

        @Override
        public Node<T> build() {
            List<Tree<T>> builtChildren = children.stream()
                    .map(TreeBuilder::build)
                    .collect(toList());

            return new Node<>(element, builtChildren);
        }

    }
}
