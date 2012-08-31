/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.duke.biology.baughlab.wormsize;
import ij.plugin.filter.ParticleAnalyzer;
import ij.plugin.frame.RoiManager;
import ij.gui.*;
import ij.ImagePlus;
import ij.process.*;
import java.util.*;
import ij.measure.Calibration;
import java.awt.Rectangle;


/**
 * This is basically the same thing that the ParticlerAnalyzer in ImageJ does.  However,
 * it will hopefully be greatly simplified.
 *
 * @author bradleymoore
 */
public class PolygonComponentFinder {
    protected double minSize;
    protected double maxSize;
    protected ArrayList<PolygonRoi> polygons;
    protected ArrayList<Double> areas;
    protected ImagePlus marked;
    protected Calibration calibration;
    protected int PADDING=3;

    public PolygonComponentFinder(double minSize, double maxSize)
    {
        this(minSize, maxSize, new Calibration());
    }

    public PolygonComponentFinder(double minSize, double maxSize, Calibration calibration) {
        this.minSize = minSize;
        this.maxSize = maxSize;
        this.calibration = calibration;
    }

    /**
     *
     * @param ip a thresholded image (binary, white is object, black is background)
     */
    public void find(ImagePlus ip)
    {
        ImagePlus ip2 = getPaddedImage(ip, PADDING);

        int roiType = Wand.allPoints()?Roi.FREEROI:Roi.TRACED_ROI; // not sure why this is required, but eh
        polygons = new ArrayList<PolygonRoi>();
        areas = new ArrayList<Double>();
        BinaryProcessor bp = new BinaryProcessor(new ByteProcessor(ip2.getWidth(), ip2.getHeight()));
        marked = new ImagePlus("", bp); // will contain the pixels we've already touched
        bp.setValue(0);
        bp.fill();

        // this wand class isn't reliable on edge polygons - not sure how to work around it.
        ImageProcessor p = ip2.getProcessor();

       
        for (int y = 0; y < p.getHeight(); y++)
        {
            for (int x = 0; x < p.getWidth(); x++)
            {
                int vm = bp.get(x, y);
                int vi = p.get(x,y);
                if (vm == 0 && vi == 255) // labeled and not already marked
                {
                    Wand w = new Wand(p);
                    w.autoOutline(x, y, 0, Wand.EIGHT_CONNECTED);

                    // PolygonRoi destructively reads these point arrays, go figure
                    int[] xs = Arrays.copyOf(w.xpoints, w.npoints);
                    int[] ys = Arrays.copyOf(w.ypoints, w.npoints);
                    int[] xs2 = Arrays.copyOf(xs, xs.length);
                    int[] ys2 = Arrays.copyOf(ys, ys.length);
                    
                    for (int j = 0; j < xs2.length; j++) {
                       xs2[j] -= PADDING;
                       ys2[j] -= PADDING;
                    }

                    PolygonRoi pr = new PolygonRoi(xs, ys, w.npoints, roiType);
                    PolygonRoi pr2 = new PolygonRoi(xs2, ys2, w.npoints, roiType);

                    bp.setRoi(pr);
                    ImageStatistics stat = ImageStatistics.getStatistics(bp, 0, calibration);
                    if (minSize <= stat.area && stat.area <= maxSize)
                    {
                        polygons.add(pr2);
                        areas.add(stat.area);
                    }

                    bp.setValue(255);
                    bp.fill(pr);
                    bp.resetRoi();
                }
            }
        }
    }

    /**
     * There's some weird thing with off-by-one pixel values.
     *
     * @param ip
     * @param padding
     * @return
     */
    protected ImagePlus getPaddedImage(ImagePlus ip, int padding)
    {
        ImageProcessor op = ip.getProcessor();
        ImageProcessor p = new ByteProcessor(ip.getWidth() + 2*padding, ip.getHeight() + 2*padding);
        for (int y = 0; y < ip.getHeight(); y++)
        {
            for (int x = 0; x < ip.getWidth(); x++)
            {
                p.set(x+padding, y+padding, op.get(x, y));
            }
        }
//        ip.copy(false);
        ImagePlus ans = new ImagePlus(null, p);
  //      ans.setRoi(new Rectangle(padding, padding, ip.getWidth(), ip.getHeight()));
    //    ans.paste(); // should paste in "center" of image
        return ans;
    }

    public ArrayList<PolygonRoi> getPolygons()
    {
        return polygons;
    }

}
