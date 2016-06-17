package com.wx.grammar.cnf.rule;

import com.wx.grammar.symbol.Symbol;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Stream;

/**
 * This class represents a production rule or an epsilon rule.
 *
 * <ul>
 * <li>A := BC...</li>
 * <li>S := &epsilon;</li>
 * </ul>
 *
 * @author Raffaele Canale (raffaelecanale@gmail.com)
 * @version 0.1
 */
public class ProductionRule extends CnfRule implements Iterable<Symbol> {

    private final List<Symbol> elements;

    /**
     * Constructs an epsilon rule. <br><br>
     * symbol := &epsilon;
     *
     * @param symbol Symbol of the rule
     */
    public ProductionRule(Symbol symbol) {
        this(symbol, new ArrayList<>());
    }

    /**
     * Constructs a new rule. <br>
     *
     * A rule in Chomsky Normal Form is formed with a concatenation of symbols.
     *
     * @param symbol   Symbol of the rule
     * @param elements Concatenation of symbols (or empty list for an epsilon
     *                 rule)
     */
    public ProductionRule(Symbol symbol, List<Symbol> elements) {
        super(symbol);
        this.elements = Collections.unmodifiableList(elements);
    }

    @Override
    public Iterator<Symbol> iterator() {
        return elements.iterator();
    }

    public Stream<Symbol> stream() {
        return elements.stream();
    }

    public List<Symbol> getElements() {
        return elements;
    }

    /**
     * Get the last symbol of the rule
     * 
     * @return Last symbol of this rule
     */
    public Symbol lastSymbol() {
        assert !isEpsilon() : "Epsilon has no last symbol";
        return elements.get(elements.size() - 1);
    }

    /**
     * 
     * @return true if this is an epsilon rule
     */
    public boolean isEpsilon() {
        return elements.isEmpty();
    }
    
    @Override
    public String toString() {
        if (isEpsilon()) {
            return getSymbol() + " := \u03B5";
        }
        return (getSymbol() + " := " + elements.toString()).replaceAll("\n", "\\\\n");
    }
}
