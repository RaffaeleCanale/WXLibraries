package com.wx.properties.page;

import java.io.IOException;
import java.io.InputStream;

/**
 *
 * @author canale
 */
@FunctionalInterface
public interface InputLink {
    InputStream getInputStream() throws IOException;
}
