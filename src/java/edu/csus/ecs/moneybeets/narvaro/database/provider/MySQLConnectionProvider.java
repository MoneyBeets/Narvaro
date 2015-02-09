/**
 * Narvaro: @VERSION@
 * Build Date: @DATE@
 * Commit Head: @HEAD@
 * JDK: @JDK@
 * ANT: @ANT@
 *
 */

package edu.csus.ecs.moneybeets.narvaro.database.provider;

import java.sql.SQLException;

import org.apache.log4j.Logger;

import edu.csus.ecs.moneybeets.narvaro.database.DatabaseType;
import edu.csus.ecs.moneybeets.narvaro.database.DefaultConnectionManager;
import edu.csus.ecs.moneybeets.narvaro.database.DefaultConnectionProvider;
import edu.csus.ecs.moneybeets.narvaro.util.ConfigurationManager;

/**
 * MySQL Database connection provider.
 *
 */
public class MySQLConnectionProvider extends DefaultConnectionProvider {

    private static final Logger LOG = Logger.getLogger(MySQLConnectionProvider.class.getName());
    
    @Override
    public Logger getLogger() {
        return LOG;
    }

    @Override
    protected void loadProperties() {
        setPoolName(
                ConfigurationManager.NARVARO.getString("narvaro.connectionprovider.mysql.poolname"));
        setDatabaseType(DatabaseType.valueOf(ConfigurationManager.NARVARO.getString("narvaro.connectionprovider.mysql.dbname")));
        setServerURL(
                ConfigurationManager.NARVARO.getString("narvaro.connectionprovider.mysql.serverurl"));
        setUsername(
                ConfigurationManager.NARVARO.getString("narvaro.connectionprovider.mysql.username"));
        setPassword(
                ConfigurationManager.NARVARO.getString("narvaro.connectionprovider.mysql.password"));
        setTestSQL(
                ConfigurationManager.NARVARO.getString("narvaro.connectionprovider.mysql.testSql", DefaultConnectionManager.getTestSQL(getServerURL())));
        setIdleTestInterval(
                ConfigurationManager.NARVARO.getInt("narvaro.connectionprovider.mysql.idleTestInterval", 900));
        setTestBeforeUse(
                ConfigurationManager.NARVARO.getBoolean("narvaro.connectionprovider.mysql.testBeforeUse", false));
        setTestAfterUse(
                ConfigurationManager.NARVARO.getBoolean("narvaro.connectionprovider.mysql.testAfterUse", false));
        setMinConnections(
                ConfigurationManager.NARVARO.getInt("narvaro.connectionprovider.mysql.minConnections", 3));
        setMaxConnections(
                ConfigurationManager.NARVARO.getInt("narvaro.connectionprovider.mysql.maxConnections", 25));
        setMaxIdleTimeExcessConnections(
                ConfigurationManager.NARVARO.getInt("narvaro.connectionprovider.mysql.maxIdleTimeExcessConnections", 900));
        setConnectionTimeout(
                ConfigurationManager.NARVARO.getInt("narvaro.connectionprovider.mysql.connectionTimeout", 43200));
    }

    @Override
    public String toString() {
        try {
            return getPooledDataSource().getMinPoolSize() + ","
                    + getPooledDataSource().getMaxPoolSize() + ","
                    + getPooledDataSource().getNumIdleConnectionsDefaultUser() + ","
                    + getPooledDataSource().getNumBusyConnectionsDefaultUser();
        } catch (SQLException e) {
            return "MySQL Connection Provider";
        }
    }

}
