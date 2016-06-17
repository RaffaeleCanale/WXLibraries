/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.wx.io.datafile.table.id;

import com.wx.resource.property.SimpleProperty;

/**
 *
 * @author Raffaele Canale - raffaelecanale@gmail.com
 * @version 0.1
 *
 * Project: WXLibraries.ANT
 * File: IncrementalIdGeneratorTest.java (UTF-8)
 * Date: Oct 16, 2013 
 */
public class IncrementalIdGeneratorTest extends IdGeneratorTest {

    @Override
    protected IdGeneratorInterface getGenerator() {
        return new IncrementalIdGenerator(new SimpleProperty<Integer>(), new SimpleProperty<Integer>());        
    }

}
