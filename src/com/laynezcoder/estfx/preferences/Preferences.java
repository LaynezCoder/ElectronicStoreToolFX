/*
 * Copyright 2020-2021 LaynezCode
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.laynezcoder.estfx.preferences;

import com.google.gson.Gson;
import com.laynezcoder.estfx.notifications.NotificationType;
import com.laynezcoder.estfx.notifications.NotificationsBuilder;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Preferences {

    private static final String CONFIG_FILE = "config.txt";
    
    private static final String WARNING_MESSAGE = "The configuration file was not found. A new file will be created.";

    private String initialPathFileChooserProductsController;
    
    private String initialPathFileChooserSettingsController;

    public Preferences() {
        initialPathFileChooserProductsController = System.getProperty("user.home");
        initialPathFileChooserSettingsController = System.getProperty("user.home");
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
            Logger.getLogger(Preferences.class.getName()).log(Level.SEVERE, WARNING_MESSAGE, ex);
            NotificationsBuilder.create(NotificationType.INFORMATION, WARNING_MESSAGE);
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
    
    public String getInitialPathFileChooserProductsController() {
        return initialPathFileChooserProductsController;
    }

    public void setInitialPathFileChooserProductsController(String path) {
        this.initialPathFileChooserProductsController = path;
    }

    public String getInitialPathFileChooserSettingsController() {
        return initialPathFileChooserSettingsController;
    }

    public void setInitialPathFileChooserSettingsController(String path) {
        this.initialPathFileChooserSettingsController = path;
    }
}
