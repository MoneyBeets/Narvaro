/**
 * Narvaro: @VERSION@
 * Build Date: @DATE@
 * Commit Head: @HEAD@
 * JDK: @JDK@
 * ANT: @ANT@
 *
 */

package edu.csus.ecs.moneybeets.narvaro.util;

import java.util.StringTokenizer;

import org.apache.log4j.Logger;

/**
 * Basic application version object.
 */
public class Version implements Comparable<Version> {
    
    final private static Logger LOG = Logger.getLogger(Version.class.getName());

    /**
     * The major number (ie. 1.x.x)
     */
    private int major;
    /**
     * The minor number (ie. x.1.x)
     */
    private int minor;
    /**
     * The revision number (ie. x.x.1)
     */
    private int revision;
    
    private String versionString;
    
    /**
     * Create a new version information object.
     * 
     * @param major The major release number.
     * @param minor The minor release number.
     * @param revision The revision release number.
     */
    public Version(final int major, final int minor, final int revision) {
        this.major = major;
        this.minor = minor;
        this.revision = revision;
    }
    
    /**
     * Create a new version from a simple version string (ie. "1.2.3")
     * 
     * @param versionString The version string.
     */
    public Version(final String versionString) {
        
        major = minor = revision = 0;
        if (!"".equals(versionString) && versionString != null) {
            StringTokenizer tokenizer = new StringTokenizer(versionString, ".");
            try {
                major = tokenizer.hasMoreTokens() ? Integer.parseInt(tokenizer.nextToken()) : 0;
                minor = tokenizer.hasMoreTokens() ? Integer.parseInt(tokenizer.nextToken()) : 0;
                revision = tokenizer.hasMoreTokens() ? Integer.parseInt(tokenizer.nextToken()) : 0;
            } catch (NumberFormatException e) {
                LOG.warn("Invalid version number: " + versionString);
            }
        }
        
    }
    
    /**
     * Returns the version number of this instance of Narvaro as
     * a String (ie. major.minor.revision).
     * 
     * @return The version string.
     */
    public String getVersionString() {
        if ("".equals(versionString) || versionString == null) {
            versionString = major + "." + minor + "." + revision;
        }
        return versionString;
    }
    
    /**
     * Get the major release number for this product.
     * 
     * @return The major release number (ie. 1.x.x)
     */
    public int getMajor() {
        return major;
    }
    
    /**
     * Get the minor release number for this product.
     * 
     * @return The minor release number (ie. x.1.x)
     */
    public int getMinor() {
        return minor;
    }
    
    /**
     * Get the revision release number for this product.
     * 
     * @return The revision release number (ie. x.x.1)
     */
    public int getRevision() {
        return revision;
    }
    
    public boolean isNewerThan(final Version otherVersion) {
        return this.compareTo(otherVersion) > 0;
    }
    
    @Override
    public int compareTo(final Version that) {
        if (that == null) {
            return 1;
        }
        
        long thisVersion = (this.getRevision() * 10) + (this.getMinor() * 1000) + (this.getMajor() * 100000);
        long thatVersion = (that.getRevision() * 10) + (that.getMinor() * 1000) + (that.getMajor() * 100000);
        
        return thisVersion == thatVersion ? 0 : thisVersion > thatVersion ? 1 : -1;
    }
    
}
