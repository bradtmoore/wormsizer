/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.duke.biology.baughlab.wormsize;

import ij.ImagePlus;
import java.util.ArrayList;
import junit.framework.TestCase;
import edu.duke.biology.testutil.TestUtil;
import ij.IJ;
import ij.gui.*;
import ij.process.ImageStatistics;
import java.awt.Rectangle;
import ij.process.ImageConverter;
import java.awt.Color;
import ij.gui.PolygonRoi;
/**
 *
 * @author bradleymoore
 */
public class PolygonComponentFinderTest extends TestCase {
    public final static String THRESH = "./test-resources/thresh-out2.tif";
    public final static String SQUARE = "./test-resources/white-square.tif";
    public final static String OUT_SQUARE = "./test-resources/tmp/padded-white.tif";
    public final static String WORMS = "./test-resources/worms_4count_86x.tif";
    public final static String POLY_OUT = "./test-resources/tmp/poly-out.tif";

    public PolygonComponentFinderTest(String testName) {
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
     * Test of find method, of class PolygonComponentFinder.
     */
    public void testFind() {
        System.out.println("find");
        ImagePlus ip = IJ.openImage(THRESH);
        ImagePlus ip2 = IJ.openImage(WORMS);
        ImageConverter ic = new ImageConverter(ip2);
        ic.convertToRGB();

        ip.getProcessor().invert();
        PolygonComponentFinder instance = new PolygonComponentFinder(1, 999999);
        instance.find(ip);

        for (PolygonRoi pr : instance.getPolygons())
        {
            Rectangle r = pr.getBounds();
            ip2.setColor(Color.red);
            pr.drawPixels(ip2.getProcessor());

            int[] xs = {(int)r.getX(), (int)(r.getX() + r.getWidth()), (int)(r.getX() + r.getWidth()), (int)r.getX()};
            int[] ys = {(int)r.getY(), (int)r.getY(), (int)(r.getY() + r.getHeight()), (int)(r.getY() + r.getHeight())};

            PolygonRoi pr2 = new PolygonRoi(xs, ys, xs.length, Roi.POLYGON);
            ip2.setColor(Color.blue);
            pr2.drawPixels(ip2.getProcessor());
            System.out.println(r);
        }

        IJ.save(ip2, POLY_OUT);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    public void testGetBufferedImage()
    {
        ImagePlus ip = IJ.openImage(SQUARE);
        PolygonComponentFinder pcf = new PolygonComponentFinder(1, 99999);
        ImagePlus ip2 = pcf.getPaddedImage(ip, 3);
        IJ.save(ip2, OUT_SQUARE);
    }

    /**
     * Test of getPolygons method, of class PolygonComponentFinder.
     */
    public void testGetPolygons() {
        System.out.println("getPolygons");
        PolygonComponentFinder instance = null;
        ArrayList expResult = null;
        ArrayList result = instance.getPolygons();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

}
