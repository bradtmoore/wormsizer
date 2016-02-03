/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.duke.biology.baughlab.wormsize;

import ij.ImagePlus;
import ij.process.ImageProcessor;

import java.util.ArrayList;

import sc.fiji.analyzeSkeleton.AnalyzeSkeleton_;
import sc.fiji.analyzeSkeleton.Edge;
import sc.fiji.analyzeSkeleton.Graph;
import sc.fiji.analyzeSkeleton.Point;
import sc.fiji.analyzeSkeleton.SkeletonResult;
import sc.fiji.analyzeSkeleton.Vertex;
import sc.fiji.skeletonize3D.Skeletonize3D_;


/**
 * This is a wrapper to the Skeletonize and AnalyzeSkeleton plugins that allows us to get
 * the points out of the skeleton.
 * 
 * @author bradleymoore
 */
public class SkeletonizeWrapper {
    protected ImagePlus skeletonImage;
    protected ImagePlus lspImage;
    protected ArrayList<ArrayList<double[]>> skeletons;
    protected ImagePlus pointTypeImage; // edges, vertices, junctions, slabs, etc
    protected ArrayList<Double> scores;

    public SkeletonizeWrapper()
    {

    }

    /**
     * Closes the ImagePlus objects opened during processing.
     */
    public void closeAll() {
        skeletonImage.close();
        lspImage.close();
        pointTypeImage.close();
    }
    
    /**
     *
     * @param threshold Black and white image (0 and 255) where white is object and black is background.
     */
    public void skeletonize(ImagePlus threshold)
    {
        scores = new ArrayList<Double>();
        // if i leave the ROI, the duplicate command will just copy a bounding box
        // huzzah global variable-ish behavior!
        threshold.killRoi();
        ImagePlus tmp = new ImagePlus("", threshold.getProcessor().duplicate().convertToByte(false));//threshold.duplicate();
        Skeletonize3D_ skel = new Skeletonize3D_();
        skel.setup("", tmp);
        skel.run(tmp.getProcessor());

        AnalyzeSkeleton_ as = new AnalyzeSkeleton_();
        as.setup("", tmp);

        // longest shortest path is what we want
        // stuck in a private variable
        // also in the image
        // blarghs.
        // the skeleton won't extend to tips...
        // so, we need to...
        // 1.  downsample it and fit it with splines
        // 2.  extend from the tangent of the end points to the boundary
        // 3.  sample along the curve and get widths
        SkeletonResult sr = as.run(AnalyzeSkeleton_.SHORTEST_BRANCH, false, true, tmp, true, false);
        lspImage = new ImagePlus(null, as.getResultImage(true).getProcessor(1));
        pointTypeImage = new ImagePlus(null, as.getResultImage(false).getProcessor(1));
        Graph[] gs = sr.getGraph();
        skeletons = new ArrayList<ArrayList<double[]>>();

        if (gs != null) { // we found at least one skeleton
            for (Graph g : gs)
            {
                ArrayList<double[]> path = new ArrayList<double[]>();
                ArrayList<Vertex> vertices = g.getVertices();
                Vertex v = findEndPoint(vertices, pointTypeImage);
                addToPath(v, path, true);
                scores.add(getScore(g, path));
                skeletons.add(path);
            }
        }

        skeletonImage = new ImagePlus("", threshold.getProcessor().duplicate().convertToByte(false));//threshold.duplicate();
        skeletonImage.getProcessor().threshold(255);

        for (ArrayList<double[]> path : skeletons)
        {
            for (int i = 0; i < path.size(); i++)
            {
                skeletonImage.getProcessor().set((int)path.get(i)[0], (int)path.get(i)[1], (i+1) % 255);
            }
        }
        tmp.close();
    }

    /**
     * Returns the percentage of covered points in the skeletong (i.e. more cycles lower score)
     * @param g
     * @param path
     * @return
     */
    protected double getScore(Graph g, ArrayList<double[]> path) {
        double total = g.getVertices().size();
        for (Edge ed : g.getEdges()) {
            total += ed.getSlabs().size();
        }

        return path.size() / total;
    }

