package com.wx.grammar.cnf;

import com.wx.grammar.GrammarException;
import com.wx.grammar.cnf.rule.ProductionRule;
import com.wx.grammar.cnf.rule.TerminalRule;
import com.wx.grammar.symbol.Symbol;
import com.wx.grammar.table.ParseTable;
import com.wx.lexer.tokens.Token;
import com.wx.tree.Tree;
import com.wx.tree.Tree.Leaf;
import com.wx.tree.Tree.Node;
import com.wx.util.iterator.PeekIterator;
import com.wx.util.future.IoIterator;
import com.wx.util.future.Future;

import java.io.IOException;

/**
 *
 * @author Raffaele Canale (raffaelecanale@gmail.com)
 * @version 0.1
 */
public class CNFTreeParser {

//    public static Tree<Symbol, Token> parse(ParseTable table, CNFGrammar grammar,
//            IoIterator<Token> lexer) throws GrammarException, IOException {
//        return new CNFTreeParser(table, grammar).parse(lexer);
//    }
//
//    public static Tree<Symbol, Token> parse(CNFGrammar grammar, IoIterator<Token> lexer)
//            throws GrammarException, IOException {
//        return new CNFTreeParser(grammar).parse(lexer);
//    }
//
//    private final ParseTable table;
//    private final CNFGrammar grammar;
////    private final TokenIterator parser;
//
//    public CNFTreeParser(ParseTable table, CNFGrammar grammar) {
//        this.table = table;
//        this.grammar = grammar;
////        this.parser = parser;
//    }
//
//    public CNFTreeParser(CNFGrammar grammar) throws GrammarException {
//        this(new ParseTable.Builder(grammar).build(), grammar);
//    }
//
//    public Tree<Symbol, Token> parse(IoIterator<Token> lexer)
//            throws GrammarException, IOException {
//        return parse0(table.getFirstSymbol(), new PeekIterator<>(lexer.iterator()));
//    }
//
//    private Tree<Symbol, Token> parse0(Symbol sym, PeekIterator<Future<Token>> lexer)
//            throws GrammarException, IOException {
//        TerminalRule terminal = grammar.getTerminalRule(sym);
//
//        Token current = lexer.peek().get();
//        if (terminal != null) {
//            if (!terminal.getExpectedToken().compareTokenType(current)) {
//                throw new GrammarException("Expected token "
//                        + terminal.getExpectedToken() + " but got " + lexer.peek());
//            }
//
//            Token ret = current;
//
//            next(lexer);
//            return new Leaf<>(ret);
//
//        } else {
//
//            ProductionRule rule = table.getRule(sym, current);
//            if (rule == null) {
//                throw new GrammarException("No rule found for " + sym + " and " + current);
//            }
//            Tree<Symbol, Token>[] nodes = new Tree[rule.getElements().size()];
//
//            int i = 0;
//            for (Symbol s : rule) {
//                nodes[i] = parse0(s, lexer);
//                i++;
//            }
//
//            return new Node(sym, nodes);
//
//        }
//    }
//
//    private void next(PeekIterator<Future<Token>> lexer) throws GrammarException, IOException {
//        if (!lexer.hasNext()) {
//            throw new GrammarException("More input expected after " + lexer.peek().get());
//        }
//        lexer.next();
//    }



}
