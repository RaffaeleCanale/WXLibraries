//package com.wx.actionN;
//
//import com.wx.actionN.util.CommandContainer;
//import com.wx.console.InputException;
//import com.wx.console.command.Command;
//import com.wx.console.command.executable.UserConsoleInterface;
//import com.wx.console.command.executable.options.Help;
//import com.wx.console.command.executable.options.MarkedOption;
//import com.wx.grammar.GrammarException;
//
//import java.io.File;
//import java.io.IOException;
//import java.util.HashMap;
//import java.util.Map;
//
///**
// * Program context containing the hasMore commands. This will input a command from the user, execute it and start over.
// * Note this is a runnable and can also be used in threads.
// *
// * @author Raffaele Canale - raffaelecanale@gmail.com
// * @version 0.1
// *          <p>
// *          Project: WXLibraries File: AbstractCommandContext.java (UTF-8) Date: Apr 14, 2013
// */
//public class CommandContext_old implements Runnable {
//
//
//    private final Map<String, CommandAction> commandsAction;
//    private final Map<String, CommandAction> globalCommands;
//    private final UserConsoleInterface in;
//    //    protected final String mPrefix;
////    protected final String mSep;
//    private boolean stop;
//    private boolean exitAll;
//
//
//    /**
//     * Constructs a new context. Make sure to initialize the context with the methods {@link #addCommandToEnv(String,
//     * Action, java.io.File) } and {@link #addGlobalCommandToEnv(String, Action, java.io.File) }
//     *
//     * @param console        Console
//     * @param includeExitCmd Automatically the {@link ExitCommand}
//     */
//    public CommandContext_old(UserConsoleInterface console, boolean includeExitCmd) {
//        this.commandsAction = new HashMap<>();
//        this.globalCommands = new HashMap<>();
//        this.in = console;
//
//        if (includeExitCmd) {
////            addCommandToEnv("exit", new ExitCommand());
//        }
//    }
//
//    /**
//     * Executes the given command. <br><br> <b>Note:</b> If several commands are chained together, this method executes
//     * them all.
//     *
//     * @param cmd Command to execute
//     *
//     * @throws com.wx.console.InputException
//     */
//    public void executeCommand(Command cmd) throws InputException {
//        if (in.getConsole().getConsoleWidth() > 0) {
//            in.getConsole().probeWidthBuffer();
//        }
//
//        executeSingleCommand(cmd);
//
//        while (cmd.hasNext()) {
//            cmd = cmd.getNext();
//            executeSingleCommand(cmd);
//        }
//    }
//
//    /**
//     * Run the main loop, consisting in:<br> <ul> <li>Read a command from user input</li> <li>Execute it</li> </ul> <br>
//     * Note: This loop can be broke by calling {@link #exit()}
//     */
//    @Override
//    public void run() {
//        stop = false;
//        exitAll = false;
//
//        do {
//            try {
//                executeCommand(in.inputCommand());
//            } catch (InputException ex) {
//                in.getConsole().errln(ex.getMessage());
//            }
//        } while (!stop);
//    }
//
//    public final void addGlobalCommandToEnv(String name, Action action, File helpFile) throws IOException, GrammarException {
//        addGlobalCommandToEnv(name, action, new HelpLoader_old().load(helpFile));
//    }
//
//    /**
//     * Add a global command to this context. A global command will persist even through inner contexts.
//     *
//     * @param name   Command's name
//     * @param action Command
//     *
//     * @see #runInner(CommandContext_old)
//     */
//    public final void addGlobalCommandToEnv(String name, Action action, CommandContainer commandInfo) {
//        globalCommands.put(name, new CommandAction(commandInfo, action));
//    }
//
//
//    public final void addCommandToEnv(String name, Action action, CommandContainer commandInfo) {
//        commandsAction.put(name, new CommandAction(commandInfo, action));
//    }
//
//    /**
//     * Add a command to this context.
//     *
//     * @param name Command's name
//     * @param cmd  Command
//     */
//    public final void addCommandToEnv(String name, Action action, File helpFile) throws IOException, GrammarException {
//        addCommandToEnv(name, action, new HelpLoader_old().load(helpFile));
//    }
//
//    public final void addHelpCommand(String... names) {
//        assert names.length > 0;
//        Map<String, CommandAction> allCmds = new HashMap<>(commandsAction);
//        allCmds.putAll(globalCommands);
//        Help help = new Help(allCmds);
//
//        for (String n : names) {
////            commandsAction.put(n, help);
//        }
//    }
//
//    /**
//     * Exit the main run loop. Note that this will only have effect at the beginning of the next loop, so if a command
//     * is being awaited or executed, this will effect only afterwards.
//     */
//    public void exit() {
//        stop = true;
//    }
//
//    public UserConsoleInterface getIn() {
//        return in;
//    }
//
//    /**
//     * Switches the current context. Note that the new context will inherit of all the global commands.<br> Also note
//     * that this context is note stopped but put on hold. As soon as the new context stops, this one takes over (unless
//     * the 'exit all' option is used with the {@link ExitCommand}).
//     *
//     * @param context New context to switch to
//     */
//    public void runInner(CommandContext_old context) {
//        context.globalCommands.putAll(globalCommands);
//        context.run();
//
//        if (context.exitAll) {
//            exit();
//            exitAll = true;
//        }
//    }
//
//    private void executeSingleCommand(Command cmd) throws InputException {
//        CommandAction action = getActionFor(cmd.getName());
//
//        if (action == null) {
//            throw new InputException(404, "No command found for '"
//                    + cmd.getName() + "'");
//        } else {
//            action.loadAndExecute(in, cmd);
//        }
//    }
//
//    private CommandAction getActionFor(String name) {
//        CommandAction action = commandsAction.get(name);
//        if (action == null) {
//            action = globalCommands.get(name);
//        }
//        return action;
//    }
//
//    /**
//     * Exit the current context. A '-s' option can be added to exit all contexts.
//     */
//    private class ExitCommand extends com.wx.console.command.executable.options.CommandAction {
//
//        private final MarkedOption<?> exitAllOpt;
//
//        public ExitCommand() {
//            exitAllOpt = MarkedOption.markerOnly("-s");
//            addOption(exitAllOpt, "", "Exit context AND program");
//        }
//
//        @Override
//        public void execute(UserConsoleInterface in, com.wx.console.command.Command cmd)
//                throws InputException {
//            exit();
//            if (exitAllOpt.isSet()) {
//                exitAll = true;
//            }
//        }
//
//        @Override
//        protected String getMainHelpText() {
//            return "Exit the current context";
//        }
//    }
//
//    private class CommandAction {
//        private final CommandContainer commandInfo;
//        private final Action action;
//
//        private CommandAction(CommandContainer commandInfo, Action action) {
//            this.commandInfo = commandInfo;
//            this.action = action;
//        }
//
//        public void loadAndExecute(UserConsoleInterface in, Command cmd) throws InputException {
//            action.execute(commandInfo.toArguments(cmd));
//        }
//    }
//}
