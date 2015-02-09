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
public interface NarvaroClientInfo {

    /**
     * Obtain the client's version information. Typically used for
     * display and logging information.
     * 
     * @return The version of the client.
     */
    public Version getVersion();
    
    /**
     * Obtain the client name (IP address or hostname).
     * 
     * @return The client's name as an IP address or hostname.
     */
    public String getName();
    
    /**
     * Set the client name (IP address or hostname). The client
     * must be restarted for this change to take effect.
     * 
     * @param clientName The client's name as an IP address or hostname.
     */
    public void setName(final String clientName);
    
    /**
     * Obtain the hostname (IP address or hostname) of this client.
     * 
     * @return The client's host name as an IP address or hostname.
     */
    public String getHostname();
    
    /**
     * Obtain the date when the client was last started.
     * 
     * @return The date the client was started or null
     * if the client has not been started.
     */
    public Date getLastStarted();
    
}
