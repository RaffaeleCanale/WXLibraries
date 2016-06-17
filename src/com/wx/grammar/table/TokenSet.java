package com.wx.grammar.table;

import com.wx.lexer.tokens.Token;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Objects;
import java.util.Set;


/**
 *
 * @author Raffaele Canale (raffaelecanale@gmail.com)
 * @version 0.1
 */
public class TokenSet implements Iterable<Token> {

    private final Set<Token> set;
    private final boolean containsEpsilon;

    private TokenSet(Set<Token> set, boolean containsEpsilon) {
        this.set = set;
        this.containsEpsilon = containsEpsilon;
    }

    public boolean hasEpsilon() {
        return containsEpsilon;
    }
    
    @Override
    public Iterator<Token> iterator() {
        return set.iterator();
    }
    
    public boolean contains(Token token) {
        return set.stream().anyMatch((t) -> (t.compareTokenType(token)));
    }

    @Override
    public String toString() {
        String result = "[ ";
        result = set.stream().map((token) -> token + " ").reduce(result, String::concat);
        if (hasEpsilon()) {
            result += "\u03B5 ";            
        }
        
        return result + "]";
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 89 * hash + Objects.hashCode(this.set);
        hash = 89 * hash + (this.containsEpsilon ? 1 : 0);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final TokenSet other = (TokenSet) obj;
        if (!Objects.equals(this.set, other.set)) {
            return false;
        }
        if (this.containsEpsilon != other.containsEpsilon) {
            return false;
        }
        return true;
    }

    
    
    

    public static class Builder {

        private final Set<Token> first = new HashSet<>();
        private boolean containsEpsilon;
        
        public Builder addToken(Token token) {
            first.add(token);
            return this;
        }
        
        public Builder addEpsilon() {
            containsEpsilon = true;
            return this;
        }
        
        public TokenSet build() {
            return new TokenSet(first, containsEpsilon);
        }
        
        public boolean addFromBuilder(Builder otherBuilder) {
            return first.addAll(otherBuilder.first);
        }
        
        public boolean hasEpsilon() {
            return containsEpsilon;
        }
        
        
        
//        public Builder addSet(FirstsSet set) {
//            first.addAll(set.first);
//            
//            return this;
//        }

        @Override
        public String toString() {
            return build().toString();
        }
    }
}
