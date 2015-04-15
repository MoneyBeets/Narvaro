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
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;

import org.apache.log4j.Logger;

import edu.csus.ecs.moneybeets.narvaro.database.DatabaseManager;
import edu.csus.ecs.moneybeets.narvaro.util.ConfigurationManager;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
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
        
        stage.setScene(scene);
        
        // used for graceful shutdown when user clicks the X in top right corner
        stage.setOnCloseRequest(new EventHandler<WindowEvent>() {

            @Override
            public void handle(final WindowEvent we) {
                shutdown();
            }
            
        });
        
        // startup the database manager by requesting a connection
        try {
            Connection con = DatabaseManager.Narvaro.getConnection();
            DatabaseManager.Narvaro.closeConnection(con);
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            return;
        }
        LOG.info("Database Manager Started");
        
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
        String narvaroConfigName = "conf" + File.separator + "narvaro.properties";
        if (narvaroHome == null) {
            String homeProperty = System.getProperty("narvaroHome");
            try {
                if (homeProperty != null && !"".equals(homeProperty)) {
                    narvaroHome = verifyHome(homeProperty, narvaroConfigName);
                }
            } catch (FileNotFoundException e) {
                // ignore
            }
            // if we still haven't found home directory, try checking the local working direcotry
            String wd = System.getProperty("user.dir");
            try {
                if (wd != null && !"".equals(wd)) {
                    narvaroHome = verifyHome(wd, narvaroConfigName);
                }
            } catch (FileNotFoundException e) {
                // ingore
            }
            // if still nothing, try one directory higher
            try {
                String wdParent = Paths.get(wd).getParent().toString();
                if (wdParent != null && !"".equals(wdParent)) {
                    narvaroHome = verifyHome(wdParent, narvaroConfigName);
                }
            } catch (FileNotFoundException e) {
                // if still nothing, give up
                throw new FileNotFoundException("Failed to locate Narvaro home");
            }
        }
        ConfigurationManager.NARVARO.setHomeDirectory(narvaroHome.toString());
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
        Platform.exit();
        System.exit(0);
    }

}
