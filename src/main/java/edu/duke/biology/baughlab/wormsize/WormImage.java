/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.duke.biology.baughlab.wormsize;
import edu.duke.biology.baughlab.wormsize.xml.*;
import ij.ImagePlus;
import ij.plugin.filter.BackgroundSubtracter;
import java.util.ArrayList;
import fiji.threshold.Auto_Threshold;
import ij.process.BinaryProcessor;
import ij.process.ByteProcessor;
import ij.process.ImageProcessor;

/**
 * This is the workhorse class.  This takes either a raw image or a previously segmented image, parameters, and produces the identified worm objects.
 * @author bradleymoore
 */
public class WormImage {
    protected ArrayList<Worm> worms;
    protected WormOutputType wormOutput;
    
    public static final String[] METHODS={"Default", "Huang", "Intermodes", "IsoData",  "Li", "MaxEntropy","Mean", "MinError(I)", "Minimum", "Moments", "Otsu", "Percentile", "RenyiEntropy", "Shanbhag" , "Triangle", "Yen"};
    
    /**
     * Compute worms from image (image must be grayscale byte)
     * @param image
     * @param micronsPerPixel
     * @param ballSize
     * @param thresholdMethod
     * @param closeRadius
     * @param minArea
     * @param maxArea
     * @param minSkeletonScore 
     */
    public void process(ImagePlus image, double micronsPerPixel, double ballSize, String thresholdMethod, double closeRadius, double minArea, double maxArea, double minSkeletonScore, String imageFile, int sampleInterval) 
    {
        ImagePlus thresh = getThresholdedImage(image, ballSize, thresholdMethod, closeRadius);
        process(micronsPerPixel, thresh, minArea, maxArea, minSkeletonScore, imageFile, sampleInterval);
        thresh.close();
    }
    
    /**
     * This is slightly different than the results returned by the AutoThresholdWrapper, as they 
     * @param image
     * @param ballSize
     * @param thresholdMethod
     * @param closeRadius
     * @return 
     */
    protected ImagePlus getThresholdedImage(ImagePlus image, double ballSize, String thresholdMethod, double closeRadius) {
        BackgroundSubtracter bs = new BackgroundSubtracter();  
        bs.rollingBallBackground(image.getProcessor(), ballSize, false, true, true, true, true);
       
        Auto_Threshold at = new Auto_Threshold();
        /// ungggh, exec() method will change the ImagePlus passed to it.
        ImagePlus tmp = new ImagePlus("", image.getProcessor().duplicate().convertToByte(false)); //image.duplicate();, CellProfiler version doesn't support his mehtod
        Object[] res = at.exec(tmp, thresholdMethod, false, false, true, false, false, false);
        tmp.close();
        
        //ImagePlus thresh = (ImagePlus)res[1];
        ImagePlus thresh = AutoThresholdWrapper.getThresholdedImage(image, (Integer)res[0], true);
        
        //ImagePlus thresh = getThresholdedImage(image, (Integer)res[0]+1, true);
        //thresh.getProcessor().invert();

        // close the image, there are usually a bright edge in the middle of the worm
        for (int i = 0; i < closeRadius; i++) {
            thresh.getProcessor().dilate();
        }

        for (int i = 0; i < closeRadius; i++) {
            thresh.getProcessor().erode();
        }
        thresh.getProcessor().invert();
        
        return thresh;
    }
    
    /**
     * Compute worms from a segmented image.
     * 
     * @param micronsPerPixel
     * @param segmented
     * @param minArea
     * @param maxArea
     * @param minSkeletonScore 
     */
    public void process(double micronsPerPixel, ImagePlus segmented, double minArea, double maxArea, double minSkeletonScore, String imageFile, int sampleInterval)
    {
        PolygonComponentFinder pcf = new PolygonComponentFinder(minArea, maxArea);
        pcf.find(segmented);

        //thresh.getProcessor().invert();
        SkeletonizeWrapper sw = new SkeletonizeWrapper();
        sw.skeletonize(segmented);
        sw.closeAll();
 
        // remove low scoring skeletons
        ArrayList<ArrayList<double[]>> skeletons = sw.getSkeletons();
        ArrayList<Double> scores = sw.getScores();
        for (int j = scores.size()-1; j >= 0; j--) {
            if (scores.get(j) < minSkeletonScore) {
                skeletons.remove(j);
            }
        }
        
        worms = Worm.getInstances(sw.getSkeletons(), pcf.getPolygons(), sampleInterval);
        setWormOutput(imageFile, micronsPerPixel);
    }
    
    protected void setWormOutput(String imageFile, double micronsPerPixel) {
        wormOutput = new WormOutputType();
        WormsType wt = new WormsType();
        for (Worm worm : worms) {
            WormType w = new WormType();
            
            int[] xs = worm.getRoi().getXCoordinates();
            int[] ys = worm.getRoi().getYCoordinates();
            xs = xs.clone();
            ys = ys.clone();
            java.awt.Rectangle r = worm.getRoi().getBounds();
            for (int i = 0; i < xs.length; i++) {
                xs[i] += r.x;
                ys[i] += r.y;
            }
            
            w.setContour(pointsToString(xs, ys));
            w.setId(java.math.BigInteger.valueOf(worm.getId()));       
            w.setLength(worm.getLength() * micronsPerPixel);
            
            int n = worm.getWidths().size();
            if (n > 0) {
                w.setMiddleWidth(worm.getWidths().get((int)Math.ceil(n/2.0)) * micronsPerPixel);
            }
            
            double m = 0.0;
            for (int i = 0; i < worm.getWidths().size(); i++) {
                m += worm.getWidths().get(i);
            }
            m /= worm.getWidths().size();
            
            w.setSampledRadii(linesToString(worm.getSampleWidths()));
            w.setSkeleton(pointsToString(worm.getSkeleton()));
            w.setVolume(worm.getVolume() * Math.pow(micronsPerPixel, 3.0) / 1000); // volume in picoliters (not microliters)
            w.setSurfaceArea(worm.getSurfaceArea() * micronsPerPixel * micronsPerPixel); // microns^2
            w.setMeanWidth(m * micronsPerPixel);
            
            wt.getWorm().add(w);
        }
        wormOutput.setWorms(wt);
        wormOutput.setImageFile(imageFile);        
    }
    
    protected String pointsToString(int[] xpts, int[] ypts) {
        String ans = "";
        if (xpts.length > 0) {
            ans += String.format("%d,%d", xpts[0], ypts[0]);
        }
        for (int i = 1; i < xpts.length; i++) {
            ans += String.format(",%d,%d", xpts[i], ypts[i]);
        }
        
        return ans;
    }
    
    protected String pointsToString(ArrayList<double[]> list) {
        String ans = "";
        if (list.size() > 0) {
            double[] d = list.get(0);
            ans += String.format("%f,%f", d[0], d[1]);
        }
        for (int i = 1; i < list.size(); i++) {
            double[] d = list.get(i);
            ans += String.format(",%f,%f", d[0], d[1]);
        }
                
        return ans;
    }
    
    protected String linesToString(ArrayList<double[]> list) {
        String ans = "";
        if (list.size() > 0) {
            double[] d = list.get(0);
            ans += String.format("%f,%f,%f,%f", d[0], d[1], d[2], d[3]);
        }
        for (int i = 1; i < list.size(); i++) {
            double[] d = list.get(i);
            ans += String.format(",%f,%f,%f,%f", d[0], d[1], d[2], d[3]);
        }
                
        return ans;
    }

    public WormOutputType getWormOutput() {
        return wormOutput;
    }
    
    
}
