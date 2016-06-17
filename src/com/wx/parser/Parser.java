//package com.wx.parser;
//
//import com.wx.lexer.Lexer;
//import com.wx.lexer.TokenIterator;
//import com.wx.lexer.Types;
//import com.wx.lexer.tokens.EOFToken;
//import com.wx.lexer.tokens.EOLToken;
//import com.wx.lexer.tokens.KeyWordToken;
//import com.wx.lexer.tokens.NumberToken;
//import com.wx.lexer.tokens.SpecialCharToken;
//import com.wx.lexer.tokens.StringToken;
//import com.wx.lexer.tokens.Token;
//import java.io.IOException;
//import java.util.Collection;
//import java.util.Iterator;
//
///**
// *
// * @author Canale
// */
//public class Parser implements Iterator<Token> {
//
//    private Token currentToken;
//
//    private final Collection<String> keyWords;
//    private final Lexer lexer;
//
//    public Parser(Lexer lexer, Collection<String> keyWords) throws IOException {
//        this.keyWords = keyWords;
//        this.lexer = lexer;
//        this.currentToken = parseNextToken();
//    }
//
//    @Override
//    public Token current() {
//        if (!hasMore()) {
//            throw new IllegalStateException("Nothing is hasMore");
//        }
//        return currentToken;
//    }
//
//    @Override
//    public boolean hasMore() {
//        return currentToken != null;
//    }
//
//    @Override
//    public void next() throws IOException {
//        if (!hasMore()) {
//            throw new IllegalStateException("Iterator reached end");
//        }
//        currentToken = parseNextToken();
//    }
//
//    private Token parseNextToken() throws IOException {
//        if (!lexer.hasMore()) {
//            return null;
//        }
//        Types type = lexer.parseNext();
//        switch (type) {
//            case EOF:
//                return new EOFToken();
//
//            case EOL:
//                return new EOLToken();
//
//            case NUMBER:
//                return new NumberToken(lexer.getNumVal());
//
//            case SPECIAL_CHAR:
//                return new SpecialCharToken(lexer.getSpecialVal());
//
//            case WORD:
//                String word = lexer.getStringVal();
//
//                if (keyWords.contains(word)) {
//                    return new KeyWordToken(word);
//                } else {
//                    return new StringToken(word);
//                }
//            default:
//                throw new AssertionError();
//        }
//
//    }
//
//}
