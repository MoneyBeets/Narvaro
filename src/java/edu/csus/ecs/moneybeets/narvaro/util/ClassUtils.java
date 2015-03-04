/**
 * Narvaro: @VERSION@
 * Build Date: @DATE@
 * Commit Head: @HEAD@
 * JDK: @JDK@
 * ANT: @ANT@
 *
 */

package edu.csus.ecs.moneybeets.narvaro.util;

import java.io.InputStream;

/**
 * A utility class to assist with loading of classes or resources by name. Many application servers use
 * custom classloaders, which will break uses of:
 * <pre>
 *    Class.forName(className);
 * </pre>
 * 
 * This utility attempts to load the class or resource using a number of different mechanisms
 * to work around this problem.
 *
 */
public class ClassUtils {

    private static final ClassUtils instance = new ClassUtils();
    
    /**
     * Loads the class with the specified name.
     * 
     * @param className The name of the class.
     * @return The resulting <code>Class</code> object.
     * @throws ClassNotFoundException If the class was not found.
     */
    public static Class<?> forName(final String className) throws ClassNotFoundException {
        return instance.loadClass(className);
    }
    
    /**
     * Loads the given resource as a stream.
     * 
     * @param name The name of the resource that exists in the classpath.
     * @return The resource as an input stream or <code>null</code> if the resource was not found.
     */
    public static InputStream getResourceAsStream(final String name) {
        return instance.loadResource(name);
    }
    
    /**
     * Not instantiatable
     */
    private ClassUtils() {}
    
    private Class<?> loadClass(final String className) throws ClassNotFoundException {
        Class<?> theClass = null;
        try {
            theClass = Class.forName(className);
        } catch (ClassNotFoundException e1) {
            try {
                theClass = Thread.currentThread().getContextClassLoader().loadClass(className);
            } catch (ClassNotFoundException e2) {
                theClass = getClass().getClassLoader().loadClass(className);
            }
        }
        return theClass;
    }
    
    private InputStream loadResource(final String name) {
        InputStream in = getClass().getResourceAsStream(name);
        if (in == null) {
            in = Thread.currentThread().getContextClassLoader().getResourceAsStream(name);
            if (in == null) {
                in = getClass().getClassLoader().getResourceAsStream(name);
            }
        }
        return in;
    }
    
}