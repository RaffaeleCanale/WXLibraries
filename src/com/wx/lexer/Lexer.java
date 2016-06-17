package com.wx.lexer;

import com.wx.lexer.tokens.*;
import com.wx.util.future.IoIterator;
import com.wx.util.pair.Pair;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Set;

/**
 * Parses any text input stream into token of the following types.
 *
 * @author Raffaele Canale (raffaelecanale@gmail.com)
 * @version 1.0
 */
public class Lexer implements IoIterator<Token> {

    private final PositionedReader reader;
    private final Set<Character> separators;
    private final Set<Character> specials;
    private final String singleLineCommentsMarker;
    private final Pair<String, String> multiLineCommentsMarker;
    private final Set<String> keyWords;
    private final boolean keyWordsIgnoreCase;
    private final boolean ignoreNumericLitterals;
    private final boolean ignoreEol;


    private boolean eofReached;
    private Position position;

    Lexer(String singleLineCommentsMarker,
          Pair<String, String> multiLineCommentsMarker,
          Set<Character> separators, Set<Character> specials,
          Set<String> keyWords, boolean ignoreEol, boolean keyWordsIgnoreCase,
          boolean ignoreNumericLitterals,
          InputStream inputStream) {
        this.separators = separators;
        this.specials = specials;
        this.reader = new PositionedReader(inputStream);
        this.singleLineCommentsMarker = singleLineCommentsMarker;
        this.multiLineCommentsMarker = multiLineCommentsMarker;
        this.keyWords = keyWords;
        this.ignoreEol = ignoreEol;
        this.keyWordsIgnoreCase = keyWordsIgnoreCase;
        this.ignoreNumericLitterals = ignoreNumericLitterals;

        eofReached = false;
        this.position = null;
    }

    /**
     * Creates a new instance of {@link LexerBuilder}. <br><br>
     * <p>
     * <i>(Same as calling {@code new LexerBuilder()})</i>
     *
     * @return A new {@code LexerBuilder}
     */
    public static LexerBuilder create() {
        return new LexerBuilder();
    }

    /**
     * @return {@code true} if there are more tokens to read
     */
    @Override
    public boolean hasNext() {
        return !eofReached;
    }

    /**
     * Parses the next token and returns its type. <br><br>
     * <p>
     * When the last token has been reached, this method returns the type {@code EOF}.
     *
     * @return Type of the currently parsed token
     *
     * @throws IOException
     */
    @Override
    public Token next() throws IOException {
        if (!hasNext()) {
            throw new IllegalStateException("EOF already reached");
        }

        return parseNextVal();
    }

    private Token parseNextVal() throws IOException {
        Character currentChar = reader.read();
        // skip separators
        while (currentChar != null && separators.contains(currentChar)) {
            currentChar = reader.read();
        }


        savePosition();
        if (currentChar == null) {
            eofReached = true;
            reader.close();
            return new EOFToken(position);
        } else if (!ignoreEol && currentChar == '\n') {
            return new EOLToken(position);
        }

        if (checkForCommentsMarker(currentChar)) {
            return parseNextVal();
        }

        if (specials.contains(currentChar)) {
            return new SpecialCharToken(currentChar, position);
        }

        StringBuilder buffer = new StringBuilder().append(currentChar);
        NumAnalyzer num = new NumAnalyzer(currentChar);

        reader.mark(1);
        currentChar = reader.read();
        while (currentChar != null && !isDelimiter(currentChar)) {
            buffer.append(currentChar);
            num.feed(currentChar);

            reader.mark(1);
            currentChar = reader.read();
        }
        reader.reset();

        String value = buffer.toString();
        if (num.isNumber()) {
            double numVal = Double.parseDouble(value);
            return new NumberToken(numVal, position);

        } else {
            String potentialKeyWord = keyWordsIgnoreCase ? value.toLowerCase() : value;

            if (keyWords.contains(potentialKeyWord)) {
                return new KeyWordToken(potentialKeyWord, position);
            } else {
                return new StringToken(value, position);
            }
        }
    }

