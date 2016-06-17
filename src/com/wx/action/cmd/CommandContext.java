package com.wx.action.cmd;

import com.wx.action.Action;
import com.wx.action.arg.ArgumentsSupplier;
import com.wx.action.arg.ObjectsSupplier;
import com.wx.action.help.HelpLoader;
import com.wx.action.help.HelpPrinter;
import com.wx.action.type.CVBFactory;
import com.wx.action.type.CasterCVBFactory;
import com.wx.action.type.InteractiveCVBFactory;
import com.wx.action.util.CommandContainer;
import com.wx.action.util.PropertyContainer;
import com.wx.console.system.UnixJLineConsole;
import com.wx.action.command.Command;
import com.wx.action.command.DefaultCommandBuilder;
import com.wx.console.UserConsoleInterface;
import com.wx.grammar.ParseException;
import jline.console.completer.StringsCompleter;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

/**
 * Created on 02/01/2015
 *
 * @author Raffaele Canale (raffaelecanale@gmail.com)
 * @version 0.1
 */
public class CommandContext implements Runnable {

    private static CommandContainer exitCmdContainer;
    private static CommandContainer helpCmdContainer;
    private static CommandContainer interactiveCmdContainer;

    private static CommandContainer getExitCmdContainer() throws IOException {
        if (exitCmdContainer == null) {
            loadCommands();
        }

        return exitCmdContainer;
    }

    private static CommandContainer getHelpCmdContainer() throws IOException {
        if (helpCmdContainer == null) {
            loadCommands();
        }
        return helpCmdContainer;
    }

//    private static CommandContainer getInteractiveCmdContainer() throws IOException {
//        if (interactiveCmdContainer == null) {
//            loadCommands();
//        }
//
//        return interactiveCmdContainer;
//    }

    private static void loadCommands() throws IOException {
        CommandContainer[] commands;
        try {
            commands = new HelpLoader().load(CommandContext.class.getResourceAsStream("ExitHelp.sp"));
        } catch (ParseException e) {
            throw new IOException(e);
        }
        assert commands.length == 2;

        for (CommandContainer cmd : commands) {
            switch (cmd.getName()) {
                case "help":
                    helpCmdContainer = cmd;
                    break;
                case "exit":
                    exitCmdContainer = cmd;
                    break;
                default:
                    throw new IOException("Unexpected command: " + cmd.getName());
            }
        }
    }

    private final Map<String, CommandPair> localCommands;
    private final Map<String, CommandPair> globalCommands;
    private final CommandPair defaultCommand;
    private final UserConsoleInterface in;

    private boolean stop;
    private boolean exitAll;

    private CommandContext(Map<String, CommandPair> localCommands, Map<String, CommandPair> globalCommands, UserConsoleInterface in, CommandPair defaultCommand, boolean addHelp, boolean addExit) throws IOException {
        this.localCommands = localCommands;
        this.globalCommands = globalCommands;
        this.in = in;
        this.defaultCommand = defaultCommand;

        if (addHelp) {
            CommandContainer help = getHelpCmdContainer();
            localCommands.put(help.getName(), new CommandPair(help, new HelpCmd()));
        }

        if (addExit) {
            CommandContainer exit = getExitCmdContainer();
            localCommands.put(exit.getName(), new CommandPair(exit, new ExitCmd()));
        }
    }

    //    public CommandContext(UserConsoleInterface in, boolean includeExit, boolean includeHelp) throws IOException {
//        this.in = in;
//
//        this.localCommands = new HashMap<>();
//        this.globalCommands = new HashMap<>();
//
//        if (includeExit) {
//            addCommandToEnv(getExitCmdContainer(), new ExitCmd());
//        }
//        if (includeHelp) {
//            addCommandToEnv(getHelpCmdContainer(), new HelpCmd());
//        }
//    }
//
//    public void addCommandToEnv(CommandContainer cmd, Action action) {
////        addCompleter(cmd);
//        localCommands.put(cmd.getName(), new CommandPair(cmd, action));
//    }
//
//    public void addCommandToGlobalEnv(CommandContainer cmd, Action action) {
//        globalCommands.put(cmd.getName(), new CommandPair(cmd, action));
//    }

