package com.laynezcoder.estfx.models;

import java.io.InputStream;

public class Users {

    private Integer id;
    private String nameUser;
    private String email;
    private String pass;
    private String biography;
    private String dialogTransition;
    private String userType;
    private InputStream profileImage;

    public Users() {
    }

    public Users(String nameUser, String email, String pass, String userType) {
        this.nameUser = nameUser;
        this.email = email;
        this.pass = pass;
        this.userType = userType;
    }

    public Users(Integer id, String nameUser, String email, String pass, String biography, String dialogTransition, String userType) {
        this.id = id;
        this.nameUser = nameUser;
        this.email = email;
        this.pass = pass;
        this.biography = biography;
        this.dialogTransition = dialogTransition;
        this.userType = userType;
    }

    public Users(Integer id, String nameUser, String email, String pass, String userType) {
        this.id = id;
        this.nameUser = nameUser;
        this.email = email;
        this.pass = pass;
        this.userType = userType;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNameUser() {
        return nameUser;
    }

    public void setNameUser(String nameUser) {
        this.nameUser = nameUser;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
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
}
