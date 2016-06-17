package com.wx.action.command;

import com.wx.util.representables.TypeCaster;
import com.wx.util.representables.string.IntRepr;
import com.wx.util.representables.string.StringRepr;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * This class is a representation of a command. A typical line command is built
 * in two parts, first the Command name and an undefined number of parameters
 * (all separated by spaces). If the input contains no command name, it will be
 * interpreted as an action command that can be identified by using {@link #isActionCommand()
 * } or by comparing
 * {@link #getName()}{@code .equals(}{@link #ACTION_CMD}{@code )}<br><br>
 *
 * A command can be chained with others.
 *
 * @see CommandBuilder
 *
 * @author Raffaele Canale
 * @version 1.1
 */
public class Command {

    public final static String ACTION_CMD = "&action";

    private final String name;
    private final List<String> parameters;
    private final Command next;

   
    Command(String name, List<String> params, Command next) {
        this.name = name;
        this.parameters = Collections.unmodifiableList(new LinkedList<>(params));
        this.next = next;
    }

    public Command moveNameToFirstArg() {
        List<String> newParams = new LinkedList<>(parameters);
        newParams.add(0, name);

        return new Command("", newParams, next);
    }

//    /**
//     * Seeks and returns the index of the given parameter or -1 if it does not
//     * exist.
//     *
//     * @param expected The parameter to search
//     *
//     * @return The index of the given parameter or -1 if it does not contain it
//     */
//    public int indexOf(String expected) {
//        return parameters.indexOf(expected);
//    }

    /**
     * Get this command's name in lower case.
     *
     * @return Command's name in lower case
     *
     * @see #getOriginalName()
     */
    public String getName() {
        return name.toLowerCase();
    }

    public List<String> getParameters() {
        return parameters;
    }

    /**
     * Determine if this is an action command (ie. if this command has no name
     * nor parameters, the user's input is just a blank line).
     *
     * @return {@code true} if this is an action command
     *
     */
    public boolean isActionCommand() {
        return ACTION_CMD.equals(name);
    }

    /**
     * Get this command's name unformatted.
     *
     * @return Command's name unformatted
     */
    public String getOriginalName() {
        return name;
    }

    /**
     * Number of parameters for this command.
     *
     * @return Number of parameters
     */
    public int length() {
        return parameters.size();
    }

    /**
     * Split a parameter into a sublist of parameters according to the given
     * regular expression. All the new parameters will be inserted from the same
     * position (shifting all the next parameters further).
     *
     * @param index Index of the parameter to split
     * @param regex Regular expression to use (see {@link String#split(String)})
     *
     * @return A new Command with the parameters split
     *
     * @throws java.lang.IllegalArgumentException if the index is out of bounds
     */
    public Command splitParam(int index, String regex) {
        String[] splitParam = param(index).split(regex);
        List<String> newParams = new LinkedList<>(parameters);

        newParams.set(index, splitParam[0]);
        for (int i = 1; i < splitParam.length; i++) {
            newParams.add(index + i, splitParam[i]);
        }

        return new Command(name, newParams, next);
    }

    /**
     * Split a parameter into a sublist of parameters according to the given
     * regular expression. All the new parameters will be inserted from the same
     * position (shifting all the next parameters further).<br><br>
     *
     * If the number of newly created parameters does not match to the expected
     * number, then an input exception is thrown.<br><br>
     *
     *
     * @param index    Index of the parameter to split
     * @param regex    Regular expression to use (see
     *                 {@link String#split(String)})
     * @param expected Expected number of new parameters
     *
     * @return A new Command with the parameters split as expected
     *
     * @throws IllegalArgumentException if the index is out of bounds or if the number of
     *                        newly created parameters does not match the
     *                        expected number.
     */
    public Command splitParam(int index, String regex, int expected) {
        Command newCmd = splitParam(index, regex);
        int newParamsCount = newCmd.parameters.size() - parameters.size() + 1;
        if (newParamsCount != expected) {
            throw new IllegalArgumentException("The parameter " + (index + 1) + " is "
                    + "supposed to contain " + expected + " sub-parameters "
                    + "separated by '" + regex + "' but " + newParamsCount + " where "
                    + "found");
        }

        return newCmd;
    }

    /**
     * Get the parameter at the given index.
     *
     * @param index Parameter's index
     *
     * @return The parameter at that index
     *
     * @throws IllegalArgumentException if the index is out of bounds
     */
    public String param(int index) {
        return param(index, new StringRepr());
    }

    /**
     * Get the parameter at the given index using a custom representable.
     *
     * @param <E>   Type of the parameter to retrieve
     * @param index Parameter's index
     * @param repr  Representable to cast the parameter
     *
     * @return The parameter at that index casted to type {@code E}
     *
     * @throws IllegalArgumentException if the index is out of bounds or if the found
     *                        parameter could not be casted
     *
     */
    public <E> E param(int index, TypeCaster<String, E> repr) {
        if (index < 0 || index >= parameters.size()) {
            throw new IllegalArgumentException("Missing parameter n°" + index + " for the"
                    + " command " + name + "\nTry 'help " + name + "'");
        } else {
            try {
                return repr.castOut(parameters.get(index));
            } catch (ClassCastException ex) {
                throw new IllegalArgumentException("Parameter '" + parameters.get(index) + "'"
                        + " is not of the expected type: " + ex.getMessage());
            }
        }
    }

    /**
     * Get the parameter at the given index or {@code null} if the index is out
     * of bounds.
     *
     * @param index Parameter's index
     *
     * @return The parameter at that index or {@code null} if it does not exist
     */
    public String tryParam(int index) {
        return tryParam(index, new StringRepr(), null);
    }

    /**
     * Get the parameter at the given index or return the default value if the
     * index is out of bounds.
     *
     * @param index        Parameter's index
     * @param defaultValue The default value to return if the parameter does not
     *                     exist
     *
     * @return The parameter at that index or {@code defaultValue} if it does
     *         not exist
     */
    public String tryParam(int index, String defaultValue) {
        return tryParam(index, new StringRepr(), defaultValue);
    }

    /**
     * Get the parameter at the given index and cast it or {@code null} if the
     * index is out of bounds.
     *
     * @param <E>   Parameter's type
     * @param index Parameter's index
     * @param repr  Representable to cast the parameter
     *
     * @return The parameter at that index or {@code null} if it does not exist
     */
    public <E> E tryParam(int index, TypeCaster<String, E> repr) {
        return tryParam(index, repr, null);
    }

    /**
     * Get the parameter at the given index or return the default value if the
     * index is out of bounds.
     *
     * @param <E>   Parameter's type
     * @param index Parameter's index
     * @param repr  Representable to cast the parameter
     * @param def   The default value to return if the parameter does not exist
     *
     * @return The parameter at that index or {@code def} if it does not exist
     */
    public <E> E tryParam(int index, TypeCaster<String, E> repr, E def) {
        try {
            return param(index, repr);
        } catch (IllegalArgumentException ex) {
            return def;
        }
    }

    /**
     * Stacks all the parameters starting from the given index into one string
     * separated by spaces.
     *
     * @param index Index to start from
     *
     * @return All the parameters concatenated
     *
     * @throws IllegalArgumentException if the the index is out of bounds
     */
    public String stackFrom(int index) {
        String result = param(index);
        if (result == null) {
            return null;
        }

        for (int i = index + 1; i < parameters.size(); i++) {
            result += " " + parameters.get(i);
        }

        return result;
    }

    @Deprecated
    public boolean isNumeric(int index) {
        if (index >= parameters.size()) {
            return false;
        }

        try {
            Integer.parseInt(parameters.get(index));
            return true;
        } catch (NumberFormatException ex) {
            return false;
        }
    }

    /**
     * Get the parameter at the given index as an {@code int}
     *
     * @param index Parameter's index
     *
     * @return The parameter at that index casted to {@code int}
     *
     * @throws IllegalArgumentException if the index is out of bounds or if the found
     *                        parameter could not be casted
     */
    public int getInt(int index) {
        return param(index, new IntRepr());
    }

    /**
     * Get the parameter at the given index as an {@code int} and throws an
     * exception if it is bigger than the given upper bound.<br><br>
     *
     *
     * @param index      Parameter's index
     * @param upperBound Upper bound
     *
     * @return The parameter at that index casted to {@code int}
     *
     * @deprecated
     * @throws IllegalArgumentException if the index is out of bounds, if the found
     *                        parameter could not be casted or if the upper
     *                        bound is exceeded
     */
    public int getInt(int index, int upperBound) {
        int n = getInt(index);
        if (n <= upperBound) {
            return n;
        }

        throw new IllegalArgumentException("Wrong input: The parameter n°" + (index + 1) + " must be "
                + "smaller or equal than " + upperBound);
    }

    /**
     *
     * @param index
     * @param lowerBound
     * @param upperBound
     *
     * @return
     *
     * @throws IllegalArgumentException
     *
     * @deprecated
     */
    public int getInt(int index, int lowerBound, int upperBound) {
        int n = getInt(index);
        if (n >= lowerBound && (n <= upperBound || upperBound <= lowerBound)) {
            return n;
        }
        throw new IllegalArgumentException("Wrong input: The parameter n°" + (index + 1) + " must be "
                + (upperBound <= lowerBound ? "bigger or equal than " + lowerBound
                : "in [" + lowerBound + ", " + upperBound + "]"));
    }

    /**
     * Drop a parameter.
     *
     * @param index Index of the parameter to remove
     *
     * @return The Command with a dropped parameters
     */
    public Command drop(int index) {
        testBounds(index);

        LinkedList<String> newParams = new LinkedList<>(parameters);
        newParams.remove(index);
        return new Command(name, newParams, next);
    }

    public Command drop(int from, int to) {
        testBounds(from);
        testBounds(to);

        LinkedList<String> newParams = new LinkedList<>();
        if (from > 0) {
            newParams.addAll(parameters.subList(0, from));
        }
        if (to < parameters.size() - 1) {
            newParams.addAll(parameters.subList(to + 1, parameters.size()));
        }

        return new Command(name, newParams, next);
    }

    /**
     * Similar to drop(0)
     *
     * @return The shifted command
     */
    public Command shift() {
        return drop(0);
    }

    public Command updateName(String newName) {
        return new Command(newName, parameters, next);
    }

    /**
     * Test if a parameter exists or not.
     *
     * @param parameter Parameter to test
     *
     * @return {@code true} if {@code parameter} is contained in the parameters
     *         list
     */
    public boolean contains(String parameter) {
        return parameters.contains(parameter);
    }

    /**
     * Get the index of the first encountered value or -1 if none was found.
     *
     * @param values Values to search
     *
     * @return The first index of the first found value or -1 if none found
     */
    public int indexOf(String... values) {
        for (String v : values) {
            int index = parameters.indexOf(v);
            if (index >= 0) {
                return index;
            }
        }

        return -1;
    }

//    /**
//     * Removes any of the given parameters or {@code this} if none was found.
//     * <br><br>
//     * Note that this removes only 1 parameter (ie. the first of the given
//     * parameters that is found).
//     * 
//     * @param parameter Any of the parameters to remove
//     *
//     * @return New command with the parameter removed or {@code this}
//     */
//    public Command take(String... parameter) {
//        LinkedList<String> newParams = new LinkedList<>(parameters);
//        
//        for (String param : parameter) {
//            if (newParams.remove(param)) {
//                return new Command(name, newParams, next);
//            }
//        }
//        
//        return this;
//    }
//    /**
//     * If at leat one of the given parameters is contained in the list, this
//     * method removes it and returns its index, else it returns -1 and does
//     * nothing.
//     *
//     * @param parameter Parameter to remove
//     *
//     * @return The index of the parameter or -1 if it was not found
//     */
//    public int takeParam(String... parameter) {
//        int paramIndex = 0;
//        for (String param : parameter) {
//            int index = parameters.indexOf(param);
//            if (index >= 0) {
//                drop(index);
//                return paramIndex;
//            }
//            paramIndex++;
//        }
//        return -1;
//    }
//
//    public int takeIndex(String... parameters) {
//        for (String param : parameters) {
//            int index = parameters.indexOf(param);
//            if (index >= 0) {
//                drop(index);
//                return index;
//            }
//        }
//        return -1;
//    }
    /**
     * Search an option (any of the given) an return its parameter. If the
     * option does not exist, this method returns {@code null}. If the option
     * does exist but is not followed by any parameter, an exception is
     * thrown.<br><br>
     *
     * Here is an example of use:
     * <pre> {@code
     * cmd -p param
     * } </pre>
     *
     * Where the option is {@code -p} and the returned parameter is
     * {@code param}.
     *
     * @param optionId Any of the identifiers of the option to find
     *
     * @return The option's parameter or {@code null} if the option does not
     *         exist
     *
     * @throws IllegalArgumentException if the option's parameter is missing
     */
    public String getOption(String... optionId) {
        for (String option : optionId) {
            int index = parameters.indexOf(option);
            if (index >= 0) {
                String value = tryParam(index + 1);
                if (value == null) {
                    throw new IllegalArgumentException("The option '" + option
                            + "' must be followed by a parameter");
                }
//                drop(index + 1);
//                drop(index);
                return value;
            }
        }

        return null;
    }
    
    public List<String> getOptions(int max, String... markers) {
        int markerIndex = indexOf(markers);
        if (markerIndex < 0) {
            return null;
        }
        
        List<String> result = new LinkedList<>();
        int index = markerIndex+1;
        while (index < parameters.size() && result.size() < max) {
            result.add(parameters.get(index));
            index++;
        }
        
        return result;
    }

    /**
     * Tells if another command has been buffered within this one. This can
     * happen if the user inputs multiple commands at the same time using the
     * command separator.
     *
     * @return {@code true} if it has a next command
     */
    public boolean hasNext() {
        return next != null;
    }

    /**
     * Returns the next command buffered within this one or {@code null} if it
     * has not such command. A next command can exist if the user inputs
     * multiple commands at the same time using the command separator.
     *
     * @return The next command or {@code null} if it hasn't one
     */
    public Command getNext() {
        return next;
    }

//    /**
//     * Override this command with the next one. For more informations about the
//     * next command, see {@link #getNext()}
//     *
//     * @throws IllegalStateException if it has no next command
//     */
//    public void next() {
//        if (next == null) {
//            throw new IllegalStateException();
//        }
//        this.name = next.name;
//        this.mParams = next.mParams;
//        this.next = next.next;
//    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Command command = (Command) o;

        if (!name.equals(command.name)) return false;
        if (next != null ? !next.equals(command.next) : command.next != null) return false;
        return parameters.equals(command.parameters);
    }

    @Override
    public int hashCode() {
        int result = name.hashCode();
        result = 31 * result + parameters.hashCode();
        result = 31 * result + (next != null ? next.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        if (length() > 0) {
            try {
                return name + " " + stackFrom(0);
            } catch (IllegalArgumentException ex) {
                return null; //Can't happen
            }
        } else {
            return name;
        }
    }

    public MutableCommand mutable() {
        return new MutableCommand(this);
    }

    private void testBounds(int value) {
        if (value < 0 || value >= parameters.size()) {
            throw new IllegalArgumentException("Invalid index");
        }
    }
}
