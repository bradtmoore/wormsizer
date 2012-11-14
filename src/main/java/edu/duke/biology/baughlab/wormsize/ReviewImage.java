/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.duke.biology.baughlab.wormsize;
import edu.duke.biology.baughlab.wormsize.xml.*;
import ij.gui.*;
import java.awt.Color;
import java.util.*;

/**
 * This class takes a set of worm objects and renders them onto an image.
 * 
 * @author bradleymoore
 */
public class ReviewImage extends ij.ImagePlus {
    public static final int DEFAULT = 0;
    public static final int HIDE_OVERLAY = 1;
    
    protected WormOutputType wot;
    protected WormType worm;
    protected int curPos;
    
    /**
     * 
     * @param wot is the set of worm objects to render, note, the pass field of wot may be written to
     * @param ip  is the image to render the objects onto
     */
    public ReviewImage(WormOutputType wot, ij.ImagePlus ip) {
        super(ip.getTitle(), ip.getImage());
        this.wot = wot;
        ip.close();
        curPos = 0;
    }
    
    @Override
    public void show() {
        super.show();
        setOverlay(new ij.gui.Overlay());
        
        // check to deal with cases where the segmentation fails completely
        if (wot.getWorms().getWorm().size() > 0) {
            render(curPos);
        }
        //this.setHideOverlay(false);
        //updateAndRepaintWindow();   
    }
    
    public int getWormCount()
    {
        return wot.getWorms().getWorm().size();
    }
    
    /**
     * Render only the specified worm
     * @param i 
     */
    public void renderOnly(int i) {
        curPos = i;
        setOverlay(new ij.gui.Overlay());
        render(curPos);
    }
    
    /**
     * Render the next worm
     */
    public void renderNext() {
        curPos++;
        setOverlay(new ij.gui.Overlay());
        render(curPos);
    }
    
    /**
     * Render the previous worm
     */
    public void renderPrev() {
        curPos--;
        setOverlay(new ij.gui.Overlay());
        render(curPos);
    }
    
    /**
     * Render all worms simultaneously
     */
    public void renderAll() {
       setOverlay(new ij.gui.Overlay());
       for (int i = 0; i < wot.getWorms().getWorm().size(); i++) {
           render(i);
       }       
    }

    public void setWormOutput(WormOutputType wot) {
        this.wot = wot;
    }
    
    
    public void setRenderStyle(int style) {
        switch (style) {
            case DEFAULT:
                setHideOverlay(false);
                break;
            case HIDE_OVERLAY:
                setHideOverlay(true);
                break;
        }
    }
    
    public boolean isCurPass() {
        return worm.isPass();
    }
    
    public void setCurPass(boolean pass) {
        worm.setPass(pass);
    }
    
    protected void render(int pos) {
       curPos = pos;
       ij.gui.Overlay overlay = getOverlay();
       
       worm = (WormType)wot.getWorms().getWorm().get(pos);
      
       PolygonRoi contour = getContour(worm.getContour());
       overlay.add(contour);
       overlay.add(getSkeleton(worm.getSkeleton()));
       
       for (ij.gui.Line line : getSampleWidths(worm.getSampledRadii())) {
            overlay.add(line);
       }
       
       java.awt.Rectangle r = contour.getBounds();
       int x = r.x + r.width / 2;
       int y = r.y + r.height / 2;
       TextRoi text = new TextRoi(x, y, worm.getId().toString());
       text.setStrokeColor(Color.red);
       overlay.add(text);
       
       setOverlay(overlay);
    }
    
    protected ij.gui.PolygonRoi getContour(String pts) {
        String[] strs = pts.split(",");
        int[] xpts = new int[strs.length/2];
        int[] ypts = new int[xpts.length];
        int j = 0;
        for (int i = 0; i < strs.length; i += 2) {
            xpts[j] = Integer.parseInt(strs[i]);
            ypts[j] = Integer.parseInt(strs[i+1]);
            j++;
        }
        
        ij.gui.PolygonRoi ans = new ij.gui.PolygonRoi(xpts, ypts, xpts.length, ij.gui.PolygonRoi.POLYGON);
        ans.setStrokeColor(Color.yellow);
        return ans;
    }
    
    protected ij.gui.PolygonRoi getSkeleton(String pts) {
        String[] strs = pts.split(",");
        float[] xpts = new float[strs.length/2];
        float[] ypts = new float[xpts.length];
        int j = 0;
        for (int i = 0; i < strs.length; i += 2) {
            xpts[j] = Float.parseFloat(strs[i]);
            ypts[j] = Float.parseFloat(strs[i+1]);
            j++;
        }
        
        ij.gui.PolygonRoi ans = new ij.gui.PolygonRoi(xpts, ypts, xpts.length, ij.gui.PolygonRoi.POLYLINE);
       
        ans.setStrokeColor(Color.blue);
        return ans;
        
    }
    
    protected ArrayList<ij.gui.Line> getSampleWidths(String pts) {
        String[] strs = pts.split(",");
        int[] xpts1 = new int[strs.length/4];
        int[] ypts1 = new int[xpts1.length];
        int[] xpts2 = new int[xpts1.length];
        int[] ypts2 = new int[xpts1.length];
        
        int j = 0;
        ArrayList<ij.gui.Line> ans = new ArrayList<ij.gui.Line>();
        for (int i = 0; i < strs.length; i += 4) {
            xpts1[j] = (int)Double.parseDouble(strs[i]);
            ypts1[j] = (int)Double.parseDouble(strs[i+1]);
            xpts2[j] = (int)Double.parseDouble(strs[i+2]);
            ypts2[j] = (int)Double.parseDouble(strs[i+3]);

            
            ij.gui.Line tmp = new ij.gui.Line(xpts1[j], ypts1[j], xpts2[j], ypts2[j]);
            tmp.setStrokeColor(Color.cyan);
            ans.add(tmp);
            j++;
        }
        
        return ans;
    }
}
