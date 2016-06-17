package com.wx.grammar.table;

import com.wx.grammar.symbol.Symbol;
import com.wx.grammar.cnf.rule.ProductionRule;
import com.wx.lexer.tokens.Token;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Raffaele Canale (raffaelecanale@gmail.com)
 * @version 0.1
 */
public class ExpectedParseTable {
    
    private final Map<Symbol, Map<Token, ProductionRule>> table = new HashMap<>();
    
    public void add(Symbol s, Token t, ProductionRule p) {
        Map<Token, ProductionRule> map = table.get(s);
        if (map == null) {
            map = new HashMap<>();
            table.put(s, map);
        }
        
        map.put(t, p);
    }
    
    public ProductionRule get(Symbol s, Token t) {
        Map<Token, ProductionRule> map = table.get(s);
        
        return map == null ? null : map.get(t);
    }

}
