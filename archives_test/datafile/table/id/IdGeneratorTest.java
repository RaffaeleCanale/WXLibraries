/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wx.io.datafile.table.id;

import com.wx.io.datafile.DataAccessException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

/**
 *
 * @author Raffaele Canale - raffaelecanale@gmail.com
 */
@Ignore
public abstract class IdGeneratorTest extends Assert {
    
    private static final int TEST_COUNT = 2000;
    
    private final IdGeneratorInterface generator = getGenerator();    
    

    @Test
    public void test() throws DataAccessException {
        Set<Integer> blackList = new HashSet<>();
        blackList.addAll(Arrays.asList(23, 87, 1234, 33, 10));
        
        for (Integer id : blackList) {
            generator.registerId(id);
        }
        
        int iterations = TEST_COUNT - blackList.size();
        assertTrue(iterations > 0);
        Set<Integer> ids = new HashSet<>();
        
        for (int i = 0; i < iterations; i++) {
            int id = generator.generateNewId();
            assertTrue(id >= 0);
            assertFalse(blackList.contains(id));
            assertFalse(ids.contains(id));
            
            ids.add(id);
        }
        
    }

    
    protected abstract IdGeneratorInterface getGenerator();    
}