/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.duke.biology.baughlab.wormsize;

import ij.gui.PolygonRoi;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * This is the main novel computational object.  Given a worm's skeleton and contour
 * this will return length, sampled widths, and volume.  All values are returned in pixels.
 * 
 * @author bradleymoore
 */
public class Worm {
    /**
     * Given a two sets of unpaired skeletons and polygons, return a list of pairable
     * Worms.
     *
     * @param skeletons
     * @param rois
     * @return
     */
    public static ArrayList<Worm> getInstances(ArrayList<ArrayList<double[]>> skeletons, ArrayList<PolygonRoi> rois, int sampleInterval) {
        ArrayList<Worm> ans = new ArrayList<Worm>();

        for (ArrayList<double[]> skeleton : skeletons)
        {
            // the +2 is weird, but the extend end may chop off a pixel
            if (skeleton.size() >= sampleInterval+2) {
                double[] pt2 = skeleton.get((int)(Math.floor(skeleton.size()/2.0)));
                int[] pt = {(int)pt2[0], (int)pt2[1]};
                for (PolygonRoi roi : rois)
                {
                    if (roi.contains(pt[0], pt[1]))
                    {
                        ans.add(new Worm(skeleton, roi, ans.size(), sampleInterval));
                        ans.get(ans.size()-1).compute();
                        break;
                    }
                }
            }
        }

        return ans;
    }

    /**
     * The duplicate of the skeleton passed to the constructor with ends extended to boundary.
     */
    protected ArrayList<double[]> skeleton;

    /**
     * Distances between sample points along the skeleton.
     */
    protected ArrayList<Double> dists; // N-1 distances between sample points

    /**
     * Widths at each sample point.
     */
    protected ArrayList<Double> widths; // width at N sample points

    /**
     * Diagnostic image showing sample points and normal lines.
     */
//    protected ImagePlus diagnostic;

    /**
     * The passed polygon roi representing the object.
     */
    protected PolygonRoi roi;

    /**
     * Interval at which to sample the skeleton points
     */
    protected int sampleInterval;
    
    /**
     * Volume of the object (piecewise sum of frustrums of cones)
     */
    protected double volume;

    /**
     * Surface area of object (piecewise sum of surface area of frustrums)
     */
    protected double surfaceArea;
    
    /**
     * Array of 4-tuples {s.x, s.y, b.x, b.y} where s is the skeleton sample pt
     * and b is the computed orthogonal intersection pt on the boundary.
     */
    protected ArrayList<double[]> sampleWidths;

    /**
     * ID number to draw on diagnostic image.
     */
    protected int id;
    /**
     *
     * @param skeleton Modifies this in constructor
     * @param roi
     */

    /**
     * The worm length in pixel units
     */
    protected double length;

    /**
     * Whether this worm is considered good/correct for analysis.
     */
    protected boolean pass;


    public boolean isPass() {
        return pass;
    }

    public void setPass(boolean pass) {
        this.pass = pass;
    }

    public Worm(ArrayList<double[]> skeleton, PolygonRoi roi, int id, int sampleInterval) {
        this.skeleton = skeleton;
        this.roi = roi;
        this.id = id;
        this.pass = true;
        this.sampleInterval = sampleInterval;
        
        extendEnd(skeleton, true, roi);
        extendEnd(skeleton, false, roi);
    }

    public int getId() {
        return id;
    }



    public void compute()
    {
        sampleWidths = new ArrayList<double[]>();
        dists = new ArrayList<Double>();
        widths = new ArrayList<Double>();

        ArrayList<Integer> idxs = new ArrayList<Integer>();
        for (int i = sampleInterval; i < skeleton.size()-1; i+=sampleInterval) {
            idxs.add(i);
        }
        idxs.add(0, 0);
        idxs.add(skeleton.size()-1);

        widths.add(0.0);
        for (int i = 1; i < idxs.size(); i++) {
            double[] pt1 = skeleton.get(idxs.get(i-1));
            double[] pt2 = skeleton.get(idxs.get(i));
            double d = dist(pt2,pt1);
            dists.add(d);

            // if it is the last point, add 0, otherwise, calculate the orthogonal point
            double w = 0;
            if (i != idxs.size()-1) {
                double[] bpt = findOrthogonalBoundaryPt(skeleton, idxs.get(i), roi, sampleInterval);
                w = 2.0*Math.sqrt(Math.pow(bpt[0]-pt2[0],2) + Math.pow(bpt[1]-pt2[1],2));

                sampleWidths.add(new double[]{pt2[0],pt2[1], bpt[0], bpt[1]});

            }
            
            widths.add(w);
        }

        //System.out.println("last width:" + Arrays.toString(sampleWidths.get(sampleWidths.size()-1)));
        //System.out.println("worm: " + this.id);
        //System.out.println("widths: " + widths);
        //System.out.println("dists:" + dists);
        
        volume = computeVolume();
        surfaceArea = computeSurfaceArea();
        length = 0.0;
        for (int i = 0; i < skeleton.size() - 1; i++) {
            double[] pt1 = skeleton.get(i);
            double[] pt2 = skeleton.get(i+1);
            length += dist(pt1,pt2);
        }
    }

    public ArrayList<double[]> getSampleWidths() {
        return sampleWidths;
    }

    /**
     * Returns length of worm in pixels
     * @return
     */
    public double getLength() {
        return length;
    }

    /**
     * Returns volume of worm in cubic pixels
     * @return
     */
    public double getVolume() {
        return volume;
    }

    /**
     * Returns the surface area of the worm in pixels
     * @return 
     */
    public double getSurfaceArea() {
        return surfaceArea;
    }
    
    public ArrayList<Double> getDists() {
        return dists;
    }

