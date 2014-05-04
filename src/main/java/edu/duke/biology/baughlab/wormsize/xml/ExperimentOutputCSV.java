/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.duke.biology.baughlab.wormsize.xml;
import java.io.*; 
import au.com.bytecode.opencsv.*;
/**
 * Write the ExperimentOutputType object to CSV files.
 * 
 * @author bradleymoore
 */
public class ExperimentOutputCSV {
    public static String[] SETTINGS_HEADER = {"experimentId", "micronsPerPixel", "rollingBallRadius", "thresholdMethod", "closeRadius", "minWormArea", "maxWormArea", "minTubeness"};
    public static String[] RESULTS_HEADER = {"experimentId", "imageFile", "id", "pass", "volume", "length", "middleWidth", "meanWidth", "surfaceArea"};
    public static String SETTINGS_SUFFIX = "-settings.csv";
    public static String RESULTS_SUFFIX = "-results.csv";
    
    public static void writeToCSV(ExperimentOutputType eot, File outDir) throws IOException {
        File f1 = new File(outDir.getAbsolutePath() + File.separator + eot.getExperimentId() + SETTINGS_SUFFIX);
        File f2 = new File(outDir.getAbsolutePath() + File.separator + eot.getExperimentId() + RESULTS_SUFFIX);
        ExperimentOutputCSV.writeToCSV(eot, f1, f2);
    }
    
    /**
     * 
     * @param eot
     * @param outDir
     * @throws IOException 
     */
    public static void writeToCSV(ExperimentOutputType eot, File settingsFile, File resultsFile) throws IOException {
        
        if (settingsFile != null) {
            CSVWriter cw = null;
            try {

                cw = new CSVWriter(new FileWriter(settingsFile), ',');
                cw.writeNext(SETTINGS_HEADER);
                cw.writeNext(settingsToStringArray(eot));            
            }
            finally {
                if (cw != null) {
                    cw.close();
                }
            }
        }
        
        if (resultsFile != null) {
            CSVWriter cw = null;
            try {

                cw = new CSVWriter(new FileWriter(resultsFile), ',');
                cw.writeNext(RESULTS_HEADER);
                for (WormOutputType wot : eot.getWormOutputs().getWormOutput()) {
                    for (WormType wt : wot.getWorms().getWorm()) {
                        cw.writeNext(wormOutputToStringArray(eot.getExperimentId(), wot.getImageFile(), wt));
                    }
                }
            }
            finally {
                if (cw != null) {
                    cw.close();
                }
            }
        }
    }
    
    protected static String[] settingsToStringArray(ExperimentOutputType eot) {
        ExperimentSettingsType est = eot.getExperimentSettings();
        return new String[]{eot.getExperimentId(), Double.toString(est.micronsPerPixel), Double.toString(est.getRollingBallRadius()), est.getThresholdMethod(), Double.toString(est.getCloseRadius()), Double.toString(est.getMinWormArea()), Double.toString(est.getMaxWormArea()), Double.toString(est.getMinTubeness())};
    }
    
    protected static String[] wormOutputToStringArray(String experimentId, String imageFile, WormType wt) {
        String pass = (wt.isPass() == null)? "NA" : Boolean.toString(wt.isPass());
        return new String[] {experimentId, imageFile, wt.getId().toString(), pass, Double.toString(wt.getVolume()), Double.toString(wt.getLength()), Double.toString(wt.getMiddleWidth()), Double.toString(wt.getMeanWidth()), Double.toString(wt.getSurfaceArea())};        
    }
}
