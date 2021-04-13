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

import java.io.InputStream;

public class Users {

    private int id;
    private String name;
    private String username;
    private String password;
    private String biography;
    private String dialogTransition;
    private String userType;
    private boolean active;
    private String linkProfile;
    private InputStream profileImage;

    public Users() {}

    public Users(String name, String username, String password, String userType) {
        this.name = name;
        this.username = username;
        this.password = password;
        this.userType = userType;
    }

    public Users(int id, String name, String username, String password, String biography, String dialogTransition, String userType) {
        this.id = id;
        this.name = name;
        this.username = username;
        this.password = password;
        this.biography = biography;
        this.dialogTransition = dialogTransition;
        this.userType = userType;
    }

    public Users(int id, String name, String username, String password, String userType) {
        this.id = id;
        this.name = name;
        this.username = username;
        this.password = password;
        this.userType = userType;
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

    public void setName(String nameUser) {
        this.name = nameUser;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String email) {
        this.username = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String pass) {
        this.password = pass;
    }

    public String getBiography() {
        return biography;
    }

    public void setBiography(String biography) {
        this.biography = biography;
    }

    public String getDialogTransition() {
        return dialogTransition;
    }

    public void setDialogTransition(String dialogTransition) {
        this.dialogTransition = dialogTransition;
    }

    public InputStream getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(InputStream imageProfile) {
        this.profileImage = imageProfile;
    }
    
    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String getLinkProfile() {
        return linkProfile;
    }

    public void setLinkProfile(String linkProfile) {
        this.linkProfile = linkProfile;
    }
}
