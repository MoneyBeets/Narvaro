package edu.csus.ecs.moneybeets.narvaro.ui;

import java.util.zip.DataFormatException;

import org.apache.log4j.Logger;

import edu.csus.ecs.moneybeets.narvaro.database.DatabaseType;
import edu.csus.ecs.moneybeets.narvaro.util.ConfigurationManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

/**
 * This class acts as the controller for the 
 * first-time setup menu.
 *
 */
public class NarvaroSetup {
    
    private static final Logger LOG = Logger.getLogger(NarvaroSetup.class.getName());
    
    @FXML private MenuButton databaseTypeSelector;
    @FXML private TextField serverName;
    @FXML private TextField portNumber;
    @FXML private TextField databaseUser;
    @FXML private PasswordField databasePassword;

    /**
     * This method is invoked when the OK button is pressed.
     * 
     * @param event The event.
     */
    @FXML
    public void handleOKButton(final ActionEvent event) {
        if (validate()) {
            writeConfig();
        }
        portNumber.getScene().getWindow().hide();
    }
    
    private boolean validate() {
        return true;
    }
    
    private void writeConfig() {
    	String dbName = "";
    	DatabaseType dbType = null;
    	try {
    		dbType = getDatabaseType();
    	} catch (Exception e) {
    		LOG.error("Unable to determine database type", e);
    	}
    	if (dbType == DatabaseType.sqlserver) {
    		dbName = "/narvaro";
    	} else {
    		dbName = "/narvaro?rewriteBatchedStatements=true";
    	}
        ConfigurationManager.NARVARO
        	.setProperty("narvaro.connectionprovider.serverurl", 
        			getServerName() + ":" + getPortNumber() + dbName);
        ConfigurationManager.NARVARO.setProperty("narvaro.connectionprovider.username", getDatabaseUser());
        ConfigurationManager.NARVARO.setProperty("narvaro.connectionprovider.password", getDatabasePassword());
        ConfigurationManager.NARVARO.saveProperties();
    }

    /**
     * @return the Database type.
     * @throws DataFormatException If selected option is not a valid db type.
     */
    public DatabaseType getDatabaseType() throws DataFormatException {
        for (MenuItem item : databaseTypeSelector.getItems()) {
            if (((CheckMenuItem)item).isSelected()) {
                return DatabaseType.valueOf(item.getText().toLowerCase());
            }
        }
        // this should never happen
        throw new DataFormatException("Invalid database type");
    }

    /**
     * @return the serverName
     */
    private String getServerName() {
        return serverName.getText();
    }

    /**
     * @return the portNumber
     */
    private int getPortNumber() {
        int port = -1;
        try {
            port = Integer.parseInt(portNumber.getText());
        } catch (Exception e) {
            LOG.error("Port number is invalid: " + portNumber.getText());
            LOG.error("Using default port for database type");
        }
        return port;
    }

    /**
     * @return the databaseUser
     */
    private String getDatabaseUser() {
        return databaseUser.getText();
    }

    /**
     * @return the databasePassword
     */
    private String getDatabasePassword() {
        return databasePassword.getText();
    }
    
}