    public ArrayList<ArrayList<double[]>> getSkeletons()
    {
        return skeletons;
    }

    public ArrayList<Double> getScores() {
        return scores;
    }


    public ImagePlus getSkeletonImage()
    {
        return skeletonImage;
    }

    protected boolean pointEquals(double[] p1, Point p2)
    {
        return p1[0] == p2.x && p1[1] == p2.y;
    }

    protected double[] pointToDouble(Point p)
    {
        return new double[]{p.x, p.y};
    }

    /**
     * Reverse engineers the graph structure of AnalyzeSkeleton to return the
     * longest shortest path as a list of points in order of one end to another.
     * @param v A vertex
     * @param path A path to build
     * @param first Whether this is the first call to this recursive function (i.e. root vertex)
     */
    protected void addToPath(Vertex v, ArrayList<double[]> path, boolean first)
    {
        Point p = v.getPoints().get(0);
        path.add(pointToDouble(p));
        if (first || ! isPointType(p, AnalyzeSkeleton_.END_POINT)) // check this isn't the last end point
        {
            for (Edge ed : v.getBranches())
            {
                Point p1 = ed.getV1().getPoints().get(0);
                Point p2 = ed.getV2().getPoints().get(0);

                // this is somewhat messy, just check that we haven't touched this edge and
                // that it is a part of the shortest_path
                boolean doEdge = false;
                if (ed.getSlabs().size() > 0)
                {
                    Point s1 = ed.getSlabs().get(0);
                    doEdge = true;
                    for (int i = path.size()-1; i >= 0; i--)
                    {
                        if (pointEquals(path.get(i), s1))
                        {
                            doEdge = false;
                            break;
                        }
                    }

                    doEdge = doEdge && isPointType(s1, AnalyzeSkeleton_.SHORTEST_PATH);
                }


                if (doEdge)
                {
                    // find out which direction we are going on this edge
                    double[] pp = pointToDouble(p);
                    boolean forward = pointEquals(pp, p1);

                    ArrayList<Point> slab = ed.getSlabs();
                    if (forward)
                    {
                        for (int i = 0; i < slab.size(); i++)
                        {
                            path.add(pointToDouble(slab.get(i)));
                        }
                        addToPath(ed.getV2(), path, false);
                    }
                    else
                    {
                        for (int i = slab.size() - 1; i >= 0; i--)
                        {
                            path.add(pointToDouble(slab.get(i)));
                        }
                        addToPath(ed.getV1(), path, false);
                    }
                }
            }
        }
    }

    /**
     * Returns whether the given point is of the given type.  Type is one of
     * AnalyzeSkeleton types.
     * @param p
     * @param type
     * @return
     */
    protected boolean isPointType(Point p, int type)
    {
        boolean ans = false;

        if (type == AnalyzeSkeleton_.SHORTEST_PATH)
        {
            ans = lspImage.getProcessor().get(p.x, p.y) == type;
        }
        else
        {
            ans = pointTypeImage.getProcessor().get(p.x, p.y) == type;
        }

        return ans;
    }

    /**
     * The AnalyzeSkeleton_ plugin hasn't exposed as much of its members as it should.  I'm working with
     * what I got to find the end points (even though they are already annotated as members in the Graph class.
     * 
     * @param vertices
     * @param pointTypeImage Image denoting the type of each point
     * @return
     */
    protected Vertex findEndPoint(ArrayList<Vertex> vertices, ImagePlus pointTypeImage)
    {
        Vertex ans = null;
        ImageProcessor pr = pointTypeImage.getProcessor();
        for (Vertex v : vertices)
        {
            for (Point p : v.getPoints())
            {
                if (isPointType(p, AnalyzeSkeleton_.END_POINT))
                {
                    ans = v;
                    return ans;
                }
            }
        }

        return ans;
    }
 /**
  * Returns the longest-shortest path tagged skeleton image.
  * @return
  */
    public ImagePlus getLspImage()
    {
        return lspImage;
    }
}
