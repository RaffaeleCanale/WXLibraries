package com.wx.io.sp;

import com.wx.grammar.ParseException;
import com.wx.lexer.Lexer;
import com.wx.lexer.LexerBuilder;
import com.wx.lexer.tokens.EOFToken;
import com.wx.lexer.tokens.SpecialCharToken;
import com.wx.lexer.tokens.StringToken;
import com.wx.lexer.tokens.Token;
import com.wx.util.iterator.PeekIterator;
import com.wx.util.future.Future;

import java.io.IOException;
import java.io.InputStream;

/**
* Created on 20/11/2014
*
* @author Raffaele Canale (raffaelecanale@gmail.com)
* @version 0.1
*/
public class SPObjectParser {

    private static final LexerBuilder LEXER_BUILDER = Lexer.create().specials(':', '=', '\n', '\t').ignoreEOL();
    private SPLexer lexer;


    public SPObject parse(InputStream is) throws IOException, ParseException {
        lexer = new SPLexer(new PeekIterator<>(LEXER_BUILDER.build(is).iterator()));

        lexer.eatNewLines();
        return parseObject(0);
    }

    private SPObject parseObject(int objectLevel) throws IOException, ParseException {
        SPObject.Builder builder = new SPObject.Builder();

        int currentLevel;
        while (lexer.hasMore() && (currentLevel = lexer.peekTabs()) >= objectLevel) {
            lexer.eatTabs();

            parseObjectOrProperty(builder, currentLevel);
            lexer.eatNewLines();
        }

        return builder.build();
    }


    private void parseObjectOrProperty(SPObject.Builder builder, int level) throws IOException, ParseException {
        String name = lexer.eatString().trim();
        if (lexer.nextIs(':')) {
            // Its an object
            lexer.eat(':', '\n');
            lexer.eatNewLines();
            SPObject object = parseObject(level + 1);
            builder.put(name, object);


        } else {
            // Its a value
            lexer.eat('=');
            String value;
            if (lexer.nextIs('\n')) {
                lexer.eat('\n');
                value = parseValue(level+1);
            } else {
                value = lexer.eatString();
            }
            builder.put(name, value.trim());
        }
    }


    private String parseValue(int valueLevel) throws IOException, ParseException {
        boolean stop = false;
        String result = "";
        while (!stop) {
            String tmp = lexer.eatNewLines();

            int currentLevel = lexer.peekTabs();
            if (currentLevel >= valueLevel) {
                lexer.eatTabs();

                result += tmp;
                for (int i = 0; i < (currentLevel - valueLevel); i++) {
                    result += "\t";
                }
                while (lexer.hasMore() && !lexer.nextIs('\n')) {
                    result += lexer.lexer.next().get().getStringValue();
                }
                stop = !lexer.hasMore();
            } else {
                stop = true;
            }
        }

        return result;
    }

    private class SPLexer {
        private final PeekIterator<Future<Token>> lexer;
        private int bufferedLevel = -1;

        private SPLexer(PeekIterator<Future<Token>> lexer) {
            this.lexer = lexer;
        }

        private String eatString() throws IOException, ParseException {
            assert bufferedLevel < 0;

            Token next = lexer.next().get();

            if (!next.compareTokenType(new StringToken(""))) {
                throw new ParseException(new StringToken(""), next);
            }

            return next.getStringValue();
        }


        private boolean nextIs(char special) throws IOException {
            assert bufferedLevel < 0;

            return lexer.peek().get().compareTokenType(new SpecialCharToken(special));
        }


        private void eat(char... special) throws IOException, ParseException {
            assert bufferedLevel < 0;

            for (char s : special) {
                Token token = lexer.next().get();
                if (!token.compareTokenType(new SpecialCharToken(s))) {
                    throw new ParseException(new SpecialCharToken(s), token);
                }
            }
        }

        public boolean hasMore() throws IOException {
            return bufferedLevel >= 0 ||
                    !lexer.peek().get().compareTokenType(new EOFToken());

        }

        public String eatNewLines() throws IOException, ParseException {
            if (bufferedLevel >= 0) {
                return "";
            }

            String result = "";
            while (nextIs('\n')) {
                eat('\n');
                result += "\n";
            }

            return result;
        }

        public int peekTabs() throws IOException, ParseException {
            if (bufferedLevel >= 0) {
                return bufferedLevel;
            }

            return (bufferedLevel = eatTabs());
        }

        public int eatTabs() throws IOException, ParseException {
            if (bufferedLevel >= 0) {
                int level = bufferedLevel;
                bufferedLevel = -1;
                return level;
            }

            int count = 0;
            while (nextIs('\t')) {
                eat('\t');
                count++;
            }

            return count;
        }

    }
}
