package org.jotserver.io;

public class TextInput {
    public static boolean getBoolean(String text) {
        if(text == null) {
            return false;
        } else if(text.equalsIgnoreCase("true") || text.equalsIgnoreCase("yes")) {
            return true;
        } else if(text.equalsIgnoreCase("false") || text.equalsIgnoreCase("no")) {
            return false;
        } else {
            try {
                int i = Integer.parseInt(text);
                return i != 0;
            } catch(NumberFormatException e) {
                return false;
            }
        }
    }

    public static int getInt(String text, int defaultValue) {
        if(text == null) {
            return defaultValue;
        } else {
            try {
                return Integer.parseInt(text);
            } catch(NumberFormatException e) {
                return defaultValue;
            }
        }
    }
}
