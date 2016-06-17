//package zcom.wx.console.command.executable.options;
//
//import com.wx.console.Console;
//import com.wx.action.command.Command;
//import com.wx.console.UserConsoleInterface;
//
//import java.util.Map;
//
///**
// * Implements a mode command. A mode command is a command of the type:<br><br>
// *
// * cmd MODE [MODE_OPTIONS]
// * <br><br>
// *
// *
// * @author Raffaele Canale
// * @version 1.5
// */
//public class ModeCommand extends CommandAction {
//
//    private final Map<String, CommandAction> modes;
//    private final Argument<String> mode;
//    private final String desc;
////    private MarkedOption help;
//
//
//    public ModeCommand(Map<String, CommandAction> modes, String desc) {
//        this.modes = modes;
//        this.desc = desc;
//
//        this.mode = Argument.single();
////        this.mode = Argument.single(null, true);
//        addArg(mode, "mode", "Command mode, see MODES section for more details");
//    }
//
//    public void addHelp(String... helpName) {
////        help = MarkedOption.markerOnly(helpName);
////        addOption(help, null, "Show general help");
//        ModeHelpCmd helpCmd = new ModeHelpCmd();
//
//        for (String name : helpName) {
//            modes.put(name, helpCmd);
//        }
//    }
//
//    @Override
//    protected final void execute(UserConsoleInterface in, Command cmd) {
////        if (help.isSet() || !mode.isSet()) {
////            Console c = in.getConsole();
////
////            Help.printHelp(c, ModeCommand.this, cmd.getOriginalName());
////            Help.printModesHelp(c, modes);
////            return;
////        }
//
//        CommandAction action = modes.get(mode.getValue());
//        if (action == null) {
//            throw new IllegalArgumentException("Unknown mode: " + mode.getValue());
//        }
//
//        action.loadAndExecute(in, cmd);
//    }
//
////    @Override
////    protected final int execute(UserConsoleInterface in, String cmdName)
////            throws InputException {
////        throw new UnsupportedOperationException();
////    }
//
//    @Override
//    protected String getMainHelpText() {
//        return desc;
//    }
//
//    private class ModeHelpCmd extends CommandAction {
//
//        @Override
//        protected void execute(UserConsoleInterface in, Command cmd) {
//            Console c = in.getConsole();
//
//            Help.printHelp(c, ModeCommand.this, cmd.getOriginalName());
//            Help.printModesHelp(c, modes);
//        }
//
//        @Override
//        protected String getMainHelpText() {
//            return "Show help";
//        }
//
//    }
//}
