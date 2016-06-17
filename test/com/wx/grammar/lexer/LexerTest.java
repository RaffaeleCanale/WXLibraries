package com.wx.grammar.lexer;

import com.wx.lexer.Lexer;
import com.wx.lexer.Position;
import com.wx.lexer.Types;
import com.wx.lexer.tokens.*;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;

/**
 * @author canale
 */
public class LexerTest extends Assert {

    private static final String lineComments = "//";
    private static final String[] multiLineComments = {"/*", "*/"};
    private static final char[] separators = {' '};
    private static final char[] specials = {'\t', '/', '$'};
    private static final String[] keyWords = {"foo", "bar"};

    private Token word(String s, int l, int c) {
        return new StringToken(s, new Position(l, c));
    }

    private Token keyword(String s, int l, int c) {
        return new KeyWordToken(s, new Position(l, c));
    }

    private Token spe(char s, int l, int c) {
        return new SpecialCharToken(s, new Position(l, c));
    }

    private Token num(double s, int l, int c) {
        return new NumberToken(s, new Position(l, c));
    }

    private Token eof(int l, int c) {
        return new EOFToken(new Position(l, c));
    }

    private Token eol(int l, int c) {
        return new EOLToken(new Position(l, c));
    }


//    private static final String testSpaces = " word   word   ";
//    private static final String testComments = "word//comment";
//    private static final String testMultiComments = "word/* comment"
//            + "\nmore comment"
//            + "\n*/"
//            + "   word";
//    private static final String testNumberedWord = "wo21rd22";
//    private static final String testNewLine = "word\nword\n";
//    private static final String testSpecials = "\t  $   ";
//    private static final String testNumber = "24 word";
//    private static final String testNumberReal = "24.30 word";
//    private static final String testBadComment = "word /* word";
//    private static final String testAmbiguous  = "$word $$ $";

    private void test(String value, int eofL, int eofC, Token... expected) throws IOException {
        Lexer lexer = buildLexer(value);

        for (Token token : expected) {
            assertNextToken(lexer, token);
        }

        assertNextToken(lexer, eof(eofL, eofC));

        assertFalse("Lexer did not reach end", lexer.hasNext());
    }

    private void assertNextToken(Lexer lexer, Token expected) throws IOException {
        assertTrue("Lexer reached end, but " + expected + " toString", lexer.hasNext());
        final Token found = lexer.next();
        assertEquals("\nExpected: " + expected.formatted()
                +"\nFound:" + found.formatted() + "\n\n", expected, found);
    }

    protected Lexer buildLexer(String text) {
        return Lexer.create()
                .lineCommentsMarker(lineComments)
                .multiCommentsMarkers(multiLineComments[0], multiLineComments[1])
                .separators(separators)
                .specials(specials)
                .keyWords(keyWords)
                .build(new ByteArrayInputStream(text.getBytes()));
    }

//    @Test(toString = IllegalStateException.class)
//    public void testInterface1() throws IOException {
//        Lexer lexer = buildLexer("word");
//        lexer.parseNext();
//        lexer.getNumVal();
//    }
//
//    @Test(toString = IllegalStateException.class)
//    public void testInterface2() throws IOException {
//        Lexer lexer = buildLexer("word");
//        lexer.parseNext();
//        lexer.getSpecialVal();
//    }
//
//    @Test(toString = IllegalStateException.class)
//    public void testInterface3() throws IOException {
//        Lexer lexer = buildLexer("23");
//        lexer.parseNext();
//        lexer.getStringVal();
//    }

    @Test
    public void testSpaces() throws IOException {
        test("  word    word   ", 1, 18, word("word", 1, 3), word("word", 1, 11));
    }

    @Test
    public void testComments() throws IOException {
        test("word//comment", 1, 14, word("word", 1, 1));
    }

    @Test
    public void testPosition() throws IOException {
        String input = "word1 /* comment\n"
                        + "more comment\n"
                        + "*/\n"
                        + "/* */ word2\n"
                        + "24/24     word3";

        test(input, 5, 16,
                word("word1", 1, 1),
                eol(3, 3),
                word("word2", 4, 7),
                eol(4, 12),
                num(24.0, 5, 1),
                spe('/', 5, 3),
                num(24.0, 5, 4),
                word("word3", 5, 11));
    }

    @Test
    public void testMultiLineComments() throws IOException {
        test("word/* comment\n"
                + "more comment\n"
                + "*/   word",
                3, 10,
                word("word", 1, 1),
                word("word", 3, 6));
    }

