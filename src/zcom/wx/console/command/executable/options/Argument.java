package zcom.wx.console.command.executable.options;

import com.wx.action.command.Command;
import com.wx.util.representables.TypeCaster;
import com.wx.util.representables.string.StringRepr;

import java.util.Arrays;
import java.util.List;

/**
 * This class represents a command argument.
 *
 * @author Raffaele Canale
 * @version 1.5
 * @param <Type> Type of the argument
 */
public class Argument<Type> extends AbstractArgument<Type> {

    /**
     * Build an array argument. With such an argument, the user can input
     * several items for this argument (as a list).<br>
     * The minimum and maximum count for the array can be defined, if the user
     * does not respect them, an error message is automatically shown. <br><br>
     * The arguments parsing for this list stops if:
     * <ul>
     * <li>There are no more arguments to parse</li>
     * <li>The maximum arguments has been reached</li>
     * <li>A marked option is found (see {@link MarkedOption})</li>
     * </ul>
     *
     * @param <E>    Type of the elements
     * @param caster Caster of the elements
     * @param min    Minimum elements accepted for the array
     * @param max    Maximum elements accepted for the array (or -1 for no
     *               limit)
     *
     * @return The argument in which the user data will be stored
     */
    public static <E> Argument<E> array(TypeCaster<String, E> caster, int min, int max) {
        return new Argument<>(caster, min, max, null);
    }

    /**
     * Build an array argument. With such an argument, the user can input
     * several items for this argument (as a list).<br>
     * The minimum and maximum count for the array can be defined, if the user
     * does not respect them, an error message is automatically shown. <br><br>
     * The arguments parsing for this list stops if:
     * <ul>
     * <li>There are no more arguments to parse</li>
     * <li>The maximum arguments has been reached</li>
     * <li>A marked option is found (see {@link MarkedOption})</li>
     * </ul>
     *
     * @param <E>    Type of the elements
     * @param caster Caster of the elements
     * @param min    Minimum elements accepted for the array
     * @param max    Maximum elements accepted for the array (or -1 for no
     *               limit)
     * @param def    Default values that will be used if the user does not
     *               provide them (must respect the above limits)
     *
     * @return The argument in which the user data will be stored
     */
    public static <E> Argument<E> array(TypeCaster<String, E> caster, int min, int max,
            E... def) {
//        assert max > 1;
        return new Argument<>(caster, min, max,
                def == null ? null : Arrays.asList(def));
    }

    /**
     * Build a single optional argument. If the user does not input this
     * argument, a default value will be used.
     *
     * @param <E>    Type of the argument
     * @param caster Caster of type
     * @param def    Default value to set in the case the user didn't input the
     *               argument.
     *
     * @return The argument in which the user data will be stored.
     */
    public static <E> Argument<E> singleOptional(TypeCaster<String, E> caster,
            E def) {
        return new Argument<>(caster, 0, 1, 
                def == null ? null : Arrays.asList(def));
    }

    /**
     * Build a none optional argument. If the user does not input this argument,
     * an error message is shown.
     *
     * @param <E>    Type of the argument
     * @param caster Caster of type
     *
     * @return The argument in which the user data will be stored.
     */
    public static <E> Argument<E> single(TypeCaster<String, E> caster) {
        return new Argument<>(caster, 1, 1, null);
    }

    public static Argument<String> singleOptional(String def) {
        return singleOptional(new StringRepr(), def);
    }

    public static Argument<String> single() {
        return single(new StringRepr());
    }

    private Argument(TypeCaster<String, Type> caster, int min, int max, List<Type> def) {
        super(caster, min, max, def);
    }

    @Override
    Command registerFrom(Command cmd) {
        return registerWithMinMax(cmd, 0);
    }

//    /**
//     * @deprecated Use the others
//     */
//    public static <E> Argument<E> single(TypeCaster<String, E> caster, E def,
//            boolean isOptional) {
//        return new Argument<>(caster, isOptional ? 0 : 1, 1, def);
//    }
//    private final TypeCaster<String, Type> caster;
//    private final int min;
//    private final int max;
//    private List<Type> values;
//    private final List<Type> def;
//
////    Argument(TypeCaster<String, Type> caster, int min, int max,
////            Type def) {
////        this(caster, min, max, def == null ? null : Arrays.asList(def));
////    }
////
////    Argument(TypeCaster<String, Type> caster, int min, int max,
////            Type[] def) {
////        this(caster, min, max, def == null ? null : Arrays.asList(def));
////    }
//
//    private Argument(TypeCaster<String, Type> caster, int min, int max,
//            List<Type> def) {
//        this.def = new ArrayList<>(def);
//        this.caster = caster;
//        this.min = min < 0 ? 0 : min;
//        this.max = max < 0 ? Integer.MAX_VALUE : max;
//
//        assert !(def != null && def.size() < min);
//    }
//
//    public boolean isSet() {
//        return values != null;
//    }
//
//
//    public Type getValue() {
//        assert max == 1;
//        List<Type> v = getValues();
//        if (v == null || v.isEmpty()) {
//            return null;
//        }
//        return v.get(0);
//    }
//
//    public List<Type> getValues() {
//        if (!isSet()) {
//            return def;
//        }
//        if (def != null) {
//            for (int i = values.size(); i < def.size(); i++) {
//                values.add(def.get(i));
//            }
//        }
////        return isSet() ? values : def;
//        return values;
//    }
//
//    public List<Type> getDefaults() {
//        return def;
//    }
//
//    void reinit() {
//        values = null;
//    }
//
//    void take(Command cmd) throws InputException {
//        take(cmd, 0, true);
//    }
//
//    protected void take(Command cmd, final int from,
//            boolean autoBreakArray) throws InputException {
//        List<Type> result = new LinkedList<>();
//
//        int count = 0;
//        while (count < max && cmd.length() > from
//                && !(autoBreakArray && cmd.param(from).startsWith("-"))) {
//            result.add(cmd.param(from, caster));
//            cmd.drop(from);
//            count++;
//        }
//
//        if (count < min) {
//            throw new InputException("Missing argument(s)\n"
//                    + "Try 'help " + cmd.getName() + "'");
//        }
//
//        if (count > 0) {
//            values = result;
//        }
//    }
//
//    void setValues(List<Type> values) {
//        assert values != null;
//        this.values = values;
//    }
//
////    Type getValue() {
////        assert max == 1;
////        if (values == null || values.isEmpty()) {
////            return null;
////        } else {
////            return values.get(0);
////        }
////    }
////    
////    List<Type> getValues() {
////        assert max > 1;
////        return values;
////    }
//    @Override
//    public String toString() {
//        if (max == 1) {
//            return "VALUE(" + getValue() + ")";
//        }
//        return "VALUES " + getValues();
//    }
}
