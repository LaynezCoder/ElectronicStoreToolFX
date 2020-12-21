package com.laynezcoder.preferences;

import com.google.gson.Gson;
import com.laynezcoder.resources.Resources;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Preferences {

    public static final String CONFIG_FILE = "config.txt";

    private String initialPathFileChooser;

    public Preferences() {
        initialPathFileChooser = System.getProperty("user.home");
    }

    public String getInitialPathFileChooser() {
        return initialPathFileChooser;
    }

    public void setInitialPathFileChooser(String initialPathFileChooser) {
        this.initialPathFileChooser = initialPathFileChooser;
    }

    public static void initConfig() {
        Writer writer = null;
        try {
            Preferences preferences = new Preferences();
            Gson gson = new Gson();
            writer = new FileWriter(CONFIG_FILE);
            gson.toJson(preferences, writer);
        } catch (IOException ex) {
            Logger.getLogger(Preferences.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                writer.close();
            } catch (IOException ex) {
                Logger.getLogger(Preferences.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    public static Preferences getPreferences() {
        Gson gson = new Gson();
        Preferences preferences = new Preferences();
        try {
            preferences= gson.fromJson(new FileReader(CONFIG_FILE), Preferences.class);
        } catch (FileNotFoundException ex) {
            initConfig();
            Logger.getLogger(Preferences.class.getName()).log(Level.SEVERE, null, ex);
            Resources.notification("Warning", "The configuration file was not found. A new file will be created.", "warning.png");
        }
        return preferences;
    }
    
    public static void writePreferencesToFile(Preferences preferences) {
        Writer writer = null;
        try {
            Gson gson = new Gson();
            writer = new FileWriter(CONFIG_FILE);
            gson.toJson(preferences, writer);
        } catch (IOException ex) {
            Logger.getLogger(Preferences.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                writer.close();
            } catch (IOException ex) {
                Logger.getLogger(Preferences.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
