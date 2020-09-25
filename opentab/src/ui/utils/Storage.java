package ui.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.Scanner;

/**
 * @author Derek Li, Rithik Mamidi, Samarth Patel
 * @version 1.0 <br>
 *
 *          A class used to store and access user preferences
 */
public class Storage {
    /*
     * @requires none
     * 
     * @modifies this
     * 
     * @effects none
     * 
     * @returns the setting if it can be read from the specified config file or a
     * default it it does not exist
     */
    public static String readSettingOrDefault(String fileName, String defaultValue) {
        File file;
        String value = defaultValue;

        try {
            file = new File("./settings/" + fileName);
            Scanner sc = new Scanner(file);
            value = sc.nextLine();
            sc.close();

            return value;
        } catch (Exception e) {
            return defaultValue;
        }
    }

    /*
     * @requires none
     * 
     * @modifies filesystem
     * 
     * @effects creates / sets the given value to the specifed config file
     * 
     * @returns none
     */
    public static void setSetting(String fileName, String value) {
        // File file = new File("./settings/" + fileName);
        try (PrintWriter p = new PrintWriter(new FileOutputStream("./settings/" + fileName, false))) {
            p.println(value);
        } catch (Exception e) {
        }
    }
}