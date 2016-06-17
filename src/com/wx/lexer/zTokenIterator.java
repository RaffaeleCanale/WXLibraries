package com.wx.lexer;

import com.wx.lexer.tokens.Token;
import java.io.IOException;

/**
 *
 * @author Canale
 */
public interface zTokenIterator {

    public boolean hasNext();
    
    public Token next() throws IOException;
}
