package com.wx.util.representables.string;

import com.wx.util.representables.GenericCasterTest;
import com.wx.util.representables.TypeCaster;
import org.junit.Test;

import static com.wx.util.representables.string.EnumCasterLCTest.SomeEnum.*;

/**
 * Created by canale on 12.05.16.
 */
public class EnumCasterLCTest extends GenericCasterTest<String, EnumCasterLCTest.SomeEnum> {

    enum SomeEnum {
        A, B, C, D, e
    }

    enum InvalidEnum {
        A, a
    }

    @Test(expected = IllegalArgumentException.class)
    public void invalidEnumTest() {
        new EnumCasterLC<>(InvalidEnum.class);
    }

    @Test(expected = NullPointerException.class)
    public void nullConstructorTest() {
        new EnumCasterLC<>((Class<SomeEnum>) null);
    }

    @Override
    protected TypeCaster<String, SomeEnum> getCaster() {
        return new EnumCasterLC<>(SomeEnum.class);
    }

    @Override
    protected void setUpPairs() {
        pair("a", A);
        pair("b", B);
        pair("c", C);
        pair("d", D);
        pair("e", e);

        outPair("A", A);

        invalidInput("");
        invalidInput("foo");
    }

}