/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.laynezcoder.estfx.constants;

public enum View {
    
    START("StartView"),
    LOGIN("LoginView"),
    MAIN("MainView"),
    HOME("HomeView"),
    CUSTOMERS("CustomersView"),
    QUOTES("QuotesView"),
    PRODUCTS("ProductsView"),
    USERS("UsersView"),
    STATISTICS("StatisticsView"),
    ABOUT("AboutView"),
    SETTINGS("SettingsView");

    private final String name;

    private final String FXML_EXTENSION = ".fxml";

    View(String name) {
        this.name = name;
    }
    
    public String getValue() {
        return name;
    }
    
    public String getFirstNameView() {
        int vIndex = name.length() - 4;
        return name.substring(0, vIndex);
    }
   
    public String getValueWithExtension() {
        return name + FXML_EXTENSION;
    }   
}
