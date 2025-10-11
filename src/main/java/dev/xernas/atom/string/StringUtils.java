package dev.xernas.atom.string;

public class StringUtils {

    public static boolean beginsWith(final String str, final String prefix) {
        return str != null && prefix != null && str.startsWith(prefix);
    }

}
