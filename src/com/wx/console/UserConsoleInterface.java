package com.wx.console;

import com.wx.console.color.Color;
import com.wx.action.command.Command;
import com.wx.action.command.CommandBuilder;
import com.wx.action.command.DefaultCommandBuilder;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * This class implements an interactive console interface with the user and allows to input different type of data from
 * the user.
 *
 * @author Canale
 */
public class UserConsoleInterface {

    private final Console console;
    private String prefix;
    private final String separator;
    private final CommandBuilder builder;

    private final List<Command> toExecute;

    public UserConsoleInterface(Console console,
                                String mPrefix, String mSep) {
        this(console, mPrefix, mSep, new DefaultCommandBuilder());
    }

    public UserConsoleInterface(Console console,
                                String mPrefix, String mSep, CommandBuilder mBuilder) {
        this.console = console;
        this.prefix = mPrefix;
        this.separator = mSep;
        this.builder = mBuilder;
        this.toExecute = new LinkedList<>();
    }


    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    //<editor-fold defaultstate="collapsed" desc="util methods">

    /**
     * Allows the user to input several lines. This method will prompt the user for input until an empty line is
     * entered.
     *
     * @return List of string input by the user
     */
    public List<String> readCollection() {
        return readCollection(prefix("collection"));
    }

    /**
     * Allows the user to input several lines. This method will prompt the user for input until an empty line is
     * entered.
     *
     * @param prefix Prefix to display at each new line
     *
     * @return List of string input by the user
     */
    public List<String> readCollection(String prefix) {
        List<String> collection = new LinkedList<>();

        String line;
        int count = 0;
        boolean stopInput = false;
        do {
            line = readLine(prefix + " [" + count + "]");
            if (line.isEmpty()) {
                stopInput = true;
            } else {
                collection.add(line);
                count++;
            }
        } while (!stopInput);

        return collection;
    }

    /**
     * @return Line input by the user
     */
    public String readLine() {
        return readLine(prefix("string"));
    }

    public String readLine(int minLength) {
        return readLine(prefix("string"), minLength);
    }

    /**
     * @param prefix Prefix to display
     *
     * @return Line input by the user
     */
    public String readLine(String prefix) {
        printPrefix(prefix);
        console.flushOut();
        return console.nextLine();
    }

    public char[] readPassword(String prefix) {
        printPrefix(prefix);
        console.flushOut();
        return console.readPassword();
    }

    /**
     * Input the user for a line with a minimum length. An error will be prompt and the user will be prompt as long as
     * the condition is not met.
     *
     * @param prefix    Prefix to display
     * @param minLength Minimum accepted length
     *
     * @return A line input by the user
     */
    public String readLine(String prefix, int minLength) {
        String result;
        boolean valid = false;
        do {
            result = readLine(prefix);
            if (result == null || result.length() < minLength) {
                console.errln("Must have at least " + minLength + " characters");
            } else {
                valid = true;
            }
        } while (!valid);

        return result;
    }

    public int readInt() {
        return readInt(prefix("integer"));
    }

    public int readInt(String prefix) {
        int result = 0;
        boolean valid;
        do {
            try {
                printPrefix(prefix);
                console.flushOut();

                result = console.nextInt();
                valid = true;
            } catch (NumberFormatException ex) {
                console.errln("Wrong input type, expected type 'int'");
                console.flushErr();
                valid = false;
            }
        } while (!valid);

        return result;
    }

    public int readIntSmallerThan(int max) {
        return readIntIn(Integer.MIN_VALUE, max);
    }

    public int readIntBiggerThan(int min) {
        return readIntIn(min, Integer.MAX_VALUE);
    }

    public int readIntIn(int min, int max) {
        final String bound;
        if (min == Integer.MIN_VALUE) {
            bound = "<= " + max;
        } else if (max == Integer.MAX_VALUE) {
            bound = ">= " + min;
        } else {
            bound = "[" + min + ", " + max + "]";
        }

        int result = readInt(prefix(bound));
        while (result < min || result > max) {
            console.errln("Wrong range, " + result + " not in " + bound);
            console.flushErr();

            result = readInt(prefix(bound));
        }

        return result;
    }

