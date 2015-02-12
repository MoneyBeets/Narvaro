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
 * Information 'snapshot' of a client's state. Useful for
 * statistics gathering and administration display.
 */
public class NarvaroClientInfo {

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
    public NarvaroClientInfo(final String name, final String hostname, final Version version, final Date startDate) {
        this.name = name;
        this.hostname = hostname;
        this.version = version;
        this.startDate = startDate;
    }
    
    /**
     * Obtain the client's version information. Typically used for
     * display and logging information.
     * 
     * @return The version of the client.
     */
    public Version getVersion() {
        return version;
    }
    
    /**
     * Obtain the client name (IP address or hostname).
     * 
     * @return The client's name as an IP address or hostname.
     */
    public String getName() {
        return name;
    }
    
    /**
     * Set the client name (IP address or hostname). The client
     * must be restarted for this change to take effect.
     * 
     * @param clientName The client's name as an IP address or hostname.
     */
    public void setName(final String name) {
        this.name = name;
    }
    
    /**
     * Obtain the hostname (IP address or hostname) of this client.
     * 
     * @return The client's host name as an IP address or hostname.
     */
    public String getHostname() {
        return hostname;
    }
    
    /**
     * Obtain the date when the client was last started.
     * 
     * @return The date the client was started or null
     * if the client has not been started.
     */
    public Date getLastStarted() {
        return startDate;
    }
}
