package com.wx.util.representables.string;

import com.wx.util.representables.GenericCasterTest;
import com.wx.util.representables.TypeCaster;
import org.junit.Test;

import static com.wx.util.representables.string.EnumCasterTest.SomeEnum.*;

public class EnumCasterTest extends GenericCasterTest<String, EnumCasterTest.SomeEnum> {

    enum SomeEnum {
        A, B, C, D, d
    }

    @Test(expected = NullPointerException.class)
    public void nullConstructorTest() {
        new EnumCaster<>((Class<SomeEnum>) null);
    }

    @Override
    protected TypeCaster<String, SomeEnum> getCaster() {
        return new EnumCaster<>(SomeEnum.class);
    }

    @Override
    protected void setUpPairs() {
        pair("A", A);
        pair("B", B);
        pair("C", C);
        pair("D", D);
        pair("d", d);

        invalidInput("a");
        invalidInput("foo");
    }

}