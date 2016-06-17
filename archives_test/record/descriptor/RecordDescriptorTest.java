/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.wx.io.datamanager.record.descriptor;

import com.wx.io.datamanager.record.RecordObject;
import java.util.List;
import org.junit.Assert;
import org.junit.Test;

/**
 *
 * @author Canale
 */
public class RecordDescriptorTest extends Assert {
    
    private final RecordDescriptor desc1list2 = new RecordDescriptor(1,
            RecordField.simpleField("subField0", RecordFieldType.string),
            RecordField.simpleField("subField1", RecordFieldType.string),
            RecordField.simpleField("subField2", RecordFieldType.string),
            RecordField.simpleField("subField4", RecordFieldType.string)
    );
    private final RecordDescriptor desc1 = new RecordDescriptor(2, 
            RecordField.simpleField("field0", RecordFieldType.string),
            RecordField.simpleField("field1", RecordFieldType.string),
            RecordField.simpleList("field2", desc1list2),
            RecordField.simpleField("field3", RecordFieldType.string)
    );
    private final RecordDescriptor desc2 = new RecordDescriptor(0, 
            RecordField.simpleField("othField0", RecordFieldType.string),
            RecordField.simpleField("othField1", RecordFieldType.string)
    );
    
    private final RecordDescriptor desc12 = new RecordDescriptor(2, 
            RecordField.simpleField("field0", RecordFieldType.string),
            RecordField.simpleField("field1", RecordFieldType.string),
            RecordField.simpleList("field2", desc1list2),
            RecordField.simpleField("field3", RecordFieldType.string),
            RecordField.simpleField("othField0", RecordFieldType.string),
            RecordField.simpleField("othField1", RecordFieldType.string)
    ); 
    
    private final RecordDescriptor desc1flatten = new RecordDescriptor(2, 
            RecordField.simpleField("field0", RecordFieldType.string),
            RecordField.simpleField("field1", RecordFieldType.string),
            RecordField.simpleField("field2", RecordFieldType.integer),
            RecordField.simpleField("field2.subField0", RecordFieldType.string),
            RecordField.simpleField("field2.subField1", RecordFieldType.string),
            RecordField.simpleField("field2.subField2", RecordFieldType.string),
            RecordField.simpleField("field2.subField4", RecordFieldType.string),
            RecordField.simpleField("field3", RecordFieldType.string)
    );

    @Test
    public void testJoin() {
        RecordDescriptor join = RecordDescriptor.join(desc1, desc2);
        assertEquals(desc12, join);
    }
    
    @Test
    public void testGetField() {
        RecordField field = desc1.getField("field2");
        assertEquals(desc1.getField(2), field);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testGetBadField() {
        desc1.getField("Unexisting field");
    }
    
    @Test
    public void testEmptyRecord() {
        RecordObject rec = desc1.createEmptyRecord();
        assertEquals(desc1.count(), rec.getCount());
        
        for (int i = 0; i < desc1.count(); i++) {
            if (desc1.getField(i).isListType()) {
                assertNotNull(rec.getElement(i));
                assertTrue(rec.getElement(i) instanceof List);
            } else {
                assertNull(rec.getElement(i));
            }
        }
    }
    
    @Test
    public void testFlatten() {
        RecordDescriptor flatten = desc1.flatten(2);
        assertEquals(desc1flatten, flatten);
    }
    
    @Test(expected = AssertionError.class)
    public void testBadFlatten() {
        desc1.flatten(0);
    }
}
