/**
 * Narvaro: @VERSION@
 * Build Date: @DATE@
 * Commit Head: @HEAD@
 * JDK: @JDK@
 * ANT: @ANT@
 *
 */

package edu.csus.ecs.moneybeets.narvaro.database;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.log4j.Logger;

import edu.csus.ecs.moneybeets.narvaro.util.ConfigurationManager;

/**
 * Manages database schemas for Narvaro. The manager
 * uses the narvaroVersion database table to figure out which database schema is
 * currently installed and then attempts to automatically apply database schema changes
 * as necessary.
 * 
 * <p>
 * Running database schemas automatically requires appropriate database permissions.
 * Without those permissions, the automatic installation/upgrade process will fail
 * and users will be prompted to apply database changes manually.
 * </p>
 * 
 * @see DatabaseManager#getSchemaManager()
 *
 */
public class SchemaManager {
    
    private static final Logger LOG = Logger.getLogger(SchemaManager.class.getName());
    
    private static final String CHECK_VERSION = "SELECT version FROM jcVersion WHERE name = ?";
    
    private static final int DATABASE_VERSION = ConfigurationManager.NARVARO.getInt("narvaro.database.version", -1);
    
    /**
     * Checks the Narvaro database schema to ensure that it's installed and up to date.
     * If the schema isn't present or up to date, an automatic update will be attempted.
     * 
     * @param con The connection to the database.
     * @return True if the database schema checked out fine, or was automatically installed
     *              or updated successfully.
     */
    public boolean checkNarvaroSchema(final Connection con) {
        try {
            return checkSchema(con, "narvaro", DATABASE_VERSION, new ResourceLoader() {
                @Override
                public InputStream loadResource(final String resourceName) {
                    File file = new File(ConfigurationManager.NARVARO.getHomeDirectory() 
                            + File.separator + "resources" + File.separator + "database", resourceName);
                    try {
                        return new FileInputStream(file);
                    } catch (FileNotFoundException e) {
                        return null;
                    }
                }
            });
        } catch (Exception e) {
            LOG.error("Database update failed. Please manually upgrade your database.");
        }
        return false;
    }
    
