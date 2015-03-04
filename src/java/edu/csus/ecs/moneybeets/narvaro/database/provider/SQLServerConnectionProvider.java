/**
 * Narvaro: @VERSION@
 * Build Date: @DATE@
 * Commit Head: @HEAD@
 * JDK: @JDK@
 * ANT: @ANT@
 *
 */

package edu.csus.ecs.moneybeets.narvaro.database.provider;

import org.apache.log4j.Logger;

import edu.csus.ecs.moneybeets.narvaro.database.AbstractConnectionProvider;
import edu.csus.ecs.moneybeets.narvaro.database.DatabaseType;
import edu.csus.ecs.moneybeets.narvaro.util.ConfigurationManager;

/**
 * Narvaro SQL Server Connection Provider.
 */
public class SQLServerConnectionProvider extends AbstractConnectionProvider {
    
    private static final Logger LOG = Logger.getLogger(SQLServerConnectionProvider.class.getName());
    
    @Override
    public Logger getLogger() {
        return LOG;
    }
    
    /**
     * Load properties into memory using setters/getters
     */
    @Override
    protected void loadProperties() {
        setDatabaseType(DatabaseType.sqlserver);
        setServerURL(
                ConfigurationManager.NARVARO.getString("narvaro.connectionprovider.serverurl"));
        setUsername(
                ConfigurationManager.NARVARO.getString("narvaro.connectionprovider.username"));
        setPassword(
                ConfigurationManager.NARVARO.getString("narvaro.connectionprovider.password"));
        setTestSQL(
                ConfigurationManager.NARVARO.getString("narvaro.connectionprovider.testSql"));
        setIdleTestInterval(
                ConfigurationManager.NARVARO.getInt("narvaro.connectionprovider.idleTestInterval", 900));
        setTestBeforeUse(
                ConfigurationManager.NARVARO.getBoolean("narvaro.connectionprovider.testBeforeUse", false));
        setTestAfterUse(
                ConfigurationManager.NARVARO.getBoolean("narvaro.connectionprovider.testAfterUse", false));
        setMinConnections(
                ConfigurationManager.NARVARO.getInt("narvaro.connectionprovider.minConnections", 3));
        setMaxConnections(
                ConfigurationManager.NARVARO.getInt("narvaro.connectionprovider.maxConnections", 25));
        setMaxIdleTimeExcessConnections(
                ConfigurationManager.NARVARO.getInt("narvaro.connectionprovider.maxIdleTimeExcessConnections", 900));
        setConnectionTimeout(
                ConfigurationManager.NARVARO.getInt("narvaro.connectionprovider.connectionTimeout", 43200));
    }
    
    @Override
    public String toString() {
        return ConfigurationManager.NARVARO.getString("narvaro.connectionprovider.username") 
                + "@" + ConfigurationManager.NARVARO.getString("narvaro.connectionprovider.serverurl");
    }

}
