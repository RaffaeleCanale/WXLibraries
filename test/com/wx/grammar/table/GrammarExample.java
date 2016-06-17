package com.wx.grammar.table;

import com.wx.grammar.symbol.NamedSymbol;
import com.wx.grammar.cnf.CNFGrammar;
import com.wx.grammar.GrammarException;
import com.wx.grammar.symbol.Symbol;
import com.wx.grammar.cnf.rule.ProductionRule;
import com.wx.lexer.tokens.EOFToken;
import com.wx.lexer.tokens.SpecialCharToken;
import com.wx.lexer.tokens.Token;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;


/**
 *
 * @author Raffaele Canale (raffaelecanale@gmail.com)
 * @version 0.1
 */
public class GrammarExample {

    public static final GrammarExample[] EXAMPLES = {example1(), example2()};
    
    private static GrammarExample example2() {
        Builder builder = new Builder();
        // http://www.jambe.co.nz/UNI/FirstAndFollowSets.html
        
        Symbol E = builder.declareStartingSymbol("E");
        Symbol R = builder.declareSymbol("E'");        
        Symbol T = builder.declareSymbol("T");
        Symbol Z = builder.declareSymbol("T'");
        Symbol F = builder.declareSymbol("F");
        
        Symbol leftP = builder.declareSymbol("(");
        Symbol rightP = builder.declareSymbol(")");
        Symbol kleene = builder.declareSymbol("*");
        Symbol plus = builder.declareSymbol("+");
        Symbol id = builder.declareSymbol("id");
        
        builder.addTerminalRule(leftP, '(')
                .addTerminalRule(rightP, ')')
                .addTerminalRule(kleene, '*')
                .addTerminalRule(plus, '+')
                .addTerminalRule(id, 'i')
                .addProductiveRule(E, T, R)
                .addProductiveRule(R, plus, T, R)
                .addEpsilonSymbol(R)
                .addProductiveRule(T, F, Z)
                .addProductiveRule(Z, kleene, F, Z)
                .addEpsilonSymbol(Z)
                .addProductiveRule(F, leftP, E, rightP)
                .addProductiveRule(F, id);
        
        builder.firsts(leftP, false, '(')
                .firsts(rightP, false, ')')
                .firsts(kleene, false, '*')
                .firsts(plus, false, '+')
                .firsts(id, false, 'i')
                .firsts(E, false, '(', 'i')
                .firsts(R, true, '+')
                .firsts(T, false, '(', 'i')
                .firsts(Z, true, '*')
                .firsts(F, false, '(', 'i');
        
        builder.follow(leftP, false, '(', 'i')
                .follow(rightP, true, ')', '+', '*')
                .follow(kleene, false, '(', 'i')
                .follow(plus, false, '(', 'i')
                .follow(id, true, ')', '+', '*')
                .follow(E, true, ')')
                .follow(R, true, ')')
                .follow(T, true, '+', ')')
                .follow(Z, true, '+', ')')
                .follow(F, true, '*', '+', ')');
        
        builder.addToTable(E, '(', 1)
                .addToTable(E, 'i', 1)                
                .addToTable(F, '(', 7)
                .addToTable(F, 'i', 8)                
                .addToTable(R, ')', 3)
                .addToTable(R, '+', 2)
//                .addToTableEOF(R, 3)                
                .addToTable(T, '(', 4)
                .addToTable(T, 'i', 4)                
                .addToTable(Z, ')', 6)
                .addToTable(Z, '*', 5)
                .addToTable(Z, '+', 6);
//                .addToTableEOF(Z, 6);
                
                
        
        
        return builder.build();
    }

    private static GrammarExample example1() {
        Builder builder = new Builder();

        Symbol S = builder.declareStartingSymbol("S");
        Symbol F = builder.declareSymbol("F");

        Symbol leftP = builder.declareSymbol("_(");
        Symbol rightP = builder.declareSymbol("_)");
        Symbol plus = builder.declareSymbol("_+");

        builder.addTerminalRule(leftP, '(')
                .addTerminalRule(rightP, ')')
                .addTerminalRule(plus, '+')
                .addTerminalRule(F, 'a')
                .addProductiveRule(S, F)
                .addProductiveRule(S, leftP, S, plus, F, rightP);
        
        builder.firsts(S, false, 'a', '(')
                .firsts(F, false, 'a')
                .firsts(leftP, false, '(')
                .firsts(rightP, false, ')')
                .firsts(plus, false, '+');
        
        builder.follow(S, true, '+')
                .follow(F, true, ')', '+')
                .follow(leftP, false, 'a', '(')
                .follow(rightP, true, '+')                
                .follow(plus, false, 'a');
        
        builder.addToTable(S, '(', 2);
        builder.addToTable(S, 'a', 1);

        return builder.build();
    }

    private final CNFGrammar grammar;
    private final Map<Symbol, TokenSet> first;
    private final Map<Symbol, TokenSet> follow;
    private final ExpectedParseTable table;
    private final Set<Token> allDeclaredTokens;

    public GrammarExample(CNFGrammar grammar, Map<Symbol, TokenSet> first, 
            Map<Symbol, TokenSet> follow, ExpectedParseTable table, 
            Set<Token> allDeclaredTokens) {
        this.grammar = grammar;
        this.first = first;
        this.follow = follow;
        this.table = table;
        this.allDeclaredTokens = allDeclaredTokens;
    }    

