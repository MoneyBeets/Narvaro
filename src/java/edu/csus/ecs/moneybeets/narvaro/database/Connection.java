/**
 * Narvaro: @VERSION@
 * Build Date: @DATE@
 * Commit Head: @HEAD@
 * JDK: @JDK@
 * ANT: @ANT@
 *
 */

package edu.csus.ecs.moneybeets.narvaro.database;

/**
 * Custom Database Connection object.
 */
public class Connection extends AbstractConnection {

    private String poolName;
    private DatabaseType dbType;
    
    /**
     * Creates a new Connection that wraps the specified java.sql.Connection.
     * 
     * @param connection The java.sql.Connection to wrap.
     * @param poolName The database pool name this connection originated from.
     * @param dbType The database type of this pooled connection.
     */
    public Connection(final java.sql.Connection connection, 
            final String poolName, final DatabaseType dbType) {
        super(connection);
        setPoolName(poolName);
        setDatabaseType(dbType);
    }
    
    /**
     * 
     * 
     * @param poolName
     */
    public void setPoolName(final String poolName) {
        this.poolName = poolName;
    }
    
    public String getPoolName() {
        return poolName;
    }
    
    public void setDatabaseType(final DatabaseType dbType) {
        this.dbType = dbType;
    }
    
    public DatabaseType getDatabaseType() {
        return dbType;
    }

}