    public void addCompleters() {

        if (in.getConsole().getClass().equals(UnixJLineConsole.class)) {
            UnixJLineConsole jlc = (UnixJLineConsole) in.getConsole();
            List<String> allCommands = new LinkedList<>(localCommands.keySet());
            allCommands.addAll(globalCommands.keySet());

            jlc.addCompleter(new StringsCompleter(allCommands));
        }
    }

    public void runOrExecute(String[] args) {
        if (args == null  || args.length == 0) {
            run();
        } else {
            in.setNextCommand(args);
            try {
                executeCommand(in.inputCommand());
            } catch (IllegalArgumentException ex) {
                in.getConsole().errln(ex.getMessage());
            }
        }
    }

    @Override
    public void run() {
        stop = false;
        exitAll = false;

        do {
            try {
                executeCommand(in.inputCommand());
            } catch (IllegalArgumentException ex) {
                in.getConsole().errln(ex.getMessage());
            }
        } while (!stop);
    }

    public void runInner(CommandContext context) {
        context.globalCommands.putAll(globalCommands);
        context.run();

        if (context.exitAll) {
            exit();
            exitAll = true;
        }
    }


    public void executeCommand(Command cmd) {
        if (in.getConsole().getConsoleWidth() > 0) {
            in.getConsole().probeWidthBuffer();
        }

        executeSingleCommand(cmd);

        while (cmd.hasNext()) {
            cmd = cmd.getNext();
            executeSingleCommand(cmd);
        }
    }

    public void exit() {
        stop = true;
    }

    public void executeSingleCommand(Command cmd) {
        CommandPair action = getActionFor(cmd.getName());

        if (action == null) {
            if (!cmd.isActionCommand()) {
                if (defaultCommand != null) {
                    defaultCommand.execute(cmd.moveNameToFirstArg(), in);
                } else {
                    throw new IllegalArgumentException("No command found for '"
                            + cmd.getName() + "'");
                }
            }
        } else {
            action.execute(cmd, in);
        }
    }



    private CommandPair getActionFor(String name) {
        CommandPair action = localCommands.get(name);
        if (action == null) {
            action = globalCommands.get(name);
        }
        return action;
    }


    private class ExitCmd implements Action {
        @Override
        public void execute(UserConsoleInterface in, ArgumentsSupplier args) {
            final boolean exitAll = args.supplyBoolean();

            exit();
            if (exitAll) {
                CommandContext.this.exitAll = true;
            }
        }
    }

    private class HelpCmd implements Action {
        @Override
        public void execute(UserConsoleInterface in, ArgumentsSupplier args) {
            String requestName = args.supplyString();
            final HelpPrinter helpPrinter = new HelpPrinter(in.getConsole());

            if (requestName != null) {
                final CommandPair pair = getActionFor(requestName);
                if (pair == null) {
                    throw new IllegalArgumentException("No help found for '" + requestName + "'");
                } else {
                    helpPrinter.printHelp(pair.container);
                }

            } else {
                for (CommandPair pair : localCommands.values()) {
                    helpPrinter.printUsage(pair.container, true);
                }
                for (CommandPair pair : globalCommands.values()) {
                    helpPrinter.printUsage(pair.container, true);
                }
            }
        }
    }

    private class InteractiveCmd implements Action {
        @Override
        public void execute(UserConsoleInterface in, ArgumentsSupplier args) {
            final String cmdName = args.supplyString();

            final CVBFactory factory = CVBFactory.instance();
            if (!(factory instanceof CasterCVBFactory)) {
                throw new AssertionError("Only compatible with a subclass of " + CasterCVBFactory.class);
            }

            CVBFactory.setInstance(new InteractiveCVBFactory((CasterCVBFactory) factory, in));

            final CommandPair pair = getActionFor(cmdName);
            pair.execute(new DefaultCommandBuilder().build(cmdName), in);
        }
    }


    private static class CommandPair {
        private final CommandContainer container;
        private final Action action;

        public CommandPair(CommandContainer container, Action action) {
            this.container = container;
            this.action = action;
        }

        public void execute(Command cmd, UserConsoleInterface in) {
            action.execute(in, container.toArguments(cmd));
        }
    }

    public static class Builder {

        private final Map<String, CommandContainer> loadedDescriptions = new HashMap<>();
        private final Set<String> attachedDescriptions = new HashSet<>();

