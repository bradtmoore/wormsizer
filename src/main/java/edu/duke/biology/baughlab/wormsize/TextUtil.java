/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.duke.biology.baughlab.wormsize;

/**
 *
 * @author bradleymoore
 */
public class TextUtil {
    public static String toCsv(Object[] arr, String format) {
        String ans = "";

        if (arr.length > 0) {
            ans += String.format(format, arr[0]);
        }

        String format2 = "," + format;
        for (int i = 1; i < arr.length; i++) {
            ans += String.format(format2, arr[i]);
        }
        ans += "\n";

        return ans;
    }
}
