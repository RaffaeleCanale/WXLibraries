package com.wx.properties.structures.view;

import java.util.Map;

/**
 * Created by canale on 03.05.16.
 */
public interface StructureView extends Map<String, String> {

    String getHeader();

    void setHeader(String value);

    void removeHeader();
}
