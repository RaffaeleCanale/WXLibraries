/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wx.io.datafile.page.impl;

import com.wx.io.datafile.page.DataPageFactory;
import com.wx.io.datafile.page.DataPageTest;
import com.wx.io.datafile.register.RegisterSaveStrategy;

/**
 *
 * @author Raffaele Canale - raffaelecanale@gmail.com
 */
public class CachedOnlyPageTest extends DataPageTest {
    
    @Override
    protected DataPageFactory createFactory(RegisterSaveStrategy strategy) {
        return CachedOnlyPage.getFactory(strategy, getPageSize());
    }

}