/**
 * Narvaro: @VERSION@
 * Build Date: @DATE@
 * Commit Head: @HEAD@
 * JDK: @JDK@
 * ANT: @ANT@
 *
 */

package edu.csus.ecs.moneybeets.narvaro.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.log4j.Logger;

import edu.csus.ecs.moneybeets.narvaro.database.provider.MySQLConnectionProvider;

/**
 * Central manager of database connections.
 * 
 * <p>
 * This manager also provides a set of utility methods
 * that abstract out operations that may not work on all databases
 * such as setting the max number of rows that a query should return.
 * </p>
 *
 */
public enum DatabaseManager {
    
    Narvaro,
    ;
    
    private DatabaseManager() {
    }
    
    private static final Logger LOG = Logger.getLogger(DatabaseManager.class.getName());
    
    private ConnectionProvider connectionProvider = new MySQLConnectionProvider();
    
    private static SchemaManager schemaManager = new SchemaManager();

    public Connection getConnection() throws SQLException {
        
        Integer retryCount = 0;
        Integer retryMax = 10;
        Integer retryWait = 250; // milliseconds
        Connection con = null;
        SQLException lastException = null;
        do {
            try {
                con = connectionProvider.getConnection();
                if (con != null) {
                    // got one, let's hand it off.
                    return con;
                }
            } catch (SQLException e) {
                lastException = e;
                LOG.info("Unable to get a connection from the database pool (attempt " 
                            + retryCount + " out of " + retryMax + ")", e);
            }
            
            try {
                Thread.sleep(retryWait);
            } catch (Exception e) {
                // ignore
            }
            retryCount++;
        } while (retryCount <= retryMax);
        
        // if we made it to here, it means we did not get a connection. throw an exception
        throw new SQLException("DatabaseManager.getConnection() failed to obtain a connection after " 
                        + retryCount + " retries. The exception from the last attept is as follows" + lastException);
    }
    
    /**
     * Returns a Connection from the currently active connection provider that
     * is ready to participate in transactions (auto commit is set to false).
     * 
     * @return A connection with transactions enabled.
     * @throws SQLException If a SQL exception occurs.
     */
    public Connection getTransactionConnection() throws SQLException {
        Connection con = getConnection();
        con.setAutoCommit(false);
        return con;
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
        // rollback or commit the transaction
        try {
            if (abortTransaction) {
                con.rollback();
            } else {
                con.commit();
            }
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
        }
        // reset the connection to auto-commit mode.
        try {
            con.setAutoCommit(true);
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
        }
        closeConnection(con);
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
     *         DatabaseManager.closeResultSet(rs);
     *         DatabaseManager.closeStatement(ps);
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
     *         DatabaseManager.closeStatement(ps);
     *     }
     * }
     * </pre>
     * 
     * @param stmt The statement.
     */
    public void closeStatement(final PreparedStatement ps) {
        if (ps != null) {
            try {
                ps.close();
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
     *         DatabaseManager.closeStatement(rs, ps);
     *     }
     * }
     * </pre>
     * 
     * @param rs The result set.
     * @param stmt The statement.
     */
    public void closeStatement(final ResultSet rs, final PreparedStatement ps) {
        closeResultSet(rs);
        closeStatement(ps);
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
     *         <b>DatabaseManager.fastCloseStatement(ps);</b>
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
     *         <b>DatabaseManager.fastCloseStatement(rs, ps);</b>
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
     *     con = DatabaseManager.getConnection();
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
    public void closeConnection(final ResultSet rs, final PreparedStatement ps, final Connection con) {
        closeResultSet(rs);
        closeStatement(ps);
        closeConnection(con);
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
     *     con = DatabaseManager.getConnection();
     *     ps = con.prepareStatement("SELECT foo FROM bar");
     *     ....
     * } catch (SQLException e) {
     *     LOG.error(e.getMessage(), e);
     * } finally {
     *     DatabaseManager.closeConnection(ps, con);
     * }
     * </pre>
     * 
     * @param stmt The statement.
     * @param con The connection.
     */
    public void closeConnection(final PreparedStatement ps, final Connection con) {
        closeStatement(ps);
        closeConnection(con);
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
     *     con = DatabaseManager.getConnection();
     *     ....
     * } catch (SQLException e) {
     *     LOG.error(e.getMessage(), e);
     * } finally {
     *     DatabaseManager.closeConnection(con);
     * }
     * </pre>
     * 
     * @param con The connection.
     */
    public void closeConnection(final Connection con) {
        if (con != null) {
            try {
                con.close();
            } catch (Exception e) {
                LOG.error(e.getMessage(), e);
            }
        }
    }
    
    /**
     * Returns the current connection provider. The only case in which
     * this method should be called is if more information about the current
     * connection provider is needed. Database connections should always be
     * obtained by calling the <code>getConnection()</code> method of this class.
     * 
     * @return The connection provider.
     */
    public ConnectionProvider getConnectionProvider() {
        return connectionProvider;
    }
    
    /**
     * Returns a SchemaManager instance, which can be used to manage
     * the database schema information for Narvaro.
     * 
     * @return A SchemaManager instance.
     */
    public static SchemaManager getSchemaManager() {
        return schemaManager;
    }
    
    /**
     * Terminates the DatabaseManager.
     * 
     * <p>
     * The shutdown() method call will:
     * <ol>
     *   <li>Destroy all ConnectionProvider instances, which will terminate any open connections in the pool.</li>
     * </ol>
     * </p>
     */
    public void shutdown() {
        connectionProvider.destroy();
    }
}
