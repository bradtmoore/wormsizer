/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.duke.biology.baughlab.wormsize;
import edu.duke.biology.baughlab.wormsize.gui.ProgressFrame;
import edu.duke.biology.baughlab.wormsize.BindingFactory;
import org.codehaus.plexus.util.PathTool;
import java.io.*;
import edu.duke.biology.baughlab.wormsize.xml.*;
import edu.duke.biology.baughlab.wormsize.ImageFileFilter;
import java.util.ArrayList;
import ij.ImagePlus;
import java.util.List;
import javax.swing.JLabel;

/**
 * A java SwingWorker is the correct way for running a separate thread that "may" update
 * the GUI thread with information about processing.
 * 
 * @author bradleymoore
 */
public class WormSizerWorker extends javax.swing.SwingWorker<ArrayList<ExperimentOutputType>, int[]> {
    protected ArrayList<File> inputDirectories;
    protected File outputDirectory;
    protected double micronsPerPixel;
    protected double rollingBallRadius;
    protected String thresholdMethod;
    protected double closeRadius;
    protected double minWormArea;
    protected double maxWormArea;
    protected double minTubenessScore;
    protected ProgressFrame progressFrame;

    public WormSizerWorker(ArrayList<File> inputDirectories, File outputDirectory, double micronsPerPixel, double rollingBallRadius, String thresholdMethod, double closeRadius, double minWormArea, double maxWormArea, double minTubenessScore) {
        this.inputDirectories = inputDirectories;
        this.outputDirectory = outputDirectory;
        this.micronsPerPixel = micronsPerPixel;
        this.rollingBallRadius = rollingBallRadius;
        this.thresholdMethod = thresholdMethod;
        this.closeRadius = closeRadius;
        this.minWormArea = minWormArea;
        this.maxWormArea = maxWormArea;
        this.minTubenessScore = minTubenessScore;
    }

    public void setProgressFrame(ProgressFrame progressFrame) {
        this.progressFrame = progressFrame;
    }
    
   
    @Override
    protected ArrayList<ExperimentOutputType> doInBackground() throws Exception {
        ArrayList<ExperimentOutputType> ans = new ArrayList<ExperimentOutputType>();
        for (int i = 0; i < inputDirectories.size(); i++) {
            File dir = inputDirectories.get(i);
            ExperimentOutputType eot = new ExperimentOutputType();
            eot.setExperimentId(dir.getName());
            ExperimentSettingsType est = new ExperimentSettingsType();
            est.setMicronsPerPixel(micronsPerPixel);
            est.setRollingBallRadius(rollingBallRadius);
            est.setThresholdMethod(thresholdMethod);
            est.setCloseRadius(closeRadius);
            est.setMinWormArea(minWormArea);
            est.setMaxWormArea(maxWormArea);
            est.setMinTubeness(minTubenessScore);
            eot.setExperimentSettings(est);
            eot.setWormOutputs(new WormOutputsType());
           
            File[] files = dir.listFiles(new ImageFileFilter());
            for (int j = 0; j < files.length; j++) {
                if (! this.isCancelled()) {
                    this.publish(new int[]{i+1, inputDirectories.size(), j+1, files.length});
                    File img = files[j];
                    WormImage wi = new WormImage();
                    ImagePlus ip = ij.IJ.openImage(img.getAbsolutePath());  
                    String imgPath = PathTool.getRelativeFilePath(outputDirectory.getAbsolutePath(), img.getAbsolutePath());
                    wi.process(ip, micronsPerPixel, rollingBallRadius, thresholdMethod, closeRadius, minWormArea, maxWormArea, minTubenessScore, imgPath);
                    ip.close();
                    eot.getWormOutputs().getWormOutput().add(wi.getWormOutput());                
                }
                else {
                    break;
                }
            }
            
            if (! this.isCancelled()) {
                File f = new File(outputDirectory.getAbsolutePath() + File.separator + eot.getExperimentId() + ".xml");
                BindingFactory.marshal(f, eot, new ObjectFactory().createExperimentOutput(eot));
                ExperimentOutputCSV.writeToCSV(eot, outputDirectory);
            }
            else {
                break;
            }
        }
        
        //System.out.println("SHOULD BE DONE");
        return ans;
    }

    @Override
    protected void process(List<int[]> list) {
        super.process(list);
        
        int[] cur = list.get(list.size()-1);
        progressFrame.setProgress(cur[0], cur[1], cur[2], cur[3]);
    }
}
