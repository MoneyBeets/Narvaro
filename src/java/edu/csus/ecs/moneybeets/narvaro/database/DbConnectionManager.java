/**
 * Narvaro: @VERSION@
 * Build Date: @DATE@
 * Commit Head: @HEAD@
 * JDK: @JDK@
 * ANT: @ANT@
 *
 */

package edu.csus.ecs.moneybeets.narvaro.database;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.log4j.Logger;

/**
 * Core database connection manager.
 */
public enum DbConnectionManager {

    Narvaro,
    ;
    
    private DbConnectionManager() {
        connectionManagers = new ConcurrentHashMap<String, DefaultConnectionManager>();
    }
    
    private static final Logger LOG = Logger.getLogger(DbConnectionManager.class.getName());
    
    private Map<String, DefaultConnectionManager> connectionManagers;
    
    /**
     * Registers a DefaultConnectionManager to be managed by
     * this DbConnectionManager.
     * 
     * <p>
     * A unique name should be provided with each registered
     * DefaultConnectionManager.
     * </p>
     * 
     * @param name The name of the DefaultConnectionManager instance.
     * @param connectionManager The DefaultConnectionManager to be managed.
     */
    public void registerConnectionManager(final String name, final DefaultConnectionManager connectionManager) {
        LOG.debug("DbConnectionManager: Registering DefaultConnectionManager " + name);
        connectionManagers.put(name, connectionManager);
    }
    
    /**
     * Unregisters a DefaultConnectionManager from this manager.
     * 
     * <p>
     * This method call will destroy the underlying ConnectionProvider
     * resulting in any open connections managed by this DefaultConnectionManager
     * being terminated.
     * </p>
     * 
     * @param name The DefaultConnectionManager to unregister.
     */
    public void unregisterConnectionManager(final String name) {
        LOG.debug("DbConnectionManager: Unregistering DefaultConnectionManager " + name);
        connectionManagers.get(name).destroyConnectionProvider();
        connectionManagers.remove(name);
    }
    
    /**
     * Returns a database connection from the specified manager by name.
     * 
     * @param poolName The manager name to return a database connection from.
     * @return A connection to the database managed by the named manager.
     * @throws SQLException If an error occurs.
     */
    public Connection getConnection(final String poolName) throws SQLException {
        LOG.debug("DbConnectionManager: Obtaining connection from manager '" + poolName + "'");
        return connectionManagers.get(poolName).getConnection();
    }
    
    /**
     * Returns a Connection from the currently active connection provider that
     * is ready to participate in transactions (auto commit is set to false).
     * 
     * @return A connection with transactions enabled.
     * @throws SQLException If a SQL exception occurs.
     */
    public Connection getTransactionConnection(final String poolName) throws SQLException {
        return connectionManagers.get(poolName).getTransactionConnection();
    }
    
    /**
     * Closes a PreparedStatement and Connection. However, it firsts rolls back the transaction or
     * commits it depending on the value of the <code>abortTransaction</code>.
     * 
     * @param ps The prepared statement to close.
     * @param con The connection to close.
     * @param abortTransaction True if the transaction should be rolled back.
     */
    public void closeTransactionConnection(final PreparedStatement ps, 
                        final Connection con, final boolean abortTransaction) {
        closeStatement(ps);
        closeTransactionConnection(con, abortTransaction);
    }
    
    /**
     * Closes a Connection. However, it first rolls back the transaction or
     * commits it depending on the value of the <code>abortTransaction</code>.
     * 
     * @param con The connection to close.
     * @param abortTransaction True if the transaction should be rolled back.
     */
    public void closeTransactionConnection(final Connection con, final boolean abortTransaction) {
        getConnectionManager(con.getPoolName()).closeTransactionConnection(con, abortTransaction);
    }
    
