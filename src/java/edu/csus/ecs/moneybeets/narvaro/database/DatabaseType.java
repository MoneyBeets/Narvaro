/**
 * Narvaro: @VERSION@
 * Build Date: @DATE@
 * Commit Head: @HEAD@
 * JDK: @JDK@
 * ANT: @ANT@
 *
 */

package edu.csus.ecs.moneybeets.narvaro.database;

import java.util.zip.DataFormatException;

/**
 * Narvaro supported database types.
 *
 */
public enum DatabaseType {

    sqlserver("SQL Server"),
    
    mysql("MySQL"),
    
    ;
    
    private DatabaseType(final String name) {
    	this.name = name;
    }
    
    final String name;
    
    /**
     * @return The database name.
     */
    public String getName() {
    	return name;
    }
    
    /**
     * Converts a string to the proper matching DatabaseType.
     * 
     * @param in The string.
     * @return The database type if found.
     * @throws DataFormatException If input is null or empty, or not found.
     */
    public static DatabaseType fromString(final String in) throws DataFormatException {
    	if ("".equals(in) || in == null) {
    		throw new DataFormatException("Input not found");
    	}
    	for (DatabaseType dbType : DatabaseType.values()) {
    		if (in.equalsIgnoreCase(dbType.toString()) 
    				|| in.equalsIgnoreCase(dbType.getName())) {
    			return dbType;
    		}
    	}
    	throw new DataFormatException("Input not found");
    }
    
}
