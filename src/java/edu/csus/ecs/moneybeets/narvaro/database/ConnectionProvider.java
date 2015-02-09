/**
 * Narvaro: @VERSION@
 * Build Date: @DATE@
 * Commit Head: @HEAD@
 * JDK: @JDK@
 * ANT: @ANT@
 *
 */

package edu.csus.ecs.moneybeets.narvaro.database;

import java.sql.SQLException;

/**
 * Interface that defines the connection provider framework.
 * Implementations of this interface will make connection to
 * actual data sources.
 * <p>
 * It is expected that subclasses be a JavaBean, so that properties
 * of the connection provider are exposed through bean introspection.
 * </p>
 * 
 */
public interface ConnectionProvider {

    /**
     * Returns true if this connection provider provides connections
     * out of the connection pool. Implementing and using connection providers
     * that are pooled is strongly recommended, as they greatly increase the
     * speed of Narvaro.
     * 
     * @return True if the Connection objects returned by this provider are pooled.
     */
    public boolean isPooled();
    
    /**
     * Returns a database connection. While a Narvaro component is
     * done with a connection, it will call the close() method of that connection.
     * Therefore, connection pools with special release methods are not directly
     * supported by the connection provider infrastructure. Instead, connections
     * from those pools should be wrapped such that calling the close method on the
     * wrapper class will release the connection from the pool.
     * 
     * @return A Connection object.
     * @throws SQLException If an SQL error occurred while retrieving the connection.
     */
    public Connection getConnection() throws SQLException;
    
    /**
     * Starts the connection provider. For some connection providers, this
     * will be a no-op. However, connection provider users should always call
     * this method to make sure the connection provider is started.
     */
    public void start();
    
    /**
     * This method should be called whenever properties have been changed so
     * that the changes will take effect.
     */
    public void restart();
    
    /**
     * Tells the connection provider to destroy itself. For many connection
     * providers, this will essentially result in a no-op. However, connection
     * providers users should always call this method when changing from one
     * connection provider to another to ensure that there are no dangling
     * database connections.
     */
    public void destroy();
    
}
