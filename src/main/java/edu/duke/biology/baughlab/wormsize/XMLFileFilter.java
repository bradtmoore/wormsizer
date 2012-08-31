/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.duke.biology.baughlab.wormsize;

import java.io.File;

/**
 *
 * @author bradleymoore
 */
public class XMLFileFilter extends javax.swing.filechooser.FileFilter {

    @Override
    public boolean accept(File file) {
        return file.isDirectory() || file.getAbsolutePath().toLowerCase().endsWith(".xml");
    }

    @Override
    public String getDescription() {
        return "XML files";
    }
    
    
}
