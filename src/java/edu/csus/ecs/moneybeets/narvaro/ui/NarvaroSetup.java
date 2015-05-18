package edu.csus.ecs.moneybeets.narvaro.ui;

import java.util.TimerTask;
import java.util.zip.DataFormatException;

import org.apache.log4j.Logger;

import edu.csus.ecs.moneybeets.narvaro.database.DatabaseType;
import edu.csus.ecs.moneybeets.narvaro.util.ConfigurationManager;
import edu.csus.ecs.moneybeets.narvaro.util.TaskEngine;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Region;
import javafx.stage.WindowEvent;

/**
 * This class acts as the controller for the 
 * first-time setup menu.
 *
 */
public class NarvaroSetup {
    
    private static final Logger LOG = Logger.getLogger(NarvaroSetup.class.getName());
    
    @FXML private ComboBox<String> databaseTypeSelector;
    @FXML private TextField serverName;
    @FXML private TextField portNumber;
    @FXML private TextField databaseUser;
    @FXML private PasswordField databasePassword;
    
    @FXML
    public void initialize() {
        TaskEngine.INSTANCE.schedule(new TimerTask() {
            @Override
            public void run() {
                serverName.getScene().getWindow().setOnCloseRequest(new EventHandler<WindowEvent>() {
                    @Override
                    public void handle(final WindowEvent we) {
                        Platform.exit();
                        System.exit(0);
                    }
                });
            }
        }, 1000);
        for (DatabaseType dbType : DatabaseType.values()) {
            databaseTypeSelector.getItems().add(dbType.getName());
        }
        installEventHandler(serverName.getParent().getParent().getParent().getParent());
    }
    private void installEventHandler(final Node keyNode) 
    {
        final EventHandler<KeyEvent> keyEventHandler = new EventHandler<KeyEvent>() {
            @Override
            public void handle(final KeyEvent keyEvent) {
                if (keyEvent.getCode() == KeyCode.ENTER) {
                    handleOKButton(null);
                }
            }
        };
        keyNode.setOnKeyReleased(keyEventHandler);
    }


    /**
     * This method is invoked when the OK button is pressed.
     * 
     * @param event The event.
     */
    @FXML
    public void handleOKButton(final ActionEvent event) {
        if (validate()) {
            writeConfig();
            portNumber.getScene().getWindow().hide();
        }
    }
    
    private boolean validate() {
        
        boolean success = true;
        
        try {
            String s = getServerName();    
            if (!"".equals(s) && s != null) {
                showValid(serverName);
            }
            else {
                LOG.error("Server Name not valid: " + s);
                showError(serverName);
                success = false;
            }
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            showError(serverName);
        }
        
        try {
            String s = getDatabasePassword();
            if (!"".equals(s) && s != null) {
                showValid(databasePassword);
            }
            else {
                LOG.error("Database Password not valid: " + s);
                showError(databasePassword);
                success = false;
            }
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            showError(databaseUser);
        }
        
        try {
            String s = getDatabaseUser();
            if (!"".equals(s) && s != null) {
                showValid(databaseUser);
            }
            else {
                LOG.error("Database user not valid: " + s);
                showError(databaseUser);
                success = false;
            }
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            showError(databaseUser);
        }
        
        try {
            int i = getPortNumber();
            if (i > 0) {
                showValid(portNumber);
            } else {
                LOG.error("Port number not valid: " + i);
                showError(portNumber);
                success = false;
            }
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
        }

        return success;
    }

    // helper methods
    private void showValid(final Region r) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                r.setStyle("-fx-control-inner-background:#87D37C;");
            }
        });
    }

    private void showError(final Region r) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                r.setStyle("-fx-control-inner-background:#EF4836;");
            }
        });
    }

    private void resetValid(final Region r) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                r.setStyle("-fx-control-inner-background:#FFFFFF;");
            }
        });
    }
    
    /**
     * Writes configs specified by user in the menu page back into the runtime config file.
     */
    private void writeConfig() {
        
        ConfigurationManager.NARVARO.setProperty("narvaro.connectionprovider.classname", getConnectionProviderClassName());
        ConfigurationManager.NARVARO
            .setProperty("narvaro.connectionprovider.serverurl", getDatabaseConnectionUrl());
        ConfigurationManager.NARVARO.setProperty("narvaro.connectionprovider.username", getDatabaseUser());
        ConfigurationManager.NARVARO.setProperty("narvaro.connectionprovider.password", getDatabasePassword());
        ConfigurationManager.NARVARO.saveProperties();
    }
    
    /**
     * @return The connection provider class name.
     */
    private String getConnectionProviderClassName() {
        DatabaseType dbType = null;
        try {
            dbType = getDatabaseType();
        } catch (Exception e) {
            LOG.error("Unable to determine database type", e);
        }
        if (dbType == DatabaseType.sqlserver) {
            return "edu.csus.ecs.moneybeets.narvaro.database.provider.SQLServerConnectionProvider";
        } else {
            return "edu.csus.ecs.moneybeets.narvaro.database.provider.MySQLConnectionProvider";
        }
    }
    
    /**
     * @return The complete database connection url.
     */
    private String getDatabaseConnectionUrl() {
        StringBuilder url = new StringBuilder("jdbc:");
        DatabaseType dbType = null;
        try {
            dbType = getDatabaseType();
        } catch (Exception e) {
            LOG.error("Unable to determine database type", e);
        }
        if (dbType == DatabaseType.sqlserver) {
            url.append("sqlserver://");
            url.append(getServerName());
            url.append(":");
            url.append(getPortNumber());
            url.append("/narvaro");
        } else {
            url.append("mysql://");
            url.append(getServerName());
            url.append(":");
            url.append(getPortNumber());
            url.append("/narvaro?rewriteBatchedStatements=true");
        }
        return url.toString();
    }

    /**
     * @return the Database type.
     * @throws DataFormatException If selected option is not a valid db type.
     */
    private DatabaseType getDatabaseType() throws DataFormatException {
        return DatabaseType.fromString(
                databaseTypeSelector.getSelectionModel().getSelectedItem().toString().toLowerCase());
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

    private EventHandler<KeyEvent> keyListener = new EventHandler<KeyEvent>() {
        @Override
        public void handle(KeyEvent event) {
            if(event.getCode() == KeyCode.ENTER) {
                handleOKButton(null);
            }
        }
    };
}