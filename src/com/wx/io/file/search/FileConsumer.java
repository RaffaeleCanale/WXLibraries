package com.wx.io.file.search;

import java.io.File;

/**
 * Created on 18/03/2015
 *
 * @author Raffaele Canale (raffaelecanale@gmail.com)
 * @version 0.1
 */
@FunctionalInterface
public interface FileConsumer {

    public void found(File file);

}
