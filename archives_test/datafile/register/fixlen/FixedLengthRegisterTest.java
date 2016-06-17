/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wx.io.datafile.register.fixlen;

import com.wx.io.Accessor;
import com.wx.io.CounterOutputStream;
import com.wx.io.datafile.register.RegisterColType;
import com.wx.io.datafile.register.RegisterColumn;
import com.wx.io.datafile.register.RegisterSaveStrategy;
import com.wx.io.datafile.register.RegisterSaveStrategyTest;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import org.junit.Test;

/**
 *
 * @author Raffaele Canale - raffaelecanale@gmail.com
 */
public class FixedLengthRegisterTest extends RegisterSaveStrategyTest {
    
    
    @Test(expected = AssertionError.class)
    public void noLengthTest() throws IOException {
        RegisterColumn[] cols = {
            new RegisterColumn("test", RegisterColType.id),
            new RegisterColumn("test2", RegisterColType.string)
        };
        
        RegisterSaveStrategy strategy = createStrategy(cols);
        Object[] values = {22, "value"};
        strategy.write(values, new Accessor().setOut(new ByteArrayOutputStream()));
    }

    @Test
    public void caluclatedLengthTest() throws IOException {
        RegisterColumn[] cols = {
            new RegisterColumn("test", RegisterColType.id),
            new RegisterColumn("test2", RegisterColType.string, 256),
            new RegisterColumn("test3", RegisterColType.integer),
            new RegisterColumn("test4", RegisterColType.real)
        };
        FixedLengthRegister strategy = createStrategy(cols);        
        Object[] values = {22, "value", 21, 9f};
        
        CounterOutputStream cos = new CounterOutputStream(new ByteArrayOutputStream());
        try (Accessor accessor = new Accessor().setOut(cos)) {
            strategy.write(values, accessor);
        }
        
        assertEquals(cos.getCount(), strategy.getRegisterSize());
    }
    
    @Override
    protected FixedLengthRegister createStrategy(RegisterColumn[] columns) {
        return new FixedLengthRegister(columns);
    }
    
}