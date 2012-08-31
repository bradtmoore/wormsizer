/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.duke.biology.baughlab.wormsize;

import ij.ImagePlus;
import ij.process.ImageProcessor;
import ij.plugin.filter.PlugInFilter;
import edu.duke.biology.baughlab.wormsize.gui.IntroFrame;
import java.io.*;


/**
 * This class is executes the WormSizer GUI.
 * 
 * @author bradleymoore
 */
public class WormSizerGUIPlugin implements ij.plugin.filter.PlugInFilter {

    @Override
    public int setup(String arg, ImagePlus imp) {
        return PlugInFilter.NO_IMAGE_REQUIRED;
    }

    @Override
    public void run(ImageProcessor ip) {
        //final ClassLoader cl = Thread.currentThread().getContextClassLoader();
        java.awt.EventQueue.invokeLater(new java.lang.Runnable() {

            @Override
            public void run() {
                //Thread.currentThread().setContextClassLoader(ClassLoader.getSystemClassLoader());
                Thread.setDefaultUncaughtExceptionHandler(new java.lang.Thread.UncaughtExceptionHandler() {

                    @Override
                    public void uncaughtException(Thread t, Throwable e) {
                        StringWriter sw = new StringWriter();
                        PrintWriter pw = new PrintWriter(sw);
                        e.printStackTrace(pw);
                        ij.IJ.log(sw.toString());
                        //ij.IJ.log(e.getLocalizedMessage());
                    }
                });
                //ij.IJ.log("WormSizerGUIPlugin.run()");
                new IntroFrame().setVisible(true);
            }
        });
    }
        
}
