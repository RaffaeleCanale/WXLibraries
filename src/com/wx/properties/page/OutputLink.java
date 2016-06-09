package com.wx.properties.page;

import java.io.IOException;
import java.io.OutputStream;

/**
 * @author canale
 */
@FunctionalInterface
public interface OutputLink {
    OutputStream getOutputStream() throws IOException;
}
