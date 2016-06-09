package com.wx.properties;

import com.wx.properties.structures.view.AbstractStructureView;

import java.util.HashMap;

/**
 * Created by canale on 02.05.16.
 */
public class StructureInterface extends AbstractStructureView {

    public StructureInterface() {
        super("__HEADER__", new HashMap<>());
    }

    @Override
    protected boolean isKeyInView(String realKey) {
        return !"__HEADER__".equals(realKey);
    }

    @Override
    protected String realKeyToView(String realKey) {
        testNotHeader(realKey);
        return realKey;
    }

    @Override
    protected String viewKeyToReal(String viewKey) {
        testNotHeader(viewKey);
        return viewKey;
    }

    private void testNotHeader(String key) {
        if ("__HEADER__".equals(key)) {
            throw new RuntimeException();
        }
    }
}
