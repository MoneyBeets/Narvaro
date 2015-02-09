/**
 * Narvaro: @VERSION@
 * Build Date: @DATE@
 * Commit Head: @HEAD@
 * JDK: @JDK@
 * ANT: @ANT@
 *
 */

package edu.csus.ecs.moneybeets.narvaro;

/**
 * Interface that lets observers be notified when the client has
 * been started or is about to be stopped.
 * Use {@link Narvaro#addClientListener(NarvaroClientListener)} to
 * add new listeners.
 *
 */
public interface NarvaroClientListener {

    /**
     * Notification message indicating that the client has been stated. At this point
     * all client modules have been initialized and started.
     */
    public void clientStarted();
    
    /**
     * Notification message indication that the client is about to be stopped. At this
     * point all modules are still running so all client services are still available.
     */
    public void clientStopping();
    
}
