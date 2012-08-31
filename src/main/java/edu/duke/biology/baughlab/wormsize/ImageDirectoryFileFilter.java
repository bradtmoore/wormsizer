/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.duke.biology.baughlab.wormsize;

import java.io.File;

/**
 * Filters for directories that contain images.
 * 
 * @author bradleymoore
 */
public class ImageDirectoryFileFilter implements java.io.FileFilter {

    @Override
    public boolean accept(File pathname) {
        return (pathname.isDirectory() && pathname.listFiles(new ImageFileFilter()).length > 0);
    }
    
}
