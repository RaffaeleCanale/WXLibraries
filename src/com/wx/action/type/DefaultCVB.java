package com.wx.action.type;

import com.wx.action.util.PropertyContainer;
import com.wx.action.command.Command;
import com.wx.util.representables.TypeCaster;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 *
 *
 * Created on 7/11/15
 *
 * @author Raffaele Canale (raffaelecanale@gmail.com)
 * @version 0.1
 */
public class DefaultCVB<Type> implements CVB<Type> {

    final PropertyContainer info;
    final TypeCaster<String, ? extends Type> caster;

    private List<Type> values;


    public DefaultCVB(PropertyContainer info, TypeCaster<String, ? extends Type> caster) {
        this.info = info;
        this.caster = caster;
    }

//    public static CommandValueBuilder<Object> getFromType(PropertyContainer info) {
//        switch (info.getType()) {
//            case "string":
//            case "String":
//                return new CommandValueBuilder<>(info, new StringRepr());
//            case "int":
//            case "integer":
//            case "Integer":
//                return new CommandValueBuilder<>(info, new IntRepr());
//            case "bool":
//            case "boolean":
//            case "Boolean":
//                return new CommandValueBuilder<>(info, new BooleanRepr());
//            default:
//                throw new UnsupportedOperationException("No caster found for " + info.getType());
//        }
//    }

    @Override
    public Command registerFrom(Command cmd) {
        values = null;

        return info.isMarkedOption() ? registerFromOpt(cmd) : registerFromArg(cmd);
    }

    @Override
    public Type getValue() {
        if (values == null) {
            return null;
        }
        if (values.isEmpty()) {
            if (info.isMarkedOption() && info.getMax() == 0) {
                return caster.castOut("true"); // Not very pretty
            } else {
                return null;
            }
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

    private Command registerFromArg(Command cmd) {
        cmd = registerWithMinMax(cmd, 0);

        if (values == null) {  // Nothing read
            if (!trySetDefaults()) {
                checkMin(cmd.getName(), 0);
            }
        }

        return cmd;
    }

    private Command registerFromOpt(Command cmd) {
        assert info.isMarkedOption();

        int index = cmd.indexOf(info.getMarkers());

        if (index >= 0) {
            cmd = registerWithMinMax(cmd.drop(index), index);
            if (values == null) {  // Nothing read
                checkMin(cmd.getName(), 0);
                values = Collections.emptyList();
            }
        } else {
            trySetDefaults();
        }

        return cmd;
    }

    private boolean trySetDefaults() {
        if (info.hasDefaults()) {
            values = new LinkedList<>();
            for (String def : info.getDefaultValues()) {
                values.add(caster.castOut(def));
            }
            values = Collections.unmodifiableList(values);
            return true;
        }

        return false;
    }

    private void checkMin(String cmdName, int count) {
        if (count < info.getMin()) {
            throw new IllegalArgumentException("Missing argument(s) '" + info.getName() + "' for '" + cmdName + "'");
        }
    }

    /**
     * Tries to read as many arguments as possible from the given command.
     *
     * The number of read arguments is ensured to be <= max
     *
     * If the arguments read are:
     *      - None      => values are set to null
     *      - < min     => exception thrown (values will be null)
     *      - >= min    => stored in values (min can be 0)
     *
     * @param cmd
     * @param from
     * @return
     */
    private Command registerWithMinMax(Command cmd, final int from) {
        assert values == null;
        List<Type> result = new LinkedList<>();

        int count = 0;
        while ((count < info.getMax() || info.getMax() < 0)
                && cmd.length() > from
                && !cmd.param(from).startsWith("-")) {
            result.add(cmd.param(from, caster));
            cmd = cmd.drop(from);
            count++;
        }

        if (count == 0) {
            return cmd;
        }


        checkMin(cmd.getName(), count);

        values = Collections.unmodifiableList(result);

        return cmd;
    }

}
