package com.wx.grammar.table;

import com.wx.grammar.GrammarException;
import com.wx.grammar.cnf.CNFGrammar;
import com.wx.grammar.cnf.rule.ProductionRule;
import com.wx.grammar.cnf.rule.TerminalRule;
import com.wx.grammar.symbol.Symbol;
import com.wx.lexer.tokens.EOFToken;
import com.wx.properties.property.Property;
import com.wx.properties.property.SimpleProperty;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 *
 * This class allows to build the first and follow sets of a {@link CNFGrammar}.
 *
 * @author Raffaele Canale (raffaelecanale@gmail.com)
 * @version 0.1
 */
public class FirstFollowSets {

    private final Map<Symbol, TokenSet> first;
    private final Map<Symbol, TokenSet> follow;

    /**
     * Constructs the sets from the given grammar
     *
     * @param grammar Grammar to analyze
     *
     * @throws GrammarException
     */
    public FirstFollowSets(CNFGrammar grammar) throws GrammarException {
        Builder builder = new Builder(grammar);
//        builder.buildFirst(grammar);
//        builder.buildFollow(grammar);

        first = builder.buildFirst();
        follow = builder.buildFollow();
    }

    /**
     * Get the first set for the given symbol
     *
     * @param s Symbol of the first set
     *
     * @return The first set of the given symbol
     */
    public TokenSet getFirst(Symbol s) {
        return first.get(s);
    }

    /**
     * Get the follow set for the given symbol
     *
     * @param s Symbol of the follow set
     *
     * @return The follow set of the given symbol
     */
    public TokenSet getFollow(Symbol s) {
        return follow.get(s);
    }

    private class Builder {

        private final CNFGrammar grammar;
        private final Map<Symbol, TokenSet.Builder> first = new HashMap<>();
        private final Map<Symbol, TokenSet.Builder> follow = new HashMap<>();

        public Builder(CNFGrammar grammar) {
            this.grammar = grammar;
        }

        private Map<Symbol, TokenSet> build(Map<Symbol, TokenSet.Builder> map) {
            Map<Symbol, TokenSet> result = new HashMap<>();

            for (Map.Entry<Symbol, TokenSet.Builder> entry : map.entrySet()) {
                result.put(entry.getKey(), entry.getValue().build());
            }

            return result;
        }

        private TokenSet.Builder builder(Map<Symbol, TokenSet.Builder> map, Symbol symbol) {
            TokenSet.Builder builder = map.get(symbol);
            if (builder == null) {
                builder = new TokenSet.Builder();
                map.put(symbol, builder);
            }

            return builder;
        }

        //<editor-fold defaultstate="collapsed" desc="FIRST">
        public Map<Symbol, TokenSet> buildFirst() throws GrammarException {
            // Add epsilons
            grammar.getProductiveRules().stream().filter(
                    ProductionRule::isEpsilon).forEach(
                            e -> builder(first, e.getSymbol()).addEpsilon());

            // Add terminals
            for (TerminalRule terminalRule : grammar.getTerminalRules()) {
                builder(first, terminalRule.getSymbol()).
                        addToken(terminalRule.getExpectedToken());
            }

            // Add productive rules until nothing changes
            Set<ProductionRule> prod = grammar.getProductiveRules();
            boolean changed;
            do {
                changed = addFirsts(prod);
            } while (changed);

            // return
            return build(first);
        }

        private boolean addFirsts(Set<ProductionRule> prod) {
            boolean changed = false;
            for (ProductionRule rule : prod) {
                if (addFirsts(rule)) {
                    changed = true;
                }
            }

            return changed;
        }

        private boolean addFirsts(ProductionRule rule) {
            boolean changed = false;

            TokenSet.Builder builder = builder(first, rule.getSymbol());

            for (Symbol el : rule) {
                TokenSet.Builder subFirst = first.get(el);

                if (subFirst == null) {
                    // Element does not have a first yet, we should break
                    return true;
                }

                if (builder.addFromBuilder(subFirst)) {
                    changed = true;
                }

                if (!subFirst.hasEpsilon()) {
                    return changed;
                }
            }

            if (!builder.hasEpsilon()) {
                builder.addEpsilon();
                return true;
            }

            return false;
        }
//</editor-fold>

        //<editor-fold defaultstate="collapsed" desc="FOLLOW">
        public Map<Symbol, TokenSet> buildFollow() {
            // Add EOF to start symbol
            builder(follow, grammar.getStartSymbol()).
                    addToken(new EOFToken(null));
            
            boolean changed;
            do {
                changed = addFollow(grammar.getProductiveRules());
            } while (changed);
            
            return build(follow);
        }
        
        private boolean addFollow(Set<ProductionRule> rules) {
            boolean changed = false;
            for (ProductionRule rule : rules) {
                if (addFollow(rule)) {
                    changed = true;
                }
            }
            
            return changed;
        }
        
        private boolean addFollow(ProductionRule rule) {
            if (rule.isEpsilon()) {
                return false;
            }
            
            Property<Boolean> changed = new SimpleProperty<>(false);
            
            rule.stream().reduce((a, b) -> {
                
                TokenSet.Builder followA = builder(follow, a);
                TokenSet.Builder firstB = builder(first, b);
                
                if (followA.addFromBuilder(firstB)) {
                    changed.set(true);
                }
                
                if (firstB.hasEpsilon()) {
                    if (followA.addFromBuilder(builder(follow, b))) {
                        changed.set(true);
                    }
                }
                
                return b;
            });
            
            TokenSet.Builder lastFollow = builder(follow, rule.lastSymbol());
            if (lastFollow.addFromBuilder(builder(follow, rule.getSymbol()))) {
                changed.set(true);
            }
            
            return changed.get().get();
        }
//</editor-fold>
    }
}
