/*
 * Copyright (c) 2002-2012, the original author or authors.
 *
 * This software is distributable under the BSD license. See the terms of the
 * BSD license in the documentation provided with this software.
 *
 * http://www.opensource.org/licenses/bsd-license.php
 */
package com.wx.action.cmd;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import com.wx.lexer.Lexer;
import com.wx.lexer.LexerBuilder;
import com.wx.lexer.tokens.EOFToken;
import com.wx.lexer.tokens.Token;
import com.wx.util.iterator.PeekIterator;
import com.wx.util.future.Future;
import com.wx.util.pair.Pair;
import jline.console.ConsoleReader;
import jline.console.completer.*;

public class Example {
    public static void usage() {
        System.out.println("Usage: java " + Example.class.getName()
                + " [none/simple/files/dictionary [trigger mask]]");
        System.out.println("  none - no completors");
        System.out.println("  simple - a simple completor that comples "
                + "\"foo\", \"bar\", and \"baz\"");
        System.out
                .println("  files - a completor that comples " + "file names");
        System.out.println("  classes - a completor that comples "
                + "java class names");
        System.out
                .println("  trigger - a special word which causes it to assume "
                        + "the next line is a password");
        System.out.println("  mask - is the character to print in place of "
                + "the actual password character");
        System.out.println("  color - colored prompt and feedback");
        System.out.println("\n  E.g - java Example simple su '*'\n"
                + "will use the simple compleator with 'su' triggering\n"
                + "the use of '*' as a password mask.");
    }

