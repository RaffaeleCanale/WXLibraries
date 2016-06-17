//package com.wx.grammar.loader;
//
//import com.wx.grammar.GrammarException;
//import com.wx.grammar.symbol.NamedSymbol;
//import com.wx.grammar.symbol.Symbol;
//import com.wx.grammar.zGrammarBuilder;
//import com.wx.grammar.cf.element.Element;
//import com.wx.grammar.cf.element.Concatenation;
//import static com.wx.grammar.cf.element.BuilderHelper.*;
//import com.wx.grammar.cnf.CNFGrammar;
//import com.wx.lexer.Lexer;
//import com.wx.lexer.LexerBuilder;
//import com.wx.parser.Parser;
//import com.wx.grammar.symbol.GeneratedSymbol;
//import com.wx.lexer.tokens.SpecialChar;
//import com.wx.lexer.tokens.StringLit;
//import com.wx.tree.Node;
//import com.wx.tree.Tree;
//import com.wx.tree.PrintTree;
//import com.wx.tree.visitor.VisitorAdapter;
//import com.wx.util.pair.Pair;
//import java.io.ByteArrayInputStream;
//import java.io.IOException;
//import java.util.LinkedList;
//import com.wx.grammar.cnf.CNFTreeParser;
//
///**
// *
// * @author Raffaele Canale (raffaelecanale@gmail.com)
// * @version 0.1
// */
//public class LoaderGrammar {
//
//    public static void main(String[] args) throws IOException, GrammarException {
//        LoaderGrammar loader = new LoaderGrammar(":=", "|");
//
//        String test = "rule := rrr > vvv\n"
//                + "         element := litteral | ( element )      elementCont?\n"
//                + "         elementCont := ( element | ? elementCont | element\n";
//
//        test = "rule := (test | lol) (test2)\n";
//
//        Pair<String, Element> parseRule = new LoaderGrammar(":=", "|").parseRule(test);
//        System.out.println(parseRule);
//        GeneratedSymbol.resetIds();
//        
//        Symbol s = new NamedSymbol(parseRule.get1());
//        zGrammarBuilder builder = new zGrammarBuilder(s);
//        builder.declareRule(s, parseRule.get2());
//        
//        System.out.println(builder.toCnf());
////        Lexer lexer = new LexerBuilder().separators(" \t").specials("\n:=|()?")
////                .build(new ByteArrayInputStream(test.getBytes()));
////        
////        Grammar grammar = loader.buildLoadGrammar();
////        Parser parser = new Parser(lexer, new LinkedList<>());
//////        System.out.println(grammar);
////        Tree<Object> tree = new TreeParser(grammar, parser).start();
////        
////        new PrintTree().print(tree);
//    }
//
//    private final String eqSymbolString;
//    private final String orSymbolString;
//    private final CNFGrammar loadGrammar;
//
//    private final LexerBuilder lexerBuilder = new LexerBuilder()
//            .separators(" \t").specials("\n:=|()?");
//
//    public LoaderGrammar(String eqSymbolString, String orSymbolString) {
//        this.eqSymbolString = eqSymbolString;
//        this.orSymbolString = orSymbolString;
//        loadGrammar = buildLoadGrammar();
//    }
//
//    public CNFGrammar parse(String grammarText) {
//        new zGrammarBuilder(null);
//        return null;
//    }
//
//    private Pair<String, Element> parseRule(String rule) throws IOException, GrammarException {
//        if (!rule.endsWith("\n")) {
//            rule = rule + "\n";
//        }
//
//        Lexer lexer = lexerBuilder.build(new ByteArrayInputStream(rule.getBytes()));
//        Parser parser = new Parser(lexer, new LinkedList<>());
//
//        Tree<Object> tree = CNFTreeParser.parse(loadGrammar, parser);
//
//        new PrintTree().print(tree);
//
//        return tree.accept(new RuleVisitor());
//    }
//
//    private final class RuleVisitor implements VisitorAdapter<Pair<String, Element>, Object> {
//
//        @Override
//        public Pair<String, Element> visit(Node<Object> node) {
//            Tree<Object>[] nodes = node.getNodes();
//            assert nodes.length == 4;
//
//            String ruleName = ((StringLit) nodes[0].getElement()).getValue();
//
//            Element rule = nodes[2].accept(new ElementVisitor());
//            return new Pair<>(ruleName, rule);
//        }
//    }
//
//    private final class ElementVisitor implements VisitorAdapter<Element, Object> {
//
//        @Override
//        public Element visit(Node<Object> node) {
//            Tree<Object>[] nodes = node.getNodes();
//            assert nodes.length == 2 : "Element: Expected 2 nodes but got "+ nodes.length;
//            
//            Element lhsElement = nodes[0].accept(new ElementLHSVisitor());
//            return nodes[1].accept(new ElementRHSVisitor(lhsElement));
//        }
//
//
//    }
//    
//    private class ElementLHSVisitor implements VisitorAdapter<Element, Object> {
//
//        @Override
//        public Element visit(Node<Object> node) {
//            Tree<Object>[] nodes = node.getNodes();
//            
//            switch (nodes.length) {
//                case 1: // litteral
//                    return keyWord("TODO");
//                case 3: // ( element )
//                    return nodes[1].accept(new ElementVisitor());
//                default:
//                    new PrintTree().print(node);
//                    throw new AssertionError("Expected 1 or 3 but got " + nodes.length);
//            }
//        }
//        
//    }
//    
//    private class ElementRHSVisitor implements VisitorAdapter<Element, Object> {
//        private final Element lhsElement;
//
//        public ElementRHSVisitor(Element lhsElement) {
//            this.lhsElement = lhsElement;
//        }
//
//        @Override
//        public Element visit(Node<Object> node) {
//            Node<Object> orNode = dropALevel(dropALevel(node));
//            
//            
//            Tree<Object>[] nodes = orNode.getNodes();
//            System.out.println("RHS from " + orNode.getElement());
//            switch (nodes.length) {
//                case 0:
//                    System.out.println("\tNothing");
//                    return lhsElement;
//                case 1:
//                    System.out.println("\t1: " + nodes[0].getElement());
//                    return concat(lhsElement, nodes[0].accept(new ElementVisitor()));
//                case 2:
//                    System.out.println("\t2: " + nodes[0].getElement() + ", " + nodes[1].getElement());
//                    
//                    assert nodes[0].getClass().equals(Node.class);
//                    Node<Object> orNode2 = (Node<Object>) nodes[0];
//                    assert orNode2.getNodes().length == 1;
//                    Tree<Object> symLeaf = orNode2.getNodes()[0];
//                    assert symLeaf.getElement().getClass().equals(SpecialChar.class)
//                            : "Expected a special char: " + 
//                            symLeaf.getElement().getClass() +
//                            " - " + symLeaf.getElement();
//                    char value = ((SpecialChar) symLeaf.getElement()).getValue();
//                    
//                    switch (value) {
//                        case '|':
//                            return or(lhsElement, nodes[1].accept(new ElementVisitor()));
//                        case '?':
//                            return nodes[1].accept(new ElementRHSVisitor(optional(lhsElement)));
//                        default:
//                            throw new AssertionError();
//                    }
//                default:
//                    throw new AssertionError();
//            }
//        }
//        
//    }
//    
//    private Node<Object> dropALevel(Node<Object> node) {
//        Tree<Object>[] nodes = node.getNodes();
//        
//        if (nodes.length == 0) {
//            return node;
//        } else if (nodes.length == 1) {
//            assert nodes[0].getClass().equals(Node.class);
//            return (Node<Object>) nodes[0];
//        } else {
//            throw new AssertionError();
//        }
//    }
//    
//    private CNFGrammar buildLoadGrammar() {
//        /*
//         rule := name := element
//         element := litteral | ( element )      elementCont?
//         elementCont := \| element | ? elementCont | element
//        
//         //        elements := element (elements)?
//         //        element := litteral | ( element ) | element \| element
//         */
////        BSymbol rules = new BSymbol("rules");
//        Symbol rule = new NamedSymbol("rule");
//        Symbol element = new NamedSymbol("element");
//        Symbol elementCont = new NamedSymbol("elementCont");
//        Symbol eqSymbol = new NamedSymbol("eqSymbol");
//        Symbol orSymbol = new NamedSymbol("orSymbol");
//
//        zGrammarBuilder grammar = new zGrammarBuilder(rule);
//
//        grammar.declareRule(eqSymbol, specialSign(eqSymbolString));
//        grammar.declareRule(orSymbol, specialSign(orSymbolString));
//
////        grammar.declareRule(rules, concat(
////                rule, optional(rules)
////        ));
//        grammar.declareRule(rule, concat(
//                litteral(), eqSymbol, element, special('\n')
//        ));
//        grammar.declareRule(element, concat(
//                or(litteral(), concat(special('('), element, special(')'))),
//                optional(elementCont)
//        ));
//        grammar.declareRule(elementCont, or(
//                concat(orSymbol, element),
//                concat(special('?'), element),
//                element
//        ));
//
//        try {
//            //        Parser parser = new Parser(lexer, new LinkedList<>());
//            return grammar.toCnf();
//        } catch (GrammarException ex) {
//            throw new RuntimeException(ex);
//        }
//    }
//
//    private Element specialSign(String value) {
//        if (value.length() == 1) {
//            return special(value.charAt(0));
//        } else {
//            Element[] symbols = new Element[value.length()];
//            for (int i = 0; i < symbols.length; i++) {
//                symbols[i] = special(value.charAt(i));
//            }
//            return new Concatenation(symbols);
//        }
//    }
//
//}
