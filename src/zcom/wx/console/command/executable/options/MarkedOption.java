package zcom.wx.console.command.executable.options;

import com.wx.action.command.Command;
import com.wx.util.representables.TypeCaster;
import com.wx.util.representables.string.StringRepr;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * This class represents a marked option. A marked option is an argument of the
 * form:<br><br>
 *
 * cmd <b>-m option</b> <br><br>
 * where <b>m</b> is the marker and <b>option</b> the
 * value of the option.<br>
 * Note that a marker can also be alone, as well as it can be followed by
 * several options.
 *
 *
 * @author Raffaele Canale
 * @version 1.5
 */
public class MarkedOption<Type> extends AbstractArgument<Type> {

    /**
     * Build a marked options with no associated values. <br><br>
     * To test if the user did input or not the marked, use {@link #isSet()}.
     *
     * @param markers Any marker that identify this option.
     *
     * @return The built option
     */
    public static MarkedOption<?> markerOnly(String... markers) {
        return new MarkedOption<>(null, 0, 0, markers, null);
    }

    public static <E> MarkedOption<E> singleOptional(TypeCaster<String, E> caster,
            String... markers) {
        return new MarkedOption<>(caster, 0, 1, markers, null);
    }
    
    public static MarkedOption<String> singleOptional(String... markers) {
        return singleOptional(new StringRepr(), markers);
    }
    
    public static <E> MarkedOption<E> single(E def, 
            TypeCaster<String, E> caster, String... markers) {
        return new MarkedOption<>(caster, 1, 1, markers, 
                def == null ? null : Arrays.asList(def));
    }
    
    public static MarkedOption<String> single(String def, String... markers) {
        return single(def, new StringRepr(), markers);
    }
    
    public static <E> MarkedOption<E> array(TypeCaster<String, E> caster,
            int min, int max, String... markers) {
        return array(caster, min, max, null, markers);
    }

    public static <E> MarkedOption<E> array(TypeCaster<String, E> caster,
            int min, int max, E[] def, String... markers) {
//        assert max > 1;
        return new MarkedOption<>(caster, min, max, markers, 
                def == null ? null : Arrays.asList(def));
    }

    private final String[] markers;

    private MarkedOption(TypeCaster<String, Type> caster, int min, int max,
            String[] markers, List<Type> def) {
        super(caster, min, max, def);
        assert markers.length > 0;
        this.markers = markers;
    }

    @Override
    public String getUsage(String optionName, final boolean brief) {
        String markerUsage = markers[0];
        for (int i = 1; !brief && i < markers.length; i++) {
            markerUsage += ", " + markers[i];
        }

        return markerUsage + " " + super.getUsage(optionName, brief);
    }

    @Override
    Command registerFrom(Command cmd) {
        int index = cmd.indexOf(markers);
        
        if (index >= 0) {
            cmd = registerWithMinMax(cmd.drop(index), index);
            if (!isSet()) {
                setValues(new LinkedList<>());
            }
        }
        
        return cmd;
    }

}
