/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.duke.biology.baughlab.wormsize;

/**
 *
 * @author bradleymoore
 */
public class VersionNumber implements Comparable<VersionNumber> {
    protected int major;
    protected int minor;
    protected int revision;
    
    public VersionNumber(int major, int minor, int revision) {
        this.major = major;
        this.minor = minor;
        this.revision = revision;
    }
    
    public VersionNumber(String s) {
        String[] ss = s.split(".");
        major = Integer.parseInt(ss[0]);
        minor = Integer.parseInt(ss[1]);
        revision = Integer.parseInt(ss[2]);
    }

    @Override
    public int compareTo(VersionNumber t) {
        int ans = 0;
        
        if (major > t.major) {
            ans = 1;
        }
        else if (major < t.major) {
            ans = -1;
        }
        else {
            if (minor > t.minor) {
                ans = 1;
            }
            else if (minor < t.minor) {
                ans = -1;
            }
            else {
                if (revision > t.revision) {
                    ans = 1;
                }
                else if (revision < t.revision) {
                    ans = -1;
                }
            }
        }
        
        return ans;
    }
    
}
