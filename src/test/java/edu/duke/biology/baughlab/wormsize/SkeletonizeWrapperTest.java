/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.duke.biology.baughlab.wormsize;

import edu.duke.biology.testutil.*;
import ij.IJ;
import ij.ImagePlus;
import ij.gui.Roi;
import java.util.ArrayList;
import junit.framework.TestCase;

/**
 *
 * @author bradleymoore
 */
public class SkeletonizeWrapperTest extends TestCase {
    public static final String THRESHOLD = "./test-resources/thresh-out1.tif";
    public static final String SKELOUT = "./test-resources/tmp/skel-out.tif";
    public static final String WORM1 = "./test-resources/synth-worm.tif";
    public static final String WORM1OUT = "./test-resources/tmp/synth-worm-out.tif";
    public static final String SKELOUT2 = "./test-resources/tmp/skel-out2.tif";

    public SkeletonizeWrapperTest(String testName) {
        super(testName);
        TestUtil.bootstrap();
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
     * Test of skeletonize method, of class SkeletonizeWrapper.
     */
    public void testSkeletonize() {
        System.out.println("skeletonize");
        ImagePlus threshold = IJ.openImage(THRESHOLD);
        threshold.getProcessor().invert();
        ArrayList<Roi> rois = null;
        SkeletonizeWrapper instance = new SkeletonizeWrapper();
        instance.skeletonize(threshold);
        IJ.save(threshold, SKELOUT);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    public void testSkeletonize2()
    {
        ImagePlus t = IJ.openImage(WORM1);
        SkeletonizeWrapper sw = new SkeletonizeWrapper();
        sw.skeletonize(t);
        IJ.save(sw.getLspImage(), WORM1OUT);
        IJ.save(sw.getSkeletonImage(), SKELOUT2);
    }

}
