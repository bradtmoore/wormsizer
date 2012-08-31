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
public class FilenamePrefixFilter implements java.io.FilenameFilter {
    protected String[] pres;
    
    public FilenamePrefixFilter(String pre)
    {
        this(new String[]{pre});
    }
    
    public FilenamePrefixFilter(String[] pres)
    {
        this.pres = pres;
    }

    @Override
    public boolean accept(File dir, String name) {
        boolean ans = false;
        for (String pre : pres)
        {
            if (name.toLowerCase().endsWith(pre)) {
                ans = true;
                break;
            }
        }

        return ans;
    }
}
