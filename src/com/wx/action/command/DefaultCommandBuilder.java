package com.wx.action.command;

import com.wx.console.Console;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;


/**
 * 
 * @author Raffaele Canale
 */
public class DefaultCommandBuilder implements CommandBuilder {
    
    private static final char DEFAULT_COMMAND_SEPARATOR = ';';
    private static final char DEFAULT_PARAM_SEPARATOR = ',';
    private static final char DEFAULT_WORD_SEPARATOR = ' ';
    private static final char DEFAULT_TEXT_PROTECT_1 = '"';
    private static final char DEFAULT_TEXT_PROTECT_2 = '\'';
    
    
    public static void print(Command cmd, Console console, String prefix) {
        if (cmd == null) {
            console.println("None");
            return;
        }
        console.println("Name : " + cmd.getName());
        console.print(prefix + "Args: ");
        for (int i = 0; i < cmd.length(); i++) {
            console.print(cmd.param(i) + ", ");
        }
        console.println();
        console.print(prefix + "Next : ");
        print(cmd.getNext(), console, prefix + "       ");
    }
    
    private final char commandSeparator;
    private final char paramSeparator;
    private final char wordSeparator; 
    private final char textProtect1; 
    private final char textProtect2;

    public DefaultCommandBuilder(char commandSeparator, char paramSeparator,
            char wordSeparator, char textProtect1, char textProtect2) {
        this.commandSeparator = commandSeparator;
        this.paramSeparator = paramSeparator;
        this.wordSeparator = wordSeparator;
        this.textProtect1 = textProtect1;
        this.textProtect2 = textProtect2;
    }

    public DefaultCommandBuilder() {
        this(DEFAULT_COMMAND_SEPARATOR, DEFAULT_PARAM_SEPARATOR, 
                DEFAULT_WORD_SEPARATOR, DEFAULT_TEXT_PROTECT_1, 
                DEFAULT_TEXT_PROTECT_2);
    }

    @Override
    public Command build(String cmdName, String[] args) {
        assert cmdName != null;
        
        String[] cmd;
        if (args == null) {
            cmd = new String[]{cmdName};
        } else {
            cmd = new String[args.length + 1];
            cmd[0] = cmdName;
            System.arraycopy(args, 0, cmd, 1, args.length);
        }
        
        return build(cmd);
    }
    
    @Override
    public Command build(String... input) {
        String name;
        List<String> params;
        Command next;

        if (input == null || input.length == 0) {
            name = Command.ACTION_CMD;
            params = new LinkedList<>();
            next = null;

        } else {
            boolean stop = false;
            name = input[0];
            params = new ArrayList<>();
            next = null;

            for (int i = 1; i < input.length && !stop; i++) {
                if (input[i].equals("" + paramSeparator)) {
                    String[] nextCommandInput = new String[input.length - i];
                    System.arraycopy(input, i + 1, nextCommandInput, 1, nextCommandInput.length - 1);

                    nextCommandInput[0] = name;
                    next = build(nextCommandInput);
                    //next = new Command(nextCommandInput);
                    stop = true;

                } else if (input[i].equals("" + commandSeparator)) {
                    String[] nextInput = new String[input.length - i - 1];
                    System.arraycopy(input, i + 1, nextInput, 0, nextInput.length);

                    next = build(nextInput);
                    //next = new Command(nextInput);
                    stop = true;
                } else {
                    params.add(input[i]);
                }
            }
        }

        return build(name, params, next);
    }

    @Override
    public Command build(final String input) {
        if (input != null) {
            String trimmedInput = input.trim();
            if (!trimmedInput.isEmpty()) {
                return build0(trimmedInput);
            }
        }
        return build(Command.ACTION_CMD, new LinkedList<String>(), null);
    }

    private Command build(String name, List<String> params, Command next) {
        return new Command(name, params, next);
    }