    public double readDouble() {
        return readDouble(prefix("double"));
    }

    public double readDouble(String prefix) {
        double result = 0;
        boolean valid;
        do {
            try {
                printPrefix(prefix);
                console.flushOut();

                result = console.nextDouble();
                valid = true;
            } catch (NumberFormatException ex) {
                console.errln("Wrong input type, expected type 'double'");
                console.flushErr();
                valid = false;
            }
        } while (!valid);

        return result;
    }

    public double readDoubleSmallerThan(double max) {
        return readDoubleIn(Double.MIN_VALUE, max);
    }

    public double readDoubleBiggerThan(double min) {
        return readDoubleIn(min, Double.MAX_VALUE);
    }

    public double readDoubleIn(double min, double max) {
        final String bound;
        if (min == Double.MIN_VALUE) {
            bound = "< " + max;
        } else if (max == Double.MAX_VALUE) {
            bound = "> " + min;
        } else {
            bound = "[" + min + ", " + max + "]";
        }

        double result = readDouble(bound);
        while (result < min || result > max) {
            console.errln("Wrong range, " + result + " not in " + bound);
            console.flushErr();

            result = readDouble(bound);
        }

        return result;
    }

    public String[] readArray(int min, int max, boolean allowCancel) {
        final String bound;
        if (min <= 0) {
            bound = "<= " + max;
            min = 0;
        } else if (max == Integer.MAX_VALUE || max < 0) {
            bound = ">= " + min;
            max = Integer.MAX_VALUE;
        } else {
            bound = "[" + min + ", " + max + "]";
        }


        String[] result = readSplit(prefix(bound));
        while (result.length < min || result.length > max) {
            if (result.length == 0 && allowCancel) {
                return null;
            }
            console.errln("Wrong size, must have " + bound + " elements");
            console.flushErr();

            result = readSplit(prefix(bound));
        }

        return result;
    }

    private String[] readSplit(String prefix) {
        String line = readLine(prefix);
        if (line.isEmpty()) {
            return new String[0];
        } else {
            return line.split(" ");
        }
    }

    public Command inputCommand() {
        if (toExecute.isEmpty()) {
            printPrefix(prefix("cmd"));
            console.flush();
            return builder.build(console.nextLine());
        } else {
            Command cmd = toExecute.remove(0);
            printPrefix(prefix("cmd"));
            console.setColor(Color.Magenta);
            console.print("Executing ");
            console.resetStyle();
            console.println(cmd);

            return cmd;
        }
    }

    public boolean inputYesNo() {
        return inputMultipleChar(true, 'y', 'n') == 0;
    }

    public boolean inputChar(char match) {
        return inputMultipleChar(false, match) == 0;
    }

    public int inputMultipleChar(boolean closedChoice, char... matches) {
        assert matches.length > 0;

        String desc = "" + matches[0];
        for (int i = 1; i < matches.length; i++) {
            desc += "/" + matches[i];
        }
        if (!closedChoice) {
            desc += "/-";
        }
        do {
            printPrefix(prefix(desc));
            console.flushOut();

            char input = Character.toLowerCase(console.nextChar());
            for (int i = 0; i < matches.length; i++) {
                if (input == matches[i]) {
                    return i;
                }
            }
        } while (closedChoice);
        return -1;
    }

    protected String prefix(String type) {
        return prefix + "(" + type + ")" + separator + " ";
    }

//</editor-fold>


    protected void printPrefix(String prefix) {
        console.print(prefix);
    }

    public Console getConsole() {
        return console;
    }

    public CommandBuilder getBuilder() {
        return builder;
    }

    public String getPrefix() {
        return prefix;
    }

    public String getSeparator() {
        return separator;
    }

    public void setNextCommands(Command... cmds) {
        toExecute.addAll(Arrays.asList(cmds));
    }

    public void setNextCommand(String[] args) {
        toExecute.add(builder.build(args));
    }
}
