/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.wxbeta.database;

import java.io.Closeable;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashSet;
import java.util.Set;


/**
 *
 * @author Raffaele Canale - raffaelecanale@gmail.com
 * @version 0.1
 *
 * Project: WXLibraries
 * File: Database.java (UTF-8)
 * Date: 31 déc. 2012 
 */
public abstract class Database implements SqlConstants, Closeable {

    // derby.system.home
    private Connection mConnection;

    public Database(String driverName) throws ClassNotFoundException, 
            IllegalAccessException, IllegalAccessException, 
            InstantiationException {
        Class.forName(driverName).newInstance();
    }
        
    public void connect(String subProtocol, String subName, String userName,
            String password, boolean create) throws SQLException, 
            InstantiationException, IllegalAccessException, 
            ClassNotFoundException {
        //Class.forName().newInstance();
        String url = "jdbc:" + subProtocol + ":" + subName + ";create=" 
                + create;
        mConnection = DriverManager.getConnection(url, userName, password);
    }
    
    public Set<String> getTables() throws SQLException {
        ResultSet tables = mConnection.getMetaData().getTables(null, null, null,
                new String[]{"TABLE"});
        Set<String> result = new HashSet<>();
        while (tables.next()) {
            result.add(tables.getString(GET_TABLES_KEY));
        }
              
        return result;
    }
    
    public void setPathProperty(String path) {
        System.setProperty(getPathPropertyKey(), path);
    }
    
    protected abstract String getPathPropertyKey();
    
    public ResultSet querySelect(String sql) throws SQLException {
        Statement statement = mConnection.createStatement();
        statement.closeOnCompletion();
        return statement.executeQuery(sql);
    }
    
    public int queryUpdate(String sql) throws SQLException {
        try (Statement statement = mConnection.createStatement()) {
            return statement.executeUpdate(sql);
        }
    }
    /*
    public static void main(String[] args) throws SQLException, InstantiationException, InstantiationException, IllegalAccessException, ClassNotFoundException {
        //Test t = new Test("org.apache.derby.jdbc.EmbeddedDriver");
        Test t = new Test("org.apache.derby.jdbc.ClientDriver");
        //t.setPathProperty("C:/Users/Raffaele/Documents/Temporary/Desktop/dbtest2");
        //t.connect("derby", "test", "me", "pass", true);
        t.connect("derby", "//localhost:1527/test", "me", "pass", true);
        System.out.println(t.getTables());
    }//*/
    
    public boolean isConnected() throws SQLException {
        return mConnection != null && !mConnection.isClosed();
    }

    @Override
    public void close() throws IOException {
        try {
            mConnection.close();
        } catch (SQLException ex) {
            throw new IOException(ex);
        }
    }
}