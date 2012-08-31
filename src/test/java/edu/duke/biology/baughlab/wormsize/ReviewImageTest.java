/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.duke.biology.baughlab.wormsize;

import ij.gui.PointRoi;
import ij.gui.PolygonRoi;
import java.util.ArrayList;
import junit.framework.TestCase;
import edu.duke.biology.baughlab.wormsize.xml.*;

/**
 *
 * @author bradleymoore
 */
public class ReviewImageTest extends TestCase {
    
    public ReviewImageTest(String testName) {
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
     * Test of show method, of class ReviewImage.
     */
    public void testShow() {
        System.out.println("show");
        ij.ImagePlus ip = ij.IJ.openImage("./test-resources/test3.tif");
        ij.ImagePlus ip2 = ij.IJ.openImage("./test-resources/test3.tif");
        WormImage wi = new WormImage();
        ExperimentOutputType eot = new ExperimentOutputType();
        wi.process(ip, 1, 75, "Minimum", 5, 0, Double.MAX_VALUE, 0, "happy.tif");
        
        
        ReviewImage instance = new ReviewImage(wi.getWormOutput(), ip2);
        instance.show();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getWormCount method, of class ReviewImage.
     */
    public void testGetWormCount() {
        System.out.println("getWormCount");
        ReviewImage instance = null;
        int expResult = 0;
        int result = instance.getWormCount();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of renderNext method, of class ReviewImage.
     */
    public void testRenderNext() {
        System.out.println("renderNext");
        ReviewImage instance = null;
        instance.renderNext();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of renderPrev method, of class ReviewImage.
     */
    public void testRenderPrev() {
        System.out.println("renderPrev");
        ReviewImage instance = null;
        instance.renderPrev();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of setRenderStyle method, of class ReviewImage.
     */
    public void testSetRenderStyle() {
        System.out.println("setRenderStyle");
        int style = 0;
        ReviewImage instance = null;
        instance.setRenderStyle(style);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of setCurPass method, of class ReviewImage.
     */
    public void testSetCurPass() {
        System.out.println("setCurPass");
        boolean pass = false;
        ReviewImage instance = null;
        instance.setCurPass(pass);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of render method, of class ReviewImage.
     */
    public void testRender() {
        System.out.println("render");
        int pos = 0;
        ReviewImage instance = null;
        instance.render(pos);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getContour method, of class ReviewImage.
     */
    public void testGetContour() {
        System.out.println("getContour");
        String pts = "";
        ReviewImage instance = null;
        PolygonRoi expResult = null;
        PolygonRoi result = instance.getContour(pts);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getSkeleton method, of class ReviewImage.
     */
    public void testGetSkeleton() {
        System.out.println("getSkeleton");
        String pts = "";
        ReviewImage instance = null;
        PointRoi expResult = null;
        ij.gui.PolygonRoi result = instance.getSkeleton(pts);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getSampleWidths method, of class ReviewImage.
     */
    public void testGetSampleWidths() {
        System.out.println("getSampleWidths");
        String pts = "";
        ReviewImage instance = null;
        ArrayList expResult = null;
        ArrayList result = instance.getSampleWidths(pts);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
}
