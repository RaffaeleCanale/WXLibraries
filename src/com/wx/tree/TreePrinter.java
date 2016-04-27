package com.wx.tree;

import com.wx.console.Console;
import com.wx.console.system.SystemConsole;
import com.wx.tree.visitor.MapReduceOperation;
import com.wx.tree.visitor.TreeVisitor;

import static com.wx.tree.Tree.*;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;

import java.io.PrintStream;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * This is a utility class that allows to print trees.
 *
 * @author Raffaele Canale (raffaelecanale@gmail.com)
 * @version 0.5
 */
public class TreePrinter<T> {

    private enum Chars {
        TOP_LEFT('\u256D'),
        TOP_RIGHT('\u256E'),
        BOTTOM_RIGHT('\u256F'),
        BOTTOM_LEFT('\u2570'),
        CROSS('\u253C'),
        HORIZONTAL('\u2500'),
        VERTICAL('\u2502'),
        BOTTOM_OUT('\u252C'),
        TOP_OUT('\u2534'),
        RIGHT_OUT('\u251C'),
        LEFT_OUT('\u2524');

        private final char code;

        Chars(char code) {
            this.code = code;
        }

        @Override
        public String toString() {
            return String.valueOf(code);
        }
    }

    public static void print(Tree<?> tree) {
        print(tree, System.out);
    }

    public static void print(Tree<?> tree, PrintStream out) {
        List<String> result = tree.mapReduce(new VerticalPrinter<>());
        result.forEach(out::println);
    }

    public static String toString(Tree<?> tree) {
        return String.join("\n", tree.mapReduce(new VerticalPrinter<>()));
    }

    private static class VerticalPrinter<T> implements MapReduceOperation<List<String>, T> {

        private List<String> boxed(Object value, boolean bottomOut) {
            String s = Objects.toString(value);

            int half1 = s.length() / 2;
            int half2 = s.length() - half1 - 1;

            return Arrays.asList(
                    Chars.TOP_LEFT + repeat(half1, Chars.HORIZONTAL.code) + Chars.TOP_OUT + repeat(half2, Chars.HORIZONTAL.code) + Chars.TOP_RIGHT,
                    Chars.VERTICAL + s + Chars.VERTICAL,
                    bottomOut ? Chars.BOTTOM_LEFT + repeat(half1, Chars.HORIZONTAL.code) + Chars.BOTTOM_OUT + repeat(half2, Chars.HORIZONTAL.code) + Chars.BOTTOM_RIGHT :
                            Chars.BOTTOM_LEFT + repeat(s.length(), Chars.HORIZONTAL.code) + Chars.BOTTOM_RIGHT
            );
        }

        @Override
        public List<String> compute(Leaf<T> leaf) {
            return boxed(leaf.getElement(), false);
        }

        @Override
        public List<String> compute(Node<T> node) {
            return boxed(node.getElement(), true);
        }

        @Override
        public List<String> reduce(List<String> parent, Stream<List<String>> leaves) {
            List<String> result = new ArrayList<>();

            List<List<String>> blocks = leaves.collect(toList());
            int rootWidth = parent.get(0).length();
            int totalWidth = Math.max(blocks.stream().mapToInt(l -> l.get(0).length()).sum(), rootWidth);

            int nodeStart = (totalWidth - rootWidth) / 2;

            for (String line : parent) {
                result.add(spaces(nodeStart) + line + spaces(totalWidth - nodeStart - line.length()));
            }

            List<String> children = blocks.stream().reduce(TreePrinter::zip)
                    .orElse(Collections.emptyList());
            children = children.stream()
                    .map(l -> l + spaces(totalWidth - l.length()))
                    .collect(Collectors.toList());

            if (!children.isEmpty()) {
                result.add(buildJoiningLine(result.get(result.size() - 1), children.get(0)));
            }
            result.addAll(children);

            return result;
        }
    }

    private static String buildJoiningLine(String top, String bottom) {
        if (top.length() != bottom.length()) {
            throw new IllegalArgumentException(top + " != " + bottom);
        }

        StringBuilder line = new StringBuilder(spaces(top.length()));

        int leftMost = Math.min(top.indexOf(Chars.BOTTOM_OUT.code), bottom.indexOf(Chars.TOP_OUT.code));
        int rightMost = Math.max(top.lastIndexOf(Chars.BOTTOM_OUT.code), bottom.lastIndexOf(Chars.TOP_OUT.code));

        Chars[] leftMostMap = {null, Chars.TOP_LEFT, Chars.BOTTOM_LEFT, Chars.RIGHT_OUT};
        Chars[] rightMostMap = {null, Chars.TOP_RIGHT, Chars.BOTTOM_RIGHT, Chars.LEFT_OUT};
        Chars[] middleMap = {Chars.HORIZONTAL, Chars.BOTTOM_OUT, Chars.TOP_OUT, Chars.CROSS};

        setCharAccordingToState(top, bottom, leftMost, line, leftMostMap);
        setCharAccordingToState(top, bottom, rightMost, line, rightMostMap);

        for (int i = leftMost + 1; i < rightMost; i++) {
            setCharAccordingToState(top, bottom, i, line, middleMap);
        }

        return line.toString();
    }

    private static void setCharAccordingToState(String top, String bottom, int index, StringBuilder line, Chars[] map) {
        int topPresent = top.charAt(index) == Chars.BOTTOM_OUT.code ? 0b10 : 0;
        int bottomPresent = bottom.charAt(index) == Chars.TOP_OUT.code ? 0b01 : 0;

        int state = topPresent | bottomPresent;

        line.setCharAt(index, map[state].code);
    }

    private static List<String> zip(List<String> l1, List<String> l2) {
        List<String> result = new ArrayList<>(Math.max(l1.size(), l2.size()));

        int l1Width = l1.get(0).length();
        int l2Width = l2.get(0).length();
        int min = Math.min(l1.size(), l2.size());

        for (int i = 0; i < min; i++) {
            result.add(l1.get(i) + l2.get(i));
        }

        for (int i = min; i < l1.size(); i++) {
            result.add(l1.get(i) + spaces(l2Width));
        }
        for (int i = min; i < l2.size(); i++) {
            result.add(spaces(l1Width) + l2.get(i));
        }

        return result;
    }

    private static String spaces(int count) {
        return repeat(count, ' ');
    }

    private static String repeat(int count, char c) {
        if (count <= 0) {
            return "";
        }
        StringBuilder builder = new StringBuilder(count);
        for (int i = 0; i < count; i++) {
            builder.append(c);
        }

        return builder.toString();
    }


    private static void printSpaces(int count, Console console) {
        for (int i = 0; i < count; i++) {
            console.print(' ');
        }
    }


    private TreePrinter() {}


    private class TreeHeight implements MapReduceOperation<Integer, T> {

        @Override
        public Integer compute(Leaf<T> leaf) {
            return 1;
        }

        @Override
        public Integer compute(Node<T> node) {
            return 1;
        }

        @Override
        public Integer reduce(Integer parent, Stream<Integer> leaves) {
            return leaves.reduce(parent, (a, b) -> a+b);
        }
    }

    private static class TreeWidth<T> implements MapReduceOperation<Integer, T> {

        @Override
        public Integer compute(Leaf<T> leaf) {
            return Objects.toString(leaf.getElement()).length();
        }

        @Override
        public Integer compute(Node<T> node) {
            return Objects.toString(node.getElement()).length();
        }

        @Override
        public Integer reduce(Integer parent, Stream<Integer> leaves) {
            return leaves.reduce(parent, (a, b) -> a + b);
        }
    }


}
