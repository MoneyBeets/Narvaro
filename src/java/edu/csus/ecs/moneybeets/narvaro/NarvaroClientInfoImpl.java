/**
 * Narvaro: @VERSION@
 * Build Date: @DATE@
 * Commit Head: @HEAD@
 * JDK: @JDK@
 * ANT: @ANT@
 * 
 */

package edu.csus.ecs.moneybeets.narvaro;

import java.util.Date;

import edu.csus.ecs.moneybeets.narvaro.util.Version;

/**
 * Implements the client info for a basic client.
 */
public class NarvaroClientInfoImpl implements NarvaroClientInfo {

    private Date startDate;
    private String name;
    private String hostname;
    private Version version;
    
    /**
     * Simple constructor.
     * 
     * @param name The client's name.
     * @param hostname The client's hostname.
     * @param version The client's version number.
     * @param startDate The client's last start time (can be null indicating
     *              it hasn't been started yet).
     */
    public NarvaroClientInfoImpl(final String name, final String hostname, final Version version, final Date startDate) {
        this.name = name;
        this.hostname = hostname;
        this.version = version;
        this.startDate = startDate;
    }
    
    public Version getVersion() {
        return version;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(final String name) {
        this.name = name;
    }
    
    public String getHostname() {
        return hostname;
    }
    
    public Date getLastStarted() {
        return startDate;
    }
}