    public ArrayList<Double> getWidths() {
        return widths;
    }

    public PolygonRoi getRoi() {
        return roi;
    }

    public ArrayList<double[]> getSkeleton() {
        return skeleton;
    }

    protected double computeVolume()
    {
        double ans = 0.0;
        
        // piece-wise addition of frustrums of cones
        for (int i = 0; i < dists.size(); i++)
        {
            double r1 = widths.get(i)/2.0;
            double r2 = widths.get(i+1)/2.0;

            // pi*h/3(r1^2 + r1*r2 + r2^2)
            ans+=(Math.PI*dists.get(i)/3.0)*(r1*r1 + r1*r2 + r2*r2);
        }
        
        return ans;
    }
    
    /**
     * Computes the surface area of a worm as the sum of surface areas of frustrum
     * of cones.  Static method since I need to expose it to the script that will
     * add the column to existing data.
     * @param dists in pixels
     * @param widths in pixels
     * @return surface area in pixels
     */
    public static double computeSurfaceArea(ArrayList<Double> dists, ArrayList<Double> widths) {
        double ans = 0.0;
        
        // piece-wise addition of surface areas of frustrums of cones
        // A = pi * (r1 + r2) * sqrt( (r1 - r2)^2 + h^2)
        for (int i = 0; i < dists.size(); i++)
        {
            double r1 = widths.get(i)/2.0;
            double r2 = widths.get(i+1)/2.0;

            ans += Math.PI * (r1 + r2) * Math.sqrt((r1-r2)*(r1-r2) + dists.get(i)*dists.get(i));
        }
        
        return ans;
    }
    
    protected double computeSurfaceArea() {
        return Worm.computeSurfaceArea(dists, widths);
    }

    public static double dist(double[] p1, double[] p2)
    {
        return Math.sqrt(Math.pow(p2[0]-p1[0],2) + Math.pow(p2[1]-p1[1],2));
    }

    /**
     * Given a point on the skeleton, find an orthogonal boundary point
     *
     * @param skeleton
     * @param idx index into skeleton for point to find orthogonal boundary pt for
     * @param roi polygon roi representing the object (used to find inside/outside object)
     * @param sampleInterval points away from idx to calculate tangent
     * @return
     */
    public double[] findOrthogonalBoundaryPt(ArrayList<double[]> skeleton, int idx, PolygonRoi roi, int sampleInterval) {
        double[] ans = null;
        int idx2 = idx-sampleInterval;
        idx2 = (idx2 < 0) ? 0 : idx2;
        int idx3 = idx+sampleInterval;
        idx3 = (idx3 >= skeleton.size()) ? skeleton.size()-1 : idx3;
        
        double[] pt = skeleton.get(idx);
        double[] pt2 = skeleton.get(idx2);
        double[] pt3 = skeleton.get(idx3);
        
        // calculate tangent
        double[] tan = {pt3[0] - pt2[0], pt3[1] - pt2[1]}; // tangent to pt
        double mag = Math.sqrt(tan[0]*tan[0] + tan[1]*tan[1]);
        tan[0] /= mag;
        tan[1] /= mag;
        
        // convert to polar
        double theta = Math.atan2(tan[1], tan[0]);
        
        // rotate by 90 to get normal
        double theta2 = theta + Math.PI/2.0;
        
        // direction to travel until we hit boundary
        double[] dir = {Math.cos(theta2), Math.sin(theta2)};
        ans = new double[]{pt[0], pt[1]};
        while (roi.contains((int)ans[0], (int)ans[1])) {
            ans[0] += dir[0];
            ans[1] += dir[1];
        }
        
        ans[0] = Math.floor(ans[0]);
        ans[1] = Math.floor(ans[1]);
        
        return ans;
    }


    /**
     * Most skeleton algorithms don't extend the end of the skeleton to the boundary.  We want to.
     * @param skeleton
     * @param first
     * @param roi
     * @param sampleInterval The offset of the point to sample from the end in order to determine the tangent vector.
     */
    final protected void extendEnd(ArrayList<double[]> skeleton, boolean first, PolygonRoi roi)
    {
        if (skeleton.size() < sampleInterval + 1) {
            throw new SkeletonPolygonException("Not enough points in skeleton", null);
        }

        double[] pt;
        double[] pt2;
        if (first) {
            pt = skeleton.get(0);
            pt2 = skeleton.get(sampleInterval);

        }
        else {
            pt = skeleton.get(skeleton.size()-1);
            pt2 = skeleton.get(skeleton.size()-1-sampleInterval);
        }

        double dx = pt[0] - pt2[0];
        double dy = pt[1] - pt2[1];
        double s = Math.sqrt(dx*dx + dy*dy);
        dx /= s;
        dy /= s;

        double[] cur = {pt[0] + dx, pt[1] + dy};
        double[] fcur = {Math.floor(cur[0]), Math.floor(cur[1])};
        while (roi.contains((int)fcur[0], (int)fcur[1])) {
            int idx = first? 0 : skeleton.size()-1;
            double[] tmp = skeleton.get(idx);
            if (! Arrays.equals(tmp, fcur)) {
                int idx2 = first? idx : idx+1;
                skeleton.add(idx2, fcur);
            }

            cur[0]+=dx;
            cur[1]+=dy;
            fcur = new double[]{Math.floor(cur[0]), Math.floor(cur[1])};
        }

        skeleton.remove(first? 0 : skeleton.size()-1); // make sure the skeleton is still contained within the boundary
    }

    protected static class SkeletonPolygonException extends RuntimeException
    {
        public SkeletonPolygonException(String msg, Throwable th)
        {
            super(msg, th);
        }
    }
}
