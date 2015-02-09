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
import java.io.FilenameFilter;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;

/**
 * A simple classloader to extend the classpath to
 * include all jars in a lib directory.
 * 
 * <p>
 * The new classpath includes all <code>*.jar</code> and
 * <code>*.zip</code> archives (zip is commonly used in packaging
 * JDBC drivers). The extended classpath is used for both the
 * initial client startup, as well as loading plugin support jars.
 * </p>
 *
 */
public class NarvaroClassLoader extends URLClassLoader {

    /**
     * Constructs the classloader.
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
                boolean accept = false;
                String smallName = name.toLowerCase();
                if (smallName.endsWith(".jar")) {
                    accept = true;
                } else if (smallName.endsWith(".zip")) {
                    accept = true;
                }
                return accept;
            }
        });
        
        // Do nothing if no jar or zip files were found
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
