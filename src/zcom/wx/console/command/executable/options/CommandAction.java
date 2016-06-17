//package zcom.wx.console.command.executable.options;
//
//import com.wx.action.command.Command;
//import com.wx.action.command.executable.CommandContext;
//import com.wx.console.UserConsoleInterface;
//
//import java.util.LinkedList;
//import java.util.List;
//
///**
// * This class represents a command, more specifically, the command as a whole
// * with its options and its action. <br><br>
// * Note that the command's name information is note contained in the class but
// * is set as an implicit relation in the {@link CommandContext}, this class is
// * really only
// * about the command's action.
// *
// * @author Raffaele Canale
// * @version 1.5
// */
//public abstract class CommandAction {
//
//    private final List<OptionContainer> options = new LinkedList<>();
//    private final List<OptionContainer> args = new LinkedList<>();
//
//    /**
//     * Executes this action with the given command.
//     *
//     * @param cmd Command as input by the user.
//     * @param in  User interface
//     *
//     *
//     */
//    public final void loadAndExecute(UserConsoleInterface in, Command cmd) {
//        initOptions();
//
//        for (OptionContainer cont : options) {
//            cmd = cont.getOption().registerFrom(cmd);
//        }
//
//        for (OptionContainer cont : args) {
//            cmd = cont.getOption().registerFrom(cmd);
//        }
//
//        execute(in, cmd);
//    }
//
////    // Ugly work around for ModeCommand
////    int execute0(UserConsoleInterface in, Command cmd) throws InputException {
////        return execute(in, cmd.getOriginalName());
////    }
//    /**
//     * Add an option to this command description.
//     *
//     * @param option      Option that will be filled with the user data if any
//     * @param name        Name of the option's parameter if any
//     * @param description Description of this option
//     */
//    protected void addOption(MarkedOption<?> option, String name,
//            String description) {
//        options.add(new OptionContainer(option, name, description));
//    }
//
//    /**
//     * Add an argument to this command description. Note that arguments are not
//     * optional and an {@code InputException} will automatically be thrown if it
//     * is missing.
//     *
//     * @param option      Object that will be filled with the user data
//     * @param name        Name of the argument
//     * @param description Description of this argument
//     */
//    protected void addArg(Argument<?> option, String name, String description) {
//        args.add(new OptionContainer(option, name, description));
//    }
//
//    /**
//     * Execute this commands action. Note that past this point, all the options
//     * and arguments set with {@link #addArg} and {@link #addOption} are
//     * guaranteed to be correctly initialized with the user input.
//     *
//     * @param in  User console interface
//     * @param cmd Original command
//     *
//     */
//    protected abstract void execute(UserConsoleInterface in,
//            Command cmd);
//
//    /**
//     *
//     * @return this command's description
//     */
//    protected abstract String getMainHelpText();
//
//    List<OptionContainer> getArgs() {
//        return args;
//    }
//
//    List<OptionContainer> getOptions() {
//        return options;
//    }
//
//    private void initOptions() {
//        options.forEach(op -> op.getOption().reinit());
//        args.forEach(op -> op.getOption().reinit());
//    }
//}
