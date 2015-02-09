/**
 * Narvaro: @VERSION@
 * Build Date: @DATE@
 * Commit Head: @HEAD@
 * JDK: @JDK@
 * ANT: @ANT@
 *
 */

package edu.csus.ecs.moneybeets.narvaro.util;

/**
 * Enumeration of known license types.
 */
public enum License {

    /**
     * Distributed under a commercial license.
     */
    commercial,
    
    /**
     * Distributed under the GNU Public License (GPL).
     */
    gpl,
    
    /**
     * Distributed under the Apache License.
     */
    apache,
    
    /**
     * For internal use at an organization only and is not re-distributed.
     */
    internal,
    
    /**
     * Distributed under another license agreement not covered by one of the other choices.
     * The license agreement should be detailed in the component's README
     */
    other,
    
    ;
    
}
