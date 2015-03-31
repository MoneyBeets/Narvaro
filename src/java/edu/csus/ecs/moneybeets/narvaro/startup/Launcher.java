/**
 * Narvaro: @VERSION@
 * Build Date: @DATE@
 * Commit Head: @HEAD@
 * JDK: @JDK@
 * ANT: @ANT@
 * 
 */

package edu.csus.ecs.moneybeets.narvaro.startup;

import java.io.File;
import java.lang.reflect.Method;

/**
 * The main application launcher for Narvaro.
 * 
 * This class uses a custom classloader to dynamically
 * load all libraries into the classpath then attempts to
 * startup the main Narvaro application.
 *
 */
public class Launcher {

    private static final String DEFAULT_LIB_DIR = "../lib";
    
    public static void main(String[] args) {
        new Launcher().start();
    }
    
    /**
     * Starts Narvaro after loading all found libraries onto the classpath.
     */
    private void start() {
        
        // setup the classpath using NarvaroClassLoader
        try {
            // load the bootstrap container
            final ClassLoader parent = findParentClassLoader();
            
            String libDirString = System.getProperty("narvaro.lib.dir");
            
            File libDir = null;
            if (!"".equals(libDirString) && libDirString != null) {
                // if the lib directory property has been specified and exists,
                //    use it, else use the default.
                libDir = new File(libDirString);
                if (!libDir.exists()) {
                    // logging does not exist yet
                    System.out.println("Lib directory " 
                            + libDirString 
                            + " does not exist. Using default " 
                            + DEFAULT_LIB_DIR);
                    libDir = new File(DEFAULT_LIB_DIR);
                }
            } else {
                libDir = new File(DEFAULT_LIB_DIR);
            }
            
            ClassLoader loader = new NarvaroClassLoader(parent, libDir);
            
            Thread.currentThread().setContextClassLoader(loader);
            
            // get the main app class
            Class<?> narvaroClass = loader.loadClass("edu.csus.ecs.moneybeets.narvaro.Narvaro");
            
            // start Narvaro
            Method startupMethod = narvaroClass.getDeclaredMethod("startup");
            startupMethod.invoke(new Object[0], new Object[0]);

        } catch (Exception e) {
            // there's no logging yet...
            e.printStackTrace();
        }
        
    }
    
    /**
     * Locates the best class loader based on context.
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