    /**
     * Closes a result set. This method should be called within the finally section of
     * your database logic, as in the following example:
     * 
     * <pre>
     * public void doSomething(final Connection con) {
     *     ResultSet rs = null;
     *     PreparedStatement ps = null;
     *     try {
     *         ps = con.prepareStatement("SELECT foo FROM bar");
     *         rs = ps.executeQuery();
     *         ....
     *     } catch (SQLException e) {
     *         LOG.error(e.getMessage(), e);
     *     } finally {
     *         ConnectionManager.closeResultSet(rs);
     *         ConnectionManager.closeStatement(ps);
     *     }
     * }
     * </pre>
     * 
     * @param rs The result set to close.
     */
    public void closeResultSet(final ResultSet rs) {
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException e) {
                LOG.error(e.getMessage(), e);
            }
        }
    }
    
    /**
     * Closes a statement. This method should be called within the finally section of
     * your database logic, as in the following example:
     * 
     * <pre>
     * public void doSomething(final Connection con) {
     *     PreparedStatement ps = null;
     *     try {
     *         ps = con.prepareStatement("SELECT foo FROM bar");
     *         ....
     *     } catch (SQLException e) {
     *         LOG.error(e.getMessage(), e);
     *     } finally {
     *         ConnectionManager.closeStatement(ps);
     *     }
     * }
     * </pre>
     * 
     * @param stmt The statement.
     */
    public void closeStatement(final Statement stmt) {
        if (stmt != null) {
            try {
                stmt.close();
            } catch (Exception e) {
                LOG.error(e.getMessage(), e);
            }
        }
    }
    
    /**
     * Closes a statement and result set. This method should be called within the finally
     * section of your database logic, as in the following example:
     * 
     * <pre>
     * public void doSomething(final Connection con) {
     *     PreparedStatement ps = null;
     *     ResultSet rs = null;
     *     try {
     *         ps = con.prepareStatement("SELECT foo FROM bar");
     *         rs = ...
     *         ....
     *     } catch (SQLException e) {
     *         LOG.error(e.getMessage(), e);
     *     } finally {
     *         ConnectionManager.closeStatement(rs, ps);
     *     }
     * }
     * </pre>
     * 
     * @param rs The result set.
     * @param stmt The statement.
     */
    public void closeStatement(final ResultSet rs, final Statement stmt) {
        closeResultSet(rs);
        closeStatement(stmt);
    }
    
    /**
     * Closes a statement. This method should be called within the try section of
     * you database logic when you reuse a statement. It may throw an exception,
     * so don't place it in the finally section. Example:
     * 
     * <pre>
     * public void doSomething(final Connection con) {
     *     PreparedStatement ps = null;
     *     try {
     *         ps = con.prepareStatement("SELECT foo FROM bar");
     *         ps.execute();
     *         ....
     *         <b>ConnectionManager.fastCloseStatement(ps);</b>
     *         ps = con.prepareStatement("SELECT * FROM blah");
     *         ....
     *     }
     *     ....
     * }
     * </pre>
     * 
     * @param ps The statement to close.
     * @throws SQLException If an exception occurs while closing the statement.
     */
    public void fastCloseStatement(final PreparedStatement ps) throws SQLException {
        ps.close();
    }
    
    /**
     * Closes a statement and a result set. This method should be called within
     * the try section of you database logic when you reuse a statement. It may
     * throw an exception, so don't place it in the finally section. Example:
     * 
     * <pre>
     * public void doSomething(final Connection con) {
     *     PreparedStatement ps = null;
     *     ResultSet rs = null;
     *     try {
     *         ps = con.prepareStatement("SELECT foo FROM bar");
     *         rs = ps.executeQuery();
     *         ....
     *         <b>ConnectionManager.fastCloseStatement(rs, ps);</b>
     *         ps = con.prepareStatement("SELECT * FROM blah");
     *         ....
     *     }
     *     ....
     * }
     * </pre>
     * 
     * @param rs The result set to close.
     * @param ps The statement to close.
     * @throws SQLException If an exception occurs while closing the statement or result set.
     */
    public void fastCloseStatement(final ResultSet rs, final PreparedStatement ps) throws SQLException {
        rs.close();
        ps.close();
    }
    
    /**
     * Closes a result set, statement and database connection (returning the connection to
     * the connection pool). This method should be called within the finally section of
     * your database logic, as in the following example:
     * 
     * <pre>
     * Connection con = null;
     * PreparedStatement ps = null;
     * ResultSet rs = null;
     * try {
     *     con = ConnectionManager.getConnection();
     *     ps = con.prepareStatement("SELECT foo FROM bar");
     *     rs = ps.executeQuery();
     *     ....
     * } catch (SQLException e) {
     * 
     * }
     * </pre>
     * 
     * @param rs The result set.
     * @param stmt The statement.
     * @param con The connection.
     */
    public void closeConnection(final ResultSet rs, final Statement stmt, final Connection con) {
        getConnectionManager(con.getPoolName()).closeConnection(rs, stmt, con);
    }
    
    /**
     * Closes a statement and database connection (returning the connection to the
     * connection pool). This method should be called within the finally section of
     * your database logic, as in the following example:
     * 
     * <pre>
     * Connection con = null;
     * PreparedStatement ps = null;
     * try {
     *     con = ConnectionManager.getConnection();
     *     ps = con.prepareStatement("SELECT foo FROM bar");
     *     ....
     * } catch (SQLException e) {
     *     LOG.error(e.getMessage(), e);
     * } finally {
     *     ConnectionManager.closeConnection(ps, con);
     * }
     * </pre>
     * 
     * @param stmt The statement.
     * @param con The connection.
     */
    public void closeConnection(final Statement stmt, final Connection con) {
        getConnectionManager(con.getPoolName()).closeConnection(stmt, con);
    }
    
    /**
     * Closes a database connection (returning the connection to the connection pool).
     * Any statement associated with the connection should be closed before calling this method.
     * This method should be called within the finally section of your database logic, as
     * in the following example:
     * 
     * <pre>
     * Connection con = null;
     * try {
     *     con = ConnectionManager.getConnection();
     *     ....
     * } catch (SQLException e) {
     *     LOG.error(e.getMessage(), e);
     * } finally {
     *     ConnectionManager.closeConnection(con);
     * }
     * </pre>
     * 
     * @param con The connection.
     */
    public void closeConnection(final Connection con) {
        getConnectionManager(con.getPoolName()).closeConnection(con);
    }
    
    /**
     * Returns the DefaultConnectionManager specified by name.
     * 
     * @param name The name of the DefaultConnectionManager.
     * @return The DefaultConnectionManager.
     */
    public DefaultConnectionManager getConnectionManager(final String name) {
        return connectionManagers.get(name);
    }
    
    /**
     * Returns a Collection of all registered Connection Managers.
     * 
     * @return A Collection of all registered Connection Managers.
     */
    public Collection<DefaultConnectionManager> getConnectionManagers() {
        return Collections.unmodifiableCollection(connectionManagers.values());
    }
    
    /**
     * Returns a Collection of all registered pool names.
     * 
     * @return A Collection of all registered pool names.
     */
    public Collection<String> getPoolNames() {
        return Collections.unmodifiableCollection(connectionManagers.keySet());
    }
    
    /**
     * Terminates the DbConnectionManager.
     * 
     * <p>
     * The shutdown() method call will:
     * <ol>
     *   <li>Destroy all ConnectionProvider instances, which will terminate any open connections in the pool.</li>
     *   <li>Remove all DefaultConnectionManager instances.</li>
     * </ol>
     * </p>
     */
    public void shutdown() {
        for (DefaultConnectionManager manager : connectionManagers.values()) {
            manager.destroyConnectionProvider();
        }
        connectionManagers.clear();
        connectionManagers = null;
    }
    
}
