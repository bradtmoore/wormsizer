/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.duke.biology.testutil;
import java.io.*;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;

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
     
     Path start = FileSystems.getDefault().getPath(f.getAbsolutePath());
     try {
        Files.walkFileTree(start, new SimpleFileVisitor<Path>() {
                     
            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                Files.delete(file);
                return FileVisitResult.CONTINUE;
            }
              
            @Override
            public FileVisitResult postVisitDirectory(Path dir, IOException e)
                throws IOException
            {
                if (e == null) {
                    Files.delete(dir);
                    return FileVisitResult.CONTINUE;
                } else {
                    // directory iteration failed
                    throw e;
                }
            }
        });       
     }
     catch (IOException e) {
         e.printStackTrace();
     }
    }

    protected static class TestUtilException extends RuntimeException {
        public TestUtilException(String msg, Throwable th) {
            super(msg, th);
        }
    }
}
