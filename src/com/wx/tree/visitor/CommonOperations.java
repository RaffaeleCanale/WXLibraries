package com.wx.tree.visitor;

import com.wx.tree.Tree;

import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

import static javafx.scene.input.KeyCode.T;

/**
 * Created by canale on 27.04.16.
 */
public class CommonOperations {

    public static <T> int elementsCount(Tree<T> tree) {
        return tree.mapReduce(new MapReduceOperation<Integer, T>() {
            @Override
            public Integer compute(Tree.Leaf<T> leaf) {
                return 1;
            }

            @Override
            public Integer compute(Tree.Node<T> node) {
                return 1;
            }

            @Override
            public Integer reduce(Integer parent, Stream<Integer> children) {
                return parent + children.mapToInt(Integer::intValue).sum();
            }
        });
    }

    public static <T> int maxDepth(Tree<T> tree) {
        return tree.mapReduce(new MapReduceOperation<Integer, T>() {
            @Override
            public Integer compute(Tree.Leaf<T> leaf) {
                return 1;
            }

            @Override
            public Integer compute(Tree.Node<T> node) {
                return 1;
            }

            @Override
            public Integer reduce(Integer parent, Stream<Integer> children) {
                return parent + children.mapToInt(Integer::intValue).max().orElse(0);
            }
        });
    }


    public static <T> boolean recursiveEquals(Tree<T> tree1, Tree<T> tree2) {
        if (tree1 == null || tree2 == null) {
            return false;
        }
        if (tree1 == tree2) {
            return true;
        }
        return tree1.isLeaf() == tree2.isLeaf() &&
                Objects.equals(tree1.getElement(), tree2.getElement()) &&
                (tree1.isLeaf() || recursiveEquals(tree1.getChildren(), tree2.getChildren()));
    }

    public static <T> boolean recursiveEquals(List<Tree<T>> children1, List<Tree<T>> children2) {
        if (children1 == null || children2 == null) {
            return false;
        }
        if (children1 == children2) {
            return true;
        }
        if (children1.size() != children2.size()) {
            return false;
        }

        Iterator<Tree<T>> it1 = children1.iterator();
        Iterator<Tree<T>> it2 = children2.iterator();

        while (it1.hasNext()) {
            Tree<T> c1 = it1.next();
            Tree<T> c2 = it2.next();
            if (!recursiveEquals(c1, c2)) {
                return false;
            }
        }

        return true;
    }

}
