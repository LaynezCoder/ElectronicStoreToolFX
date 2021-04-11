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
package com.laynezcoder.estfx.constants;

public enum Views {
    
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
    SETTINGS("SettingsView"),
    TEST("TestView");

    private final String name;

    private final String FXML_EXTENSION = ".fxml";

    Views(String name) {
        this.name = name;
    }
    
    public String value() {
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