    public static void main(String[] args) throws IOException {
        try {
            Character mask = null;
            String trigger = null;
            boolean color = false;

            ConsoleReader reader = new ConsoleReader();

            reader.setPrompt("prompt> ");

            if ((args == null) || (args.length == 0)) {
                usage();

                return;
            }

            List<Completer> completors = new LinkedList<Completer>();


            /*
                exit -a
                constants file -u a b -f filter -i
             */


            if (args.length > 0) {
                if (args[0].equals("none")) {
                } else if (args[0].equals("files")) {
                    completors.add(new FileNameCompleter());
                } else if (args[0].equals("simple")) {
//                    completors.add(new StringsCompleter("fooz", "bar", "baz"));

                    Completer exitCompleter = new ArgumentCompleter(
                            new StringsCompleter("exit"),
                            new StringsCompleter("-a"),
                            new NullCompleter()
                    );

                    Completer filterOption = new ArgumentCompleter(
                            new StringsCompleter("-f", "--filter"),
                            new NullCompleter()
                    );

                    Completer updateOption = new ArgumentCompleter(
                            new StringsCompleter("-u", "--update"),
                            new NullCompleter(),
                            new NullCompleter()
                    );

                    Completer allOptions = new AggregateCompleter(
                            filterOption,
                            updateOption,
                            new StringsCompleter("-i")
                    );

                    Completer constantsCompleter = new ArgumentCompleter(
                            new StringsCompleter("constants"),
                            new NullCompleter(),
                            allOptions
                    );


                    TreeCompleter completer = new TreeCompleter();
                    completer.completers = Arrays.asList(
                            new SimpleCompleter(new StringsCompleter("foo", "bar"))
                            , new SimpleCompleter(new StringsCompleter("lol", "lil"))
                            );
                    completors.add(completer);

//                    completors.add(new ArgumentCompleter(
////                            new ArgumentCompleter.AbstractArgumentDelimiter() {
////                                @Override
////                                public boolean isDelimiterChar(final CharSequence buffer, final int pos) {
////                                    return buffer.charAt(pos) == '-' && pos - 1 >= 0 && buffer.charAt(pos - 1) == ' ';
////                                }
////                            },
//                            new StringsCompleter("foo", "bar", "baz"),
//                            new StringsCompleter("-lol", "-lil", "-lal", "arg"),
//                            new StringsCompleter("hello", "world")
//                    ));
//                    completors.add(new AggregateCompleter(
//                            new StringsCompleter("foo", "bar", "baz"),
//                            new StringsCompleter("lol", "lil", "lal")
//                    ));
                } else if (args[0].equals("color")) {
                    color = true;
                    reader.setPrompt("\u001B[1mfoo\u001B[0m@bar\u001B[32m@baz\u001B[0m> ");
                } else {
                    usage();

                    return;
                }
            }

            if (args.length == 3) {
                mask = args[2].charAt(0);
                trigger = args[1];
            }

            for (Completer c : completors) {
                reader.addCompleter(c);
            }

            String line;
            PrintWriter out = new PrintWriter(reader.getOutput());

            while ((line = reader.readLine()) != null) {
                if (color) {
                    out.println("\u001B[33m======>\u001B[0m\"" + line + "\"");

                } else {
                    out.println("======>\"" + line + "\"");
                }
                out.flush();

                // If we input the special word then we will mask
                // the next line.
                if ((trigger != null) && (line.compareTo(trigger) == 0)) {
                    line = reader.readLine("password> ", mask);
                }
                if (line.equalsIgnoreCase("quit") || line.equalsIgnoreCase("exit")) {
                    break;
                }
                if (line.equalsIgnoreCase("cls")) {
                    reader.clearScreen();
                }
            }
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

    private static class SimpleCompleter implements PropertyCompleter {
        private final Completer completer;

        public SimpleCompleter(Completer completer) {
            this.completer = completer;
        }

        @Override
        public int completeEmpty(List<CharSequence> candidates) {
            return completer.complete("", 0, candidates);
        }

        @Override
        public int complete(PeekIterator<Future<Token>> lexer, int cursor, List<CharSequence> candidates) throws IOException {
            String stringValue = lexer.peek().get().getStringValue();

            int result = completer.complete(stringValue, cursor, candidates);

            if (result >= 0) {
                lexer.next();
            }

//            System.out.println("SC: " + result + " <= " + stringValue);

            return result;
        }
    }

    private static interface PropertyCompleter {

        public int completeEmpty(List<CharSequence> candidates);

        public int complete(PeekIterator<Future<Token>> lexer, int cursor, List<CharSequence> candidates) throws IOException;
    }

    private static class TreeCompleter implements Completer {

        private List<PropertyCompleter> completers;

        private LexerBuilder lexerBuilder = new LexerBuilder().ignoreNumericLitterals().separators(' ').specials('"', '\'');

        public int complete(final String buffer, final int cursor, final List<CharSequence> candidates) {

            if (buffer == null || buffer.isEmpty()) {
                return completers.get(0).completeEmpty(candidates);
            }

            Lexer tmp = lexerBuilder.build(new ByteArrayInputStream(buffer.getBytes()));
            PeekIterator<Future<Token>> lexer = new PeekIterator<>(tmp.iterator());

            List<PropertyCompleter> completers = new LinkedList<>(this.completers);

            String previousMatcherPrefix = "";
            int wordStart = 0;

//            System.out.println("\n\nINPUT = " + buffer);
            try {
                while (hasNext(lexer)) {
//                    System.out.println("WORD = " + lexer.peek().get() + " AT " + wordStart);

                    Pair<Integer, List<CharSequence>> matchingCompleter = findMatchingCompleter(lexer, completers, cursor - wordStart);
                    if (matchingCompleter == null) {
                        return -1;
                    }

                    if (!hasNext(lexer)) {
//                        String prefix = buffer.substring(0, matchingCompleter.get1());

                        Integer tmpCursor = matchingCompleter.get1() + wordStart;

                        candidates.addAll(matchingCompleter.get2());
                        System.out.println("END FOUND = " + tmpCursor + " / " + candidates);
                        return tmpCursor;
                    }

//                    System.out.println("TMP FOUND = " + matchingCompleter);
                    wordStart = lexer.peek().get().getPosition().getCharNo() - 1;
                }


            } catch (IOException e) {
                e.printStackTrace();
            }


//            while (lexer.hasNext()) {
//                try {
//                    final Token next = lexer.next();
//                    if (next.compareTokenType(new EOFToken())) {
//                        return -1;
//                    }
//
//                    List<CharSequence> currentCandidates = new LinkedList<>();
//                    int pos = currentPosition.getElement().complete(next.getStringValue(), 0, currentCandidates);
//
//                    if (isExactMatch(next, currentCandidates)) {
//
//                    }
//
////
////                    Completer aggregate = getAggregateCompleter(currentPosition);
////
////                    List<CharSequence> currentCandidates = new LinkedList<>();
////                    int c = aggregate.complete(next.getStringValue(), 0, currentCandidates);
////
////                    if (isExactMatch(next, currentCandidates)) {
////
////                    }
//
//
//                } catch (IOException e) {
//                    throw new AssertionError(e); // Should never happen
//                }
//            }

            return -1;
        }

        private Pair<Integer, List<CharSequence>> findMatchingCompleter(PeekIterator<Future<Token>> lexer, List<PropertyCompleter> completers, int cursor) throws IOException {
            for (PropertyCompleter completer : completers) {
                List<CharSequence> candidates = new LinkedList<>();
                int result = completer.complete(lexer, cursor, candidates);

                if (result >= 0) { //Match
                    completers.remove(completer);
                    return new Pair<>(result, candidates);
                }
            }

            return null;
        }

        private boolean isExactMatch(Token token, List<CharSequence> currentCandidates) {
            return currentCandidates.size() == 1 && currentCandidates.get(0).toString().contains(token.getStringValue());
        }

//        private Completer getAggregateCompleter(STTree<Completer> currentPosition) {
//            List<STTree<Completer>> completers = currentPosition.getNodes();
//
//            return null;
//        }

        private boolean hasNext(PeekIterator<Future<Token>> lexer) throws IOException {
            return lexer.hasNext() && !lexer.peek().get().compareTokenType(new EOFToken());
        }

        private boolean parseUntil(Lexer lexer, Token token) throws IOException {
            while (lexer.hasNext()) {
                if (lexer.next().compareTokenType(token)) {
                    return true;
                }
            }
            return false;
        }
    }
}