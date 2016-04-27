package com.wx.tree.visitor;

import com.wx.tree.Tree;
import com.wx.tree.TreeBuilderTest;
import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.*;

/**
 * Created by canale on 27.04.16.
 */
public class TreeVisitorTest {

    @Test
    public void testAllElementsVisitedOnce() {
        TreeBuilderTest.TreeInfo treeInfo = TreeBuilderTest.exampleTree1();

        Set<Tree<TreeBuilderTest.NodeInfo>> visitedNodes = new HashSet<>();

        treeInfo.tree.mapReduce(new TreeVisitor<TreeBuilderTest.NodeInfo>() {
            @Override
            public void visit(Tree.Leaf<TreeBuilderTest.NodeInfo> leaf) {
                assertTrue(visitedNodes.add(leaf));
            }

            @Override
            public void visit(Tree.Node<TreeBuilderTest.NodeInfo> node) {
                assertTrue(visitedNodes.add(node));
            }
        });

        assertEquals(treeInfo.elementsCount, visitedNodes.size());
    }

    @Test
    public void testVisitsInDepthFirstOrder() {
        TreeBuilderTest.TreeInfo treeInfo = TreeBuilderTest.exampleTree1();

        treeInfo.tree.mapReduce(new TreeVisitor<TreeBuilderTest.NodeInfo>() {
            int nextExpectedNode = 0;

            @Override
            public void visit(Tree.Leaf<TreeBuilderTest.NodeInfo> leaf) {
                assertEquals(nextExpectedNode, leaf.getElement().dfIndex);
                nextExpectedNode++;
            }

            @Override
            public void visit(Tree.Node<TreeBuilderTest.NodeInfo> node) {
                assertEquals(nextExpectedNode, node.getElement().dfIndex);
                nextExpectedNode++;
            }
        });
    }

}