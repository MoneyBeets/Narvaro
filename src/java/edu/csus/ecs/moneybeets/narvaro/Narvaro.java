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

import org.apache.log4j.Logger;

import edu.csus.ecs.moneybeets.narvaro.util.ConfigurationManager;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

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
    
    private boolean started = false;
    
    /**
     * Creates an instance of Narvaro and starts it.
     */
    public Narvaro() {
        // only allow a single instance to run
        if (instance != null) {
            //throw new IllegalStateException("Narvaro is already running");
        }
        instance = this;
    }
    
    /**
     * Returns a singleton instance of Narvaro.
     * 
     * @return an instance.
     */
    public static Narvaro getInstance() {
        return instance;
    }
    
    public void startup() {
        // don't allow this method to be called
        //   more than once
        synchronized (this) {
            if (started == true) {
                throw new IllegalStateException("Narvaro is already running");
            }
        }
        try {
            locateNarvaro();
        } catch (Exception e) {
            // failed to locate home directory
            //   terminate app.
            e.printStackTrace();
            LOG.fatal(e.getMessage(), e);
            return;
        }
        // launches JavaFX application
        launch();
        started = true;
    }

    @Override
    public void start(final Stage stage) throws Exception {
        
        this.stage = stage;

        // locate FXML layout
        Path fxml = Paths.get(ConfigurationManager.NARVARO.getHomeDirectory() 
                + File.separator + "resources" + File.separator + "NarvaroUISceneBuilder.fxml");
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
        
        stage.show();
        
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
        
        Platform.exit();
        System.exit(0);
    }

}
