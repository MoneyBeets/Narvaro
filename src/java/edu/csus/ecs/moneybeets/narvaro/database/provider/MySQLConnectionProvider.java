/**
 * Narvaro: @VERSION@
 * Build Date: @DATE@
 * Commit Head: @HEAD@
 * JDK: @JDK@
 * ANT: @ANT@
 *
 */

package edu.csus.ecs.moneybeets.narvaro.database.provider;

import java.sql.Connection;
import java.sql.SQLException;

import com.mchange.v2.c3p0.ComboPooledDataSource;

import edu.csus.ecs.moneybeets.narvaro.database.ConnectionProvider;
import edu.csus.ecs.moneybeets.narvaro.util.ConfigurationManager;

/**
 * Default Narvaro connection provider, which uses and internal connection pool.
 */
public class MySQLConnectionProvider implements ConnectionProvider {
    
    private ComboPooledDataSource cpds;
    
    private String serverURL;
    private String username;
    private String password;
    private int minConnections = 3;
    private int maxConnections = 10;
    private int idleTestInterval = 900; // 15 minutes in seconds
    private String testSQL = "";
    private Boolean testBeforeUse = true;
    private Boolean testAfterUse = true;
    
    /**
     * Maximum time an idle connection will remain open if the pool
     * has exceeded the minimum number of connections. (in seconds)
     */
    private int maxIdleTimeExcessConnections = 900; // 15 minutes in seconds
    
    /**
     * Maximum time a connection can be open before it's reopened (in seconds).
     */
    private int connectionTimeout = 43200; // 0.5 of a day
    
    /**
     * Creates a new DefaultConnectionProvider.
     */
    public MySQLConnectionProvider() {
        loadProperties();
    }
    
    @Override
    public boolean isPooled() {
        return true;
    }
    
    @Override
    public Connection getConnection() throws SQLException {
        return cpds.getConnection();
    }
    
    /**
     * Gets the ComboPooledDataSource used to create pooled connections.
     * 
     * @return The ComboPooledDataSource used to create pooled connections.
     */
    protected ComboPooledDataSource getPooledDataSource() {
        return cpds;
    }
    
    @Override
    public void start() {
        cpds = new ComboPooledDataSource();
        cpds.setJdbcUrl(getServerURL());
        cpds.setUser(getUsername());
        cpds.setPassword(getPassword());
        cpds.setMinPoolSize(getMinConnections());
        cpds.setMaxPoolSize(getMaxConnections());
        cpds.setIdleConnectionTestPeriod(getIdleTestInterval());
        cpds.setTestConnectionOnCheckout(getTestBeforeUse());
        cpds.setTestConnectionOnCheckin(getTestAfterUse());
        cpds.setPreferredTestQuery(getTestSQL());
        cpds.setMaxConnectionAge(getConnectionTimeout());
    }
    
    @Override
    public void restart() {
    }
    
    @Override
    public void destroy() {
        cpds.close();
    }
    
    /**
     * Returns the JDBC connection URL used to make database connections.
     * 
     * @return The JDBC connection URL.
     */
    public String getServerURL() {
        return serverURL;
    }
    
    /**
     * Sets the JDBC connection URL used to make database connections.
     * 
     * @param serverURL The JDBC connection URL.
     */
    public void setServerURL(final String serverURL) {
        this.serverURL = serverURL;
    }
    
    /**
     * Returns the username used to connect to the database. In some
     * cases, a username is not needed so this method will return null.
     * 
     * @return The username used to connect to the database.
     */
    public String getUsername() {
        return username;
    }
    
    /**
     * Sets the username used to connect to the database. In some
     * cases, a username is not needed so null should be passed in.
     * 
     * @param username The username used to connect to the database.
     */
    public void setUsername(final String username) {
        this.username = username;
    }
    
    /**
     * Returns the password used to connect to the database. In some cases,
     * a password is not needed so this method will return null.
     * 
     * @return The password used to connect to the database.
     */
    public String getPassword() {
        return password;
    }
    
    /**
     * Sets the password used to connect to the database. In some cases, a
     * password is not needed so null should be passed in.
     * 
     * @param password The password used to connect to the database.
     */
    public void setPassword(final String password) {
        this.password = password;
    }
    
    /**
     * Returns the minimum number of connections that the pool will use. This
     * should probably be at least three.
     * 
     * @return The minimum number of connections in the pool.
     */
    public int getMinConnections() {
        return minConnections;
    }
    
    /**
     * Sets the minimum number of connections that the pool will use. This
     * should probably be at least three.
     * 
     * @param minConnections The minimum number of connections in the pool.
     */
    public void setMinConnections(final int minConnections) {
        this.minConnections = minConnections;
    }
    
