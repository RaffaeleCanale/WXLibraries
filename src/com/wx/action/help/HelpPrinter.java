package com.wx.action.help;

import com.wx.action.util.CommandContainer;
import com.wx.action.util.PropertyContainer;
import com.wx.console.Console;
import com.wx.console.color.Color;

import java.util.List;
import java.util.Map;

/**
 * Created on 02/01/2015
 *
 * @author Raffaele Canale (raffaelecanale@gmail.com)
 * @version 0.1
 */
public class HelpPrinter {

    private static final int MAX_NAMES_TO_DISPLAY = 3;

    private final Console c;


    public HelpPrinter(Console c) {
        this.c = c;
    }

    public void printHelp(CommandContainer cmd) {
        printTitle("USAGE");
        printUsage(cmd, false);
        printArgsHelp(cmd);

        printTitle("DESCRIPTION");
        printDescription(cmd);

        if (!cmd.getMarkedOptions().isEmpty()) {
            printTitle("OPTIONS");
            printOptionsDesc(cmd);
        }
    }

    public void printModesHelp(Map<String, CommandContainer> modes) {
        printTitle("MODES");

        for (CommandContainer cmd : modes.values()) {
            printTitle(cmd.getName());
            printDescription(cmd);
            c.println();
            printUsage(cmd, true);
            printArgsHelp(cmd);
            printOptionsDesc(cmd);
//            printUsage(c, cmd.getKey(), cmd.getValue(), true);
//            printArgsHelp(c, cmd.getValue());
//            printOptionsDesc(c, cmd.getValue());
//            printHelp(c, cmd.getValue(), cmd.getKey());
        }
    }

    private void printOptionsDesc(CommandContainer cmd) {
        cmd.getMarkedOptions().forEach(this::printOptionHelp);
    }

    public void printOptionHelp(PropertyContainer option) {
        c.print("\t");
        printInlineOption(option, false);
        c.resetStyle();
        c.println("\n\t\t" + option.getHelpText().replaceAll("\n", "\n\t\t"));
        String[] defaults = option.getDefaultValues();
        if (defaults != null && defaults.length != 0) {
            c.setBold();
            c.print("\t\tDefaults: ");
            c.resetBold();
            c.setColor(Color.Yellow);
            boolean first = true;
            for (Object def : defaults) {
                if (first) {
                    first = false;
                } else {
                    c.resetStyle();
                    c.print(", ");
                    c.setColor(Color.Yellow);
                }
                c.print(def);

            }
            c.println();
            c.resetStyle();
        }
        c.println();
    }

    private void printDescription(CommandContainer cmd) {
        c.println("\t" + cmd.getHelpText().replaceAll("\n", "\n\t"));
    }

    public void printUsage(CommandContainer cmd, boolean withOptions) {
        c.print("\t" + cmd.getName() + " ");
        c.setBold();

        for (PropertyContainer arg : cmd.getArguments()) {
            c.print(getPropertyUsage(arg, true) + " ");
        }
        c.resetBold();

        List<PropertyContainer> options = cmd.getMarkedOptions();
        if (!options.isEmpty()) {
            if (withOptions) {
                for (PropertyContainer option : options) {
                    printInlineOption(option, true);
                }
            } else {
                c.setColor(Color.Cyan);
                c.print("[OPTIONS]");
            }
        }
        c.println();
        c.resetStyle();
    }

    private void printTitle(String title) {
        c.println();
        c.setBold();
        c.println(title);
        c.resetBold();
    }

    private void printArgsHelp(CommandContainer cmd) {
        c.println();
        for (PropertyContainer arg : cmd.getArguments()) {
            printArgHelp(arg);
        }
    }

    public void printArgHelp(PropertyContainer arg) {
        c.setBold();
        c.print("\t\t" + arg.getName());
        c.resetBold();
        c.println(": " + arg.getHelpText());
    }

    private void printInlineOption(PropertyContainer property, boolean brief) {
        c.setColor(Color.Cyan);
        String usage = getPropertyUsage(property, brief);
        int nameIndex = usage.indexOf('<');
        if (nameIndex < 0) {
            c.print(usage + " ");
        } else {
            c.print(usage.substring(0, nameIndex));
            c.resetStyle();
            c.setBold();
            c.setColor(Color.Magenta);
            c.print(usage.substring(nameIndex) + " ");
        }
    }



    public String getPropertyUsage(PropertyContainer prop, boolean brief) {
        String result = "";

        if (prop.isMarkedOption()) {
            final String[] markers = prop.getMarkers();
            String markerUsage = markers[0];
            for (int i = 1; !brief && i < markers.length; i++) {
                markerUsage += ", " + markers[i];
            }

//            return markerUsage + " " + super.getUsage(optionName, brief);
            result = markerUsage + " ";
        }


        if (prop.getMax() == 0) {
            return result;
        }


        if (prop.getMin() == 0 && prop.getMax() > 0) {
            result += "</";
            result += getPropertyArrayUsage(prop.getName(), prop.getMax());
            result += ">";
        } else {
            result += "<";
            result += getPropertyArrayUsage(prop.getName(), prop.getMax());
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
    private String getPropertyArrayUsage(String name, int max) {
        if (max < 0) {
            max = MAX_NAMES_TO_DISPLAY + 1;
        }
        return getPropertyArrayUsage(name, Math.min(max, MAX_NAMES_TO_DISPLAY),
                max > MAX_NAMES_TO_DISPLAY);
    }
    private String getPropertyArrayUsage(String name, int count, boolean includeDots) {
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
}
