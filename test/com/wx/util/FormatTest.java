package com.wx.util;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

/**
 * Created by canale on 10.05.16.
 */
public class FormatTest {

    private static Map<Long, String> sizeConversions() {
        Map<Long, String> expected = new HashMap<>();
        expected.put(0L, "0 B");
        expected.put(100L, "100 B");
        expected.put(1023L, "1023 B");

        expected.put(1024L, "1 KiB");
        expected.put(1024000L, "1000 KiB");
        expected.put(1047552L, "1023 KiB");

        expected.put(1048576L, "1 MiB");
        expected.put(536870912L, "512 MiB");
        expected.put(1072798106L, "1023.1 MiB");
        expected.put(1072850535L, "1023.15 MiB");
        expected.put(1073731339L, "1023.99 MiB");
        expected.put(1073727144L, "1023.99 MiB");
        expected.put(1073735533L, "1023.99 MiB");

        expected.put(1073741824L, "1 GiB");
        expected.put(2199023255552L, "2 TiB");
        expected.put(1124800395214848L, "1023 TiB");
        return expected;
    }

    // SIZE
    @Test(expected = IllegalArgumentException.class)
    public void testInvalidSize() {
        Format.formatSize(-1);
    }

    @Test
    public void testSize() {
        Map<Long, String> expected = sizeConversions();

        expected.forEach((size, text) -> assertEquals(text, Format.formatSize(size)));
    }



    // DATE
    @Test
    public void testSpeed() {
        Map<Long, String> expected = sizeConversions();

        expected.forEach((size, text) -> assertEquals(text + "/s", Format.formatSpeed(size)));

    }

}