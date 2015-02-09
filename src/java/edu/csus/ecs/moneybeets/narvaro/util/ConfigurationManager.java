/**
 * Narvaro: @VERSION@
 * Build Date: @DATE@
 * Commit Head: @HEAD@
 * JDK: @JDK@
 * ANT: @ANT@
 *
 */

package edu.csus.ecs.moneybeets.narvaro.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Properties;

import org.apache.log4j.Logger;

/**
 * Manages configuration of Narvaro
 */

public enum ConfigurationManager {
        
    NARVARO,
    ;
    
    private ConfigurationManager() {}
    
    private final Logger LOG = Logger.getLogger(ConfigurationManager.class.getName());
    
    private String home;
    private Properties narvaroProperties;
    private String propertiesFile;
    
    /**
     * Allows the name of the local config file to be changed.
     * The default is "narvaro.properties"
     * 
     * @param configName The name of the config file.
     */
    public void setConfigName(final String configName) {
        this.propertiesFile = configName;
    }
    
    /**
     * Returns the name of the local config file.
     * 
     * @return The name of the config file.
     */
    public String getConfigName() {
        return this.propertiesFile;
    }
    
    /**
     * Loads Narvaro properties if necessary. Property loading must be done lazily so
     * that we give outside classes a chance to set <code>home</code>
     */
    private void loadNarvaroProperties() {
        if ("".equals(home) || home == null) {
            LOG.error("The home directory has not been configured");
        } else {
            narvaroProperties = loadproperties(home + File.separator + getConfigName());
        }
    }
    
    /**
     * Returns the location of the <code>home</code> directory.
     * 
     * @return The location of the home directory.
     */
    public String getHomeDirectory() {
        if (narvaroProperties == null) {
            loadNarvaroProperties();
        }
        return home;
    }
    
    /**
     * Sets the location of the <code>home</code directory. The directory must exist and
     * the user running the application must have read and write permissions over the
     * specified directory.
     * 
     * @param pathname
     */
    public void setHomeDirectory(final String pathname) {
        File nh = new File(pathname);
        // Do a permission check on the new home directory
        if (!nh.exists()) {
            LOG.error("The specified home directory does not exist (" + pathname + ")");
        } else if (!nh.canRead() || !nh.canWrite()) {
            LOG.error("The user running this application cannot read and write to "
                    + "the specified home directory (" + pathname + "). "
                    + "Please grant the executing user read and write permissions.");
        } else {
            home = pathname;
        }
    }
    
    /**
     * Get <code>long</code> value of key property.
     * 
     * @param key Key to search for.
     * @return Value of key property.
     * @throws NumberFormatException If value is not a <code>long</code>.
     */
    public long getLong(final String key) throws NumberFormatException {
        if (narvaroProperties == null) {
            loadNarvaroProperties();
        }
        return Long.parseLong(this.narvaroProperties.getProperty(key));
    }
    
    /**
     * Get <code>long</code> value of key property. If the property
     * doesn't exist, the <code>defaultValue</code> will be returned.
     * 
     * @param key Key to search for.
     * @param defaultValue The value returned if the property doesn't exist.
     * @return Value of key property.
     */
    public long getLong(final String key, final long defaultValue) {
        if (narvaroProperties == null) {
            loadNarvaroProperties();
        }
        String val = this.narvaroProperties.getProperty(key);
        if (val != null) {
            try {
                return Long.parseLong(val);
            } catch (NumberFormatException e) {
                LOG.warn("Value of '" + key + "' is not a long. "
                        + "Using specified default value '" + defaultValue + "'", e);
            }
        }
        return defaultValue;
    }
    
    /**
     * Get <code>int</code> value of key property.
     * 
     * @param key Key to search for.
     * @return Value of key property.
     * @throws NumberFormatException If value is not an <code>int</code>.
     */
    public int getInt(final String key) throws NumberFormatException {
        if (narvaroProperties == null) {
            loadNarvaroProperties();
        }
        return Integer.parseInt(this.narvaroProperties.getProperty(key));
    }
    
    /**
     * Get <code>int</code> value of key property. If the property
     * doesn't exist, the <code>defaultValue</code> will be returned.
     * 
     * @param key Key to search for.
     * @param defaultValue The value returned if the property doesn't exist.
     * @return Value of key property.
     */
    public int getInt(final String key, final int defaultValue) {
        if (narvaroProperties == null) {
            loadNarvaroProperties();
        }
        String val = this.narvaroProperties.getProperty(key);
        if (val != null) {
            try {
                return Integer.parseInt(val);
            } catch (NumberFormatException e) {
                LOG.warn("Value of '" + key + "' is not an integer. "
                        + "Using specified default value '" + defaultValue + "'", e);
            }
        }
        return defaultValue;
    }
    
    /**
     * Get <code>boolean</code> value of key property.
     * 
     * @param key Key to search for.
     * @return Value of key property.
     */
    public boolean getBoolean(final String key) {
        if (narvaroProperties == null) {
            loadNarvaroProperties();
        }
        return Boolean.parseBoolean(this.narvaroProperties.getProperty(key));
    }
    
    /**
     * Get <code>boolean</code> value of key property. If the property
     * doesn't exist, the <code>defaultValue</code> will be returned.
     * 
     * @param key Key to search for.
     * @param defaultValue The value returned if the property doesn't exist.
     * @return Value of key property.
     */
    public boolean getBoolean(final String key, final boolean defaultValue) {
        if (narvaroProperties == null) {
            loadNarvaroProperties();
        }
        String val = this.narvaroProperties.getProperty(key);
        if (val != null) {
            return Boolean.parseBoolean(val);
        } else {
            return defaultValue;
        }
    }
    
    /**
     * Get <code>String</code> value of key property.
     * 
     * @param key Key to search for.
     * @return Value of key property.
     */
    public String getString(final String key) {
        if (narvaroProperties == null) {
            loadNarvaroProperties();
        }
        return this.narvaroProperties.getProperty(key);
    }
    
    /**
     * Get <code>String</code> value of a key property. If the property
     * doesn't exist, the <code>defaultValue</code> will be returned.
     * 
     * @param key The key of the property to return.
     * @param defaultValue The value returned if the property doesn't exist.
     * @return The property value specified by the key.
     */
    public String getString(final String key, final String defaultValue) {
        if (narvaroProperties == null) {
            loadNarvaroProperties();
        }
        String val = getString(key);
        if (val != null) {
            return val;
        } else {
            return defaultValue;
        }
    }
    
    /**
     * @return <code>HashTable</code> of all properties.
     */
    public Properties getProperties() {
        if (narvaroProperties == null) {
            loadNarvaroProperties();
        }
        return this.narvaroProperties;
    }
    
    /**
     * @return The property file.
     */
    public File getPropertyFile() {
        return Paths.get(this.propertiesFile).toFile();
    }
    
    /**
     * Loads local properties file into memory.
     */
    private Properties loadproperties(final String propPath) {
        Properties props = new Properties();
        FileInputStream in = null;
        try {
            in = new FileInputStream(propPath);
            props.load(in);
        } catch (FileNotFoundException e) {
            LOG.error("File Not Found: " + propPath, e);
        } catch (IOException e) {
            LOG.error("Unable To Read File: " + propPath, e);
        } finally {
            try {
                in.close();
            } catch (IOException e) {
                // don't let this cause an issue
                LOG.warn("Error While Closing FileInputStream.", e);
            }
        }
        return props;
    }

}