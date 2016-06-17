/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wx.io.datafile.page.impl;

import com.wx.io.datafile.page.DataPageFactory;
import com.wx.io.datafile.page.DataPageTest;
import com.wx.io.datafile.register.RegisterColumn;
import com.wx.io.datafile.register.RegisterSaveStrategy;
import com.wx.io.datafile.register.fixlen.FixedLengthRegister;

/**
 *
 * @author Raffaele Canale - raffaelecanale@gmail.com
 */
public class BitMappedPageTest extends DataPageTest {
    
    private int registerLength;
    @Override
    protected DataPageFactory createFactory(RegisterSaveStrategy strategy) {
        int registersCapacity = (int) Math.floor(getPageSize() / registerLength);
        return BitMappedPage.getFactory(strategy, registerLength, registersCapacity);
    }

    @Override
    protected RegisterSaveStrategy[] getStrategies(RegisterColumn[] columns) {
        FixedLengthRegister strategy = new FixedLengthRegister(columns);
        registerLength = strategy.getRegisterSize();
        return new RegisterSaveStrategy[]{strategy};
    }
    
    
    
    
    
}