/**
 * Narvaro: @VERSION@
 * Build Date: @DATE@
 * Commit Head: @HEAD@
 * JDK: @JDK@
 * ANT: @ANT@
 *
 */

package edu.csus.ecs.moneybeets.narvaro.database;

import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.log4j.Logger;

import edu.csus.ecs.moneybeets.narvaro.util.ClassUtils;

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
public abstract class DefaultConnectionManager {
    
    public DefaultConnectionManager() {
    }
    
    abstract public Logger getLogger();
    
    private ConnectionProvider connectionProvider;
    private final Object providerLock = new Object();
    
    // true if the database supports transactions.
    private boolean transactionsSupported;
    // true if the database requires large text fields to be streamed.
    private boolean streamTextRequired;
    // true if the database supports the Statement.setMaxRows() method
    private boolean maxRowsSupported;
    // true if the database supports rs.setFetchSize() method
    private boolean fetchSizeSupported;
    // true if the database supports correlated subqueries
    private boolean subqueriesSupported;
    // true if the database supports scroll-insensitive results
    private boolean scrollResultsSupported;
    // true if the database supports batch updates
    private boolean batchUpdatesSupported;
    // true if the database supports Statement.setFetchSize() method
    private boolean pstmtFetchSizeSupported = true;
    
    private DatabaseType databaseType = DatabaseType.unknown;
    
    private static SchemaManager schemaManager = new SchemaManager();
    
    private String poolName;
    
    private String connectionProviderClassName;
    
    public void setConnectionProviderClassName(final String connectionProviderClassName) {
        this.connectionProviderClassName = connectionProviderClassName;
    }
    
    public String getConnectionProviderClassName() {
        return connectionProviderClassName;
    }
    
    abstract public DefaultConnectionProvider getNewConnectionProvider();
    
