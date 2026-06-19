package dev.xernas.atom.input;

import dev.xernas.atom.system.OS;
import dev.xernas.atom.system.OSUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public final class Console {

    private static final BufferedReader READER = new BufferedReader(new InputStreamReader(System.in));

    public static void print(Object obj) {
        System.out.print(obj);
    }

    public static void println(Object obj) {
        System.out.println(obj);
    }

    public static void println() {
        System.out.println();
    }

    public static void error(Object obj) {
        System.err.println(obj);
    }

    public static String input(String prompt) {
        print(prompt);

        try {
            return READER.readLine();
        } catch (IOException e) {
            throw new RuntimeException("Failed to read input", e);
        }
    }

    public static void pause() {
        input("Press ENTER to continue...");
    }

    public static int inputInt(String prompt) {
        while (true) {
            try {
                return Integer.parseInt(input(prompt));
            } catch (NumberFormatException e) {
                error("Please enter a valid integer.");
            }
        }
    }

    public static long inputLong(String prompt) {
        while (true) {
            try {
                return Long.parseLong(input(prompt));
            } catch (NumberFormatException e) {
                error("Please enter a valid long.");
            }
        }
    }

    public static double inputDouble(String prompt) {
        while (true) {
            try {
                return Double.parseDouble(input(prompt));
            } catch (NumberFormatException e) {
                error("Please enter a valid number.");
            }
        }
    }

    public static boolean inputBoolean(String prompt) {
        while (true) {
            String value = input(prompt + " (y/n): ")
                    .trim()
                    .toLowerCase();

            switch (value) {
                case "y":
                case "yes":
                case "true":
                case "1":
                    return true;

                case "n":
                case "no":
                case "false":
                case "0":
                    return false;

                default:
                    error("Please enter y or n.");
            }
        }
    }

    public static void clear() {
        try {
            if (OSUtils.getOS() == OS.WIN) {
                new ProcessBuilder("cmd", "/c", "cls")
                        .inheritIO()
                        .start()
                        .waitFor();
            } else {
                System.out.print("\033[H\033[2J");
                System.out.flush();
            }
        } catch (Exception ignored) {
        }
    }

    public static void separator() {
        println("--------------------------------------------------");
    }

    public static void title(String title) {
        separator();
        println(title);
        separator();
    }
}
