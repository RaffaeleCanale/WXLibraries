package com.wx.grammar.cnf;

import com.wx.grammar.GrammarException;
import com.wx.grammar.symbol.Symbol;
import com.wx.grammar.cnf.rule.ProductionRule;
import com.wx.grammar.cnf.rule.CnfRule;
import com.wx.grammar.cnf.rule.TerminalRule;
import com.wx.lexer.tokens.Token;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 *
 * Representation of a Grammar in Chomsky Normal Form. That includes rules of
 * the following form:
 * <ul>
 * <li>A := BC...</li>
 * <li>A := a</li>
 * <li>S := &epsilon;</li>
 * <li>...</li>
 * </ul>
 *
 * To build a Grammar, use the {@link Builder}
 *
 *
 * <br><br><br>See http://en.wikipedia.org/wiki/Chomsky_normal_form
 *
 * @author Raffaele Canale (raffaelecanale@gmail.com)
 * @version 0.1
 */
public class CNFGrammar {

    private final Symbol startSymbol;
    private final Set<ProductionRule> productiveRules;  // includes epsilon symbols
    private final Set<TerminalRule> terminalRules;

    private CNFGrammar(Symbol startSymbol, Set<ProductionRule> productiveSymbols,
            Set<TerminalRule> terminalSymbols) throws GrammarException {
        this.startSymbol = startSymbol;
        this.productiveRules = Collections.unmodifiableSet(productiveSymbols);
        this.terminalRules = Collections.unmodifiableSet(terminalSymbols);

        testSymbolsValidity();
    }

    public TerminalRule getTerminalRule(Symbol sym) {
        // terminal rule symbols are unique
        return terminalRules.stream().filter(t -> t.getSymbol().equals(sym))
                .findAny().orElse(null);
    }
    
    /**
     * Get the set of all productive rules (including epsilon rules)
     *
     * @return The set of productive rules
     */
    public Set<ProductionRule> getProductiveRules() {
        return productiveRules;
    }

    /**
     * Get the set of all terminal rules,
     *
     * @return The set of terminal rules
     */
    public Set<TerminalRule> getTerminalRules() {
        return terminalRules;
    }

    /**
     * Get the starting symbol to use for parsing.
     *
     * @return Starting symbol
     */
    public Symbol getStartSymbol() {
        return startSymbol;
    }

    private boolean isDeclared(Symbol sym) {
        return Stream.concat(productiveRules.stream(), terminalRules.stream())
                .map(CnfRule::getSymbol).anyMatch(s -> s.equals(sym));
    }

    private void testSymbolsValidity() throws GrammarException {
        if (!isDeclared(startSymbol)) {
            throw new GrammarException("Start symbol (" + startSymbol + ") "
                    + "not declared");
        }

        Set<Symbol> unusedSymbols = getAllSymbols();
        unusedSymbols.remove(startSymbol);

        for (ProductionRule rule : productiveRules) {
            for (Symbol sym : rule) {
                if (!isDeclared(sym)) {
                    throw new GrammarException("Undeclared symbol (" + sym + ") "
                            + "in the following rule: " + rule);
                }
                unusedSymbols.remove(sym);
            }
        }

        if (!unusedSymbols.isEmpty()) {
            throw new GrammarException("Unused symbols: " + unusedSymbols);
        }
        
        Set<Symbol> productiveSymbols = productiveRules.stream()
                .map(CnfRule::getSymbol).collect(Collectors.toSet());
        Set<Symbol> terminalSymbols = terminalRules.stream()
                .map(CnfRule::getSymbol).collect(Collectors.toSet());
        
        productiveSymbols.retainAll(terminalSymbols);
        if (!productiveSymbols.isEmpty()) {
            throw new GrammarException("Terminal symbols must be unique\n"
                    + productiveSymbols);
        }
        
        if (terminalSymbols.size() != terminalRules.size()) {
            throw new GrammarException("A terminal rule is declared twice");
        }
    }

    private Set<Symbol> getAllSymbols() {
        return Stream.concat(productiveRules.stream(), terminalRules.stream())
                .map(CnfRule::getSymbol).collect(Collectors.toSet());
    }

    @Override
    public String toString() {
        return Stream.concat(
                productiveRules.stream()
                        .sorted((s1, s2) -> s1.getSymbol().equals(startSymbol) ? -10 : 
                                s1.getSymbol().toString().compareTo(s2.getSymbol().toString())),
                terminalRules.stream()).map(CnfRule::toString)
                .collect(Collectors.joining("\n"));        
    }

    /**
     * Builder to construct a {@link CNFGrammar}
     */
    public static class Builder {

        private final Symbol startSymbol;
        private final Set<ProductionRule> productiveSymbols = new HashSet<>();
        private final Set<TerminalRule> terminalSymbols = new HashSet<>();
        private boolean simplify;

        /**
         * Constructs a new Grammar builder
         *
         * @param startSymbol Grammar's start symbol
         */
        public Builder(Symbol startSymbol) {
            this.startSymbol = startSymbol;
        }
        
        /**
         * Declare an epsilon rule
         *
         * @param sym Symbol of the rule
         *
         * @return A reference to the created rule
         */
        public ProductionRule addEpsilonSymbol(Symbol sym) {
            ProductionRule rule = new ProductionRule(sym);
            productiveSymbols.add(rule);

            return rule;
        }

        /**
         * Declare a terminal rule
         *
         * @param sym   Symbol of the rule
         * @param token Terminal token
         *
         * @return A reference to the created rule
         */
        public TerminalRule addTerminalRule(Symbol sym, Token token) {
            TerminalRule rule = new TerminalRule(sym, token);
            terminalSymbols.add(rule);

            return rule;
        }

        /**
         * Declare a productive rule
         *
         * @param sym  Symbol of the rule
         * @param rule Symbols forming (concatenation) the rule
         *
         * @return A reference to the created rule
         */
        public ProductionRule addProductiveRule(Symbol sym, Symbol... rule) {
            ProductionRule r = new ProductionRule(sym, Arrays.asList(rule));
            productiveSymbols.add(r);

            return r;
        }
        
        public Builder simplify() {
            this.simplify = true;
            
            return this;
        }

        /**
         * Build the grammar
         *
         * @return The created grammar
         *
         * @throws com.wx.grammar.GrammarException
         */
        public CNFGrammar build() throws GrammarException {
            if (simplify) {
                performSimplifications();
            }
            return new CNFGrammar(startSymbol, productiveSymbols, terminalSymbols);
        }
        
        private void performSimplifications() {
            for (ProductionRule productionRule : productiveSymbols) {
                if (productionRule.getElements().size() == 1) {
                    // TODO
                }
            }
        }
    }

}
