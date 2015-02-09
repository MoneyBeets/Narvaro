/**
 * Narvaro: @VERSION@
 * Build Date: @DATE@
 * Commit Head: @HEAD@
 * JDK: @JDK@
 * ANT: @ANT@
 *
 */

package edu.csus.ecs.moneybeets.narvaro.starter;

import java.io.File;

import edu.csus.ecs.moneybeets.narvaro.Narvaro;

/**
 * Starts the core Narvaro client. A bootstrap class that configures
 * classloaders to ensure easy, dynamic client startup.
 * 
 * <p>
 * This class should be for standalone mode only.
 * </p>
 * <p>
 * Tasks:
 * <ul>
 *   <li>Add all jars in the lib directory to the classpath.</li>
 *   <li>Add the config directory to the classpath for loadResource().</li>
 *   <li>Start the client.</li>
 * </ul>
 * </p>
 * <p>
 * Note: If the environment property <code>narvaro.lib.dir</code> is specified,
 * ClientStarter will attempt to use this value as the value for Narvaro's lib
 * directory. If the property is not specified the default value of <code>../lib</code>
 * will be used.
 * </p>
 *
 */
public class ClientStarter {

    /**
     * Default to this location if one has not been specified.
     */
    private static final String DEFAULT_LIB_DIR = "../lib";
    
    public static void main(String[] args) {
        new ClientStarter().start();
    }
    
    /**
     * Starts the client by loading and installing the bootstrap container.
     * Once the start method is called, the client is started and the client
     * starter should not be used again.
     */
    private void start() {
        // Setup the classpath using NarvaroClassLoader
        try {
            // Load up the bootstrap container
            final ClassLoader parent = findParentClassLoader();
            
            String libDirString = System.getProperty("narvaro.lib.dir");
            
            File libDir;
            if (!"".equals(libDirString) && libDirString != null) {
                // If the lib directory property has been specified and it actually
                // exists, use it, else use the default.
                libDir = new File(libDirString);
                if (!libDir.exists()) {
                    System.out.println("Lib directory " + libDirString 
                            + " does not exist. Using default " + DEFAULT_LIB_DIR);
                    libDir = new File(DEFAULT_LIB_DIR);
                }
            } else {
                libDir = new File(DEFAULT_LIB_DIR);
            }
            
            ClassLoader loader = new NarvaroClassLoader(parent, libDir);

            Thread.currentThread().setContextClassLoader(loader);
            // starts up the main client
            Class<?> containerClass = loader.loadClass("edu.csus.ecs.moneybeets.narvaro.Narvaro");
            containerClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();;
        }
    }
    
    /**
     * Locates the best class loader based on context (see class description).
     * 
     * @return The best parent classloader to use.
     */
    private ClassLoader findParentClassLoader() {
        ClassLoader parent = Thread.currentThread().getContextClassLoader();
        if (parent == null) {
            parent = this.getClass().getClassLoader();
            if (parent == null) {
                parent = ClassLoader.getSystemClassLoader();
            }
        }
        return parent;
    }
    
}
