package com.wx.lexer.tokens;

import com.wx.lexer.Position;
import org.junit.Test;

import java.util.LinkedList;
import java.util.List;
import java.util.function.Supplier;

import static org.junit.Assert.*;

public class TokenTest {

    @Test
    public void testPositionInEquals1() {
        final EOFToken a = new EOFToken();
        final EOFToken b = new EOFToken();

        assertEquals(a, b);
    }

    @Test
    public void testPositionInEquals2() {
        final EOFToken a = new EOFToken(new Position(22, 1));
        final EOFToken b = new EOFToken(new Position(22, 1));

        assertEquals(a, b);
    }

    @Test
    public void testPositionInEquals3() {
        final EOFToken a = new EOFToken(new Position(22, 1));
        final EOFToken b = new EOFToken(new Position(22, 2));

        assertNotEquals(a, b);
    }

    @Test
    public void testEqualsDifferentTokens() {
        assertNotEquals(new EOFToken(), new EOLToken());
    }

    @Test
    public void testEofType() {
        final EOFToken eof1 = new EOFToken();
        final EOFToken eof2 = new EOFToken(new Position(22, 1));

        final Token other1 = new EOLToken();
        final Token other2 = new StringToken("");

        new TypeTest().sameType(eof1, eof2)
                .differentType(other1, other2).test();
    }


    @Test
    public void testStringToken() {
        final Token t1 = new StringToken("foo");
        final Token t2 = new StringToken("foo");
        final Token t3 = new StringToken("bar");
        final Token t4 = new StringToken("bar", new Position(22, 1));

        final Token other1 = new EOLToken();
        final Token other2 = new KeyWordToken("foo");

        new TypeTest().sameType(t1, t2, t3, t4)
                .differentType(other1, other2)
                .equals(t1, t2)
                .notEquals(t1, t3, t4)
                .test();
    }

    @Test
    public void testSpecialCharToken() {
        final Token t1 = new SpecialCharToken('f');
        final Token t2 = new SpecialCharToken('f', new Position(22, 1));

        final Token other1 = new EOLToken();
        final Token other2 = new StringToken("f");
        final Token other3 = new SpecialCharToken('b');

        new TypeTest().sameType(t1, t2)
                .differentType(other1, other2, other3)
                .notEquals(t1, t2)
                .test();
    }

    @Test
    public void testKeyWordsToken() {
        final Token t1 = new KeyWordToken("foo");
        final Token t2 = new KeyWordToken("foo", new Position(22, 1));

        final Token other1 = new EOLToken();
        final Token other2 = new StringToken("foo");
        final Token other3 = new KeyWordToken("bar");

        new TypeTest().sameType(t1, t2)
                .differentType(other1, other2, other3)
                .notEquals(t1, t2)
                .test();
    }

    @Test
    public void testNumberToken() {
        final Token t1 = new NumberToken(22);
        final Token t2 = new NumberToken(22.0);
        final Token t3 = new NumberToken(24);
        final Token t4 = new NumberToken(24, new Position(22, 1));

        final Token other1 = new EOLToken();
        final Token other2 = new KeyWordToken("22");

        new TypeTest().sameType(t1, t2, t3, t4)
                .differentType(other1, other2)
                .equals(t1, t2)
                .notEquals(t1, t3, t4)
                .test();
    }


    @Test
    public void testStringValue() {
        assertEquals("foo", new StringToken("foo").getStringValue());
        assertEquals("foo", new KeyWordToken("foo").getStringValue());
        assertEquals("\n", new EOLToken().getStringValue());
        assertEquals("24.11", new NumberToken(24.11).getStringValue());
        assertEquals("$", new SpecialCharToken('$').getStringValue());
    }

    @Test
    public void testStringValueFail1() {
        testUnsupportedAction(new EOFToken()::getStringValue);
    }

    @Test
    public void testCharValue() {
        assertEquals('$', new SpecialCharToken('$').getCharValue());
        assertEquals('\n', new EOLToken().getCharValue());
    }

    @Test
    public void testCharValueFail1() {
        testUnsupportedAction(
                new EOFToken()::getCharValue,
                new KeyWordToken("1")::getCharValue,
                new NumberToken(1)::getCharValue,
                new StringToken("1")::getCharValue
        );
    }

    @Test
    public void testNumberValue() {
        assertEquals(22.11, new NumberToken(22.11).getDoubleValue(), 0.0);
    }

    @Test
    public void testNumberValueFail1() {
        testUnsupportedAction(
                new EOFToken()::getDoubleValue,
                new EOLToken()::getDoubleValue,
                new KeyWordToken("1")::getDoubleValue,
                new SpecialCharToken('1')::getDoubleValue,
                new StringToken("1")::getDoubleValue
        );
    }



    private void testUnsupportedAction(Supplier<?>... methods) {
        for (Supplier<?> method : methods) {
            try {
                method.get();
                throw new AssertionError("Expected an unsupported operation");
            } catch (UnsupportedOperationException e) {
                // All fine
            }
        }
    }


    private class TypeTest {
        private Token[] sameType = {};
        private Token[] differentType = {};
        private List<Token[]> equals = new LinkedList<>();
        private List<Token[]> notEquals = new LinkedList<>();

        public TypeTest sameType(Token... tokens) {
            this.sameType = tokens;
            return this;
        }

        public TypeTest differentType(Token... tokens) {
            this.differentType = tokens;
            return this;
        }

        public TypeTest equals(Token... tokens) {
            this.equals.add(tokens);
            return this;
        }

        public TypeTest notEquals(Token... tokens) {
            this.notEquals.add(tokens);
            return this;
        }

        public void test() {
            // Test same type
            for (Token t1 : sameType) {
                for (Token t2 : sameType) {
                    assertTrue(t1.formatted() + " should be of the same type as " + t2.formatted(), t1.compareTokenType(t2));
                }
            }

            // Test equals
            for (Token[] group : equals) {
                for (int i = 0; i < group.length; i++) {
                    for (int j = i + 1; j < group.length; j++) {
                        assertEquals(group[i] + " should be equal to " + group[j], group[i], group[j]);
                    }
                }
            }

            // Test NOT equals
            for (Token[] group : notEquals) {
                for (int i = 0; i < group.length; i++) {
                    for (int j = i + 1; j < group.length; j++) {
                        assertNotEquals(group[i].formatted() + " should not be equal to " + group[j].formatted(), group[i], group[j]);
                    }
                }
            }


            // Test different types
            for (Token t1 : sameType) {
                for (Token other : differentType) {
                    assertNotEquals(t1 + " should not be equal to " + other, t1, other);
                    assertFalse(t1 + " should not be of same type as " + other, t1.compareTokenType(other));
                }
            }
        }
    }

}