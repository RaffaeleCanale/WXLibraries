package zcom.wx.console.command.executable.options;

import com.wx.action.command.Command;
import com.wx.util.representables.TypeCaster;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author canale
 */
abstract class AbstractArgument<Type> {

    private static final int MAX_NAMES_TO_DISPLAY = 3;
    
    private final TypeCaster<String, Type> caster;
    private final int min;
    private final int max;
    private List<Type> values;
    private final List<Type> def;

//    Argument(TypeCaster<String, Type> caster, int min, int max,
//            Type def) {
//        this(caster, min, max, def == null ? null : Arrays.asList(def));
//    }
//
//    Argument(TypeCaster<String, Type> caster, int min, int max,
//            Type[] def) {
//        this(caster, min, max, def == null ? null : Arrays.asList(def));
//    }
    AbstractArgument(TypeCaster<String, Type> caster, int min, int max,
            List<Type> def) {
        this.def = def == null ? null : new ArrayList<>(def);
        this.caster = caster;
        this.min = min < 0 ? 0 : min;
        this.max = max < 0 ? Integer.MAX_VALUE : max;

        assert !(def != null && def.size() < min);
        assert !(def != null && def.isEmpty());
    }

    public boolean isSet() {
        return values != null;
    }    

    public String getUsage(String optionName, boolean brief) {
        if (max == 0) {
            return "";
        }

        String result = "";
        if (min == 0 && max > 0) {
            result += "</";
            result += getArrayUsage(optionName, max);
            result += ">";
        } else {
            result += "<";
            result += getArrayUsage(optionName, max);
            result += ">";
//            result += "<";
//            result += getArrayUsage(optionName, min);
//            result += ">";
//            
//            if (max > min) {
//                result += " </";
//                result += getArrayUsage(optionName, max - min);
//                result += ">";
//            }

        }

        return result;
    }
    private String getArrayUsage(String name, int actualCount) {
        return getArrayUsage(name, Math.min(actualCount, MAX_NAMES_TO_DISPLAY),
                actualCount > MAX_NAMES_TO_DISPLAY);
    }
    private String getArrayUsage(String name, int count, boolean includeDots) {
        if (count == 1) {
            return name;
        }
        String result = "";
        for (int i = 0; i < count; i++) {
            result += name + i;
            if (i + 1 < count) {
                result += ", ";
            }
        }
        if (includeDots) {
            result += ", ...";
        }
        return result;
    }
    
    /**
     * Get the value associated with this argument. <br>
     * In the case of an optional argument that the user did not input, this
     * will return {@code null} (or default if set). <br><br>
     *
     * This should be used only for single element arguments (where max = 1).
     * Else,
     * use {@link #getValues() }
     *
     * @return Value associated or {@code null} if no value is set
     */
    public Type getValue() {
        assert max == 1;
        List<Type> v = getValues();
        if (v == null || v.isEmpty()) {
            return null;
        }
        return v.get(0);
    }

    /**
     * Get the list of values associated with this argument. <br>
     * If the minimum is 0 and no elements have been input by the user, this
     * will return {@code null} (or default if set). <br><br>
     *
     * @return The list of values associated with this argument
     */
    public List<Type> getValues() {
        return isSet() ? values : def;
    }

    List<Type> getDefaults() {
        return def;
    }

    void reinit() {
        values = null;
    }

    abstract Command registerFrom(Command cmd);
    
    protected Command registerWithMinMax(Command cmd, final int from) {
        List<Type> result = new LinkedList<>();

        int count = 0;
        while (count < max && cmd.length() > from
                && !cmd.param(from).startsWith("-")) {
            result.add(cmd.param(from, caster));
            cmd = cmd.drop(from);
            count++;
        }

//        if (def != null) {
//            for (int i = result.size(); i < def.size(); i++) {
//                result.add(def.get(i));
//            }
//        }
        
        if (count < min) {
            throw new IllegalArgumentException("Missing argument(s) for command '" + cmd.getName() + "'");
        }

        if (count > 0) {
//            values = result;
            setValues(result);
        }
        
        return cmd;
    }

    void setValues(List<Type> values) {        
        this.values = values;
    }

//    Type getValue() {
//        assert max == 1;
//        if (values == null || values.isEmpty()) {
//            return null;
//        } else {
//            return values.get(0);
//        }
//    }
//    
//    List<Type> getValues() {
//        assert max > 1;
//        return values;
//    }
    @Override
    public String toString() {
        if (max == 1) {
            return "VALUE(" + getValue() + ")";
        }
        return "VALUES " + getValues();
    }
}
