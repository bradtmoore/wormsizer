/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.duke.biology.baughlab.wormsize;

import java.util.*;
import java.io.*;
import ij.measure.Calibration;

/**
 * Gets the per pixel resolution (microns) per pixel
 *
 * @author bradleymoore
 */
public class Micrometer {

    protected HashMap<String, MicrometerElement> map;

    public Micrometer() {
        map = new HashMap<String, MicrometerElement>();

    }

    /**
     * 
     * @param f
     * @return true if micrometer file parsed successfully.  false otherwise.
     */
    public boolean open(File f) {
        BufferedReader br = null;
        boolean ans = true;
        try {
            br = new BufferedReader(new FileReader(f));
            parseCsv(br);
        } catch (Throwable th) {
            ans = false;
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    
                }
            }
        }

        return ans;
    }

    public Calibration getResolution(String objective, double zoom) {
        Calibration ans = new Calibration();
        ans.setUnit("microns");
        ans.pixelWidth = map.get(objective).getResolution(zoom);
        ans.pixelHeight = ans.pixelWidth;
        return ans;
    }

    public Set getObjectives() {
        return map.keySet();
    }

    final protected void parseCsv(BufferedReader br) throws IOException {

        HashMap<String, ArrayList<double[]>> hm = new HashMap<String, ArrayList<double[]>>();

        String s = null;
        while ((s = br.readLine()) != null) {
            String[] ss2 = s.split(",");

            String obj = ss2[0];
            double zoom = Double.parseDouble(ss2[1]);
            double resolution = Double.parseDouble(ss2[2]);

            if (!hm.containsKey(obj)) {
                hm.put(obj, new ArrayList<double[]>());
            }
            hm.get(obj).add(new double[]{zoom, resolution});
        }


        for (Map.Entry<String, ArrayList<double[]>> ent : hm.entrySet()) {
            map.put(ent.getKey(), new MicrometerElement(ent.getValue()));
        }
    }

    protected static class MicrometerException extends RuntimeException {

        public MicrometerException(String msg, Throwable th) {
            super(msg, th);
        }
    }
}
