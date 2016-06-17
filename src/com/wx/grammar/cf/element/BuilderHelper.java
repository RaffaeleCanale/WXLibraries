package com.wx.grammar.cf.element;

import com.wx.grammar.cf.element.Concatenation;
import com.wx.grammar.cf.element.Element;
import com.wx.grammar.cf.element.Kleene;
import com.wx.grammar.cf.element.Litteral;
import com.wx.grammar.cf.element.NumberLit;
import com.wx.grammar.cf.element.Optional;
import com.wx.grammar.cf.element.Or;
import com.wx.grammar.cf.element.Special;
import com.wx.grammar.cf.element.Variable;

/**
 *
 * @author Raffaele Canale (raffaelecanale@gmail.com)
 * @version 0.1
 */
public class BuilderHelper {
    
    public static Concatenation concat(Element... rules) {
        return new Concatenation(rules);
    }
    
    public static Or or(Element... rules) {
        return new Or(rules);
    }
    
    public static Litteral litteral() {
        return new Litteral();
    }
    
    public static Variable keyWord(String value) {
        return new Variable(value);
    }
    
    public static Optional optional(Element... rule) {
        if (rule.length == 1) {
            return new Optional(rule[0]);
        }
        return new Optional(concat(rule));
    }
    
    public static Kleene kleene(Element... rule) {
        if (rule.length == 1) {
            return new Kleene(rule[0]);
        }
        return new Kleene(concat(rule));
    }
    
    public static Special special(char value) {
        return new Special(value);
    }
    
    public static NumberLit number() {
        return new NumberLit();
    }
    
    public static Kleene kleene(Element rule) {
        return new Kleene(rule);
    }

}
