/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.wxbeta.database;

import java.sql.SQLException;

/**
 *
 * @author Raffaele Canale - raffaelecanale@gmail.com
 * @version 0.1
 *
 * Project: WXLibraries
 * File: DerbyDatabase.java (UTF-8)
 * Date: 28 janv. 2013 
 */
public class DerbyDatabase extends Database {
    
    private static final String PATH_KEY = "derby.system.home";
    private static final String DRIVER_NAME = 
                                           "org.apache.derby.jdbc.ClientDriver";
    private static final String SUB_PROTOCOL = "derby";
    private static final String LOCALHOST_PREFIX = "//localhost:";

    public DerbyDatabase() throws ClassNotFoundException, 
            IllegalAccessException, IllegalAccessException, 
            InstantiationException {
        super(DRIVER_NAME);
    }

    
    public void connect(String subName, String userName, String password,
                boolean create) throws SQLException, 
            InstantiationException, IllegalAccessException, 
            ClassNotFoundException {
        super.connect(SUB_PROTOCOL, subName, userName, password, create);
    }

    public void connectLocal(int port, String subName, String userName,
            String password, boolean create) throws SQLException, 
            InstantiationException, IllegalAccessException, 
            ClassNotFoundException {
        subName = LOCALHOST_PREFIX + port + "/" + subName;
        super.connect(SUB_PROTOCOL, subName, userName, password, create);
    }
    
    @Override
    protected String getPathPropertyKey() {
        return PATH_KEY;
    }

}
