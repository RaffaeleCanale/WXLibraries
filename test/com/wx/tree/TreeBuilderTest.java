package com.wx.tree;

import org.junit.Test;

import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by canale on 27.04.16.
 */
public class TreeBuilderTest {

    public static class TreeInfo {
        public final Tree<NodeInfo> tree;
        public final int maxDepth;
        public final int maxWidth;
        public final int elementsCount;

        public TreeInfo(Tree<NodeInfo> tree, int maxDepth, int maxWidth, int elementsCount) {
            this.tree = tree;
            this.maxDepth = maxDepth;
            this.maxWidth = maxWidth;
            this.elementsCount = elementsCount;
        }
    }

    public static class NodeInfo {
        public final int depthLevel;
        public final int widthLevel;
        public final int dfIndex;
        public final int bfIndex;

        public NodeInfo(int depthLevel, int widthLevel, int dfIndex, int bfIndex) {
            this.depthLevel = depthLevel;
            this.widthLevel = widthLevel;
            this.dfIndex = dfIndex;
            this.bfIndex = bfIndex;
        }

        @Override
        public String toString() {
            return "(" + depthLevel + "," + widthLevel + ") -- " + dfIndex + "/" + bfIndex;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            NodeInfo nodeInfo = (NodeInfo) o;

            if (depthLevel != nodeInfo.depthLevel) return false;
            if (widthLevel != nodeInfo.widthLevel) return false;
            if (dfIndex != nodeInfo.dfIndex) return false;
            return bfIndex == nodeInfo.bfIndex;

        }

        @Override
        public int hashCode() {
            int result = depthLevel;
            result = 31 * result + widthLevel;
            result = 31 * result + dfIndex;
            result = 31 * result + bfIndex;
            return result;
        }
    }

    public static TreeInfo exampleTree1() {
        TreeBuilder.NodeBuilder<NodeInfo> root = TreeBuilder.fromRoot(new NodeInfo(0,0,0,0));

        root.addLeaf(new NodeInfo(1,0,1,1))
                .addLeaf(new NodeInfo(1,1,2,2));

        TreeBuilder.NodeBuilder<NodeInfo> child3 = root.addNode(new NodeInfo(1, 2, 3, 3));
        child3.addNode(new NodeInfo(2,0,4,5));
        child3.addNode(new NodeInfo(2,1,5,6));

        TreeBuilder.NodeBuilder<NodeInfo> child4 = root.addNode(new NodeInfo(1,3,6,4));

        child4.addNode(new NodeInfo(2,2,7,7)).addNode(new NodeInfo(3,0,8,9)).addLeaf(new NodeInfo(4,0,9,10));
        child4.addLeaf(new NodeInfo(2,3,10,8));


        return new TreeInfo(root.build(), 5, 4, 11);
    }

    @Test
    public void testExampleTree() {

        Tree<NodeInfo> tree = exampleTree1().tree;
        assertEquals(0, tree.getElement().widthLevel);
        assertEquals(0, tree.getElement().depthLevel);

        List<Tree<NodeInfo>> nextChildren = tree.getChildren();

        int level = 1;
        while (!nextChildren.isEmpty()) {
            nextChildren = testChildren(nextChildren, level);
            level++;
        }
    }

    private List<Tree<NodeInfo>> testChildren(List<Tree<NodeInfo>> children, int level) {
        List<Tree<NodeInfo>> nextChildren = new LinkedList<>();

        int x = 0;
        for (Tree<NodeInfo> child : children) {
            assertEquals(level, child.getElement().depthLevel);
            assertEquals(x, child.getElement().widthLevel);
            x++;

            if (!child.isLeaf()) {
                nextChildren.addAll(child.getChildren());
            }
        }


        return nextChildren;
    }


}