/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wx.action.command;

import com.wx.util.representables.string.IntRepr;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.*;

/**
 *
 * @author Raffaele
 * 
 * TODO take, getOption, drop, contains
 */
public class CommandsTest {
    
    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }
    
    private CommandBuilder builder;
    
    public CommandsTest() throws IOException {
//        DefaultCommandBuilder.init();
        builder = new DefaultCommandBuilder();
    }
    
    
    @Test
    public void simple() {
        Command cmd = builder.build("test simple easy");
        
        assertEquals(cmd.length(), 2);
        assertEquals(cmd.getName(), "test");
        assertEquals(cmd.param(0), "simple");
        assertEquals(cmd.param(1), "easy");
    }
    
    @Test
    public void indexOf() {
        Command cmd = builder.build("test a1 a2 a3");
        
        assertEquals(1, cmd.indexOf("a2"));
    }
    
    
    
    @Test
    public void quotes() {
        Command cmd = builder.build("test \"  with quotes ''\"'\"t\"'");
        assertEquals(cmd.length(), 2);
        assertEquals(cmd.getName(), "test");
        assertEquals(cmd.param(0), "  with quotes ''");
        assertEquals(cmd.param(1), "\"t\"");
    }
    
    @Test
    public void split() {
        Command cmd = builder.build("test ip:port");
        cmd = cmd.splitParam(0, ":", 2);
        
        assertEquals(cmd.length(), 2);
        assertEquals(cmd.getName(), "test");
        assertEquals(cmd.param(0), "ip");
        assertEquals(cmd.param(1), "port");
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void expectedSplit() {
        Command cmd = builder.build("test ip:port");
        cmd.splitParam(0, ":", 3);        
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void param() {
        Command cmd = builder.build("test a1 a2 a3");
        assertNull(cmd.tryParam(3));
        cmd.param(3);
    }
    
    @Test
    public void param2() {
        Command cmd = builder.build("test a1 a2 a3");
        assertNull(cmd.tryParam(1, new IntRepr()));
    }
    
    public void stack() {
        Command cmd = builder.build("test a1 a2 a3");
        assertEquals("a1 a2 a3", cmd.stackFrom(0));
    }
    
    @Test
    public void multi_param() {
        Command cmd = builder.build("test a b, c d, e f");
        
        assertEquals(cmd.length(), 2);
        assertEquals(cmd.getName(), "test");
        assertEquals(cmd.param(0), "a");
        assertEquals(cmd.param(1), "b");
        
        cmd = cmd.getNext();
        assertNotNull(cmd);
        assertEquals(cmd.length(), 2);
        assertEquals(cmd.getName(), "test");
        assertEquals(cmd.param(0), "c");
        assertEquals(cmd.param(1), "d");
        
        cmd = cmd.getNext();
        assertNotNull(cmd);
        assertEquals(cmd.length(), 2);
        assertEquals(cmd.getName(), "test");
        assertEquals(cmd.param(0), "e");
        assertEquals(cmd.param(1), "f");
    }
    
    @Test
    public void multi_cmd() {
        Command cmd = builder.build("test a b; debug c d");
        
        assertEquals(cmd.length(), 2);
        assertEquals(cmd.getName(), "test");
        assertEquals(cmd.param(0), "a");
        assertEquals(cmd.param(1), "b");
        
        cmd = cmd.getNext();
        assertNotNull(cmd);
        assertEquals(cmd.length(), 2);
        assertEquals(cmd.getName(), "debug");
        assertEquals(cmd.param(0), "c");
        assertEquals(cmd.param(1), "d");
        
        cmd = builder.build("test a b;");
        assertEquals(cmd.length(), 2);
        assertEquals(cmd.getName(), "test");
        assertEquals(cmd.param(0), "a");
        assertEquals(cmd.param(1), "b");
        
        cmd = cmd.getNext();
        assertNotNull(cmd);
        assertTrue(cmd.isActionCommand());
        
        cmd = builder.build("; test ab");
        assertNotNull(cmd);
        assertTrue(cmd.isActionCommand());
        
        cmd = cmd.getNext();
        assertNotNull(cmd);
        assertEquals(cmd.getName(), "test");
        assertEquals("ab", cmd.param(0));
        
        
        cmd = builder.build("test a b; debug c d");
        
        assertEquals(cmd.length(), 2);
        assertEquals(cmd.getName(), "test");
        assertEquals(cmd.param(0), "a");
        assertEquals(cmd.param(1), "b");
        
        cmd = cmd.getNext();
        assertNotNull(cmd);
        assertEquals(cmd.length(), 2);
        assertEquals(cmd.getName(), "debug");
        assertEquals(cmd.param(0), "c");
        assertEquals(cmd.param(1), "d");
    }
    
    @Test()
    public void nextFail() {
        Command cmd = builder.build("test a b");
        assertNull(cmd.getNext());
    }
    
    @Test
    public void advanced() {
        String input = 
            "test \"spaced argument\", b;debug,a''";
        
        Command cmd = builder.build(input);
        
        assertEquals(1, cmd.length());
        assertEquals("test", cmd.getName());
        assertEquals("spaced argument", cmd.param(0));
        
        cmd = cmd.getNext();
        assertNotNull(cmd);
        assertEquals(1, cmd.length());
        assertEquals("test", cmd.getName());
        assertEquals("b", cmd.param(0));
        
        cmd = cmd.getNext();
        assertNotNull(cmd);
        assertEquals(0, cmd.length());
        assertEquals("debug", cmd.getName());
    
        cmd = cmd.getNext();
        assertNotNull(cmd);
        assertEquals(2, cmd.length());
        assertEquals("debug", cmd.getName());
        assertEquals("a", cmd.param(0));
        assertEquals("", cmd.param(1));
    }

    @Test
    public void arrayAdvanced() {
        String[] input = {
            "test", "spaced argument", ",", "b", ";","debug", ",", "a", ""
        };
        
        Command cmd = builder.build(input);
        
        assertEquals(1, cmd.length());
        assertEquals("test", cmd.getName());
        assertEquals("spaced argument", cmd.param(0));
        
        cmd = cmd.getNext();
        assertNotNull(cmd);
        assertEquals(1, cmd.length());
        assertEquals("test", cmd.getName());
        assertEquals("b", cmd.param(0));
        
        cmd = cmd.getNext();
        assertNotNull(cmd);
        assertEquals(0, cmd.length());
        assertEquals("debug", cmd.getName());
    
        cmd = cmd.getNext();
        assertNotNull(cmd);
        assertEquals(2, cmd.length());
        assertEquals("debug", cmd.getName());
        assertEquals("a", cmd.param(0));
        assertEquals("", cmd.param(1));
    }
}
