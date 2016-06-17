/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wx.io.datafile.page;

import com.wx.io.Accessor;
import com.wx.io.CounterOutputStream;
import com.wx.io.datafile.register.RegisterColType;
import com.wx.io.datafile.register.RegisterColumn;
import com.wx.io.datafile.register.RegisterSaveStrategy;
import com.wx.io.datafile.register.fixlen.FixedLengthRegister;
import com.wx.io.datafile.register.varlen.DynamicLengthRegister;
import com.wx.io.table.search.AlwaysTrueCondition;
import com.wx.io.table.search.FieldMatch;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import org.junit.Assert;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNull;
import org.junit.Ignore;
import org.junit.Test;

/**
 *
 * @author Raffaele Canale - raffaelecanale@gmail.com
 */
@Ignore
public abstract class DataPageTest extends Assert {

    private final RegisterColumn[] columns = {
        new RegisterColumn("col1", RegisterColType.id),
        new RegisterColumn("col2", RegisterColType.integer),
        new RegisterColumn("col3", RegisterColType.real),
        new RegisterColumn("col4", RegisterColType.string, 64)
    };
    private final RegisterSaveStrategy[] saveStrategies = getStrategies(columns);
    
    
    @Test
    public void addTest() throws IOException {
        for (RegisterSaveStrategy strategy : saveStrategies) {
            addTest(new TestContext(strategy, true));
        }
    }

    private void addTest(TestContext ctx) throws IOException {        
        ctx.page.close();

        ctx.reloadPage();
        RegisterSetIterator iterator = ctx.page.getIterator(new AlwaysTrueCondition());        
        assertArrayEquals(ctx.registers[0], iterator.readRegister());
        assertArrayEquals(ctx.registers[1], iterator.readRegister());
        assertArrayEquals(ctx.registers[2], iterator.readRegister());
        assertArrayEquals(ctx.registers[3], iterator.readRegister());
        assertNull(iterator.readRegister());
        iterator.close();
    }

    //*
    @Test
    public void readConditionTest() throws IOException {
        for (RegisterSaveStrategy strategy : saveStrategies) {
            readConditionTest(new TestContext(strategy, true));
        }
    }
    
    private void readConditionTest(TestContext ctx) throws IOException {
        final int registerIndex = 2;
        final int colIndex = 2;
        
        FieldMatch cond = new FieldMatch(ctx.registers[registerIndex][colIndex], colIndex);
        RegisterSetIterator iterator = ctx.page.getIterator(cond);        
        assertArrayEquals(ctx.registers[registerIndex], iterator.readRegister());
        assertNull(iterator.readRegister());
        iterator.close();
    }

    @Test
    public void setRegisterTest() throws IOException {
        for (RegisterSaveStrategy strategy : saveStrategies) {
            setRegisterTest(new TestContext(strategy, true));
        }
    }
    
    private void setRegisterTest(TestContext ctx) throws IOException {
        final int registerIndex = 1;
        final int colIndex = 0;
        
        Object[] newRegister = {5, 27, 50.5f, "testtttt"};
        FieldMatch cond = new FieldMatch(ctx.registers[registerIndex][colIndex], colIndex);
        RegisterSetIterator iterator = ctx.page.getIterator(cond);
        
        assertArrayEquals(ctx.registers[registerIndex], iterator.readRegister());
        iterator.update(newRegister); // Update
        assertNull(iterator.readRegister());
        iterator.close();
        
        iterator = ctx.page.getIterator(new AlwaysTrueCondition());
        assertArrayEquals(ctx.registers[0], iterator.readRegister());
        assertArrayEquals(newRegister, iterator.readRegister());
        assertArrayEquals(ctx.registers[2], iterator.readRegister());
        assertArrayEquals(ctx.registers[3], iterator.readRegister());
        assertNull(iterator.readRegister());
        iterator.close();
    }
    
    @Test
    public void testRemove() throws IOException {
        for (RegisterSaveStrategy strategy : saveStrategies) {
            testRemove(new TestContext(strategy, true));
        }
    }
    
    private void testRemove(TestContext ctx) throws IOException {
        final int registerIndex = 2;
        final int colIndex = 0;
        
        FieldMatch cond = new FieldMatch(ctx.registers[registerIndex][colIndex], colIndex);
        RegisterSetIterator iterator = ctx.page.getIterator(cond);
        assertArrayEquals(ctx.registers[registerIndex], iterator.readRegister());
        iterator.remove();
        assertNull(iterator.readRegister());
        iterator.close();
        
        iterator = ctx.page.getIterator(new AlwaysTrueCondition());
        assertArrayEquals(ctx.registers[0], iterator.readRegister());
        assertArrayEquals(ctx.registers[1], iterator.readRegister());
        assertArrayEquals(ctx.registers[3], iterator.readRegister());
        assertNull(iterator.readRegister());
        iterator.close();
    }

    @Test
    public void overFlowTest() throws IOException {
        for (RegisterSaveStrategy strategy : saveStrategies) {
            overFlowTest(new TestContext(strategy, false));
        }
    }
    
    private void overFlowTest(TestContext ctx) throws IOException {
        Object[] simpleRegister = ctx.registers[0];
        
        CounterOutputStream cos = new CounterOutputStream(new ByteArrayOutputStream());
        try (Accessor accessor = new Accessor().setOut(cos)) {
            ctx.rs.write(simpleRegister, accessor);
        }
        final int n = (int) Math.floor(getPageSize() / cos.getCount());
        
        for (int i = 0; i < n; i++) {
            assertTrue(ctx.page.add(simpleRegister));
        }
        
        assertFalse(ctx.page.add(simpleRegister));
    }//*/
    
    private File createTemporaryFile() throws IOException {
        File file = File.createTempFile("temp", ".tmp");
        file.deleteOnExit();

        return file;
    }

    protected RegisterSaveStrategy[] getStrategies(RegisterColumn[] columns) {
        return new RegisterSaveStrategy[]{
            new FixedLengthRegister(columns),
            new DynamicLengthRegister(columns)
        };
    }

    protected abstract DataPageFactory createFactory(
            RegisterSaveStrategy strategy);

    protected int getPageSize() {
        return 1 << 16;
    }


//    private TestContext[] ctxs;

    private class TestContext {

        private final File tmpFile;
        private final RegisterSaveStrategy rs;
        private final DataPageFactory factory;
        private DataPage page;
        private final Object[][] registers = {
            new Object[]{1, 23, 50.1f, "test"},
            new Object[]{2, 24, 50.2f, "testt"},
            new Object[]{3, 25, 50.3f, "testtt"},
            new Object[]{4, 26, 50.4f, "testttt"}
        };

        private TestContext(RegisterSaveStrategy rs, boolean fill) throws IOException {
            this.rs = rs;
            this.tmpFile = createTemporaryFile();
            this.factory = createFactory(rs);
            reloadPage();
            if (fill) {
                fillPage();
            }
//            page.flush();
        }

        private void reloadPage() throws IOException {
            page = factory.getNewPage(tmpFile);
        }

        private void fillPage() throws IOException {
            for (Object[] register : registers) {
                page.add(register);
            }
        }

    }

}