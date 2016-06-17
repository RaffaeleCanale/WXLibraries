///*
// * To change this template, choose Tools | Templates
// * and open the template in the editor.
// */
//
//package com.wx.servercomm;
//
//import com.sun.javafx.fxml.builder.URLBuilder;
//
//import java.io.IOException;
//import java.io.UnsupportedEncodingException;
//import java.net.URLEncoder;
//
///**
// *
// * @author Raffaele Canale - raffaelecanale@gmail.com
// * @version 0.1
// *
// * Project: Worx
// * File: AbstractServerComm.java (UTF-8)
// * Date: May 12, 2013
// */
//public abstract class AbstractServerComm<Result> extends Thread {
//
//    private ServerCommCallBack<Result> callBack;
//
//    @Override
//    public void run() {
//        assert callBack != null;
//        try {
//            Result result = execute();
//            callBack.taskSucceeded(result);
//        } catch (IOException ex) {
//            callBack.taskFailed(ex);
//        }
//    }
//
//    @Override
//    public void start() {
//        throw new UnsupportedOperationException("Use executeInBackground");
//    }
//
//    public abstract Result execute() throws IOException;
//
//    public void executeInBackground(ServerCommCallBack<Result> callBack) {
//        this.callBack = callBack;
//        super.start();
//    }
//
//    protected void addParameter(StringBuilder url, String parameterName,
//            String parameterValue) throws UnsupportedEncodingException {
//
//        addParameter("&", url, parameterName, parameterValue);
//    }
//
//    protected void addParameter(String separator, StringBuilder url,
//            String parameterName, String parameterValue)
//            throws UnsupportedEncodingException {
//        url.append(separator).append(parameterName).append("=").
//                append(URLEncoder.encode(parameterValue, "UTF-8"));
//    }
//}
