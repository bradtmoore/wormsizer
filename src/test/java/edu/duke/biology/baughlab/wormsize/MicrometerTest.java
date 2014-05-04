/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.duke.biology.baughlab.wormsize;

import java.io.InputStream;
import junit.framework.TestCase;
import ij.measure.*;

/**
 *
 * @author bradleymoore
 */
public class MicrometerTest extends TestCase {
    
    public MicrometerTest(String testName) {
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
     * Test of getResolution method, of class Micrometer.
     */
    public void testGetResolution() {
        System.out.println("getResolution");
        String objective = "1";
        double zoom = 86.0;
        Micrometer instance = new Micrometer();
        try {
            instance.open(new java.io.File("./test-resources/micrometer.csv"));
        } 
        catch (Exception e)
        {
            fail(e.getMessage());
        }
        double expResult = 0.0;
        Calibration result = instance.getResolution(objective, zoom);
        System.out.println("1,86=" + result);

        //assertEquals(expResult, result, 0.0);
        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
    }

}
