package com.wx.tree.visitor;

import com.wx.tree.Tree;
import com.wx.tree.TreeBuilderTest;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static com.wx.tree.visitor.CommonOperations.*;
import static org.junit.Assert.*;

/**
 * Created by canale on 27.04.16.
 */
public class CommonOperationsTest {


    @Test
    public void testElementsCount() {
        TreeBuilderTest.TreeInfo treeInfo = TreeBuilderTest.exampleTree1();
        assertEquals(treeInfo.elementsCount, elementsCount(treeInfo.tree));
    }

    @Test
    public void testTreeDepth() {
        TreeBuilderTest.TreeInfo treeInfo = TreeBuilderTest.exampleTree1();
        assertEquals(treeInfo.maxDepth, maxDepth(treeInfo.tree));
    }

    @Test
    public void testRecursiveEquals() {
        Tree<TreeBuilderTest.NodeInfo> tree1 = TreeBuilderTest.exampleTree1().tree;
        Tree<TreeBuilderTest.NodeInfo> tree2 = TreeBuilderTest.exampleTree1().tree;

        assertTrue(recursiveEquals(tree1, tree1));
        assertTrue(recursiveEquals(tree1, tree2));

        List<Tree<String>> leaves1 = Arrays.asList(new Tree.Leaf<>("1"), new Tree.Leaf<>("2"));
        List<Tree<String>> leaves2 = Arrays.asList(new Tree.Leaf<>("1"), new Tree.Leaf<>("3"));
        List<Tree<String>> leaves3 = Arrays.asList(new Tree.Leaf<>("1"), new Tree.Leaf<>("2"), new Tree.Leaf<>("3"));

        assertFalse(recursiveEquals(new Tree.Node<>("", leaves1), new Tree.Node<>("", leaves3)));
        assertFalse(recursiveEquals(new Tree.Node<>("", leaves1), new Tree.Node<>("", leaves2)));
        assertTrue(recursiveEquals(new Tree.Node<>("", leaves1), new Tree.Node<>("", leaves1)));
        assertFalse(recursiveEquals(new Tree.Node<>("", leaves1), new Tree.Node<>("Hey", leaves1)));
    }

}