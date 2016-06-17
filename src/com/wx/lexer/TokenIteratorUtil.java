package com.wx.lexer;

/**
 *
 * @author Raffaele Canale (raffaelecanale@gmail.com)
 * @version 0.1
 */
public class TokenIteratorUtil {
    
//    public static Iterator<Token> fromList(List<Token> tokens) {
//        return new ListTokenIterator(tokens);
//    }
//
//
//    private static class ListTokenIterator implements Iterator<Token> {
//
//        private final LinkedList<Token> tokens;
//
//        public ListTokenIterator(List<Token> tokens) {
//            this.tokens = new LinkedList<>(tokens);
//            this.tokens.add(new EOFToken(null)); // Actually risky
//        }
//
//
////        @Override
////        public Token next() {
////            assert !tokens.isEmpty();
////
////            return tokens.getFirst();
////        }
//
//        @Override
//        public boolean hasNext() {
//            return tokens.size() > 1;
//        }
//
//        @Override
//        public Token next() throws IOException {
//            tokens.removeFirst();
//        }
//
//    }
}
