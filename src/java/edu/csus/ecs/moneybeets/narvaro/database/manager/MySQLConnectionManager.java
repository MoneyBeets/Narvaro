/**
 * Narvaro: @VERSION@
 * Build Date: @DATE@
 * Commit Head: @HEAD@
 * JDK: @JDK@
 * ANT: @ANT@
 *
 */

package edu.csus.ecs.moneybeets.narvaro.database.manager;

import org.apache.log4j.Logger;

import edu.csus.ecs.moneybeets.narvaro.database.DefaultConnectionManager;
import edu.csus.ecs.moneybeets.narvaro.database.DefaultConnectionProvider;
import edu.csus.ecs.moneybeets.narvaro.database.provider.MySQLConnectionProvider;
import edu.csus.ecs.moneybeets.narvaro.util.ConfigurationManager;

/**
 * MySQL Database Connection Manager
 */
public class MySQLConnectionManager extends DefaultConnectionManager {

    private static final Logger LOG = Logger.getLogger(MySQLConnectionManager.class.getName());
    
    @Override
    public Logger getLogger() {
        return LOG;
    }
    
    public MySQLConnectionManager() {
        setConnectionProviderClassName(
                ConfigurationManager.NARVARO.getString("narvaro.connectionprovider.mysql.classname", 
                        "edu.csus.ecs.moneybeetsy.narvaro.database.provider.MySQLConnectionProvider"));
    }
    
    public MySQLConnectionManager(final String poolName) {
        setPoolName(poolName);
        setConnectionProviderClassName(
                ConfigurationManager.NARVARO.getString("narvaro.connectionprovider.mysql.classname", 
                        "edu.csus.ecs.moneybeetsy.narvaro.database.provider.MySQLConnectionProvider"));
    }

    @Override
    public DefaultConnectionProvider getNewConnectionProvider() {
        return new MySQLConnectionProvider();
    }
}
