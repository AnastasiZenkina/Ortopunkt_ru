package com.ortopunkt.logging;

public class LoggingUtils {

    public static final String RESET = "\033[0m";
    public static final String RED = "\033[31m";
    public static final String YELLOW = "\033[33m";
    public static final String GREEN = "\033[32m";

    public static String error(String message) {
        return RED + message + RESET;
    }

    public static String warn(String message) {
        return YELLOW + message + RESET;
    }

    public static String info(String message) {
        return GREEN + message + RESET;
    }
}
