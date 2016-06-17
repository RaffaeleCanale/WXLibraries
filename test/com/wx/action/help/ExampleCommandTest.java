package com.wx.action.help;

import com.wx.action.arg.ArgumentsSupplier;
import com.wx.action.util.CommandContainer;
import com.wx.action.command.Command;
import com.wx.action.command.DefaultCommandBuilder;
import com.wx.grammar.ParseException;
import org.junit.Test;

import java.io.IOException;
import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;

/**
 * Created on 03/01/2015
 *
 * @author Raffaele Canale (raffaelecanale@gmail.com)
 * @version 0.1
 */
public class ExampleCommandTest {

    private final CommandContainer exampleCommand;

    public ExampleCommandTest() throws ParseException, IOException {
        exampleCommand = loadExampleCommand();
    }

    private static CommandContainer loadExampleCommand() throws IOException, ParseException {
        final CommandContainer[] commands = new HelpLoader().load(ExampleCommandTest.class.getResourceAsStream("CommandExample.sp"));

        assertEquals(1, commands.length);
        assertEquals("foo", commands[0].getName());

        return commands[0];
    }

    @Test
    public void test1() {
        final Command cmd = new DefaultCommandBuilder().build("foo arg1 1 2 3 4");
        final ArgumentsSupplier args = exampleCommand.toArguments(cmd);

        assertEquals("arg1", args.supplyString());
        assertNull(args.supplyString());
        assertEquals(Arrays.asList(1, 2, 3, 4), args.supplyIntegerList());
        assertFalse(args.hasMore());
    }

    @Test
    public void test2() {
        final Command cmd = new DefaultCommandBuilder().build("foo arg1 -bar arg2 1");
        final ArgumentsSupplier args = exampleCommand.toArguments(cmd);

        assertEquals("arg1", args.supplyString());
        assertEquals("arg2", args.supplyString());
        assertEquals(Arrays.asList(1), args.supplyIntegerList());
        assertFalse(args.hasMore());
    }

}
