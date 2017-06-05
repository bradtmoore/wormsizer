/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.duke.biology.baughlab.wormsize;
import ij.ImagePlus;
import java.lang.reflect.*;
import fiji.threshold.Auto_Threshold;
import ij.process.*;

/**
 * Wraps the Fiji Auto Threshold plugin so it can more easily be called within
 * code (without dialogues or images being showed).
 *
 * @author bradleymoore
 */
public class AutoThresholdWrapper {
    protected boolean ignoreWhite;
    protected boolean ignoreBlack;
    protected int method;

    public static final int MINIMUM=1;

    public AutoThresholdWrapper(boolean ignoreWhite, boolean ignoreBlack, boolean invert, int method) {
        this.ignoreWhite = ignoreWhite;
        this.ignoreBlack = ignoreBlack;
        this.method = method;
    }

    public int threshold(ImagePlus ip)
    {
        int[] data = ip.getProcessor().getHistogram();
        int min = -1;
        int max = -1;
        for (int i = 0; i < data.length; i++)
        {
            if (data[i] > 0)
            {
                if (min == -1)
                {
                    min = i;
                }
                max = i;
            }
        }

        int[] data2 = new int[max - min + 1]; // i believe these have to be correct, assuming there is some intensity value in the image
        for (int i = min; i <= max; i++)
        {
            data2[i-min] = data[i];
        }

        return Auto_Threshold.Minimum(data2) + min;
    }

    public static ImagePlus getThresholdedImage(ImagePlus ip, int threshold, boolean invert)
    {
        ImageProcessor p2 = ip.getProcessor();
        BinaryProcessor p = new BinaryProcessor(new ByteProcessor(ip.getWidth(), ip.getHeight()));
        ImagePlus ans = new ImagePlus("", p);
        int v1 = invert ? 0 : 255;
        int v2 = invert ? 255 : 0;
        for (int j = 0; j < ip.getProcessor().getPixelCount(); j++)
        {
            int v = (p2.get(j) < threshold) ? v1 : v2;
            p.set(j, v);
        }

        return ans;
    }

    protected static class AutoThresholdWrapperException extends RuntimeException
    {
        public AutoThresholdWrapperException(String msg, Throwable th)
        {
            super(msg, th);
        }
    }
}
