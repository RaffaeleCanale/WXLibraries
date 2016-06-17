package com.wx.action.command;

import com.wx.util.representables.TypeCaster;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * Created on 21/02/2015
 *
 * @author Raffaele Canale (raffaelecanale@gmail.com)
 * @version 0.1
 */
public class MutableCommand {

    private Command cmd;

    public MutableCommand(Command cmd) {
        this.cmd = cmd;
    }

    public String getName() {
        return cmd.getName();
    }

    public boolean hasNext() {
        return cmd.hasNext();
    }

    public boolean contains(String parameter) {
        return cmd.contains(parameter);
    }

    public int indexOf(String... values) {
        return cmd.indexOf(values);
    }

//    public Command splitParam(int index, String regex, int expected) {
//        return cmd.splitParam(index, regex, expected);
//    }

    public int length() {
        return cmd.length();
    }

    public <E> E tryParam(int index, TypeCaster<String, E> repr) {
        return cmd.tryParam(index, repr);
    }

    public String shift() {
        return drop(0);
    }

    public String drop(int index) {
        String value = cmd.tryParam(index);
        cmd = cmd.drop(index);

        return value;
    }

    public String param(int index) {
        return cmd.param(index);
    }

    public String getOriginalName() {
        return cmd.getOriginalName();
    }

    public void updateName(String newName) {
        this.cmd = cmd.updateName(newName);
    }

    public void next() {
        if (!hasNext()) {
            throw new IllegalStateException("No more available command");
        }
        cmd = cmd.getNext();
    }

    public List<String> drop(int from, int to) {
        final LinkedList<String> result = new LinkedList<>();
        for (int i = from; i < to; i++) {
            result.add(tryParam(i));
        }

        cmd = cmd.drop(from, to);

        return result;
    }

    public <E> E param(int index, TypeCaster<String, E> repr) {
        return cmd.param(index, repr);
    }

    public int getInt(int index) {
        return cmd.getInt(index);
    }

    public boolean isActionCommand() {
        return cmd.isActionCommand();
    }

//    public Command splitParam(int index, String regex) {
//        return cmd.splitParam(index, regex);
//    }

    public <E> E tryParam(int index, TypeCaster<String, E> repr, E def) {
        return cmd.tryParam(index, repr, def);
    }


    public List<String> takeOptions(int max, String... markers) {
        int markerIndex = indexOf(markers);
        if (markerIndex < 0) {
            return null;
        }

        List<String> result = new LinkedList<>();
        int index = markerIndex+1;
        while (index < length() && result.size() < max) {
            result.add(tryParam(index));
            index++;
        }
        cmd = cmd.drop(markerIndex, index - 1);

        return result;
    }

    @Deprecated
    public boolean isNumeric(int index) {
        return cmd.isNumeric(index);
    }

    public String stackFrom(int index) {
        return cmd.stackFrom(index);
    }

    public boolean remove(String... option) {
        int index = indexOf(option);
        if (index >= 0) {
            drop(index);
            return true;
        }

        return false;
    }

    public String tryParam(int index, String defaultValue) {
        return cmd.tryParam(index, defaultValue);
    }

    public String takeOption(String... optionId) {
        int index = indexOf(optionId);

        if (index >= 0) {
            String value = tryParam(index + 1);
            if (value == null) {
                throw new IllegalArgumentException("The option '" + Arrays.toString(optionId)
                        + "' must be followed by a parameter");
            }

            cmd = cmd.drop(index, index+1);

            return value;
        }

        return null;
    }

    public Command get() {
        return cmd;
    }

    public String tryParam(int index) {
        return cmd.tryParam(index);
    }

    @Override
    public String toString() {
        return cmd.toString();
    }
}
