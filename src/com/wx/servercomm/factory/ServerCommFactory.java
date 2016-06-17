/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wx.servercomm.factory;

import com.wx.servercomm.http.AbstractHttpRequest;

import java.io.IOException;

/**
 *
 * @author Raffaele Canale - raffaelecanale@gmail.com
 * @version 0.1
 *
 * Project: Worx File: ServerCommFactory.java (UTF-8) Date: May 12, 2013
 */
public interface ServerCommFactory {

    AbstractHttpRequest createHttpGet() throws IOException;

    AbstractHttpRequest createHttpPost() throws IOException;

}
