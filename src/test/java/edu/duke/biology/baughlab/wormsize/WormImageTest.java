/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.duke.biology.baughlab.wormsize;

import edu.duke.biology.baughlab.wormsize.xml.ExperimentOutputType;
import fiji.threshold.Auto_Threshold;
import ij.ImagePlus;
import ij.plugin.filter.BackgroundSubtracter;
import junit.framework.TestCase;

/**
 *
 * @author bradleymoore
 */
public class WormImageTest extends TestCase {
    
    public WormImageTest(String testName) {
        super(testName);
    }
    
    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }
    
    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    /**
     * Test of process method, of class WormImage.
     */
    public void testProcess_8args() {
        System.out.println("process");
        ImagePlus image = ij.IJ.openImage("./test-resources/test3.tif");
        double micronsPerPixel = 1.0;
        double ballSize = 75.0;
        //int thresholdMethod = 0;
        double closeRadius = 5.0;
        double minArea = 0.0;
        double maxArea = Double.MAX_VALUE;
        double minSkeletonScore = 0.0;
        WormImage instance = new WormImage();
        instance.process(image, micronsPerPixel, ballSize, "Minimum", closeRadius, minArea, maxArea, minSkeletonScore, "happy.tif");
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of process method, of class WormImage.
     */
    public void testProcess_5args() {
        System.out.println("process");
        double micronsPerPixel = 0.980819532;
        ImagePlus segmented = ij.IJ.openImage("./test-resources/tmp4-img1.tif");
        double minArea = 0.0;
        double maxArea = Double.MAX_VALUE;
        double minSkeletonScore = 0.0;
        WormImage instance = new WormImage();
        //instance.process(micronsPerPixel, segmented, minArea, maxArea, minSkeletonScore, "happy.tif");
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    public void testGetThresholdedImage() {
        String thresholdMethod = "Minimum";
        double ballSize = 75.0;
        double closeRadius = 5.0;
        
        ImagePlus image = ij.IJ.openImage("./test-resources/tmp4-img.tif");
        ImagePlus image2 = ij.IJ.openImage("./test-resources/tmp4-img.tif");
        ij.IJ.save(image, "./test-resources/tmp/mytmp1.tif");
        WormImage wi = new WormImage();
        ImagePlus img2 = wi.getThresholdedImage(image2, ballSize, thresholdMethod, closeRadius);
       
        BackgroundSubtracter bs = new BackgroundSubtracter();
        bs.rollingBallBackground(image.getProcessor(), ballSize, false, true, true, true, true);
       
        int ret = new edu.duke.biology.baughlab.wormsize.AutoThresholdWrapper(false, false, true, AutoThresholdWrapper.MINIMUM).threshold(image);
        ImagePlus thresh = AutoThresholdWrapper.getThresholdedImage(image, ret, true);
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
        
        ij.IJ.save(img2, "./test-resources/tmp/mytmp2.tif");
        ij.IJ.save(thresh, "./test-resources/tmp/mytmp3.tif");
        //img
    }
    

    /**
     * Test of pointsToString method, of class WormImage.
     */
    public void testPointsToString() {
        System.out.println("pointsToString");
        int[] xpts = null;
        int[] ypts = null;
        WormImage instance = new WormImage();
        String expResult = "";
        String result = instance.pointsToString(xpts, ypts);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
}
