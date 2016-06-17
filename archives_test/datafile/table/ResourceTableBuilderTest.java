package com.wx.io.datafile.table;

import com.wx.io.datafile.BufferedRegisterIterator;
import com.wx.io.datafile.DataAccessException;
import com.wx.io.datafile.StrategyHelper;
import com.wx.io.datafile.register.RegisterColType;
import com.wx.io.datafile.register.RegisterColumn;
import com.wx.io.datafile.table.id.IncrementalIdGenerator;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

/**
 *
 * @author Canale
 */
public class ResourceTableBuilderTest extends Assert {

    @Rule
    public TemporaryFolder tmpFolder = new TemporaryFolder();
    private final String tableName = "test_table";
    private final RegisterColumn[] columns = {
        new RegisterColumn("col1", RegisterColType.id),
        new RegisterColumn("col2", RegisterColType.integer),
        new RegisterColumn("col3", RegisterColType.real),
        new RegisterColumn("col4", RegisterColType.string, 64)
    };
    private final Object[] testRegister = {null, 22, 22.4f, "test"};

    @BeforeClass
    public static void init() {
        StrategyHelper.setFileStrategy(StrategyHelper.DataFileStrategies.single_page, "${param:0}_page.dll");
        StrategyHelper.setIdGeneratorStrategy(StrategyHelper.IdGeneratorStrategies.incremental);
        StrategyHelper.setPageStrategy(StrategyHelper.DataPageStrategies.cached_only, 2048);
        StrategyHelper.setRegisterStrategy(StrategyHelper.RegisterStrategies.fixed_length);
    }

    @Test
    public void createAndLoadTest() throws IOException, DataAccessException {
        File dataFileDirectory = createTemporaryDir();
        
        createAndSave(dataFileDirectory);
        loadAndCheck(dataFileDirectory);
    }
    
    @Test(expected = FileNotFoundException.class)
    public void unexistingTest() throws IOException {
        File dataFileDirectory = createTemporaryDir();
        
        ResourceTableBuilder builder = new ResourceTableBuilder(tableName, dataFileDirectory);
        builder.loadTable();
    }
    
    private void createAndSave(File dataFileDirectory) throws IOException, DataAccessException {        
        ResourceTableBuilder builder = new ResourceTableBuilder(tableName, dataFileDirectory);
        Table table = builder.createTable(columns);
        table.addRegister(testRegister);
        table.flushDataFile();       
    }
    
    private void loadAndCheck(File dataFileDirectory) throws IOException {
        ResourceTableBuilder builder = new ResourceTableBuilder(tableName, dataFileDirectory);
        Table table = builder.loadTable();

        assertArrayEquals(columns, table.getColumns());
        assertEquals(IncrementalIdGenerator.class, table.idGenerator.getClass());
        
        Object[] idTestRegister = {1, testRegister[1], testRegister[2], 
            testRegister[3]};
        BufferedRegisterIterator buffer = table.getRegisters(-1);
        assertArrayEquals(idTestRegister, buffer.readRegister());
        assertFalse(buffer.hasNext());
    }



    private File createTemporaryDir() throws IOException {
        return tmpFolder.newFolder();
    }
}
