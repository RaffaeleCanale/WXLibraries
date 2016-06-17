/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wx.io.datafile.register.varlen;

import com.wx.io.Accessor;
import com.wx.io.datafile.register.RegisterColType;
import com.wx.io.datafile.register.RegisterColumn;
import com.wx.io.datafile.register.RegisterSaveStrategy;
import com.wx.io.datafile.register.RegisterSaveStrategyTest;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import org.junit.Test;

/**
 *
 * @author Raffaele Canale - raffaelecanale@gmail.com
 */
public class DynamicLengthRegisterTest extends RegisterSaveStrategyTest {
    
    
    @Test(expected = IOException.class)
    public void invalidLengthTest() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Accessor accessor = new Accessor().setOut(baos);
        
        accessor.writeInt(-22);
        accessor.writeInt(12);
        RegisterSaveStrategy strategy = createStrategy(
                new RegisterColumn[]{new RegisterColumn("test", RegisterColType.id)});
        accessor.close();
        accessor.setIn(new ByteArrayInputStream(baos.toByteArray()));
        strategy.read(accessor);
    }

    @Override
    protected RegisterSaveStrategy createStrategy(RegisterColumn[] columns) {
        return new DynamicLengthRegister(columns);
    }
    
}