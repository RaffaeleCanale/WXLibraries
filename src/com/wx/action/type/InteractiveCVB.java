package com.wx.action.type;

import com.wx.action.help.HelpPrinter;
import com.wx.action.util.PropertyContainer;
import com.wx.action.command.Command;
import com.wx.console.UserConsoleInterface;
import com.wx.util.representables.TypeCaster;

import java.util.*;

/**
 * Created on 17/03/2015
 *
 * @author Raffaele Canale (raffaelecanale@gmail.com)
 * @version 0.1
 */
public class InteractiveCVB<Type> implements CVB<Type> {

    private final PropertyContainer info;
    private final TypeCaster<String, ? extends Type> caster;
    private final UserConsoleInterface in;
    private final HelpPrinter helpPrinter;
    private List<Type> values;


    public InteractiveCVB(PropertyContainer info, TypeCaster<String, ? extends Type> caster, UserConsoleInterface in) {
        this.info = info;
        this.caster = caster;
        this.in = in;
        this.helpPrinter = new HelpPrinter(in.getConsole());
    }

    @Override
    public Command registerFrom(Command cmd) {
        assert cmd.length() == 0;
        values = null;

        if (info.isMarkedOption()) {
            registerFromOption();
        }
        else {
            registerFromArg();
        }

        return cmd;
    }

    private void registerFromOption() {
        in.getConsole().print("\n \n");
        helpPrinter.printOptionHelp(info);

        if (info.getMax() == 0) {
            in.getConsole().print("Use this option? ");
            boolean useOption = in.inputYesNo();
            setValues(String.valueOf(useOption));

        } else if (info.hasDefaults()) {
            in.getConsole().print("Input a value or leave empty to use defaults: ");
            if (!inputValues(true)) {
                setValues(info.getDefaultValues());
            }

        } else {
            in.getConsole().print("Input a value or leave empty ignore this option: ");
            inputValues(true);
        }
    }

    private void registerFromArg() {
        in.getConsole().print("\n \n");
        helpPrinter.printArgHelp(info);
        in.getConsole().println();
        assert info.getMax() > 0;

        if (info.hasDefaults()) {
            in.getConsole().print("Input a value or leave empty to use defaults: ");
            if (!inputValues(true)) {
                setValues(info.getDefaultValues());
            }
        } else if (info.getMin() == 0) {
            in.getConsole().print("Input a value or leave empty to ignore: ");
            inputValues(true);

        } else {
            in.getConsole().print("Input a value: ");
            inputValues(false);
        }
    }

    private boolean inputValues(boolean allowCancel) {
        if (isList()) {
            String[] array = in.readArray(info.getMin(), info.getMax(), allowCancel);
            if (array == null) {
                return false;
            }

            setValues(array);

        } else {
            String value = allowCancel ? in.readLine() : in.readLine(1);
            if (value == null || value.isEmpty()) {
                return false;
            }

            values = Arrays.asList(caster.castOut(value));
        }

        return true;
    }

    @Override
    public Type getValue() {
        if (values == null) {
            return null;
        }

        assert values.size() == 1 : "Size is " + values.size(); // TODO Maybe not an assert?
        return values.get(0);
    }

    @Override
    public List<Type> getValues() {
        return values;
    }

    @Override
    public boolean isList() {
        return info.getMax() > 1 || info.getMax() < 0;
    }

    private void setValues(String... stringValues) {
        List<Type> castedValues = new ArrayList<>(stringValues.length);
        for (String value : stringValues) {
            castedValues.add(caster.castOut(value));
        }

        values = Collections.unmodifiableList(castedValues);
    }
}