        private final Map<String, CommandPair> localCommands = new HashMap<>();
        private final Map<String, CommandPair> globalCommands = new HashMap<>();
        private final boolean ignoreCommandsWithoutActions;

        private CommandPair defaultCommand = null;
        private boolean addHelpCmd;
        private boolean addExitCmd;
        private boolean addInteractiveCmd;

        public Builder() {
            this(false);
        }

        public Builder(boolean ignoreCommandsWithoutActions) {
            this.ignoreCommandsWithoutActions = ignoreCommandsWithoutActions;
        }

        private void check(CommandPair old) {
            if (old != null) {
                throw new IllegalArgumentException("Action already set for " + old.container.getName());
            }
        }

        private CommandContainer getLoadedDescription(String name) {
            CommandContainer desc = loadedDescriptions.get(name);
            if (desc != null) {
                attachedDescriptions.add(name);
            }

            return desc;
        }

        public Builder loadDescriptionsFrom(String resourceName, Object... params) throws IOException, ParseException {
            final CommandContainer[] load = new HelpLoader(params).load(resourceName);

            for (CommandContainer cmd : load) {
                loadedDescriptions.put(cmd.getName(), cmd);
            }

            return this;
        }

        public Builder addCommandToEnv(CommandContainer description, Action action) {
            CommandPair old = localCommands.put(description.getName(), new CommandPair(description, action));
            check(old);
            return this;
        }

        public Builder addCommandToGlobalEnv(CommandContainer description, Action action) {
            CommandPair old = globalCommands.put(description.getName(), new CommandPair(description, action));
            check(old);

            return this;
        }

        public Builder attachAction(String name, Action action) {
            CommandContainer description = getLoadedDescription(name);
            if (description == null) {
                throw new IllegalArgumentException("Description not present for: " + name);
            }

            return addCommandToEnv(description, action);
        }

        public Builder attachGlobalAction(String name, Action action) {
            CommandContainer description = getLoadedDescription(name);
            if (description == null) {
                throw new IllegalArgumentException("Description not present for: " + name);
            }

            return addCommandToGlobalEnv(description, action);
        }

        public Builder addDefaultAction(CommandContainer description, Action action) {
            defaultCommand = new CommandPair(description, action);
            return this;
        }

        public Builder addDefaultAction(String descriptionName) {
            CommandPair pair = localCommands.get(descriptionName);
            if (pair == null) {
                pair = globalCommands.get(descriptionName);
                if (pair == null) {
                    throw new IllegalArgumentException("Unknown command " + descriptionName);
                }
            }

            defaultCommand = pair;
            return this;
        }

        public Builder addDefaultAction(String descriptionName, Action action) {
            CommandContainer description = getLoadedDescription(descriptionName);
            if (description == null) {
                throw new IllegalArgumentException("Description not present for: " + descriptionName);
            }

            return addDefaultAction(description, action);
        }

        public Builder addDefaultAction(Action action) {
            PropertyContainer arguments = new PropertyContainer.Builder()
                    .setType("string")
                    .setMin(0)
                    .setMax(-1)
                    .setName("arguments")
                    .setHelpText("Arguments for the default action").build();

            CommandContainer container = new CommandContainer(Arrays.asList(arguments), "Default command", "defaultCmd") {
                @Override
                public ArgumentsSupplier toArguments(Command cmd) {
                    return new ObjectsSupplier(cmd.getParameters());
                }
            };

            addDefaultAction(container, action);

            return this;
        }

        public Builder addHelpCommand() {
            addHelpCmd = true;
            return this;
        }

        public Builder addExitCommand() {
            addExitCmd = true;
            return this;
        }

        public CommandContext build(UserConsoleInterface in) throws IOException {
            if (!ignoreCommandsWithoutActions) {
                HashSet<String> remaining = new HashSet<>(loadedDescriptions.keySet());
                remaining.removeAll(attachedDescriptions);
                if (!remaining.isEmpty()) {
                    throw new IllegalArgumentException("The following commands have no action:\n" + remaining);
                }
            }

            return new CommandContext(localCommands, globalCommands, in, defaultCommand, addHelpCmd, addExitCmd);
        }
    }
}
