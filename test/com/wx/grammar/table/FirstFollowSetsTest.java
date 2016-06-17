//package com.wx.grammar.table;
//
//import com.wx.grammar.GrammarException;
//import com.wx.grammar.symbol.Symbol;
//import com.wx.lexer.Lexer;
//import com.wx.parser.Parser;
//import com.wx.lexer.tokens.Token;
//
//import com.wx.tree.Tree;
//import com.wx.tree.PrintTree;
//import java.io.ByteArrayInputStream;
//import java.io.IOException;
//import java.util.function.Function;
//import static org.junit.Assert.assertSame;
//import org.junit.Test;
//import com.wx.grammar.cnf.CNFTreeParser;
//
///**
// *
// * @author Raffaele Canale (raffaelecanale@gmail.com)
// */
//public class FirstFollowSetsTest {
//
//    @Test
//    public void testFirsts() throws GrammarException {
//        for (GrammarExample example : GrammarExample.EXAMPLES) {
//            FirstFollowSets sets = new FirstFollowSets(example.getGrammar());
//
//            for (Symbol sym : example.getAllSymbols()) {
//                assertSetEquals(example::getFirst, sets::getFirst, sym, "first");
//            }
//        }
//    }
//
//    @Test
//    public void testFollow() throws GrammarException {
//        for (GrammarExample example : GrammarExample.EXAMPLES) {
//            FirstFollowSets sets = new FirstFollowSets(example.getGrammar());
//
//            for (Symbol sym : example.getAllSymbols()) {
//                assertSetEquals(example::getFollow, sets::getFollow, sym, "follow");
//            }
//
//        }
//    }
//
//    @Test
//    public void testTable() throws GrammarException {
//        for (GrammarExample example : GrammarExample.EXAMPLES) {
//            ParseTable table = new ParseTable.Builder(example.getGrammar()).build();
//
//            for (Symbol sym : example.getAllSymbols()) {
//                for (Token tok : example.getAllTokens()) {
////                    System.out.println("[" + sym + ", " + tok + "]   " +
////                        example.getFromTable(sym, tok) + " ::: " + table.getRule(sym, tok));
//                    assertSame(example.getFromTable(sym, tok), table.getRule(sym, tok));
//                }
//            }
//        }
//    }
//
//    @Test
//    public void testParser() throws GrammarException, IOException {
//        GrammarExample example = GrammarExample.EXAMPLES[0];
//        ParseTable table = new ParseTable.Builder(example.getGrammar()).build();
//
//        ByteArrayInputStream input = new ByteArrayInputStream("(((a+a)+a)+a)".getBytes());
//
//        Parser parser = new Parser(Lexer.create().specials('(', ')', 'a', '+').build(input), null);
//
//        CNFTreeParser treeParser = new CNFTreeParser(table, example.getGrammar(), parser);
//        Tree<Object> tree = treeParser.start();
//        new PrintTree().print(tree);
////        new PrintTree().print(new Leaf("SALUT"));
////        new PrintTree().print(new Node("LAAAAAAAAAAAARGE", new Leaf("L1")));
////        new PrintTree().print(new Node("N1", new Leaf("LAAAAAAAAAAAARGE")));
////        new PrintTree().print(new Node("N1", new Leaf("L1")));
////        new PrintTree().print(new Node("N1", new Leaf("L1"),new Leaf("L1"),new Leaf("L1")));
//    }
//
//    private void assertSetEquals(Function<Symbol, TokenSet> expected,
//            Function<Symbol, TokenSet> actual, Symbol s, String name) {
//        TokenSet ex = expected.apply(s);
//        TokenSet ac = actual.apply(s);
//
//        if (ex == null) {
//            return;
//        }
//
//        if (!ex.equals(ac)) {
//            throw new AssertionError(name + " set for " + s + " should contain:"
//                    + " " + ex
//                    + "\nbut contains:"
//                    + " " + ac);
//        }
//    }
//
//}
