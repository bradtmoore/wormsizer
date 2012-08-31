/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.duke.biology.baughlab.wormsize;
import java.util.*;

/**
 * You have a fixed set of micrometer measurements.  You want to interpolate between them.
 * 
 * @author bradleymoore
 */
public class MicrometerElement {
    protected ArrayList<double[]> samples;


    /**
     * Consumes samples, i.e. sorts it and relies on it not changing.
     *
     * @param samples
     */
    public MicrometerElement(ArrayList<double[]> samples) {
        this.samples = samples;
        Collections.sort(samples, new FirstElemComparator());
    }

    public double getResolution(double zoom) {
        double ans = 0.0;

        int pos = Collections.binarySearch(samples, new double[]{zoom,0.0}, new FirstElemComparator());
        if (pos >= 0) {
            ans = samples.get(pos)[1];
        }
        else {
            int idx = -(pos+1);
            idx = (idx == samples.size()) ? idx-1 : idx;
            double[] dd = samples.get(idx);
            ans = (dd[0]/zoom)*dd[1];
        }

        return ans;
    }

    public static class FirstElemComparator implements java.util.Comparator<double[]> {

        @Override
        public int compare(double[] o1, double[] o2) {
            int ans = 0;
            if (o1[0] < o2[0]) {
                ans = -1;
            }
            else if (o1[0] > o2[0]) {
                ans = 1;
            }
            return ans;
        }

    }
}
