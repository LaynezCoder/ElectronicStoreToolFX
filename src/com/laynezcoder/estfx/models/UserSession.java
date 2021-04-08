package com.laynezcoder.estfx.models;

public class UserSession {

    private static UserSession instance;

    private int id;
    private String name;
    private String username;
    private String password;
    private String biography;
    private String dialogTransition;
    private boolean isActive;
    private String userType;
    
    private UserSession() {}

    private UserSession(int id, String name, String username, String password, String biography, String dialogTransition, boolean isActive, String userType) {
        this.id = id;
        this.name = name;
        this.username = username;
        this.password = password;
        this.biography = biography;
        this.dialogTransition = dialogTransition;
        this.isActive = isActive;
        this.userType = userType;
    }
   
    public static UserSession getInstace() {
        if (instance == null) {
            instance = new UserSession();
        }
        return instance;
    }
    
    public static UserSession getInstace(int id, String name, String username, String password, String biography, String dialogTransition, boolean isActive, String userType) {
        if (instance == null) {
            instance = new UserSession(id, name, username, password, biography, dialogTransition, isActive, userType);
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

    public String getDialogTransition() {
        return dialogTransition;
    }

    public void setDialogTransition(String dialogTransition) {
        this.dialogTransition = dialogTransition;
    }

    public boolean isIsActive() {
        return isActive;
    }

    public void setIsActive(boolean isActive) {
        this.isActive = isActive;
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
        this.isActive = false;
        this.dialogTransition = "";
    }
}
