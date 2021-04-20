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
package com.laynezcoder.estfx.models;

public class UserSession {

    private static UserSession instance;

    private int id;
    private String name;
    private String username;
    private String password;
    private String biography;
    private String linkProfile;
    private String userType;
    
    private UserSession() {}

    public static UserSession getInstace() {
        if (instance == null) {
            instance = new UserSession();
        }
        return instance;
    } 
     
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getBiography() {
        return biography;
    }

    public void setBiography(String biography) {
        this.biography = biography;
    }

    public String getLinkProfile() {
        return linkProfile;
    }

    public void setLinkProfile(String linkProfile) {
        this.linkProfile = linkProfile;
    }
    
    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public void logout() {
        this.id = 0;
        this.name = "";
        this.username = "";
        this.password = "";
        this.biography = "";
        this.userType = "";
    }
}
