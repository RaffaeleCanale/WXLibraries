package com.wx.grammar.table;

import com.wx.grammar.cnf.CNFGrammar;
import com.wx.grammar.GrammarException;
import com.wx.grammar.symbol.Symbol;
import com.wx.grammar.cnf.rule.ProductionRule;
import com.wx.grammar.cnf.rule.CnfRule;
import com.wx.lexer.tokens.Token;
import com.wx.util.pair.Pair;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Represents a parse table.
 * To build such a table from a {@link CNFGrammar}, see {@link Builder}
 *
 * @author Raffaele Canale (raffaelecanale@gmail.com)
 * @version 0.1
 */
public class ParseTable {

    private final Map<Symbol, SubTable> table;
    private final Symbol firstSymbol;

    private ParseTable(Map<Symbol, SubTable> table, Symbol firstSymbol) {
        assert !table.isEmpty();
        this.table = table;
        this.firstSymbol = firstSymbol;
    }

    /**
     * Get the rule located in the table or {@code null} if it does not contain
     * any.
     *
     * @param symbol Current symbol
     * @param token  Current token
     *
     * @return The rule (or {@code null}) from the table at the given position
     */
    public ProductionRule getRule(Symbol symbol, Token token) {
        SubTable subMap = table.get(symbol);
        
        return subMap == null ? null : subMap.get(token);
    }

    /**
     *
     * @return Starting symbol of the grammar
     */
    public Symbol getFirstSymbol() {
        return firstSymbol;
    }

    @Override
    public String toString() {
        String result = "";
        int index = 1;

        List<Token> tokens = new LinkedList<>(table.values().iterator().next().keySet());
//        List<Symbol> symbols = new LinkedList<>(table.keySet());

        List<ProductionRule> rules = new LinkedList<>();

        for (SubTable entry : table.values()) {           
            for (ProductionRule r : entry.values()) {
                result += String.format("%2d", index) + ". " + r + "\n";
                rules.add(r);
                index++;
            }
        }

//        result += "\n     |";
        result += "\n_____|";
        for (Token token : tokens) {
            result += " " + token + " |";
        }
        result += "\n";

        for (Symbol symbol : table.keySet()) {
            result += "| " + String.format("%2s", symbol) + " |";

            for (Token token : tokens) {
                ProductionRule rule = table.get(symbol).get(token);
                if (rule == null) {
                    result += "  -  |";
                } else {
                    result += "  " + (rules.indexOf(rule) + 1) + "  |";
                }
            }
            result += "\n";
        }

        return result;
    }
    
    private static class SubTable {
        
        private final List<Pair<Token, ProductionRule>> subTable = new LinkedList<>();
        
        public ProductionRule get(Token token) {
            for (Pair<Token, ProductionRule> pair : subTable) {
                if (pair.get1().compareTokenType(token)) {
                    return pair.get2();
                }
            }
            
            return null;
        }
        
        public ProductionRule put(Token token, ProductionRule rule) {
            Pair<Token, ProductionRule> newPair = new Pair<>(token, rule);
            
            ListIterator<Pair<Token, ProductionRule>> it = subTable.listIterator();
            while (it.hasNext()) {
                Pair<Token, ProductionRule> current = it.next();
                if (current.get1().compareTokenType(token)) {
                    it.set(newPair);
                    return current.get2();
                }
            }
            
            subTable.add(newPair);
            return null;
        }
        
        public List<Token> keySet() {
            return subTable.stream().map(Pair::get1).collect(Collectors.toList());
        }

        public List<ProductionRule> values() {
            return subTable.stream().map(Pair::get2).collect(Collectors.toList());
        }
    }

    /**
     * Build a {@link ParseTable} from a {@link CNFGrammar}
     */
    public static class Builder {

        private final Map<Symbol, SubTable> table = new HashMap<>();
        private final FirstFollowSets sets;
        private final CNFGrammar grammar;

        /**
         * Constructs a new parse table builder.
         *
         * @param grammar Grammar to analyze
         *
         * @throws GrammarException
         */
        public Builder(CNFGrammar grammar) throws GrammarException {
            this.grammar = grammar;
            this.sets = new FirstFollowSets(grammar);
        }

        /**
         * Build the table
         * @return The created table
         * @throws GrammarException 
         */
        public ParseTable build() throws GrammarException {
            for (ProductionRule rule : grammar.getProductiveRules()) {
                process(rule, rule.iterator());
            }

            return new ParseTable(table, grammar.getStartSymbol());
        }

        private void process(ProductionRule rule, Iterator<Symbol> it) throws GrammarException {
            if (rule.isEpsilon()) {
                TokenSet follow = sets.getFollow(rule.getSymbol());
                for (Token tok : follow) {
                    putInTable(tok, rule);
                }
                return;
            }

            TokenSet firsts = sets.getFirst(it.next());
            for (Token tok : firsts) {
                putInTable(tok, rule);
            }

            if (firsts.hasEpsilon()) {
                if (it.hasNext()) {
                    process(rule, it);
                } else {
//                    System.out.println("!!!!! Special case " + rule);
                    // TODO ?
                }
            }
        }

        private void putInTable(Token token, ProductionRule rule) throws GrammarException {
            Symbol sym = rule.getSymbol();

            SubTable row = table.get(sym);
            if (row == null) {
                row = new SubTable();
                table.put(sym, row);
            }

            CnfRule lastValue = row.put(token, rule);
            if (lastValue != null) {
                throw new GrammarException("There are at least two rules for "
                        + "[" + sym + ", " + token + "]:\n"
                        + "- " + lastValue + "\n"
                        + "- " + rule);
            }
        }
    }
}