    private boolean checkForCommentsMarker(Character read) throws IOException {
        if (singleLineCommentsMarker != null
                && singleLineCommentsMarker.startsWith(read + "")) {
            reader.mark(singleLineCommentsMarker.length() - 1);
            if (testCommentsMarker(read + "", singleLineCommentsMarker)) {
                skipUntil("\n");
                return true;
            } else {
                reader.reset();
            }
        }
        if (multiLineCommentsMarker != null
                && multiLineCommentsMarker.get1().startsWith(read + "")) {
            reader.mark(multiLineCommentsMarker.get1().length() - 1);
            if (testCommentsMarker(read + "", multiLineCommentsMarker.get1())) {
                skipUntil(multiLineCommentsMarker.get2());
                return true;
            } else {
                reader.reset();
            }
        }
        return false;
    }

    private boolean testCommentsMarker(String value, String marker) throws IOException {
        assert value.length() <= marker.length();

        if (marker.length() == value.length()) {
            return marker.equals(value);

        } else {
            if (!marker.startsWith(value)) {
                return false;
            }

            Character read = reader.read();
            return read != null && testCommentsMarker(value + read, marker);
        }
    }

    private boolean isDelimiter(char c) {
        return (!ignoreEol && c == '\n') || separators.contains(c) || specials.contains(c);
    }

    private void skipUntil(String marker) throws IOException {
        int markerSize = marker.length();
        String buffer = "";

        for (int i = 0; i < markerSize; i++) {
            Character read = reader.read();
            if (read == null) {
                return;
            }
            buffer += read;
        }

        while (!buffer.equals(marker)) {
            Character read = reader.read();
            if (read == null) {
                return;
            }
            buffer = buffer.substring(1) + read;
        }
    }

    private void savePosition() {
        position = reader.getCurrentPosition();
    }


    private static class PositionedReader {

        private final InputStream stream;

        private int lineNo;
        private int charNo;
        private boolean endReached;
        private boolean startNewlineNext;

        private Position markedPosition;
        private boolean markedEndReached;
        private boolean markedNewlineNext;

        public PositionedReader(InputStream stream) {
            this.stream = new BufferedInputStream(stream);
            assert this.stream.markSupported();

            lineNo = 1;
            charNo = 0;
        }

        public Character read() throws IOException {
            if (endReached) {
                return null;
            }

            int read = stream.read();

            if (startNewlineNext) {
                startNewlineNext = false;
                lineNo++;
                charNo = 1;
            } else {
                charNo++;
            }

            if (read < 0) {
                endReached = true;
                return null;
            } else {
                char result = (char) read;
                if (result == '\n') {
                    startNewlineNext = true;
                }
                return result;
            }
        }

        public synchronized void mark(int readLimit) {
            stream.mark(readLimit);
            markedPosition = getCurrentPosition();
            markedEndReached = endReached;
            markedNewlineNext = startNewlineNext;
        }

        public synchronized void reset() throws IOException {
            stream.reset();
            lineNo = markedPosition.getLineNo();
            charNo = markedPosition.getCharNo();
            endReached = markedEndReached;
            startNewlineNext = markedNewlineNext;
        }

        public Position getCurrentPosition() {
            return new Position(lineNo, charNo);
        }

        public void close() throws IOException {
            stream.close();
        }
    }

    /**
     * Simple class that verifies (char by char) if the token is a valid number.
     */
    private final class NumAnalyzer {

        private boolean isNumeric;
        private boolean dotFound;

        public NumAnalyzer(char c) {
            isNumeric = true;
            dotFound = false;
            feed(c);
        }

        public void feed(char c) {
            if (c == '.') {
                if (dotFound) {
                    isNumeric = false;
                } else {
                    dotFound = true;
                }

            } else if (!isNumeric(c)) {
                isNumeric = false;
            }
        }

        public boolean isNumber() {
            return !ignoreNumericLitterals && isNumeric;
        }

        private boolean isNumeric(char c) {
            return c >= '0' && c <= '9';
        }
    }
}
