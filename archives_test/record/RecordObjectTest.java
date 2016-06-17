/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.wx.io.datamanager.record;

import java.util.Arrays;
import java.util.List;
import org.junit.Assert;
import org.junit.Test;

/**
 *
 * @author Canale
 */
public class RecordObjectTest extends Assert {
    
    private final List<RecordObject> testSubList = Arrays.asList(
            new RecordObject(50, "11", "12", "13"),
            new RecordObject(51, "21", "22", "23"),
            new RecordObject(52, "31", "32", "33")
    );
    private final RecordObject testRecord = new RecordObject(23, 
            "0",
            "1",
            testSubList,
            "3"
    );
    
    private final List<RecordObject> testFlatten = Arrays.asList(
            new RecordObject(23, "0", "1", 50, "11", "12", "13", "3"),
            new RecordObject(23, "0", "1", 51, "21", "22", "23", "3"),
            new RecordObject(23, "0", "1", 52, "31", "32", "33", "3")
    );
    
    
    public RecordObjectTest() {
    }

    @Test
    public void testGetList() {
        assertEquals(testSubList, testRecord.getList(2));
    }
    
    @Test(expected = ClassCastException.class)
    public void testBadGetList1() {
        testRecord.getList(0);
    }
        
    @Test(expected = ClassCastException.class)
    public void testBadFlatten() {
        testRecord.flatten(0);
    }
    
    @Test
    public void testFlatten() {
        List<RecordObject> rec = testRecord.flatten(2);
        assertEquals(testFlatten.size(), rec.size());
        
        for (int i = 0; i < testFlatten.size(); i++) {
            assertEquals(testFlatten.get(i), rec.get(i));
        }
    }
    
}
