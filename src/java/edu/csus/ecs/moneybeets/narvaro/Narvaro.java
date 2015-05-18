/**
 * Narvaro: @VERSION@
 * Build Date: @DATE@
 * Commit Head: @HEAD@
 * JDK: @JDK@
 * ANT: @ANT@
 * 
 */

package edu.csus.ecs.moneybeets.narvaro;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;

import org.apache.log4j.Logger;

import edu.csus.ecs.moneybeets.narvaro.database.DatabaseManager;
import edu.csus.ecs.moneybeets.narvaro.model.DataManager;
import edu.csus.ecs.moneybeets.narvaro.util.ConfigurationManager;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

/**
 * The main application which loads, initializes, and starts
 * the GUI and supporting classes.
 *
 */
public class Narvaro extends Application {
    
    private static final Logger LOG = Logger.getLogger(Narvaro.class.getName());
    
    private static Narvaro instance = null;
    
    private Stage stage;
    private Parent root;
    private Scene scene;
    
    private boolean setupMode = false;
    
    // stage for first-time boot setup
    private Stage subStage;
    
    /**
     * Location of the home directory. All config files should be
     * located here.
     */
    private Path narvaroHome;
    
    /**
     * True if the application has started.
     */
    private boolean started = false;
    
    /**
     * Creates an instance of Narvaro and starts it.
     */
    public Narvaro() {
        if (instance != null) {
            throw new IllegalStateException("Narvaro is already initialized");
        }
        instance = this;
    }
    
    /**
     * Launches the JavaFX platform
     *   and invokes the start method.
     */
    public static void startup() {
        Application.launch();
    }

    /**
     * Returns a singleton instance of Narvaro.
     * 
     * @return an instance.
     */
    public static Narvaro getInstance() {
        return instance;
    }

    @Override
    public void start(final Stage stage) throws Exception {

        // only allow startup once
        synchronized (Narvaro.class) {
            if (started == true) {
                throw new IllegalStateException("Narvaro is already running");
            }
        }
        try {
            locateNarvaro();
        } catch (Exception e) {
            // failed to locate home directory
            //   terminate app.
            LOG.fatal(e.getMessage(), e);
            return;
        }
        
        if (setupMode) {
            LOG.info("Setup Mode Activated");
            doFirstTimeSetup();
        }
        
        // startup the database manager by requesting a connection
        try {
            Connection con = DatabaseManager.Narvaro.getConnection();
            DatabaseManager.Narvaro.closeConnection(con);
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            return;
        }
        LOG.info("Database Manager Started");
        
        // startup DataManager
        try {
            DataManager.Narvaro.start();
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
        }
        LOG.info("Data Manager Started");
        
        this.stage = stage;

        // locate FXML layout
        Path fxml = Paths.get(ConfigurationManager.NARVARO.getHomeDirectory() 
                + File.separator + "resources" + File.separator + "Narvaro.fxml");
        root = FXMLLoader.load(fxml.toUri().toURL());
            
        scene = new Scene(root);
        
        // add CSS here to skin Narvaro
        Path css = Paths.get(ConfigurationManager.NARVARO.getHomeDirectory() 
                + File.separator + "resources" + File.separator + "Narvaro.css");
        root.getStylesheets().add(css.toUri().toURL().toExternalForm());
        
        // add icon
        Path icon = Paths.get(ConfigurationManager.NARVARO.getHomeDirectory() 
                + File.separator + "resources" + File.separator + "moneybeets_logo.png");
        stage.getIcons().add(new Image(icon.toUri().toURL().toExternalForm()));
        
        stage.setScene(scene);
        
        // used for graceful shutdown when user clicks the X in top right corner
        stage.setOnCloseRequest(new EventHandler<WindowEvent>() {

            @Override
            public void handle(final WindowEvent we) {
                shutdown();
            }
            
        });
        
        stage.show();
        
        started = true;
        LOG.info("Narvaro Started");
        
    }
    
