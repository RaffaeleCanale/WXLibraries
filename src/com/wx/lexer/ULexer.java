package com.wx.lexer;

import com.wx.lexer.tokens.EOFToken;
import com.wx.lexer.tokens.EOLToken;
import com.wx.lexer.tokens.KeyWordToken;
import com.wx.lexer.tokens.Token;
import com.wx.util.iterator.PeekIterator;
import com.wx.util.future.Future;
import com.wx.util.future.IoIterator;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

/**
 * Created on 24/01/2015
 *
 * @author Raffaele Canale (raffaelecanale@gmail.com)
 * @version 0.1
 */
public class ULexer implements IoIterator<Token> {

    private final PeekIterator<Future<Token>> lexer;
    private final boolean ignoreEOF;

    public ULexer(Lexer lexer, boolean ignoreEOF) {
        this.lexer = new PeekIterator<>(lexer.iterator());
        this.ignoreEOF = ignoreEOF;
    }

    @Override
    public boolean hasNext() {
        if (ignoreEOF) {
            try {
                final Token peek = lexer.peek().get();
                return !peek.compareTokenType(new EOFToken());
            } catch (IOException e) {
                return true;
            }
        } else {
            return lexer.hasNext();
        }
    }

    @Override
    public Token next() throws IOException {
        return lexer.next().get();
    }

    public Token peek() throws IOException {
        return lexer.peek().get();
    }

    public List<Token> parseUntilKeyword() throws IOException {
        final LinkedList<Token> result = new LinkedList<>();

        while (hasNext() && peek().getClass() != KeyWordToken.class) {
//            result += peek().getStringValue() + " ";
            result.add(next());
        }

        return result;
    }

    public boolean skipNewlines() throws IOException {
        EOLToken eol = new EOLToken();

        boolean changed = false;
        while (lexer.hasNext() && peek().compareTokenType(eol)) {
            lexer.next();
            changed = true;
        }

        return changed;
    }
}
