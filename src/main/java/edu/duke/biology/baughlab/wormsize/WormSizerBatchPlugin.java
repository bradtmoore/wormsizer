/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.duke.biology.baughlab.wormsize;
import java.io.*;
import edu.duke.biology.baughlab.wormsize.xml.*;
import java.io.PrintWriter;
import java.io.StringWriter;
import ij.gui.GenericDialog;
import ij.ImagePlus;
import ij.process.*;

/**
 * This is a batch version of WormSizer for use with CellProfiler or within an ImageJ macro.
 * 
 * It takes as input the parameters provided, as well as a black-white image.
 * 
 * @author bradleymoore
 */
public class WormSizerBatchPlugin implements ij.plugin.PlugIn {

    /**
     * 
     * 
     * @param arg comma-delimited string
     *  xmlFile
     *  csvFile
     *  micronsPerPixel
     *  minArea
     *  maxArea
     *  minScore
     *  imageFile
     *  experimentId
     */
    @Override
    public void run(String arg) {
        try {
            //UNGH, arg doesn't work the way I thought...
            //String[] args = arg.split(",");
            GenericDialog gd = new GenericDialog("WormSizer Batch");
            gd.addStringField("xmlFile", "");
            gd.addStringField("csvFile", "");
            gd.addNumericField("micronsPerPixel", 1.0, 8);
            gd.addNumericField("minArea", 0.0, 8);
            gd.addNumericField("maxArea", 99999999, 8);
            gd.addNumericField("minScore", 0.0, 8);
            gd.addStringField("imageFile", "");
            gd.addStringField("experimentId", "");
            gd.showDialog();
            
            File xmlFile = new File(gd.getNextString());
            File csvFile = new File(gd.getNextString());
            double micronsPerPixel = gd.getNextNumber();
            double minArea = gd.getNextNumber();
            double maxArea = gd.getNextNumber();
            double minScore = gd.getNextNumber();
            String imageFile = gd.getNextString();
            String experimentId = gd.getNextString();
            
//            File xmlFile = new File(args[0]);
//            File csvFile = new File(args[1]);
//            double micronsPerPixel = Double.parseDouble(args[2]);
//            double minArea = Double.parseDouble(args[3]);
//            double maxArea = Double.parseDouble(args[4]);
//            double minScore = Double.parseDouble(args[5]);
//            String imageFile = args[6];
//            String experimentId = args[7];

            WormImage wi = new WormImage();
            ImagePlus imp = new ImagePlus("",ij.IJ.getImage().getProcessor().convertToByte(false));
            wi.process(micronsPerPixel, imp, minArea, maxArea, minScore, imageFile);

            // if the xml file exists, open it otherwise create a new one
            ExperimentOutputType eot = null;
            if (xmlFile.exists()) {
                eot = BindingFactory.unmarshal(xmlFile, new ObjectFactory().createExperimentOutputType());
            }
            else {
                eot = new ObjectFactory().createExperimentOutputType();
                eot.setExperimentId(experimentId);
                
                WormOutputsType wots = new WormOutputsType();                                                
                eot.setWormOutputs(wots);
            }
            
            // add our measured worms to the experiment
            eot.getWormOutputs().getWormOutput().add(wi.getWormOutput());
            BindingFactory.marshal(xmlFile, eot, new ObjectFactory().createExperimentOutput(eot));
            ExperimentOutputCSV.writeToCSV(eot, null, csvFile);
            
        }
        catch (Exception e) {
            // log the entire stacktrace to ImageJ's log window
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            e.printStackTrace(pw);
            ij.IJ.log(sw.toString());
            
            String uh = System.getProperty("user.home");
            File log = new File(uh + File.separator + "wormsizer-error.txt");
            BufferedWriter bw = null;
            try {
                

                try {
                bw = new BufferedWriter(new FileWriter(log));
                bw.write(sw.toString());
                bw.write(String.format("arg:", arg));
                }
                finally {
                    bw.close();
                }
            }
            catch (Exception e2) {
                // not much to do at this point
            }
        }       
    }
    
}
