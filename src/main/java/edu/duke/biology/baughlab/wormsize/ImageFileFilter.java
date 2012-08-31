/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.duke.biology.baughlab.wormsize;

import java.io.File;

/**
 * Doesn't seem to be a good way to get the accepted file types out of ImageJ.  So, for now,
 * manually listing them.
 * 
 * @author bradleymoore
 */
public class ImageFileFilter implements java.io.FileFilter {
    public static final String[] ALLOWED_TYPES = {"tif", "tiff", "jpg", "jpeg", "bmp", "png"};

    @Override
    public boolean accept(File file) {
        boolean pass = false;
        String name = file.getName().toLowerCase();
        for (String s : ALLOWED_TYPES) {
            if (name.endsWith("." + s)) {
                pass = true;
                break;
            }
        }
        
        return pass;
    } 
}
