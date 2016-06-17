/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.wx.io.table.search;

/**
 *
 * @author Raffaele Canale - raffaelecanale@gmail.com
 * @version 0.1
 *
 * Project: WXLibraries.ANT
 * File: IdMatch.java (UTF-8)
 * Date: Oct 16, 2013 
 */
public class IdMatch extends FieldMatch {

    public IdMatch(int id) {
        super(id, 0);
    }

    public IdMatch(Object[] register) {
        super(register[0], 0);
    }
    
}