    /**
     * Checks to see if the database needs to be upgraded. This method should
     * be called once every time the application starts up.
     * 
     * @param con The database connection to use to check the schema with.
     * @param schemaKey The database schema key (name).
     * @param requiredVersion The version that the schema should be at.
     * @param resourceLoader A resource loader that knows how to load schema files.
     * @return True if the schema update was successful.
     * @throws Exception If an error occured.
     */
    private boolean checkSchema(final Connection con, final String schemaKey, 
                        final int requiredVersion, final ResourceLoader resourceLoader) throws Exception {

        int currentVersion = -1;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = con.prepareStatement(CHECK_VERSION);
            ps.setString(1, schemaKey);
            rs = ps.executeQuery();
            if (rs.next()) {
                currentVersion = rs.getInt(1);
            }
        } catch (SQLException e) {
            // The database schema must not be installed.
            LOG.debug("SchemaManager: Error verifying " + schemaKey + " version, probably ignorable", e);
            DatabaseManager.Narvaro.closeStatement(rs, ps);
        } finally {
            DatabaseManager.Narvaro.closeStatement(rs, ps);
        }
        // If already up to date, return
        if (currentVersion >= requiredVersion) {
            return true;
        }
        // If the database schema isn't installed at all, we need to install it.
        else if (currentVersion == -1) {
            LOG.info("Missing database schema for " + schemaKey + ". Attempting to install...");
            // Resource will be like "/database/narvaro_mysql.sql"
            String resourceName = schemaKey + "_" + "mysql.sql";
            InputStream resource = resourceLoader.loadResource(resourceName);
            if (resource == null) {
                return false;
            }
            try {
                executeSQLScript(con, resource);
            } catch (Exception e) {
                LOG.error(e.getMessage(), e);
                return false;
            } finally {
                try {
                    resource.close();
                } catch (Exception e) {
                    // ignore
                }
            }
            LOG.info("Database update successful.");
            return true;
        }
        // Must have a version of the schema that needs to be upgraded.
        else {
            // The database is an old version that needs to be upgraded.
            LOG.info("Found old database version " + currentVersion + " for " 
                    + schemaKey + ". Upgrading to version " + requiredVersion + "...");
            
            // Run all upgrade scripts until we're up to the latest schema.
            for (int i = currentVersion  +1; i <= requiredVersion; i++) {
                InputStream resource = getUpgradeResource(resourceLoader, i, schemaKey);
                
                if (resource == null) {
                    continue;
                }
                try {
                    executeSQLScript(con, resource);
                } catch (Exception e) {
                    LOG.error(e.getMessage(), e);
                    return false;
                } finally {
                    try {
                        resource.close();
                    } catch (Exception e) {
                        // ignore
                    }
                }
            }
            LOG.info("Database update successful.");
            return true;
        }
    }
    
    private InputStream getUpgradeResource(final ResourceLoader resourceLoader, final int upgradeVersion, final String schemaKey) {
        InputStream resource = null;
        if ("narvaro".equals(schemaKey)) {
            // Resource will be like "/database/upgrade/3/narvaro_mysql.sql"
            String path = ConfigurationManager.NARVARO.getHomeDirectory() + File.separator 
                    + "resources" + File.separator + "database" + File.separator + "upgrade" + File.separator + upgradeVersion;
            String filename = schemaKey + "_" + "mysql.sql";
            File file = new File(path, filename);
            try {
                resource = new FileInputStream(file);
            } catch (FileNotFoundException e) {
                // if the resource is null, the specific upgrade number is not available.
            }
        }
        else {
            String resourceName = "upgrade" + File.separator + upgradeVersion + File.separator + schemaKey + "_" + "mysql.sql";
            resource = resourceLoader.loadResource(resourceName);
        }
        return resource;
    }
    
    /**
     * Executes a SQL script.
     * 
     * @param con Database connection.
     * @param resource An input stream for the script to execute.
     * @throws IOException If an IOException occurs.
     * @throws SQLException If an SQLException occurs.
     */
    private static void executeSQLScript(final Connection con, 
            final InputStream resource) throws IOException, SQLException {
        BufferedReader in = null;
        try {
            in = new BufferedReader(new InputStreamReader(resource));
            boolean done = false;
            while (!done) {
                StringBuilder command = new StringBuilder();
                while (true) {
                    String line = in.readLine();
                    if (line == null) {
                        done = true;
                        break;
                    }
                    // Ignore comments and blank lines.
                    if (isSQLCommandPart(line)) {
                        command.append(" ").append(line);
                    }
                    if (line.trim().endsWith(";")) {
                        break;
                    }
                }
                // Send command to database.
                if (!done && !"".equals(command.toString())) {
                    PreparedStatement ps = null;
                    try {
                        String cmdString = command.toString();
                        ps = con.prepareStatement(cmdString);
                        ps.execute();
                    } catch (SQLException e) {
                        // lets show what failed
                        LOG.error("SchemaManager: Failed to execute SQL:\n" + command.toString());
                        throw e;
                    } finally {
                       DatabaseManager.Narvaro.closeStatement(ps);
                    }
                }
            }
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (Exception e) {
                    LOG.error(e.getMessage(), e);
                }
            }
        }
    }
    
    /**
     * Returns true if a line from a SQL schema is a valid command part.
     * 
     * @param line The line of the schema.
     * @return True if a valid command part.
     */
    public static boolean isSQLCommandPart(String line) {
        line = line.trim();
        if ("".equals(line) || line == null) {
            return false;
        }
        // Check to see if the line is a comment. Valid comment types:
        // "--"
        // "#"
        // "/*"
        return !(line.startsWith("--") || line.startsWith("#") || line.startsWith("/*"));
    }
    
    private static abstract class ResourceLoader {
        public abstract InputStream loadResource(final String resourceName);
    }
    
}