    /**
     * Locates Narvaro installation.
     * 
     * @throws FileNotFoundException If the home directory could not be discovered.
     */
    private void locateNarvaro() throws FileNotFoundException {
        String narvaroDefaultConfigName = "conf" + File.separator + "default_narvaro.properties";
        String narvaroConfigName = "conf" + File.separator + "narvaro.properties";
        if (narvaroHome == null) {
            String homeProperty = System.getProperty("narvaroHome");
            try {
                if (homeProperty != null && !"".equals(homeProperty)) {
                    narvaroHome = verifyHome(homeProperty, narvaroDefaultConfigName);
                }
            } catch (FileNotFoundException e) {
                // ignore
            }
            // if we still haven't found home directory, try checking the local working direcotry
            String wd = System.getProperty("user.dir");
            try {
                if (wd != null && !"".equals(wd)) {
                    narvaroHome = verifyHome(wd, narvaroDefaultConfigName);
                }
            } catch (FileNotFoundException e) {
                // ingore
            }
            // if still nothing, try one directory higher
            try {
                String wdParent = Paths.get(wd).getParent().toString();
                if (wdParent != null && !"".equals(wdParent)) {
                    narvaroHome = verifyHome(wdParent, narvaroDefaultConfigName);
                }
            } catch (FileNotFoundException e) {
                // if still nothing, give up
                throw new FileNotFoundException("Failed to locate Narvaro home");
            }
        }
        ConfigurationManager.NARVARO.setHomeDirectory(narvaroHome.toString());
        if (isFirstLaunch(narvaroHome.toString() + File.separator + narvaroConfigName)) {
            setupMode = true;
            LOG.info("First launch - Setup Mode Activated");
            try {
                Files.copy(
                        Paths.get(narvaroHome.toString() + File.separator + narvaroDefaultConfigName), 
                        Paths.get(narvaroHome.toString() + File.separator + narvaroConfigName));
            } catch (IOException e) {
                LOG.error("Failed to create runtime configuration file", e);
            }
        }
        ConfigurationManager.NARVARO.setConfigName(narvaroConfigName);
    }
    
    /**
     * Verifies that the home directory guess is valid.
     * 
     * We verify validity by checking if the config file is present in the config directory.
     * 
     * @param homeGuess
     * @param configName
     * @return
     * @throws FileNotFoundException
     */
    private Path verifyHome(final String homeGuess, final String configName) throws FileNotFoundException {
        Path home = Paths.get(homeGuess);
        Path configFile = home.resolve(configName);
        if (!Files.exists(configFile)) {
            throw new FileNotFoundException();
        } else {
            try {
                return home.toAbsolutePath();
            } catch (Exception e) {
                throw new FileNotFoundException();
            }
        }
    }
    
    /**
     * Determines if this program launch is considered
     * a first-time launch. We consider a first-time launch
     * anytime the runtime narvaro.properties config file
     * is not present.
     * 
     * @param config The path + name of the narvaro.properties file.
     * @return True if it is a first-time launch.
     */
    private boolean isFirstLaunch(final String config) {
        Path path = Paths.get(config);
        LOG.debug("Config path is: " + path.toString());
        if (Files.exists(path)) {
            LOG.debug("Config file found");
            return false;
        }
        LOG.debug("Config file not found");
        return true;
    }
    
    /**
     * Starts the Setup menu and waits for the menu to exit.
     */
    private void doFirstTimeSetup() {
        subStage = new Stage();
        Pane subRoot = null;
        try {
            // locate FXML layout
            Path fxml = Paths.get(ConfigurationManager.NARVARO.getHomeDirectory() 
                    + File.separator + "resources" + File.separator + "FirstBoot.fxml");
            subRoot = (Pane)FXMLLoader.load(fxml.toUri().toURL());
        } catch (Exception e) {
            LOG.error("Failed to load first-time boot setup");
        }
        Scene subScene = new Scene(subRoot);
        subStage.setScene(subScene);
        try {
            // add icon
            Path icon = Paths.get(ConfigurationManager.NARVARO.getHomeDirectory() 
                    + File.separator + "resources" + File.separator + "moneybeets_logo.png");
            subStage.getIcons().add(new Image(icon.toUri().toURL().toExternalForm()));
        } catch (Exception e) {
            LOG.warn(e.getMessage(), e);
        }
        subStage.showAndWait();
    }

    /**
     * @return the stage
     */
    public Stage getStage() {
        return stage;
    }

    /**
     * @return the root
     */
    public Parent getRoot() {
        return root;
    }

    /**
     * @return the scene
     */
    public Scene getScene() {
        return scene;
    }
    
    /**
     * Terminates the application
     * 
     * Should handle a graceful shutdown such as
     * closing any open connections, file handles, etc...
     */
    private void shutdown() {
        
        LOG.info("Narvaro Shutting down");
        
        // shutdown the data manager
        LOG.info("Shutting down Data Manager");
        DataManager.Narvaro.shutdown();
        
        // shutdown the database manager
        LOG.info("Shutting down Database Manager");
        DatabaseManager.Narvaro.shutdown();
        
        LOG.info("Narvaro halted");
        
        Platform.exit();
        System.exit(0);
    }

}