    private Command build0(String input) {
        TokenIterator it = new TokenIterator(input, commandSeparator, 
                paramSeparator, textProtect1, textProtect2, wordSeparator);

        String name;
        List<String> params = new LinkedList<>();
        Command next = null;


        boolean hasMore = it.stepNext();
        name = it.getWord();

        if (name.isEmpty()) {
            // IT IS AN ACTION COMMAND 
            name = Command.ACTION_CMD;
            if (hasMore) {
                // IT HAS MORE CHARACTERS
                if (it.getHop() == commandSeparator) {
                    // WE HAVE A SEPARATOR
                    next = build(it.rest());
                } else {
                    // IT IS AN ACTION CMD BUT THEN WE HAVE SPECIAL CHAR
                    throw new IllegalArgumentException("Invalid command construction");
                }
            }
        } else {
            // NAME IS NOT EMPTY   
            do {
                char hop = it.getHop();

                addParam(it, params);

                if (hop == commandSeparator) {
                    next = build(it.rest());
                } else if (hop == paramSeparator) {
                    next = build(name + " " + it.rest());
                } else if (hop == textProtect1 || hop == textProtect2) {
                    if (it.stepNext(hop)) {
                        params.add(it.getWord());
                    } else {                      
                        throw new IllegalArgumentException("Missing closing " + hop);
                    }
                }
            } while (it.stepNext());
            addParam(it, params);
        }
        return build(name, params, next);
    }

    private void addParam(TokenIterator it, List<String> params) {
        String word = it.getWord();

        if (!word.isEmpty()) {
            params.add(word);
        }
    }
    
    private class TokenIterator {

        private final String value;
        private char nextHop;
        private int cursor;
        private String word;
        private final Set<Character> mHops;

        /**
         * Build a new token iterator that will iterate through the given value.
         *
         * @param value Value to iterate
         */
        private TokenIterator(String value, char... hops) {
            this.value = value;
            this.cursor = 0;
            this.mHops = new HashSet<>();
            for (char hop : hops) {
                mHops.add(hop);
            }
        }

        /**
         * Iterates until the next hop (ie. a space, separator, etc...).<br> The
         * iterated elements are buffered and can be accessed with
         *  {@link #getWord() }
         *
         * @return {@code true} if a hop has been found or {@code false} if the
         *         iterator reached the end of the string with no result
         */
        public boolean stepNext() {
            word = "";
            while (cursor < value.length()) {
                char c = value.charAt(cursor);
                cursor++;

                if (isHop(c)) {
                    nextHop = c;
                    return true;
                } else {
                    word += c;
                }
            }
            return false;
        }

        /**
         * Steps until the given hop. If the hop is not found, no iteration is
         * performed and this method returns {@code false}. Else, the iterated
         * elements are stored into a buffer and can be accessed with
         *  {@link #getWord() }.
         *
         * @param hop Hop to step to
         *
         * @return {@code true} if the hop has been found
         */
        public boolean stepNext(char hop) {
            int n = value.indexOf("" + hop, cursor);
            if (n < 0) {
                return false;
            }

            nextHop = hop;
            word = value.substring(cursor, n);
            cursor = n+1;
            return true;
        }

        /**
         * Get the substring starting from cursor to the end.
         *
         * @return the rest of the elements that have not been iterated
         */
        public String rest() {
            if (cursor < value.length()) {
                String rest = value.substring(cursor);
                cursor = value.length();
                return rest;
            } else {
                return "";
            }
        }

        /**
         * Get the last hop found
         *
         * @return Last hop found
         */
        public char getHop() {
            return nextHop;
        }

        /**
         * Get the buffered word and empty it
         *
         * @return Buffered word
         */
        public String getWord() {
            String tmp = word;
            word = "";
            return tmp;
        }

        /**
         * Tell if the given char is a hop or not
         *
         * @param c
         *
         * @return
         */
        private boolean isHop(char c) {
            return mHops.contains(c);/*
             return c == SEPARATOR || c == TEXT_MARKER_1
             || c == TEXT_MARKER_2 || c == CMD_SEPARATOR
             || c == PARAM_SEPARATOR;//*/
        }
    }
}