    public Connection getConnection() throws SQLException {
        if (connectionProvider == null) {
            synchronized (providerLock) {
                if (connectionProvider == null) {
                    // attempt to load the connection provider classname
                    String className = getConnectionProviderClassName();
                    if (!"".equals(connectionProviderClassName) || connectionProviderClassName != null) {
                        // attempt to load the class
                        try {
                            Class<?> conClass = ClassUtils.forName(className);
                            setConnectionProvider((ConnectionProvider)conClass.newInstance());
                        } catch (Exception e) {
                            getLogger().warn("Failed to create the connection provider specified "
                                    + "by connectionProviderClassName. Using the default instance.", e);
                            setConnectionProvider(getNewConnectionProvider());
                        }
                    } else {
                        setConnectionProvider(getNewConnectionProvider());
                    }
                }
            }
        }
        
        Integer retryCount = 0;
        Integer retryMax = 10;
        Integer retryWait = 250; // milliseconds
        Connection con = null;
        SQLException lastException = null;
        do {
            try {
                con = getConnectionProvider().getConnection();
                if (con != null) {
                    // got one, let's hand it off.
                    return con;
                }
            } catch (SQLException e) {
                lastException = e;
                getLogger().info("Unable to get a connection from the database pool (attempt " 
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
        throw new SQLException("DefaultConnectionManager.getConnection() failed to obtain a connection after " 
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
        if (isTransactionsSupported()) {
            con.setAutoCommit(false);
        }
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
        if (isTransactionsSupported()) {
            try {
                if (abortTransaction) {
                    con.rollback();
                } else {
                    con.commit();
                }
            } catch (Exception e) {
                getLogger().error(e.getMessage(), e);
            }
            // reset the connection to auto-commit mode.
            try {
                con.setAutoCommit(true);
            } catch (Exception e) {
                getLogger().error(e.getMessage(), e);
            }
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
                getLogger().error(e.getMessage(), e);
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
                getLogger().error(e.getMessage(), e);
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
        closeResultSet(rs);
        closeStatement(stmt);
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
        closeStatement(stmt);
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
        if (con != null) {
            try {
                con.close();
            } catch (Exception e) {
                getLogger().error(e.getMessage(), e);
            }
        }
    }
    
    /**
     * Creates a scroll insensitive PreparedStatement if the JDBC driver supports it, or
     * a normal PreparedStatement otherwise.
     * 
     * @param con The database connection.
     * @param sql The SQL to create the PreparedStatement with.
     * @return A PreparedStatement.
     * @throws SQLException If an error occurs.
     */
    public PreparedStatement createScrollablePreparedStatement(
            final Connection con, final String sql) throws SQLException {
        if (isScrollResultsSupported()) {
            return con.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
        } else {
            return con.prepareStatement(sql);
        }
    }
    
    /**
     * Scrolls forward in a result set the specified number of rows. If the JDBC driver
     * supports the feature, the cursor will be moved directly. Otherwise, we scroll through
     * the results one by one manually calling <code>rs.next()</code>.
     * 
     * @param rs The ResultSet object to scroll.
     * @param rowNumber The row number to scroll forward to.
     * @throws SQLException If an error occurs.
     */
    public void scrollResultSet(final ResultSet rs, final int rowNumber) throws SQLException {
        // if the driver supports scrollable result sets, use that feature
        if (isScrollResultsSupported()) {
            if (rowNumber > 0) {
                // We will attempt to do a relative fetch. This may fail in SQL Server
                // if <resultset-navigation-strategy> is set to absolute. It would need to
                // be set to looping to work correctly.
                // If so, manually scroll to the correct row.
                try {
                    rs.setFetchDirection(ResultSet.FETCH_FORWARD);
                    rs.relative(rowNumber);
                } catch (SQLException e) {
                    getLogger().error("Error in JDBC method rs.relative(rowNumber)", e);
                    getLogger().error("Disabling JDBC method rs.relative(rowNumber)");
                    setScrollResultsSupported(false);
                    for (int i = 0; i < rowNumber; i++) {
                        rs.next();
                    }
                }
            }
        } else {
            // otherwise, manually scroll to the correct row
            for (int i = 0; i < rowNumber; i++) {
                rs.next();
            }
        }
    }
    
    /**
     * Limits the number of the results in a result set (to startIndex + numResults).
     * Sets the fetch size depending on the features of the JDBC driver and makes
     * sure that the size is not bigger than 500.
     * 
     * @param ps The PreparedStatement.
     * @param startIndex The first row with interesting data.
     * @param numResults The number of interesting results.
     */
    public void limitRowsAndFetchSize(final PreparedStatement ps, final int startIndex, final int numResults) {
        final int MAX_FETCHRESULTS = 500;
        final int maxRows = startIndex + numResults;
        setMaxRows(ps, maxRows);
        if (isPstmtFetchSizeSupported()) {
            if (isScrollResultsSupported()) {
                setFetchSize(ps, Math.min(MAX_FETCHRESULTS, numResults));
            } else {
                setFetchSize(ps, Math.min(MAX_FETCHRESULTS, maxRows));
            }
        }
    }
    
    /**
     * Sets the number of rows that the JDBC driver should buffer at a time.
     * The operation is automatically bypassed if Narvaro knows that the
     * JDBC driver or database doesn't support it.
     * 
     * @param ps The PreparedStatement to set the fetch size for.
     * @param fetchSize The fetch size.
     */
    public void setFetchSize(final PreparedStatement ps, final int fetchSize) {
        if (isPstmtFetchSizeSupported()) {
            try {
                ps.setFetchSize(fetchSize);
            } catch (Throwable t) {
                // Ignore. Error may happen if the driver doesn't support
                // this operation and we didn't set meta-data correctly.
                // However, it is a good idea to update the meta-data
                // so that we don't have to incur the cost of catching
                // an error each time.
                getLogger().error("Error in JDBC method ps.setFetchSize(fetchSize).", t);
                getLogger().error("Disabling JDBC method ps.setFetchSize(fetchSize).");
                setPstmtFetchSizeSupported(false);
            }
        }
    }
    
    /**
     * Sets the number of rows that the JDBC driver should buffer at a time.
     * The operation is automatically bypassed if Narvaro knows that the
     * JDBC driver or database doesn't support it.
     * 
     * @param rs The ResultSet to set the fetch size for.
     * @param fetchSize The fetch size.
     */
    public void setFetchSize(final ResultSet rs, final int fetchSize) {
        if (isFetchSizeSupported()) {
            try {
                rs.setFetchSize(fetchSize);
            } catch (Throwable t) {
                // Ignore. Error may happen if the driver doesn't support
                // this operation and we didn't set meta-data correctly.
                // However, it is a good idea to update the meta-data so
                // that we don't have to incur the cost of catching an
                // error each time.
                getLogger().error("Error in JDBC method rs.setFetchSize(fetchSize).", t);
                getLogger().error("Disabling JDBC method rs.setFetchSize(fetchSize).");
                setFetchSizeSupported(false);
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
     * Sets the connection provider. The old provider (if it exists) is
     * shut down before the new one is started. A connection provider
     * <b>should not</b> be started before being passed to the connection
     * manager because the manager will call the start() method automatically.
     * 
     * @param provider The ConnectionProvider that the manager should obtain connections from.
     */
    public void setConnectionProvider(final ConnectionProvider provider) {
        synchronized(providerLock) {
            if (connectionProvider != null) {
                connectionProvider.destroy();
                connectionProvider = null;
            }
            connectionProvider = provider;
            connectionProvider.start();
            // Now, get a connection to determine meta data.
            Connection con = null;
            try {
                con = connectionProvider.getConnection();
                setMetaData(con);
                
                // Check to see if the database needs to be upgraded.
                schemaManager.checkNarvaroSchema(con);
            } catch (Exception e) {
                getLogger().error(e.getMessage(), e);
            } finally {
                closeConnection(con);
            }
        }
    }
    
    /**
     * Destroys the current connection provider. Future calls to
     * {@link #getConnectionProvider()} will return <code>null</code>
     * until a new ConnectionProvider is set, or one is automatically loaded
     * by a call to {@link #getConnection()}.
     */
    public void destroyConnectionProvider() {
        synchronized(providerLock) {
            if (connectionProvider != null) {
                connectionProvider.destroy();
                connectionProvider = null;
            }
        }
    }
    
    /**
     * Retrieves a large text column from a result set, automatically performing
     * streaming if the JDBC driver requires it. This is necessary because different
     * JDBC drivers have different capabilities and methods for retrieving large
     * text values.
     * 
     * @param rs The ResultSet to retrieve the text from.
     * @param columnIndex The column in the ResultSet of the text field.
     * @return The String value of the text field.
     * @throws SQLException If an SQL Exception occurs.
     */
    public String getLargeTextField(final ResultSet rs, final int columnIndex) throws SQLException {
        if (isStreamTextRequired()) {
            Reader bodyReader = null;
            String value = null;
            try {
                bodyReader = rs.getCharacterStream(columnIndex);
                if (bodyReader == null) {
                    return null;
                }
                char[] buf = new char[256];
                int len;
                StringWriter out = new StringWriter(256);
                while ((len = bodyReader.read(buf)) >= 0) {
                    out.write(buf, 0, len);
                }
                value = out.toString();
                out.close();
            } catch (Exception e) {
                getLogger().error(e.getMessage(), e);
                throw new SQLException("Failed to load text field");
            } finally {
                try {
                    if (bodyReader != null) {
                        bodyReader.close();
                    }
                } catch (Exception e) {
                    // ignore
                }
            }
            return value;
        } else {
            return rs.getString(columnIndex);
        }
    }
    
    /**
     * Sets a large text column in a result set, automatically performing
     * streaming if the JDBC driver requires it. This is necessary because
     * different JDBC drivers have different capabilities and methods for
     * setting large text values.
     * 
     * @param ps The PreparedStatement to set the text field in.
     * @param parameterIndex The index corresponding to the text field.
     * @param value The String to set.
     * @throws SQLException If an SQL exception occurs.
     */
    public void setLargeTextField(final PreparedStatement ps,
            final int parameterIndex, final String value) throws SQLException {
        if (isStreamTextRequired()) {
            Reader bodyReader;
            try {
                bodyReader = new StringReader(value);
                ps.setCharacterStream(parameterIndex, bodyReader, value.length());
            } catch (Exception e) {
                getLogger().error(e.getMessage(), e);
                throw new SQLException("Failed to set text field");
            }
            // Leave bodyReader open so that the db can read from it. It *should*
            // be garbage collected after it's done without needing to call close().
        } else {
            ps.setString(parameterIndex, value);
        }
    }
    
    /**
     * Sets the max number of rows that should be returned from executing a
     * statement. The operation is automatically bypassed if Narvaro
     * knows that the JDBC driver or database doesn't support it.
     * 
     * @param stmt The statement to set the max number of rows for.
     * @param maxRows The max number of rows to return.
     */
    public void setMaxRows(final Statement stmt, final int maxRows) {
        if (isMaxRowsSupported()) {
            try {
                stmt.setMaxRows(maxRows);
            } catch (Throwable t) {
                // Ignore. Error may happen if the driver doesn't support
                // this operation and we didn't set meta-data correctly.
                // However, it is a good idea to update the meta-data so
                // that we don't have to incur the cost of catching an
                // error each time.
                getLogger().error("Error in JDBC method stmt.setMaxRows(maxRows).", t);
                getLogger().error("Disabling JDBC method stmt.setMaxRows(maxRows).");
                setMaxRowsSupported(false);
            }
        }
    }
    
    /**
     * Returns a SchemaManager instance, which can be used to manage
     * the database schema information for Narvaro and plugins.
     * 
     * @return A SchemaManager instance.
     */
    public static SchemaManager getSchemaManager() {
        return schemaManager;
    }
    
    /**
     * Uses a connection from the database to set meta data information about
     * what different JDBC drivers and databases support.
     * 
     * @param con The connection.
     * @throws SQLException If an SQL Exception occurs.
     */
    private void setMetaData(final Connection con) throws SQLException {
        DatabaseMetaData metaData = con.getMetaData();
        // Supports transactions?
        setTransactionsSupported(metaData.supportsTransactions());
        // Supports subqueries?
        setSubqueriesSupported(metaData.supportsCorrelatedSubqueries());
        // Supports scroll insensitive result sets? Try/catch block is a
        // workaround for DB2 JDBC driver, which throws an exception
        // on the method call.
        try {
            setScrollResultsSupported(metaData.supportsResultSetType(ResultSet.TYPE_SCROLL_INSENSITIVE));
        } catch (Exception e) {
            setScrollResultsSupported(false);
        }
        // Supports batch updates
        setBatchUpdatesSupported(metaData.supportsBatchUpdates());
        
        // Set defaults for other meta properties
        setStreamTextRequired(false);
        setMaxRowsSupported(true);
        setFetchSizeSupported(true);
        
        // Get the database name so that we can perform meta data settings.
        String dbName = metaData.getDatabaseProductName().toLowerCase();
        String driverName = metaData.getDriverName().toLowerCase();
        
        // MySQL properties
        if (dbName.contains("mysql")) {
            setDatabaseType(DatabaseType.mysql);
            setTransactionsSupported(false); // TODO comment and test this, should be supported since 5.0
        }

    }
    
    /**
     * Sets the Database Pool Name.
     * 
     * @param name The Database Pool Name.
     */
    public void setPoolName(final String name) {
        poolName = name;
    }
    
    /**
     * Returns the Database Pool Name.
     * 
     * @return The Database Pool Name.
     */
    public String getPoolName() {
        return poolName;
    }
    
    /**
     * Returns the database type. The possible types are constants of the 
     * DatabaseType class. Any database that doesn't have it's own constant
     * falls into the "unknown" category.
     * 
     * @return The database type.
     */
    public DatabaseType getDatabaseType() {
        return databaseType;
    }
    private void setDatabaseType(final DatabaseType dbType) {
        databaseType = dbType;
    }
    
    public boolean isTransactionsSupported() {
        return transactionsSupported;
    }
    private void setTransactionsSupported(final boolean b) {
        transactionsSupported = b;
    }
    
    public boolean isStreamTextRequired() {
        return streamTextRequired;
    }
    private void setStreamTextRequired(final boolean b) {
        streamTextRequired = b;
    }
    
    public boolean isMaxRowsSupported() {
        return maxRowsSupported;
    }
    private void setMaxRowsSupported(final boolean b) {
        maxRowsSupported = b;
    }
    
    public boolean isFetchSizeSupported() {
        return fetchSizeSupported;
    }
    private void setFetchSizeSupported(final boolean b) {
        fetchSizeSupported = b;
    }
    
    public boolean isPstmtFetchSizeSupported() {
        return pstmtFetchSizeSupported;
    }
    private void setPstmtFetchSizeSupported(final boolean b) {
        pstmtFetchSizeSupported = b;
    }
    
    public boolean isSubqueriesSupported() {
        return subqueriesSupported;
    }
    private void setSubqueriesSupported(final boolean b) {
        subqueriesSupported = b;
    }
    
    public boolean isScrollResultsSupported() {
        return scrollResultsSupported;
    }
    private void setScrollResultsSupported(final boolean b) {
        scrollResultsSupported = b;
    }
    
    public boolean isBatchUpdatesSupported() {
        return batchUpdatesSupported;
    }
    private void setBatchUpdatesSupported(final boolean b) {
        batchUpdatesSupported = b;
    }
    
    public static String getTestSQL(final String url) {
        return "SELECT 1";
    }
}