    public CNFGrammar getGrammar() {
        return grammar;
    }

    public TokenSet getFirst(Symbol symbol) {
        return first.get(symbol);
    }
    
    public TokenSet getFollow(Symbol symbol) {
        return follow.get(symbol);
    }

    public ProductionRule getFromTable(Symbol s, Token t) {
        return table.get(s, t);
    }

    public Set<Symbol> getAllSymbols() {
        return first.keySet();
    }

    Iterable<Token> getAllTokens() {
        return allDeclaredTokens;
    }
    
    private static class Builder {

        private CNFGrammar.Builder grammar;
        private final Map<Symbol, TokenSet> symFirsts = new HashMap<>();
        private final Map<Symbol, TokenSet> symFollows = new HashMap<>();
        private final ExpectedParseTable table = new ExpectedParseTable();        
        private final List<ProductionRule> rules = new ArrayList<>();
        
        private final Set<Token> declaredTokens = new HashSet<>();
        private final Set<Symbol> declaredSymbols = new HashSet<>();

        
        public Symbol declareSymbol(String name) {
            Symbol sym = new NamedSymbol(name);
            
            declaredSymbols.add(sym);
            
            return sym;
        }

        public Symbol declareStartingSymbol(String name) {
            assert grammar == null : "Starting symbol already declared";
            
            Symbol sym = declareSymbol(name);
            grammar = new CNFGrammar.Builder(sym);

            return sym;
        }

        public Builder addTerminalRule(Symbol sym, char c) {
            assert grammar != null : "Must declare starting symbol first";
            assert declaredSymbols.contains(sym) : "Undeclared symbol " + sym;
            
            SpecialCharToken tok = new SpecialCharToken(c, null);
            
            declaredTokens.add(tok);
            grammar.addTerminalRule(sym, tok);

            return this;
        }

        public Builder addProductiveRule(Symbol sym, Symbol... rule) {
            assert grammar != null : "Must declare starting symbol first";
            assert declaredSymbols.contains(sym) : "Undeclared symbol " + sym;
            for (Symbol s : rule) {
                assert declaredSymbols.contains(s) : "Undeclared symbol " + sym;
            }
            
            ProductionRule r = grammar.addProductiveRule(sym, rule);
            rules.add(r);

            return this;
        }
        
        public Builder addEpsilonSymbol(Symbol sym) {
            assert grammar != null : "Must declare starting symbol first";
            assert declaredSymbols.contains(sym) : "Undeclared symbol " + sym;
            
            ProductionRule rule = grammar.addEpsilonSymbol(sym);
            rules.add(rule);

            return this;
        }
        
        public Builder firsts(Symbol sym, boolean epsilon, Character... chars) {
            assert grammar != null : "Must declare starting symbol first";
            assert declaredSymbols.contains(sym) : "Undeclared symbol " + sym;
            for (Character c : chars) {
                assert declaredTokens.contains(new SpecialCharToken(c, null)) : "Undeclared token '" + c + "'";
            }
            
            TokenSet.Builder builder = new TokenSet.Builder();
            if (epsilon) {
                builder.addEpsilon();
            }
            for (Character c : chars) {
                builder.addToken(new SpecialCharToken(c, null));
            }

            symFirsts.put(sym, builder.build());

            return this;
        }

        public Builder follow(Symbol sym, boolean eof, Character... chars) {
            assert grammar != null : "Must declare starting symbol first";
            assert declaredSymbols.contains(sym) : "Undeclared symbol " + sym;
            for (Character c : chars) {
                assert declaredTokens.contains(new SpecialCharToken(c, null)) : "Undeclared token '" + c + "'";
            }
            
            TokenSet.Builder builder = new TokenSet.Builder();
            if (eof) {
                builder.addToken(new EOFToken(null));
            }
            for (Character c : chars) {
                builder.addToken(new SpecialCharToken(c, null));
            }

            symFollows.put(sym, builder.build());

            return this;
        }

//        public Builder addToTableEOF(Symbol sym, int rule) {            
//            assert declaredSymbols.contains(sym) : "Undeclared symbol " + sym;            
//            assert rule > 0 && rule <= rules.size() : "Out of bound rule: " + rule;
//            
//            table.add(sym, new Token(TokenKind.EOF), rules.get(rule - 1));
//            
//            return this;
//        }
        
        public Builder addToTable(Symbol sym, char t, int rule) {
            assert declaredSymbols.contains(sym) : "Undeclared symbol " + sym;            
            assert rule > 0 && rule <= rules.size() : "Out of bound rule: " + rule;
            
            table.add(sym, new SpecialCharToken(t, null), rules.get(rule - 1));
            
            return this;
        }

        public GrammarExample build() {
            testUndeclaredSet(symFirsts);
            testUndeclaredSet(symFollows);
            
            try {
                return new GrammarExample(grammar.build(), symFirsts, symFollows,
                        table, declaredTokens);
            } catch (GrammarException ex) {
                throw new RuntimeException(ex);
            }
        }

        private void testUndeclaredSet(Map<Symbol, TokenSet> sets) {
            Set<Symbol> declared = new HashSet<>(declaredSymbols);
            
            declared.removeAll(sets.keySet());
            
            assert declared.isEmpty() : "A first/follow set declaration is "
                    + "missing for " + declared;
        }
        
        
    }

}
