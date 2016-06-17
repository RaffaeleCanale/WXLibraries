package com.wx.grammar.cf;

import com.wx.grammar.GrammarException;
import com.wx.grammar.cf.element.Concatenation;
import com.wx.grammar.cf.element.Element;
import com.wx.grammar.cnf.CNFGrammar;
import com.wx.grammar.symbol.Symbol;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Raffaele Canale (raffaelecanale@gmail.com)
 * @version 0.1
 */
public class CFGrammar {

    private final Map<Symbol, Element> productions;
    private final Symbol startSymbol;

    private CFGrammar(Map<Symbol, Element> productions, Symbol startSymbol) {
        this.productions = Collections.unmodifiableMap(productions);
        this.startSymbol = startSymbol;
    }
    
    public Element getProduction(Symbol sym) {
        return productions.get(sym);
    }
    
    public Element getStartProduction() {
        return productions.get(startSymbol);
    }

    public CNFGrammar normalize() throws GrammarException {
        CNFGrammar.Builder builder = new CNFGrammar.Builder(startSymbol);

        for (Map.Entry<Symbol, Element> production : productions.entrySet()) {
            builder.addProductiveRule(production.getKey(),
                    production.getValue().normalize(builder));
        }

        return builder.build();
    }

    public static class Builder {

        private final Map<Symbol, Element> productions = new HashMap<>();
        private final Symbol startSymbol;

        public Builder(Symbol startSymbol) {
            this.startSymbol = startSymbol;
        }

        public Builder declareRule(Symbol sym, Element... rules) {
            Element rule = rules.length == 1 ? rules[0]
                    : new Concatenation(rules);

            productions.put(sym, rule);
            
            return this;
        }
        
        public CFGrammar build() {
            return new CFGrammar(productions, startSymbol);
        }
    }
    
    

}
