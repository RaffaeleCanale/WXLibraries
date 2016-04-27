package com.wx.tree;

import org.junit.Test;

import java.util.*;

import static com.wx.tree.visitor.CommonOperations.recursiveEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Created by canale on 27.04.16.
 */
public class TreeTest {

    @Test(expected = NoSuchElementException.class)
    public void testLeafHasNoChildren() {
        Tree.Leaf<String> leaf = new Tree.Leaf<>("");
        leaf.getChildren();
    }

    @Test
    public void testIsLeaf() {
        assertTrue(new Tree.Leaf<>("").isLeaf());
        assertFalse(new Tree.Node<>("", Collections.emptyList()).isLeaf());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testNodeIsImmutable() {
        List<Tree<String>> leaves = Arrays.asList(new Tree.Leaf<>("1"), new Tree.Leaf<>("2"));

        Tree.Node<String> node = new Tree.Node<>("N", leaves);
        List<Tree<String>> children = new LinkedList<>(node.getChildren());

        assertTrue(recursiveEquals(children, leaves));

        leaves.remove(0);
        assertFalse(recursiveEquals(children, leaves));

        node.getChildren().remove(0);
    }


}