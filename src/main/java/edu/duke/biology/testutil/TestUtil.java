/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.duke.biology.testutil;
import java.io.*;
/**
 *
 * @author bradleymoore
 */
public class TestUtil {
    /**
     * Clears, if exists, tmp directory.  If tmp doesn't exist, creates it.
     */
    public static void bootstrap()
    {
        File f = new File("./test-resources/tmp");
        if (f.exists())
        {
            deleteAll(f);
        }

        f.mkdir();
    }

    public static void deleteAll(File f) {
        String[] cmd = new String[]{"rm", "-r", f.getAbsolutePath()};
        ProcessBuilder pb = new ProcessBuilder(cmd);
        Process p = null;
        try {
            p = pb.start();
            p.waitFor();
        }
        catch (IOException e) {
            throw new TestUtilException(null, e);
        }
        catch (InterruptedException e) {
            throw new TestUtilException(null, e);
        }
        finally
        {
            if (p != null) {
                if (p.getErrorStream() != null) {
                    try
                    {
                        p.getErrorStream().close();
                    }
                    catch (IOException e)
                    {

                    }
                }
                if (p.getInputStream() != null) {
                    try
                    {
                        p.getInputStream().close();
                    }
                    catch (IOException e)
                    {

                    }
                }
                if (p.getOutputStream() != null) {
                    try
                    {
                        p.getOutputStream().close();
                    }
                    catch (IOException e)
                    {

                    }
                }
            }
        }
    }

    protected static class TestUtilException extends RuntimeException {
        public TestUtilException(String msg, Throwable th) {
            super(msg, th);
        }
    }
}
