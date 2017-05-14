package com.wx.util;

/**
 * @author Raffaele Canale (<a href="mailto:raffaelecanale@gmail.com?subject=WXLibraries">raffaelecanale@gmail.com</a>)
 * @version 0.1 - created on 25.06.16.
 */
public class OsUtils {

    /**
     * Enumeration of supported operating systems
     */
    public enum OsFamily {
        UNIX,
        WINDOWS,
        MAC_OS,
        OTHER
    }

    private static OsFamily os;

    private static void checkOs() {
        String osName = System.getProperty("os.name").toLowerCase();

        if (osName.contains("win")) {
            os = OsFamily.WINDOWS;
        } else if (osName.contains("nix") || osName.contains("nux") || osName.indexOf("aix") > 0) {
            os = OsFamily.UNIX;
        } else if (osName.contains("mac")){
            os = OsFamily.MAC_OS;
        } else {
            os = OsFamily.OTHER;
        }
    }

    /**
     * Get the current operating system.
     *
     * @return The current operating system
     */
    public static OsFamily getOsFamily() {
        if (os == null) {
            checkOs();
        }

        return os;
    }

}
