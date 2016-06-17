/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.wx.servercomm;

/**
 *
 * @author Raffaele Canale - raffaelecanale@gmail.com
 * @version 0.1
 *
 * Project: Worx
 * File: ServerCommCallBack.java (UTF-8)
 * Date: May 12, 2013 
 */
public interface ServerCommCallBack<Result> {

    void taskSucceeded(Result result);
    
    void taskFailed(Exception ex);
    
}
