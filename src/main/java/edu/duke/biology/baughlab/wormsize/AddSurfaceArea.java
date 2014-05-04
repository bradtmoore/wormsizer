/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.duke.biology.baughlab.wormsize;

import edu.duke.biology.baughlab.wormsize.xml.*;
import java.io.*;
import java.util.*;

/**
 * Script that will append surface area to each .xml (and its corresponding CSV)
 * specified.
 *
 * @author bradleymoore
 */
public class AddSurfaceArea {

    public static void main(String[] args) {
        for (String path : args) {
            File f = new File(path);
            if (f.exists() && f.getPath().endsWith(".xml")) {
                ExperimentOutputType eot = new ExperimentOutputType();
                File outputDirectory = f.getParentFile();
                System.out.println("Adding surface area to: " + f.getAbsolutePath());
                try {
                    eot = BindingFactory.unmarshal(f, new ExperimentOutputType());
                } catch (BindingFactoryException e) {
                    e.printStackTrace();
                }

                double micronsPerPixel = eot.getExperimentSettings().getMicronsPerPixel();
                for (WormOutputType wot : eot.getWormOutputs().getWormOutput()) {
                    for (WormType w : wot.getWorms().getWorm()) {
                        // the sample radii have all the information we need to
                        // calculate the surface area.  However, they are missing
                        // the first point (which are width = 0)
                        String[] radiiStr = w.getSampledRadii().split(",");
                        String[] skeletonStr = w.getSkeleton().split(",");
                        
                        int rn = radiiStr.length / 4; 
                        double[][] radii = new double[rn][4];
                        for (int i = 0; i < rn; i++) {
                            double p1 = Double.parseDouble(radiiStr[i*4 + 0]);
                            double p2 = Double.parseDouble(radiiStr[i*4 + 1]);
                            double p3 = Double.parseDouble(radiiStr[i*4 + 2]);
                            double p4 = Double.parseDouble(radiiStr[i*4 + 3]);
                            
                            radii[i] = new double[]{p1, p2, p3, p4}; // skeleton pt, boundary bt
                        }
                        
                        double p5 = Double.parseDouble(skeletonStr[0]);
                        double p6 = Double.parseDouble(skeletonStr[1]);
                        double p7 = Double.parseDouble(skeletonStr[skeletonStr.length - 2]);
                        double p8 = Double.parseDouble(skeletonStr[skeletonStr.length - 1]);
                        
                        
                        ArrayList<Double> dists = new ArrayList<Double>();
                        ArrayList<Double> widths = new ArrayList<Double>();
                        
                        // i know this is confusing as hell
                        // the sample radii i return doesn't include the first and last points
                        // so i have to add them from the skeleton manually
                        widths.add(0.0); // at point 0
                        widths.add(2.0*Worm.dist(new double[]{radii[0][0], radii[0][1]}, new double[]{radii[0][2], radii[0][3]}));
                        dists.add(Worm.dist(new double[]{p5, p6}, new double[]{radii[0][0], radii[0][1]}));
                        
                        for (int i = 1; i < rn; i++) {
                            double[] pt1 = new double[]{radii[i-1][0], radii[i-1][1]};
                            double[] pt2 = new double[]{radii[i][0], radii[i][1]};
                            double[] bpt = new double[]{radii[i][2], radii[i][3]};
                            
                         
                            widths.add(2.0*Worm.dist(pt2, bpt));                            
                            dists.add(Worm.dist(pt1, pt2));
                        }
                        widths.add(0.0);
                        dists.add(Worm.dist(new double[]{radii[rn-1][0], radii[rn-1][1]}, new double[]{p7,p8}));
                        
                        double cnt = 0.0;
                        for (double wid : widths) {
                            cnt += wid;
                        }
                        cnt = cnt / widths.size() * micronsPerPixel;
                        
                        double cnt2 = 0.0;
                        for (double wid : dists) {
                            cnt2 += wid;
                        }
                        cnt2 *= micronsPerPixel;
                        
                        //System.out.println("mean width:" + cnt + ", length:" + cnt2 + ", worm " + w.getId() + ", img " + wot.getImageFile());
                        //System.out.println("last radii:" + Arrays.toString(radii[rn-1]));
                        //System.out.println(widths);
                        //System.out.println(dists);
                        double a = Worm.computeSurfaceArea(dists, widths);
                        
                        w.setSurfaceArea(a * micronsPerPixel * micronsPerPixel);
                    }
                }
                
                try {
                    BindingFactory.marshal(f, eot, new ObjectFactory().createExperimentOutput(eot));
                    ExperimentOutputCSV.writeToCSV(eot, outputDirectory);
                }
                catch (BindingFactoryException e) {
                    e.printStackTrace();
                }
                catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }
    }
}
