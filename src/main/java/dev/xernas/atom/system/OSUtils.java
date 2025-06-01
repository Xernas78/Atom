package dev.xernas.atom.system;

public class OSUtils {

    public static OS getOS() {
        String os = System.getProperty("os.name").toLowerCase();
        if (os.contains("win")) {
            return OS.WIN;
        } else if (os.contains("mac")) {
            return OS.MAC;
        } else if (os.contains("nix") || os.contains("nux") || os.contains("aix")) {
            return OS.LINUX;
        } else if (os.contains("sunos")) {
            return OS.SOLARIS;
        } else {
            return OS.UNKNOWN;
        }
    }

}
