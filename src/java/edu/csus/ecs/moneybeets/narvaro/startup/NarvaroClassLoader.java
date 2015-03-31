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
import java.io.FilenameFilter;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;

/**
 * A classloader to extend the classpath to include
 * all jars in the application lib directory.
 *
 */
public class NarvaroClassLoader extends URLClassLoader {

    /**
     * Constructs the application parent classloader.
     * 
     * @param parent The parent class loader (or null for none).
     * @param libDir The directory to load jar files from.
     * @throws MalformedURLException If the libDir path is not valid.
     */
    NarvaroClassLoader(final ClassLoader parent, final File libDir) throws MalformedURLException {
        super(new URL[] { libDir.toURI().toURL() }, parent);
        
        File[] jars = libDir.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(final File dir, final String name) {
                String smallName = name.toLowerCase();
                // allow only jar files and zips (some JDBC drivers are zips)
                if (smallName.endsWith(".jar") || smallName.endsWith(".zip")) {
                    return true;
                }
                return false;
            }
        });
        
        // do nothing if no jar or zip files found
        if (jars == null) {
            return;
        }
        
        for (int i = 0; i < jars.length; i++) {
            if (jars[i].isFile()) {
                addURL(jars[i].toURI().toURL());
            }
        }
    }
    
}