    /**
     * Returns the maximum number of connections that the pool will use.
     * The actual number of connections in the pool will vary between this value
     * and the minimum based on the current load.
     * 
     * @return The max possible number of connections in the pool.
     */
    public int getMaxConnections() {
        return maxConnections;
    }
    
    /**
     * Sets the maximum number of connections that the pool will use.
     * The actual number of connections in the pool will vary between this
     * value and the minimum based on the current load.
     * 
     * @param maxConnections The max possible number of connections in the pool.
     */
    public void setMaxConnections(final int maxConnections) {
        this.maxConnections = maxConnections;
    }
    
    /**
     * Returns the amount of time between connection recycles in seconds. For
     * example, a value of 43200 would correspond to recycling the connections
     * in the pool once every half day.
     * 
     * @return The amount of time in seconds between connection recycles.
     */
    public int getConnectionTimeout() {
        return connectionTimeout;
    }
    
    /**
     * Sets the amount of time between connection recycles in days. For
     * example, a value of 43200 would correspond to recycling the connections
     * in the pool once every half day.
     * 
     * @param connectionTimeout The amount of time in seconds between connection recycles.
     */
    public void setConnectionTimeout(final int connectionTimeout) {
        this.connectionTimeout = connectionTimeout;
    }
    
    /**
     * Returns the idle connection test interval in seconds.
     * 
     * @return The idle connection test interval in seconds.
     */
    public int getIdleTestInterval() {
        return idleTestInterval;
    }
    
    /**
     * Sets the idle connection test interval in seconds.
     * 
     * @param idleTestInterval The idle connection test interval in seconds.
     */
    public void setIdleTestInterval(final int idleTestInterval) {
        this.idleTestInterval = idleTestInterval;
    }
    
    /**
     * Returns the SQL statement used to test if the connection is valid.
     * 
     * @return The SQL statement that will be run to test a connection.
     */
    public String getTestSQL() {
        return testSQL;
    }
    
    /**
     * Sets the SQL statement used to test if a connection is valid. House keeping
     * and before/after connection tests make use of this. This should be something
     * that causes the minimal amount of work by the database server and is
     * quick as possible.
     * 
     * @param testSQL The SQL statement that will be run to test a connection.
     */
    public void setTestSQL(final String testSQL) {
        this.testSQL = testSQL;
    }
    
    /**
     * Returns whether returned connections will be tested before being handed
     * over to be used.
     * 
     * @return True if connections are tested before use.
     */
    public Boolean getTestBeforeUse() {
        return testBeforeUse;
    }
    
    /**
     * Sets whether connections will be tested before being handed over to be used.
     * 
     * @param testBeforeUse True of false if connections are to be tested before use.
     */
    public void setTestBeforeUse(final Boolean testBeforeUse) {
        this.testBeforeUse = testBeforeUse;
    }
    
    /**
     * Returns whether returned connections will be tested after being returned
     * to the pool.
     * 
     * @return True if connections are tested after use.
     */
    public Boolean getTestAfterUse() {
        return testAfterUse;
    }
    
    /**
     * Sets whether connections will be tested after being returned to the pool.
     * 
     * @param testAfterUse True of false if connections are to be tested after use.
     */
    public void setTestAfterUse(final Boolean testAfterUse) {
        this.testAfterUse = testAfterUse;
    }
    
    /**
     * Returns the maximum idle time in seconds an excess connection
     * may remain open. A connection is considered "excess" if
     * there are more than the minimum specified connections
     * open concurrently.
     * 
     * @return The maximum idle time in seconds an excess connection
     *              may remain open.
     */
    public int getMaxIdleTimeExcessConnections() {
        return maxIdleTimeExcessConnections;
    }
    
    /**
     * Sets the maximum idle time in seconds an excess connection
     * may remain open. A connection is considered "excess" if there
     * are more than the minimum specified connections open
     * concurrently.
     * 
     * @param maxIdleTimeExcessConnections The maximum idle time in seconds
     *              an excess connection may remain open.
     */
    public void setMaxIdleTimeExcessConnections(final int maxIdleTimeExcessConnections) {
        this.maxIdleTimeExcessConnections = maxIdleTimeExcessConnections;
    }
    
    /**
     * Load properties into memory using setters/getters
     */
    private void loadProperties() {
        setServerURL(
                ConfigurationManager.NARVARO.getString("narvaro.connectionprovider.mysql.serverurl"));
        setUsername(
                ConfigurationManager.NARVARO.getString("narvaro.connectionprovider.mysql.username"));
        setPassword(
                ConfigurationManager.NARVARO.getString("narvaro.connectionprovider.mysql.password"));
        setTestSQL(
                ConfigurationManager.NARVARO.getString("narvaro.connectionprovider.mysql.testSql"));
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

}
