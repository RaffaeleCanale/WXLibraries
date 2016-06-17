//package zcom.wx.console.command.executable.options;
//
//import com.wx.console.Console;
//import com.wx.console.color.Color;
//import com.wx.action.command.Command;
//import com.wx.console.UserConsoleInterface;
//
//import java.util.List;
//import java.util.Map;
//
///**
// *
// * @author Canale
// */
//public class Help extends CommandAction {
//
//    private final Map<String, CommandAction> cmds;
//    private final Argument<String> name = Argument.singleOptional(null);
//
//    public Help(Map<String, CommandAction> cmds) {
//        this.cmds = cmds;
//        addArg(name, "name", "Command name for more detailled information");
//    }
//
//    @Override
//    protected void execute(UserConsoleInterface in, Command cmd) {
//        Console c = in.getConsole();
//
//        if (name.isSet()) {
//            if (name.getValue().equals(cmd.getName())) {
//                printHelp(c, this, cmd.getName());
//            } else {
//                CommandAction mode = cmds.get(name.getValue());
//                if (mode == null) {
//                    throw new IllegalArgumentException("No help found for '" + name.getValue() + "'");
//                } else {
//                    printHelp(c, mode, name.getValue());
//                }
//            }
//        } else {
//            for (Map.Entry<String, CommandAction> entry : cmds.entrySet()) {
//                printUsage(c, entry.getKey(), entry.getValue(), true);
//            }
//        }
//    }
//
//    @Override
//    protected String getMainHelpText() {
//        return "Show this help";
//    }
//
//    public static void printHelp(Console c, CommandAction cmd, String name) {
//        printTitle(c, "USAGE");
//        printUsage(c, name, cmd, false);
//        printArgsHelp(c, cmd);
//
//        printTitle(c, "DESCRIPTION");
//        printDescription(c, cmd);
//
//        if (!cmd.getOptions().isEmpty()) {
//            printTitle(c, "OPTIONS");
//            printOptionsDesc(c, cmd);
//        }
//    }
//
//    public static void printModesHelp(Console c, Map<String, CommandAction> modes) {
//        printTitle(c, "MODES");
//
//        for (Map.Entry<String, CommandAction> mode : modes.entrySet()) {
//            printTitle(c, mode.getKey());
//            printDescription(c, mode.getValue());
//            c.println();
//            printUsage(c, mode.getKey(), mode.getValue(), true);
//            printArgsHelp(c, mode.getValue());
//            printOptionsDesc(c, mode.getValue());
////            printUsage(c, mode.getKey(), mode.getValue(), true);
////            printArgsHelp(c, mode.getValue());
////            printOptionsDesc(c, mode.getValue());
////            printHelp(c, mode.getValue(), mode.getKey());
//        }
//    }
//
//    private static void printOptionsDesc(Console c, CommandAction cmd) {
//        for (OptionContainer option : cmd.getOptions()) {
//            c.print("\t");
//            printInlineOption(c, option, false);
//            c.resetStyle();
//            c.println("\n\t\t" + option.getOptionDesc().replaceAll("\n", "\n\t\t"));
//            List<?> defaults = option.getOption().getDefaults();
//            if (defaults != null && !defaults.isEmpty()) {
//                c.setBold();
//                c.print("\t\tDefaults: ");
//                c.resetBold();
//                c.setColor(Color.Yellow);
//                boolean first = true;
//                for (Object def : defaults) {
//                    if (first) {
//                        first = false;
//                    } else {
//                        c.resetStyle();
//                        c.print(", ");
//                        c.setColor(Color.Yellow);
//                    }
//                    c.print(def);
//
//                }
//                c.println();
//                c.resetStyle();
//            }
//            c.println();
//        }
//    }
//
//    private static void printDescription(Console c, CommandAction cmd) {
//        c.println("\t" + cmd.getMainHelpText().replaceAll("\n", "\n\t"));
//    }
//
//    private static void printUsage(Console c, String name, CommandAction cmd,
//            boolean withOptions) {
//        c.print("\t" + name + " ");
//        c.setBold();
//        for (OptionContainer arg : cmd.getArgs()) {
//            c.print(arg.getUsage(true) + " ");
//        }
//        c.resetBold();
//        if (!cmd.getOptions().isEmpty()) {
//            if (withOptions) {
//                for (OptionContainer option : cmd.getOptions()) {
//                    printInlineOption(c, option, true);
//                }
//            } else {
//                c.setColor(Color.Cyan);
//                c.print("[OPTIONS]");
//            }
//        }
//        c.println();
//        c.resetStyle();
//    }
//
//    private static void printTitle(Console c, String title) {
//        c.println();
//        c.setBold();
//        c.println(title);
//        c.resetBold();
//    }
//
//    private static void printArgsHelp(Console c, CommandAction cmd) {
//        c.println();
//        for (OptionContainer arg : cmd.getArgs()) {
//            c.setBold();
//            c.print("\t\t" + arg.getOptionName());
//            c.resetBold();
//            c.println(": " + arg.getOptionDesc());
//        }
//    }
//
//    private static void printInlineOption(Console c, OptionContainer option,
//            boolean brief) {
//        c.setColor(Color.Cyan);
//        String usage = option.getUsage(brief);
//        int nameIndex = usage.indexOf('<');
//        if (nameIndex < 0) {
//            c.print(usage + " ");
//        } else {
//            c.print(usage.substring(0, nameIndex));
//            c.resetStyle();
//            c.setBold();
//            c.setColor(Color.Magenta);
//            c.print(usage.substring(nameIndex) + " ");
//        }
//    }
//}