    @Test
    public void testIntegerNumber() throws IOException {
        test("24 word",
                1, 8,
                num(24.0, 1, 1),
                word("word", 1, 4));
    }

    @Test
    public void testRealNumber() throws IOException {
        test("24.300 word",
                1, 12,
                num(24.3, 1, 1),
                word("word", 1, 8));
    }

    @Test
    public void testNumberedWord() throws IOException {
        test("word word23",
                1, 12,
                word("word", 1, 1),
                word("word23", 1, 6));
    }

    @Test
    public void testMultiLine() throws IOException {
        test("word\n" +
                "word\n",
                3, 1,
                word("word", 1, 1),
                eol(1, 5),
                word("word", 2, 1),
                eol(2, 5));
    }

    @Test
    public void testSpecials() throws IOException {
        test(" $  \t ",
                1, 7,
                spe('$', 1, 2),
                spe('\t', 1, 5));
    }

    @Test
    public void testBadComment() throws IOException {
        test("word /* word2",
                1, 14,
                word("word", 1, 1));
    }

    @Test
    public void testEmpty() throws IOException {
        test("", 1, 1);
    }

    @Test
    public void testSpecial1() throws IOException {
        test("$word wo$rd",
                1, 12,
                spe('$', 1, 1),
                word("word", 1, 2),
                word("wo", 1, 7),
                spe('$', 1, 9),
                word("rd", 1, 10));
    }

    @Test
    public void testSpecial2() throws IOException {
        test("24.1/33 1/1",
                1, 12,
                num(24.1, 1, 1),
                spe('/',1, 5),
                num(33, 1, 6),
                num(1, 1, 9),
                spe('/', 1, 10),
                num(1, 1, 11));
    }

    @Test
    public void testSpecialAndComment() throws IOException {
        test("word1 /// word2",
                1, 16,
                word("word1", 1, 1));
    }

    @Test
    public void testNumWord() throws IOException {
        test("24word",
                1, 7,
                word("24word", 1, 1));
    }

//    private void assertWord(Lexer lexer) throws IOException {
//        assertTrue(lexer.hasMore());
//        assertEquals(Types.WORD, lexer.parseNext());
//        assertEquals("word", lexer.getStringVal());
//    }
//    
//    private void assertEOF(Lexer lexer) throws IOException {
//        assertTrue(lexer.hasMore());
//        assertEquals(Types.EOF, lexer.parseNext());
//        assertFalse(lexer.hasMore());
//    }
//    
//    private void assertSpecial(Lexer lexer, char sp) throws IOException {
//        assertType(lexer, Types.SPECIAL_CHAR);
//        assertEquals(sp, lexer.getSpecialVal());
//    }
//    
//    private void assertType(Lexer lexer, Types type) throws IOException {
//        assertTrue(lexer.hasMore());
//        assertEquals(type, lexer.parseNext());
//    }


//    private class TokenTest {
//        private final Types expectedType;
//
//        public TokenTest(Types expectedType) {
//            this.expectedType = expectedType;
//        }
//
//        public void test(Lexer lexer) throws IOException {
//            assertTrue(lexer.hasMore());
//            assertEquals(expectedType, lexer.parseNext());
//        }
//    }
//
//    private class StrToken extends TokenTest {
//        private final String value;
//
//        public StrToken(String value) {
//            super(Types.WORD);
//            this.value = value;
//        }
//
//        @Override
//        public void test(Lexer lexer) throws IOException {
//            super.test(lexer);
//            assertEquals(value, lexer.getStringVal());
//        }
//    }
//
//    private class NumToken extends TokenTest {
//        private final double value;
//
//        public NumToken(double value) {
//            super(Types.NUMBER);
//            this.value = value;
//        }
//
//        @Override
//        public void test(Lexer lexer) throws IOException {
//            super.test(lexer);
//            assertEquals(value, lexer.getNumVal(), 0.0);
//        }
//    }
//
//    private class SpeToken extends TokenTest {
//        private final char value;
//
//        public SpeToken(char value) {
//            super(Types.SPECIAL_CHAR);
//            this.value = value;
//        }
//
//        @Override
//        public void test(Lexer lexer) throws IOException {
//            super.test(lexer);
//            assertEquals(value, lexer.getSpecialVal());
//        }
//    }
//
//    private class EofToken extends TokenTest {
//
//        public EofToken() {
//            super(Types.EOF);
//        }
//
//        @Override
//        public void test(Lexer lexer) throws IOException {
//            super.test(lexer);
//            assertFalse(lexer.hasMore());
//        }
//
//    }
}
